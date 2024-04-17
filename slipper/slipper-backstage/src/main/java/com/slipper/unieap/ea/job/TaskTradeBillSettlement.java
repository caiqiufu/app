package com.slipper.unieap.ea.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.slipper.service.unieap.base.email.MailBO;
import com.slipper.service.unieap.base.sms.SMSBO;
import com.slipper.unieap.ea.repository.TradeNotifyRepository;
import com.slipper.unieap.task.job.DynamicJob;

/**
 * 策略提醒
 * 
 * @author caiqi
 *
 */
@Service
public class TaskTradeBillSettlement extends DynamicJob {

	@Autowired
	public SMSBO sMSBO;

	@Autowired
	public MailBO mailBO;

	@Autowired
	public TradeNotifyRepository tradeNotifyRepository;

	@Value("${spring.mail.username}")
	String username;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

	}
}
