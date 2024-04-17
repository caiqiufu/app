package com.slipper.unieap.ea.myagent.vo;

import java.io.Serializable;

import com.slipper.unieap.exttools.JpaConvert;

import lombok.Data;

/**
 * 代理汇总信息,包括代理用户数,订阅数,欠费,收入
 */
@Data
@JpaConvert
public class AgentSummaryInfoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 账户ID
	 */
	private Long id;
	//代理账户总数
	private String totalNumber;
	//代理账户总订阅数
	private String subscribedNumber;
	//代理账户总欠费数
	private String outstandingNumber;
	//代理当月日期
	private String revenueMonth;
	//代理当月收入
	private double revenueAmount;
	//代理总收入
	private double totalRevenueAmount;
}
