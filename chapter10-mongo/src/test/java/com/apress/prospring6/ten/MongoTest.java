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

import com.apress.prospring6.ten.config.MongoCfg;
import com.apress.prospring6.ten.document.Singer;
import com.apress.prospring6.ten.service.SingerService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@SpringJUnitConfig(classes = {MongoCfg.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MongoTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoTest.class);

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.0-rc7");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("mongo.url", mongoDBContainer::getConnectionString);
        registry.add("mongo.db", () -> "testdb");
    }

    @Autowired
    SingerService singerService;

    @Order(1)
    @Test
    public void testSaveThree() {
        singerService.saveAll(
                List.of(new Singer("John", "Mayer", LocalDate.of(1977, 10, 16)),
                        new Singer("Ben", "Barnes", LocalDate.of(1980, 8, 20)),
                        new Singer("John", "Butler", LocalDate.of(1975, 4, 1))
                ));
    }

    @Order(2)
    @Test
    public void testFindAll(){
        var singers = singerService.findAll().peek(s -> LOGGER.info(s.toString())).toList();
        assertEquals(3, singers.size());
    }

    @Order(3)
    @Test
    public void testFindWithParams(){
        assertNotNull(singerService.findByFullName(true, "John", "Mayer"));

        assertNotNull(singerService.findByFullName(false, "John", "Mayer"));
    }
}
