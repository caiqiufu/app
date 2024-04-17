package com.slipper.unieap.school.jiaowuguanli.vo;

import java.io.Serializable;
import java.util.Date;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.exttools.JpaConvert;

import lombok.Data;

@Data
@JpaConvert
public class ClassStaffVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long classId;

	private String className;

	private Long staffId;

	private String staffName;

	private String staffType;

	private String activateFlag;

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
