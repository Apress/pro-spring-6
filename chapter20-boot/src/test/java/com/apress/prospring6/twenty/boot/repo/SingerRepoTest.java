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
package com.apress.prospring6.twenty.boot.repo;

import com.apress.prospring6.twenty.boot.ReactiveDbConfigTests;
import com.apress.prospring6.twenty.boot.model.Singer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.dao.TransientDataAccessResourceException;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Created by iuliana on 13/04/2023
 */
@DataR2dbcTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SingerRepoTest extends ReactiveDbConfigTests {

    @Autowired
    SingerRepo singerRepo;

    @Order(1)
    @Test
    public void testRepoExists() {
        assertNotNull(singerRepo);
    }

    @Order(2)
    @Test
    public void testCount() {
        singerRepo.count()
                .log()
                .as(StepVerifier:: create)
                .expectNextMatches(p -> p == 4)
                .verifyComplete();
    }

    @Order(3)
    @Test
    public void testFindByFistName() {
        singerRepo.findByFirstName("John")
                .log()
                .as(StepVerifier:: create)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Order(4)
    @Test
    public void testFindByFistNameAndLastName() {
        singerRepo.findByFirstNameAndLastName("John", "Mayer")
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
        singerRepo.findByFirstNameAndLastName("Gigi", "Pedala")
                .log()
                .as(StepVerifier:: create)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Order(6)
    @Test
    public void testCreateSinger() {
        singerRepo.save(Singer.builder()
                        .firstName("Test")
                        .lastName("Test")
                        .birthDate(LocalDate.now())
                        .build())
                .log()
                .as(StepVerifier:: create)
                .assertNext(s -> assertNotNull(s.getId()))
                .verifyComplete();
    }

    @Order(7)
    @Test // negative test, lastName is null which is not allowed
    public void testFailedCreateSinger() {
        singerRepo.save(Singer.builder()
                        .firstName("Test")
                        .birthDate(LocalDate.now())
                        .build())
                .log()
                .as(StepVerifier:: create)
                .verifyError(TransientDataAccessResourceException.class);
    }

    @Order(8)
    @Test
    public void testDeleteSinger() {
        singerRepo.deleteById(4L)
                .log()
                .as(StepVerifier:: create)
                .expectNextCount(0)
                .verifyComplete();
    }
}
