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
import lombok.AllArgsConstructor;
import lombok.Data;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

/***************************************************************************
 * <PRE>
 *  Project Name    : cqrs-demo
 *
 *  Package Name    : com.example.cqrs.demo.command.commands
 *
 *  File Name       : TaskAccountCommand.java
 *
 *  Creation Date   : 2/20/19
 *
 *  Author          : snowdeng
 *
 *  Purpose         : it used to set a task for account
 *
 *
 *  History         : 2019-02-20, snowdeng, add this class
 *
 * </PRE>
 ***************************************************************************/
@Data
@AllArgsConstructor
public class TaskAccountCommand {

    @TargetAggregateIdentifier
    private AccountId accountId;
    private long balance;
}
