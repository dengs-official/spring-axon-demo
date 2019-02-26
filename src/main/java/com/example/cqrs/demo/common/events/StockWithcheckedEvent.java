package com.example.cqrs.demo.common.events;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockWithcheckedEvent {
    private String id;
    private int stock;
}
