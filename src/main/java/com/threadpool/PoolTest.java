package com.threadpool;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class PoolTest {
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		  final HttpServletRequest req = (HttpServletRequest)request;
		WebApplicationContext context = WebApplicationContextUtils
				.getWebApplicationContext(req.getSession().getServletContext());
		ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) context.getBean("taskExecutor");
		taskExecutor.execute(new Runnable() {
			public void run() {

			}
		});
	}
}
