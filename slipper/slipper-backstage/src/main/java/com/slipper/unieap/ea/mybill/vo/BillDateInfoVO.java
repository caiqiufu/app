package com.slipper.unieap.ea.mybill.vo;

import java.io.Serializable;

import javax.persistence.Transient;

import com.slipper.unieap.exttools.JpaConvert;

import lombok.Data;

@Data
@JpaConvert
public class BillDateInfoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//账期
	private String billDate;
    //账单起始日期
	private String billStartDate;
    //账单结束日期
	private String billEndDate;
	//最晚缴费日期
	private String latestPaymentDate;

	@Transient
	private String billDuration;

	private String getBillDuration() {
		return billStartDate + "-" + billEndDate;
	}

	private String createDate;

}
