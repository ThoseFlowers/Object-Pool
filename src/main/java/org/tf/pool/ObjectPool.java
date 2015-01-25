package org.tf.pool;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author hezhiyu on 15/1/25.
 */
public class ObjectPool implements Pool<Worker> {

    private ObjectPool(int permits) {
        manager     = new Semaphore(permits);
        idleQueue   = new ArrayBlockingQueue<>(permits);
        activeCount = 0;
        initializedCount = 0;
        maxCapacity = permits;
        acquireLock = new ReentrantLock();
        releaseLock = new ReentrantLock();
    }

    public static ObjectPool newInstance(ObjectPoolConfig config) {
        if (!config.isValidConfig()) {
            throw new RuntimeException("object config error");
        }
        synchronized (ObjectPool.class) {
            if (objectPool == null) {
                objectPool = new ObjectPool(config.initCapacity);
            }
        }
        return objectPool;
    }

    // 通过semaphore管理资源的获取, 如果tryAcquire失败, 直接返回空, 不进行任何等待
    // 当然这种处理是需要结合具体情况给出特殊对待的
    @Override
    public Worker acquireResource() {
        try {
            acquireLock.lock();
            manager.acquire();
            activeCount++;
            if (initializedCount < maxCapacity) {
                initializedCount++;
                return new Worker(initializedCount);
            }
            return idleQueue.poll();
        } catch (InterruptedException e) {
            // ignore
        } finally {
            acquireLock.unlock();
        }
        return null;
    }

    @Override
    public void releaseResource(Worker worker) {
        try {
            releaseLock.lock();
            System.out.println("release work to object pool: " + worker.getIndex());
            idleQueue.add(worker);
            activeCount--;
            manager.release();
        } finally {
            releaseLock.unlock();
        }
    }

    @Override
    public void releaseBrokenResource(Worker worker) {
        try {
            releaseLock.lock();
            int index = worker.getIndex();
            Worker fixedWorker = new Worker(index);
            idleQueue.add(fixedWorker);
            activeCount--;
            manager.release();
        } finally {
            releaseLock.unlock();
        }
    }


    public int getActiveCount() {
        return activeCount;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    private static ObjectPool objectPool;
    private Semaphore manager;    // 利用信号量控制Object的迁入迁出
    private int activeCount;
    private int maxCapacity;
    private int initializedCount;
    private Queue<Worker> idleQueue;
    private Lock acquireLock;
    private Lock releaseLock;

    /**
     * 静态内部类与非静态内部类的区别:
     * http://docs.oracle.com/javase/tutorial/java/javaOO/nested.html
     *
     * 非内部类:   可以访问外部类的成员
     * 静态内部类: 外部类的小伙伴
     */
    public static class ObjectPoolConfig {
        public final int initCapacity;

        public ObjectPoolConfig(int initCapacity) {
            this.initCapacity = initCapacity;
        }

        public boolean isValidConfig() {
            return initCapacity > 0;
        }
    }

}
