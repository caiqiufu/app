package com.slipper.unieap.demo.bo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SaveOperlogBO {
	Logger logger = LoggerFactory.getLogger(SaveOperlogBO.class);

	// 通过@Async注解表明该方法是一个异步方法，如果注解在类级别，表明该类下所有方法都是异步方法，而这里的方法自动被注入使用ThreadPoolTaskExecutor
	// 作为 TaskExecutor
	@Async
	public void executeAsyncTask() {
		saveOperlog();
	}

	public Map<String, String> saveOperlog() {
		System.out.println((new Date()).toString()+"Job 执行");
		Map<String, String> r = new HashMap<String, String>();
		r.put("processResult", "success");
		return  r;
	}

	@Async
	public void asyncInvokeWithParameter(List<Object> datas) {
		
	}

}