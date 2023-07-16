package com.apress.prospring6.ten.boot;

import com.apress.prospring6.ten.boot.service.SingerService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql({ "classpath:testcontainers/drop-schema.sql", "classpath:testcontainers/create-schema.sql" })
@SpringBootTest(classes = {Chapter10Application.class})
public class Chapter10ApplicationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(Chapter10Application.class);

    @Autowired
    SingerService singerService;

    @Test
    public void testFindAll(){
        var singers = singerService.findAll().peek(s -> LOGGER.info(s.toString())).toList();
        assertEquals(3, singers.size());
    }

    @Test
    public void testFindAllWithAlbums(){
        var singers = singerService.findAllWithAlbums()
                .peek(s -> {
                    LOGGER.info(s.toString());
                    if (s.getAlbums() != null) {
                        s.getAlbums().forEach(a -> LOGGER.info("\t" + a.toString()));
                    }
                }).toList();
        assertEquals(3, singers.size());
    }

}
