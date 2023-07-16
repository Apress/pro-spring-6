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

package com.apress.prospring6.ten;
import com.apress.prospring6.ten.config.DataJpaCfg;
import com.apress.prospring6.ten.service.SingerService;
import jakarta.annotation.PostConstruct;
import org.hibernate.cfg.Environment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Created by iuliana.cosmina on 01/08/2022
 */
@Testcontainers
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql({ "classpath:testcontainers/drop-schema.sql", "classpath:testcontainers/create-schema.sql" })
@SpringJUnitConfig(classes = {SingerServiceTest.TestContainersConfig.class})
public class SingerServiceTest extends TestContainersBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(SingerServiceTest.class);

    @Autowired
    SingerService singerService;

    @Test
    public void testFindAll(){
        var singers = singerService.findAll().peek(s -> LOGGER.info(s.toString())).toList();
        assertEquals(3, singers.size());
    }

    @Test
    public void testFindByFirstName(){
        var singers = singerService.findByFirstName("John").peek(s -> LOGGER.info(s.toString())).toList();
        assertEquals(2, singers.size());
    }

    @Test
    public void testFindByFirstNameAndLastName(){
        var singers = singerService.findByFirstNameAndLastName("John", "Mayer")
                .peek(s -> LOGGER.info(s.toString())).toList();
        assertEquals(1, singers.size());
    }

    @Test
    public void testFindByLastName(){
        var singers = singerService.findByLastName("Mayer")
                .peek(s -> LOGGER.info(s.getFullName())).toList();
        assertEquals(1, singers.size());
    }

    @Rollback
    @Test
    @SqlGroup({
            @Sql(scripts = {"classpath:testcontainers/add-nina.sql"},
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
    })
    @DisplayName("should update a singer's name")
    public void testUpdateFirstNameByQuery(){
        var nina = singerService.updateFirstName("Eunice Kathleen", 5L);
        assertAll( "nina was not updated" ,
                () -> assertNotNull(nina),
                () -> assertEquals("Eunice Kathleen", nina.getFirstName())
        );
    }

    @Configuration
    @Import(DataJpaCfg.class)
    public static class TestContainersConfig {
        @Autowired
        Properties jpaProperties;

        @PostConstruct
        public void initialize() {
            jpaProperties.put(Environment.FORMAT_SQL, true);
            jpaProperties.put(Environment.USE_SQL_COMMENTS, true);
            jpaProperties.put(Environment.SHOW_SQL, true);
            jpaProperties.put(Environment.STATEMENT_BATCH_SIZE, 30);
        }
    }
}
