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
package com.example.cqrs.demo.command.aggregates;

import com.example.cqrs.demo.command.commands.CreateAccountCommand;
import com.example.cqrs.demo.command.commands.WithdrawMoneyCommand;
import com.example.cqrs.demo.common.domain.AccountId;
import com.example.cqrs.demo.common.events.AccountCreatedEvent;
import com.example.cqrs.demo.common.events.AccountTaskedEvent;
import com.example.cqrs.demo.common.events.MoneyWithdrawnEvent;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

/***************************************************************************
 * <PRE>
 *  Project Name    : cqrs-demo
 *
 *  Package Name    : com.example.cqrs.demo.command.aggregates
 *
 *  File Name       : BankAccount.java
 *
 *  Creation Date   : 2/18/19
 *
 *  Author          : snowdeng
 *
 *  Purpose         : it used as the Bank Account Aggregate
 *
 *
 *  History         : 2019-02-18, snowdeng, add this class
 *
 * </PRE>
 ***************************************************************************/
@Aggregate
@Data
@Slf4j
public class AccountAggregate {

    @AggregateIdentifier
    private AccountId accountId;
    private String name;
    private BigDecimal balance;
    private Integer taskId;

    public AccountAggregate() {
        super();
    }

    @CommandHandler
    public AccountAggregate(CreateAccountCommand command) {
        log.info("Construct a new Account");
        apply(new AccountCreatedEvent(command.getAccountId(), command.getName(), command.getBalance()));
    }

    @EventHandler
    public void on(AccountCreatedEvent event){
        this.accountId = event.getAccountId();
        this.name = event.getName();
        this.balance = new BigDecimal(event.getBalance());
        log.info("Account {} is created with balance {}", accountId, this.balance);

    }

    @EventHandler
    public void on(AccountTaskedEvent event) {
        this.taskId = event.getTaskId();
        log.info("Account {} is tasked with task {}", accountId, this.taskId);
    }

    @EventHandler
    public void on(MoneyWithdrawnEvent event){
        this.balance = this.balance.subtract(new BigDecimal(event.getBalance()));

    }
}
