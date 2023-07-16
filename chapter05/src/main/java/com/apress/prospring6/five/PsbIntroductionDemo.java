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
package com.apress.prospring6.five;

import com.apress.prospring6.five.introduction.Contact;
import com.apress.prospring6.five.introduction.IsModified;
import com.apress.prospring6.five.introduction.IsModifiedAdvisor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

/**
 * Created by iuliana.cosmina on 17/04/2022
 */

@Configuration
class IntroductionAopConfig {

    @Bean
    public Contact guitarist(){
        var contact = new Contact();
        contact.setName("John Mayer");
        return contact;
    }

    @Bean
    public IsModifiedAdvisor advisor() {
        return new IsModifiedAdvisor();
    }

    @Bean
    public Contact proxy(){
        ProxyFactoryBean pfb = new ProxyFactoryBean();
        pfb.setProxyTargetClass(true);
        pfb.setTarget(guitarist());
        pfb.addAdvisor(advisor());
        pfb.setFrozen(true);
        return (Contact) pfb.getObject();
    }
}

public class PsbIntroductionDemo {
    private static Logger LOGGER = LoggerFactory.getLogger(PsbIntroductionDemo.class);

    public static void main(String... args) {
        GenericApplicationContext ctx = new AnnotationConfigApplicationContext(IntroductionAopConfig.class);

        Contact proxy = (Contact) ctx.getBean("proxy");
        IsModified proxyInterface = (IsModified) proxy;

        LOGGER.info("Is Contact? => {} " , (proxy instanceof Contact));
        LOGGER.info("Is IsModified? => {} " , (proxy instanceof IsModified));
        LOGGER.info("Has been modified? => {} " , proxyInterface.isModified());

        proxy.setName("John Mayer");
        LOGGER.info("Has been modified? => {} " , proxyInterface.isModified());

        proxy.setName("Ben Barnes");
        LOGGER.info("Has been modified? => {} " , proxyInterface.isModified());
    }
}
