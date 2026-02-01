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
package com.apress.prospring6.three.autowiring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by iuliana.cosmina on 11/03/2022
 */
public class AutowiringDemo {
    private static Logger LOGGER = LoggerFactory.getLogger(AutowiringDemo.class);

    public static void main(String... args) {

        var ctx = new AnnotationConfigApplicationContext(AutowiringCfg.class);

        var target = ctx.getBean(Target.class);
        LOGGER.info("target: Created target? {}" , target != null);
        LOGGER.info("target: Injected bar? {}" , target.bar != null);
        LOGGER.info("target: Injected fooOne? {}" , target.fooOne != null ? target.fooOne.id: "");
        LOGGER.info("target: Injected fooTwo? {}" , target.fooTwo != null ? target.fooTwo.id : "");

        var anotherTarget = ctx.getBean(AnotherTarget.class);
        LOGGER.info("anotherTarget: Created anotherTarget? {}" , anotherTarget != null);
        LOGGER.info("anotherTarget: Injected bar? {}" , anotherTarget.bar != null);
        LOGGER.info("anotherTarget: Injected fooOne? {}" , anotherTarget.fooOne != null ? anotherTarget.fooOne.id: "");
        LOGGER.info("anotherTarget: Injected fooTwo? {}" , anotherTarget.fooTwo != null ? anotherTarget.fooTwo.id : "");

        var fieldTarget = ctx.getBean(FieldTarget.class);
        LOGGER.info("fieldTarget: Created fieldTarget? {}" , fieldTarget != null);
        LOGGER.info("fieldTarget: Injected bar? {}" , fieldTarget.bar != null);
        LOGGER.info("fieldTarget: Injected fooOne? {}" , fieldTarget.fooOne != null ? fieldTarget.fooOne.id: "");
        LOGGER.info("fieldTarget: Injected fooTwo? {}" , fieldTarget.fooTwo != null ? fieldTarget.fooTwo.id : "");
    }
}

@Configuration
@ComponentScan
class AutowiringCfg {

    @Bean
    Foo anotherFoo() {
        return new Foo();
    }
}

@Component
@Lazy
class Target {

    private static Logger logger = LoggerFactory.getLogger(Target.class);
    Foo fooOne;
    Foo fooTwo;
    Bar bar;

   /* public Target() {
        logger.info(" --> Target() called");
    }*/

    @Autowired
    public Target(@Qualifier("foo") Foo foo) {
        this.fooOne = foo;
        logger.info(" --> Target(Foo) called");
    }

    //@Autowired
    public Target(@Qualifier("foo")Foo foo, Bar bar) {
        this.fooOne = foo;
        this.bar = bar;
        logger.info(" --> Target(Foo, Bar) called");
    }
}

@Component
@Lazy
class AnotherTarget {

    private static Logger logger = LoggerFactory.getLogger(AnotherTarget.class);
    Foo fooOne;
    Foo fooTwo;
    Bar bar;

    @Autowired
    public void setFooOne(@Qualifier("foo")Foo fooOne) {
        logger.info(" --> AnotherTarget#setFooOne(Foo) called");
        this.fooOne = fooOne;
    }

    @Autowired
    public void setFooTwo(@Qualifier("anotherFoo")Foo fooTwo) {
        logger.info(" --> AnotherTarget#setFooTwo(Foo) called");
        this.fooTwo = fooTwo;
    }

    @Autowired
    public void setBar(Bar bar) {
        logger.info(" --> AnotherTarget#setBar(Bar) called");
        this.bar = bar;
    }
}

@Component
@Lazy
class FieldTarget {

    @Autowired @Qualifier("foo") Foo fooOne;
    @Autowired @Qualifier("anotherFoo") Foo fooTwo;
    @Autowired Bar bar;

}


@Component
class Foo {
    String id = UUID.randomUUID().toString().replace("-","").substring(0,8);
}

@Component
class Bar {}
