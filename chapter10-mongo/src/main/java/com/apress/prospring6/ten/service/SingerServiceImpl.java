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
package com.apress.prospring6.ten.service;

import com.apress.prospring6.ten.document.Singer;
import com.apress.prospring6.ten.repos.SingerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Transactional
@Service
public class SingerServiceImpl implements SingerService {
    private final SingerRepository singerRepository;

    public SingerServiceImpl(SingerRepository singerRepository) {
        this.singerRepository = singerRepository;
    }

    @Override
    public Stream<Singer> findAll() {
        return StreamSupport.stream(singerRepository.findAll().spliterator(), false);
    }

    @Override
    public Stream<Singer> findByFirstName(String firstName) {
        return StreamSupport.stream(singerRepository.findByFirstName(firstName).spliterator(), false);
    }

    @Override
    public void saveAll(Iterable<Singer> singers) {
        singerRepository.saveAll(singers);
    }

    @Override
    public Singer findByFullName(boolean positional, String firstName, String lastName) {
        var singers = positional
                ? singerRepository.findByPositionedParams(firstName, lastName)
                : singerRepository.findByNamedParams(firstName, lastName);
        if(singers != null) {
            return singers.iterator().next();
        }
        return null;
    }
}
