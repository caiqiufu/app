package com.slipper.unieap.school.renyuanguanli.vo;

import java.io.Serializable;
import java.util.Date;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.exttools.JpaConvert;

import lombok.Data;

@Data
@JpaConvert
public class KidsContactVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private Long kidsId;
	
	private String kidsName;

	private Long contactId;

	private String contactName;

	private String contactNumber;

	private String relationship;
	private String relationshipDesc;

	private String priority;
	private String priorityDesc;

	private boolean checked;

	private String idType;
	private String idTypeDesc;

	private String idNumber;

	private String nation;
	private String nationDesc;

	private String education;
	private String educationDesc;

	private String occupation;

	private String email;
	
	private Long profileId;
	private String profileUrl;
	
	private String sourceType;
	private String sourceTypeDesc;

	// 通用属性
	private String activateFlag;

	private String activateFlagDesc;

	public String getActivateFlagDesc() {
		this.activateFlagDesc = UnieapConstants.getDicData("ACTIVATE_FLAG", activateFlag).getDicName();
		return activateFlagDesc;
	}
	
	public String getIdTypeDesc() {
		this.idTypeDesc = UnieapConstants.getDicData("ID_TYPE", idType).getDicName();
		return idTypeDesc;
	}
	
	public String getEducationDesc() {
		this.educationDesc = UnieapConstants.getDicData("EDUCATION", education).getDicName();
		return educationDesc;
	}
	
	public String getRelationshipDesc() {
		this.relationshipDesc = UnieapConstants.getDicData("CONTACT_RELATIONSHIP", relationship).getDicName();
		return relationshipDesc;
	}
	
	public String getNationDesc() {
		this.nationDesc = UnieapConstants.getDicData("NATION", nation).getDicName();
		return nationDesc;
	}

	private Date createDate;

	private String createBy;

	private Date modifyDate;

	private String modifyBy;

	private Long tenantId;

	private String remark;
}
