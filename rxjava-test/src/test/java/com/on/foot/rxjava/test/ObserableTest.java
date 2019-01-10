package com.on.foot.rxjava.test;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * ObserableTest
 *
 * @author yeguangkun on 2019/1/7 下午3:28
 * @version 1.0
 */
public class ObserableTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ObserableTest.class);


    @Test
    public void testColdObserable() {
        Consumer<Long> subscriber1 = aLong -> System.out.println("subscriber1:" + aLong);
        Consumer<Long> subscriber2 = aLong -> System.out.println("  subscriber2:" + aLong);
        Consumer<Long> subscriber3 = aLong -> System.out.println("      subscriber3:" + aLong);

        ConnectableObservable<Long> observable = Observable.<Long>create(observableEmitter ->
                Observable.interval(10, TimeUnit.MILLISECONDS).
                        take(Integer.MAX_VALUE).
                        subscribe(observableEmitter::onNext))
                .observeOn(Schedulers.newThread())
                .publish();

        observable.connect();
        observable.subscribe(subscriber1);
        observable.subscribe(subscriber2);

        try {
            Thread.sleep(20L);
        } catch (Exception e) {
        }

        observable.subscribe(subscriber3);

        try {
            Thread.sleep(100L);
        } catch (Exception e) { }
    }

    @Test
    public void testSchedulers1(){
        Disposable disposable = Observable.just("aaa", "bbb")
                .observeOn(Schedulers.newThread())
                .map(String::toString)
                .subscribeOn(Schedulers.single())
                .observeOn(Schedulers.io())
                .subscribe(System.out::println);
    }
}
