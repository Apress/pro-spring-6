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
package com.apress.prospring6.five.advice;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by iuliana.cosmina on 18/04/2022
 */
@Component
@Aspect
public class AnnotatedAdvice {
    private static Logger LOGGER = LoggerFactory.getLogger(AnnotatedAdvice.class);

    @Pointcut("execution(* com.apress.prospring6.five..sing*(com.apress.prospring6.five.common.Guitar))")
    public void singExecution() {}

    @Before("singExecution()")
    public void simpleBeforeAdvice(JoinPoint joinPoint) {
            LOGGER.info("Executing: " +joinPoint.getSignature().getDeclaringTypeName() + " "
                    + joinPoint.getSignature().getName());
        }
    }


    /*



    @Pointcut("bean(john*)")
    public void isJohn() {}

    @Before("singExecution(value) && isJohn()")
    public void simpleBeforeAdvice(JoinPoint joinPoint, Guitar value) {
        if(value.getBrand().equals("Gibson")) {
            LOGGER.info("Executing: " +joinPoint.getSignature().getDeclaringTypeName() + " "
                    + joinPoint.getSignature().getName() + " argument: " + value.getBrand());
        }
    }

    @Around("singExecution(value) && isJohn()")
    public Object simpleAroundAdvice(ProceedingJoinPoint pjp, Guitar value) throws Throwable {
        LOGGER.info("Before execution: " +
                pjp.getSignature().getDeclaringTypeName() + " "
                + pjp.getSignature().getName()
                + " argument: " + value.getBrand());
        Object retVal = pjp.proceed();
        LOGGER.info("After execution: " +
                pjp.getSignature().getDeclaringTypeName() + " "
                + pjp.getSignature().getName()
                + " argument: " + value.getBrand());
        return retVal;
    }*/

