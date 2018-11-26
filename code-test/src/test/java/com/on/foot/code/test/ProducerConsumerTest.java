package com.on.foot.code.test;

import com.google.common.collect.Queues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ProducerConsumerTest
 *
 * @author yeguangkun on 2018/11/23 上午10:11
 * @version 1.0
 */
public class ProducerConsumerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerConsumerTest.class);



    /** 使用 JDK wait notify*/
    private static final Object P_LOCK = new Object();
    private static final Object C_CLOCK = new Object();

    /** 使用 Lock condition */
    private static ReentrantLock lock = new ReentrantLock();
    private static Condition notFull = lock.newCondition();
    private static Condition notEmpty = lock.newCondition();


    private static int index = 0;



    private static Queue<String> datas = Queues.newLinkedBlockingDeque(20);

    public static void main(String[] args) throws InterruptedException {

        /* JDK*/
//        new Thread(new Producer(1), "p1").start();
//        new Thread(new Producer(2), "p2").start();
//        new Thread(new Producer(3), "p3").start();
//        new Thread(new Producer(4), "p4").start();
//        new Thread(new Producer(5), "p5").start();
//        new Thread(new Consumer(1), "c1").start();
//        new Thread(new Consumer(2), "c2").start();
//        new Thread(new Consumer(3), "c3").start();

        /* LOCK */
        new Thread(new Producer2(1), "p1").start();
        new Thread(new Producer2(2), "p2").start();
        new Thread(new Producer2(3), "p3").start();
        new Thread(new Producer2(4), "p4").start();
        new Thread(new Producer2(5), "p5").start();
        new Thread(new Consumer2(1), "c1").start();
        new Thread(new Consumer2(2), "c2").start();
        new Thread(new Consumer2(3), "c3").start();


        Thread.sleep(30000);
    }


    private static class Producer implements Runnable {

        private int id;
        Producer(int id){
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {
                if (datas.size() >= 20) {
                    synchronized (P_LOCK) {
                        try {
                            if (datas.size() >= 20) {
                                LOGGER.info("队列已经满了，队列大小:{}", datas.size());
                                P_LOCK.wait();
                            }
                        } catch (InterruptedException e) {

                        }
                    }
                }

                if (datas.size() < 20) {
                    synchronized (C_CLOCK) {
                        if (datas.size() < 20) {
                            String data = String.valueOf(++index);
                            datas.offer(data);
                            LOGGER.info("Producer id :{} offer:{}", id, data);
                        }
                        C_CLOCK.notifyAll();
                    }
                }


                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private static class Consumer implements Runnable {

        private int id;
        Consumer(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {
                if (datas.size() == 0) {
                    synchronized (C_CLOCK) {
                        try {
                            if (datas.size() == 0) {
                                LOGGER.info("队列已经空了，队列大小:{}", datas.size());
                                C_CLOCK.wait();
                            }
                        } catch (InterruptedException e) {

                        }
                    }
                }

                if (datas.size() > 0){
                    synchronized (P_LOCK) {
                        if (datas.size() > 0) {
                            String data = datas.poll();
                            LOGGER.info("Consumer id :{} peek:{}", id, data);
                        }

                        P_LOCK.notifyAll();
                    }
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private static class Producer2 implements Runnable{

        private int id;

        Producer2(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {

                lock.lock();
                try {
                    while (datas.size() == 20) {
                        notFull.await();
                    }

                    String data = String.valueOf(++index);
                    datas.offer(data);
                    LOGGER.info("Producer id :{} offer:{}", id, data);

                    notEmpty.signalAll();
                } catch (InterruptedException e) {
                    LOGGER.error("Producer id:{}, Interrupted", id, e);
                } finally {
                    lock.unlock();
                }


                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private static class Consumer2 implements Runnable {

        private int id;

        Consumer2(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {

                lock.lock();
                try {
                    while (datas.size() <= 0) {
                        notEmpty.await();
                    }

                    String data = datas.poll();
                    LOGGER.info("Consumer id :{} poll:{}", id, data);

                    notFull.signalAll();
                } catch (InterruptedException e) {
                    LOGGER.error("Consumer id:{}, Interrupted", id, e);
                } finally {
                    lock.unlock();
                }


                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
