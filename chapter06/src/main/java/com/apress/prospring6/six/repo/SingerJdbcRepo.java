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
package com.apress.prospring6.six.repo;

import com.apress.prospring6.six.plain.records.Album;
import com.apress.prospring6.six.plain.records.Singer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.apress.prospring6.six.QueryConstants.FIND_SINGER_ALBUM;

/**
 * Created by iuliana.cosmina on 11/05/2022
 */
@Repository("singerRepo")
public class SingerJdbcRepo implements SingerRepo {
    private static final Logger LOGGER = LoggerFactory.getLogger(SingerJdbcRepo.class);
    private DataSource dataSource;
    private SelectAllSingers selectAllSingers;
    private SelectSingerByFirstName selectSingerByFirstName;
    private UpdateSinger updateSinger;
    private InsertSinger insertSinger;
    private InsertSingerAlbum insertSingerAlbum;

    private StoredFunctionFirstNameById storedFunctionFirstNameById;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.selectAllSingers = new SelectAllSingers(dataSource);
        this.selectSingerByFirstName = new SelectSingerByFirstName(dataSource);
        this.updateSinger = new UpdateSinger(dataSource);
        this.insertSinger = new InsertSinger(dataSource);
        this.insertSingerAlbum = new InsertSingerAlbum(dataSource);
        this.storedFunctionFirstNameById = new StoredFunctionFirstNameById(dataSource);
    }
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public List<Singer> findAll() {
        return selectAllSingers.execute();
    }

    @Override
    public List<Singer> findByFirstName(String firstName) {
        return selectSingerByFirstName.executeByNamedParam(Map.of("first_name", firstName));
    }

    @Override
    public Optional<String> findNameById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<String> findLastNameById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<String> findFirstNameById(Long id) {
        var result = storedFunctionFirstNameById.execute(id).get(0);
        return result != null ? Optional.of(result): Optional.empty();
    }

    @Override
    public List<Singer> findAllWithAlbums() {
        var jdbcTemplate = new JdbcTemplate(getDataSource());

        return jdbcTemplate.query(FIND_SINGER_ALBUM, rs -> {
            Map<Long, Singer> map = new HashMap<>();
            Singer singer;
            while (rs.next()) {
                var singerID = rs.getLong("id");
                singer = map.computeIfAbsent(singerID,
                        s-> {
                            try {
                                return new Singer(singerID, rs.getString("first_name"),
                                        rs.getString("last_name"),
                                        rs.getDate("birth_date").toLocalDate(), new HashSet<>());
                            } catch (SQLException sex) {
                                LOGGER.error("Malformed data!", sex);
                            }
                            return null;
                        });

                var albumID = rs.getLong("album_id");
                if (albumID > 0) {
                    Objects.requireNonNull(singer).albums().add(
                            new Album(albumID,singerID,rs.getString("title"),
                                    rs.getDate("release_date").toLocalDate()));
                }
            }
            return new ArrayList<>(map.values());
        });
    }

    @Override
    public void insert(Singer singer) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        insertSinger.updateByNamedParam(Map.of("first_name", singer.firstName(),
                "last_name", singer.lastName(),
                "birth_date", singer.birthDate()), keyHolder);
        var generatedId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        LOGGER.info("New singer  {} {} inserted with id {}  ", singer.firstName(), singer.lastName(), generatedId);
    }

    @Override
    public void update(Singer singer) {
        updateSinger.updateByNamedParam(
                Map.of("first_name", singer.firstName(),
                        "last_name", singer.lastName(),
                        "birth_date", singer.birthDate(),
                        "id", singer.id())
        );
        LOGGER.info("Existing singer updated with id: " + singer.id());
    }

    @Override
    public void delete(Long singerId) {

    }

    @Override
    public void insertWithAlbum(Singer singer) {
        var keyHolder = new GeneratedKeyHolder();
        insertSinger.updateByNamedParam(Map.of("first_name", singer.firstName(),
                "last_name", singer.lastName(),
                "birth_date", singer.birthDate()), keyHolder);
        var newSingerId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        LOGGER.info("New singer  {} {} inserted with id {}  ", singer.firstName(), singer.lastName(), newSingerId);

        var albums = singer.albums();
        if (albums != null) {
            for (Album album : albums) {
                insertSingerAlbum.updateByNamedParam(Map.of("singer_id", newSingerId,
                        "title", album.title(),
                        "release_date", album.releaseDate()));
            }
        }
        insertSingerAlbum.flush();
    }
}
