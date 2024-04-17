package com.slipper.unieap.demo.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UnieapDemoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String code;

	private String name;

	private String activateFlag;

	private String language;

	private Long groupId;

	private String activateFlagDesc;

	private Date createDate;

	private String createBy;

	private Date modifyDate;

	private String modifyBy;

	private Long tenantId;

	private String remark;
}
