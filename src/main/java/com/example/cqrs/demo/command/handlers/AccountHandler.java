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

import com.example.cqrs.demo.command.aggregates.AccountAggregate;
import com.example.cqrs.demo.command.commands.TaskAccountCommand;
import com.example.cqrs.demo.common.events.AccountTaskedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.Aggregate;
import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.axonframework.commandhandling.model.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    @CommandHandler
    public void handle(TaskAccountCommand command) {
        log.info("Task account {}", command.getAccountId().toString());
        try {
            Aggregate<AccountAggregate> aggregate = repository.load(command.getAccountId().toString());
            Integer taskId = 183008;
            aggregate.execute(accountAggregate -> {
                apply(new AccountTaskedEvent(command.getAccountId(), taskId));
            });
        } catch (AggregateNotFoundException e) {
            log.error("The to be task account not exist");
        }
    }
    
}
