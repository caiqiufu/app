package com.slipper.unieap.school.jiaowuguanli.vo;

import java.io.Serializable;
import java.util.Date;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.exttools.JpaConvert;

import lombok.Data;

@Data
@JpaConvert
public class ClassInfoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String className;

	private Long gradeId;

	private String gradeName;

	private Long mainTeacherId;

	private String mainTeacherName;

	private Long nurseId;

	private String nurseName;

	/**
	 * 多个用逗号(,)分割
	 */
	private String assistTeachers;

	private String assistTeachersName;
	/**
	 * 多个用逗号(,)分割
	 */

	private String othertTeachers;

	private String othertTeachersName;

	private String graduationFlag;
	/**
	 * 1人(男:女 = 1 : 0)
	 */
	private String kidsCount;

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
