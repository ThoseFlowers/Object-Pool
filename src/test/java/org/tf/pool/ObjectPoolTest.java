package org.tf.pool;

import java.util.Random;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * @author hezhiyu on 15/1/25.
 */
public class ObjectPoolTest {

    public static void main(String[] args) throws Exception {
        final ObjectPool objectPool = initObjectPool();

        final BlockingDeque<Worker> blockQueue = new LinkedBlockingDeque<>();
        final Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Worker worker = objectPool.acquireResource();
                        worker.doSomething(random.nextInt(10));
                        blockQueue.add(worker);
                    } catch (Exception e) {
                        // ignore
                    }
                }
            }).start();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Worker worker = blockQueue.take();
                        objectPool.releaseResource(worker);
                    } catch (Exception e) {
                        // ignore
                    }
                }
            }
        }).start();
        System.out.println(objectPoolStatus(objectPool));
        TimeUnit.SECONDS.sleep(100);
    }

    private static String objectPoolStatus(ObjectPool objectPool) {
        String status = "";
        status += "active-count: " + objectPool.getActiveCount() + "\n";
        status += "max-capacity: " + objectPool.getMaxCapacity();
        return status;
    }

    private static ObjectPool initObjectPool() {
        ObjectPool.ObjectPoolConfig config = new ObjectPool.ObjectPoolConfig(10000);
        return ObjectPool.newInstance(config);
    }
}
