package com.slipper.unieap.bakerstreet.scriptmgt.scriptinfo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSON;
import com.slipper.common.utils.R;
import com.slipper.service.annotation.Log;
import com.slipper.unieap.bakerstreet.scriptmgt.pojo.ClueInfo;
import com.slipper.unieap.bakerstreet.scriptmgt.pojo.PictureInfo;
import com.slipper.unieap.bakerstreet.scriptmgt.pojo.ScriptInfo;
import com.slipper.unieap.bakerstreet.scriptmgt.vo.ChapterInfoVO;
import com.slipper.unieap.bakerstreet.scriptmgt.vo.ClueDetailVO;
import com.slipper.unieap.bakerstreet.scriptmgt.vo.RoleInfoVO;
import com.slipper.unieap.bakerstreet.scriptmgt.vo.ScriptInfoVO;
import com.slipper.unieap.controller.CommonController;
import com.slipper.unieap.vo.PaginationSupport;

@RestController
@RequestMapping("/backstage/bakerstreet/scriptmgt/scriptinfo")
public class ScriptInfoController extends CommonController {

	@Autowired
	ScriptInfoBO scriptInfoBO;

	@RequestMapping("/getScriptDicData")
	public R getScriptDicData(ScriptInfoVO vo) {
		return R.success(scriptInfoBO.getScriptDicDataList(vo));
	}

	@RequestMapping("/getScriptDMDicData")
	public R getScriptDMDicData(ScriptInfoVO vo) {
		return R.success(scriptInfoBO.getScriptDMDicDataList(vo));
	}

	@RequestMapping("/getRoleDicData")
	public R getRoleDicData(ScriptInfoVO vo) {
		return R.success(scriptInfoBO.getRoleDicDataList(vo));
	}

	@RequestMapping("/getClueDicData")
	public R getClueDicData(ScriptInfoVO vo) {
		return R.success(scriptInfoBO.getClueDicDataList(vo));
	}

	@RequestMapping("/getChapterNumberDicData")
	public R getChapterNumberDicData(ScriptInfoVO vo) {
		return R.success(scriptInfoBO.getChapterNumberDicDataList(vo));
	}

	@Log("更新剧本上架状态")
	@RequestMapping("/updatePublishFlag")
	public R updatePublishFlag(ScriptInfoVO vo) throws Exception {
		scriptInfoBO.updatePublishFlag(vo);
		return R.success();
	}

	@Log("更新剧本DM列表")
	@RequestMapping("/updateScriptDM")
	public R updateScriptDM(ScriptInfoVO vo, String keys) throws Exception {
		List<Long> lkeys = convertKeys(keys);
		scriptInfoBO.updateScriptDM(vo, lkeys);
		return R.success();
	}

	@RequestMapping("/scriptPage")
	public R scriptPage(PaginationSupport page, ScriptInfoVO vo) throws Exception {
		return R.success(scriptInfoBO.getGridList(page, vo).getMap());
	}

	@RequestMapping("/rolePage")
	public R rolePage(PaginationSupport page, ScriptInfoVO vo) throws Exception {
		return R.success(scriptInfoBO.getRoleGridList(page, vo).getMap());
	}

	@RequestMapping("/scriptInfo")
	public R scriptInfo(@RequestBody ScriptInfoVO vo) throws Exception {
		return R.success(scriptInfoBO.getScriptInfo(vo));
	}

	@Log("新增剧本")
	@RequestMapping("/create")
	public R create(@RequestBody ScriptInfoVO vo) throws Exception {
		vo.setClueInfoList(JSON.parseArray(vo.getClueList(), ClueInfo.class));
		vo.setPosterList(JSON.parseArray(vo.getPoster(), PictureInfo.class));
		vo.setGuidebookList(JSON.parseArray(vo.getGuidebook(), PictureInfo.class));
		scriptInfoBO.create(vo);
		return R.success();
	}

	@Log("编辑剧本")
	@RequestMapping("/update")
	public R update(@RequestBody ScriptInfoVO vo) throws Exception {
		vo.setClueInfoList(JSON.parseArray(vo.getClueList(), ClueInfo.class));
		vo.setPosterList(JSON.parseArray(vo.getPoster(), PictureInfo.class));
		vo.setGuidebookList(JSON.parseArray(vo.getGuidebook(), PictureInfo.class));
		scriptInfoBO.update(vo);
		return R.success();
	}

	@RequestMapping("/roleInfo")
	public R roleInfo(@RequestBody RoleInfoVO vo) throws Exception {
		return R.success(scriptInfoBO.getRoleInfo(vo));
	}

	@Log("新增剧本角色")
	@RequestMapping("/createRole")
	public R createRole(@RequestBody RoleInfoVO vo) throws Exception {
		vo.setFileList(JSON.parseArray(vo.getFile(), PictureInfo.class));
		scriptInfoBO.createRole(vo);
		return R.success();
	}

	@Log("编辑剧本角色")
	@RequestMapping("/editRole")
	public R editRole(@RequestBody RoleInfoVO vo) throws Exception {
		vo.setFileList(JSON.parseArray(vo.getFile(), PictureInfo.class));
		scriptInfoBO.editRole(vo);
		return R.success();
	}

	@RequestMapping("/chapterPage")
	public R chapterPage(PaginationSupport page, ScriptInfo vo) throws Exception {
		return R.success(scriptInfoBO.getChapterGridList(page, vo).getMap());
	}

	@RequestMapping("/chapterInfo")
	public R chapterInfo(@RequestBody ChapterInfoVO vo) throws Exception {
		return R.success(scriptInfoBO.getChapterInfo(vo));
	}

	@Log("新增剧本章节")
	@RequestMapping("/createChapter")
	public R createChapter(@RequestBody ChapterInfoVO vo) throws Exception {
		vo.setFileList(JSON.parseArray(vo.getFile(), PictureInfo.class));
		scriptInfoBO.createChapter(vo);
		return R.success();
	}

	@Log("编辑剧本章节")
	@RequestMapping("/editChapter")
	public R editChapter(@RequestBody ChapterInfoVO vo) throws Exception {
		vo.setFileList(JSON.parseArray(vo.getFile(), PictureInfo.class));
		scriptInfoBO.editChapter(vo);
		return R.success();
	}

	@RequestMapping("/clueDetailPage")
	public R clueDetailPage(PaginationSupport page, ScriptInfo vo) throws Exception {
		return R.success(scriptInfoBO.getClueDetailGridList(page, vo).getMap());
	}

	@RequestMapping("/clueDetailInfo")
	public R clueDetailInfo(@RequestBody ClueDetailVO vo) throws Exception {
		return R.success(scriptInfoBO.getClueDetailInfo(vo));
	}

	@Log("新增剧本幕")
	@RequestMapping("/createClueDetailByBatch")
	public R createClueDetailByBatch(@RequestBody ClueDetailVO vo) throws Exception {
		vo.setFileList(JSON.parseArray(vo.getFile(), PictureInfo.class));
		scriptInfoBO.createClueDetailByBatch(vo);
		return R.success();
	}

	@Log("编辑剧本幕")
	@RequestMapping("/editClueDetail")
	public R editClueDetail(@RequestBody ClueDetailVO vo) throws Exception {
		vo.setFileList(JSON.parseArray(vo.getFile(), PictureInfo.class));
		scriptInfoBO.editClueDetail(vo);
		return R.success();
	}
}
