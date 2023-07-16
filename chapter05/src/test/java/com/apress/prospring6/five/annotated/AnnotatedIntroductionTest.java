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
package com.apress.prospring6.five.annotated;

import com.apress.prospring6.five.common.Guitar;
import com.apress.prospring6.five.common.Performer;
import com.apress.prospring6.five.common.Singer;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by iuliana.cosmina on 24/04/2022
 */
public class AnnotatedIntroductionTest {
    private static Logger LOGGER = LoggerFactory.getLogger(AnnotatedIntroductionTest.class);

    @Test
    void testAnnotatedIntroduction() {
        var ctx = new AnnotationConfigApplicationContext();
        ctx.register(AspectJAopConfig.class, AnnotatedIntroduction.class);
        ctx.refresh();
        assertTrue(Arrays.asList(ctx.getBeanDefinitionNames()).contains("annotatedIntroduction"));

        var guitar = new Guitar();
        var guitarist = ctx.getBean("agustin", PretentiosGuitarist.class);

        assertTrue(guitarist instanceof Singer);
        guitarist.sing(guitar);

        LOGGER.info("Proxy type: {} ", guitar.getClass().getName());

        assertTrue(guitarist instanceof Performer);
        Performer performer  = (Performer)guitarist;
        performer.perform();

        ctx.close();
    }
}
