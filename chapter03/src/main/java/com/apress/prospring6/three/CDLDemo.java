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
package com.apress.prospring6.three;

import static java.lang.System.out;

interface ManagedComponent {
    void performLookup(Container container);
}

interface Container {
    Object getDependency(String key);
}

class DefaultContainer implements Container {
    @Override
    public Object getDependency(String key) {
        if("provider".equals(key)) {
            return new HelloWorldMessageProvider();
        }

        throw new RuntimeException("Unknown dependency: " + key);
    }
}

public class CDLDemo {
    public static void main(String... args) {
        Container container = new DefaultContainer();

        MessageRenderer renderer = new StandardOutMessageRenderer();
        renderer.performLookup(container);

        renderer.render();
    }
}

interface MessageProvider {
    String getMessage();
}

class HelloWorldMessageProvider implements MessageProvider {

    public HelloWorldMessageProvider(){
        System.out.println(" --> HelloWorldMessageProvider: constructor caled");
    }

    @Override
    public String getMessage() {
        return "Hello World!";
    }
}

interface MessageRenderer extends ManagedComponent {
    void render();
}

class StandardOutMessageRenderer implements MessageRenderer {

    private MessageProvider messageProvider;

    public StandardOutMessageRenderer(){
        out.println(" --> StandardOutMessageRenderer: constructor called");
    }

    @Override
    public void performLookup(Container container) {
        this.messageProvider = (MessageProvider) container.getDependency("provider");
    }

    @Override
    public String toString() {
        return messageProvider.toString();
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