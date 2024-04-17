package com.slipper.unieap.demo;

import java.util.Date;
import java.util.Map;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.slipper.unieap.demo.bo.SaveOperlogBO;
import com.slipper.unieap.task.job.DynamicJob;

@Service
public class TaskDemo extends DynamicJob {
	@Autowired
	private SaveOperlogBO saveOperlog;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Date executeStartDate = new Date();
		String executeResult = "no data have been processed";
		String executeStatus = "";
		JobDataMap map = context.getMergedJobDataMap();
		String taskId = map.getString("taskId");
		String jobId = map.getString("jobId");
		System.out.println(map.getWrappedMap().toString());
		Map<String, String> r = saveOperlog.saveOperlog();
		executeResult = r.get("processResult");
		executeStatus = "S";
		Date executeEndDate = new Date();
		/*
		 * this.saveJobExecute(Long.valueOf(taskId), Long.valueOf(jobId),
		 * executeStartDate, executeEndDate, executeStatus, executeResult);
		 */
	}

}
