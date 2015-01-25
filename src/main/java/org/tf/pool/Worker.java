package org.tf.pool;

import java.util.concurrent.TimeUnit;

/**
 * @author hezhiyu on 15/1/25.
 */
public class Worker {

    private int index;

    public Worker(int index) {
        this.index = index;
    }

    // 此处应该是一个范型化的东东, 然后deal with it
    public void doSomething(int cost) throws Exception {
        System.out.println(String.format("%sth worker has do work for %s!", index, Thread.currentThread().getName()));
        TimeUnit.SECONDS.sleep(cost);
    }

    public int getIndex() {
        return index;
    }
}
