package com.slipper.unieap.ea.myagent.vo;

import java.io.Serializable;

import javax.persistence.Transient;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.exttools.JpaConvert;

import lombok.Data;

@Data
@JpaConvert
public class AgentAccountInfoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 账户ID
	 */
	private Long id;
	private String accountCode;
	private String userCode;
	private String userName;

	// 订阅
	private String subscribeFlag;
	@Transient
	private String subscribeFlagDesc;
	// 欠费
	private String outstandingFlag;
	// 欠费金额
	private double outstandingAmount;
	// 账户的用户信息
	private String phoneNumber;
	private String weixin;
	private String whatsApp;
	private String email;
	private String address;
	private String subscribeStartDate;
	// 账户余额
	private double accountBalance;

	// 用户状态,启用/停用,停用后不再产生计费
	private String activateFlag;
	@Transient
	private String activateFlagDesc;
	
	public String getActivateFlagDesc() {
		this.activateFlagDesc = UnieapConstants.getDicData("ACTIVATE_FLAG", activateFlag).getDicName();
		return activateFlagDesc;
	}
}
