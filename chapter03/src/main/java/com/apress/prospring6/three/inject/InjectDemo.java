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
package com.apress.prospring6.three.inject;

import com.apress.prospring6.two.decoupled.MessageProvider;
import com.apress.prospring6.two.decoupled.MessageRenderer;
import jakarta.inject.Inject;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import static java.lang.System.out;

/**
 * Created by iuliana.cosmina on 05/03/2022
 * I made the decision to put all classes related to this example in the same file.
 * It is easier to navigate the project, also you have all the components in the same file,
 * so there is no doubt where the bean definitions are coming from.
 */
public class InjectDemo {

    public static void main(String... args) {
        var ctx =
                new AnnotationConfigApplicationContext(HelloWorldConfiguration.class);
        var mr = ctx.getBean("renderer", MessageRenderer.class);
        mr.render();
    }
}

// --- Java configuration class  ---
@Configuration
@ComponentScan(basePackages = "com.apress.prospring6.three.inject")
class HelloWorldConfiguration {
}

//  --- bean definitions using @Component and @Inject ---
//simple bean
@Component("provider")
class HelloWorldMessageProvider implements MessageProvider {

    @Override
    public String getMessage() {
        return "Hello World!";
    }
}

//complex bean requiring a dependency
@Component("renderer")
class StandardOutMessageRenderer implements MessageRenderer {

    private MessageProvider messageProvider;

    @Override
    @Inject
    public void setMessageProvider(MessageProvider provider) {
        out.println(" ~~ Injecting dependency using setter & @Inject ~~");
        this.messageProvider = provider;
    }

    @Override
    public MessageProvider getMessageProvider() {
        return this.messageProvider;
    }

    @Override
    public void render() {
        if (messageProvider == null) {
            throw new RuntimeException(
                    "You must set the property messageProvider of class:"
                            + StandardOutMessageRenderer.class.getName());
        }
        out.println(messageProvider.getMessage());
    }
}
