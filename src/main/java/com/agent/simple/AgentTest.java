package com.agent.simple;

import java.util.logging.Logger;

/**
 * vm options =-javaagent:E:\treasury\target\treasury-jar-with-dependencies.jar=agentargs
 */
public class AgentTest {
	private static Logger logger = Logger.getLogger("AgentTest");

	public static void main(String[] args) {
		String userId = "100001";
		queryUserAge(userId);
		queryUserName(userId);
	}

	private static void queryUserAge(String userId) {
		try {
			Thread.sleep(300);
			logger.info("hello userId" + userId + " age 18");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void queryUserName(String userId) {
		try {
			Thread.sleep(100);
			logger.info("hello userId" + userId + " name agent");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
