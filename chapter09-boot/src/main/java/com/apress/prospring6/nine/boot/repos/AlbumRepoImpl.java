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
package com.apress.prospring6.nine.boot.repos;

import com.apress.prospring6.nine.boot.entities.Album;
import com.apress.prospring6.nine.boot.entities.Singer;
import com.apress.prospring6.nine.boot.ex.TitleTooLongException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Created by iuliana.cosmina on 17/07/2022
 */
@Repository
public class AlbumRepoImpl implements AlbumRepo{

    @PersistenceContext
    private EntityManager em;

    @Value("#{jpaProperties.get('hibernate.jdbc.batch_size')}")
    private int batchSize;
    @Override
    public Stream<Album> findBySinger(Singer singer) {
        return  em.createNamedQuery(Album.FIND_ALL, Album.class)
                .setParameter("singer", singer)
                .getResultList().stream();
    }

    @Override
    public Set<Album> save(Set<Album> albums) throws TitleTooLongException {
        final Set<Album> savedAlbums = new HashSet<>();
        int i = 0;
        for (Album a : albums) {
            savedAlbums.add(save(a));
            i++;
            if (i % batchSize == 0) {
                // Flush a batch of inserts and release memory.
                em.flush();
                em.clear();
            }
        }
        return savedAlbums;
    }

    @Override
    public Album save(Album album) throws TitleTooLongException {
        if (50 < album.getTitle().length()) {
            throw  new TitleTooLongException("Title "+ album.getTitle() + "too long!");
        }
        if (album.getId() == null) {
            em.persist(album);
            return album;
        } else {
            return em.merge(album);
        }
    }
}
