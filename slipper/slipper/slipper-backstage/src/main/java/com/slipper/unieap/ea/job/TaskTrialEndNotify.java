package com.slipper.unieap.ea.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.slipper.service.unieap.base.email.MailBO;
import com.slipper.service.unieap.base.email.MailVO;
import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.ea.repository.TradeUserAccountRepository;
import com.slipper.unieap.task.job.DynamicJob;

/**
 * 账户状态更新,如果未缴费,停用账户
 */

@Service
public class TaskTrialEndNotify extends DynamicJob {

	@Autowired
	public TradeUserAccountRepository tradeUserAccountRepository;

	@Autowired
	public MailBO mailBO;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		trialEndNotify();
	}

	@Value("${spring.mail.username}")
	String username;
	@Value("${app_lzy.servicePhoneNumber}")
	String servicePhoneNumber;
	@Value("${app_lzy.serviceEmail}")
	String serviceEmail;

	private void trialEndNotify() {
		List<Map<String, String>> emailList = tradeUserAccountRepository.getAllTrialEndUserList();
		if (emailList != null && emailList.size() > 0) {
			for (int i = 0; i < emailList.size(); i++) {
				String email = emailList.get(i).get("email");
				String userName = emailList.get(i).get("userName");
				String accountCode = emailList.get(i).get("accountCode");
				String trialEndDate = emailList.get(i).get("trialEndDate");
				MailVO mailvo = new MailVO();
				mailvo.setSubject("【深圳龙知易科技】账户试用到期提醒");
				mailvo.setToAddressList(new String[] { email });
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("title", mailvo.getSubject());
				model.put("userName", userName);
				model.put("accountCode", accountCode);
				model.put("trialEndDate", trialEndDate);
				/// 公共信息
				model.put("LZY_APP_URL", UnieapConstants.SYS_DATA.get("LZY_APP_URL"));
				model.put("servicePhoneNumber", servicePhoneNumber);
				model.put("serviceEmail", serviceEmail);
				try {
					mailBO.sendTemplateMail(username, mailvo, "trial-end-email.ftl", model);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}
}
