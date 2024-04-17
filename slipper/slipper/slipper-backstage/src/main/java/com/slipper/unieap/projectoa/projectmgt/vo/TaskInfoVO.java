package com.slipper.unieap.projectoa.projectmgt.vo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.exttools.JpaConvert;

import lombok.Data;

@Data
@JpaConvert
public class TaskInfoVO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private Long id;

	private Long taskId;

	private String taskName;
	
	private String taskType;
	
	private String taskTypeDesc;
	
	private String notifyCron;
	
	private String messageTemplate;

	private Long projectId;
	
	private String projectName;

	private String employeeId;

	private String[] employeeIds;

	private String notifyFlag;

	@Transient
	private String notifyFlagDesc;

	public String getNotifyFlagDesc() {
		this.notifyFlagDesc = UnieapConstants.getDicData("YESNO_FLAG", notifyFlag).getDicName();
		return notifyFlagDesc;
	}
	
	public String getTaskTypeDesc() {
		this.taskTypeDesc = UnieapConstants.getDicData("TASK_TYPE", taskType).getDicName();
		return taskTypeDesc;
	}

	public String[] getEmployeeIds() {
		if (StringUtils.isNotEmpty(employeeId)) {
			employeeIds = employeeId.split(",");
		}
		return employeeIds;
	}

	// 通用属性
	private String activateFlag;

	@Transient
	private String activateFlagDesc;

	public String getActivateFlagDesc() {
		this.activateFlagDesc = UnieapConstants.getDicData("ACTIVATE_FLAG", activateFlag).getDicName();
		return activateFlagDesc;
	}

	private Date createDate;

	private String createBy;

	private Date modifyDate;

	private String modifyBy;

	private Long tenantId;

	private String remark;

}
