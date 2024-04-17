package com.slipper.unieap.ea.mybill.vo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Transient;

import com.slipper.unieap.exttools.JpaConvert;

import lombok.Data;

@Data
@JpaConvert
public class BillDetailInfoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 用户账单费用关系ID
	 */
	private Long id;

	private Long userId;

	private String userCode;

	private String userName;

	private String accountCode;

	private String feeType;

	private String feeDesc;

	private String billDate;

	private String billStartDate;

	private String billEndDate;

	@Transient
	private String billDuration;

	private String getBillDuration() {
		return billStartDate + "-" + billEndDate;
	}

	private Date createDate;

	private String latestPaymentDate;

	// 初始金额
	private double originalAmount;
	// 账单金额
	private double billAmount;
	// 待缴费金额
	private double outstandingAmount;
	// 已付金额
	private double paidAmount;

}
