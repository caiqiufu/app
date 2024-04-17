package com.slipper.unieap.task.web;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slipper.unieap.ApplicationContextProvider;
import com.slipper.unieap.pojo.UnieapJobEntity;
import com.slipper.unieap.repository.UnieapJobEntityRepository;
import com.slipper.unieap.task.job.DynamicJob;
import com.slipper.unieap.task.service.DynamicJobService;
import com.slipper.unieap.vo.JobVO;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class JobController {

	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	@Autowired
	private DynamicJobService jobService;

	@Autowired
	private UnieapJobEntityRepository repository;

	// 根据ID重启某个Job
	@RequestMapping("/refresh/{id}")
	public String refresh(@PathVariable @NotNull Long id) throws SchedulerException {
		String result;
		JobVO entity = getJobVO(id);
		if (Objects.isNull(entity))
			return "error: id is not exist ";
		synchronized (log) {
			JobKey jobKey = jobService.getJobKey(entity);
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			scheduler.pauseJob(jobKey);
			scheduler.unscheduleJob(TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup()));
			scheduler.deleteJob(jobKey);
			JobDataMap map = jobService.getJobDataMap(entity);
			DynamicJob dynamicJob = (DynamicJob) ApplicationContextProvider.getBean(entity.getHandlerName());
			JobDetail jobDetail = jobService.getJobDetail(dynamicJob.getClass(), jobKey, entity.getDescription(), map);
			if (entity.getStatus().equals("OPEN")) {
				scheduler.scheduleJob(jobDetail, jobService.getTrigger(entity));
				result = "Refresh Job : " + entity.getDescription() + "\t jarPath: " + entity.getDescription()
						+ " success !";
			} else {
				result = "Refresh Job : " + entity.getDescription() + "\t jarPath: " + entity.getDescription()
						+ " failed ! , " + "Because the Job status is " + entity.getStatus();
			}
		}
		return result;
	}

	public JobVO getJobVO(Long id) {
		UnieapJobEntity entity = repository.getById(id);
		JobVO vo = new JobVO();
		vo.setId(entity.getId());
		vo.setJobGroup(entity.getJobGroup());
		return vo;
	}

}
