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
package com.apress.prospring6.eighteen.boot;

import com.apress.prospring6.eighteen.boot.entities.Singer;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by iuliana on 22/03/2023
 */
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Chapter18ApplicationTest {

    final Logger LOGGER = LoggerFactory.getLogger(Chapter18ApplicationTest.class);


    @Value(value="${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testFindAll() {
        LOGGER.info("--> Testing retrieve all singers");
        var singers = restTemplate.getForObject("http://localhost:"+port+"/singer/", Singer[].class);
        assertTrue( singers.length >= 15);
        Arrays.stream(singers).forEach(s -> LOGGER.info(s.toString()));
    }

    @Test
    public void testPositiveFindById() throws URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        RequestEntity<HttpHeaders> req = new RequestEntity<>(headers, HttpMethod.GET, new URI("http://localhost:"+port+"/singer/1"));
        LOGGER.info("--> Testing retrieve a singer by id : 1");
        ResponseEntity<Singer> response =  restTemplate.exchange(req, Singer.class);
        assertAll("testPositiveFindById",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertTrue(Objects.requireNonNull(response.getHeaders().get(HttpHeaders.CONTENT_TYPE)).contains(MediaType.APPLICATION_JSON_VALUE)),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(Singer.class, response.getBody().getClass())
        );
    }


    @Test
    public void testNegativeFindById() throws URISyntaxException {
        LOGGER.info("--> Testing retrieve a singer by id : 99");
        RequestEntity<Singer>  req = new RequestEntity<>(HttpMethod.GET, new URI("http://localhost:"+port+"/singer/99"));

        ResponseEntity<Singer> response = restTemplate.exchange(req, Singer.class);
        assertAll("testNegativeFindById",
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertNull(response.getBody().getFirstName()),
                () -> assertNull(response.getBody().getLastName())
        );
    }

    @Test
    public void testNegativeCreate() throws URISyntaxException {
        LOGGER.info("--> Testing create singer");
        Singer singerNew = new Singer();
        singerNew.setFirstName("Ben");
        singerNew.setLastName("Barnes");
        singerNew.setBirthDate(LocalDate.now());

        RequestEntity<Singer>  req = new RequestEntity<>(singerNew, HttpMethod.POST, new URI("http://localhost:"+port+"/singer/"));

        ResponseEntity<String> response =  restTemplate.exchange(req, String.class);
        assertAll("testNegativeCreate",
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                ()-> assertTrue(response.getBody().contains("Duplicate entry 'Ben-Barnes'")));
    }

}
