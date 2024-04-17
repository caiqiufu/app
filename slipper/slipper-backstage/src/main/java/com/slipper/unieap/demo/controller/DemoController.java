package com.slipper.unieap.demo.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slipper.common.utils.R;
import com.slipper.modules.AbstractController;
import com.slipper.service.annotation.Log;
import com.slipper.service.model.KeysModel;
import com.slipper.unieap.demo.bo.DemoBO;
import com.slipper.unieap.demo.pojo.UnieapDemo;
import com.slipper.unieap.vo.PaginationSupport;

@RestController
@RequestMapping("/backstage/demo")
public class DemoController extends AbstractController {

	@Autowired
	DemoBO demoBO;

	@RequiresPermissions("demo:page")
	@GetMapping("/page")
	public R page(PaginationSupport page, UnieapDemo vo) throws Exception {
		return R.success(demoBO.getGridList(page, vo).getMap());
	}

	@RequiresPermissions("demo:info")
    @GetMapping("/info")
    public R info(Long id) {
        return R.success(demoBO.getInfo(id));
    }
	
	@Log("新增企业")
	@RequiresPermissions("demo:create")
	@PostMapping("/create")
	public R create(@RequestBody UnieapDemo vo) throws Exception {
		demoBO.create(vo);
		return R.success();
	}

	@Log("更新企业")
	@RequiresPermissions("demo:update")
	@PostMapping("/update")
	public R update(@RequestBody UnieapDemo vo) throws Exception {
		demoBO.update(vo);
		return R.success();
	}

	@Log("删除企业")
	@RequiresPermissions("demo:delete")
	@PostMapping("/delete")
	public R delete(@RequestBody UnieapDemo vo) throws Exception {
		demoBO.delete(vo);
		return R.success();
	}
	@Log("批量删除企业")
	@RequiresPermissions("demo:deleteBatch")
	@PostMapping("/deleteBatch")
	public R deleteBatch(@RequestBody KeysModel<Long> keysModel) throws Exception {
		demoBO.deleteBatch(keysModel.getKeys());
		return R.success();
	}
	@Log("更新企业")
	@PostMapping("/email")
	public R email() throws Exception {
		demoBO.email();
		return R.success();
	}
}
