package com.slipper.unieap.projectoa.supervisionmgt.employee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slipper.common.utils.R;
import com.slipper.unieap.controller.CommonController;
import com.slipper.unieap.projectoa.supervisionmgt.employee.bo.EmployeeBO;
import com.slipper.unieap.projectoa.supervisionmgt.vo.EmployeeInfoVO;
import com.slipper.unieap.vo.PaginationSupport;

@RestController
@RequestMapping("/backstage/projectoa/supervisionmgt/employeeInfo")
public class EmployeeInfoController extends CommonController {

	@Autowired
	EmployeeBO employeeBO;

	@RequestMapping("/employeeInfoPage")
	public R employeeInfoPage(PaginationSupport page, EmployeeInfoVO vo) throws Exception {
		return R.success(employeeBO.getGridList(page, vo).getMap());
	}
}
