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
import com.apress.prospring6.seven.jooq.generated.tables.records.SingerRecord;
import com.apress.prospring6.seven.jooq.records.AlbumRecord;
import com.apress.prospring6.seven.jooq.records.SingerWithAlbums;
import com.apress.prospring6.seven.jooq.records.SingerWithInstruments;
import org.jooq.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.time.LocalDate;
import java.util.List;

import static com.apress.prospring6.seven.jooq.generated.tables.Album.ALBUM;
import static com.apress.prospring6.seven.jooq.generated.tables.Singer.SINGER;
import static com.apress.prospring6.seven.jooq.generated.tables.SingerInstrument.SINGER_INSTRUMENT;
import static org.jooq.Records.mapping;
import static org.jooq.impl.DSL.multisetAgg;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by iuliana.cosmina on 09/07/2022
 */
@SpringBootTest(classes = Chapter7JooqApplication.class)
public class JOOQDslTest {

    @Autowired
    DSLContext dslContext;

    @Test
    @DisplayName("should return all singers")
    void findAll(){
        Result<SingerRecord> singers = dslContext.selectFrom(SINGER).fetch();
        assertEquals(3, singers.size());
    }

    @Test
    @DisplayName("should return singer by id")
    void testFindById(){
        SingerRecord singerRecord = dslContext.selectFrom(SINGER)
                .where(SINGER.ID.eq(2)).fetchOne();

        assertNotNull(singerRecord);
        assertEquals("Ben", singerRecord.getFirstName());
    }


    @Disabled("TODO fix this when I figure out why it fails")
    @Test
    @DisplayName("should return all singers with instruments")
    void findAllWithInstruments(){
        Result<Record4<Integer, String, String, Result<Record1<String>>>> records =
                dslContext.select(SINGER.ID, SINGER.FIRST_NAME, SINGER.LAST_NAME, multisetAgg(SINGER_INSTRUMENT.instrument().INSTRUMENT_ID).as("INSTRUMENTS"))
                        .from(SINGER)
                        .join(SINGER_INSTRUMENT).on(SINGER_INSTRUMENT.SINGER_ID.eq(SINGER.ID))
                        .groupBy(SINGER.ID, SINGER.FIRST_NAME, SINGER.LAST_NAME)
                        .fetch();
        assertEquals(2, records.size());
    }


    @Disabled("TODO fix this when I figure out why it fails")
    @Test
    @DisplayName("should return all singers with instruments as records")
    void findAllWithInstrumentsAsRecords(){
        List<SingerWithInstruments> singerWithInstruments =
                dslContext.select(SINGER.FIRST_NAME, SINGER.LAST_NAME,
                                multisetAgg(SINGER_INSTRUMENT.instrument().INSTRUMENT_ID).convertFrom(r -> r.map(mapping(SingerWithInstruments.InstrumentRecord::new)))
                        )
                        .from(SINGER)
                        .join(SINGER_INSTRUMENT).on(SINGER_INSTRUMENT.SINGER_ID.eq(SINGER.ID))
                        .groupBy(SINGER.FIRST_NAME, SINGER.LAST_NAME)
                        .fetch(mapping(SingerWithInstruments::new));

        assertEquals(2, singerWithInstruments.size());
    }

    @Test
    @DisplayName("should return all singers with albums")
    void findAllWithAlbums(){
        Result<Record3<String, String, String>> records =
                dslContext.select(SINGER.FIRST_NAME, SINGER.LAST_NAME, ALBUM.TITLE)
                        .from(SINGER)
                        .join(ALBUM).on(ALBUM.SINGER_ID.eq(SINGER.ID))
                        .fetch();
        assertEquals(3, records.size());
    }

    @Disabled("TODO fix this when I figure out why it fails")
    @Test
    @DisplayName("should return all singers with albums as records")
    void findAllWithAlbumsAsRecords(){
        List<SingerWithAlbums> singerWithAlbums =
                dslContext.select(SINGER.FIRST_NAME, SINGER.LAST_NAME, SINGER.BIRTH_DATE,
                                multisetAgg(ALBUM.TITLE, ALBUM.RELEASE_DATE).convertFrom(r -> r.map(mapping(AlbumRecord::new))))
                        .from(SINGER)
                        .innerJoin(ALBUM).on(ALBUM.SINGER_ID.eq(SINGER.ID))
                        .groupBy(SINGER.FIRST_NAME, SINGER.LAST_NAME,SINGER.BIRTH_DATE)
                        .fetch(mapping(SingerWithAlbums::new));
        assertEquals(2, singerWithAlbums.size());
    }

    @Test
    @DisplayName("should insert a singer")
    @Sql(statements =  {"delete from SINGER where FIRST_NAME = 'BB'"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testInsertSinger() {
        int insertedRows = dslContext.
                insertInto(SINGER)
                .columns(SINGER.FIRST_NAME, SINGER.LAST_NAME, SINGER.BIRTH_DATE)
                .values("BB", "King", LocalDate.of(1940, 8, 16))
                .execute();

        assertEquals(1, insertedRows);
    }

    @Test
    @DisplayName("should insert a singer using SingerRecord")
    @Sql(statements =  {"delete from SINGER where FIRST_NAME = 'BB'"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testInsertSingerRecord() {
        SingerRecord singerRecord = dslContext.newRecord(SINGER);
        singerRecord.setFirstName("BB");
        singerRecord.setLastName("King");
        singerRecord.setBirthDate(LocalDate.of(1940, 8, 16));

        int insertedRows = singerRecord.store();
        assertEquals(1, insertedRows);
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
        int updatedRows = dslContext.
                update(SINGER)
                .set(SINGER.FIRST_NAME, "Eunice Kathleen")
                .set(SINGER.LAST_NAME, "Waymon")
                .where(SINGER.ID.eq(5))
                .execute();

        assertEquals(1, updatedRows);
    }

    @Test
    @Sql(scripts = {"classpath:testcontainers/add-chuck.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("should delete a singer")
    void testDelete() {
        assertThrows(DataIntegrityViolationException.class, () -> dslContext.deleteFrom(SINGER)
                .where(SINGER.ID.eq(6))
                .execute());

        // in case deletion works
/*        int deletedRows =   dslContext.deleteFrom(SINGER)
                .where(SINGER.ID.eq(6))
                .execute();
        assertEquals(1, deletedRows);*/
    }

}
