package com.apress.prospring6.five.boot.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by iuliana.cosmina on 18/04/2022
 */
@Component
@Aspect
public class SimpleBeforeAdvice {
    private static Logger LOGGER = LoggerFactory.getLogger(SimpleBeforeAdvice.class);
    @Pointcut("execution(* com.apress.prospring6.five..sing*(com.apress.prospring6.five.boot.aspect.Guitar))  && args(value)")
    public void singExecution(Guitar value) {
    }

    @Pointcut("bean(john*)")
    public void isJohn() {
    }

    @Before(value = "singExecution(guitar) && isJohn()", argNames = "joinPoint,guitar")
    public void simpleBeforeAdvice(JoinPoint joinPoint, Guitar guitar) {
        if(guitar.getBrand().equals("Gibson")) {
            var signature = (MethodSignature) joinPoint.getSignature();
            LOGGER.info(" > Executing: {} from {} with {}", signature.getName(), signature.getDeclaringTypeName(), guitar.getBrand());
        }
    }

}
