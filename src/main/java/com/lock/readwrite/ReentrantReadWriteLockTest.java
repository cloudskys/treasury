package com.lock.readwrite;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/**
 * 读写锁
 * 相比Java中的锁(Locks in Java)里Lock实现，读写锁更复杂一些。假设你的程序中涉及到对一些共享资源的读和写操作，且写操作没有读操作那么频繁。在没有写操作的时候，两个线程同时读一个资源没有任何问题，所以应该允许多个线程能在同时读取共享资源。但是如果有一个线程想去写这些共享资源，就不应该再有其它线程对该资源进行读或写（译者注：也就是说：读-读能共存，读-写不能共存，写-写不能共存）。这就需要一个读/写锁来解决这个问题
 * @author Administrator
 *
 */
public class ReentrantReadWriteLockTest {
    static Map<String, Object> map = new HashMap<String, Object>();
    static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    static Lock r = rwl.readLock();
    static Lock w = rwl.writeLock();    

    // 获取一个key对应的value
    public static final Object get(String key) {
        r.lock();
        try {
            System.out.println("正在做读的操作,key:" + key + " 开始");
            Thread.sleep(100);
            Object object = map.get(key);
            System.out.println("正在做读的操作,key:" + key + " 结束");
            System.out.println();
            return object;
        } catch (InterruptedException e) {

        } finally {
            r.unlock();
        }
        return key;
    }

    // 设置key对应的value，并返回旧有的value
    public static final Object put(String key, Object value) {
        w.lock();
        try {
            System.out.println("正在做写的操作,key:" + key + ",value:" + value + "开始.");
            Thread.sleep(100);
            Object object = map.put(key, value);
            System.out.println("正在做写的操作,key:" + key + ",value:" + value + "结束.");
            return object;
        } catch (InterruptedException e) {

        } finally {
            w.unlock();
        }
        return value;
    }

    // 清空所有的内容
    public static final void clear() {
        w.lock();
        try {
            map.clear();
        } finally {
            w.unlock();
        }
    }

    public static void main(String[] args) {
        new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < 10; i++) {
                    ReentrantReadWriteLockTest.put(i + "", i + "");
                }

            }
        }).start();
        new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < 10; i++) {
                    ReentrantReadWriteLockTest.get(i + "");
                }

            }
        }).start();
        clear();
    }
}
