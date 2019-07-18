package com.reactor;

import org.junit.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Random;

/**
 * Flux 表示的是包含 0 到 N 个元素的异步序列。
 * 在该序列中可以包含三种不同类型的消息通知：正常的包含元素的消息、序列结束的消息和序列出错的消息。
 * 当消息通知产生时，订阅者中对应的方法 onNext(), onComplete()和 onError()会被调用
 *
 * Flux 类的静态方法：
 * 1. just()：可以指定序列中包含的全部元素。创建出来的 Flux 序列在发布这些元素之后会自动结束。
 * 2. fromArray()，fromIterable()和 fromStream()：可以从一个数组、Iterable 对象或 Stream 对象中创建 Flux 对象。
 * 3. empty()：创建一个不包含任何元素，只发布结束消息的序列。
 * 4. error(Throwable error)：创建一个只包含错误消息的序列。
 * 5. never()：创建一个不包含任何消息通知的序列。
 * 6. range(int start, int count)：创建包含从 start 起始的 count 个数量的 Integer 对象的序列。
 * 7. interval(Duration period)和 interval(Duration delay, Duration period)：创建一个包含了从 0 开始递增的 Long 对象的序列。
 *    其中包含的元素按照指定的间隔来发布。除了间隔时间之外，还可以指定起始元素发布之前的延迟时间。
 */
public class FluxDemo {

    /**
     * just()：可以指定序列中包含的全部元素。创建出来的 Flux 序列在发布这些元素之后会自动结束
     */
    @Test
    public void just() {
        Flux.just("Hello", "World").subscribe(System.out::println);
    }

    /**
     * fromArray()，fromIterable()和 fromStream()：可以从一个数组、Iterable 对象或 Stream 对象中创建 Flux 对象。
     */
    @Test
    public void fromArray() {
        Flux.fromArray(new Integer[] {1, 2, 3}).subscribe(System.out::println);
    }

    /**
     * empty()：创建一个不包含任何元素，只发布结束消息的序列。
     */
    @Test
    public void empty() {
        Flux.empty().subscribe(System.out::println);
    }

    /**
     * error(Throwable error)：创建一个只包含错误消息的序列。
     */
    @Test
    public void error() {
        Flux.error(new Exception("this is an exception")).subscribe(System.out::println);
    }

    /**
     * never()：创建一个不包含任何消息通知的序列。
     */
    @Test
    public void never() {
        Flux.never().subscribe(System.out::println);
    }

    /**
     * range(int start, int count)：创建包含从 start 起始的 count 个数量的 Integer 对象的序列。
     */
    @Test
    public void range() {
        Flux.range(1, 10).subscribe(System.out::println);
    }

    /**
     * interval(Duration period)和 interval(Duration delay, Duration period)：创建一个包含了从 0 开始递增的 Long 对象的序列。
     * 其中包含的元素按照指定的间隔来发布。除了间隔时间之外，还可以指定起始元素发布之前的延迟时间。
     */
    @Test
    public void interval() {
        Flux.interval(Duration.ofSeconds(10)).subscribe(System.out::println);
    }

    /**
     * generate()方法通过同步和逐一的方式来产生 Flux 序列。
     * 序列的产生是通过调用所提供的 SynchronousSink 对象的 next()，complete()和 error(Throwable)方法来完成的。
     * 逐一生成的含义是在具体的生成逻辑中，next()方法只能最多被调用一次。
     * 在有些情况下，序列的生成可能是有状态的，需要用到某些状态对象。
     * 此时可以使用 generate()方法的另外一种形式 generate(Callable<S> stateSupplier, BiFunction<S,SynchronousSink<T>,S> generator)，
     * 其中 stateSupplier 用来提供初始的状态对象。在进行序列生成时，状态对象会作为 generator 使用的第一个参数传入，可以在对应的逻辑中对该状态对象进行修改以供下一次生成时使用。
     */
    @Test
    public void generate() {
        // 必须要调用complete()方法，不然所产生的是一个无限序列
        Flux.generate(sink -> {
            sink.next("Hello");
            sink.complete();
        }).subscribe(System.out::println);

        final Random random = new Random();
        Flux.generate(ArrayList::new, (list, sink) -> {
            int value = random.nextInt(100);
            list.add(value);
            sink.next(value);
            if (list.size() == 10) {
                sink.complete();
            }
            return list;
        }).subscribe(System.out::println);
    }

    /**
     * create()方法与 generate()方法的不同之处在于所使用的是 FluxSink 对象。
     * FluxSink 支持同步和异步的消息产生，并且可以在一次调用中产生多个元素
     */
    @Test
    public void create() {
        Flux.create(sink -> {
            for (int i = 0; i < 10; i++) {
                sink.next(i);
            }
            sink.complete();
        }).subscribe(System.out::println);
    }
}