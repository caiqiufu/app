package com.slipper.unieap.ea.myaccount.vo;

import java.io.Serializable;

import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.exttools.JpaConvert;

import lombok.Data;

@Data
@JpaConvert
public class AccountInfoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 用户账户关系ID
	 */
	private Long id;
	private Long userId;
	private String userCode;
	private String userName;
	private String accountCode;
	private String password;
	private String brokerCode;
	private String brokerName;
	private String subscribeFlag;
	private String subscribeStartDate;
	private String eaType;

	private String accountType;

	private String activateFlag;
	@Transient
	private String activateFlagDesc;

	public String getActivateFlagDesc() {
		this.activateFlagDesc = UnieapConstants.getDicData("ACTIVATE_FLAG", activateFlag).getDicName();
		return activateFlagDesc;
	}

	private String createDate;
	private String agentCode;
	private String agentUserCode;
	private String agentUserName;

	// 试用期开始时间
	private String trialStartDate;
	// 试用期结束时间
	private String trialEndDate;

	// 试用期
	@Transient
	private String trialDuration;

	public String getTrialDuration() {
		if (StringUtils.contains(trialStartDate, " ") && StringUtils.contains(trialEndDate, " ")) {
			trialDuration = trialStartDate.split(" ")[0] + " / " + trialEndDate.split(" ")[0];
		}
		return trialDuration;
	}

	// 自动交易手数
	private String valumn;
	// 是否启动自动交易
	private String autoFlag;
	@Transient
	private String autoFlagDesc;

	public String getAutoFlagDesc() {
		this.autoFlagDesc = UnieapConstants.getDicData("AUTO_FLAG", autoFlag).getDicName();
		return autoFlagDesc;
	}

	// 账户余额
	private double accountBalance;
	// 开始账户余额
	private double startBalance;
	// 最终账户余额
	private double endBalance;
	// 账户可用余额
	private double accountEquity;
	// 账户已用保证金
	private double accountMargin;
	// 账户可用保证金
	private double accountFreemargin;
	// 当天盈亏
	private double dailyProfitPoint;
	// 当天盈亏
	private double dailyProfitAmount;
	// 当天初始余额
	private double dailyBalanceStart;
	// 当天最终余额
	private double dailyBalanceEnd;
	// 当天盈利百分比
	private String dailyProfitpercentage;
	// 本周盈亏
	private double weeklyProfitPoint;

	private double weeklyProfitAmount;

	private double weeklyBalanceStart;
	private double weeklyBalanceEnd;
	// 本周盈利百分比
	private String weeklyProfitpercentage;
	// 本月盈亏
	private double monthlyProfitPoint;

	private double monthlyProfitAmount;
	private double monthlyBalanceStart;
	private double monthlyBalanceEnd;
	// 本月盈利百分比
	private String monthlyProfitpercentage;
}
