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
package com.example.cqrs.demo.command.commands;

import com.example.cqrs.demo.common.domain.AccountId;
import lombok.Data;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

/***************************************************************************
 * <PRE>
 *  Project Name    : cqrs-demo
 *
 *  Package Name    : com.example.cqrs.demo.command
 *
 *  File Name       : CreateAccountCommand.java
 *
 *  Creation Date   : 2/18/19
 *
 *  Author          : snowdeng
 *
 *  Purpose         : it used as the operate create account
 *
 *
 *  History         : 2019-02-18, snowdeng, add this class
 *
 * </PRE>
 ***************************************************************************/
@Data
public class CreateAccountCommand {

    @TargetAggregateIdentifier
    private AccountId accountId;
    private String name;
    private long balance;

    public CreateAccountCommand(String name, long balance) {
        this.accountId = new AccountId();
        this.name = name;
        this.balance = balance;
    }
}
