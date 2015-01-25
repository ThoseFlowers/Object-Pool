/**
 * <p>
 *
 * 这是一个简单的对象池, 在工程中对象池的概念普遍存在
 *
 * 如 线程池、连接池（jedis、dbcp）等
 *
 * 对于一个对象池, 需要保证的是
 *
 * 1. 资源管理
 *     1.1 创建与回收
 *     1.2 使用与重用
 * 2. 错误处理
 *
 * </p>
 * @author hezhiyu on 15/1/25.
 */
package org.tf.pool;