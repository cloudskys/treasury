package com.executor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RejectedExecutionHandlerTest implements RejectedExecutionHandler {  
      private static final Logger logger = LoggerFactory.getLogger("test");  
      
      @Override  
      public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {  
    	
          if (r instanceof MyTask) {  
        	  MyTask runnable = (MyTask)r;  
        	  runnable.getTaskId();
          }else if(r instanceof UseThreadPoolExecutor2){
        	  UseThreadPoolExecutor2 runnable = (UseThreadPoolExecutor2)r;  
        	  int id = runnable.getTaskId();
        	  System.out.println(("Task " + id));
          }

      }  

 }  
