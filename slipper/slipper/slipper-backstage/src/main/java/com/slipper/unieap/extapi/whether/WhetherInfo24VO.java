package com.slipper.unieap.extapi.whether;

import java.io.Serializable;

import com.slipper.unieap.exttools.JpaConvert;

import lombok.Data;

@Data
@JpaConvert
public class WhetherInfo24VO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String condition;	
	private String date;
	private String hour;
	private String humidity;
	private String iconDay;
	private String iconNight;
	private String pressure;
	private String realFeel;
	private String temp;
	private String uvi;
	private String windDir;
	private String windSpeed;
	
}
