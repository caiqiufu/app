package com.slipper.unieap.school.jiaowuguanli.nianji.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slipper.common.utils.R;
import com.slipper.service.annotation.Log;
import com.slipper.service.model.KeysModel;
import com.slipper.unieap.controller.CommonController;
import com.slipper.unieap.school.jiaowuguanli.nianji.bo.GradeBO;
import com.slipper.unieap.school.jiaowuguanli.pojo.GradeInfo;
import com.slipper.unieap.school.jiaowuguanli.vo.GradeInfoVO;
import com.slipper.unieap.vo.PaginationSupport;

@RestController
@RequestMapping("/backstage/jiaowuguanli/nianji")
public class GradeController extends CommonController {

	@Autowired
	GradeBO gradeBO;

	@GetMapping("/getGradeDicData")
	public R getGradeDicData(String params, HttpServletRequest request, HttpServletResponse response) {
		return R.success(gradeBO.getGradeDicDataList());
	}

	@GetMapping("/page")
	public R page(PaginationSupport page, GradeInfoVO vo) throws Exception {
		return R.success(gradeBO.getGridList(page, vo).getMap());
	}

	@GetMapping("/info")
	public R info(Long id) {
		return R.success(gradeBO.getInfo(id));
	}

	@Log("新增班级")
	@RequiresPermissions("jiaowuguanli.nianji:create")
	@PostMapping("/create")
	public R create(@RequestBody GradeInfo vo,String url) throws Exception {
		return R.success(gradeBO.create(vo));
	}

	@Log("更新班级")
	@RequiresPermissions("jiaowuguanli.nianji:update")
	@PostMapping("/update")
	public R update(@RequestBody GradeInfo vo) throws Exception {
		return R.success(gradeBO.update(vo));
	}

	@Log("批量删除班级")
	@RequiresPermissions("jiaowuguanli.nianji:deleteBatch")
	@PostMapping("/deleteBatch")
	public R deleteBatch(@RequestBody KeysModel<Long> keysModel) throws Exception {
		gradeBO.deleteBatch(keysModel.getKeys());
		return R.success();
	}
}
