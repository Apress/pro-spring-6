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

import com.apress.prospring6.eleven.domain.Blogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.net.URL;
import java.time.LocalDate;

/**
 * Created by iuliana on 17/08/2022
 */
@PropertySource("classpath:blogger.properties")
@Configuration
public class AppConfig {

    @Bean
    public Blogger awsBlogger(@Value("Alex") String firstName,
                              @Value("DeBrie") String lastName,
                              @Value("https://www.alexdebrie.com/") URL personalSite,
                              @Value("1980-01-02") LocalDate birthDate) throws Exception { // I really don't know when his birthday is ;)
        return new Blogger(firstName, lastName, birthDate, personalSite);
    }

    @Bean
    public Blogger springBlogger(@Value("${springBlogger.firstName}") String firstName,
                                 @Value("${springBlogger.lastName}") String lastName,
                                 @Value("${springBlogger.personalSite}") URL personalSite,
                                 @Value("${springBlogger.birthDate}") LocalDate birthDate) throws Exception {
        return new Blogger(firstName, lastName, birthDate, personalSite);
    }
}
