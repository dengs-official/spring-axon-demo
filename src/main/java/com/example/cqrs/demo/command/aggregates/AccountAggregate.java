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
        log.debug("Construct a new Account");
        apply(new AccountCreatedEvent(command.getAccountId(), command.getName(), command.getBalance()));
    }

    @CommandHandler
    public void handle(WithdrawMoneyCommand command){
        apply(new MoneyWithdrawnEvent(command.getAccountId(), command.getBalance()));
    }

    @EventHandler
    public void on(AccountCreatedEvent event){
        log.info("After Create Event, Begin to update the aggregate");
        this.accountId = event.getAccountId();
        this.name = event.getName();
        this.balance = new BigDecimal(event.getBalance());
        log.info("Account {} is created with balance {}", accountId, this.balance);

    }

    @EventHandler
    public void on(AccountTaskedEvent event) {
        log.info("After Task Event, Begin to update the aggregate");
        this.taskId = event.getTaskId();
        log.info("Account {} is tasked with task {}", accountId, this.taskId);
    }

    @EventHandler
    public void on(MoneyWithdrawnEvent event){
        BigDecimal result = this.balance.subtract(new BigDecimal(event.getBalance()));
        if(result.compareTo(BigDecimal.ZERO)<0)
            log.error("Cannot withdraw more money than the balance!");
        else {
            this.balance = result;
            log.info("Withdraw {} from account {}, balance result: {}", event.getBalance(), accountId, balance);
        }
    }
}
