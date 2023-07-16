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

import com.apress.prospring6.sixteen.boot.entities.Instrument;
import com.apress.prospring6.sixteen.boot.entities.Singer;
import com.apress.prospring6.sixteen.boot.repos.InstrumentRepo;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

/**
 * Created by iuliana on 16/02/2023
 */
@Controller
public class InstrumentController {

    private final InstrumentRepo instrumentRepo;

    public InstrumentController(InstrumentRepo instrumentRepo) {
        this.instrumentRepo = instrumentRepo;
    }

    @QueryMapping
    public Iterable<Instrument> instruments(){
        return instrumentRepo.findAll();
    }

    @QueryMapping
    public Long instrumentsCount(){
        return instrumentRepo.count();
    }

    @MutationMapping
    public Instrument newInstrument(@Argument String name){
        return instrumentRepo.save(new Instrument(name, null));
    }
}
