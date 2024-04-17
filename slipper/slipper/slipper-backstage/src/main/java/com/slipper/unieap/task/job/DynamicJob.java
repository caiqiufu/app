package com.slipper.unieap.task.job;

import java.util.Date;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.pojo.UnieapJobExecute;
import com.slipper.unieap.repository.UnieapJobExecuteRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * :@DisallowConcurrentExecution : 此标记用在实现Job的类上面,意思是不允许并发执行.
 * :注意org.quartz.threadPool.threadCount线程池中线程的数量至少要多个,否则@DisallowConcurrentExecution不生效
 * :假如Job的设置时间间隔为3秒,但Job执行时间是5秒,设置@DisallowConcurrentExecution以后程序会等任务执行完毕以后再去执行,否则会在3秒时再启用新的线程执行
 */
@DisallowConcurrentExecution
@Component
@Slf4j
public abstract class DynamicJob implements Job {
	@Autowired
	private UnieapJobExecuteRepository unieapJobExecuteRepository;

	public void saveJobExecute(Long taskId, Long jobId, Date executeStartDate, Date executeEndDate,
			String executeStatus, String executeResult) {
		UnieapJobExecute execute = new UnieapJobExecute();
		execute.setId(UnieapConstants.getSeq(null));
		execute.setTaskId(taskId);
		execute.setJobId(jobId);
		execute.setExecuteStartDate(executeStartDate);
		execute.setExecuteEndDate(executeEndDate);
		execute.setExecuteResult(executeResult);
		execute.setExecuteStatus(executeStatus);
		execute.setActivateFlag(UnieapConstants.YES);
		execute.setCreateBy("system");
		execute.setCreateDate(UnieapConstants.getDateTime());
		execute.setTenantId(Long.decode("1"));
		unieapJobExecuteRepository.save(execute);
	}

	/**
	 * 核心方法,Quartz Job真正的执行逻辑.
	 *
	 * @param executorContext executorContext
	 *                        JobExecutionContext中封装有Quartz运行所需要的所有信息
	 * @throws JobExecutionException execute()方法只允许抛出JobExecutionException异常
	 */
	public void execute(JobExecutionContext executorContext) throws JobExecutionException {
		// JobDetail中的JobDataMap是共用的,从getMergedJobDataMap获取的JobDataMap是全新的对象
		JobDataMap map = executorContext.getMergedJobDataMap();
		String jarPath = map.getString("jarPath");
		String parameter = map.getString("parameter");
		String vmParam = map.getString("vmParam");
		log.info("Running Job name : {} ", map.getString("name"));
		log.info("Running Job description : {}", map.getString("jobDescription"));
		log.info("Running Job group: {} ", map.getString("group"));
		log.info(String.format("Running Job cron : %s", map.getString("cronExpression")));
		log.info("Running Job jar path : {} ", jarPath);
		log.info("Running Job parameter : {} ", parameter);
		log.info("Running Job vmParam : {} ", vmParam);
		long startTime = System.currentTimeMillis();
		long endTime = System.currentTimeMillis();
		log.info(">>>>>>>>>>>>> Running Job has been completed , cost time : {}ms\n ", (endTime - startTime));
	}
}
