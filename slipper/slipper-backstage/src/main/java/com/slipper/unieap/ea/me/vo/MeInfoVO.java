package com.slipper.unieap.ea.me.vo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Transient;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.ea.myaccount.vo.AccountInfoVO;
import com.slipper.unieap.exttools.JpaConvert;

import lombok.Data;

@Data
@JpaConvert
public class MeInfoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	private Long id;
	private String userCode;
	private String userName;
	private String password;
	private String agentCode;
	private String phoneNumber;
	private String email;
	private String weixin;
	private String whatsapp;
	private String address;
	private String createDate;
	private String activateFlag;
	@Transient
	private String activateFlagDesc;

	public String getActivateFlagDesc() {
		this.activateFlagDesc = UnieapConstants.getDicData("ACTIVATE_FLAG", activateFlag).getDicName();
		return activateFlagDesc;
	}
	private List<AccountInfoVO> accountList;
}
