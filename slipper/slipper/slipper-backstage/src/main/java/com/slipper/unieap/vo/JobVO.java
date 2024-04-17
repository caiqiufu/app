package com.slipper.unieap.vo;

import java.io.Serializable;
import java.util.Date;

import com.slipper.unieap.exttools.JpaConvert;

import lombok.Data;

@Data
@JpaConvert
public class JobVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String createBy;
	private Date createDate;
	private String cron;
	private String jobGroup;
	private String description;
	private String status;
	private String parameters;
	private String remark;

	private Long taskId;
	private String taskType;
	private String taskDesc;
	private String className;
	private String handlerName;
	private String retryTimes;
	private String activateFlag;
}
