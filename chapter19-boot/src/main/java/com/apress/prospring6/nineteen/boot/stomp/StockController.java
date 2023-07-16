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
package com.apress.prospring6.nineteen.boot.stomp;

import jakarta.annotation.PostConstruct;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
public class StockController {
    private final TaskScheduler taskScheduler;
    private final SimpMessagingTemplate simpMessagingTemplate;

    private List<Stock> stocks = new ArrayList<>();
    private Random random = new Random(System.currentTimeMillis());

    public StockController(TaskScheduler taskScheduler, SimpMessagingTemplate simpMessagingTemplate) {
        this.taskScheduler = taskScheduler;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/addStock")
    public void addStock(Stock stock) throws Exception {
        stocks.add(stock);
        broadcastUpdatedPrices();
    }

    private void broadcastUpdatedPrices() {
        for(Stock stock : stocks) {
            stock.setPrice(stock.getPrice() + (getUpdatedStockPrice() * stock.getPrice()));
            stock.setDate(LocalDateTime.now());
        }

        simpMessagingTemplate.convertAndSend("/topic/price", stocks);
    }

    private double getUpdatedStockPrice() {
        double priceChange = random.nextDouble() * 5.0;

        if (random.nextInt(2) == 1) {
            priceChange = -priceChange;
        }

        return priceChange / 100.0;
    }

    @PostConstruct
    private void broadcastTimePeriodically() {
        stocks.add(new Stock("VMW", 1.00d));
        stocks.add(new Stock("EMC", 1.00d));
        stocks.add(new Stock("GOOG", 1.00d));
        stocks.add(new Stock("IBM", 1.00d));

        taskScheduler.scheduleAtFixedRate(this::broadcastUpdatedPrices, Duration.ofSeconds(2));
    }
}
