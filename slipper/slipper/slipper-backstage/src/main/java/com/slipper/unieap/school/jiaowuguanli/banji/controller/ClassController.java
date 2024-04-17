package com.slipper.unieap.school.jiaowuguanli.banji.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slipper.common.utils.R;
import com.slipper.service.annotation.Log;
import com.slipper.service.model.KeysModel;
import com.slipper.unieap.controller.CommonController;
import com.slipper.unieap.school.jiaowuguanli.banji.bo.ClassBO;
import com.slipper.unieap.school.jiaowuguanli.vo.ClassInfoVO;
import com.slipper.unieap.vo.PaginationSupport;

@RestController
@RequestMapping("/backstage/jiaowuguanli/banji")
public class ClassController extends CommonController {

	@Autowired
	ClassBO classBO;

	@PostMapping("/getClassDicData")
	public R getClassDicData(Long gradeId, String params, HttpServletRequest request, HttpServletResponse response) {
		return R.success(classBO.getClassDicDataList(gradeId));
	}

	@PostMapping("/page")
	public R page(PaginationSupport page, ClassInfoVO vo) throws Exception {
		return R.success(classBO.getGridList(page, vo).getMap());
	}

	@PostMapping("/info")
	public R info(Long id) throws Exception {
		return R.success(classBO.getInfo(id));
	}

	@Log("新增年级")
	@RequiresPermissions("jiaowuguanli.banji:create")
	@PostMapping("/create")
	public R create(@RequestBody ClassInfoVO vo) throws Exception {
		return R.success(classBO.create(vo));
	}

	@Log("更新年级")
	@RequiresPermissions("jiaowuguanli.banji:update")
	@PostMapping("/update")
	public R update(@RequestBody ClassInfoVO vo) throws Exception {
		return R.success(classBO.update(vo));
	}

	@Log("批量删除年级")
	@RequiresPermissions("jiaowuguanli.banji:deleteBatch")
	@PostMapping("/deleteBatch")
	public R deleteBatch(@RequestBody KeysModel<Long> keysModel) throws Exception {
		classBO.deleteBatch(keysModel.getKeys());
		return R.success();
	}

	@Log("升班")
	@RequiresPermissions("jiaowuguanli.banji:gradeUp")
	@PostMapping("/gradeUp")
	public R gradeUp(@RequestBody ClassInfoVO vo) throws Exception {
		classBO.gradeUp(vo);
		return R.success();
	}

	@Log("升班")
	@RequiresPermissions("jiaowuguanli.banji:graduation")
	@PostMapping("/graduation")
	public R graduation(@RequestBody ClassInfoVO vo) throws Exception {
		classBO.graduation(vo);
		return R.success();
	}
}
