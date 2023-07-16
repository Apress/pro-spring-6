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

import com.apress.prospring6.five.advice.AfterAdviceV1;
import com.apress.prospring6.five.advice.AfterReturningAdviceV1;
import com.apress.prospring6.five.advice.AfterThrowingAdviceV1;
import com.apress.prospring6.five.advice.AfterThrowingAdviceV2;
import com.apress.prospring6.five.advice.AroundAdviceV1;
import com.apress.prospring6.five.advice.AroundAdviceV2;
import com.apress.prospring6.five.advice.BeforeAdviceV1;
import com.apress.prospring6.five.advice.BeforeAdviceV2;
import com.apress.prospring6.five.advice.BeforeAdviceV3;
import com.apress.prospring6.five.advice.BeforeAdviceV4;
import com.apress.prospring6.five.advice.BeforeAdviceV5;
import com.apress.prospring6.five.advice.BeforeAdviceV6;
import com.apress.prospring6.five.advice.BeforeAdviceV7;
import com.apress.prospring6.five.common.Guitar;
import com.apress.prospring6.five.common.RejectedInstrumentException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by iuliana.cosmina on 18/04/2022
 */
public class AnnotatedAdviceTest {
    private static Logger LOGGER = LoggerFactory.getLogger(AnnotatedAdviceTest.class);

    @Test
    void testBeforeAdviceV1(){
        var ctx = new AnnotationConfigApplicationContext();
        ctx.register(AspectJAopConfig.class, BeforeAdviceV1.class);
        ctx.refresh();
        assertTrue(Arrays.asList(ctx.getBeanDefinitionNames()).contains("beforeAdviceV1"));

        NewDocumentarist documentarist = ctx.getBean("documentarist", NewDocumentarist.class);
        documentarist.execute();
        ctx.close();
    }

    @Test
    void testBeforeAdviceV2(){
        var ctx = new AnnotationConfigApplicationContext();
        ctx.register(AspectJAopConfig.class, BeforeAdviceV2.class);
        ctx.refresh();
        assertTrue(Arrays.asList(ctx.getBeanDefinitionNames()).contains("beforeAdviceV2"));

        var documentarist = ctx.getBean("documentarist", NewDocumentarist.class);
        documentarist.execute();
        ctx.close();
    }

    @Test
    void testBeforeAdviceV3(){
        var ctx = new AnnotationConfigApplicationContext();
        ctx.register(AspectJAopConfig.class, BeforeAdviceV3.class);
        ctx.refresh();
        assertTrue(Arrays.asList(ctx.getBeanDefinitionNames()).contains("beforeAdviceV3"));

        var documentarist = ctx.getBean("documentarist", NewDocumentarist.class);
        documentarist.execute();
        ctx.close();
    }

    @Test
    void testBeforeAdviceV4(){
        var ctx = new AnnotationConfigApplicationContext();
        ctx.register(AspectJAopConfig.class, BeforeAdviceV4.class);
        ctx.refresh();
        assertTrue(Arrays.asList(ctx.getBeanDefinitionNames()).contains("beforeAdviceV4"));

        var documentarist = ctx.getBean("documentarist", NewDocumentarist.class);
        documentarist.execute();
        ctx.close();
    }

    @Test
    void testAroundAdviceV1(){
        var ctx = new AnnotationConfigApplicationContext();
        ctx.register(AspectJAopConfig.class, AroundAdviceV1.class);
        ctx.refresh();
        assertTrue(Arrays.asList(ctx.getBeanDefinitionNames()).contains("aroundAdviceV1"));

        var documentarist = ctx.getBean("documentarist", NewDocumentarist.class);
        documentarist.execute();
        ctx.close();
    }

    @Test
    void testAroundAdviceV2(){
        var ctx = new AnnotationConfigApplicationContext();
        ctx.register(AspectJAopConfig.class, CommandingDocumentarist.class, AroundAdviceV2.class);
        ctx.refresh();
        assertTrue(Arrays.asList(ctx.getBeanDefinitionNames()).contains("aroundAdviceV2"));

        var documentarist = ctx.getBean("commandingDocumentarist", CommandingDocumentarist.class);
        documentarist.execute();
        ctx.close();
    }

    @Test
    void testAfterAdviceV1(){
        var ctx = new AnnotationConfigApplicationContext();
        ctx.register(AspectJAopConfig.class, AfterAdviceV1.class);
        ctx.refresh();
        assertTrue(Arrays.asList(ctx.getBeanDefinitionNames()).contains("afterAdviceV1"));

        var guitar = new Guitar();
        var guitarist = ctx.getBean("agustin", PretentiosGuitarist.class);
        guitarist.sing(guitar);
        LOGGER.info("-------------------");
        guitar.setBrand("Musicman");

        assertThrows(IllegalArgumentException.class, () -> guitarist.sing(guitar), "Unacceptable guitar!");
        ctx.close();
    }

    @Test
    void testAfterReturningAdviceV1(){
        var ctx = new AnnotationConfigApplicationContext();
        ctx.register(AspectJAopConfig.class, AfterReturningAdviceV1.class);
        ctx.refresh();
        assertTrue(Arrays.asList(ctx.getBeanDefinitionNames()).contains("afterReturningAdviceV1"));

        var guitar = new Guitar();
        var guitarist = ctx.getBean("agustin", PretentiosGuitarist.class);
        guitarist.sing(guitar);
        LOGGER.info("-------------------");
        guitar.setBrand("Musicman");

        assertThrows(IllegalArgumentException.class, () -> guitarist.sing(guitar), "Unacceptable guitar!");
        ctx.close();
    }

    @Test
    void testAfterThrowingAdviceV1(){
        var ctx = new AnnotationConfigApplicationContext();
        ctx.register(AspectJAopConfig.class, AfterThrowingAdviceV1.class);
        ctx.refresh();
        assertTrue(Arrays.asList(ctx.getBeanDefinitionNames()).contains("afterThrowingAdviceV1"));

        var guitar = new Guitar();
        var guitarist = ctx.getBean("agustin", PretentiosGuitarist.class);
        guitarist.sing(guitar);
        LOGGER.info("-------------------");
        guitar.setBrand("Musicman");

        assertThrows(IllegalArgumentException.class, () -> guitarist.sing(guitar), "Unacceptable guitar!");
        ctx.close();
    }

    @Test
    void testAfterThrowingAdviceV2(){
        var ctx = new AnnotationConfigApplicationContext();
        ctx.register(AspectJAopConfig.class, AfterThrowingAdviceV2.class);
        ctx.refresh();
        assertTrue(Arrays.asList(ctx.getBeanDefinitionNames()).contains("afterThrowingAdviceV2"));

        var guitar = new Guitar();
        var guitarist = ctx.getBean("agustin", PretentiosGuitarist.class);
        guitarist.sing(guitar);
        LOGGER.info("-------------------");
        guitar.setBrand("Musicman");

        assertThrows(RejectedInstrumentException.class, () -> guitarist.sing(guitar), "Unacceptable guitar!");
        ctx.close();
    }

    @Test
    void testAfterThrowingAdviceV5(){
        var ctx = new AnnotationConfigApplicationContext();
        ctx.register(AspectJAopConfig.class, BeforeAdviceV5.class);
        ctx.refresh();
        assertTrue(Arrays.asList(ctx.getBeanDefinitionNames()).contains("beforeAdviceV5"));

        var johnMayer = ctx.getBean("johnMayer", GrammyGuitarist.class);
        johnMayer.sing(new Guitar());

        var pretentiousGuitarist = ctx.getBean("agustin", PretentiosGuitarist.class);
        pretentiousGuitarist.sing(new Guitar());

        ctx.close();
    }

    @Test
    void testAfterThrowingAdviceV6(){
        var ctx = new AnnotationConfigApplicationContext();
        ctx.register(AspectJAopConfig.class, BeforeAdviceV6.class);
        ctx.refresh();
        assertTrue(Arrays.asList(ctx.getBeanDefinitionNames()).contains("beforeAdviceV6"));

        var johnMayer = ctx.getBean("johnMayer", GrammyGuitarist.class);
        johnMayer.sing(new Guitar());

        var pretentiousGuitarist = ctx.getBean("agustin", PretentiosGuitarist.class);
        pretentiousGuitarist.sing(new Guitar());

        ctx.close();
    }

    @Test
    void testAfterThrowingAdviceV7(){
        var ctx = new AnnotationConfigApplicationContext();
        ctx.register(AspectJAopConfig.class, BeforeAdviceV7.class);
        ctx.refresh();
        assertTrue(Arrays.asList(ctx.getBeanDefinitionNames()).contains("beforeAdviceV7"));

        var johnMayer = ctx.getBean("johnMayer", GrammyGuitarist.class);
        johnMayer.sing(new Guitar());

        var pretentiousGuitarist = ctx.getBean("agustin", PretentiosGuitarist.class);
        pretentiousGuitarist.sing(new Guitar());

        var gretsch = new Guitar();
        gretsch.setBrand("Gretsch");
        johnMayer.sing(gretsch);
        pretentiousGuitarist.sing(gretsch);

        ctx.close();
    }
}
