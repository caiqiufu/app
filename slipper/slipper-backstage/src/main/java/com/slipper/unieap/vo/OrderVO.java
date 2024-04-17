package com.slipper.unieap.vo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Transient;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.exttools.JpaConvert;

import lombok.Data;

@Data
@JpaConvert
public class OrderVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private Long id;

	private String orderType;

	private Long pendingOrderId;

	private String inputParameters;

	private String outputParameters;

	private String orderStatus;

	private String executeSchedule;

	private String executeClass;

	private String executeResult;

	private Date executeEndDate;

	private Date executeStartDate;

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
