package com.slipper.netty.unieap.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class MessageEntity implements Serializable {
	/**
	 * 序列化字段
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 消息确认字段
	 */
	private String ack;
	/**
	 * 发送者
	 */
	private String from;
	/**
	 * 接收者
	 */
	private String to;
	/**
	 * 接收类型 1-私聊 2-群聊
	 */
	private Integer type;
	/**
	 * 消息内容
	 */
	private String content;
	/**
	 * 消息类型：1-text 2-image 3-video
	 */
	private Integer messageType;
	/**
	 * 资源路径
	 */
	private String url;
}
