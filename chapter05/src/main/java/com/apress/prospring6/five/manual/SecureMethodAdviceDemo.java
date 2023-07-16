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
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.ProxyFactory;

import java.lang.reflect.Method;

/**
 * Created by iuliana.cosmina on 09/04/2022
 * Example that allows users to authenticate with any password, and it also allows only a single, hard-coded user access to the secured methods. However, it
 * does illustrate how easy it is to use AOP to implement a crosscutting concern such as security.
 * This is a demonstration for before advice.
 */
public class SecureMethodAdviceDemo {
    private static Logger LOGGER = LoggerFactory.getLogger(SecureMethodAdviceDemo.class);

    public static void main(String... args) {
        SimpleSecurityManager mgr = new SimpleSecurityManager();
        LOGGER.info("---- Successful access to message of SecureBean ---- ");
        SecureBean bean = getSecureBean();
        mgr.login("John", "pwd");
        bean.writeSecureMessage();
        mgr.logout();

        try {
            LOGGER.info("---- Prohibited access to message of SecureBean ---- ");
            mgr.login("invalid user", "pwd");
            bean.writeSecureMessage();
        } catch(SecurityException ex) {
            LOGGER.error("Exception Caught: {}",  ex.getMessage());
        } finally {
            mgr.logout();
        }
        try {
            LOGGER.info("----  No user logged in. Prohibited access to message of SecureBean ---- ");
            bean.writeSecureMessage();
        } catch(SecurityException ex) {
            LOGGER.error("Exception Caught: {}", ex.getMessage());
        }
    }

    private static SecureBean getSecureBean() {
        SecureBean target = new SecureBean();
        SecurityAdvice advice = new SecurityAdvice();
        ProxyFactory factory = new ProxyFactory();
        factory.setTarget(target);
        factory.addAdvice(advice);
        SecureBean proxy = (SecureBean)factory.getProxy();
        return proxy;
    }
}

class SecureBean {
    private static Logger LOGGER = LoggerFactory.getLogger(SecureBean.class);

    public void writeSecureMessage() {
        LOGGER.debug("""
                Every time I learn something new, 
                it pushes some old stuff out of my brain
                """ );
    }
}

class SecurityAdvice implements MethodBeforeAdvice {
    private static Logger LOGGER = LoggerFactory.getLogger(SecurityAdvice.class);

    private SimpleSecurityManager securityManager;

    public SecurityAdvice() {
        this.securityManager = new SimpleSecurityManager();
    }

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        UserInfo user = securityManager.getLoggedOnUser();
        if (user == null) {
            LOGGER.debug("No user authenticated");
            throw new SecurityException("You must login before attempting to invoke the method: " + method.getName());
        } else if ("John".equals(user.getUserName())) {
            LOGGER.debug("Logged in user is John - OKAY!");
        } else {
            LOGGER.debug("Logged in user is {} NOT GOOD :(", user.getUserName());
            throw new SecurityException("User " + user.getUserName() + " is not allowed access to method " + method.getName());
        }
    }
}

class UserInfo {
    private String userName;
    private String password;
    public UserInfo(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
    public String getPassword() {
        return password;
    }
    public String getUserName() {
        return userName;
    }
}

/**
 * The application uses the SecurityManager class to authenticate a user and, later, to retrieve the details
 * of the currently authenticated user.
 */
class SimpleSecurityManager {
    private static ThreadLocal<UserInfo>
            threadLocal = new ThreadLocal<>();
    public void login(String userName, String password) {
        threadLocal.set(new UserInfo(userName, password));
    }
    public void logout() {
        threadLocal.set(null);
    }
    public UserInfo getLoggedOnUser() {
        return threadLocal.get();
    }
}