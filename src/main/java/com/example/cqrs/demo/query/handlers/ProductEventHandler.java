package com.example.cqrs.demo.query.handlers;

import com.example.cqrs.demo.command.aggregates.ProductAggregate;
import com.example.cqrs.demo.common.events.ProductCreatedEvent;
import com.example.cqrs.demo.common.events.ProductTaskedEvent;
import com.example.cqrs.demo.common.events.StockWithcheckedEvent;
import com.example.cqrs.demo.query.entries.ProductEntry;
import com.example.cqrs.demo.query.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.model.Aggregate;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductEventHandler {

    @Autowired
    private ProductRepository dao;

    @Autowired
    private Repository<ProductAggregate> repository;

    @EventHandler
    public void on(ProductCreatedEvent event) {
        dao.save(new ProductEntry(
                event.getId(),
                event.getName(),
                event.getStock(),
                event.getPrice(),
                null,
                null
        ));
        log.info("product saved into mysql for query");
    }

    @EventHandler
    public void on(ProductTaskedEvent event) {
        update(event.getId());
        log.info("product task id into mysql for query");
    }

    @EventHandler
    public void on(StockWithcheckedEvent event) {
        update(event.getId());
        log.info("product stock checked times update into mysql for query");
    }

    private void update(String id) {
        Aggregate<ProductAggregate> aggregate = repository.load(id);
        aggregate.execute(productAggregate -> {
            dao.save(new ProductEntry(
                    productAggregate.getId(),
                    productAggregate.getName(),
                    productAggregate.getStock(),
                    productAggregate.getPrice(),
                    productAggregate.getTaskId(),
                    productAggregate.getTimes()));
        });
    }
}
