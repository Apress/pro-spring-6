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
package com.apress.prospring6.eighteen.boot.audit;

import com.apress.prospring6.eighteen.boot.entities.Singer;
import com.apress.prospring6.eighteen.boot.services.SingerService;
import org.springframework.jmx.export.annotation.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by iuliana on 21/03/2023
 */
@Component
@ManagedResource(description = "JMX managed resource", objectName = "jmxBootDemo:name=ProSpring6SingerApp")
public class AppStatisticsImpl implements AppStatistics{

    private final SingerService singerService;

    public AppStatisticsImpl(SingerService singerService) {
        this.singerService = singerService;
    }

    @ManagedAttribute(description = "Number of singers in the application")
    @Override
    public int getTotalSingerCount() {
        return singerService.findAll().size();
    }

    @ManagedOperation
    public String findJohn() {
        List<Singer> singers = singerService.findByFirstNameAndLastName("John", "Mayer");
        if (!singers.isEmpty()) {
            return singers.get(0).getFirstName() + " " + singers.get(0).getLastName() + " " + singers.get(0).getBirthDate();
        }
        return "not found";
    }

    @ManagedOperation(description="Find Singer by first name and last name")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "firstName", description = "Singer's first name"),
            @ManagedOperationParameter(name = "lastName", description = "Singer's last name")})
    public String findSinger(String firstName, String lastName) {
        List<Singer> singers = singerService.findByFirstNameAndLastName(firstName, lastName);
        if (!singers.isEmpty()) {
            return singers.get(0).getFirstName() + " " + singers.get(0).getLastName() + " " + singers.get(0).getBirthDate();
        }
        return "not found";
    }
}

