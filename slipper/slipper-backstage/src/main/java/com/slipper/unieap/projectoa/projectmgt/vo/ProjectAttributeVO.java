package com.slipper.unieap.projectoa.projectmgt.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.exttools.JpaConvert;
import com.slipper.unieap.mdm.dic.vo.DicDataVO;

import lombok.Data;

@Data
@JpaConvert
public class ProjectAttributeVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long attributeId;
	private String groupCode;
	private String groupName;
	private String displayName;
	private String attributeName;
	private String uiType;
	private String dicgroupCode;
	private String dataType;
	private String unit;

	// 项目属性
	private Long projectId;
	private Long projectAttributeId;
	private String assigned;
	private Long seq;
	@Transient
	private String assignedDesc;

	// 项目属性值
	private String attributeValue;
	private String attributeValueDesc;
	@Transient
	private List<DicDataVO> dicDataList;

	public String getAttributeValueDesc() {
		if (StringUtils.equals(uiType, "select")) {
			this.attributeValueDesc = UnieapConstants.getDicData(dicgroupCode, attributeValue).getDicName();
		} else {
			attributeValueDesc = attributeValue;
		}
		return attributeValueDesc;
	}

	public List<DicDataVO> getDicDataList() {
		if (StringUtils.equals(uiType, "select")) {
			this.dicDataList = UnieapConstants.getDicGroup(dicgroupCode).getDataList();
		}
		return this.dicDataList;
	}

	public String getAssignedDesc() {
		this.assignedDesc = UnieapConstants.getDicData("ACTIVATE_FLAG", assigned).getDicName();
		return assignedDesc;
	}

	private List<ProjectAttributeVO> children;

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
