package com.slipper.netty.unieap.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class RequestBodyEntity implements Serializable {
	/**
	 * 序列化字段
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 消息
	 */
	private MessageEntity message;

}
