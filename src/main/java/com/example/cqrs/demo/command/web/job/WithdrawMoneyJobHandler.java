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
package com.example.cqrs.demo.command.web.job;

import com.alibaba.fastjson.JSONObject;
import com.example.cqrs.demo.command.commands.WithdrawMoneyCommand;
import com.example.cqrs.demo.common.domain.AccountId;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/***************************************************************************
 * <PRE>
 *  Project Name    : cqrs-demo
 *
 *  Package Name    : com.example.cqrs.demo.command.web.job
 *
 *  File Name       : WithdrawMoneyJobHandler.java
 *
 *  Creation Date   : 2/19/19
 *
 *  Author          : snowdeng
 *
 *  Purpose         : it used to run the bank account business job
 *
 *
 *  History         : 2019-02-19, snowdeng, add this class.
 *
 * </PRE>
 ***************************************************************************/
@JobHandler(value = "WithdrawMoneyJobHandler")
@Component
public class WithdrawMoneyJobHandler extends IJobHandler {

    @Autowired
    public CommandGateway commandGateway;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log("Start to Draw Money");
        JSONObject object = JSONObject.parseObject(s);
        try {
            commandGateway.sendAndWait(new WithdrawMoneyCommand(new AccountId(object.getString("id")), object.getLong("balance")));
        } catch (Exception e) {
            XxlJobLogger.log("Draw account {} money occur exception ", e);
            return FAIL;
        }

        return SUCCESS;
    }
}
