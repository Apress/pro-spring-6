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
package com.apress.prospring6.six.plain.dao.pojos;

import com.apress.prospring6.six.plain.pojos.Singer;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import static com.apress.prospring6.six.QueryConstants.SIMPLE_INSERT;
import static com.apress.prospring6.six.QueryConstants.SIMPLE_DELETE;
import static com.apress.prospring6.six.QueryConstants.ALL_SELECT;

/**
 * Created by iuliana.cosmina on 03/05/2022
 */
public class PlainSingerDao implements SingerDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlainSingerDao.class);

    @Override
    public Set<Singer> findAll() {
        Set<Singer> result = new HashSet<>();
        try (var connection = getConnection();
             var statement = connection.prepareStatement(ALL_SELECT);
             var resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                var singer = new Singer();
                singer.setId(resultSet.getLong("id"));
                singer.setFirstName(resultSet.getString("first_name"));
                singer.setLastName(resultSet.getString("last_name"));
                singer.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
                result.add(singer);
            }
        } catch (SQLException ex) {
            LOGGER.error("Problem when executing SELECT!",ex);
        }
        return result;
    }

    @Override
    public Set<Singer> findByFirstName(String firstName) {
        throw new NotImplementedException("findByFirstName");
    }

    @Override
    public String findNameById(Long id) {
        throw new NotImplementedException("findNameById");
    }

    @Override
    public String findLastNameById(Long id) {
        throw new NotImplementedException("findLastNameById");
    }

    @Override
    public String findFirstNameById(Long id) {
        throw new NotImplementedException("findFirstNameById");
    }

    @Override
    public Singer insert(Singer singer) {
        try ( var connection = getConnection()){
            var statement = connection.prepareStatement(SIMPLE_INSERT, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, singer.getFirstName());
            statement.setString(2, singer.getLastName());
            statement.setDate(3, java.sql.Date.valueOf(singer.getBirthDate()));
            statement.execute();
            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                singer.setId(generatedKeys.getLong(1));
            }
            return singer;
        } catch (SQLException ex) {
            LOGGER.error("Problem executing INSERT", ex);
        }
        return null;
    }

    @Override
    public void update(Singer singer) {
        throw new NotImplementedException("update");
    }

    @Override
    public void delete(Long singerId) {
        try (var connection = getConnection();
             var statement = connection.prepareStatement(SIMPLE_DELETE)) {
            statement.setLong(1, singerId);
            statement.execute();
        } catch (SQLException ex) {
            LOGGER.error("Problem executing DELETE", ex);
        }
    }

    @Override
    public Set<Singer> findAllWithAlbums() {
        throw new NotImplementedException("findAllWithAlbums");
    }

    @Override
    public void insertWithAlbum(Singer singer) {
        throw new NotImplementedException("insertWithAlbum");
    }
}
