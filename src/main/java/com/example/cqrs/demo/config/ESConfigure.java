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
package com.example.cqrs.demo.config;

import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/***************************************************************************
 * <PRE>
 *  Project Name    : cqrs-demo
 *
 *  Package Name    : com.example.cqrs.demo.config
 *
 *  File Name       : ESConfiguration.java
 *
 *  Creation Date   : 2/18/19
 *
 *  Author          : snowdeng
 *
 *  Purpose         : it used to configure the event sourcing
 *
 *
 *  History         : 2019-02-18, snowdeng, add this class
 *
 * </PRE>
 ***************************************************************************/
@Configuration
public class ESConfigure {

   /* @Value("${mongodb.url}")
    private String mongoUrl;

    @Value("${mongodb.dbname}")
    private String mongoDbName;

    @Value("${mongodb.events.collection.name}")
    private String eventsCollectionName;

    @Value("${mongodb.events.snapshot.collection.name}")
    private String snapshotCollectionName;*/

    @Bean
    public Serializer axonJsonSerializer() {
        return new JacksonSerializer();
    }

    @Bean
    public EventStorageEngine eventStorageEngine(){
        return new InMemoryEventStorageEngine();
//        return new MongoEventStorageEngine(axonJsonSerializer(),null, axonMongoTemplate(), new DocumentPerEventStorageStrategy());
    }

    /*@Bean(name = "axonMongoTemplate")
    public MongoTemplate axonMongoTemplate() {
        return new DefaultMongoTemplate(mongoClient(), mongoDbName, eventsCollectionName, snapshotCollectionName);
    }

    @Bean
    public MongoClient mongoClient(){
        MongoFactory mongoFactory = new MongoFactory();
        mongoFactory.setMongoAddresses(Arrays.asList(new ServerAddress(mongoUrl)));
        return mongoFactory.createMongo();
    }*/

    /*@Bean
    public SagaStore sagaStore(){
        org.axonframework.mongo.eventhandling.saga.repository.MongoTemplate mongoTemplate = new org.axonframework.mongo.eventhandling.saga.repository.DefaultMongoTemplate(mongoClient());
        return new MongoSagaStore(mongoTemplate, axonJsonSerializer());
    }*/

}
