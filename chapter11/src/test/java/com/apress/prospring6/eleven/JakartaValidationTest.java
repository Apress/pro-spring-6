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
package com.apress.prospring6.eleven;

import com.apress.prospring6.eleven.domain.Singer;
import com.apress.prospring6.eleven.domain.SingerTwo;
import com.apress.prospring6.eleven.validator.JakartaValidationCfg;
import com.apress.prospring6.eleven.validator.SingerTwoValidationService;
import com.apress.prospring6.eleven.validator.SingerValidationService;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by iuliana on 28/08/2022
 */
public class JakartaValidationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JakartaValidationTest.class);

    @Test
    void testSingerValidation() {
        try (var ctx = new AnnotationConfigApplicationContext(JakartaValidationCfg.class)) {
            var singerBeanValidationService = ctx.getBean(SingerValidationService.class);
            var singer = new Singer();
            singer.setFirstName("J");
            singer.setLastName("Mayer");
            singer.setGenre(null);
            singer.setGender(null);
            var violations = singerBeanValidationService.validateSinger(singer);
            assertEquals(2, violations.size());
            listViolations(violations);
        }
    }

    @Test
    void testCountrySingerValidation() {
        try (var ctx = new AnnotationConfigApplicationContext(JakartaValidationCfg.class)) {
            var singerBeanValidationService = ctx.getBean(SingerValidationService.class);
            var singer = new Singer();
            singer.setFirstName("John");
            singer.setLastName("Mayer");
            singer.setGenre(Singer.Genre.COUNTRY);
            singer.setGender(null);
            var violations = singerBeanValidationService.validateSinger(singer);
            assertEquals(1, violations.size());
            listViolations(violations);
        }
    }

    @Test
    void testCountrySingerTwoValidation() {
        try (var ctx = new AnnotationConfigApplicationContext(JakartaValidationCfg.class)) {
            var singerBeanValidationService = ctx.getBean(SingerTwoValidationService.class);
            var singer = new SingerTwo();
            singer.setFirstName("John");
            singer.setLastName("Mayer");
            singer.setGenre(Singer.Genre.COUNTRY);
            singer.setGender(null);
            var violations = singerBeanValidationService.validateSinger(singer);
            assertEquals(1, violations.size());
            violations.forEach(violation ->
                    LOGGER.info("Validation error for property: {} with value: {} with error message: {}" ,
                            violation.getPropertyPath(), violation.getInvalidValue(), violation.getMessage()));
        }
    }

    private static void listViolations(Set<ConstraintViolation<Singer>> violations) {
        violations.forEach(violation ->
            LOGGER.info("Validation error for property: {} with value: {} with error message: {}" ,
                    violation.getPropertyPath(), violation.getInvalidValue(), violation.getMessage()));
    }
}
