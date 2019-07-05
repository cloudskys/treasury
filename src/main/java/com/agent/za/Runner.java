package com.agent.za;

import java.util.logging.Logger;

public class Runner {
	private static Logger logger = Logger.getLogger("Runner");

	public void run() throws InterruptedException {
		long sleep = (long) (Math.random() * 1000 + 200);
		Thread.sleep(sleep);
		logger.info("run in [{}] millis!" + sleep);
	}
}