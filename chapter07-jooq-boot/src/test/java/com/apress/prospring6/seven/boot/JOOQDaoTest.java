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
package com.apress.prospring6.seven.boot;

import com.apress.prospring6.seven.Chapter7JooqApplication;
import com.apress.prospring6.seven.jooq.generated.tables.daos.SingerDao;
import org.jooq.DSLContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Created by iuliana.cosmina on 09/07/2022
 */
@SpringBootTest(classes = Chapter7JooqApplication.class)
public class JOOQDaoTest {

    @Autowired
    DSLContext dslContext;

    @Test
    @DisplayName("should return all singers")
    void findAll(){
        var dao = new SingerDao(dslContext.configuration());

        var singers = dao.findAll();
        assertEquals(3, singers.size());
    }

    @Test
    @DisplayName("should return singer by id")
    void testFindById(){
        var dao = new SingerDao(dslContext.configuration());

        var singer = dao.findById(2);

        assertNotNull(singer);
        assertEquals("Ben", singer.getFirstName());
    }

    @Test
    @SqlGroup({
            @Sql(scripts = {"classpath:testcontainers/add-nina.sql"},
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = {"classpath:testcontainers/remove-nina.sql"},
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @DisplayName("should update a singer")
    void testUpdate() {
        var dao = new SingerDao(dslContext.configuration());
        var nina = dao.findById(5);
        assertNotNull(nina);

        nina.setFirstName("Eunice Kathleen");
        nina.setLastName("Waymon");
        dao.update(nina);

        var updatedNina = dao.findById(5);

        assertNotNull(updatedNina);
        assertEquals("Eunice Kathleen", updatedNina.getFirstName());
        assertEquals("Waymon", updatedNina.getLastName());
    }
}
