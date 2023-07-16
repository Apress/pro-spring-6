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
package com.apress.prospring6.five.introduction;

import java.io.Serial;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.DelegatingIntroductionInterceptor;

/**
 * Created by iuliana.cosmina on 17/04/2022
 */
public class IsModifiedMixin extends DelegatingIntroductionInterceptor implements IsModified {
    @Serial
    private static final long serialVersionUID = 2L;

    private boolean isModified = false;
    private final Map<Method, Method> methodCache = new HashMap<>();
    private final Predicate<MethodInvocation> isSetter = invocation ->
            invocation.getMethod().getName().startsWith("set") && (invocation.getArguments().length == 1);

    @Override
    public boolean isModified() {
        return isModified;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (!isModified) {
            if (isSetter.test(invocation)) {
                Method getter = getGetter(invocation.getMethod());
                if (getter != null) {
                    Object newVal = invocation.getArguments()[0];
                    Object oldVal = getter.invoke(invocation.getThis());
                    if (newVal == null && oldVal == null) {
                        isModified = false;
                    } else if ((newVal == null && oldVal != null) || (newVal != null && oldVal == null)) {
                        isModified = true;
                    }  else {
                        isModified = !newVal.equals(oldVal);
                    }
                }
            }
        }
        return super.invoke(invocation);
    }

    private Method getGetter(Method setter) {
        Method getter = methodCache.get(setter);
        if (getter != null) {
            return getter;
        }
        String getterName = setter.getName().replaceFirst("set", "get");
        try {
            getter = setter.getDeclaringClass().getMethod(getterName);
            synchronized (methodCache) {
                methodCache.put(setter, getter);
            }
            return getter;
        } catch (NoSuchMethodException ex) {
            return null;
        }
    }
}
