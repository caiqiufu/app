package com.slipper.unieap.ea.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.slipper.unieap.ea.repository.TradeUserAccountRepository;
import com.slipper.unieap.task.job.DynamicJob;

/**
 * 账户状态更新,如果未缴费,停用账户
 */

@Service
public class TaskUpdateAutoTradeStatus extends DynamicJob {

	@Autowired
	public TradeUserAccountRepository tradeUserAccountRepository;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		updateAccountStatus();
	}

	private void updateAccountStatus() {
		tradeUserAccountRepository.updateAccountStatusWithOutstandingAmount();
	}
}
