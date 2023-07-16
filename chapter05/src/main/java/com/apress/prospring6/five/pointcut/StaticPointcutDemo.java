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
import com.apress.prospring6.five.common.GreatGuitarist;
import com.apress.prospring6.five.common.SimpleAroundAdvice;
import com.apress.prospring6.five.common.Singer;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.lang.reflect.Method;

/**
 * Created by iuliana.cosmina on 10/04/2022
 */
class SimpleStaticPointcut extends StaticMethodMatcherPointcut {
    @Override
    public boolean matches(Method method, Class<?> cls) {
        return ("sing".equals(method.getName()));
    }

    @Override
    public ClassFilter getClassFilter() {
        return cls -> (cls == GoodGuitarist.class);
    }
}

public class StaticPointcutDemo {
    public static void main(String... args) {
        GoodGuitarist johnMayer = new GoodGuitarist();
        GreatGuitarist ericClapton = new GreatGuitarist();

        Singer proxyOne;
        Singer proxyTwo;

        Pointcut pc = new SimpleStaticPointcut();
        Advice advice = new SimpleAroundAdvice();
        Advisor advisor = new DefaultPointcutAdvisor(pc, advice);

        ProxyFactory pf = new ProxyFactory();
        pf.addAdvisor(advisor);
        pf.setTarget(johnMayer);
        proxyOne = (Singer)pf.getProxy();

        pf = new ProxyFactory();
        pf.addAdvisor(advisor);
        pf.setTarget(ericClapton);
        proxyTwo = (Singer)pf.getProxy();

        proxyOne.sing();
        proxyTwo.sing();
    }
}