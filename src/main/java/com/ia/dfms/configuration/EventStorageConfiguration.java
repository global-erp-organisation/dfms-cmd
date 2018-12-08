package com.ia.dfms.configuration;

import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.mongo.DefaultMongoTemplate;
import org.axonframework.mongo.MongoTemplate;
import org.axonframework.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.mongo.eventsourcing.tokenstore.MongoTokenStore;
import org.axonframework.serialization.Serializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClient;

@Configuration
public class EventStorageConfiguration {

    @Value("${axon.events.database}")
    private String dbName;

    @Bean
    public EventStorageEngine engineStorage(MongoTemplate template) {
        return new MongoEventStorageEngine(template);
    }

    @Bean
    EventStore eventStore(EventStorageEngine eventStorageEngine) {
       return new EmbeddedEventStore(eventStorageEngine);
     }

    @Bean
    public MongoTemplate template(MongoClient client) {
        return new DefaultMongoTemplate(client, dbName);
    }

    @Bean
    public TokenStore tokenStore(Serializer serializer, MongoTemplate template) {
        return new MongoTokenStore(template, serializer);
    }
}
