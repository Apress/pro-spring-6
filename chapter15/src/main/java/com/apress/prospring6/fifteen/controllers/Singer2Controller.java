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
package com.apress.prospring6.fifteen.controllers;

import com.apress.prospring6.fifteen.entities.Singer;
import com.apress.prospring6.fifteen.repos.SingerRepo;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Created by iuliana on 22/01/2023
 */
@RestController
@RequestMapping(path = "singer2")
public class Singer2Controller {

    final Logger LOGGER = LoggerFactory.getLogger(Singer2Controller.class);

    private final SingerRepo singerRepo;

    public Singer2Controller(SingerRepo singerRepo) {
        this.singerRepo = singerRepo;
    }

    @GetMapping(path={"/", ""})
    public ResponseEntity<List<Singer>> all() {
        var singers = singerRepo.findAll();
        if(singers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(singerRepo.findAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Singer> findSingerById(@PathVariable Long id) {
        Optional<Singer> fromDb = singerRepo.findById(id);
        return fromDb
                .map(s -> ResponseEntity.ok().body(s))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(path="/")
    public ResponseEntity<Singer> create(@RequestBody @Valid Singer singer) {
        LOGGER.info("Creating singer: {}" , singer);

        try {
            var saved = singerRepo.save(singer);
            LOGGER.info("Singer created successfully with info: {}" , saved);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException dive) {
            LOGGER.debug("Could not create singer." , dive);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<Object> update(@RequestBody @Valid Singer singer, @PathVariable Long id) {
        LOGGER.info("Updating singer: {}" , singer);
        Optional<Singer> fromDb = singerRepo.findById(id);

        return fromDb
                .map(s ->  {
                    s.setFirstName(singer.getFirstName());
                    s.setLastName(singer.getLastName());
                    s.setBirthDate(singer.getBirthDate());
                    try {
                        singerRepo.save(s);
                        return ResponseEntity.ok().build();
                    } catch (DataIntegrityViolationException dive) {
                        LOGGER.debug("Could not update singer." , dive);
                        return ResponseEntity.badRequest().build();
                    }
                })
                .orElseGet(() ->  ResponseEntity.notFound().build());
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        LOGGER.info("Deleting singer with id: {}" , id);

        Optional<Singer> fromDb = singerRepo.findById(id);
        return fromDb
                .map(s -> {
                    singerRepo.deleteById(id);
                    return ResponseEntity.noContent().build();
                    })
                .orElseGet(() ->  ResponseEntity.notFound().build());

    }
}
