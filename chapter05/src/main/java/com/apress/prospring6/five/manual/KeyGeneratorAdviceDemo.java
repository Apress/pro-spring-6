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
package com.apress.prospring6.five.manual;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.framework.ProxyFactory;

import java.lang.reflect.Method;
import java.util.Random;

/**
 * Created by iuliana.cosmina on 09/04/2022
 * Example showing an after-returning advice that throws an exception.
 * It is possible for a cryptographic key generator to generate a key
 * that is considered weak for a particular algorithm. Ideally, the key generator would check for these weak
 * keys, but since the chance of these keys arising is often very small, many generators do not check. By using
 * after-returning advice, we can advise the method that generates the key and performs this additional check.
 */
public class KeyGeneratorAdviceDemo {
    private static Logger LOGGER = LoggerFactory.getLogger(KeyGeneratorAdviceDemo.class);

    /**
     * As you can see, the KeyGenerator class sometimes generates weak keys, as expected, and
     * WeakKeyCheckAdvice ensures that SecurityException is raised whenever a weak key is encountered.
     * @param args
     */
    public static void main(String... args) {
        KeyGenerator keyGen = getKeyGenerator();
        for(int x = 0; x < 10; x++) {
            try {
                long key = keyGen.getKey();
                LOGGER.info("Key: " + key);
            } catch(SecurityException ex) {
                LOGGER.error("Weak Key Generated!"); // we swallow the exception for a cleaner output
            }
        }
    }

    private static KeyGenerator getKeyGenerator() {
        KeyGenerator target = new KeyGenerator();

        ProxyFactory factory = new ProxyFactory();
        factory.setTarget(target);
        factory.addAdvice(new WeakKeyCheckAdvice());
        return (KeyGenerator)factory.getProxy();
    }
}

class KeyGenerator {
    protected static final long WEAK_KEY = 0xFFFFFFF0000000L;
    protected static final long STRONG_KEY = 0xACDF03F590AE56L;

    private Random rand = new Random();
    public long getKey() {
        int x = rand.nextInt(3);
        if (x == 1) {
            return WEAK_KEY;
        }
        return STRONG_KEY;
    }
}

class WeakKeyCheckAdvice implements AfterReturningAdvice {

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args,Object target) throws Throwable {
        if ((target instanceof KeyGenerator) && ("getKey".equals(method.getName()))) {
            long key = (Long) returnValue;
            if (key == KeyGenerator.WEAK_KEY) {
                throw new SecurityException("Key Generator generated a weak key. Try again");
            }
        }
    }
}