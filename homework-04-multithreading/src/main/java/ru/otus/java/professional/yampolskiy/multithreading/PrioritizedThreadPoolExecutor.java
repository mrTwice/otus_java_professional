package ru.otus.java.professional.yampolskiy.multithreading;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class PrioritizedThreadPoolExecutor implements ExecutorService {
    private final BlockingQueue<PrioritizedRunnable> taskQueue;
    private final List<Worker> workers;
    private final AtomicBoolean isShutdown;
    private final AtomicBoolean isTerminated;
    private final Object terminationLock = new Object();

    private class Worker extends Thread {
        public Worker(String name) {
            super(name);
        }

        @Override
        public void run() {
            try {
                while (true) {
                    if (isShutdown.get() && taskQueue.isEmpty()) {
                        break;
                    }
                    try {
                        PrioritizedRunnable task = taskQueue.poll(1, TimeUnit.SECONDS);
                        if (task != null) {
                            task.run();
                        }
                    } catch (InterruptedException e) {
                        if (isShutdown.get()) {
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            } finally {
                checkTermination();
            }
        }
    }

    private void checkTermination() {
        synchronized (terminationLock) {
            boolean allTerminated = true;
            for (Worker worker : workers) {
                if (worker.isAlive()) {
                    allTerminated = false;
                    break;
                }
            }
            if (allTerminated && isShutdown.get()) {
                isTerminated.set(true);
                terminationLock.notifyAll();
            }
        }
    }

    public PrioritizedThreadPoolExecutor(int numThreads) {
        if (numThreads <= 0) throw new IllegalArgumentException("Количество потоков должно быть больше нуля");
        this.taskQueue = new PrioritizedTaskQueue();
        this.workers = new ArrayList<>();
        this.isShutdown = new AtomicBoolean(false);
        this.isTerminated = new AtomicBoolean(false);
        for (int i = 0; i < numThreads; i++) {
            Worker worker = new Worker("Worker-" + i);
            worker.start();
            workers.add(worker);
        }
    }

    public void addWorker() {
        if (isShutdown.get()) return;
        Worker worker = new Worker("Worker-" + workers.size());
        worker.start();
        workers.add(worker);
    }

    public void removeWorker() {
        if (workers.isEmpty()) return;
        Worker worker = workers.removeLast();
        worker.interrupt();
    }

    @Override
    public void execute(Runnable command) {
        if (!(command instanceof PrioritizedRunnable)) {
            command = new PrioritizedTask(command, 0);
        }
        if (!isShutdown.get()) {
            try {
                taskQueue.put((PrioritizedRunnable) command);
            } catch (InterruptedException e) {
                //
            }
        } else {
            throw new IllegalStateException("Пул потоков остановлен");
        }
    }

    @Override
    public void shutdown() {
        if (isShutdown.compareAndSet(false, true)) {
            workers.forEach(Thread::interrupt);
        }
        checkTermination();
    }

    @Override
    public List<Runnable> shutdownNow() {
        if (isShutdown.compareAndSet(false, true)) {
            workers.forEach(Thread::interrupt);
        }
        List<Runnable> remainingTask = new ArrayList<>();
        taskQueue.drainTo(remainingTask);
        checkTermination();
        return remainingTask;
    }

    @Override
    public boolean isShutdown() {
        return isShutdown.get();
    }

    @Override
    public boolean isTerminated() {
        return isTerminated.get();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        long millisTimeout = unit.toMillis(timeout);
        long deadline = System.currentTimeMillis() + millisTimeout;
        synchronized (terminationLock) {
            while (!isTerminated.get()) {
                long waitTime = deadline - System.currentTimeMillis();
                if (waitTime <= 0) {
                    break;
                }
                terminationLock.wait(waitTime);
            }
        }
        return isTerminated.get();
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        if (task == null) throw new NullPointerException();
        PrioritizedTaskCallable<T> prioritizedTask = new PrioritizedTaskCallable<>(task, 0);
        execute(prioritizedTask);
        return prioritizedTask;
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        if (task == null) throw new NullPointerException();
        PrioritizedTask prioritizedTask = new PrioritizedTask(task, 0);
        execute(prioritizedTask);
        return new FutureTask<>(prioritizedTask, result);
    }

    @Override
    public Future<?> submit(Runnable task) {
        if (task == null) throw new NullPointerException();
        PrioritizedTask prioritizedTask = new PrioritizedTask(task, 0);
        execute(prioritizedTask);
        return new FutureTask<>(prioritizedTask, null);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) {
        if (tasks == null) throw new NullPointerException();
        List<Future<T>> futures = new ArrayList<>(tasks.size());
        for (Callable<T> task : tasks) {
            PrioritizedTaskCallable<T> prioritizedTask = new PrioritizedTaskCallable<>(task, 0);
            execute(prioritizedTask);
            futures.add(prioritizedTask);
        }
        return futures;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) {
        if (tasks == null) throw new NullPointerException();
        long deadline = System.currentTimeMillis() + unit.toMillis(timeout);
        List<Future<T>> futures = new ArrayList<>(tasks.size());
        submitCallablesWithDeadline(tasks, deadline, futures);
        return futures;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks)
            throws InterruptedException, ExecutionException {
        if (tasks == null || tasks.isEmpty()) throw new IllegalArgumentException("Список задач не должен быть null или пустым");
        List<Future<T>> futures = new ArrayList<>();
        try {
            for (Callable<T> task : tasks) {
                PrioritizedTaskCallable<T> prioritizedTask = new PrioritizedTaskCallable<>(task, 0);
                execute(prioritizedTask);
                futures.add(prioritizedTask);
            }
            for (Future<T> future : futures) {
                try {
                    return future.get();
                } catch (ExecutionException e) {
                    // Игнорируем и продолжаем
                }
            }
            throw new ExecutionException("Задача не была выполнена", null);
        } finally {
            for (Future<T> future : futures) {
                future.cancel(true);
            }
        }
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        if (tasks == null || tasks.isEmpty()) throw new IllegalArgumentException("Список задач не должен быть null или пустым");
        long deadline = System.currentTimeMillis() + unit.toMillis(timeout);
        List<Future<T>> futures = new ArrayList<>();
        try {
            submitCallablesWithDeadline(tasks, deadline, futures);
            for (Future<T> future : futures) {
                long remaining = deadline - System.currentTimeMillis();
                if (remaining <= 0) throw new TimeoutException("Время ожидания истекло");
                try {
                    return future.get(remaining, TimeUnit.MILLISECONDS);
                } catch (ExecutionException e) {
                    // Игнорируем и продолжаем
                }
            }
            throw new ExecutionException("Задача не была выполнена", null);
        } finally {
            for (Future<T> future : futures) {
                future.cancel(true);
            }
        }
    }

    private <T> void submitCallablesWithDeadline(Collection<? extends Callable<T>> tasks, long deadline, List<Future<T>> futures) {
        for (Callable<T> task : tasks) {
            if (System.currentTimeMillis() > deadline) break;
            PrioritizedTaskCallable<T> prioritizedTask = new PrioritizedTaskCallable<>(task, 0);
            execute(prioritizedTask);
            futures.add(prioritizedTask);
        }
    }
}
