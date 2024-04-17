package com.slipper.unieap.ea.eainfo.vo;

import java.io.Serializable;
import java.util.List;

import com.slipper.unieap.exttools.JpaConvert;

import lombok.Data;

@Data
@JpaConvert
public class SerieVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 横坐标数据
	 */
	private List<String> categories;
	/**
	 * 坐标名称
	 */
	private String name;
	/**
	 * 纵坐标数据
	 */
	private List<String> datas;
	/**
	 * 指令明细
	 */
	private List<String> eaCommandList;

	/**
	 * 指令信息
	 */
	private List<EAInfoVO> eaInfoList;

	/**
	 * 总计盈亏点数
	 */
	private String totalProfitPoint;

	/**
	 * 总计订单数
	 */
	private String totalTradeOrder;
	/**
	 * 总计盈利单数
	 */

	private String totalProfitOrder;
	/**
	 * 总计亏损单数
	 */

	private String totalLostOrder;

}
