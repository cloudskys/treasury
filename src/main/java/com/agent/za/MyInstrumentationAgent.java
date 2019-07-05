package com.agent.za;

import java.lang.instrument.Instrumentation;
import java.util.logging.Logger;


public class MyInstrumentationAgent {
	private static Logger logger = Logger.getLogger("MyInstrumentationAgent");

    public static void agentmain(String agentArgs, Instrumentation inst) {
        logger.info("[Agent] In agentmain method");

        //需要监控的类
        String className = "com.hebh.demo.application.Runner";
        transformClass(className, inst);
    }

    private static void transformClass(String className, Instrumentation instrumentation) {
        Class<?> targetCls = null;
        ClassLoader targetClassLoader = null;
        // see if we can get the class using forName
        try {
            targetCls = Class.forName(className);
            targetClassLoader = targetCls.getClassLoader();
            transform(targetCls, targetClassLoader, instrumentation);
            return;
        } catch (Exception ex) {
            logger.info("Class [{}] not found with Class.forName");
        }
        // otherwise iterate all loaded classes and find what we want
        for(Class<?> clazz: instrumentation.getAllLoadedClasses()) {
            if(clazz.getName().equals(className)) {
                targetCls = clazz;
                targetClassLoader = targetCls.getClassLoader();
                transform(targetCls, targetClassLoader, instrumentation);
                return;
            }
        }
        throw new RuntimeException("Failed to find class [" + className + "]");
    }
    private static void transform(Class<?> clazz, ClassLoader classLoader, Instrumentation instrumentation) {
        MyTransformer dt = new MyTransformer(clazz.getName(), classLoader);
        instrumentation.addTransformer(dt, true);
        try {
            instrumentation.retransformClasses(clazz);
        } catch (Exception ex) {
            throw new RuntimeException("Transform failed for class: [" + clazz.getName() + "]", ex);
        }
    }

}
