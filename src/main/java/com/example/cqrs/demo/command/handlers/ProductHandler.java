package com.example.cqrs.demo.command.handlers;

import com.alibaba.fastjson.JSONObject;
import com.example.cqrs.demo.command.aggregates.ProductAggregate;
import com.example.cqrs.demo.command.commands.TaskProductCommand;
import com.example.cqrs.demo.command.commands.WithcheckStockCommand;
import com.example.cqrs.demo.common.events.ProductTaskedEvent;
import com.example.cqrs.demo.common.events.StockWithcheckedEvent;
import com.example.cqrs.demo.common.xxljob.XxlJobService;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.Aggregate;
import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.axonframework.commandhandling.model.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Component
@Slf4j
public class ProductHandler {

    @Autowired
    Repository<ProductAggregate> repository;

    @Autowired
    private XxlJobService service;

    @CommandHandler
    public void handle(TaskProductCommand command) {
        log.info("Task product {}", command.getId());
        try {
            Aggregate<ProductAggregate> aggregate = repository.load(command.getId());
            aggregate.execute(productAggregate -> {
                // create a task in the xxl-job-admin
                JSONObject create = service.add(genXxlJob(productAggregate.getName(), command.getId()));
                log.info("the create result is: {}", create);
                if (create.getIntValue("code") == 200) {
                    int taskId = create.getInteger("content");
                    JSONObject start = service.start(taskId);
                    log.info("the start result is: {}", start);
                    if (start.getIntValue("code") == 200) {
                        apply(new ProductTaskedEvent(command.getId(), taskId));
                    }
                }
            });
        } catch (AggregateNotFoundException e) {
            log.error("The to be task account not exist");
        }
    }

    @CommandHandler
    public void handle(WithcheckStockCommand command) {
        log.info("Check product {} stock", command.getId());
        XxlJobLogger.log("Check product {} stock", command.getId());
        try {
            Aggregate<ProductAggregate> aggregate = repository.load(command.getId());
            aggregate.execute(productAggregate -> {
                productAggregate.setTimes(productAggregate.getTimes()+1);
                int stock = new Random().nextInt();
                log.info("Withcheck product {} stock is {}", command.getId(), stock);
                XxlJobLogger.log("Withcheck product {} stock is {}", command.getId(), stock);
                apply(new StockWithcheckedEvent(command.getId(), stock));
            });

        } catch (AggregateNotFoundException e) {
            log.error("The to be check product not exist");
        }
    }

    private Map<String, ?> genXxlJob(String name, String id) {
        Map<String, Object> form = new HashMap<>();
        form.put("jobGroup", 2);
        form.put("jobCron", "0 0/1 * * * ?");
        form.put("jobDesc", "Check Product Stock -- " + name);
        form.put("author", "CQRS DEMO SYSTEM");
        form.put("executorRouteStrategy", "FIRST");
        form.put("executorHandler", "WithcheckStockJobHandler");
        form.put("executorParam", id);
        form.put("executorBlockStrategy", "SERIAL_EXECUTION");
        form.put("glueType", "BEAN");
        return form;
    }
}
