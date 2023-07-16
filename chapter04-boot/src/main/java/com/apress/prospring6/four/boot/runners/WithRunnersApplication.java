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
package com.apress.prospring6.four.boot.runners;

import com.apress.prospring6.two.decoupled.MessageProvider;
import com.apress.prospring6.two.decoupled.MessageRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by iuliana.cosmina on 03/04/2022
 */
@Order(2)
@Component("messageRenderer")
class StandardOutMessageRenderer  implements MessageRenderer, CommandLineRunner {
    private static Logger logger = LoggerFactory.getLogger(StandardOutMessageRenderer.class);

    private MessageProvider messageProvider;

    @Autowired
    @Override
    public void setMessageProvider(MessageProvider provider) {
        this.messageProvider = provider;
    }

    @Override
    public MessageProvider getMessageProvider() {
        return this.messageProvider;
    }

    @Override
    public void render() {
        logger.info(messageProvider.getMessage());
    }

    @Override
    public void run(String... args) throws Exception {
        render();
    }
}

@Order(1)
@Component
class ConfigurableMessageProvider implements MessageProvider, CommandLineRunner {

    private String message;

    public ConfigurableMessageProvider(@Value("Configurable message") String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void run(String... args) throws Exception {
        if(args.length >=1) {
            message = args[0];
        }
    }
}

@SpringBootApplication
public class WithRunnersApplication {

    public static void main(String... args) {
        SpringApplication.run(WithRunnersApplication.class, args);

    }
}
