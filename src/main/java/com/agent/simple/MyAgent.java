package com.agent.simple;

import java.lang.instrument.Instrumentation;

public class MyAgent {
	// JVM首先尝试在代理类上调用以下方法
	public static void premain(String agentArgs, Instrumentation inst) {
		inst.addTransformer(new MyTransformer());
	}

	// 如果代理类没有实现上面的方法,那么JVM将尝试调该方法
	public static void premain(String agentArgs) {

	}
}
