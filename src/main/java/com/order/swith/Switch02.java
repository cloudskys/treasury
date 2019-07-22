package com.order.swith;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import com.order.swith.dao.ProductDao;
import com.order.swith.dao.UserDao;
import com.order.swith.po.PlaceOrderReq;
import com.order.swith.po.Product;
import com.order.swith.po.User;
import com.order.swith.service.RedisService;

/**
 * 问题 一件商品只有100个库存，现在有1000或者更多的用户来购买，每个用户计划同时购买1个到几个不等商品。如何保证库存在高并发的场景下是安全的？
 * 如何保证不多发 不少发
 *
 * 下单的步骤 下单-->下单同时预占库存-->支付-->支付成功真正减扣库存-->取消订单-->回退预占库存
 *
 * 在UI拦截，点击后按钮置灰，不能继续点击，防止用户，连续点击造成的重复下单。
 * 1、在下单前获取一个下单的唯一token，下单的时候需要这个token。后台系统校验这个 token是否有效，才继续进行下单操作。
 */

public class Switch02 {
	private static Logger logger = Logger.getLogger("AgentTest");
	@Autowired
	private UserDao userDao;
	@Autowired
	private ProductDao productDao;
	@Autowired
	private RedisService redisService;

	/**
	 * 下单操作1  不考虑库存安全的写法
	 *方法 1 * 减可用 * 加预占 * 库存数据不安全 
	 *  @param req
	 */
	private int place(PlaceOrderReq req) {
		User user = userDao.findOne(req.getUserId());
		Product product = productDao.findOne(req.getProductId());
		// 下单数量
		Integer num = req.getNum();
		// 可用库存
		Integer availableNum = product.getAvailableNum();
		// 可用预定
		if (availableNum >= num) {
			// 减库存
			int count = productDao.reduceStock1(product.getId(), num);
			if (count == 1) {
				// 生成订单
				createOrders(user, product, num);
			} else {
				logger.info("库存不足 3");
			}
			return 1;
		} else {
			logger.info("库存不足 4");
			return -1;
		}
	}

	private void createOrders(User user, Product product, Integer num) {
		// TODO Auto-generated method stub

	}

	/**
	 * 这个操作可以保证库存数据是安全的
	 * * 下单操作2  * 减可用 * 加预占 * 库存数据不安全 * *
	 * 
	 * @param req
	 */
	private int place2(PlaceOrderReq req) {
		User user = userDao.findOne(req.getUserId());
		Product product = productDao.findOne(req.getProductId());
		// 下单数量
		Integer num = req.getNum();
		// 可用库存
		Integer availableNum = product.getAvailableNum();
		// 可用预定
		if (availableNum >= num) {
			// 减库存
			int count = productDao.reduceStock2(product.getId(), num);
			if (count == 1) {
				// 生成订单
				createOrders(user, product, num);
			} else {
				logger.info("库存不足 3");
			}
			return 1;
		} else {
			logger.info("库存不足 4");
			return -1;
		}
	}

	// 方法3 ：该方法也可以保证库存数量安全
	/**
	 * 方法 3 采用 Redis 锁 通一个时间 只能一个 请求修改 同一个商品的数量
	 * <p>
	 * 缺点并发不高,同时只能一个用户抢占操作,用户体验不好！ *
	 * 
	 * @param req
	 */
	// @Override
	public void placeOrder2(PlaceOrderReq req) {
		String lockKey = "placeOrder:" + req.getProductId();
		Boolean isLock = redisService.lock(lockKey);
		if (!isLock) {
			logger.info("系统繁忙稍后再试!");
			// return 2;
		}
		// place2(req); place1(req);
		// 这两个方法都可以
		redisService.unLock(lockKey);
	}

	// 方法4 ：可以保证库存安全，满足高并发处理，但是相对复杂一点
	/**
	 * 方法 4 商品的数量 等其他信息 先保存 到 Redis 检查库存 与 减少库存 不是原子性， 以 increment > 0 为准 *
	 * 可以保证库存安全，满足高并发处理，但是相对复杂一点
	 * @param req
	 */
	// @Override
	public void placeOrder3(PlaceOrderReq req) {
		String key = "product:" + req.getProductId();
		// 先检查 库存是否充足
		Integer num = (Integer) redisService.get(key);
		if (num < req.getNum()) {
			logger.info("库存不足 1");
		} else {
			// 不可在这里下单减库存，否则导致数据不安全， 情况类似 方法1；
		}
		// 减少库存
		long value = redisService.increment(key, -req.getNum().longValue());
		// 库存充足
		if (value >= 0) {
			logger.info("成功抢购 ! ");
			// TODO 真正减 扣 库存 等操作 下单等操作 ,这些操作可用通过 MQ 或 其他方式
			place2(req);
		} else {
			// 库存不足，需要增加刚刚减去的库存
			redisService.increment(key, req.getNum().longValue());
			logger.info("库存不足 2 ");
		}
	}
}
