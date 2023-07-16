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
package com.apress.prospring6.seven.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by iuliana.cosmina on 16/06/2022
 */
@Configuration
@ComponentScan(basePackages = {"com.apress.prospring6.seven.base"})
@EnableTransactionManagement
public class HibernateTestConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateTestConfig.class);

    @Bean
    public DataSource dataSource() {
        try {
            var dbBuilder = new EmbeddedDatabaseBuilder();
            return dbBuilder.setType(EmbeddedDatabaseType.H2).setName("testdb").build();
        } catch (Exception e) {
            LOGGER.error("Embedded DataSource bean cannot be created!", e);
            return null;
        }
    }

    @Bean
    public Properties hibernateProperties() {
        Properties hibernateProp = new Properties();
        hibernateProp.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");
        hibernateProp.put(Environment.HBM2DDL_AUTO, "create-drop");
        hibernateProp.put(Environment.FORMAT_SQL, true);
        hibernateProp.put(Environment.USE_SQL_COMMENTS, true);
        hibernateProp.put(Environment.HIGHLIGHT_SQL, true);
        hibernateProp.put(Environment.SHOW_SQL, true);
        hibernateProp.put(Environment.MAX_FETCH_DEPTH, 3);
        hibernateProp.put(Environment.STATEMENT_BATCH_SIZE, 10);
        hibernateProp.put(Environment.STATEMENT_FETCH_SIZE, 50);
        return hibernateProp;
    }

    @Bean
    public SessionFactory sessionFactory() {
        return new LocalSessionFactoryBuilder(dataSource())
                .scanPackages("com.apress.prospring6.seven.base.entities")
                .addProperties(hibernateProperties())
                .buildSessionFactory();
    }

    @Bean public PlatformTransactionManager transactionManager() {
        return new HibernateTransactionManager(sessionFactory());
    }

}
