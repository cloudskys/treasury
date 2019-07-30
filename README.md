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
## 命令模式
	CQRS 是一种思想很简单清晰的设计模式，他通过在业务上分离操作和查询来使得系统具有更好的可扩展性及性能，使得能够对系统的不同部分进行扩展和优化。在 CQRS 中，所有的涉及到对 DB 的操作都是通过发送 Command，然后特定的 Command 触发对应事件来完成操作，也可以做成异步的，主要看业务上的需求了。
## 7:雪花算法生成不重复的订单ID-->>(进阶)高可用分布式ID生成项目Leaf 
       详见https://github.com/cloudskys/Leaf  