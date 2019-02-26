package com.example.cqrs.demo.common.events;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductTaskedEvent {
    private String id;
    private int taskId;
}
