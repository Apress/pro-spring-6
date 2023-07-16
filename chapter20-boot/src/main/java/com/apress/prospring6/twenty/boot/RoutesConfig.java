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

import com.apress.prospring6.twenty.boot.handler.HomeHandler;
import com.apress.prospring6.twenty.boot.handler.SingerHandler;
import com.apress.prospring6.twenty.boot.problem.MissingValueException;
import com.apress.prospring6.twenty.boot.problem.SaveException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.queryParam;
import static org.springframework.web.reactive.function.server.RouterFunctions.resources;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * Created by iuliana on 03/04/2023
 */
@Slf4j
@Configuration
public class RoutesConfig {
    final static Logger LOGGER = LoggerFactory.getLogger(RoutesConfig.class);

    @Bean
    public RouterFunction<ServerResponse> staticRouter() {
        return resources("/images/**", new ClassPathResource("static/images/"))
                .and(resources("/styles/**", new ClassPathResource("static/styles/")))
                .and(resources("/js/**", new ClassPathResource("static/js/")));
    }


    @Bean
    public RouterFunction<ServerResponse> singerRoutes(HomeHandler homeHandler, SingerHandler singerHandler) {
        return route()
                // returns home view template
                .GET("/", homeHandler::view)
                .GET("/home", homeHandler::view)
                .GET("/singers/search", singerHandler::searchView)
                .POST("/singers/go", singerHandler::search)
                // these need to be here, otherwise parameters will not be considered
                .GET("/handler/singer", queryParam("name", t -> true), singerHandler::searchSingers)
                .GET("/handler/singer", RequestPredicates.all()
                        .and(queryParam("fn", t -> true))
                        .and(queryParam("ln", t -> true)), singerHandler::searchSinger)
                // requests with parameters always come first
                .GET("/handler/singer", singerHandler.list)
                .POST("/handler/singer", singerHandler::create)
                .GET("/handler/singer/{id}", singerHandler::findById)
                .PUT("/handler/singer/{id}", singerHandler::updateById)
                .DELETE("/handler/singer/{id}", singerHandler.deleteById)
                .filter((request, next) -> {
                    LOGGER.info("Before handler invocation: {}" , request.path());
                    return next.handle(request);
                })
                .build();
    }

    @Bean
    @Order(-2)
    public WebExceptionHandler exceptionHandler() {
        return (ServerWebExchange exchange, Throwable ex) -> {
            if (ex instanceof SaveException se) {
                log.debug("RouterConfig:: handling exception :: " , se);
                exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);

                // marks the response as complete and forbids writing to it
                return exchange.getResponse().setComplete();
            } else if (ex instanceof IllegalArgumentException iae) {
                log.debug("RouterConfig:: handling exception :: " , iae);
                exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);

                // marks the response as complete and forbids writing to it
                return exchange.getResponse().setComplete();
            } else if (ex instanceof MissingValueException mve) {
                exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                exchange.getResponse().getHeaders().add("Content-Type", "application/json");
                final String message;
                try {
                    message = new JsonMapper().writeValueAsString(mve.getFieldNames());
                    var buffer = exchange.getResponse().bufferFactory().wrap(message.getBytes());
                    return exchange.getResponse().writeWith(Flux.just(buffer));
                } catch (JsonProcessingException e) {
                }
            }
            return Mono.error(ex);
        };
    }
}
