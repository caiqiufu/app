package com.slipper.unieap.ea.myaccount.vo;

import java.io.Serializable;

import javax.persistence.Transient;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.exttools.JpaConvert;

import lombok.Data;

@Data
@JpaConvert
public class TradeAutoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 用户账户关系ID
	 */
	private Long id;
	private String executeIp;
	private String eaIp;
	private String formNo;
	private String accountCode;
	private String loginStatus;
	@Transient
	private String loginStatusDesc;

	public String getLoginStatusDesc() {
		this.loginStatusDesc = UnieapConstants.getDicData("ACTIVATE_FLAG", loginStatus).getDicName();
		return loginStatusDesc;
	}

	private String loginDate;
	private String autoStatus;
	
	private String autoTrade;
	
	@Transient
	private String autoStatusDesc;

	public String getAutoStatusDesc() {
		this.autoStatusDesc = UnieapConstants.getDicData("ACTIVATE_FLAG", autoStatus).getDicName();
		return autoStatusDesc;
	}

	private String autoDate;
	
	private String errorMsg;

}
