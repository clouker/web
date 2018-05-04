package org.clc.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.Callable;

public class Job implements Callable {

    private Object task;

    @Override
    public Object call() {
            System.out.println("111111111----" + 1 / 0);
        System.out.println(LocalDateTime.now() + " : " + Thread.currentThread().getName());
        return null;
    }
}
