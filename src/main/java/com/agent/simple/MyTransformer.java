package com.agent.simple;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

public class MyTransformer implements ClassFileTransformer {
	private final static String prefix = "\nlong startTime = System.currentTimeMillis();\n";
	private final static String postfix = "\nlong endTime = System.currentTimeMillis();\n";
	// 被处理的方法列表
	private final static Map<String, List<String>> methodMap = new HashMap<String, List<String>>();

	public MyTransformer() {
		// 对指定方法监控
		add("com.agent.AgentTest.queryUserAge");
		add("com.agent.AgentTest.queryUserName");
	}

	private void add(String methodString) {
		String className = methodString.substring(0, methodString.lastIndexOf("."));
		String methodName = methodString.substring(methodString.lastIndexOf(".") + 1);
		List<String> list = methodMap.get(className);
		if (list == null) {
			list = new ArrayList<String>();
			methodMap.put(className, list);
		}
		list.add(methodName);
	}

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		className = className.replace("/", ".");
		// 判断加载的class的包路径是不是需要监控的类
		if (methodMap.containsKey(className)) {
			CtClass ctclass = null;
			try {
				ctclass = ClassPool.getDefault().get(className);// 使用全称,用于取得字节码类<使用javassist>
				for (String methodName : methodMap.get(className)) {
					String outputStr = "\nSystem.out.println(\"监控信息(执行耗时)：" + className + "." + methodName
							+ " => \" +(endTime - startTime) +\"毫秒\");";
					CtMethod ctmethod = ctclass.getDeclaredMethod(methodName);// 得到这方法实例
					String newMethodName = methodName + "$new";// 新定义一个方法叫做比如queryUserAge$new
					ctmethod.setName(newMethodName);// 将原来的方法名字修改
					// 创建新的方法，复制原来的方法，名字为原来的名字
					CtMethod newMethod = CtNewMethod.copy(ctmethod, methodName, ctclass, null);
					// 构建新的方法体
					StringBuilder methodBodyStr = new StringBuilder();
					methodBodyStr.append("{");
					methodBodyStr.append(prefix);
					methodBodyStr.append(newMethodName + "($$);\n");// 调用原有代码，类似于method();($$)表示所有的参数
					methodBodyStr.append(postfix);
					methodBodyStr.append(outputStr);
					methodBodyStr.append("}");
					newMethod.setBody(methodBodyStr.toString());// 替换新方法
					ctclass.addMethod(newMethod); // 增加新方法
				}
				return ctclass.toBytecode();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
