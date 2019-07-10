package com.order.swith;

/**
 * 问题 一件商品只有100个库存，现在有1000或者更多的用户来购买，每个用户计划同时购买1个到几个不等商品。如何保证库存在高并发的场景下是安全的？
 * 如何保证不多发 不少发
 *
 * 下单的步骤 下单-->下单同时预占库存-->支付-->支付成功真正减扣库存-->取消订单-->回退预占库存
 *
 * 在UI拦截，点击后按钮置灰，不能继续点击，防止用户，连续点击造成的重复下单。
 * 1、在下单前获取一个下单的唯一token，下单的时候需要这个token。后台系统校验这个 token是否有效，才继续进行下单操作。
 */

public class Switch01_UI {
	/**
	 * 先生成 token 保存到 Redis token 作为 key , 并设置过期时间 时间长度 根据任务需求 value 为数字 自增判断
	 * 是否使用过 *
	 * 
	 * @param user
	 * @return
	 */
	public String createToken(User user) {
		String key = "placeOrder:token:" + user.getId();
		String token = UUID.randomUUID().toString();
		// 保存到Redis
		redisService.set(key + token, 0, 1000L);
		return token;
	}

	/**
	 * 校验下单的token是否有效
	 * 
	 * @param user
	 * @param token
	 * @return
	 */
	public Boolean checkToken(User user, String token) {
		String key = "placeOrder:token:" + user.getId();
		if (null != redisService.get(key + token)) {
			long times = redisService.increment(key + token, 1);
			if (times == 1) {
				// 利用increment 原子性 判断是否 该token 是否使用
				return true;
			} else {
				// 已经使用过了
			}
			// 删除
			redisService.remove(key + token);
		}
		return false;
	}
}
