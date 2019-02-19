package com.example.cqrs.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CqrsDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CqrsDemoApplication.class, args);
        /*Configuration config = DefaultConfigurer.defaultConfiguration()
                .configureAggregate(BankAccountAggregate.class)
                .configureEmbeddedEventStore(c -> new InMemoryEventStorageEngine())
                .buildConfiguration();
        config.start();
        AccountId id = new AccountId();
        config.commandGateway().send(new CreateAccountCommand(id, "MyAccount",1000));
        config.commandGateway().send(new WithdrawMoneyCommand(id, 500));
        config.commandGateway().send(new WithdrawMoneyCommand(id, 500));*/
    }

}
