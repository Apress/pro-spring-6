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
package com.apress.prospring6.twenty.boot.controller;

import com.apress.prospring6.twenty.boot.model.Singer;
import com.apress.prospring6.twenty.boot.service.SingerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by iuliana on 03/04/2023
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/reactive/singer")
public class ReactiveSingerController {
    private final SingerService singerService;

    /* 1 */
    @GetMapping(path = {"", "/"})
    public Flux<Singer> list() {
        return singerService.findAll();
    }

    /* 3 */
    @GetMapping(path = "/{id}")
    public Mono<ResponseEntity<Singer>> findById(@PathVariable Long id) {
        return singerService.findById(id)
                .map(s -> ResponseEntity.ok().body(s))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /* 4 */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Singer> create (@RequestBody Singer singer) {
        return singerService.save(singer);
    }

    /* 5 */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Singer>> updateById(@PathVariable Long id, @RequestBody Singer singer){
        return singerService.update(id,singer)
                .map(s -> ResponseEntity.ok().body(s))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    /* 2 */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable Long id){
        return singerService.delete(id)
                .then(Mono.fromCallable(() -> ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /* 6 */
    @GetMapping(params = {"name"})
    public Flux<Singer> searchSingers(@RequestParam("name") String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Missing request parameter 'name'");
        }
        return singerService.findByFirstName(name);
    }

    /* 7 */
    @GetMapping(params = {"fn", "ln"})
    public Mono<Singer> searchSinger(@RequestParam("fn") String fn, @RequestParam("ln") String ln) {
        if ((StringUtils.isBlank(fn) || StringUtils.isBlank(ln))) {
            throw new IllegalArgumentException("Missing request parameter, one of {'fn', 'ln'}");
        }
        return singerService.findByFirstNameAndLastName(fn, ln);
    }


}
