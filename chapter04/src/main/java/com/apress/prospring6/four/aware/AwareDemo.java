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
package com.apress.prospring6.four.aware;

import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by iuliana.cosmina on 25/03/2022
 */
class NamedSinger implements BeanNameAware {
    private static Logger logger = LoggerFactory.getLogger(NamedSinger.class);
    private String name;

    @Override /** @Implements {@link BeanNameAware#setBeanName(String)} */
    public void setBeanName(String beanName) {
        this.name = beanName;
    }

    public void sing() {
        logger.info("Singer " + name + " - sing()");
    }
}

class FileManager {
    private static Logger logger = LoggerFactory.getLogger(FileManager.class);
    private Path file;

    public FileManager() {
        logger.info("Creating bean of type {}", FileManager.class);
        try {
            file = Files.createFile(Path.of("sample"));
        } catch (IOException e) {
            logger.error("Could not create file");
        }
    }

    @PreDestroy
    private void preDestroy() throws IOException {
        logger.info("Calling preDestroy() on bean of type {}", FileManager.class);
        if (file != null) {
            Files.deleteIfExists(file);
        }
    }
}

class ShutdownHookBean implements ApplicationContextAware {
    private ApplicationContext ctx;
    /** @Implements {@link ApplicationContextAware#setApplicationContext(ApplicationContext)} }*/
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        if (ctx instanceof GenericApplicationContext) {
            ((GenericApplicationContext) ctx).registerShutdownHook();
        }
    }
}

@ComponentScan
class AwareConfig {

    @Bean
    NamedSinger johnMayer(){
        return new NamedSinger();
    }

    @Bean
    FileManager fileManager() {
        return new FileManager();
    }
    @Bean
    ShutdownHookBean shutdownHookBean() {
        return new ShutdownHookBean();
    }
}


public class AwareDemo {

    public static void main(String... args) {
        var ctx = new AnnotationConfigApplicationContext(AwareConfig.class);

        var singer = ctx.getBean(NamedSinger.class);
        singer.sing();

       //ctx.registerShutdownHook(); // no longer needed because of the ShutdownHookBean
    }
}
