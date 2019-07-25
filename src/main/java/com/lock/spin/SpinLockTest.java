package com.lock.spin;

public class SpinLockTest implements Runnable {
    static int sum;
    private SpinLock lock;

    public SpinLockTest(SpinLock lock) {
        this.lock = lock;
    }

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        SpinLock lock = new SpinLock();
        for (int i = 0; i < 100; i++) {
            SpinLockTest test = new SpinLockTest(lock);
            Thread t = new Thread(test);
            t.start();
        }

        Thread.currentThread().sleep(1000);
        System.out.println(sum);
    }

    public void run() {
        this.lock.lock();
        //this.lock.lock();
        sum++;
        this.lock.unlock();
        //this.lock.unlock();
    }

}
