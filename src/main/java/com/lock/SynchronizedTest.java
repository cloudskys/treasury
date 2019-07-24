package com.lock;
/**
 * 重入锁  
 * 指的是同一线程外层函数获得锁之后 ，内层递归函数仍然有获取该锁的代码，但不受影响。在JAVA环境下 ReentrantLock 和synchronized 都是 可重入锁
 * @author Administrator
 *
 */
public class SynchronizedTest implements Runnable {
    public  synchronized void get() {
        System.out.println("name:" + Thread.currentThread().getName() + " get();");
        set();
    }

    public synchronized  void set() {
        System.out.println("name:" + Thread.currentThread().getName() + " set();");
    }

    public void run() {
        get();
    }

    public static void main(String[] args) {
    	SynchronizedTest ss = new SynchronizedTest();
        new Thread(ss).start();
        new Thread(ss).start();
        new Thread(ss).start();
        new Thread(ss).start();
    }
}
