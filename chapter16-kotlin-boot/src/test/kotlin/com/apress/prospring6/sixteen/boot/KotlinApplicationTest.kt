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
package com.apress.prospring6.sixteen.boot

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.test.context.ActiveProfiles
import java.net.URI
import java.net.URISyntaxException
import java.time.LocalDate
import java.util.*

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class KotlinApplicationTest(@Autowired val restTemplate: TestRestTemplate, @Value(value = "\${local.server.port}") val port: Int) {

    val logger: Logger = LoggerFactory.getLogger(KotlinApplicationTest::class.java)

    companion object {
        @JvmStatic
        @BeforeAll
        fun setup() {
            println(">> Setup")
        }

        @JvmStatic
        @AfterAll
        fun teardown() {
            println(">> Tear down")
        }
    }

    @Test
    fun `find all singers`() {
        val singers: Array<Singer> = restTemplate.getForObject("/singer", Array<Singer>::class.java)
        Assertions.assertTrue(singers.size >= 15)
        for(it in singers) logger.info(it.toString())
    }

    @Test
    @Throws(URISyntaxException::class)
    fun `find Singer by id`() {
        val headers = HttpHeaders()
        headers.accept = listOf(MediaType.APPLICATION_JSON)
        val req = RequestEntity<HttpHeaders>(headers, HttpMethod.GET, URI("http://localhost:$port/singer/1"))
        logger.info("--> Testing retrieve a singer by id : 1")
        val response: ResponseEntity<Singer> = restTemplate.exchange(req, Singer::class.java)
        Assertions.assertAll("testPositiveFindById",
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },
                Executable { Assertions.assertTrue(Objects.requireNonNull(response.headers[HttpHeaders.CONTENT_TYPE]).contains(MediaType.APPLICATION_JSON_VALUE)) },
                Executable { Assertions.assertNotNull(response.body) },
                Executable { Assertions.assertEquals(Singer::class.java, response.body?.javaClass) }
        )
    }

    @Test
    @Throws(URISyntaxException::class)
    fun `can't find Singer by id`() {
        logger.info("--> Testing retrieve a singer by id : 99")

        val req: RequestEntity<Singer> = RequestEntity<Singer>(HttpMethod.GET, URI("http://localhost:$port/singer/99"))
        val response: ResponseEntity<Singer> = restTemplate.exchange(req, Singer::class.java)
        Assertions.assertAll("testNegativeFindById",
                Executable { Assertions.assertEquals(HttpStatus.NOT_FOUND, response.statusCode) },
                Executable { Assertions.assertNull(response.body?.firstName) },
                Executable { Assertions.assertNull(response.body?.lastName) }
        )
    }

    @Test
    @Throws(URISyntaxException::class)
    fun `cannot create singer`() {
        logger.info("--> Testing create singer")
        val singerNew = Singer(null ,0,"Ben", "Barnes", LocalDate.now())
        val req = RequestEntity( singerNew, HttpMethod.POST, URI("http://localhost:$port/singer"))
        val response = restTemplate.exchange(req, String::class.java)
        Assertions.assertAll("testNegativeCreate",
            Executable { Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode) },
            Executable { response.body?.let {
                Assertions.assertTrue(
                        it.contains("Duplicate entry 'Ben-Barnes'")
                    )
                }
            })
    }

}