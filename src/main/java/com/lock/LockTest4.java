package com.lock;

import java.util.concurrent.locks.ReentrantLock;

public class LockTest4 extends Thread {
    ReentrantLock lock = new ReentrantLock();
    public void get() {
        lock.lock();
        System.out.println(Thread.currentThread().getId());
        set();
        lock.unlock();
    }
    public void set() {
        lock.lock();
        System.out.println(Thread.currentThread().getId());
        lock.unlock();
    }
    @Override
    public void run() {
        get();
    }
    public static void main(String[] args) {
        LockTest4 ss = new LockTest4();
        new Thread(ss).start();
        new Thread(ss).start();
        new Thread(ss).start();
    }
}
