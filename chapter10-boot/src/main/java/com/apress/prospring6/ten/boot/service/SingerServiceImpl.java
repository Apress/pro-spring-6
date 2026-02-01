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
package com.apress.prospring6.ten.boot.service;

import com.apress.prospring6.ten.boot.entities.Singer;
import com.apress.prospring6.ten.boot.repos.SingerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by iuliana.cosmina on 01/08/2022
 */
@Service
@Transactional
public class SingerServiceImpl implements SingerService {

    private final SingerRepository singerRepository;

    public SingerServiceImpl(SingerRepository singerRepository) {
        this.singerRepository = singerRepository;
    }

    @Transactional(readOnly=true)
    @Override
    public Stream<Singer> findAll() {
        return StreamSupport.stream(singerRepository.findAll().spliterator(), false);
    }

    @Transactional(readOnly=true)
    @Override
    public Stream<Singer> findAllWithAlbums() {
        return StreamSupport.stream(singerRepository.findAllWithAlbums().spliterator(), false);

    }

    @Transactional(readOnly=true)
    @Override
    public Stream<Singer> findByFirstName(String firstName) {
        return StreamSupport.stream(singerRepository.findByFirstName(firstName).spliterator(), false);
    }

    @Transactional(readOnly=true)
    public Stream<Singer> findByFirstNameAndLastName(String firstName, String lastName) {
        return StreamSupport.stream(singerRepository.findByFirstNameAndLastName(firstName, lastName).spliterator(), false);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, label = "modifying")
    @Override
    public Singer updateFirstName(String firstName, Long id) {
        singerRepository.findById(id).ifPresent(s -> singerRepository.setFirstNameFor(firstName, id));
        return  singerRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly=true)
    @Override
    public Stream<SingerRepository.FullName> findByLastName(String lastName) {
        return StreamSupport.stream(singerRepository.findByLastName(lastName).spliterator(), false);
    }
}
