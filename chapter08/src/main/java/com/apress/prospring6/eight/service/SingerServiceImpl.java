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
package com.apress.prospring6.eight.service;

import com.apress.prospring6.eight.entities.Singer;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by iuliana.cosmina on 02/07/2022
 */
@Service("jpaSingerService")
@Repository
@Transactional
public class SingerServiceImpl  implements SingerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SingerServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly=true)
    @Override
    public Stream<Singer> findAllWithAlbum() {
        return em.createNamedQuery(Singer.FIND_ALL_WITH_ALBUM, Singer.class)
                .getResultList().stream();
    }

    @Transactional(readOnly=true)
    @Override
    public Stream<Singer> findAll() {
        return em.createNamedQuery(Singer.FIND_ALL, Singer.class)
                .getResultList().stream();
    }

    @Transactional(readOnly=true)
    @Override
    public Optional<Singer> findById(Long id) {
        TypedQuery<Singer> query = em.createNamedQuery(Singer.FIND_SINGER_BY_ID, Singer.class);
        query.setParameter("id", id);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException nre) {
            return Optional.empty();
        }
    }

    @Override
    public void save(Singer singer) {
        if (singer.getId() == null) {
            LOGGER.info("Inserting new singer");
            em.persist(singer);
        } else {
            em.merge(singer);
            LOGGER.info("Updating existing singer");
        }
    }

    @Override
    public void delete(Singer singer) {
        var mergedContact = em.merge(singer);
        em.remove(mergedContact);

        LOGGER.info("Singer with id: " + singer.getId()  + " deleted successfully");
    }

    // for section "using a simple native query"
   /* @SuppressWarnings({"unchecked"})
    public Stream<Singer> findAllByNativeQuery() {
        return em.createNativeQuery(ALL_SINGER_NATIVE_QUERY, Singer.class).getResultList().stream();
    }
    */
    @SuppressWarnings({"unchecked"})
    @Override
    public Stream<Singer> findAllByNativeQuery() {
        return em.createNativeQuery(ALL_SINGER_NATIVE_QUERY, "singerResult").getResultList().stream();
    }

    @Override
    public String findFirstNameById(Long id) {
       return em.createNamedQuery("Singer.getFirstNameById(?)")
                      .setParameter(1, id).getSingleResult().toString();
    }

    @Override
    public String findFirstNameByIdUsingProc(Long id) {
        StoredProcedureQuery query = em.createNamedStoredProcedureQuery("getFirstNameByIdProc");
        query.setParameter( "in_id", 1L );

        query.execute();
        return (String) query.getOutputParameterValue( "fn_res" );
    }
}
