package com.slipper.unieap.jrds;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.slipper.unieap.ApplicationContextProvider;
import com.slipper.unieap.email.MailBO;
import com.slipper.unieap.email.MailVO;

public class EmailHelper {
	public static final Log logger = LogFactory.getLog(EmailHelper.class);
	/**
	 * 发送提醒邮件
	 */
	public static void sendEmail(MailVO mailvo) {
		MailBO mailBO = (MailBO) ApplicationContextProvider.getBean("mailBO");
		//MailVO mailvo = new MailVO();
		//mailvo.setSubject("test");
		//mailvo.setContent("test email");
		mailvo.setToAddressList(new String[] {"caiqiufu@hotmail.com","caiqiufu1@huawei.com","xiebinquan@gacrnd.com"});
		try {
			mailBO.sendMaile("744161932@qq.com", mailvo);
		} catch (Exception e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("邮件发送成功");
	}

}
