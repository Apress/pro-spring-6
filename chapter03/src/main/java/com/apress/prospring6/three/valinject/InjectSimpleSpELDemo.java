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
package com.apress.prospring6.three.valinject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import static java.lang.System.out;

/**
 * Created by iuliana.cosmina on 06/03/2022
 */
@Component("injectSimpleSpEL")
public class InjectSimpleSpELDemo {
    @Value("#{injectSimpleConfig.name.toUpperCase()}")
    private String name;

    @Value("#{injectSimpleConfig.age + 1}")
    private int age;

    @Value("#{injectSimpleConfig.height}")
    private float height;

    @Value("#{injectSimpleConfig.developer}")
    private boolean developer;

    @Value("#{injectSimpleConfig.ageInSeconds}")
    private Long ageInSeconds;

    public static void main(String... args) {
        var ctx = new AnnotationConfigApplicationContext();
        ctx.register(InjectSimpleConfig.class, InjectSimpleSpELDemo.class);
        ctx.refresh();

        InjectSimpleSpELDemo simple = (InjectSimpleSpELDemo) ctx.getBean("injectSimpleSpEL");
        out.println(simple);
    }

    public String toString() {
        return "Name: " + name + "\n"
                + "Age: " + age + "\n"
                + "Age in Seconds: " + ageInSeconds + "\n"
                + "Height: " + height + "\n"
                + "Is Developer?: " + developer;
    }

}


@Component("injectSimpleConfig")
class InjectSimpleConfig {

    private String name = "John Mayer";
    private int age = 40;
    private float height = 1.92f;
    private boolean developer = false;
    private Long ageInSeconds = 1_241_401_112L;

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public float getHeight() {
        return height;
    }

    public boolean isDeveloper() {
        return developer;
    }

    public Long getAgeInSeconds() {
        return ageInSeconds;
    }
}