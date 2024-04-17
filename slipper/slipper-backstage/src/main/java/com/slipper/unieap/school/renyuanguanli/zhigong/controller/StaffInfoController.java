package com.slipper.unieap.school.renyuanguanli.zhigong.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slipper.common.utils.R;
import com.slipper.unieap.controller.CommonController;
import com.slipper.unieap.school.renyuanguanli.zhigong.bo.StaffInfoBO;

@RestController
@RequestMapping("/backstage/renyuanguanli/staff")
public class StaffInfoController extends CommonController {

	@Autowired
	StaffInfoBO staffInfoBO;

	@PostMapping("/getStaffInfoDicDataList")
	public R getStaffInfoDicDataList(String params, HttpServletRequest request, HttpServletResponse response) {
		return R.success(staffInfoBO.getStaffInfoDicDataList());
	}

}
