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
package com.apress.prospring6.twelve.spring.service;

import com.apress.prospring6.twelve.spring.entities.Car;
import com.apress.prospring6.twelve.spring.repos.CarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service("carService")
@Repository
@Transactional
public class CarServiceImpl implements CarService {
    public boolean done;

    private static final Logger LOGGER = LoggerFactory.getLogger(CarServiceImpl.class);

    private final CarRepository carRepository;

    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    @Transactional(readOnly=true)
    public Stream<Car> findAll() {
        return StreamSupport.stream(carRepository.findAll().spliterator(), false);
    }

    @Override
    public Car save(Car car) {
        return carRepository.save(car);
    }

    @Override
    @Scheduled(fixedDelay=10000)
    public void updateCarAgeJob() {
        var cars = findAll();

        var currentDate = LocalDate.now();
        LOGGER.info("Car age update job started");

        if (System.nanoTime() % 5 == 0) {
            throw new IllegalStateException("Task no " + Thread.currentThread().getName() + " is dead, dead dead...");
        }

        cars.forEach(car -> {
            var p = Period.between(car.getManufactureDate(), currentDate);
            int age = p.getYears();

            car.setAge(age);
            save(car);
            LOGGER.info("Car age update --> {}" , car);
        });

        LOGGER.info("Car age update job completed successfully");
        done = true;
    }

    @Override
    public boolean isDone() {
        return done;
    }
}
