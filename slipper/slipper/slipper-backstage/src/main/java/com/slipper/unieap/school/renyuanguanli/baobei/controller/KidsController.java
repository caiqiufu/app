package com.slipper.unieap.school.renyuanguanli.baobei.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSON;
import com.slipper.common.utils.R;
import com.slipper.service.annotation.Log;
import com.slipper.unieap.controller.CommonController;
import com.slipper.unieap.school.renyuanguanli.baobei.bo.DistrictInfoBO;
import com.slipper.unieap.school.renyuanguanli.baobei.bo.KidsBO;
import com.slipper.unieap.school.renyuanguanli.pojo.BindTimecard;
import com.slipper.unieap.school.renyuanguanli.pojo.RegisterInfo;
import com.slipper.unieap.school.renyuanguanli.vo.KidsContactVO;
import com.slipper.unieap.school.renyuanguanli.vo.KidsInfoVO;
import com.slipper.unieap.vo.PaginationSupport;

@RestController
@RequestMapping("/backstage/renyuanguanli/baobei")
public class KidsController extends CommonController {

	@Autowired
	KidsBO kidsBO;

	@Autowired
	DistrictInfoBO districtInfoBO;

	@PostMapping("/page")
	public R page(PaginationSupport page, KidsInfoVO vo) throws Exception {
		return R.success(kidsBO.getGridList(page, vo).getMap());
	}

	@PostMapping("/getDistrictDicData")
	public R getDistrictDicData(Long parentId, String params, HttpServletRequest request,
			HttpServletResponse response) {
		return R.success(districtInfoBO.getDistrictDicDataList(parentId));
	}

	@PostMapping("/kidsInfo")
	public R kidsInfo(Long id) throws Exception {
		return R.success(kidsBO.getKidsInfo(id));
	}

	@Log("新增宝贝")
	@RequiresPermissions("renyuanguanli.baobei:create")
	@PostMapping("/create")
	public R create(@RequestBody KidsInfoVO vo) throws Exception {
		vo.setContactsInfoList(JSON.parseArray(vo.getKidsContact(), KidsContactVO.class));
		vo.setTimecardInfoList(JSON.parseArray(vo.getBindTimecard(), BindTimecard.class));
		return R.success(kidsBO.create(vo));
	}

	@Log("更新宝贝")
	@RequiresPermissions("renyuanguanli.baobei:update")
	@PostMapping("/update")
	public R update(@RequestBody KidsInfoVO vo) throws Exception {
		return R.success(kidsBO.update(vo));
	}

	@PostMapping("/registerInfo")
	public R registerInfo(Long id) throws Exception {
		return R.success(kidsBO.getRegisterInfo(id));
	}

	@Log("新增学籍")
	@RequiresPermissions("renyuanguanli.baobei:create")
	@PostMapping("/createRegister")
	public R createRegisterInfo(@RequestBody RegisterInfo vo) throws Exception {
		return R.success(kidsBO.createRegisterInfo(vo));
	}

	@Log("更新学籍")
	@RequiresPermissions("renyuanguanli.baobei:update")
	@PostMapping("/updateRegister")
	public R updateRegisterInfo(@RequestBody RegisterInfo vo) throws Exception {
		return R.success(kidsBO.updateRegisterInfo(vo));
	}

	@PostMapping("/contactInfoList")
	public R contactInfoListApi(Long id) throws Exception {
		return R.success(kidsBO.getContactInfoList(id));
	}

	@PostMapping("/contactInfo")
	public R contactInfoApi(Long id) throws Exception {
		return R.success(kidsBO.getContactInfo(id));
	}

	@Log("新增联系人")
	@RequiresPermissions("renyuanguanli.baobei:create")
	@PostMapping("/createContact")
	public R createContactInfo(@RequestBody KidsContactVO vo) throws Exception {
		return R.success(kidsBO.createContactInfo(vo));
	}

	@Log("更新联系人")
	@RequiresPermissions("renyuanguanli.baobei:update")
	@PostMapping("/updateContact")
	public R updateContactInfo(@RequestBody KidsContactVO vo) throws Exception {
		return R.success(kidsBO.updateContactInfo(vo));
	}

	@Log("删除联系人")
	@RequiresPermissions("renyuanguanli.baobei:update")
	@PostMapping("/deleteContact")
	public R deleteContactInfo(@RequestBody KidsContactVO vo) throws Exception {
		return R.success(kidsBO.deleteContactInfo(vo));
	}

	@Log("设置为第一联系人")
	@RequiresPermissions("renyuanguanli.baobei:update")
	@PostMapping("/setFirstContact")
	public R setFirstContactInfo(@RequestBody KidsContactVO vo) throws Exception {
		return R.success(kidsBO.setFirstContactInfo(vo));
	}

	@Log("设置为第一联系人")
	@RequiresPermissions("renyuanguanli.baobei:update")
	@PostMapping("/resetPasswordContact")
	public R resetPasswordContactInfo(@RequestBody KidsContactVO vo) throws Exception {
		return R.success(kidsBO.resetPasswordContactInfo(vo));
	}

	@Log("绑定考勤卡")
	@RequiresPermissions("renyuanguanli.baobei:update")
	@PostMapping("/bindTimecard")
	public R bindTimecard(@RequestBody KidsInfoVO vo) throws Exception {
		vo.setTimecardInfoList(JSON.parseArray(vo.getBindTimecard(), BindTimecard.class));
		return R.success(kidsBO.bindTimecard(vo));
	}
}
