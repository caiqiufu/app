package com.slipper.unieap.school.jiaowuguanli.vo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.school.jiaowuguanli.repository.ClassInfoRepository;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GradeInfoVO implements Serializable {

	@Autowired
	public ClassInfoRepository classInfoRepository;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String name;

	private Long classCount;

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
