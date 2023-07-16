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
package com.apress.prospring6.three.naming;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by iuliana.cosmina on 08/03/2022
 */
public class BeanNamingDemo {

    private static Logger logger = LoggerFactory.getLogger(BeanNamingDemo.class);

    public static void main(String... args) {
        var ctx = new AnnotationConfigApplicationContext(BeanNamingCfg.class);
        Arrays.stream(ctx.getBeanDefinitionNames()).forEach(beanName -> logger.debug(beanName));

        try {
            ctx.getBean(SimpleBean.class);
        } catch (Exception e) {
            logger.debug("More beans than expected found. ", e);
        }

        logger.info(" ... All beans of type: {} ", SimpleBean.class.getSimpleName());
        var beans = ctx.getBeansOfType(SimpleBean.class);
        beans.entrySet().forEach(b -> System.out.println(b.getKey()));
    }
}

@Configuration
@ComponentScan
class BeanNamingCfg {

    @Bean
    public SimpleBean  anotherSimpleBean(){
        return new SimpleBean();
    }
}

@Component
class SimpleBean { }

