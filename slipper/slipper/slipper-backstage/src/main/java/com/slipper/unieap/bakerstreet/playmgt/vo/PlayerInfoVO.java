package com.slipper.unieap.bakerstreet.playmgt.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.bakerstreet.inf.vo.ScriptInfoVO;
import com.slipper.unieap.exttools.JpaConvert;

import lombok.Data;

@Data
@JpaConvert
public class PlayerInfoVO implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private Long id;
	private String code;
	private String name;
	private String wxauId;
	private String sessionKey;
	private String avatarUrl;
	private String weixin;
	private Date registerDate;

	// 当前的角色信息
	private Long roleId;
	private String roleName;

	private Long playCount;// 打本记录数
	private List<ScriptInfoVO> playerScriptList;
	private String playerType;
	private String playerTypeDesc;

	private Long dmScriptCount;// 开本记录数
	private List<ScriptInfoVO> dmScriptList;
	private String scriptFlag;
	private String scriptFlagDesc;
	private String changeFlag;
	private String changeFlagDesc;
	private String status;
	private String statusDesc;
	private String activeCode;
	private Date activeDate;
	private String blacklistFlag;
	private String blacklistFlagDesc;

	private Date createDate;
	private String activateFlag;
	private String activateFlagDesc;
	private Date modifyDate;
	private String remark;

	public String getActivateFlagDesc() {
		this.activateFlagDesc = UnieapConstants.getDicData("ACTIVATE_FLAG", activateFlag).getDicName();
		return activateFlagDesc;
	}

	public String getPlayerTypeDesc() {
		this.playerTypeDesc = UnieapConstants.getDicData("PLAYER_TYPE", playerType).getDicName();
		return playerTypeDesc;
	}

	public String getStatusDesc() {
		this.statusDesc = UnieapConstants.getDicData("PLAYER_STATUS", status).getDicName();
		return statusDesc;
	}

	public String getBlacklistFlagDesc() {
		this.blacklistFlagDesc = UnieapConstants.getDicData("YESNO_FLAG", blacklistFlag).getDicName();
		return blacklistFlagDesc;
	}

	public String getScriptFlagDesc() {
		this.scriptFlagDesc = UnieapConstants.getDicData("YESNO_FLAG", scriptFlag).getDicName();
		return scriptFlagDesc;
	}

	public String getChangeFlagDesc() {
		this.changeFlagDesc = UnieapConstants.getDicData("YESNO_FLAG", changeFlag).getDicName();
		return changeFlagDesc;
	}

	public Date getActiveDate() {
		if (StringUtils.equals(this.status, "1")) {
			this.activeDate = this.modifyDate;
		}
		return this.activeDate;
	}
}
