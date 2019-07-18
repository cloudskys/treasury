package com.reactor;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import reactor.core.publisher.Flux;
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
public class ReactorTests {
	@After
    public void after() {
        try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Test
  //创建一个流，并直接往流上发布一个值为value数据
    public void testJust() {
        Flux.just("hello", "world","Love")
            .subscribe(System.out::println);
    }

    @Test
  //通过list创建一个流，往流上依次发布list中的数据
    public void testList() {
        List<String> words = Arrays.asList(
            "hello",
            "reactive",
            "world"
        );

        Flux.fromIterable(words)
            .subscribe(System.out::println);
    }

    @Test
  //创建一个流，并向流上从i开始连续发布n个数据，数据类型为Integer
    public void testRange() {
        Flux.range(1, 10)
            .subscribe(System.out::println);
    }

    @Test
  //创建一个流，并定时向流上发布一个数据，数据从0开始递增，数据类型为Long
    public void testInterval() {
        Flux.interval(Duration.ofSeconds(80)).subscribe(System.out::println);
    }
}
