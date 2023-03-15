package com.kk.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorUtil {
    public static ExecutorService executorService
            = new ThreadPoolExecutor
            (20,
            30,
            1L,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10));
}
