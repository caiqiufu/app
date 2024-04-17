package com.slipper.unieap.job;

import javax.annotation.Resource;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

import com.slipper.service.modules.log.operation.service.OperationLogService;
import com.slipper.unieap.task.job.DynamicJob;

/**
 * 生成账单任务
 */
@Service
public class ClearOperationLog extends DynamicJob {

	@Resource
	private OperationLogService operationService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		operationService.truncate();
	}
}
