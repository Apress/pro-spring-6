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

package com.apress.prospring6.twenty.boot.repo;

import com.apress.prospring6.twenty.boot.model.Singer;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;


/**
 * Created by iuliana on 02/04/2023
 */

public interface SingerRepo extends ReactiveCrudRepository<Singer,Long>{

    @Query("select * from singer where first_name=:fn and last_name=:ln")
    Mono<Singer> findByFirstNameAndLastName(@Param("fn") String firstName, @Param("ln") String lastName);

    @Query("select * from singer where first_name=:fn")
    Flux<Singer> findByFirstName(@Param("fn") String firstName);

    @Query("select * from singer where last_name=:ln")
    Flux<Singer> findByLastName(@Param("ln") String lastName);

    @Query("select * from singer where birth_date=:ln")
    Flux<Singer> findByBirthDate(@Param("bd") LocalDate birthDate);
}

