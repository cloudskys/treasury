package com.cqrs;

import org.springframework.stereotype.Component;

@Component
public class CommandBus {
	public <T> Object dispatch(Command cmd, T model) {
		return cmd.execute(model);
	}
}
