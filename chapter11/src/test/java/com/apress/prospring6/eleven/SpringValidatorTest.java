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

import com.apress.prospring6.eleven.domain.Address;
import com.apress.prospring6.eleven.domain.Blogger;
import com.apress.prospring6.eleven.domain.BloggerWithAddress;
import com.apress.prospring6.eleven.formatter.FormattingServiceCfg;
import com.apress.prospring6.eleven.validator.*;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ValidationUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by iuliana on 28/08/2022
 */
public class SpringValidatorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringValidatorTest.class);


    @Test
    void testSimpleBloggerValidator() throws MalformedURLException {
        try (var ctx = new AnnotationConfigApplicationContext(AppConfig.class,  FormattingServiceCfg.class, SimpleBloggerValidator.class)) {
            var blogger = new Blogger("", "Pedala", LocalDate.of(2000, 1, 1), new URL("https://none.co.uk"));
            var blogger2 = new Blogger(null, "Pedala", LocalDate.of(2000, 1, 1), new URL("https://none.co.uk"));

            var bloggerValidator = ctx.getBean(SimpleBloggerValidator.class);
            var result = new BeanPropertyBindingResult(blogger, "blogger");
            ValidationUtils.invokeValidator(bloggerValidator, blogger, result);

            var errors = result.getAllErrors();
            assertEquals(1, errors.size());
            errors.forEach(e -> LOGGER.info("Object '{}' failed validation. Error code: {}", e.getObjectName(), e.getCode()));
            // ----------------'null' passes the validation---------------------
            var result2 = new BeanPropertyBindingResult(blogger2, "blogger2");
            ValidationUtils.invokeValidator(bloggerValidator, blogger2, result);

            var errors2 = result2.getAllErrors();
            assertEquals(0, errors2.size());
        }
    }

    @Test
    void testBloggerValidator() throws MalformedURLException {
        try (var ctx = new AnnotationConfigApplicationContext(AppConfig.class,  FormattingServiceCfg.class, BloggerValidator.class)) {
            var blogger = new Blogger(null, "Pedala", LocalDate.of(2000, 1, 1), new URL("https://none.co.uk"));

            var bloggerValidator = ctx.getBean(BloggerValidator.class);
            var result = new BeanPropertyBindingResult(blogger, "blogger");

            ValidationUtils.invokeValidator(bloggerValidator, blogger, result);

            var errors = result.getAllErrors();
            assertEquals(1, errors.size());
            errors.forEach(e -> LOGGER.info("Error Code: {}", e.getCode()));
        }
    }

    @Test
    void testComplexBloggerValidator() throws MalformedURLException {
        try (var ctx = new AnnotationConfigApplicationContext(AppConfig.class,  FormattingServiceCfg.class, ComplexBloggerValidator.class)) {
            var blogger = new Blogger(null, null, LocalDate.of(1973, 1, 1), new URL("https://none.co.uk"));

            var bloggerValidator = ctx.getBean(ComplexBloggerValidator.class);
            var result = new BeanPropertyBindingResult(blogger, "blogger");

            ValidationUtils.invokeValidator(bloggerValidator, blogger, result);

            var errors = result.getAllErrors();
            assertEquals(3, errors.size());
            errors.forEach(e -> LOGGER.info("Error Code: {}", e.getCode()));
        }
    }

    @Test
    void testBloggerWithAddressValidator() throws MalformedURLException {
        try (var ctx = new AnnotationConfigApplicationContext(AppConfig.class,  FormattingServiceCfg.class, AddressValidator.class, BloggerWithAddressValidator.class)) {
            var address = new Address("221B", "UK");
            var blogger = new BloggerWithAddress(null, "Mazzie", LocalDate.of(1973, 1, 1), null, address);

            var bloggerValidator = ctx.getBean(BloggerWithAddressValidator.class);
            var result = new BeanPropertyBindingResult(blogger, "blogger");

            ValidationUtils.invokeValidator(bloggerValidator, blogger, result);

            var errors = result.getAllErrors();
            assertEquals(2, errors.size());
            errors.forEach(e -> LOGGER.info("Error Code: {}", e.getCode()));
        }
    }
}
