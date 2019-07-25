package com.lock.spin;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 
 * @createTime: 创建时间：2019-06-5 上午10:12:50
 * @version: v1.0
 */

public class SpinLock {
    //AtomicReference 可以用原子方式更新的对象引用。
    private AtomicReference<Thread> sign =new AtomicReference<Thread>();//使用给定的初始值创建新的 AtomicReference
    public void lock() {
        Thread current = Thread.currentThread();
        //compareAndSet(V expect, V update) 如果当前值 == 预期值，则以原子方式将该值设置为给定的更新值。
        while (!sign.compareAndSet(null, current)) {
          }
    }
    public void unlock() {
        Thread current = Thread.currentThread();
        sign.compareAndSet(current, null);
    }

}


