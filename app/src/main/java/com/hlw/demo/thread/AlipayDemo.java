package com.hlw.demo.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author hlw
 * @date 2020-03-22 20:55:07
 * 以购买为例举例说明重入锁
 */

public class AlipayDemo {
    private double[] accounts;
    private Lock mLock;
    private Condition condition;

    public AlipayDemo(int n, double money) {
        accounts = new double[n];
        mLock = new ReentrantLock();
        //得到条件对象
        condition = mLock.newCondition();
        for (int i = 0; i < n; i++) {
            accounts[i] = money;
        }
    }

    public void transfer(int from, int to, int amount) throws InterruptedException {
        try {
            mLock.lock();
            while (accounts[from] < amount) {
                //阻塞当前线程，并放弃锁
                condition.await();
            }
            //转账的而操作
            accounts[from] = accounts[from] - amount;
            accounts[to] = accounts[to] + amount;
            //解除等待线程的阻塞
            condition.signalAll();
        } finally {
            mLock.unlock();
        }
    }
}
