package com.slipper.unieap.projectoa.supervisionmgt.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import com.slipper.unieap.UnieapConstants;
import com.slipper.service.modules.role.entity.RoleEntity;
import com.slipper.unieap.exttools.JpaConvert;

import lombok.Data;

@Data
@JpaConvert
public class EmployeeInfoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long companyId;
	private String companyName;
	private String name;
	private String gender;
	private String genderDesc;
	private String avatarUrl;
	private String position;
	private String positionDesc;
	private String idNumber;
	private String phone;
	private String wx;
	private String certificateType;
	private String certificateTypeDesc;
	private String certificateLicense;
	private List<RoleEntity> roleList;

	private String activateFlag;
	@Transient
	private String activateFlagDesc;

	public String getGenderDesc() {
		this.genderDesc = UnieapConstants.getDicData("GENDER", gender).getDicName();
		return genderDesc;
	}
	
	public String getPositionDesc() {
		this.positionDesc = UnieapConstants.getDicData("EMPLOYEE_POSITION", position).getDicName();
		return positionDesc;
	}
	
	public String getCertificateTypeDesc() {
		this.certificateTypeDesc = UnieapConstants.getDicData("CERTIFICATE_TYPE", certificateType).getDicName();
		return certificateTypeDesc;
	}
	
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
