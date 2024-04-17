package com.slipper.unieap.projectoa.projectmgt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slipper.common.utils.R;
import com.slipper.service.annotation.Log;
import com.slipper.unieap.controller.CommonController;
import com.slipper.unieap.projectoa.projectmgt.bo.ProjectInfoBO;
import com.slipper.unieap.projectoa.projectmgt.vo.ProjectAttributeVO;
import com.slipper.unieap.projectoa.projectmgt.vo.ProjectLeaderVO;
import com.slipper.unieap.projectoa.projectmgt.vo.TaskInfoVO;

@RestController
@RequestMapping("/backstage/projectoa/projectmgt/projectInfo")
public class ProjectInfoController extends CommonController {

	@Autowired
	ProjectInfoBO projectInfoBO;

	@RequestMapping("/projectDicData")
	public R projectDicData(Long parentId) {
		return R.success(projectInfoBO.getProjectDicDataList(parentId));
	}

	@RequestMapping("/buildingPartDicData")
	public R buildingPartDicData(Long projectId) {
		return R.success(projectInfoBO.getBuildingPartDicDataList(projectId));
	}

	@Log("项目属性列表")
	@RequestMapping("/allAttributeList")
	public R allAttributeList(ProjectAttributeVO vo) throws Exception {
		return R.success(projectInfoBO.getAllAttributeList(vo));
	}

	@Log("分配项目属性")
	@RequestMapping("/assignAttribute")
	public R assignAttribute(@RequestBody ProjectAttributeVO vo) throws Exception {
		projectInfoBO.assignAttribute(vo);
		return R.success();
	}

	@Log("修改属性序号")
	@RequestMapping("/updateSeq")
	public R updateSeq(@RequestBody ProjectAttributeVO vo) throws Exception {
		projectInfoBO.updateSeq(vo);
		return R.success();
	}

	@Log("修改属性值")
	@RequestMapping("/updateAttributeValue")
	public R updateAttributeValue(@RequestBody ProjectAttributeVO vo) throws Exception {
		projectInfoBO.updateAttributeValue(vo);
		return R.success();
	}

	@Log("查看项目明细信息")
	@RequestMapping("/projectAttributeList")
	public R projectAttributeList(ProjectAttributeVO vo) throws Exception {
		return R.success(projectInfoBO.getProjectAttributeList(vo));
	}

	@Log("查看项目责任人")
	@RequestMapping("/getProjectLeaderList")
	public R getProjectLeaderList(Long projectId) throws Exception {
		return R.success(projectInfoBO.getProjectLeaderList(projectId));
	}

	@Log("更新项目责任人")
	@RequestMapping("/updateProjectLeader")
	public R updateProjectLeader(@RequestBody ProjectLeaderVO vo) throws Exception {
		projectInfoBO.updateProjectLeader(vo);
		return R.success();
	}

	@RequestMapping("/allEmployeeDicDataList")
	public R allEmployeeDicDataList() {
		return R.success(projectInfoBO.getAllEmployeeDicDataList());
	}

	@Log("查看项目任務")
	@RequestMapping("/getAllTaskInfoList")
	public R getAllTaskInfoList(TaskInfoVO vo) throws Exception {
		return R.success(projectInfoBO.getAllTaskInfoList(vo));
	}

	@Log("设置项目任务提醒")
	@RequestMapping("/updateTaskInfo")
	public R updateTaskInfo(TaskInfoVO vo) throws Exception {
		projectInfoBO.updateTaskInfo(vo);
		return R.success();
	}
}
