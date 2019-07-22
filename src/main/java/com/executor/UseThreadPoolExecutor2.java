package com.executor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class UseThreadPoolExecutor2  implements Runnable{
	private static AtomicInteger count = new AtomicInteger(0);
	 private int taskId;
	 
	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public UseThreadPoolExecutor2(int taskId) {
		super();
		this.taskId = taskId;
	}

	@Override
	   public void run() {
	      try {
	         int temp = count.incrementAndGet();
	         System.out.println("任务" + temp);
	         System.out.println("线程" + Thread.currentThread().getName()+ "正在执行...");
	         Thread.sleep(2000);
	      } catch (InterruptedException e) {
	         e.printStackTrace();
	      }
	   }
	   
	   public static void main(String[] args) throws Exception{
	      BlockingQueue<Runnable> queue = 
//	          new LinkedBlockingQueue<Runnable>();
	            new ArrayBlockingQueue<Runnable>(12);
	      ExecutorService executor  = new ThreadPoolExecutor(
	               5,        //core
	               6,     //max
	               120L,  //2fenzhong
	               TimeUnit.SECONDS,
	               queue, new RejectedExecutionHandlerTest());
	      
	      for(int i = 0 ; i < 20; i++){
	         executor.execute(new UseThreadPoolExecutor2(i));
	      }
	      Thread.sleep(1000);
	      System.out.println("queue size:" + queue.size());     //10
	      Thread.sleep(2000);
	      executor.shutdown();
	   }
}
