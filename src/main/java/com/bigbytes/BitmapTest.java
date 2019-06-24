package com.bigbytes;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import redis.clients.jedis.Jedis;

public class BitmapTest {
	private Jedis jedis = null;

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

	public boolean get(String key, int companyId) {
		init();
		boolean result = jedis.getbit(key, companyId);
		destroy();
		return result;
	}

	public Set<Integer> getCid(String sid) {
		init();
		Set<Integer> set = new HashSet<Integer>(100);
		try {
			BitSet b = BitSet.valueOf(jedis.get(String.format("sid:%s", sid)).getBytes());
			for (int i = b.nextSetBit(0); i >= 0; i = b.nextSetBit(i + 1)) {
				set.add(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		destroy();
		return set;
	}

	public void insert() {
		init();
		try {
			for (int i = 0; i < 3; i++) {
				String key = String.format("sid:%08d", i);
				System.out.println("key = " + key);
				jedis.del(key);
				for (int j = 0; j < 5; j++) {
					int cid = new Random().nextInt(100000);
					System.out.println("setbit = " + key + ":" + cid);
					jedis.setbit(key, cid, true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			destroy();
		}
	}

	public static void main(String[] args) {
		BitmapTest test = new BitmapTest();
		test.insert();
		Set<Integer> cidSet = test.getCid("00000000");
		System.out.println("cidSet = " + cidSet.size());
		for (Integer cid : cidSet) {
			System.out.println("cid = " + cid);
		}
	}
}