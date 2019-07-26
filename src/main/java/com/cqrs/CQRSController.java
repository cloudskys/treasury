package com.cqrs;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CQRSController {
	@Resource
	private CreateOrderCommand createOrderCommand;
	@Resource
	private CommandBus commandBus;
	@PostMapping(value = "/creat")
    public Object createOrderInfo(CreateOrderModel model) {
        return commandBus.dispatch(createOrderCommand, model);
    }
}
