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
package com.example.cqrs.demo.command.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.cqrs.demo.command.commands.CreateAccountCommand;
import com.example.cqrs.demo.command.commands.TaskAccountCommand;
import com.example.cqrs.demo.command.commands.WithdrawMoneyCommand;
import com.example.cqrs.demo.common.domain.AccountId;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/***************************************************************************
 * <PRE>
 *  Project Name    : cqrs-demo
 *
 *  Package Name    : com.example.cqrs.demo.command.web
 *
 *  File Name       : BankAccountController.java
 *
 *  Creation Date   : 2/18/19
 *
 *  Author          : snowdeng
 *
 *  Purpose         : it used to as api to accept request
 *
 *
 *  History         : 2019-02-18, snowdeng, add this class
 *
 * </PRE>
 ***************************************************************************/
@RestController
@Slf4j
@Api(tags = "Bank Account RESTful APIs")
public class AccountController {

    @Autowired
    public CommandGateway commandGateway;

    @ApiOperation(value = "Create Bank Account")
    @GetMapping(value = "/create")
    public ResponseEntity<JSONObject> createAccount(@RequestParam String name, @RequestParam Long balance) {
      log.info("Start to create account {}", name);

      ResponseEntity<JSONObject> response;
      JSONObject result = new JSONObject();
      try {
          result.put("data", commandGateway.sendAndWait(new CreateAccountCommand(name, balance)));
          response = new ResponseEntity<>(result, HttpStatus.OK);
      } catch (Exception e) {
          log.error("When create account occur error", e);
          result.put("message", e);
          response = new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
      }
      return response;
    }

    @ApiOperation(value = "Task Bank Account")
    @GetMapping(value = "/task")
    public ResponseEntity<JSONObject> taskAccount(@RequestParam String id, @RequestParam Long balance) {
        log.info("Start to task account {}", id);

        ResponseEntity<JSONObject> response;
        JSONObject result = new JSONObject();
        try {
            result.put("data", commandGateway.sendAndWait(new TaskAccountCommand(new AccountId(id), balance)));
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.error("When task account occur error", e);
            result.put("message", e);
            response = new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
        }
        return response;
    }

    @ApiOperation(value = "Draw Bank Account Balance")
    @GetMapping(value = "/draw")
    public ResponseEntity<JSONObject> drawBalance(@RequestParam String id, @RequestParam Long balance) {
        log.info("Start to draw account balance {}", id);

        ResponseEntity<JSONObject> response;
        JSONObject result = new JSONObject();
        try {
            result.put("data", commandGateway.sendAndWait(new WithdrawMoneyCommand(new AccountId(id), balance)));
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.error("When draw money occur error", e);
            result.put("message", e);
            response = new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
        }
        return response;
    }


}
