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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by iuliana on 25/03/2023
 */
@Disabled("Enable this test then run `testCreate()` first, then run the `testPositiveFindById()` to generate Prometheus data.")
public class SingerControllerTest {

    private static String BASE_URL = "http://localhost:8081/singer/";

    private RestTemplate restTemplate = new RestTemplate();

    @RepeatedTest(50)
    public void testFindAll() {
        var singers = restTemplate.getForObject(BASE_URL, Singer[].class);
        assertTrue( singers.length >= 15);
    }

    @RepeatedTest(500)
    public void testCreate() throws URISyntaxException {
        Singer singerNew = new Singer();
        singerNew.setFirstName(UUID.randomUUID().toString().substring(0,8));
        singerNew.setLastName(UUID.randomUUID().toString().substring(0,8));
        singerNew.setBirthDate(LocalDate.now());

        RequestEntity<Singer>  req = new RequestEntity<>(singerNew, HttpMethod.POST, new URI(BASE_URL));


        ResponseEntity<String> response =  restTemplate.exchange(req, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }

    @RepeatedTest(10)
    public void testPositiveFindById() throws URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        for (int i = 1; i < 250; i++) {
            RequestEntity<HttpHeaders> req = new RequestEntity<>(headers, HttpMethod.GET, new URI(BASE_URL + i));
            ResponseEntity<Singer> response =  restTemplate.exchange(req, Singer.class);
            assertAll("testPositiveFindById",
                    () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                    () -> assertTrue(Objects.requireNonNull(response.getHeaders().get(HttpHeaders.CONTENT_TYPE)).contains(MediaType.APPLICATION_JSON_VALUE)),
                    () -> assertNotNull(response.getBody()),
                    () -> assertEquals(Singer.class, response.getBody().getClass())
            );
        }
    }


}
