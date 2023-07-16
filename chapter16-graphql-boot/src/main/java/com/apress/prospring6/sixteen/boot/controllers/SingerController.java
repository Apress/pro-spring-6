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
package com.apress.prospring6.sixteen.boot.controllers;

import com.apress.prospring6.sixteen.boot.entities.Award;
import com.apress.prospring6.sixteen.boot.entities.Instrument;
import com.apress.prospring6.sixteen.boot.entities.Singer;
import com.apress.prospring6.sixteen.boot.problem.NotFoundException;
import com.apress.prospring6.sixteen.boot.repos.SingerRepo;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingFieldSelectionSet;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Created by iuliana on 07/02/2023
 */
@Controller
public class SingerController {

    private final SingerRepo singerRepo;

    public SingerController(SingerRepo singerRepo) {
        this.singerRepo = singerRepo;
    }

    @QueryMapping
    public Singer singerById(@Argument Long id, DataFetchingEnvironment environment){
        Specification<Singer> spec = byId(id);
        DataFetchingFieldSelectionSet s = environment.getSelectionSet();
        if (s.contains("awards") && !s.contains("instruments"))
            spec =  spec.and(fetchAwards());
        else if (s.contains("awards") && s.contains("instruments"))
            spec = spec.and(fetchAwards().and(fetchInstruments()));
        else if (!s.contains("awards") && s.contains("instruments"))
            spec = spec.and(fetchInstruments());

        return singerRepo.findOne(spec).orElse(null);
    }

/*
    @QueryMapping
    public Iterable<Singer> singers(){
        return singerRepo.findAll();
    }
*/

   @QueryMapping
    public Iterable<Singer> singers(DataFetchingEnvironment environment) {
        DataFetchingFieldSelectionSet s = environment.getSelectionSet();
        if (s.contains("awards") && !s.contains("instruments"))
            return singerRepo.findAll(fetchAwards());
        else if (s.contains("awards") && s.contains("instruments"))
            return singerRepo.findAll(fetchAwards().and(fetchInstruments()));
        else if (!s.contains("awards") && s.contains("instruments"))
            return singerRepo.findAll(fetchInstruments());
        else
            return singerRepo.findAll();
    }

    @QueryMapping
    public Long singersCount(){
        return singerRepo.count();
    }

    @MutationMapping
    public Singer newSinger(@Argument SingerInput singer){
        LocalDate date;
        try {
            date = LocalDate.parse(singer.birthDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Bade date format");
        }
        var newSinger =  new Singer(null, 0, singer.firstName,singer.lastName,
                singer.pseudonym, singer.genre, date, null, null);
        return singerRepo.save(newSinger);
    }

    @MutationMapping
    public Singer updateSinger(@Argument Long id, @Argument SingerInput singer) {
       var fromDb = singerRepo.findById(id).orElseThrow(() -> new NotFoundException(Singer.class, id ));
        fromDb.setFirstName(singer.firstName);
        fromDb.setLastName(singer.lastName);
        fromDb.setPseudonym(singer.pseudonym);
        fromDb.setGenre(singer.genre);
        LocalDate date;
        try {
            date = LocalDate.parse(singer.birthDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            fromDb.setBirthDate(date);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Bade date format");
        }
        return singerRepo.save(fromDb);
    }

    @MutationMapping
    public Long deleteSinger(@Argument Long id) {
        singerRepo.findById(id).orElseThrow(() -> new NotFoundException(Singer.class, id ));
        singerRepo.deleteById(id);
        return id;
    }

    record SingerInput(String firstName, String lastName, String pseudonym, String genre, String birthDate){}

    private Specification<Singer> byId(Long id) {
        return (root, query, builder) ->
                builder.equal(root.get("id"), id);
    }

    private Specification<Singer> fetchAwards() {
        return (root, query, builder) -> {
            Fetch<Singer, Award> f = root.fetch("awards", JoinType.LEFT);
            Join<Singer, Award> join = (Join<Singer, Award>) f;
            return join.getOn();
        };
    }

    private Specification<Singer> fetchInstruments() {
        return (root, query, builder) -> {
            Fetch<Singer, Instrument> f = root.fetch("instruments", JoinType.LEFT);
            Join<Singer, Instrument> join = (Join<Singer, Instrument>) f;
            return join.getOn();
        };
    }
}
