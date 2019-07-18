package com.reactor;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Function: 消息处理demo <br/>
 * date: 2019/07/18 16:56 <br/>
 *
 */
public class MessageHandleDemo {

    /**
     * 通过 subscribe()方法同时处理了正常消息和错误消息
     */
    @Test
    public void errorPrint() {
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                .subscribe(System.out::println, System.err::println);
    }

    /**
     * 正常的消息处理相对简单。当出现错误时，有多种不同的处理策略。第一种策略是通过 onErrorReturn()方法返回一个默认值
     */
    @Test
    public void onErrorReturn() {
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                .onErrorReturn(0)
                .subscribe(System.out::println);
    }

    /**
     * 根据不同的异常类型来选择要使用的产生元素的流
     */
    @Test
    public void onErrorResume() {
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalArgumentException()))
                .onErrorResume(e -> {
                    if (e instanceof IllegalStateException) {
                        return Mono.just(0);
                    } else if (e instanceof IllegalArgumentException) {
                        return Mono.just(-1);
                    }
                    return Mono.empty();
                })
                .subscribe(System.out::println);
    }

    /**
     * 当出现错误时，还可以通过 retry 操作符来进行重试。重试的动作是通过重新订阅序列来实现的。在使用 retry 操作符时可以指定重试的次数
     */
    @Test
    public void retry() {
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                .retry(3)
                .subscribe(System.out::println);
    }
}