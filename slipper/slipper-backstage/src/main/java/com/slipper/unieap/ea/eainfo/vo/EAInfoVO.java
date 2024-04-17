package com.slipper.unieap.ea.eainfo.vo;

import java.io.Serializable;

import com.slipper.unieap.exttools.JpaConvert;

import lombok.Data;

@Data
@JpaConvert
public class EAInfoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String eaTime;

	private String profitPoint;
	
	private String orderType;
	
	private String openPrice;
	
	private String closePrice;

	private String eaCommand;
}
