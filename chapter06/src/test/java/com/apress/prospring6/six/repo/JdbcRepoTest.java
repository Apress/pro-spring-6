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
package com.apress.prospring6.six.repo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by iuliana.cosmina on 13/05/2022
 */
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql({ "classpath:h2/drop-schema.sql", "classpath:h2/create-schema.sql" })
@SpringJUnitConfig(classes = {JdbcRepoTest.EmptyEmbeddedJdbcConfig.class, SingerJdbcRepo.class})
public class JdbcRepoTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(RepoBeanTest.class);

    @Autowired
    SingerRepo singerRepo;

    @Test
    @DisplayName("should return all singers")
    @Sql(value = "classpath:h2/test-data.sql",
            config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testFindAllWithMappingSqlQuery(){
        var singers = singerRepo.findAll();
        assertEquals(3, singers.size());
        singers.forEach(singer -> LOGGER.info(singer.toString()));
    }

    @Test
    @DisplayName("should return Chuck Berry")
    /*@Sql(value = "classpath:h2/add-chuck.sql",
            config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)*/

   /* @SqlGroup({
            @Sql(value = "classpath:h2/add-chuck.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:h2/remove-chuck.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })*/
    @SqlGroup({
            @Sql(statements = "insert into SINGER (first_name, last_name, birth_date) values ('Chuck', 'Berry', '1926-09-18')",
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(statements = "delete from  SINGER where first_name = 'Chuck'",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void testFindByNameWithMappingSqlQuery() {
        var singers = singerRepo.findByFirstName("Chuck");
        assertEquals(1, singers.size());
        LOGGER.info("Result: {}", singers.get(0));
    }

    @Configuration
    public static class EmptyEmbeddedJdbcConfig {
        private static final Logger LOGGER = LoggerFactory.getLogger(EmptyEmbeddedJdbcConfig.class);

        @Bean
        public DataSource dataSource() {
            try {
                var dbBuilder = new EmbeddedDatabaseBuilder();
                return dbBuilder.setType(EmbeddedDatabaseType.H2).setName("musicdb").build();
            } catch (Exception e) {
                LOGGER.error("Embedded DataSource bean cannot be created!", e);
                return null;
            }
        }
    }

}
