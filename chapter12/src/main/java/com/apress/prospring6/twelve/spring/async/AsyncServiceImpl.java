package com.apress.prospring6.twelve.spring.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

public class AsyncServiceImpl implements AsyncService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncServiceImpl.class);

    @Async
    @Override
    public void asyncTask() {
        LOGGER.info("Start execution of async. task");

        if (System.nanoTime() % 5 == 0) {
            throw new IllegalStateException("Task no " + Thread.currentThread().getName() + " is dead, dead dead...");
        }
        try {
            Thread.sleep(10000);
        } catch (Exception ex) {
            LOGGER.error("Task Interruption", ex);
        }
        LOGGER.info("Complete execution of async. task");
    }

    @Async
    @Override
    public Future<String> asyncWithReturn(String name) {
        LOGGER.info("Start execution of async. task with return for {}",name);

        try {
            Thread.sleep(5000);
        } catch (Exception ex) {
            LOGGER.error("Task Interruption", ex);
        }

        LOGGER.info("Complete execution of async. task with return for {}", name);
        return CompletableFuture.completedFuture("Hello: " + name);
    }
}
