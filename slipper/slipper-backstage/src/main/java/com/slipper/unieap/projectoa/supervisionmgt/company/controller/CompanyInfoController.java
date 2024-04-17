package com.slipper.unieap.projectoa.supervisionmgt.company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slipper.common.utils.R;
import com.slipper.unieap.controller.CommonController;
import com.slipper.unieap.projectoa.supervisionmgt.company.bo.CompanyBO;

@RestController
@RequestMapping("/backstage/projectoa/supervisionmgt/company")
public class CompanyInfoController extends CommonController {

	@Autowired
	CompanyBO companyBO;
	
	@RequestMapping("/getCompanyDicData")
	public R getCompanyDicData() {
		return R.success(companyBO.getCompanyDicDataList());
	}
}
