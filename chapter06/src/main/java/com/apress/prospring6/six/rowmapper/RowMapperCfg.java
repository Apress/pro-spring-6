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
package com.apress.prospring6.six.rowmapper;

import com.apress.prospring6.six.config.BasicDataSourceCfg;
import com.apress.prospring6.six.plain.records.Album;
import com.apress.prospring6.six.plain.records.Singer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.apress.prospring6.six.QueryConstants.ALL_JOIN_SELECT;
import static com.apress.prospring6.six.QueryConstants.ALL_SELECT;

/**
 * Created by iuliana.cosmina on 11/05/2022
 */
interface SingerDao {
    Set<Singer> findAll();
    Set<Singer> findAllWithAlbums();
}

class RowMapperDao implements SingerDao {

    private NamedParameterJdbcTemplate namedTemplate;

    public void setNamedTemplate(NamedParameterJdbcTemplate namedTemplate) {
        this.namedTemplate = namedTemplate;
    }

    @Override
    public Set<Singer> findAll() {
        return new HashSet<>(namedTemplate.query(ALL_SELECT, (rs, rowNum) ->
                new Singer(rs.getLong("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("birth_date").toLocalDate(),
                        Set.of())
        ));
    }

    /*static class SingerMapper implements RowMapper<Singer> {

        @Override
        public Singer mapRow(ResultSet rs, int rowNum)
                throws SQLException {
            return new Singer(rs.getLong("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getDate("birth_date").toLocalDate(),
                    Set.of());
        }
    }*/

    /*@Override
    public Set<Singer> findAllWithAlbums() {
        return new HashSet<>(namedTemplate.query(ALL_JOIN_SELECT, new SingerWithAlbumsExtractor()));
    }

    static class SingerWithAlbumsExtractor implements ResultSetExtractor<Set<Singer>> {
        @Override
        public Set<Singer> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, Singer> map = new HashMap<>();
            Singer singer;
            while (rs.next()) {
                Long id = rs.getLong("id");
                singer = map.get(id);
                if (singer == null) {
                    singer = new Singer(id,
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("birth_date").toLocalDate(),
                        Set.of());
                    map.put(id, singer);
                }

                var albumId = rs.getLong("album_id");
                if (albumId > 0) {
                    Album album = new Album(albumId,id,rs.getString("title"),
                    rs.getDate("release_date").toLocalDate()
                    );
                    singer.albums().add(album);
                }
            }
            return new HashSet<>(map.values());
        }
    }*/

    public Set<Singer> findAllWithAlbums() {
        return namedTemplate.query(ALL_JOIN_SELECT, rs -> {
            Map<Long, Singer> map = new HashMap<>();
            Singer singer;
            while (rs.next()) {
                Long id = rs.getLong("id");
                singer = map.get(id);
                if (singer == null) {
                    singer = new Singer(id,rs.getString("first_name"),rs.getString("last_name"),
                            rs.getDate("birth_date").toLocalDate(),
                            new HashSet<>());
                    map.put(id, singer);
                }

                var albumId = rs.getLong("album_id");
                if (albumId > 0) {
                    Album album = new Album(albumId,id,rs.getString("title"),
                            rs.getDate("release_date").toLocalDate());
                    singer.albums().add(album);
                }
            }
            return new HashSet<>(map.values());
        });
    }
}

@Import(BasicDataSourceCfg.class)
@Configuration
public class RowMapperCfg {

    @Autowired
    DataSource dataSource;

    @Bean
    public NamedParameterJdbcTemplate namedTemplate(){
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public SingerDao singerDao(){
        var dao = new RowMapperDao();
        dao.setNamedTemplate(namedTemplate());
        return dao;
    }
}