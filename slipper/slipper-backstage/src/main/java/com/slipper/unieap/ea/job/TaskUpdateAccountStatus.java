package com.slipper.unieap.ea.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.slipper.unieap.ea.repository.TradeAutoRepository;
import com.slipper.unieap.task.job.DynamicJob;

/**
 * 账户状态更新,如果未缴费,停用账户
 */

@Service
public class TaskUpdateAccountStatus extends DynamicJob {

	@Autowired
	public TradeAutoRepository tradeAutoRepository;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		updateAutoInfo();
	}

	private void updateAutoInfo() {
		tradeAutoRepository.updateAutoInfo();
	}
}
