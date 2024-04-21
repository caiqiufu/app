package com.slipper.unieap.jrds;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.slipper.unieap.ApplicationContextProvider;

@Component
public class IGDSTask {
	public final Log logger = LogFactory.getLog(IGDSTask.class);

	/**
	 * 定时任务方法 数据采集
	 * 
	 * @Scheduled:设置定时任务 cron属性：cron表达式。定时任务触发是时间的一个字符串表达形式
	 */
	// @Scheduled(cron="0/5 * * * * ?") //每个2秒执行一次
	// @Scheduled(cron="0 25 8 * * *") //每天8：25分秒执行一次
	@Scheduled(cron = "0 */5 7-23 * * 1") // 周一06:00-23:00 每间隔5分钟执行一次
	// @Scheduled(cron="0 5 * * * *") //每小时第5分钟执行
	public void scheduledMethod1() {
		IGDBBO iGDBBO = (IGDBBO) ApplicationContextProvider.getBean("IGDBBO");
		// System.out.println("定时器被触发" + new Date());
		logger.info("IG采集数据任务开始执行");
		//iGDBBO.collectOtherDataTask();
	}
	@Scheduled(cron = "0 */5 7-23 * * 1") // 周一06:00-23:00 每间隔5分钟执行一次
	// @Scheduled(cron="0 5 * * * *") //每小时第5分钟执行
	public void scheduledMethod11() {
		IGDBBO iGDBBO = (IGDBBO) ApplicationContextProvider.getBean("IGDBBO");
		// System.out.println("定时器被触发" + new Date());
		logger.info("IG采集数据任务开始执行");
		//iGDBBO.collectLatestGoldTask();
	}
	@Scheduled(cron = "0 */5 0-5,7-23 * * 2-5") // 周二到周五每间隔15分钟执行一次，05:00-06:00 不执行
	public void scheduledMethod21() {
		IGDBBO iGDBBO = (IGDBBO) ApplicationContextProvider.getBean("IGDBBO");
		// System.out.println("定时器被触发" + new Date());
		logger.info("IG采集数据任务开始执行");
		//iGDBBO.collectOtherDataTask();
	}
	@Scheduled(cron = "0 */5 0-5,7-23 * * 2-5") // 周二到周五每间隔15分钟执行一次，05:00-06:00 不执行
	public void scheduledMethod22() {
		IGDBBO iGDBBO = (IGDBBO) ApplicationContextProvider.getBean("IGDBBO");
		// System.out.println("定时器被触发" + new Date());
		logger.info("IG采集数据任务开始执行");
		//iGDBBO.collectLatestGoldTask();
	}
	
	@Scheduled(cron = "0 0/10 0-5 * * 6") // 周六每间隔15分钟执行一次，0-4点执行
	public void scheduledMethod31() {
		IGDBBO iGDBBO = (IGDBBO) ApplicationContextProvider.getBean("IGDBBO");
		// System.out.println("定时器被触发" + new Date());
		logger.info("IG采集数据任务开始执行");
		//iGDBBO.collectOtherDataTask();
	}
	@Scheduled(cron = "0 0/5 0-5 * * 6") // 周六每间隔15分钟执行一次，0-4点执行
	public void scheduledMethod32() {
		IGDBBO iGDBBO = (IGDBBO) ApplicationContextProvider.getBean("IGDBBO");
		// System.out.println("定时器被触发" + new Date());
		logger.info("IG采集数据任务开始执行");
		//iGDBBO.collectLatestGoldTask();
	}

	/**
	 * 数据清除
	 */
	@Scheduled(cron = "0 0 4 ? * 1") // 周一早上4点执行一次
	public void scheduledMethod0() {
		 //JRDBBO jRDBBO = (JRDBBO) ApplicationContextProvider.getBean("JRDBBO");
		 //logger.info("清除数据任务开始执行");
		 //jRDBBO.clearJRSourceData();
	}
}
