package ru.otus.java.professional.yampolskiy.multithreading;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;

public class Main {
    private static Random random = new Random();

    public static void main(String[] args) {
        ExecutorService threadPool = new PrioritizedThreadPoolExecutor(3);

        List<PrioritizedTask> list = new ArrayList<>();

        for (int i = 0; i < 1000; i++){
            int priority = random.nextInt(0, 100000);
            list.add(new PrioritizedTask(() ->{
                System.out.println("Задача с приоритетом"+ priority +" выполняется");
            }, priority));
        }

        list.forEach(threadPool::execute);
    }

}
