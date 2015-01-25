package org.tf.pool;

/**
 *
 * 对于池来说, 归还Resource的方式有两种
 *
 * 第一种, 由使用者主动调用Pool的归还方法(此例中的方式)
 *
 * 第二种, 当Resource使用结束后自行归还
 *
 * @author hezhiyu on 15/1/25.
 */
public interface Pool<T> {

    public T acquireResource();

    public void releaseResource(T t);

    public void releaseBrokenResource(T t);
}