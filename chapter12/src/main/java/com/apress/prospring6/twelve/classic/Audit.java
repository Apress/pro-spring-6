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
package com.apress.prospring6.twelve.classic;

import org.slf4j.Logger;

/**
 * Created by iuliana on 25/10/2022
 */
public class Audit {
    private Runtime runtime = Runtime.getRuntime();
    public final Logger LOGGER;
    private static final long MEGABYTE = 1024L * 1024L;
    private long prevTime = 0;

    public Audit(Logger logger) {
        this.LOGGER = logger;
    }

    public void printMemory(String message) {
        long memory = runtime.totalMemory() - runtime.freeMemory();
        StringBuilder sb = new StringBuilder(" --> ").append(Thread.currentThread().getName()).append(' ');
        sb = message == null ?
                sb.append("Occupied memory is ") :
                sb.append(message).append(' ').append(bytesToMegabytes(memory));
        sb.append("MB");
        LOGGER.debug(sb.toString());
    }

    public void printMemory() {
        printMemory(null);
    }

    public void printStats(String message) {
        long memory = runtime.totalMemory() - runtime.freeMemory();
        long currentTime = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder(" --> ").append(Thread.currentThread().getName()).append(' ');
        sb = message == null ?
                sb.append("Occupied memory is ") :
                sb.append(message).append(' ').append(bytesToMegabytes(memory));
        if (prevTime > 0) {
            float seconds = ((float) (currentTime - prevTime)) / 1000;
            prevTime = currentTime;
            sb.append("MB, process took: ").append(seconds).append(" seconds");
        } else {
            sb.append("MB");
            prevTime = currentTime;
        }
        LOGGER.debug(sb.toString());
    }

    public static long bytesToMegabytes(long bytes) {
        return bytes / MEGABYTE;
    }
}
