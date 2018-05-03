package org.clc.utils;

import java.util.concurrent.Callable;

public class Job implements Callable {

    private Object task;

    public Job(Object task) {
        this.task = task;
    }

    @Override
    public Object call() {
        System.out.println(Thread.currentThread().getName());
        return task;
    }

}
