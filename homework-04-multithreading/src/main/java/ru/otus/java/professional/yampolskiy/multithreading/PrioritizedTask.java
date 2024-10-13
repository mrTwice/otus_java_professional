package ru.otus.java.professional.yampolskiy.multithreading;

public class PrioritizedTask  implements PrioritizedRunnable{
    private final Runnable task;
    private final int priority;

    public PrioritizedTask(Runnable task, int priority) {
        if(task == null) throw new NullPointerException("Задача не может быть Null");
        this.task = task;
        this.priority = priority;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(PrioritizedRunnable o) {
        return Integer.compare(o.getPriority(), this.getPriority());
    }

    @Override
    public void run() {
        task.run();
    }
}
