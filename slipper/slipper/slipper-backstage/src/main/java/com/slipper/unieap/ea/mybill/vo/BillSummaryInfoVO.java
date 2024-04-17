package com.slipper.unieap.ea.mybill.vo;

import java.io.Serializable;
import java.util.List;

import com.slipper.unieap.exttools.JpaConvert;

import lombok.Data;

@Data
@JpaConvert
public class BillSummaryInfoVO implements Serializable {

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
	
	private List<BillDetailInfoVO> billDetailList;
	
	//总账单费用
	private double totalBillAmount;
	//总待支付账费用
	private double totalOutstandingAmount;
	//总已支付金额
	private double totalPaidAmount;
}
