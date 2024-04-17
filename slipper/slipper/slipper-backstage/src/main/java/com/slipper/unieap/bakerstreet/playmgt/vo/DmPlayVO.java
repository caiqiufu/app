package com.slipper.unieap.bakerstreet.playmgt.vo;

import java.io.Serializable;
import java.util.Date;

import com.slipper.unieap.exttools.JpaConvert;
import com.slipper.unieap.utils.DateUtils;

import lombok.Data;

@Data
@JpaConvert
public class DmPlayVO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long playerId;
	private String playerName;
	private Long scriptId;
	private String scriptName;
	private Date startDate;
	private Date endDate;

	private String dmPlayTime;

	public String getDmPlayTime() {
		if (startDate != null && endDate != null) {
			String startDates = DateUtils.formatToStr(startDate, DateUtils.DATETIME_FORMAT);
			String endDates = DateUtils.formatToStr(endDate, DateUtils.DATETIME_FORMAT);
			dmPlayTime = startDates.split(" ")[0] + " " + startDates.split(" ")[1] +"---" + endDates.split(" ")[1];
		}
		return dmPlayTime;
	}
}
