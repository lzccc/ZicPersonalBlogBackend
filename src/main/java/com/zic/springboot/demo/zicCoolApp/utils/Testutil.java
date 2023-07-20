package com.zic.springboot.demo.zicCoolApp.utils;

import java.util.PriorityQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Testutil {
    public static void main(String[] args) throws Exception {
        // 使用Runnable接口创建一个任务
        Runnable runnableTask = () -> {
            System.out.println("Runnable task is running");
        };

        // 使用Callable接口创建一个任务
        Callable<String> callableTask = () -> {
            System.out.println("Callable task is running");
            return "Callable task result";
        };

        // 执行Runnable任务
        Thread thread = new Thread(runnableTask);
        thread.start();
        thread.join();

        // 执行Callable任务
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(callableTask);
        String result = future.get();
        System.out.println("Callable task result: " + result);

        executor.shutdown();
    }
}
