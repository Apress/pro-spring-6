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
package com.apress.prospring6.nine.repos;

import com.apress.prospring6.nine.entities.Singer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by iuliana.cosmina on 17/07/2022
 */
@Repository
public class SingerRepoImpl implements SingerRepo {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Stream<Singer> findAll() {
        return em.createNamedQuery(Singer.FIND_ALL, Singer.class).getResultList().stream();
    }

    @Override
    public Optional<Singer> findById(Long id) {
        var found = em.find(Singer.class, id);
        return found == null? Optional.empty() : Optional.of(found);
    }

    @Override
    public Optional<Singer> findByFirstNameAndLastName(String fn, String ln) {
        var result =  em.createNamedQuery(Singer.FIND_BY_FIRST_AND_LAST_NAME, Singer.class)
                .setParameter("fn", fn).setParameter("ln", ln).getSingleResult();
        return result == null ? Optional.empty() : Optional.of(result);
    }

    @Override
    public Long countAllSingers() {
        return em.createNamedQuery(Singer.COUNT_ALL, Long.class).getSingleResult();
    }

    @Override
    public Singer save(Singer singer) {
        if (singer.getId() == null) {
             em.persist(singer);
            return singer;
        } else {
            return em.merge(singer);
        }
    }
}
