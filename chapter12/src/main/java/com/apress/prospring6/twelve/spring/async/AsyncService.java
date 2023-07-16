package com.apress.prospring6.twelve.spring.async;

import java.util.concurrent.Future;

public interface AsyncService {
    void asyncTask();
    Future<String> asyncWithReturn(String name);
}
