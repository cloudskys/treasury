package com.agent.za;

import java.util.logging.Logger;

public class MyApplication {
	private static Logger logger = Logger.getLogger("MyApplication");

    public static void run() throws Exception {
        logger.info("[Application] Starting My application");
        Runner runner = new Runner();
        for(;;){
            runner.run();
        }
    }
}
