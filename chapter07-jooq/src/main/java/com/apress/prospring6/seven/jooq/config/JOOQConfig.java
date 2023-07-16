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
package com.apress.prospring6.seven.jooq.config;

import org.jooq.DSLContext;
import org.jooq.conf.RenderNameCase;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by iuliana.cosmina on 19/06/2022
 */
@Configuration
public class JOOQConfig {

    private static Logger LOGGER = LoggerFactory.getLogger(JOOQConfig.class);

    @Autowired
    DataSource dataSource;

    @Bean
    DSLContext dslContext() {
        try {
           return DSL.using(dataSource.getConnection(),
                    new Settings()
                        .withRenderNameCase(RenderNameCase.UPPER)
                        .withRenderQuotedNames(RenderQuotedNames.NEVER)
                        .withRenderSchema(false)
                        .withRenderGroupConcatMaxLenSessionVariable(false)); // needed because of this: https://blog.jooq.org/mysqls-allowmultiqueries-flag-with-jdbc-and-jooq/
        } catch (SQLException ex) {
            LOGGER.error("Problem initializing jOOQ.DSLContext!",ex);
        }
        return null;
    }
}
