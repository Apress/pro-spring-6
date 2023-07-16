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
package com.apress.prospring6.twenty.boot.webclient;

import com.apress.prospring6.twenty.boot.model.Singer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;

/**
 * Created by iuliana on 16/04/2023
 */
@Disabled
public class ReactiveSingerControllerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SingerHandlerTest.class);
    private final WebTestClient controllerClient = WebTestClient
            .bindToServer()
            .baseUrl("http://localhost:8081/reactive/singer")
            .build();

    @Test
    void shouldReturnAllSingers() {
        controllerClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.length()").isEqualTo(30)
                .consumeWith(body -> LOGGER.info("Result: {} ", body));
    }

    @Test
    void shouldReturnJohn(){
        controllerClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("fn", "John").queryParam("ln", "Mayer").build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.firstName").isEqualTo("John")
                .jsonPath("$.lastName").isEqualTo("Mayer")
                .jsonPath("$.birthDate").isEqualTo("1977-10-16");
    }

    @Test
    void shouldReturnAFew(){
        controllerClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("name", "John").build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.length()").isEqualTo(2);
    }

    @Test
    void shouldFailToCreateJohnMayer(){
        controllerClient.post()
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(Singer.builder().firstName("John").lastName("Mayer").birthDate(LocalDate.of(1977, 10, 16)).build())
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .consumeWith(body -> LOGGER.debug("body: {}", body));

    }
}
