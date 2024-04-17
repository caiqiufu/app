package com.slipper.service.unieap.base.email;

import java.io.File;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.slipper.service.unieap.base.SYSConfig;

import freemarker.template.Template;

@Service
public class MailBO {
	@Autowired
	private JavaMailSender javaMailSender;
	// 发送邮件的模板引擎
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;

	/**
	 * 
	 * @param from
	 * @param mailvo
	 * @throws Exception
	 */
	public void sendMail(String from, MailVO mailvo) throws Exception {
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
		if (mailvo.getToAddressList() != null && mailvo.getToAddressList().length > 0) {
			InternetAddress[] internetAddressTo = InternetAddress
					.parse(StringUtils.join(mailvo.getToAddressList(), ","));
			helper.setTo(internetAddressTo);
		} else {
			throw new Exception("send address can not be null");
		}

		if (mailvo.getCopyAddressList() != null && mailvo.getCopyAddressList().length > 0) {
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
		this.sendMail(from, newMailvo);
	}

	/**
	 * 
	 * @param from
	 * @param mailvo
	 * @throws Exception
	 */
	public void sendAttachmentMail(String from, MailVO mailvo) throws Exception {
		this.sendMail(from, mailvo);
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
		this.sendMail(from, mailvo);
	}

	public void sendTemplateMail(String from, MailVO mailvo, String templateName, Map<String, Object> model)
			throws Exception {
		MailVO newMailvo = new MailVO();
		// Map<String, Object> model = new HashMap<String, Object>();
		// model.put("content", mailvo.getContent());
		// model.put("title", mailvo.getSubject());
		Template template = freeMarkerConfigurer.getConfiguration().getTemplate(templateName);
		String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
		newMailvo.setContent(text);
		newMailvo.setCopyAddressList(mailvo.getCopyAddressList());
		newMailvo.setSubject(mailvo.getSubject());
		newMailvo.setToAddressList(mailvo.getToAddressList());
		this.sendMail(from, newMailvo);
	}

	/*
	 * boolean auth = true;// 表示 是否需要验证 String from;// 谁发？ String to;// 发给谁？ String
	 * username;// 发件箱用户名 String password;// 发件箱密码 String title;// 标题 String
	 * content;// 内容
	 */
	public void javaSendEmail(String from, MailVO mailvo) throws Exception {
		String username = SYSConfig.getConfig().get("mail.username");
		String password = SYSConfig.getConfig().get("mail.password");
		Properties prop = new Properties();
		prop.put("mail.smtp.host", SYSConfig.getConfig().get("mail.host"));
		prop.put("mail.smtp.port", SYSConfig.getConfig().get("mail.port"));
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.socketFactory.fallback", "false");
		prop.put("mail.smtp.debug", "false");
		prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		prop.put("mail.smtp.socketFactory.port", "465");
		prop.put("mail.transport.protocol", "smtp");
		Authenticator author = new MyAuthenticator(username, password);
		Session session = Session.getDefaultInstance(prop, author);
		// Transport ts = session.getTransport();
		// ts.connect(SYSConfig.getConfig().get("mail.host"), username, password);
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		String[] toAddressList = mailvo.getToAddressList();
		Address[] addresss = new InternetAddress[toAddressList.length];
		for (int i = 0; i < toAddressList.length; i++) {
			addresss[i] = new InternetAddress(toAddressList[i]);
		}
		message.addRecipients(Message.RecipientType.TO, addresss);
		if (mailvo.getSubject() == null) {
			message.setSubject("");
		} else {
			message.setSubject(mailvo.getSubject());
		}
		if (mailvo.getContent() == null) {
			message.setText("");
		} else {
			message.setText(mailvo.getContent());
		}
		Transport.send(message);
		// ts.close();
	}
}
