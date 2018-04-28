package org.clc.utils;

import java.util.concurrent.Callable;

public class Job implements Callable {

    @Override
    public Object call() {
        return Thread.currentThread().getName();
    }

}
