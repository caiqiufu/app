package com.slipper.unieap.projectoa.delivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slipper.common.utils.R;
import com.slipper.service.annotation.Log;
import com.slipper.unieap.controller.CommonController;
import com.slipper.unieap.projectoa.delivery.bo.JldailyLogBO;
import com.slipper.unieap.projectoa.delivery.pojo.ApproveLog;
import com.slipper.unieap.projectoa.delivery.vo.JldailyLogVO;

@RestController
@RequestMapping("/backstage/projectoa/delivery/jlDailyLog")
public class JldailyLogController extends CommonController {

	@Autowired
	JldailyLogBO jldailyLogBO;

	@RequestMapping("/info")
	public R info(JldailyLogVO vo) throws Exception {
		return R.success(jldailyLogBO.getInfo(vo));
	}

	@Log("新增监理日志")
	@RequestMapping("/create")
	public R create(@RequestBody JldailyLogVO vo) throws Exception {
		jldailyLogBO.create(vo);
		return R.success();
	}

	@Log("修改监理日志")
	@RequestMapping("/update")
	public R update(@RequestBody JldailyLogVO vo) throws Exception {
		jldailyLogBO.update(vo);
		return R.success();
	}
	
	@Log("审核日志")
	@RequestMapping("/approve")
	public R approve(ApproveLog vo) throws Exception {
		jldailyLogBO.approve(vo);
		return R.success();
	}
}
