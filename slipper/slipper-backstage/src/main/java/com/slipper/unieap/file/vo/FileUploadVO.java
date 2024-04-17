package com.slipper.unieap.file.vo;

import java.io.Serializable;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import lombok.Data;

@Data
public class FileUploadVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CommonsMultipartFile file;
	/**
	 * 处理类
	 */
	private String handlerName;
	/**
	 * 参数
	 */
	private String param;
}
