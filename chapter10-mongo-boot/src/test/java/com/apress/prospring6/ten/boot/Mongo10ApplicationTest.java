package com.apress.prospring6.ten.boot;


import com.apress.prospring6.ten.boot.document.Singer;
import com.apress.prospring6.ten.boot.service.SingerService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Mongo10ApplicationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(Mongo10ApplicationTest.class);

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.0-rc7");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
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
}
