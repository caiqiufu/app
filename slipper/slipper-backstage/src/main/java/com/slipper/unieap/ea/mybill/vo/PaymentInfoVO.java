package com.slipper.unieap.ea.mybill.vo;

import java.io.Serializable;

import com.slipper.unieap.exttools.JpaConvert;

import lombok.Data;

@Data
@JpaConvert
public class PaymentInfoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 账单ID
	 */
	private Long id;

	private String userName;

	private String accountCode;

	private String payMethod;
	// 已付金额
	private Double paidAmount;

	private String accountNo;

	private String screenshotUrl;

}
