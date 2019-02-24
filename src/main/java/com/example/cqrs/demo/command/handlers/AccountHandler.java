/***************************************************************************
 *
 *  COPYRIGHT (c) 2004 BY INTEGRATED CONCEPTS INTERNATIONAL LIMITED.
 *
 *  ALL RIGHTS RESERVED. NO PART OF THIS TEXT FILE MAY BE REPRODUCED,
 *  STORED IN A RETRIEVAL SYSTEM OR COMPUTER SYSTEM, OR TRANSMITTED
 *  IN ANY FORM OR BY ANY MEANS, ELECTRONIC, MECHANICAL, PHOTOCOPYING,
 *  RECORDING OR OTHERWISE, WITHOUT PRIOR WRITTEN PERMISSION OF
 *  INTEGRATED CONCEPTS INTERNATIONAL LIMITED.
 *
 *  Integrated Concepts International Limited
 *  11th Broadway
 *  64-52 Lockhart Road
 *  Wanchai, Hong Kong
 *  Telephone       (852) 27788682
 *  Facsimile       (852) 27764515
 *
 ***************************************************************************/
package com.example.cqrs.demo.command.handlers;

import com.alibaba.fastjson.JSONObject;
import com.example.cqrs.demo.command.aggregates.AccountAggregate;
import com.example.cqrs.demo.command.commands.TaskAccountCommand;
import com.example.cqrs.demo.command.commands.WithdrawMoneyCommand;
import com.example.cqrs.demo.common.events.AccountTaskedEvent;
import com.example.cqrs.demo.common.events.MoneyWithdrawnEvent;
import com.example.cqrs.demo.common.xxljob.XxlJobFeignClient;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.Aggregate;
import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.axonframework.commandhandling.model.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

/***************************************************************************
 * <PRE>
 *  Project Name    : cqrs-demo
 *
 *  Package Name    : com.example.cqrs.demo.command.handlers
 *
 *  File Name       : AccountHandler.java
 *
 *  Creation Date   : 2/20/19
 *
 *  Author          : snowdeng
 *
 *  Purpose         : it used to handle the account command
 *
 *
 *  History         : 2019-02-20, snowdeng, add this class
 *
 * </PRE>
 ***************************************************************************/
@Component
@Slf4j
public class AccountHandler {

    @Autowired
    private Repository<AccountAggregate> repository;

    @Autowired
    private XxlJobFeignClient client;

    @CommandHandler
    public void handle(TaskAccountCommand command) {
        log.info("Task account {}", command.getAccountId().toString());
        try {
            Aggregate<AccountAggregate> aggregate = repository.load(command.getAccountId().toString());
            aggregate.execute(accountAggregate -> {
                // create a task in the xxl-job-admin
                JSONObject create = client.add(genXxlJob(accountAggregate.getName(), command.getAccountId().toString(), command.getBalance()));
                log.info("the create result is: {}", create);
                if (create.getIntValue("code") == 200) {
                    Integer taskId = create.getInteger("content");
                    JSONObject start = client.start(taskId);
                    log.info("the start result is: {}", start);
                    if (start.getIntValue("code") == 200) {
                        apply(new AccountTaskedEvent(command.getAccountId(), taskId));
                    }
                }
            });
        } catch (AggregateNotFoundException e) {
            log.error("The to be task account not exist");
        }
    }

    @CommandHandler
    public void handle(WithdrawMoneyCommand command){
        log.info("Draw account {} money with balance {}", command.getAccountId().toString(), command.getBalance());
        XxlJobLogger.log("Draw account {} money with balance {}", command.getAccountId().toString(), command.getBalance());
        try {
            Aggregate<AccountAggregate> aggregate = repository.load(command.getAccountId().toString());
            aggregate.execute(accountAggregate -> {
                BigDecimal result = accountAggregate.getBalance().subtract(new BigDecimal(command.getBalance()));
                if (result.compareTo(BigDecimal.ZERO) > 0) {
                    apply(new MoneyWithdrawnEvent(command.getAccountId(), command.getBalance()));
                } else {
                    log.info("The account balance {} not enough to draw, stop the task");
                    XxlJobLogger.log("The account balance {} not enough to draw, stop the task");
                    JSONObject object = client.stop(accountAggregate.getTaskId());
                    log.info("the object is: {}", object);
                }
            });
        } catch (AggregateNotFoundException e) {
            log.error("The to be task account not exist");
        }

    }

    private Map<String, ?> genXxlJob(String name, String id, long balance) {
        Map<String, Object> form = new HashMap<>();
        form.put("jobGroup", 2);
        form.put("jobCron", "0 0/1 * * * ?");
        form.put("jobDesc", "Draw Account Money -- " + name);
        form.put("author", "CQRS DEMO SYSTEM");
        form.put("executorRouteStrategy", "FIRST");
        form.put("executorHandler", "WithdrawMoneyJobHandler");
        form.put("executorParam", "{\"id\":\"" + id +  "\",\"balance\":" + balance + "}");
        form.put("executorBlockStrategy", "SERIAL_EXECUTION");
        form.put("glueType", "BEAN");
        return form;
    }
    
}
