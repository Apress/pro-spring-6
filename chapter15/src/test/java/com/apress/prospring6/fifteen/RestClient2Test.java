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
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by iuliana on 22/01/2023
 */
@Disabled("Start Apache Tomcat and deploy the app first, then comment this line, then run each test manually and notice the result!")
public class RestClient2Test {
    final Logger LOGGER = LoggerFactory.getLogger(RestClientTest.class);
    private static final String URI_SINGER2_ROOT = "http://localhost:8080/ch15/singer2/";

    RestTemplate restTemplate = new RestTemplate();

    @Test
    public void testPositiveFindById() throws URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        RequestEntity<HttpHeaders>  req = new RequestEntity<>(headers, HttpMethod.GET, new URI(URI_SINGER2_ROOT + 1));
        LOGGER.info("--> Testing retrieve a singer by id : 1");
        ResponseEntity<Singer> response =  restTemplate.exchange(req, Singer.class);
        assertAll("findById",
                ()-> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertTrue(Objects.requireNonNull(response.getHeaders().get(HttpHeaders.CONTENT_TYPE)).contains(MediaType.APPLICATION_JSON_UTF8_VALUE)),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(Singer.class, response.getBody().getClass())
        );
    }

    @Test
    public void testNegativeFindById() throws URISyntaxException {
        LOGGER.info("--> Testing retrieve a singer by id : 99");
        RequestEntity<HttpHeaders>  req = new RequestEntity<>(HttpMethod.GET, new URI(URI_SINGER2_ROOT + 99));

        assertThrowsExactly(HttpClientErrorException.NotFound.class, () -> restTemplate.exchange(req, HttpStatus.class));
    }

    // feel free to write the rest of the tests to test your understanding of the exchange method
}
