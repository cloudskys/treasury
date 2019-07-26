package com.id;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {
	private static final SimpleDateFormat dateFormatOne = new SimpleDateFormat("yyyyMMddHHmmssSS");

	private static final ThreadLocalRandom random = ThreadLocalRandom.current();

	// 生成订单编号-方式一
	public static String generateOrderCode() {
		// TODO:时间戳+N为随机数流水号
		return dateFormatOne.format(new Date()) + generateNumber(4);
	}

	// N为随机数流水号
	public static String generateNumber(final int num) {
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i <= num; i++) {
			sb.append(random.nextInt(9));
		}
		return sb.toString();
	}
}
