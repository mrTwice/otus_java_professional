package ru.otus.java.professional.yampolskiy.multithreading;

public interface PrioritizedRunnable extends Runnable, Comparable<PrioritizedRunnable> {
    int getPriority();
}

