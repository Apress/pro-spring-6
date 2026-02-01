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
package com.apress.prospring6.twenty.boot;

import com.apress.prospring6.twenty.boot.model.Singer;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by iuliana on 08/04/2023
 */
public class SimpleProgrammingTest {
    List<Singer> singers = List.of(
            Singer.builder().firstName("John").lastName("Mayer").birthDate(LocalDate.of(1977, 10, 16)).build(),
            Singer.builder().firstName("B.B.").lastName("King").birthDate(LocalDate.of(1929, 9, 16)).build(),
            Singer.builder().firstName("Peggy").lastName("Lee").birthDate(LocalDate.of(1920, 5, 26)).build(),
            Singer.builder().firstName("Ella").lastName("Fitzgerald").birthDate(LocalDate.of(1917, 4, 25)).build()
    );

    Function<Singer, Pair<Singer, Integer>> computeAge = singer -> Pair.of(singer,Period.between(singer.getBirthDate(), LocalDate.now()).getYears());
    Predicate<Pair<Singer, Integer>> checkAge = pair -> pair.getRight() > 50;

    @Test
    void imperativePlay(){
        int agesum = 0;
        for (var s : singers) {
           var p = computeAge.apply(s);
           if (checkAge.test(p)) {
               agesum += p.getRight();
           }
        }
        assertEquals(303, agesum); // depending when you are running this test it might fail, now in October 2023 it passes ;)
    }

    @Test
    void streamsPlay() {
         var agesum = singers.stream() // Stream<Singer>
                .map(computeAge) // Stream<Pair<Singer, Integer>>
                .filter(checkAge)// Stream<Pair<Singer, Integer>>
                .map(Pair::getRight) // Stream<Integer>
                .reduce(Integer:: sum) // Optional<Integer>
                 .orElseThrow(() -> new RuntimeException("Something went wrong!"));

         assertEquals(303, agesum); // depending when you are running this test it might fail, now in October 2023 it passes ;)
    }

    @Test
    void reactivePlay() {
        Flux.fromIterable(singers)  // Flux<Singer>
                .map(computeAge) // Flux <Pair<Singer, Integer>>
                .filter(checkAge) // Flux <Pair<Singer, Integer>>
                .map(Pair::getRight) // Flux <Integer>
                .reduce(0, Integer::sum) // Mono<Integer>
                .subscribe(new BaseSubscriber<>() {
                    @Override
                    protected void hookOnNext(Integer agesum) {
                        assertEquals(302, agesum); // depending when you are running this test it might fail, now in April 2023 it passes ;)
                    }
                });
    }
}
