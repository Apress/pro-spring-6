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

import com.apress.prospring6.seven.base.config.HibernateConfig;
import com.apress.prospring6.seven.base.dao.SingerDao;
import com.apress.prospring6.seven.base.entities.Album;
import com.apress.prospring6.seven.base.entities.Instrument;
import com.apress.prospring6.seven.base.entities.Singer;
import jakarta.annotation.PostConstruct;
import org.hibernate.cfg.Environment;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Created by iuliana.cosmina on 22/05/2022
 */
@Testcontainers
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql({ "classpath:testcontainers/drop-schema.sql", "classpath:testcontainers/create-schema.sql" })
@SpringJUnitConfig(classes = {HibernateTest.TestContainersConfig.class})
public class HibernateTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateTest.class);

    @Container
    static MariaDBContainer<?> mariaDB = new MariaDBContainer<>("mariadb:11.1.2");

    @DynamicPropertySource // this does the magic
    static void setUp(DynamicPropertyRegistry registry) {
        registry.add("jdbc.driverClassName", mariaDB::getDriverClassName);
        registry.add("jdbc.url", mariaDB::getJdbcUrl);
        registry.add("jdbc.username", mariaDB::getUsername);
        registry.add("jdbc.password", mariaDB::getPassword);
    }

    @Autowired
    SingerDao singerDao;

    @Test
    @DisplayName("should return all singers")
    void testFindAll(){
        var singers = singerDao.findAll();
        assertEquals(3, singers.size());
        singers.forEach(singer -> LOGGER.info(singer.toString()));
    }

    @Test
    @DisplayName("should return singer by id")
    void testFindById(){
        var singer = singerDao.findById(2L);
        assertEquals("Ben", singer.getFirstName());
        LOGGER.info(singer.toString());
    }

    @Test
    @DisplayName("should insert a singer with associations")
    @Sql(statements =  { // avoid dirtying up the test context
            "delete from ALBUM where SINGER_ID = (select ID from SINGER where FIRST_NAME = 'BB')",
            "delete from SINGER_INSTRUMENT where SINGER_ID = (select ID from SINGER where FIRST_NAME = 'BB')",
            "delete from SINGER where FIRST_NAME = 'BB'"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testInsertSinger(){
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
        singerDao.save(singer);

        assertNotNull(singer.getId());

        var singers = singerDao.findAllWithAlbum();
        assertEquals(4, singers.size());
        listSingersWithAssociations(singers);
    }

    @Configuration
    @Import(HibernateConfig.class)
    public static class TestContainersConfig {
        @Autowired
        Properties hibernateProperties;

        @PostConstruct
        public void initialize() {
            hibernateProperties.put(Environment.FORMAT_SQL, true);
            hibernateProperties.put(Environment.USE_SQL_COMMENTS, true);
            hibernateProperties.put(Environment.SHOW_SQL, true);
        }
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
        var singer = singerDao.findById(5L);
        //making sure such singer exists
        assertNotNull(singer);
        //making sure we got expected singer
        assertEquals("Simone", singer.getLastName());
        //retrieve the album
        var album = singer.getAlbums().stream().filter(
                a -> a.getTitle().equals("I Put a Spell on You")).findFirst().orElse(null);
        assertNotNull(album);

        singer.setFirstName("Eunice Kathleen");
        singer.setLastName("Waymon");
        singer.removeAlbum(album);
        int version =  singer.getVersion();
        singerDao.save(singer);

        var nina = singerDao.findById(5L);
        assertEquals(version +1, nina.getVersion());

        // test the update
        listSingersWithAssociations(singerDao.findAllWithAlbum());
    }

    @Test
    @Sql(scripts = {"classpath:testcontainers/add-chuck.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("should delete a singer")
    void testDelete() {
        Singer singer = singerDao.findById(6L);
        //making sure such singer exists
        assertNotNull(singer);

        singerDao.delete(singer);
        listSingersWithAssociations(singerDao.findAllWithAlbum());
    }

    @Test
    void testNativeQuery() {
        var singer = singerDao.findAllDetails("Ben", "Barnes");
        assertAll("Native Singer Test",
                () -> assertEquals("Ben" , singer.getFirstName()),
                () -> assertEquals("Barnes" , singer.getLastName()),
                () -> assertEquals(1 , singer.getAlbums().size()),
                () -> assertEquals(3 , singer.getInstruments().size())
        );
    }

    @Test
    void testFindAllNamesByProjection() {
        var singers =  singerDao.findAllNamesByProjection();
        assertEquals(3, singers.size());
        singers.forEach(LOGGER::info);
    }

    @Sql({ "classpath:testcontainers/stored-function.sql" })
    @Test
    void testFindFirstNameById () {
        var res = singerDao.findFirstNameById(1L);
        assertEquals("John", res);
    }

    @Disabled("no equivalent SQL syntax supported by Testcontainers")
    @Sql({ "classpath:testcontainers/stored-procedure.sql" })
    @Test
    void testFindFirstNameByIdUsingProc () {
        var res = singerDao.findFirstNameByIdUsingProc(1L);
        assertEquals("John", res);
    }

    private static void listSingersWithAssociations(List<Singer> singers) {
        LOGGER.info(" ---- Listing singers with instruments:");
        for (Singer singer : singers) {
            LOGGER.info(singer.toString());
            if (singer.getAlbums() != null) {
                for (Album album :
                        singer.getAlbums()) {
                    LOGGER.info("\t" + album.toString());
                }
            }
            if (singer.getInstruments() != null) {
                for (Instrument instrument : singer.getInstruments()) {
                    LOGGER.info("\tInstrument: " + instrument.getInstrumentId());
                }
            }
        }
    }
}
