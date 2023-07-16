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
import com.apress.prospring6.sixteen.boot.entities.Singer;
import com.apress.prospring6.sixteen.boot.repos.AwardRepo;
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

/**
 * Created by iuliana on 13/02/2023
 */
@Controller
public class AwardController {

    private final AwardRepo awardRepo;
    private final SingerRepo singerRepo;

    public AwardController(AwardRepo awardRepo, SingerRepo singerRepo) {
        this.awardRepo = awardRepo;
        this.singerRepo = singerRepo;
    }

    @QueryMapping
    public Iterable<Award> awardsBySinger(@Argument Long singerId){
        return awardRepo.findAwardsBySinger(singerId);
    }

    @QueryMapping
    public Iterable<Award> awards(DataFetchingEnvironment environment) {
        DataFetchingFieldSelectionSet s = environment.getSelectionSet();
        return s.contains("singer") ?  awardRepo.findAll(fetchSinger()) :  awardRepo.findAll();
    }

    @QueryMapping
    public Long awardsCount(){
        return awardRepo.count();
    }

    @MutationMapping
    public Award  newAward(@Argument AwardInput award) {
        var singer = singerRepo.findById(award.singerId())
                .orElseThrow(() -> new IllegalArgumentException("singer with id " + 1 + " does not exists!"));

        var newAward =  new Award();
        newAward.setYear(award.year);
        newAward.setCategory(award.category);
        newAward.setItemName(award.itemName);
        newAward.setAwardName(award.awardName);
        newAward.setSinger(singer);
        return awardRepo.save(newAward);
    }

    record AwardInput(Integer year, String category, String itemName, String awardName, Long singerId){}

    private Specification<Award> fetchSinger() {
        return (root, query, builder) -> {
            Fetch<Award, Singer> f = root.fetch("singer", JoinType.LEFT);
            Join<Award, Singer> join = (Join<Award, Singer>) f;
            return join.getOn();
        };
    }
}
