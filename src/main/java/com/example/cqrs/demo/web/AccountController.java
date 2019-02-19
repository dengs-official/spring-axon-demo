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
package com.example.cqrs.demo.web;

import com.alibaba.fastjson.JSONObject;
import com.example.cqrs.demo.command.CreateAccountCommand;
import com.example.cqrs.demo.domain.AccountId;
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
 *  Package Name    : com.example.cqrs.demo.web
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
    public ResponseEntity<JSONObject> createAccount(@RequestParam String account, @RequestParam Long amount) {
      log.info("Start to create bank account {}", account);

      AccountId id = new AccountId();
      ResponseEntity<JSONObject> response = new ResponseEntity<>(HttpStatus.OK);
      JSONObject result = new JSONObject();
      try {
          result.put("data", commandGateway.sendAndWait(new CreateAccountCommand(id, account, amount)));
      } catch (Exception e) {
          log.error("When create account occur error", e);
          result.put("message", e);
          response = new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
      } finally {
          if (!response.hasBody()) {
              response = new ResponseEntity<>(result, HttpStatus.OK);
          }
      }
      return response;
    }


}
