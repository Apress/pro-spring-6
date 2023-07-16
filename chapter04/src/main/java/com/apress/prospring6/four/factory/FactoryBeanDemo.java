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
package com.apress.prospring6.four.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.security.MessageDigest;

/**
 * Created by iuliana.cosmina on 26/03/2022
 */

@Configuration
@ComponentScan
class MessageDigestConfig {

    @Bean
    public MessageDigestFactoryBean shaDigest(){
        MessageDigestFactoryBean shaDigest =  new MessageDigestFactoryBean();
        shaDigest.setAlgorithmName("SHA1");
        return shaDigest;
    }

    @Bean
    public MessageDigestFactoryBean defaultDigest(){
        return  new MessageDigestFactoryBean();
    }

    @Bean
    public MessageDigester digester() throws Exception {
        MessageDigester messageDigester = new MessageDigester();
        messageDigester.setDigest1(shaDigest().getObject());
        messageDigester.setDigest2(defaultDigest().getObject());
        return messageDigester;
    }
}

public class FactoryBeanDemo {
    private static Logger LOGGER = LoggerFactory.getLogger(FactoryBeanDemo.class);

    public static void main(String... args) {
        var ctx = new AnnotationConfigApplicationContext(MessageDigestConfig.class);
        MessageDigester digester = ctx.getBean("digester", MessageDigester.class);
        digester.digest("Hello World!");

        LOGGER.debug("-------------------------------------");

        MessageDigestFactoryBean factoryBean =
                (MessageDigestFactoryBean) ctx.getBean("&shaDigest");
        try {
            MessageDigest shaDigest = factoryBean.getObject();
            LOGGER.info("Explicit use digest bean: {}", shaDigest.digest("Hello world".getBytes()));
        } catch (Exception ex) {
            LOGGER.error("Could not find MessageDigestFactoryBean ", ex);
        }

        ctx.close();
    }
}
