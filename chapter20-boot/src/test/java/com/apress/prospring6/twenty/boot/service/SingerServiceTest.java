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
package com.apress.prospring6.twenty.boot.service;

import com.apress.prospring6.twenty.boot.ReactiveDbConfigTests;
import com.apress.prospring6.twenty.boot.model.Singer;
import com.apress.prospring6.twenty.boot.problem.SaveException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Created by iuliana on 16/04/2023
 */
@DataR2dbcTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(SingerServiceImpl.class)
public class SingerServiceTest extends ReactiveDbConfigTests {

    @Autowired SingerService singerService;

    @Order(1)
    @Test
    void serviceExists() {
        assertNotNull(singerService);
    }

    @Order(2)
    @Test
    void testFindAll() {
        singerService.findAll()
                .log()
                .as(StepVerifier:: create)
                .expectNextCount(4)
                .verifyComplete();
    }

    @Order(3)
    @Test
    void testFindById() {
        singerService.findById(1L)
                .log()
                .as(StepVerifier:: create)
                .expectNextMatches(s -> "John".equals(s.getFirstName()) && "Mayer".equals(s.getLastName()))
                .verifyComplete();
    }

    @Order(4)
    @Test
    void testFindByFirstNameAndLastName() {
        singerService.findByFirstNameAndLastName("John", "Mayer")
                .log()
                .as(StepVerifier:: create)
                .expectNext(Singer.builder()
                        .id(1L)
                        .firstName("John")
                        .lastName("Mayer")
                        .birthDate(LocalDate.of(1977, 10, 16))
                        .build())
                .verifyComplete();
    }

    @Order(5)
    @Test
    public void testFindByFistNameAndLastNameNoResult() {
        singerService.findByFirstNameAndLastName("Gigi", "Pedala")
                .log()
                .as(StepVerifier:: create)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Order(6)
    @Test
    public void findByCriteriaDto() {
        var criteria = new SingerService.CriteriaDto();
        criteria.setFieldName(SingerServiceImpl.FieldGroup.BIRTHDATE.name());
        criteria.setFieldValue("1977-10-16");
        singerService.findByCriteriaDto(criteria)
                .log()
                .as(StepVerifier:: create)
                .expectNext(Singer.builder()
                        .id(1L)
                        .firstName("John")
                        .lastName("Mayer")
                        .birthDate(LocalDate.of(1977, 10, 16))
                        .build())
                .verifyComplete();
    }

    @Order(7)
    @Test
    public void testCreateSinger() {
        singerService.save(Singer.builder()
                        .firstName("Test")
                        .lastName("Test")
                        .birthDate(LocalDate.now())
                        .build())
                .log()
                .as(StepVerifier:: create)
                .assertNext(s -> assertNotNull(s.getId()))
                .verifyComplete();
    }

    @Order(8)
    @Test
    public void testNoCreateSinger() {
        singerService.save(Singer.builder()
                        .firstName("John")
                        .lastName("Mayer")
                        .birthDate(LocalDate.now())
                        .build())
                .log()
                .as(StepVerifier:: create)
                .verifyError(SaveException.class);
    }

    @Order(9)
    @Test
    public void testUpdateSinger() {
        singerService.update(4L, Singer.builder()
                        .firstName("Erik Patrick")
                        .lastName("Clapton")
                        .birthDate(LocalDate.now())
                        .build())
                .log()
                .as(StepVerifier:: create)
                .expectNext(Singer.builder()
                        .id(4L)
                        .firstName("Erik Patrick")
                        .lastName("Clapton")
                        .birthDate(LocalDate.now())
                        .build())
                .verifyComplete();
    }

    @Order(10)
    @Test
    public void testUpdateSingerWithDuplicateData() {
        singerService.update(4L, Singer.builder()
                        .firstName("John")
                        .lastName("Mayer")
                        .birthDate(LocalDate.now())
                        .build())
                .log()
                .as(StepVerifier:: create)
                .verifyError(SaveException.class);
    }

    @Order(11)
    @Test // negative test, lastName is null which is not allowed
    public void testFailedCreateSinger() {
        singerService.update(4L, Singer.builder()
                        .firstName("Test")
                        .birthDate(LocalDate.now())
                        .build())
                .log()
                .as(StepVerifier:: create)
                .verifyError(SaveException.class);
    }

    @Order(12)
    @Test
    public void testDeleteSinger() {
        singerService.delete(4L)
                .log()
                .as(StepVerifier:: create)
                .expectNextCount(0)
                .verifyComplete();
    }
}
