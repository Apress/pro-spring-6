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
package com.apress.prospring6.twenty.boot.handler;

import com.apress.prospring6.twenty.boot.model.Singer;
import com.apress.prospring6.twenty.boot.problem.MissingValueException;
import com.apress.prospring6.twenty.boot.service.SingerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.server.ServerResponse.*;
/**
 * Created by iuliana on 02/04/2023
 */
@Component
public class SingerHandler {
    private final SingerService singerService;

    public HandlerFunction<ServerResponse> list;
    public HandlerFunction<ServerResponse> deleteById;


    public SingerHandler(SingerService singerService) {
        this.singerService = singerService;

        /* 1 */
        list = serverRequest ->ok()
                .contentType(MediaType.APPLICATION_JSON).body(singerService.findAll(), Singer.class);

        /* 2 */
        deleteById = serverRequest -> noContent()
                .build(singerService.delete(Long.parseLong(serverRequest.pathVariable("id"))));
    }


    /* 3 */
    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        var id = Long.parseLong(serverRequest.pathVariable("id"));

       return singerService.findById(id)
                .flatMap(singer -> ok().contentType(MediaType.APPLICATION_JSON).bodyValue(singer))
                .switchIfEmpty(notFound().build());
    }

    /* 4 */
    public Mono<ServerResponse> create(ServerRequest serverRequest) {
        Mono<Singer> singerMono =  serverRequest.bodyToMono(Singer.class);
        return singerMono
                .flatMap(singerService::save)
                .log()
                .flatMap(s -> created(URI.create("/singer/" + s.getId()))
                        .contentType(MediaType.APPLICATION_JSON).bodyValue(s));
                //.switchIfEmpty(status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    /* 5 */
    public Mono<ServerResponse>  updateById(ServerRequest serverRequest) {
        var id = Long.parseLong(serverRequest.pathVariable("id"));

        return singerService.findById(id)
                    .flatMap(fromDb -> serverRequest.bodyToMono(Singer.class)
                                .flatMap(s -> ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(singerService.update(id, s), Singer.class)
                    )
                            // we switch to 400 because this is an invalid put request
                ).switchIfEmpty(badRequest().bodyValue("Failure to update singer!"));
        // we can put anything in the ServerResponse including an exception
        //).switchIfEmpty(badRequest().bodyValue(new NotFoundException(String.class, id)));
    }

    /* 6 */
    public Mono<ServerResponse> searchSingers(ServerRequest serverRequest) {
        var name =  serverRequest.queryParam("name").orElse(null);

        if (StringUtils.isBlank(name) ) {
            // parameter is an empty string
            //return badRequest().bodyValue(new IllegalArgumentException("Missing request parameter 'name'"));
            return badRequest().bodyValue("Missing request parameter 'name'");
        }
        return ok()
                .contentType(MediaType.APPLICATION_JSON).body(singerService.findByFirstName(name), Singer.class);
    }

    /* 7 */
    public Mono<ServerResponse> searchSinger(ServerRequest serverRequest) {
        var fn =  serverRequest.queryParam("fn").orElse(null);
        var ln = serverRequest.queryParam("ln").orElse(null);

        if ((StringUtils.isBlank(fn) || StringUtils.isBlank(ln))) {
            // one of {fn, ln} (or both) parameter is an empty string
            //return badRequest().bodyValue(new IllegalArgumentException("Missing request parameter, one of {fn, ln}"));
            return badRequest().bodyValue("Missing request parameter, one of {fn, ln}");
        }

        return singerService.findByFirstNameAndLastName(fn, ln)
                .flatMap(singer -> ok().contentType(MediaType.APPLICATION_JSON).bodyValue(singer));
    }


    public Mono<ServerResponse> search(ServerRequest serverRequest) {
        var criteriaMono = serverRequest.bodyToMono(SingerService.CriteriaDto.class);
        return criteriaMono.log()
                .flatMap(this::validate)
                .flatMap(criteria -> ok().contentType(MediaType.APPLICATION_JSON)
                        .body(singerService.findByCriteriaDto(criteria), Singer.class));
    }

    private Mono<SingerService.CriteriaDto> validate(SingerService.CriteriaDto criteria) {
        var validator = new SingerService.CriteriaValidator();
        var errors = new BeanPropertyBindingResult(criteria, "criteria");
        validator.validate(criteria, errors);
        if (errors.hasErrors()) {
            // throw new ServerWebInputException(errors.toString());
            throw MissingValueException.of(errors.getAllErrors());
        }
        return Mono.just(criteria);
    }

    public Mono<ServerResponse> searchView(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(MediaType.TEXT_HTML)
                .render("singers/search", new SingerService.CriteriaDto());
    }

}
