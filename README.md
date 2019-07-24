# java领域高级技能点总结
## 1.基于redis byte位计算排行
## 2.基于javaagent 修改字节码的方式 统计方法执行时间
## 3.高并发下的秒杀方案
 	保证不多发 不少发
	下单的步骤 下单-->下单同时预占库存-->支付-->支付成功真正减扣库存-->取消订单-->回退预占库存
## 4.多线程线程池使用
	包括自定义线程池的使用
## 5.基于ThreadLocal的实例应用
## 6:基于Reactor的反应式编程实例
     Flux和 Mono实现
## 6:锁的分类
   可重入锁 synchronized    ReentrantLock
  读写锁       ReentrantReadWriteLock
    