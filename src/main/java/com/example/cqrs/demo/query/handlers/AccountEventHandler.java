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
package com.example.cqrs.demo.query.handlers;

import com.example.cqrs.demo.command.aggregates.AccountAggregate;
import com.example.cqrs.demo.common.events.AccountCreatedEvent;
import com.example.cqrs.demo.common.events.AccountTaskedEvent;
import com.example.cqrs.demo.common.events.MoneyWithdrawnEvent;
import com.example.cqrs.demo.query.entries.AccountEntry;
import com.example.cqrs.demo.query.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.model.Aggregate;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/***************************************************************************
 * <PRE>
 *  Project Name    : cqrs-demo
 *
 *  Package Name    : com.example.cqrs.demo.query.handlers
 *
 *  File Name       : AccountHandler.java
 *
 *  Creation Date   : 2/20/19
 *
 *  Author          : snowdeng
 *
 *  Purpose         : it used to handle the account event in query side
 *
 *
 *  History         : 2019-02-20, snowdeng, add this class.
 *
 * </PRE>
 ***************************************************************************/
@Component
@Slf4j
public class AccountEventHandler {

    @Autowired
    private AccountRepository dao;

    @Autowired
    private Repository<AccountAggregate> repository;

    @EventHandler
    public void on(AccountCreatedEvent event) {
        dao.save(new AccountEntry(
                event.getAccountId().toString(),
                event.getName(),
                new BigDecimal(event.getBalance()),
                null));
        log.info("account saved into mysql for query");
    }

    @EventHandler
    public void on(MoneyWithdrawnEvent event) {
        update(event.getAccountId().toString());
        log.info("account balance update into mysql for query");
    }

    @EventHandler
    public void on(AccountTaskedEvent event) {
        update(event.getAccountId().toString());
        log.info("account task id update into mysql for query");
    }

    private void update(String id) {
        Aggregate<AccountAggregate> aggregate = repository.load(id);
        aggregate.execute(accountAggregate -> {
            dao.save(new AccountEntry(
                    accountAggregate.getAccountId().toString(),
                    accountAggregate.getName(),
                    accountAggregate.getBalance(),
                    accountAggregate.getTaskId()));
        });
    }
}
