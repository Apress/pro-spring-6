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
package com.apress.prospring6.five;

import com.apress.prospring6.five.common.AuditAdvice;
import com.apress.prospring6.five.common.Documentarist;
import com.apress.prospring6.five.common.GrammyGuitarist;
import org.aopalliance.aop.Advice;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by iuliana.cosmina on 17/04/2022
 */

@Configuration
class AopConfig implements BeanFactoryAware {

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Bean
    public GrammyGuitarist johnMayer(){
        return new GrammyGuitarist();
    }

    @Bean
    public Advice advice(){
        return new AuditAdvice();
    }

    @Bean
    public GrammyGuitarist proxyOne(){
        ProxyFactoryBean pfb = new ProxyFactoryBean();
        pfb.setProxyTargetClass(true);
        pfb.setTarget(johnMayer());
        pfb.setInterceptorNames("advice");
        pfb.setBeanFactory(beanFactory);
        pfb.setFrozen(true);
        return (GrammyGuitarist) pfb.getObject();
    }

    @Bean
    public Documentarist documentaristOne() {
        Documentarist documentarist = new Documentarist();
        documentarist.setDep(proxyOne());
        return documentarist;
    }

    @Bean
    public GrammyGuitarist proxyTwo(){
        ProxyFactoryBean pfb = new ProxyFactoryBean();
        pfb.setProxyTargetClass(true);
        pfb.setTarget(johnMayer());
        pfb.setInterceptorNames("advisor");
        pfb.setBeanFactory(beanFactory);
        pfb.setFrozen(true);
        return (GrammyGuitarist) pfb.getObject();
    }

    @Bean
    public Documentarist documentaristTwo(){
        Documentarist documentarist = new Documentarist();
        documentarist.setDep(proxyTwo());
        return documentarist;
    }

    @Bean
    public DefaultPointcutAdvisor advisor(){
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setAdvice(advice());
        AspectJExpressionPointcut pc = new AspectJExpressionPointcut();
        pc.setExpression("execution(* sing*(..))");
        advisor.setPointcut(pc);
        return advisor;
    }
}

public class ProxyFactoryBeanDemo {
    public static void main(String... args) {
        var ctx = new AnnotationConfigApplicationContext(AopConfig.class);

        Documentarist documentaristOne =
                ctx.getBean("documentaristOne", Documentarist.class);
        Documentarist documentaristTwo =
                ctx.getBean("documentaristTwo", Documentarist.class);

        System.out.println("Documentarist One >>");
        documentaristOne.execute();

        System.out.println("\nDocumentarist Two >> ");
        documentaristTwo.execute();
    }
}
