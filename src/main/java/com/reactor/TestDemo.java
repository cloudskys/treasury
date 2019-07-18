package com.reactor;

import java.time.Duration;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.publisher.TestPublisher;

public class TestDemo {

    /**
     * 进行测试时的一个典型的场景是对于一个序列，验证其中所包含的元素是否符合预期。
     * StepVerifier 的作用是可以对序列中包含的元素进行逐一验证。
     * 在下面代码中，需要验证的流中包含 a 和 b 两个元素。
     * 通过 StepVerifier.create()方法对一个流进行包装之后再进行验证。
     * expectNext()方法用来声明测试时所期待的流中的下一个元素的值，而 verifyComplete()方法则验证流是否正常结束。类似的方法还有 verifyError()来验证流由于错误而终止。
     */
    @Test
    public void stepVerifier() {
        StepVerifier.create(Flux.just("a", "b"))
                .expectNext("a")
                .expectNext("c")
                .verifyError();
    }

    /**
     * 有些序列的生成是有时间要求的，比如每隔 1 分钟才产生一个新的元素。
     * 在进行测试中，不可能花费实际的时间来等待每个元素的生成。
     * 此时需要用到 StepVerifier 提供的虚拟时间功能。
     * 通过 StepVerifier.withVirtualTime()方法可以创建出使用虚拟时钟的 StepVerifier。通过 thenAwait(Duration)方法可以让虚拟时钟前进。
     */
    @Test
    public void stepVerifierTime() {
        /**
         * expectNoEvent()方法用来验证在 4 个小时之内没有任何消息产生，然后验证第一个元素 0 产生；
         * 接着 thenAwait()方法来让虚拟时钟前进一天，然后验证第二个元素 1 产生；最后验证流正常结束。
         */
        StepVerifier.withVirtualTime(() -> Flux.interval(Duration.ofHours(4), Duration.ofDays(1)).take(2))
                .expectSubscription()
                .expectNoEvent(Duration.ofHours(4))
                .expectNext(0L)
                .thenAwait(Duration.ofDays(1))
                .expectNext(1L)
                .verifyComplete();
    }

    /**
     * TestPublisher 的作用在于可以控制流中元素的产生，甚至是违反反应流规范的情况。
     * 在下面代码中，通过 create()方法创建一个新的 TestPublisher 对象，然后使用 next()方法来产生元素，使用 complete()方法来结束流。
     * TestPublisher 主要用来测试开发人员自己创建的操作符。
     */
    @Test
    public void testPublisher() {
        final TestPublisher<String> testPublisher = TestPublisher.create();
        testPublisher.next("a");
        testPublisher.next("b");
        testPublisher.complete();

        StepVerifier.create(testPublisher)
                .expectNext("a")
                .expectNext("b")
                .expectComplete();
    }

    /**
     * 另外一种做法是通过 checkpoint 操作符来对特定的流处理链来启用调试模式。
     * 在 map 操作符之后添加了一个名为 test 的检查点。当出现错误时，检查点名称会出现在异常堆栈信息中。对于程序中重要或者复杂的流处理链，可以在关键的位置上启用检查点来帮助定位可能存在的问题。
     */
    @Test
    public void checkpoint() {
        Flux.just(1, 0).map(x -> 1 / x).checkpoint("test").subscribe(System.out::println);
    }

    /**
     * 在开发和调试中的另外一项实用功能是把流相关的事件记录在日志中。这可以通过添加 log 操作符来实现。
     */
    @Test
    public void log() {
        Flux.range(1, 2).log("Range").subscribe(System.out::println);
    }

    /**
     * “冷”与“热”序列
     * 冷序列的含义是不论订阅者在何时订阅该序列，总是能收到序列中产生的全部消息。
     * 而与之对应的热序列，则是在持续不断地产生消息，订阅者只能获取到在其订阅之后产生的消息。
     *
     * @throws InterruptedException
     */
    @Test
    public void publish() throws InterruptedException {
        /**
         * 原始的序列中包含 10 个间隔为 1 秒的元素。
         * 通过 publish()方法把一个 Flux 对象转换成 ConnectableFlux 对象。
         * 方法 autoConnect()的作用是当 ConnectableFlux 对象有一个订阅者时就开始产生消息。
         * 代码 source.subscribe()的作用是订阅该 ConnectableFlux 对象，让其开始产生数据。
         * 接着当前线程睡眠 5 秒钟，第二个订阅者此时只能获得到该序列中的后 5 个元素，因此所输出的是数字 5 到 9。
         */
        final Flux<Long> source = Flux.interval(Duration.ofSeconds(1))
                .take(10)
                .publish()
                .autoConnect();
        source.subscribe();
        Thread.sleep(5000);
        source.toStream()
                .forEach(System.out::println);
    }
}
