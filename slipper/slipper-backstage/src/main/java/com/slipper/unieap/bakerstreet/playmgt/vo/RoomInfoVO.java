package com.slipper.unieap.bakerstreet.playmgt.vo;

import java.io.Serializable;
import java.util.Date;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.exttools.JpaConvert;

import lombok.Data;

@Data
@JpaConvert
public class RoomInfoVO implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long playerId;
	private String playerName;
	private Long scriptId;
	private String scriptName;
	private Date startDate;
	private Date endDate;

	private Date createDate;
	private String activateFlag;
	private String activateFlagDesc;
	private Date modifyDate;
	private String remark;

	public String getActivateFlagDesc() {
		this.activateFlagDesc = UnieapConstants.getDicData("ACTIVATE_FLAG", activateFlag).getDicName();
		return activateFlagDesc;
	}
}
