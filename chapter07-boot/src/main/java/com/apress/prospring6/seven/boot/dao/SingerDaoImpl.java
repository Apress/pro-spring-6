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
package com.apress.prospring6.seven.boot.dao;

import com.apress.prospring6.seven.boot.entities.Album;
import com.apress.prospring6.seven.boot.entities.Instrument;
import com.apress.prospring6.seven.boot.entities.Singer;
import jakarta.persistence.Tuple;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.Types;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Created by iuliana.cosmina on 22/05/2022
 */
@SuppressWarnings("unchecked")
@Transactional
@Repository("singerDao")
public class SingerDaoImpl implements  SingerDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(SingerDaoImpl.class);

    private final SessionFactory sessionFactory;

    public SingerDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Singer> findAll() {
        return sessionFactory.getCurrentSession().createQuery("from Singer s", Singer.class).list();
    }

    private static final String ALL_SELECT = """
            select distinct s.FIRST_NAME, s.LAST_NAME, a.TITLE, a.RELEASE_DATE, i.INSTRUMENT_ID
            from SINGER s
            inner join ALBUM a on s.id = a.SINGER_ID
            inner join SINGER_INSTRUMENT si on s.ID = si.SINGER_ID
            inner join INSTRUMENT i on si.INSTRUMENT_ID = i.INSTRUMENT_ID
            where s.FIRST_NAME = :firstName and s.LAST_NAME= :lastName
            """;

    @Transactional(readOnly = true)
    @Override
    public Singer findAllDetails(String firstName, String lastName) {
        List<Tuple>  results = sessionFactory.getCurrentSession()
                .createNativeQuery(ALL_SELECT, Tuple.class)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .list();

        var singer = new Singer();

        for (Tuple item : results) {
            if (singer.getFirstName() ==  null && singer.getLastName() == null) {
                singer.setFirstName((String) item.get("FIRST_NAME"));
                singer.setLastName((String) item.get("LAST_NAME"));
            }
            var album = new Album();
            album.setTitle((String) item.get("TITLE"));
            album.setReleaseDate(((Date) item.get("RELEASE_DATE")).toLocalDate());
            singer.addAlbum(album);

            var instrument = new Instrument();
            instrument.setInstrumentId((String) item.get("INSTRUMENT_ID"));
            singer.getInstruments().add(instrument);
        }

        return singer;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Singer> findAllWithAlbum() {
        return sessionFactory.getCurrentSession().createNamedQuery("Singer.findAllWithAlbum", Singer.class).list();  // this uses the NamedQuery
        //return sessionFactory.getCurrentSession().createQuery("from Singer s").list(); // this causes the LazyInitialization exception
    }

    @Transactional(readOnly = true)
    @Override
    public Singer findById(Long id) {
        return sessionFactory.getCurrentSession().createNamedQuery("Singer.findById", Singer.class).setParameter("id", id).uniqueResult();
    }

    @Transactional
    @Override
    public void save(Singer singer) {
        var session = sessionFactory.getCurrentSession();
        if(singer.getId() == null) {
            session.persist(singer);
        } else {
            session.merge(singer);
        }
        LOGGER.info("Singer saved with id: " + singer.getId());
    }

    @Transactional
    @Override
    public void delete(Singer singer) {
        sessionFactory.getCurrentSession().remove(singer);
        LOGGER.info("Singer deleted with id: " + singer.getId());
    }

    @Transactional(readOnly = true)
    @Override
    public Set<String> findAllNamesByProjection() {
        List<Tuple> projResult = sessionFactory.getCurrentSession()
                .createQuery("select s.firstName as fn, s.lastName as ln from Singer s", Tuple.class)
                .getResultList();

        return projResult.stream().map(tuple -> tuple.get("fn", String.class) + " " + tuple.get("ln", String.class))
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    @Override
    public String findFirstNameById(final Long id) {
        final AtomicReference<String> firstNameResult = new AtomicReference<>();
        sessionFactory.getCurrentSession().doWork( connection -> {
            try (CallableStatement function = connection.prepareCall(
                    "{ ? = call getfirstnamebyid(?) }" )) {
                function.registerOutParameter( 1, Types.VARCHAR );
                function.setLong( 2, id );
                function.execute();
                firstNameResult.set( function.getString( 1 ) );
            }
        });
        return firstNameResult.get();
    }
}
