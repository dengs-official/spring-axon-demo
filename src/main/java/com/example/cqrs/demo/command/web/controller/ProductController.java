package com.example.cqrs.demo.command.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.cqrs.demo.command.commands.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.common.IdentifierFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("product")
@Slf4j
@Api(tags = "Product RESTful APIs")
public class ProductController {

    @Autowired
    private CommandGateway commandGateway;

    @ApiOperation(value = "Create a Product")
    @GetMapping(value = "/create")
    public ResponseEntity<JSONObject> createProduct(@RequestParam String name, @RequestParam Integer stock, @RequestParam Long price) {
        log.info("Start to create product {}", name);

        ResponseEntity<JSONObject> response;
        JSONObject result = new JSONObject();
        try {
            String id = IdentifierFactory.getInstance().generateIdentifier();
            result.put("data", commandGateway.sendAndWait(new CreateProductCommand(id, name, price, stock)));
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.error("When create account occur error", e);
            result.put("message", e);
            response = new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
        }
        return response;
    }

    @ApiOperation(value = "Task Product")
    @GetMapping(value = "/task")
    public ResponseEntity<JSONObject> taskProduct(@RequestParam String id) {
        log.info("Start to task product {}", id);

        ResponseEntity<JSONObject> response;
        JSONObject result = new JSONObject();
        try {
            result.put("data", commandGateway.sendAndWait(new TaskProductCommand(id)));
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.error("When task account occur error", e);
            result.put("message", e);
            response = new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
        }
        return response;
    }
}
