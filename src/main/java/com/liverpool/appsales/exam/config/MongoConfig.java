package com.liverpool.appsales.exam.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    private final String mongoUri;

    public MongoConfig(
            @Value("${spring.data.mongodb.uri}")
            String mongoUri) {
        this.mongoUri = mongoUri;
    }

    @Override
    protected String getDatabaseName() {
        return new ConnectionString(mongoUri).getDatabase();
    }

    @Override
    @Bean
    public MongoClient mongoClient() {
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(mongoUri))
                .build();

        return MongoClients.create(settings);
    }
}