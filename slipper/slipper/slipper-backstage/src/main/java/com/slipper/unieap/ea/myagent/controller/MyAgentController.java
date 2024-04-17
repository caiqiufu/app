package com.slipper.unieap.ea.myagent.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slipper.common.utils.R;
import com.slipper.service.annotation.Log;
import com.slipper.unieap.controller.CommonController;
import com.slipper.unieap.ea.myagent.bo.MyAgentBO;
import com.slipper.unieap.vo.PaginationSupport;

@RestController
@RequestMapping("/backstage/ea/myagent")
public class MyAgentController extends CommonController {

	@Autowired
	MyAgentBO myAgentBO;

	
	
	@Log("获取用户账单汇总信息")
	@RequestMapping("/getAgentSummaryInfo")
	public R getAgentSummaryInfo(String userCode) {
		return R.success(myAgentBO.getAgentSummaryInfo(userCode));
	}
	
	

	@Log("代理用户明细")
	@RequestMapping("/agentAccountPage")
	public R agentAccountPage(PaginationSupport page, String userCode) throws Exception {
		return R.success(myAgentBO.getAgentGridList(page, userCode).getMap());
	}
}
