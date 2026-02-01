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

import com.apress.prospring6.ten.config.AuditCfg;
import com.apress.prospring6.ten.config.DataJpaCfg;
import com.apress.prospring6.ten.entities.SingerAudit;
import com.apress.prospring6.ten.service.SingerAuditService;
import jakarta.annotation.PostConstruct;
import org.hibernate.cfg.Environment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by iuliana.cosmina on 06/08/2022
 */
@Testcontainers
@Sql({ "classpath:testcontainers/audit/drop-schema.sql", "classpath:testcontainers/audit/create-schema.sql" })
@SpringJUnitConfig(classes = {AuditServiceTest.TestContainersConfig.class})
public class AuditServiceTest extends TestContainersBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditServiceTest.class);

    @Autowired
    SingerAuditService auditService;

    @BeforeEach
    void setUp(){
        var singer = new SingerAudit();
        singer.setFirstName("BB");
        singer.setLastName("King");
        singer.setBirthDate(LocalDate.of(1940, 8, 16));
        auditService.save(singer);
    }

    @Test
    void testFindById() {
        var singer = auditService.findAll().findFirst().orElse(null);

        assertAll( "auditFindByIdTest" ,
                () -> assertNotNull(singer),
                () -> assertTrue(singer.getCreatedBy().isPresent()),
                () -> assertTrue(singer.getLastModifiedBy().isPresent()),
                () -> assertNotNull(singer.getCreatedDate()),
                () -> assertNotNull(singer.getLastModifiedDate())
        );
        LOGGER.info(">> created record: {} ", singer);
    }

    @Test
    void testUpdate() {
        var singer = auditService.findAll().findFirst().orElse(null);
        assertNotNull(singer);
        singer.setFirstName("Riley B.");
        var updated = auditService.save(singer);

        assertAll( "auditUpdateTest" ,
                () -> assertEquals("Riley B.", updated.getFirstName()),
                () -> assertTrue(updated.getLastModifiedBy().isPresent()),
                () -> assertNotEquals(updated.getCreatedBy().orElse(null), updated.getLastModifiedBy().orElse(null))
        );
        LOGGER.info(">> updated record: {} ", updated);
    }

    @Configuration
    @Import({DataJpaCfg.class, AuditCfg.class})
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
