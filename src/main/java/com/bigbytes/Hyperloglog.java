package com.bigbytes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.clearspring.analytics.stream.cardinality.HyperLogLog;
import com.clearspring.analytics.stream.cardinality.ICardinality;
import com.clearspring.analytics.stream.membership.BloomFilter;
import com.clearspring.analytics.stream.membership.Filter;
import com.clearspring.analytics.stream.quantile.TDigest;

public class Hyperloglog {
	public static void main(String[] args) {
		ICardinality card = new HyperLogLog(10);
		for (int i : new int[] { 1, 2, 3, 2, 4, 3,4,98,4,99,1,2,4,2,3,2,3,2,3,2,3,2,3,3,44,2,3,32,3,3,4,4,3,55,2,45,1,2222 }) {
		    card.offer(i);
		}
		System.out.println(card.cardinality()); // 4
		
		Filter filter = new BloomFilter(100, 0.01);
		for (int i = 0; i <1000000000; i++) {
			filter.add("google.com"+i);
			filter.add("twitter.com"+i);
			filter.add("facebook.com"+i);
		}
		System.out.println(filter.isPresent("google.com0")); // false
		
		
		Random rand = new Random();
		List<Double> data = new ArrayList<Double>();
		TDigest digest = new TDigest(100);
		for (Long i = 0l; i < 1000000000; ++i) {
		    double d = rand.nextDouble();
		    data.add(d);
		    digest.add(d);
		}
		Collections.sort(data);
		for (double q : new double[] { 0.1, 0.5, 0.9,0.245 }) {
		    System.out.println(String.format("quantile=%.1f digest=%.4f exact=%.4f",
		            q, digest.quantile(q), data.get((int) (data.size() * q))));
		}
	}
}
