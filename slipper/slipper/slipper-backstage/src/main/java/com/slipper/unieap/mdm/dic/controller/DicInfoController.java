package com.slipper.unieap.mdm.dic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slipper.common.utils.R;
import com.slipper.service.model.KeyModel;
import com.slipper.unieap.controller.CommonController;
import com.slipper.unieap.mdm.dic.bo.DicBO;
import com.slipper.unieap.vo.TreeVO;

@RestController
@RequestMapping("/backstage/mdm/dic")
public class DicInfoController extends CommonController {

	@Autowired
	DicBO dicBO;

	@RequestMapping("/list")
	public R getDataList() {
		return R.success(dicBO.getGridList());
	}

	@RequestMapping("/info")
	public R info(Long id, String type) {
		return R.success(dicBO.getInfo(id,type));
	}
	@PostMapping("/create")
	public R create(@RequestBody TreeVO createOrUpdateForm) {
		return R.success(dicBO.create(createOrUpdateForm));
	}

	@PostMapping("/update")
	public R update(@RequestBody TreeVO createOrUpdateForm) {
		dicBO.update(createOrUpdateForm);
		return R.success();
	}

	@PostMapping("/delete")
	public R delete(@RequestBody KeyModel<Long> key) {
		dicBO.delete(key.getKey());
		return R.success();
	}

	@PostMapping("/drag")
	public R updateParent(@RequestBody TreeVO updateParentAndSortForm) {
		dicBO.updateParent(updateParentAndSortForm);
		return R.success();
	}
}
