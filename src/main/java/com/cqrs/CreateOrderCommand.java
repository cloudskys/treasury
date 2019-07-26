package com.cqrs;

import org.springframework.stereotype.Component;

@Component
public class CreateOrderCommand implements Command<CreateOrderModel> {

    @Override
    public Object execute(CreateOrderModel model) {
		return model;
        // 具体的逻辑
    }
}
