package com.example.cqrs.demo.command.commands;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WithcheckStockCommand {

    private String id;
}
