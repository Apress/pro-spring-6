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
package com.apress.prospring6.six.boot.repo;

import com.apress.prospring6.six.boot.records.Singer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlFunction;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by iuliana.cosmina on 11/05/2022
 */
@Repository("singerRepo")
public class SingerJdbcRepo implements SingerRepo {
    public static final String ALL_SELECT = "select * from SINGER";
    private JdbcTemplate jdbcTemplate;
    private StoredFunctionFirstNameById storedFunctionFirstNameById;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        storedFunctionFirstNameById = new StoredFunctionFirstNameById(jdbcTemplate.getDataSource());
    }

    @Override
    public Stream<Singer> findAll() {
        return jdbcTemplate.queryForStream(ALL_SELECT, (rs, rowNum) ->
                new Singer(rs.getLong("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("birth_date"),
                        List.of()));
    }

    @Override
    public Optional<String> findFirstNameById(Long id) {
        var result = storedFunctionFirstNameById.execute(id).get(0);
        return result != null ? Optional.of(result): Optional.empty();
    }

    static class StoredFunctionFirstNameById extends SqlFunction<String> {
        private static final String SQL_CALL = "select getfirstnamebyid(?)";
        public StoredFunctionFirstNameById (DataSource dataSource) {
            super(dataSource, SQL_CALL);
            declareParameter(new SqlParameter(Types.INTEGER));
            compile();
        }
    }
}
