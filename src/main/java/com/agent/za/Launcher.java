package com.agent.za;

public class Launcher {
    public static void main(String[] args) throws Exception {
        if(args != null && args.length > 0 && "LoadAgent".equals(args[0])) {
            new AgentLoader().run();
        }else{
            new MyApplication().run();
        }
    }
}
