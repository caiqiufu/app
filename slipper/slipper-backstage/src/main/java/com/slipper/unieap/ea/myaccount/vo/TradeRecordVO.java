package com.slipper.unieap.ea.myaccount.vo;

import java.io.Serializable;

import com.slipper.unieap.exttools.JpaConvert;

import lombok.Data;

@Data
@JpaConvert
public class TradeRecordVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String accountCode;
	private String brokerCode;
	private String brokerName;
	private String autoFlag;
	private String orderType;
	private String volume;
	private String price;
	private String openPrice;
	private String closePrice;
	private String profitAmount;
	private String tradeTime;
}
