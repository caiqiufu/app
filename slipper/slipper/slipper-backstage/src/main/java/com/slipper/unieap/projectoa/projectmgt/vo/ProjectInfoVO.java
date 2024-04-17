package com.slipper.unieap.projectoa.projectmgt.vo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Transient;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.exttools.JpaConvert;

import lombok.Data;

@Data
@JpaConvert
public class ProjectInfoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long projectId;
	private String projectCode;
	private String projectName;
	private Long bidAmount;
	private Date bidDate;
	private String jsdw;
	private Long sgdwId;
	private String sgdw;
	private String jsyt;
	private String jsdd;
	private String jzmj;
	private String duration;
	private Date planstartDate;
	private Date planfinishDate;	
	private String licenseNumber;

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
