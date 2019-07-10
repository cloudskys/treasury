package com.threadlocal;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class ThreadLocalContent {

	public static final ThreadLocal<SimpleDateFormat> smp = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};
	public static final ThreadLocal<SimpleDateFormat> smpDirect = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM");
		}
	};

	private static ThreadLocal<Map<Class<?>, Object>> context = new InheritableThreadLocal<Map<Class<?>, Object>>();

	/**
	 * 把参数设置到上下文的Map中
	 */
	public static void put(Object obj) {
		Map<Class<?>, Object> map = context.get();
		if (map == null) {
			map = new HashMap<>();
			context.set(map);
		}
		if (obj instanceof Enum) {
			map.put(obj.getClass().getSuperclass(), obj);
		} else {
			map.put(obj.getClass(), obj);
		}
	}

	/**
	 * 从上下文中，根据类名取出参数
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(Class<T> c) {
		Map<Class<?>, Object> map = context.get();
		if (map == null) {
			return null;
		}
		return (T) map.get(c);
	}

	/**
	 * 清空ThreadLocal的数据
	 */
	public static void clean() {
		context.remove();
	}
	
	
	public static void main(String[] args) {
		Map<Class<?>, Object> x =new HashMap<>();
		//Map<Animals, Object> a = new HashMap<Animals, Object>();
		Animals aa=new Animals(10,"Dog");
		x.put(Animals.class, aa);
		ThreadLocalContent.context.set(x);
		
		Map<Class<?>, Object> awwa = ThreadLocalContent.context.get();
		System.out.println("age:"+((Animals)awwa.get(Animals.class)).getAge()+".name:"+((Animals)awwa.get(Animals.class)).getName());
		ThreadLocalContent.clean();
	}

}
