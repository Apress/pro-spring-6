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
package com.apress.prospring6.twelve.spring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.ErrorHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by iuliana on 29/10/2022
 */

@Configuration
@ComponentScan(basePackages  = {"com.apress.prospring6.twelve.spring"},
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {TaskSchedulingConfig.class, TaskSchedulingConfig2.class, TaskSchedulingConfig3.class})
)
@EnableScheduling
public class TaskSchedulingConfig4 {

    @Bean
    TaskScheduler taskScheduler() {
        var tpts =  new ThreadPoolTaskScheduler();
        tpts.setPoolSize(3);
        tpts.setThreadNamePrefix("tsc4-");
        tpts.setErrorHandler(new LoggingErrorHandler("tsc4"));
        tpts.setRejectedExecutionHandler(new RejectedTaskHandler());
        return tpts;
    }
}

class LoggingErrorHandler implements ErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingErrorHandler.class);
    private final String name;

    public LoggingErrorHandler(String name) {
        this.name = name;
    }

    @Override
    public void handleError(Throwable t) {
        LOGGER.error("[{}]: task failed because {}",name , t.getMessage(), t);
    }
}

class RejectedTaskHandler implements RejectedExecutionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RejectedTaskHandler.class);

    private Map<Runnable, Integer> rejectedTasks = new ConcurrentHashMap<>();

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        LOGGER.info(" >>  check for resubmission.");
        boolean submit = true;
        if (rejectedTasks.containsKey(r)) {
            int submittedCnt = rejectedTasks.get(r);
            if (submittedCnt > 5) {
                submit = false;
            } else {
                rejectedTasks.put(r, rejectedTasks.get(r) + 1);
            }
        } else {
            rejectedTasks.put(r, 1);
        }
        if(submit) {
            executor.execute(r);
        } else {
            LOGGER.error(">> Task {} cannot be re-submitted.", r.toString());
        }
    }
}