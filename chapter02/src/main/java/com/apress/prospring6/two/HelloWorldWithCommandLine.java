package com.apress.prospring6.two;

public class HelloWorldWithCommandLine {

    public static void main(String... args) {
        if (args.length > 0) {
            System.out.println(args[0]);
        } else {
            System.out.println("Hello World!");
        }
    }
}
