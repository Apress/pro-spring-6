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
package com.apress.prospring6.seven;

import com.apress.prospring6.seven.base.dao.SingerDao;
import com.apress.prospring6.seven.base.entities.Album;
import com.apress.prospring6.seven.base.entities.Singer;
import com.apress.prospring6.seven.config.HibernateTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Created by iuliana.cosmina on 16/06/2022
 */
@SpringJUnitConfig(classes = {HibernateTestConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class H2HibernateTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateTest.class);

    @Autowired
    SingerDao singerDao;

    @Commit
    @Test
    @Order(1)
    @DisplayName("01. should insert a singer with albums")
    public void testInsert() {
        var singer = new Singer();
        singer.setFirstName("BB");
        singer.setLastName("King");
        singer.setBirthDate(LocalDate.of(1940, 8, 16));

        var album = new Album();
        album.setTitle("My Kind of Blues");
        album.setReleaseDate(LocalDate.of(1961, 7, 18));
        singer.addAlbum(album);

        album = new Album();
        album.setTitle("A Heart Full of Blues");
        album.setReleaseDate(LocalDate.of(1962, 3, 20));
        singer.addAlbum(album);

        var created =  singerDao.save(singer);
        assertNotNull(created.getId());
    }

    @Test
    @Order(2)
    @DisplayName("02. should return all singers")
    public void testFindAll() {
        var singers = singerDao.findAll();
        assertEquals(1, singers.size());
        singers.forEach(singer -> LOGGER.info(singer.toString()));
    }

    @Test
    @Order(3)
    @DisplayName("03. should update a singer")
    public void testUpdate() {
        var singer = singerDao.findAll().get(0);

        //making sure such singer exists
        assertNotNull(singer);
        singer.setFirstName("Riley B. ");
        int version =  singer.getVersion();
        singerDao.save(singer);
        var bb = singerDao.findById(singer.getId());

        assertEquals(version + 1, bb.getVersion());
    }

    @Test
    @Order(4)
    @DisplayName("04. should delete a singer")
    void testDelete() {
        Singer singer = singerDao.findAll().get(0);
        //making sure such singer exists
        assertNotNull(singer);

        singerDao.delete(singer);

        assertEquals(0, singerDao.findAll().size());
    }
}
