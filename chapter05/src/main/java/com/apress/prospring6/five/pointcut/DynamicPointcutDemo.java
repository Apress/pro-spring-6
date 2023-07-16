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
package com.apress.prospring6.five.pointcut;

import com.apress.prospring6.five.common.GoodGuitarist;
import com.apress.prospring6.five.common.SimpleAroundAdvice;
import com.apress.prospring6.five.common.Singer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.DynamicMethodMatcherPointcut;

import java.lang.reflect.Method;

/**
 * Created by iuliana.cosmina on 10/04/2022
 */
class SimpleDynamicPointcut extends DynamicMethodMatcherPointcut {
    private static Logger logger = LoggerFactory.getLogger(SimpleDynamicPointcut.class);

    @Override
    public ClassFilter getClassFilter() {
        return cls -> (cls == GoodGuitarist.class);
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        logger.debug("Static check for " + method.getName());
        return ("sing".equals(method.getName()));
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass, Object... args) {
        logger.debug("Dynamic check for " + method.getName());

        if(args.length == 0) {
            return false;
        }
        var key = (String) args[0];

        return key.equalsIgnoreCase("C");
    }
}

public class DynamicPointcutDemo {
    public static void main(String... args) {
        GoodGuitarist target = new GoodGuitarist();
        Advisor advisor = new DefaultPointcutAdvisor(new SimpleDynamicPointcut(), new SimpleAroundAdvice());
        ProxyFactory pf = new ProxyFactory();
        pf.setTarget(target);
        pf.addAdvisor(advisor);

        Singer proxy = (Singer)pf.getProxy();

        proxy.sing("C");
        proxy.sing("c");
        proxy.sing("E");

        proxy.sing();
    }
}

