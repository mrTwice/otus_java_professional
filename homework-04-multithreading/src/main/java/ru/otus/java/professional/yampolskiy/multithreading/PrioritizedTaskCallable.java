package ru.otus.java.professional.yampolskiy.multithreading;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class PrioritizedTaskCallable<T> extends FutureTask<T> implements PrioritizedRunnable {
    private final int priority;

    public PrioritizedTaskCallable(Callable<T> callable, int priority) {
        super(callable);
        this.priority = priority;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(PrioritizedRunnable other) {
        return Integer.compare(other.getPriority(), this.getPriority());
    }
}
