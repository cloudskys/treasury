package com.agent.za;

import java.io.File;
import java.util.Optional;
import java.util.logging.Logger;

import com.sun.tools.attach.VirtualMachine;

public class AgentLoader {
	private static Logger logger = Logger.getLogger("AgentLoader");

    public static void run() {
        //指定jar路径
        String agentFilePath = "/Users/baohuahe/demos/agentdemo/target/myAgent-jar-with-dependencies.jar";
        
        //需要attach的进程标识
        String applicationName = "myAgent";

        //查到需要监控的进程
        Optional<String> jvmProcessOpt = Optional.ofNullable(VirtualMachine.list()
                .stream()
                .filter(jvm -> {
                    logger.info("jvm:{}"+ jvm.displayName());
                    return jvm.displayName().contains(applicationName);
                })
                .findFirst().get().id());

        if(!jvmProcessOpt.isPresent()) {
            logger.warning("file not find..");
            return;
        }
        File agentFile = new File(agentFilePath);
        try {
            String jvmPid = jvmProcessOpt.get();
            logger.info("Attaching to target JVM with PID: " + jvmPid);
            VirtualMachine jvm = VirtualMachine.attach(jvmPid);
            jvm.loadAgent(agentFile.getAbsolutePath());
            jvm.detach();
            logger.info("Attached to target JVM and loaded Java agent successfully");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
