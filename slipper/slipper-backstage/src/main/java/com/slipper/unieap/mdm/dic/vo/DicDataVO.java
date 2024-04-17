package com.slipper.unieap.mdm.dic.vo;

import java.io.Serializable;

import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.exttools.JpaConvert;

import lombok.Data;

@Data
@JpaConvert
public class DicDataVO  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long tenantId;
	private String groupCode;
	private String groupName;
	private String language;
	private String dicCode;
	private String dicName;
	private Integer sort;
	private String activateFlag;
	@Transient
	private String activateFlagDesc;
	private String attr1;
	private String attr2;
	private String attr3;
	private String attr4;
	private String attr5;


	public String getActivateFlagDesc() {
		if (!StringUtils.isEmpty(this.activateFlag)) {
			this.activateFlagDesc = UnieapConstants.getDicData("ACTIVATE_FLAG", activateFlag).getDicName();
		}
		return activateFlagDesc;
	}

	public void setActivateFlagDesc(String activateFlagDesc) {
		this.activateFlagDesc = activateFlagDesc;
	}
}
