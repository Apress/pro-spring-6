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
package com.apress.prospring6.twenty.boot.service;

import com.apress.prospring6.twenty.boot.model.Singer;
import com.apress.prospring6.twenty.boot.problem.SaveException;
import com.apress.prospring6.twenty.boot.repo.SingerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by iuliana on 03/04/2023
 */
@RequiredArgsConstructor
@Transactional
@Service
public class SingerServiceImpl implements SingerService {
    private final SingerRepo singerRepo;

    @Override
    public Flux<Singer> findAll() {
        return singerRepo.findAll();
    }

    @Override
    public Flux<Singer> findByCriteriaDto(CriteriaDto criteria) {
        var fieldName = FieldGroup.getField(criteria.getFieldName().toUpperCase());

        return  switch (fieldName)  {
           case FIRSTNAME -> "*".equals(criteria.getFieldValue()) ? singerRepo.findAll() : singerRepo.findByFirstName(criteria.getFieldValue());
           case LASTNAME ->  "*".equals(criteria.getFieldValue()) ? singerRepo.findAll() : singerRepo.findByLastName(criteria.getFieldValue());
           case BIRTHDATE -> "*".equals(criteria.getFieldValue()) ? singerRepo.findAll() : singerRepo.findByBirthDate(LocalDate.parse(criteria.getFieldValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        };
    }

    @Override
    public Mono<Singer> findByFirstNameAndLastName(String firstName, String lastName) {
        return singerRepo.findByFirstNameAndLastName(firstName, lastName);
    }

    @Override
    public Mono<Singer> findById(Long id) {
        return singerRepo.findById(id);
    }

    @Override
    public Flux<Singer> findByFirstName(String firstName) {
        return singerRepo.findByFirstName(firstName);
    }

    @Override
    public Mono<Singer> save(Singer singer) {
        return singerRepo.save(singer)
                .onErrorMap(error -> new SaveException("Could Not Save Singer " + singer, error));
    }

    @Override
    public Mono<Singer> update(Long id, Singer updateData) {
        return singerRepo.findById(id)
                .flatMap( original -> {
                    original.setFirstName(updateData.getFirstName());
                    original.setLastName(updateData.getLastName());
                    original.setBirthDate(updateData.getBirthDate());
                    return singerRepo.save(original).onErrorMap(error -> new SaveException("Could Not Update Singer " + updateData, error));
                });
    }

    @Override
    public Mono<Void> delete(Long id) {
        return singerRepo.deleteById(id);
    }


}
