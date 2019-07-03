package com.bigbytes;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import redis.clients.jedis.Jedis;

public class RedisBitmap {
	private Jedis jedis = null;
	static String dailykey = "daily_active";

	private void init() {
		try {
			jedis = new Jedis("redis01.fybanks.cn", 6379);
			jedis.auth("654321");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void destroy() {
		if (jedis != null) {
			jedis.close();
		}
	}

	public void insertDaily() {
		init();
		try {
			for (int i = 0; i < 9; i++) {
				// String key = String.format("sid:%08d", i);
				String key = dailykey + ":2019070" + i;

				jedis.del(key);
				for (int j = 0; j < 50000; j++) {
					// int cid = new Random().nextInt(100000);
					// System.out.println("setbit = " + key + ":" + cid);
					// System.out.println("setbit = " + key + ":" + j);
					// jedis.setbit(key, cid, true);
					jedis.setbit(key, j, true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			destroy();
		}
	}

	public int uniqueCount(String action, String date) {
		init();
		String key = action + ":" + date;
		BitSet users = BitSet.valueOf(jedis.get(key.getBytes()));
		return users.cardinality();
	}

	public int uniqueCount(String action, String... dates) {
		init();
		BitSet all = new BitSet();
		for (String date : dates) {
			String key = action + ":" + date;
			BitSet users = BitSet.valueOf(jedis.get(key.getBytes()));
			all.or(users);
		}
		return all.cardinality();
	}

	public static void main(String[] args) {
		RedisBitmap b = new RedisBitmap();
		b.insertDaily();
		int i = b.uniqueCount("daily_active", "20190701");
		System.out.println(i);
		int j = b.uniqueCount(dailykey, "20190701", "20190702", "20190703", "20190704", "20190705");
		System.out.println(j);
	}
}