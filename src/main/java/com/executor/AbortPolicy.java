package com.executor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
//丢掉这个任务并抛出 RejectedExecutionException异常。
public class AbortPolicy implements RejectedExecutionHandler {
	public AbortPolicy() {

	}

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
		System.out.println(("Task " + r.toString() + " rejected from " + e.toString()));

	}

}
