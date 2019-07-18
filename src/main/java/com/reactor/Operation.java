package com.reactor;

import org.junit.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;

/**
 * Function: Reactor操作 <br/>
 * date: 2019/07/07 15:42 <br/>
 */
public class Operation {

    @Test
    public void buffer() {
        /**
         * 把当前流中的元素收集到集合中，并把集合对象作为流中的新元素。在进行收集时可以指定不同的条件：所包含的元素的最大数量或收集的时间间隔
         */
        Flux.range(1, 100).buffer(20).subscribe(System.out::println);

        System.out.println("-----------------------------------------------------------");

        /**
         * bufferUntil 会一直收集直到 Predicate 返回为 true，使得 Predicate 返回 true 的那个元素可以选择添加到当前集合或下一个集合中
         */
        Flux.range(1, 10).bufferUntil(i -> i % 2 == 0).subscribe(System.out::println);

        System.out.println("-----------------------------------------------------------");

        /**
         * bufferWhile 则只有当 Predicate 返回 true 时才会收集。一旦值为 false，会立即开始下一次收集。
         */
        Flux.range(1, 10).bufferWhile(i -> i % 2 == 0).subscribe(System.out::println);
    }

    @Test
    public void filter() {
        /**
         * 对流中包含的元素进行过滤，只留下满足 Predicate 指定条件的元素。
         */
        Flux.range(1, 10).filter(i -> i % 2 == 0).subscribe(System.out::println);
    }

    @Test
    public void window() {
        /**
         * window 操作符的作用类似于 buffer，所不同的是 window 操作符是把当前流中的元素收集到另外的 Flux 序列中，因此返回值类型是 Flux<Flux<T>>。
         * 下面语句的输出结果分别是 5 个UnicastProcessor 字符。
         * 这是因为 window 操作符所产生的流中包含的是 UnicastProcessor 类的对象，而 UnicastProcessor 类的 toString 方法输出的就是 UnicastProcessor 字符。
         */
        Flux.range(1, 100).window(20).subscribe(System.out::println);
    }

    @Test
    public void zipWith() {
        /**
         * zipWith 操作符把当前流中的元素与另外一个流中的元素按照一对一的方式进行合并。在合并时可以不做任何处理，由此得到的是一个元素类型为 Tuple2 的流；
         * 也可以通过一个 BiFunction 函数对合并的元素进行处理，所得到的流的元素类型为该函数的返回值。
         */
        Flux.just("a", "b")
                .zipWith(Flux.just("c", "d"))
                .subscribe(System.out::println);

        System.out.println("-----------------------------------------------------------");

        Flux.just("a", "b")
                .zipWith(Flux.just("c", "d"), (s1, s2) -> String.format("%s-%s", s1, s2))
                .subscribe(System.out::println);
    }

    @Test
    public void take() {
        /**
         * take(long n)，take(Duration timespan)和 takeMillis(long timespan)：按照指定的数量或时间间隔来提取。
         */
        Flux.range(1, 1000).take(10).subscribe(System.out::println);

        System.out.println("-----------------------------------------------------------");

        /**
         * takeLast(long n)：提取流中的最后 N 个元素。
         */
        Flux.range(1, 1000).takeLast(10).subscribe(System.out::println);

        System.out.println("-----------------------------------------------------------");

        /**
         * takeWhile(Predicate<? super T> continuePredicate)： 当 Predicate 返回 true 时才进行提取。
         */
        Flux.range(1, 1000).takeWhile(i -> i < 10).subscribe(System.out::println);

        System.out.println("-----------------------------------------------------------");

        /**
         * takeUntil(Predicate<? super T> predicate)：提取元素直到 Predicate 返回 true。
         */
        Flux.range(1, 1000).takeUntil(i -> i == 10).subscribe(System.out::println);

        System.out.println("-----------------------------------------------------------");

        /**
         * takeUntilOther(Publisher<?> other)：提取元素直到另外一个流开始产生元素。
         */
    }

    @Test
    public void reduce() {
        /**
         * reduce 和 reduceWith 操作符对流中包含的所有元素进行累积操作，得到一个包含计算结果的 Mono 序列。
         * 累积操作是通过一个 BiFunction 来表示的。在操作时可以指定一个初始值。如果没有初始值，则序列的第一个元素作为初始值。
         */
        Flux.range(1, 100).reduce((x, y) -> x + y).subscribe(System.out::println);
        Flux.range(1, 100).reduceWith(() -> 100, (x, y) -> x + y).subscribe(System.out::println);
    }

    @Test
    public void merge() {
        /**
         * merge 和 mergeSequential 操作符用来把多个流合并成一个 Flux 序列。
         * 不同之处在于 merge 按照所有流中元素的实际产生顺序来合并，而 mergeSequential 则按照所有流被订阅的顺序，以流为单位进行合并。
         */

        /**
         * 在使用 merge 的结果流中，来自两个流的元素是按照时间顺序交织在一起
         */
        Flux.merge(Flux.interval(Duration.ofSeconds(0), Duration.ofSeconds(1)).take(5), Flux.interval(Duration.ofSeconds(1), Duration.ofSeconds(2)).take(5))
                .toStream()
                .forEach(System.out::println);

        System.out.println("-----------------------------------------------------------");

        /**
         * 而使用 mergeSequential 的结果流则是首先产生第一个流中的全部元素，再产生第二个流中的全部元素。
         */
        Flux.mergeSequential(Flux.interval(Duration.ofSeconds(0), Duration.ofSeconds(1)).take(5), Flux.interval(Duration.ofSeconds(1), Duration.ofSeconds(2)).take(5))
                .toStream()
                .forEach(System.out::println);
    }

    @Test
    public void flatMap() {
        /**
         * flatMap 和 flatMapSequential 操作符把流中的每个元素转换成一个流，再把所有流中的元素进行合并。
         * flatMapSequential 和 flatMap 之间的区别与 mergeSequential 和 merge 之间的区别是一样的。
         */
        Flux.just(5, 10)
                .flatMap(x -> Flux.interval(Duration.ofSeconds(1)).take(x))
                .toStream()
                .forEach(System.out::println);
    }

    @Test
    public void concatMap() {
        /**
         * concatMap 操作符的作用也是把流中的每个元素转换成一个流，再把所有流进行合并。
         * 与 flatMap 不同的是，concatMap 会根据原始流中的元素顺序依次把转换之后的流进行合并；
         * 与 flatMapSequential 不同的是，concatMap 对转换之后的流的订阅是动态进行的，而 flatMapSequential 在合并之前就已经订阅了所有的流。
         */
        Flux.just(5, 10)
                .concatMap(x -> Flux.interval(Duration.ofSeconds(1)).take(x))
                .toStream()
                .forEach(System.out::println);
    }

    @Test
    public void combineLatest() {
        /**
         * combineLatest 操作符把所有流中的最新产生的元素合并成一个新的元素，作为返回结果流中的元素。
         * 只要其中任何一个流中产生了新的元素，合并操作就会被执行一次，结果流中就会产生新的元素。
         * 在下面代码中，流中最新产生的元素会被收集到一个数组中，通过 Arrays.toString 方法来把数组转换成 String。
         */
        Flux.combineLatest(
                Arrays::toString,
                Flux.interval(Duration.ofSeconds(1)).take(5),
                Flux.interval(Duration.ofSeconds(2)).take(5)
        ).toStream().forEach(System.out::println);
    }
}