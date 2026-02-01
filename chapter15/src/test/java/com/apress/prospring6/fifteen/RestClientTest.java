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
package com.apress.prospring6.fifteen;

import com.apress.prospring6.fifteen.entities.Singer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by iuliana on 17/01/2023
 */
@Disabled("Start Apache Tomcat and deploy the app first, then comment this line, then run each test manually and notice the result!")
public class RestClientTest {

    final Logger LOGGER = LoggerFactory.getLogger(RestClientTest.class);
    private static final String URI_SINGER_ROOT = "http://localhost:8080/ch15/singer/";
    private static final String URI_SINGER_WITH_ID = "http://localhost:8080/ch15/singer/{id}";

    RestTemplate restTemplate = new RestTemplate();

    @Test
    public void testFindAll() {
        LOGGER.info("--> Testing retrieve all singers");
        var singers = restTemplate.getForObject(URI_SINGER_ROOT, Singer[].class);
        assertTrue( singers.length > 0);
        Arrays.stream(singers).forEach(s -> LOGGER.info(s.toString()));
    }

    @Test
    public void testFindAllWithExecute() {
        LOGGER.info("--> Testing retrieve all singers");
        var singers = restTemplate.execute(URI_SINGER_ROOT, HttpMethod.GET,
                 request -> LOGGER.debug("Request submitted ..."),
                 response -> {
                    assertEquals(HttpStatus.OK, response.getStatusCode());
                    return new String(response.getBody().readAllBytes());
                 }
        );
        LOGGER.info("Response: {}" , singers);
    }

    @Test
    public void testFindById() {
        LOGGER.info("--> Testing retrieve a singer by id : 1");
        var singer = restTemplate.getForObject(URI_SINGER_WITH_ID, Singer.class, 1);
        assertNotNull(singer);
        LOGGER.info(singer.toString());
    }

    @Test
    public void testCreate() {
        LOGGER.info("--> Testing create singer");
        Singer singerNew = new Singer();
        singerNew.setFirstName("TEST");
        singerNew.setLastName("Singer");
        singerNew.setBirthDate(LocalDate.now());
        singerNew = restTemplate.postForObject(URI_SINGER_ROOT, singerNew, Singer.class);

        LOGGER.info("Singer created successfully: " + singerNew);
    }

    @Test
    public void testCreateWithExchange() {
        LOGGER.info("--> Testing create singer");
        Singer singerNew = new Singer();
        singerNew.setFirstName("TEST2");
        singerNew.setLastName("Singer2");
        singerNew.setBirthDate(LocalDate.now());
        HttpEntity<Singer> request = new HttpEntity<>(singerNew);
        ResponseEntity<Singer> created = restTemplate.exchange(URI_SINGER_ROOT, HttpMethod.POST,request, Singer.class);
        assertEquals(HttpStatus.CREATED, created.getStatusCode());

        var singerCreated = created.getBody();
        assertNotNull(singerCreated);

        LOGGER.info("Singer created successfully: " + singerCreated);
    }


    @Test
    public void testDelete() {
        LOGGER.info("--> Testing delete singer by id : 57"); // TODO check your database and select an ID from there
        var initialCount = restTemplate.getForObject(URI_SINGER_ROOT, Singer[].class).length;
        restTemplate.delete(URI_SINGER_WITH_ID, 57);
        var afterDeleteCount = restTemplate.getForObject(URI_SINGER_ROOT, Singer[].class).length;
        assertEquals((initialCount - afterDeleteCount), 1);
    }

    @Test
    public void testUpdate() {
        LOGGER.info("--> Testing update singer by id : 1");
        var singer = restTemplate.getForObject(URI_SINGER_WITH_ID, Singer.class, 1);
        singer.setFirstName("John");
        restTemplate.put(URI_SINGER_WITH_ID, singer, 1);
        LOGGER.info("Singer update successfully: " + singer);
    }

}
