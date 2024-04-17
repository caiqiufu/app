package com.slipper.unieap.email;

import java.io.File;
import java.io.Serializable;

public class MailVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] toAddressList; // 邮件接收人
	private String[] copyAddressList; // 抄送列表
	private String subject; // 邮件主题
	private String content; // 邮件内容
	private File[] files; //附件
	private String[] inlineList;

	public String[] getToAddressList() {
		return toAddressList;
	}

	public void setToAddressList(String[] toAddressList) {
		this.toAddressList = toAddressList;
	}

	public String[] getCopyAddressList() {
		return copyAddressList;
	}

	public void setCopyAddressList(String[] copyAddressList) {
		this.copyAddressList = copyAddressList;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public File[] getFiles() {
		return files;
	}

	public void setFiles(File[] files) {
		this.files = files;
	}

	public String[] getInlineList() {
		return inlineList;
	}

	public void setInlineList(String[] inlineList) {
		this.inlineList = inlineList;
	}

}
