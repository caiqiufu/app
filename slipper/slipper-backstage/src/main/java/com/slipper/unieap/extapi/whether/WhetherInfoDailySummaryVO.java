package com.slipper.unieap.extapi.whether;

import java.io.Serializable;

import com.slipper.unieap.exttools.JpaConvert;

import lombok.Data;

@Data
@JpaConvert
public class WhetherInfoDailySummaryVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 最高温度
	private String tempMax;
	// 最低温度
	private String tempMin;
	// 上午天气
	private String weatherMorning;

	private String weatherMorningDesc;
	// 下午天气
	private String weatherAfternoon;

	private String weatherAfternoonDesc;

}
