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
import com.apress.prospring6.ten.config.EnversConfig;
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
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by iuliana.cosmina on 06/08/2022
 */
@Testcontainers
@Sql({ "classpath:testcontainers/audit/drop-schema.sql", "classpath:testcontainers/audit/create-schema.sql" })
@SpringJUnitConfig(classes = {EnversConfig.class, AuditCfg.class})
public class EnversServiceTest extends TestContainersBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnversServiceTest.class);

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
    void testFindAuditByRevision() {
        // update to create new version
        var singer = auditService.findAll().findFirst().orElse(null);
        assertNotNull(singer);
        singer.setFirstName("Riley B.");
        auditService.save(singer);

        var oldSinger = auditService.findAuditByRevision(singer.getId(), 1);
        assertEquals("BB", oldSinger.getFirstName());
        LOGGER.info(">> old singer: {} ", oldSinger);

        var newSinger = auditService.findAuditByRevision(singer.getId(), 2);
        assertEquals("Riley B.", newSinger.getFirstName());
        LOGGER.info(">> updated singer: {} ", newSinger);
    }

    @Test
    void testFindAuditAfterDeletion() {
        // delete record
        var singer = auditService.findAll().findFirst().orElse(null);
        auditService.delete(singer.getId());

        // extract from audit
        var deletedSinger = auditService.findAuditByRevision(singer.getId(), 1);
        assertEquals("BB", deletedSinger.getFirstName());
        LOGGER.info(">> deleted singer: {} ", deletedSinger);
    }
}

