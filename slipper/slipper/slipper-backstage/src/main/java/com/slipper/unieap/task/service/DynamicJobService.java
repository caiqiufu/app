package com.slipper.unieap.task.service;

import java.util.List;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.slipper.unieap.ApplicationContextProvider;
import com.slipper.unieap.pojo.UnieapJobEntity;
import com.slipper.unieap.repository.UnieapJobEntityRepository;
import com.slipper.unieap.task.job.DynamicJob;
import com.slipper.unieap.vo.JobVO;

@Service
public class DynamicJobService {
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	@Autowired
	private UnieapJobEntityRepository repository;

	// 获取JobDataMap.(Job参数对象)
	public JobDataMap getJobDataMap(JobVO job) {
		JobDataMap map = new JobDataMap();
		map.put("jobId", job.getId().toString());
		map.put("taskId", job.getTaskId().toString());
		map.put("taskDesc", job.getTaskDesc());
		map.put("className", job.getClassName());
		map.put("handlerName", job.getHandlerName());
		map.put("retryTimes", job.getRetryTimes());
		map.put("jobGroup", job.getJobGroup());
		map.put("cronExpression", job.getCron());
		map.put("parameters", job.getParameters());
		map.put("jobDescription", job.getDescription());
		map.put("status", job.getStatus());
		map.put("remark", job.getRemark());
		return map;
	}

	// 获取JobDetail,JobDetail是任务的定义,而Job是任务的执行逻辑,JobDetail里会引用一个Job Class来定义
	/**
	 * 
	 * @param jobClass
	 * @param jobKey
	 * @param description
	 * @param map
	 * @return
	 */
	public JobDetail getJobDetail(Class<? extends DynamicJob> jobClass, JobKey jobKey, String description,
			JobDataMap map) {
		return JobBuilder.newJob(jobClass).withIdentity(jobKey).withDescription(description).setJobData(map)
				.storeDurably().build();
	}

	// 获取Trigger (Job的触发器,执行规则)
	public Trigger getTrigger(JobVO job) {
		return TriggerBuilder.newTrigger().withIdentity(job.getId().toString(), job.getJobGroup())
				.withSchedule(CronScheduleBuilder.cronSchedule(job.getCron())).build();
	}

	// 获取JobKey,包含Name和Group
	public JobKey getJobKey(JobVO job) {
		return JobKey.jobKey(job.getId().toString(), job.getJobGroup());
	}

	public void refresh(Long jobId) throws SchedulerException {
		JobVO job = repository.getJobById(jobId);
		JobKey jobKey = getJobKey(job);
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		scheduler.pauseJob(jobKey);
		scheduler.unscheduleJob(TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup()));
		scheduler.deleteJob(jobKey);
		JobDataMap map = getJobDataMap(job);
		DynamicJob dynamicJob = (DynamicJob) ApplicationContextProvider.getBean(job.getHandlerName());
		JobDetail jobDetail = getJobDetail(dynamicJob.getClass(), jobKey, job.getDescription(), map);
		scheduler.scheduleJob(jobDetail, getTrigger(job));
	}

	public void refreshAll(String taskType) throws Exception {
		List<?> datas = repository.getByTaskType(taskType);
		if (datas != null && datas.size() > 0) {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			Set<JobKey> set = scheduler.getJobKeys(GroupMatcher.anyGroup());
			scheduler.pauseJobs(GroupMatcher.anyGroup()); // 暂停所有JOB
			for (JobKey jobKey : set) { // 删除从数据库中注册的所有JOB
				scheduler.unscheduleJob(TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup()));
				scheduler.deleteJob(jobKey);
			}
			for (Object data : datas) {
				JobVO job = (JobVO) data;
				JobDataMap map = getJobDataMap(job);
				JobKey jobKey = getJobKey(job);
				DynamicJob dynamicJob = (DynamicJob) ApplicationContextProvider.getBean(job.getHandlerName());
				JobDetail jobDetail = getJobDetail(dynamicJob.getClass(), jobKey, job.getDescription(), map);
				scheduler.scheduleJob(jobDetail, getTrigger(job));

			}
		}
	}

	public void modifyJob(JobVO job) throws Exception {
		JobKey jobKey = getJobKey(job);
		TriggerKey triggerKey = new TriggerKey(jobKey.getName(), jobKey.getGroup());
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		String oldCron = cronTrigger.getCronExpression();
		if (!oldCron.equalsIgnoreCase(job.getCron())) {
			job.setCron(job.getCron());
			CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCron());
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobKey.getName(), jobKey.getGroup())
					.withSchedule(cronScheduleBuilder).usingJobData(getJobDataMap(job)).build();
			scheduler.rescheduleJob(triggerKey, trigger);
			UnieapJobEntity jobEntity = repository.getById(job.getId());
			jobEntity.setCron(job.getCron());
			repository.save(jobEntity);
		}
	}

	public void stopJob(JobVO job) throws Exception {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = getJobKey(job);
		scheduler.pauseJob(jobKey);
	}

	public void restartJob(JobVO job) throws Exception {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = getJobKey(job);
		scheduler.resumeJob(jobKey);
	}

}
