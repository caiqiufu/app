package com.slipper.unieap.email;

import java.io.File;
import java.util.Date;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailBO {
	@Autowired
	private JavaMailSender javaMailSender;

	/**
	 * 
	 * @param from
	 * @param mailvo
	 * @throws Exception
	 */
	public void sendMaile(String from, MailVO mailvo) throws Exception {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
		helper.setFrom(from);
		helper.setSubject(mailvo.getSubject());
		helper.setSentDate(new Date());
		helper.setText(mailvo.getContent(), true);
		File[] files = mailvo.getFiles();
		if (files != null && files.length > 0) {
			for (File file : files) {
				helper.addAttachment(MimeUtility.encodeText(file.getName()), file);
			}
		}
		String[] inlineList = mailvo.getInlineList();
		if (inlineList != null && inlineList.length > 0) {
			for (String inline : inlineList) {
				FileSystemResource file = new FileSystemResource(new File(inline));
				helper.addInline("picture", file);
			}
		}
		if(mailvo.getToAddressList()!=null && mailvo.getToAddressList().length>0) {
			InternetAddress[] internetAddressTo = InternetAddress.parse(StringUtils.join(mailvo.getToAddressList(), ","));
			helper.setTo(internetAddressTo);
		}else {
			throw new Exception("send address can not be null");
		}
		
		if(mailvo.getCopyAddressList()!=null && mailvo.getCopyAddressList().length>0) {
			InternetAddress[] internetAddressCopy = InternetAddress
					.parse(StringUtils.join(mailvo.getCopyAddressList(), ","));
			helper.setCc(internetAddressCopy);
		}		
		javaMailSender.send(mimeMessage);
	}

	/**
	 * 
	 * @param from
	 * @param mailvo
	 * @throws Exception
	 */
	public void sendHtmlMail(String from, MailVO mailvo) throws Exception {
		MailVO newMailvo = new MailVO();
		newMailvo.setContent("<html><head></head><body>" + mailvo.getContent() + "</body></html>");
		newMailvo.setCopyAddressList(mailvo.getCopyAddressList());
		newMailvo.setSubject(mailvo.getSubject());
		newMailvo.setToAddressList(mailvo.getToAddressList());
		this.sendMaile(from, newMailvo);
	}

	/**
	 * 
	 * @param from
	 * @param mailvo
	 * @throws Exception
	 */
	public void sendAttachmentMail(String from, MailVO mailvo) throws Exception {
		this.sendMaile(from, mailvo);
	}

	/**
	 * 
	 * @param from
	 * @param mailvo
	 * @throws Exception
	 */
	public void sendInlineMail(String from, MailVO mailvo) throws Exception {
		// mimeMessageHelper.setText("<html><body>带静态资源的邮件内容，这个一张IDEA配置的照片:<img
		// src='cid:picture' /></body></html>", true);
		// 文件路径
		// FileSystemResource file = new FileSystemResource(new
		// File("src/main/resources/static/image/mail.png"));
		// mimeMessageHelper.addInline("picture", file);
		this.sendMaile(from, mailvo);
	}
}
