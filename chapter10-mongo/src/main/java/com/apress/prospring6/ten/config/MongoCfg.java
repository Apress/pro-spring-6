/*
Freeware License, some rights reserved

Copyright (c) 2023 Iuliana Cosmina

Permission is hereby granted, free of charge, to anyone obtaining a copy
of this software and associated documentation files (the "Software"),
to work with the Software within the limits of freeware distribution and fair use.
This includes the rights to use, copy, and modify the Software for personal use.
Users are also allowed and encouraged to submit corrections and modifications
to the Software for the benefit of other users.

It is not allowed to reuse,  modify, or redistribute the Software for
commercial use in any way, or for a user's educational materials such as books
or blog articles without prior permission from the copyright holder.

The above copyright notice and this permission notice need to be included
in all copies or substantial portions of the software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS OR APRESS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.apress.prospring6.ten.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Collection;
import java.util.Collections;

@ComponentScan(basePackages = {"com.apress.prospring6.ten.service"})
@EnableMongoRepositories(basePackages = {"com.apress.prospring6.ten.repos"})
@Configuration
@PropertySource("classpath:mongo.properties")
@EnableTransactionManagement
public class MongoCfg extends AbstractMongoClientConfiguration {

    @Value("${mongo.url}")
    private String url;

    @Value("${mongo.db}")
    private String database;

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Override
    protected Collection<String> getMappingBasePackages() {
        return Collections.singleton("com.apress.prospring6.ten.document");
    }

    @Bean
    MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

    @Override
    public boolean autoIndexCreation() {
        return true;
    }

    @Override
    protected MongoClientSettings mongoClientSettings() {
        MongoClientSettings.Builder builder = MongoClientSettings.builder();
        builder.applyConnectionString(new ConnectionString(url));
        this.configureClientSettings(builder);
        return builder.build();
    }

}
