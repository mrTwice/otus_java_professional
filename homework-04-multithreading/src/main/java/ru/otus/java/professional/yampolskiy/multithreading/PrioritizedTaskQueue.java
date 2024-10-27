package ru.otus.java.professional.yampolskiy.multithreading;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public class PrioritizedTaskQueue implements BlockingQueue<PrioritizedRunnable> {
    private final PriorityBlockingQueue<PrioritizedRunnable> queue;

    public PrioritizedTaskQueue() {
        this.queue = new PriorityBlockingQueue<>();
    }

    @Override
    public boolean add(PrioritizedRunnable e) {
        return queue.add(e);
    }

    @Override
    public boolean offer(PrioritizedRunnable e) {
        return queue.offer(e);
    }

    @Override
    public void put(PrioritizedRunnable e) {
        queue.put(e);
    }

    @Override
    public boolean offer(PrioritizedRunnable e, long timeout, TimeUnit unit) {
        return queue.offer(e, timeout, unit);
    }

    @Override
    public PrioritizedRunnable take() throws InterruptedException {
        return queue.take();
    }

    @Override
    public PrioritizedRunnable poll(long timeout, TimeUnit unit) throws InterruptedException {
        return queue.poll(timeout, unit);
    }

    @Override
    public int remainingCapacity() {
        return queue.remainingCapacity();
    }

    @Override
    public boolean remove(Object o) {
        return queue.remove(o);
    }

    @Override
    public boolean contains(Object o) {
        return queue.contains(o);
    }

    @Override
    public int drainTo(Collection<? super PrioritizedRunnable> c) {
        return queue.drainTo(c);
    }

    @Override
    public int drainTo(Collection<? super PrioritizedRunnable> c, int maxElements) {
        return queue.drainTo(c, maxElements);
    }

    @Override
    public PrioritizedRunnable remove() {
        return queue.remove();
    }

    @Override
    public PrioritizedRunnable poll() {
        return queue.poll();
    }

    @Override
    public PrioritizedRunnable element() {
        return queue.element();
    }

    @Override
    public PrioritizedRunnable peek() {
        return queue.peek();
    }

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public Iterator<PrioritizedRunnable> iterator() {
        return queue.iterator();
    }

    @Override
    public Object[] toArray() {
        return queue.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return queue.toArray(a);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return queue.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends PrioritizedRunnable> c) {
        return queue.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return queue.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return queue.retainAll(c);
    }

    @Override
    public void clear() {
        queue.clear();
    }
}
