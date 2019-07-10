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

//1: 数据库操作商品库存
/** * Created by Administrator on 2017/9/8. */
public interface ProductDao extends JpaRepository<Product, Integer> {
	/** * @param pid 商品ID * @param num 购买数量 * @return */
		@Transactional
		@Modifying
		@Query("update Product set availableNum = availableNum - ?2 , reserveNum = reserveNum + ?2 where id = ?1")
		intreduceStock1(Integerpid,Integernum);/** * @param pid 商品ID * @param num 购买数量 * @return */
		@Transactional
		@Modifying
		@Query("update Product set availableNum = availableNum - ?2 , reserveNum = reserveNum + ?2 where id = ?1 and availableNum - ?2 >= 0")
		intreduceStock2(Integerpid,Integernum);
}
