package com.slipper.unieap.ea.eainfo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slipper.common.utils.R;
import com.slipper.service.annotation.Log;
import com.slipper.unieap.controller.CommonController;
import com.slipper.unieap.ea.eainfo.bo.EAInfoBO;

@RestController
@RequestMapping("/backstage/ea/eainfo")
public class EAInfoController extends CommonController {

	@Autowired
	EAInfoBO eAInfoBO;

	@Log("获取本周EA信息")
	@RequestMapping("/getWeekEAInfo")
	public R getWeekEAInfo(String eaType) {
		return R.success(eAInfoBO.getWeekData(eaType));
	}

	@Log("获取本周EA信息")
	@RequestMapping("/getMonthEAInfo")
	public R getMonthEAInfo(String eaType) {
		return R.success(eAInfoBO.getMonthData(eaType));
	}

	@Log("获取24周EA统计信息")
	@RequestMapping("/get24WeeksEAInfo")
	public R get24WeeksEAInfo(String eaType) {
		return R.success(eAInfoBO.get24WeeksData(eaType));
	}
	@Log("获取24周累积EA统计信息")
	@RequestMapping("/get24WeeksAccuEAInfo")
	public R get24WeeksAccuEAInfo(String eaType) {
		return R.success(eAInfoBO.get24WeeksAccuData(eaType));
	}
}
