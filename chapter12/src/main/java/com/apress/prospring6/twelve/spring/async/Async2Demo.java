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
package com.apress.prospring6.twelve.spring.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

/**
 * Created by iuliana on 26/01/2023
 */
class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncExceptionHandler.class);
    @Override
    public void handleUncaughtException(Throwable t, Method method, Object... obj) {
        LOGGER.error("[{}]: task method '{}' failed because {}" , Thread.currentThread(), method.getName() , t.getMessage(), t);
    }
}

@Configuration
@EnableAsync
@ComponentScan
class Async2Config implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        var tpts =  new ThreadPoolTaskExecutor();
        tpts.setCorePoolSize(2);
        tpts.setMaxPoolSize(10);
        tpts.setThreadNamePrefix("tpte2-");
        tpts.setQueueCapacity(5);
        tpts.initialize();
        return tpts;
    }

    @Bean
    public AsyncService asyncService() {
        return new AsyncServiceImpl();
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncExceptionHandler();
    }
}

public class Async2Demo {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncDemo.class);

    public static void main(String... args) throws IOException, ExecutionException, InterruptedException {
        try (var ctx = new AnnotationConfigApplicationContext(Async2Config.class)) {
            var asyncService = ctx.getBean("asyncService", AsyncService.class);

            for (int i = 0; i < 5; i++) {
                asyncService.asyncTask();
            }

            var result1 = asyncService.asyncWithReturn("John Mayer");
            var result2 = asyncService.asyncWithReturn("Eric Clapton");
            var result3 = asyncService.asyncWithReturn("BB King");
            Thread.sleep(6000);

            LOGGER.info(" >> Result1: " + result1.get());
            LOGGER.info(" >> Result2: " + result2.get());
            LOGGER.info(" >> Result3: " + result3.get());

            System.in.read();
        }
    }
}
