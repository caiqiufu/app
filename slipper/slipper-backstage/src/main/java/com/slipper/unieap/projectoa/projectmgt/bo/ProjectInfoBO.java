package com.slipper.unieap.projectoa.projectmgt.bo;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.bo.BaseBO;
import com.slipper.unieap.mdm.dic.vo.DicDataVO;
import com.slipper.unieap.projectoa.delivery.pojo.FieldDefaultvalue;
import com.slipper.unieap.projectoa.delivery.repository.FieldDefaultvalueRepository;
import com.slipper.unieap.projectoa.projectmgt.pojo.BuildingPart;
import com.slipper.unieap.projectoa.projectmgt.pojo.ProjectAttribute;
import com.slipper.unieap.projectoa.projectmgt.pojo.ProjectInfo;
import com.slipper.unieap.projectoa.projectmgt.pojo.ProjectLeader;
import com.slipper.unieap.projectoa.projectmgt.pojo.TaskInfo;
import com.slipper.unieap.projectoa.projectmgt.repository.AttributeDefineRepository;
import com.slipper.unieap.projectoa.projectmgt.repository.BuildingPartRepository;
import com.slipper.unieap.projectoa.projectmgt.repository.ProjectAttributeRepository;
import com.slipper.unieap.projectoa.projectmgt.repository.ProjectInfoRepository;
import com.slipper.unieap.projectoa.projectmgt.repository.ProjectLeaderRepository;
import com.slipper.unieap.projectoa.projectmgt.repository.TaskInfoRepository;
import com.slipper.unieap.projectoa.projectmgt.vo.ProjectAttributeVO;
import com.slipper.unieap.projectoa.projectmgt.vo.ProjectLeaderVO;
import com.slipper.unieap.projectoa.projectmgt.vo.TaskInfoVO;
import com.slipper.unieap.projectoa.supervisionmgt.pojo.EmployeeInfo;
import com.slipper.unieap.projectoa.supervisionmgt.pojo.TaskDefine;
import com.slipper.unieap.projectoa.supervisionmgt.repository.EmployeeInfoRepository;
import com.slipper.unieap.projectoa.supervisionmgt.repository.TaskDefineRepository;

@Service
public class ProjectInfoBO extends BaseBO {

	@Autowired
	public ProjectInfoRepository projectInfoRepository;

	@Autowired
	public ProjectAttributeRepository projectAttributeRepository;

	@Autowired
	public AttributeDefineRepository attributeDefineRepository;

	@Autowired
	public ProjectLeaderRepository projectLeaderRepository;

	@Autowired
	public BuildingPartRepository buildingPartRepository;

	@Autowired
	public FieldDefaultvalueRepository fieldDefaultvalueRepository;

	@Autowired
	public TaskInfoRepository taskInfoRepository;

	@Autowired
	public TaskDefineRepository taskDefineRepository;

	@Autowired
	public EmployeeInfoRepository employeeInfoRepository;

	public List<DicDataVO> getProjectDicDataList(Long parentId) {
		List<ProjectInfo> datas = projectInfoRepository.findByParentIdOrderByName(parentId);
		if (datas != null && datas.size() > 0) {
			List<DicDataVO> dataList = new ArrayList<DicDataVO>();
			for (ProjectInfo data : datas) {
				DicDataVO datavo = new DicDataVO();
				datavo.setActivateFlag(data.getActivateFlag());
				datavo.setDicCode(data.getId().toString());
				datavo.setDicName(data.getName());
				dataList.add(datavo);
			}
			return dataList;
		} else {
			return new ArrayList<DicDataVO>();
		}
	}

	public List<DicDataVO> getBuildingPartDicDataList(Long projectId) {
		List<BuildingPart> datas = buildingPartRepository.findByProjectIdOrderByNameAsc(projectId);
		if (datas != null && datas.size() > 0) {
			List<DicDataVO> dataList = new ArrayList<DicDataVO>();
			for (BuildingPart data : datas) {
				DicDataVO datavo = new DicDataVO();
				datavo.setActivateFlag(data.getActivateFlag());
				datavo.setDicCode(data.getId().toString());
				datavo.setDicName(data.getName());
				dataList.add(datavo);
			}
			return dataList;
		} else {
			return new ArrayList<DicDataVO>();
		}
	}

	/**
	 * 项目属性列表
	 * 
	 * @param vo
	 * @return
	 */
	public List<ProjectAttributeVO> getAllAttributeList(ProjectAttributeVO vo) {
		List<ProjectAttributeVO> datas = projectAttributeRepository.getAllAttributeList(vo.getDisplayName());
		List<ProjectAttributeVO> group = new ArrayList<ProjectAttributeVO>();
		if (datas != null && datas.size() > 0) {
			Map<String, ProjectAttributeVO> groupMap = new HashMap<String, ProjectAttributeVO>();
			ProjectAttributeVO groupVO;
			long groupId = 0;
			for (ProjectAttributeVO data : datas) {
				if (groupMap.containsKey(data.getGroupCode())) {
					groupVO = groupMap.get(data.getGroupCode());
				} else {
					groupVO = new ProjectAttributeVO();
					groupVO.setAttributeId(Long.valueOf(groupId));
					groupVO.setGroupCode(data.getGroupCode());
					groupVO.setGroupName(data.getGroupName());
					groupMap.put(data.getGroupCode(), groupVO);
					group.add(groupVO);
					groupId++;
				}
				if (groupVO.getChildren() == null) {
					groupVO.setChildren(new ArrayList<ProjectAttributeVO>());
				}
				groupVO.getChildren().add(data);
			}
		}
		return group;
	}

	public List<ProjectAttributeVO> getProjectAttributeList(ProjectAttributeVO vo) {
		List<ProjectAttributeVO> datas = projectAttributeRepository.getProjectAttributeList(vo.getProjectId(),
				vo.getDisplayName());
		List<ProjectAttributeVO> group = new ArrayList<ProjectAttributeVO>();
		if (datas != null && datas.size() > 0) {
			Map<String, ProjectAttributeVO> groupMap = new HashMap<String, ProjectAttributeVO>();
			ProjectAttributeVO groupVO;
			long groupId = 0;
			for (ProjectAttributeVO data : datas) {
				if (groupMap.containsKey(data.getGroupCode())) {
					groupVO = groupMap.get(data.getGroupCode());
				} else {
					groupVO = new ProjectAttributeVO();
					groupVO.setAttributeId(Long.valueOf(groupId));
					groupVO.setGroupCode(data.getGroupCode());
					groupVO.setGroupName(data.getGroupName());
					groupMap.put(data.getGroupCode(), groupVO);
					group.add(groupVO);
					groupId++;
				}
				if (groupVO.getChildren() == null) {
					groupVO.setChildren(new ArrayList<ProjectAttributeVO>());
				}
				groupVO.getChildren().add(data);
			}
		}
		return group;
	}

	public void assignAttribute(ProjectAttributeVO vo) throws Exception {
		if (StringUtils.equals(vo.getAssigned(), UnieapConstants.YES)) {
			ProjectAttribute pojo = new ProjectAttribute();
			pojo.setId(UnieapConstants.getSeq(null));
			pojo.setAttributeId(vo.getAttributeId());
			pojo.setProjectId(vo.getProjectId());
			pojo.setSeq(projectAttributeRepository.getSeq(vo.getProjectId()));
			pojo.setActivateFlag(UnieapConstants.YES);
			pojo.setTenantId(UnieapConstants.getTenantId());
			projectAttributeRepository.save(pojo);
		} else {
			projectAttributeRepository.deleteById(vo.getProjectAttributeId());
		}
	}

	public void updateSeq(ProjectAttributeVO vo) throws Exception {
		if (StringUtils.equals(vo.getAssigned(), UnieapConstants.YES)) {
			ProjectAttribute projectAttribute = projectAttributeRepository.getById(vo.getProjectAttributeId());
			projectAttribute.setSeq(vo.getSeq());
			projectAttributeRepository.save(projectAttribute);
		}
	}

	public void updateAttributeValue(ProjectAttributeVO vo) throws Exception {
		if (vo.getProjectAttributeId() != null) {
			ProjectAttribute projectAttribute = projectAttributeRepository.getById(vo.getProjectAttributeId());
			projectAttribute.setAttributeValue(vo.getAttributeValue());
			projectAttributeRepository.save(projectAttribute);
		}
	}

	public ProjectLeaderVO getProjectLeaderList(Long projectId) {
		ProjectLeaderVO projectLeaderVO = new ProjectLeaderVO();
		List<ProjectLeader> datas = projectLeaderRepository.getProjectLeaderList(projectId);
		if (datas != null && datas.size() > 0) {
			for (ProjectLeader projectLeader : datas) {
				if (StringUtils.equals(projectLeader.getCompanyType(), "1")) {
					projectLeaderVO.setJsdw(projectLeader);
				}
				if (StringUtils.equals(projectLeader.getCompanyType(), "2")) {
					projectLeaderVO.setKcdw(projectLeader);
				}
				if (StringUtils.equals(projectLeader.getCompanyType(), "3")) {
					projectLeaderVO.setSjdw(projectLeader);
				}
				if (StringUtils.equals(projectLeader.getCompanyType(), "4")) {
					projectLeaderVO.setJldw(projectLeader);
				}
				if (StringUtils.equals(projectLeader.getCompanyType(), "5")) {
					projectLeaderVO.setSgdw(projectLeader);
				}
			}
		}
		return projectLeaderVO;
	}

	public void updateProjectLeader(ProjectLeaderVO vo) {
		ProjectLeader jsdw = vo.getJsdw();
		if (jsdw != null) {
			if (jsdw.getId() == null) {
				jsdw.setId(UnieapConstants.getSeq(null));
				jsdw.setProjectId(vo.getProjectId());
				jsdw.setCompanyType("1");
				jsdw.setSeq(Long.decode("1"));
				jsdw.setActivateFlag(UnieapConstants.YES);
				jsdw.setTenantId(UnieapConstants.getTenantId());
				projectLeaderRepository.save(jsdw);
			} else {
				ProjectLeader ojsdw = projectLeaderRepository.getById(jsdw.getId());
				ojsdw.setCompanyName(jsdw.getCompanyName());
				ojsdw.setLeaderName(jsdw.getLeaderName());
				ojsdw.setPhone(jsdw.getPhone());
				projectLeaderRepository.save(ojsdw);
			}
		}
		ProjectLeader kcdw = vo.getKcdw();
		if (kcdw != null) {
			if (kcdw != null && kcdw.getId() == null) {
				kcdw.setId(UnieapConstants.getSeq(null));
				kcdw.setProjectId(vo.getProjectId());
				kcdw.setCompanyType("2");
				kcdw.setSeq(Long.decode("2"));
				kcdw.setActivateFlag(UnieapConstants.YES);
				kcdw.setTenantId(UnieapConstants.getTenantId());
				projectLeaderRepository.save(kcdw);
			} else {
				ProjectLeader okcdw = projectLeaderRepository.getById(kcdw.getId());
				okcdw.setCompanyName(kcdw.getCompanyName());
				okcdw.setLeaderName(kcdw.getLeaderName());
				okcdw.setPhone(kcdw.getPhone());
				projectLeaderRepository.save(okcdw);
			}
		}
		ProjectLeader sjdw = vo.getSjdw();
		if (sjdw != null) {
			if (sjdw != null && sjdw.getId() == null) {
				sjdw.setId(UnieapConstants.getSeq(null));
				sjdw.setProjectId(vo.getProjectId());
				sjdw.setCompanyType("3");
				sjdw.setSeq(Long.decode("3"));
				sjdw.setActivateFlag(UnieapConstants.YES);
				sjdw.setTenantId(UnieapConstants.getTenantId());
				projectLeaderRepository.save(sjdw);
			} else {
				ProjectLeader osjdw = projectLeaderRepository.getById(sjdw.getId());
				osjdw.setCompanyName(sjdw.getCompanyName());
				osjdw.setLeaderName(sjdw.getLeaderName());
				osjdw.setPhone(sjdw.getPhone());
				projectLeaderRepository.save(sjdw);
			}
		}
		ProjectLeader jldw = vo.getJldw();
		if (jldw != null) {
			if (jldw != null && jldw.getId() == null) {
				jldw.setId(UnieapConstants.getSeq(null));
				jldw.setProjectId(vo.getProjectId());
				jldw.setCompanyType("4");
				jldw.setSeq(Long.decode("4"));
				jldw.setActivateFlag(UnieapConstants.YES);
				jldw.setTenantId(UnieapConstants.getTenantId());
				projectLeaderRepository.save(jldw);
			} else {
				ProjectLeader ojldw = projectLeaderRepository.getById(jldw.getId());
				ojldw.setCompanyName(jldw.getCompanyName());
				ojldw.setLeaderName(jldw.getLeaderName());
				ojldw.setPhone(jldw.getPhone());
				projectLeaderRepository.save(ojldw);
			}
		}
		ProjectLeader sgdw = vo.getSgdw();
		if (sgdw != null) {
			if (sgdw != null && sgdw.getId() == null) {
				sgdw.setId(UnieapConstants.getSeq(null));
				sgdw.setProjectId(vo.getProjectId());
				sgdw.setCompanyType("5");
				sgdw.setSeq(Long.decode("5"));
				sgdw.setActivateFlag(UnieapConstants.YES);
				sgdw.setTenantId(UnieapConstants.getTenantId());
				projectLeaderRepository.save(sgdw);
			} else {
				ProjectLeader ojldw = projectLeaderRepository.getById(sgdw.getId());
				ojldw.setCompanyName(sgdw.getCompanyName());
				ojldw.setLeaderName(sgdw.getLeaderName());
				ojldw.setPhone(sgdw.getPhone());
				projectLeaderRepository.save(sgdw);
			}
		}
	}

	public String getFieldDefaultValue(Long projectId, String fieldCode, Object[] params) {
		String temp = this.getfieldDefaultValueTemplate(projectId, fieldCode);
		return MessageFormat.format(temp, params);
	}

	/**
	 * 缓存字典值，默认采用groupCode 作为key
	 * 
	 * @param groupCode
	 * @return
	 */
	@Cacheable(cacheNames = { "field_defaultvalue_cache" }, key = "#projectId+'_'+#fieldCode")
	public String getfieldDefaultValueTemplate(Long projectId, String fieldCode) {
		List<FieldDefaultvalue> datas = fieldDefaultvalueRepository.findByProjectIdAndFieldCode(projectId, fieldCode);
		if (datas != null && datas.size() > 0) {
			return datas.get(0).getTempValue();
		} else {
			return "";
		}
	}

	public DicDataVO addBuildingPart(Long projectId, String name) {
		DicDataVO dicDataVO = new DicDataVO();
		if (!StringUtils.isNumeric(name)) {
			BuildingPart pojo = new BuildingPart();
			pojo.setId(UnieapConstants.getSeq(null));
			pojo.setName(name);
			pojo.setProjectId(projectId);
			pojo.setActivateFlag(UnieapConstants.YES);
			pojo.setTenantId(UnieapConstants.getTenantId());
			buildingPartRepository.save(pojo);
			dicDataVO.setDicCode(pojo.getId().toString());
		} else {
			dicDataVO.setDicCode(name);
		}
		return dicDataVO;
	}

	public List<DicDataVO> getAllEmployeeDicDataList() {
		List<EmployeeInfo> datas = employeeInfoRepository.getAllEmployeeInfoByCompanyTypeList("4");
		if (datas != null && datas.size() > 0) {
			List<DicDataVO> dataList = new ArrayList<DicDataVO>();
			for (EmployeeInfo data : datas) {
				DicDataVO datavo = new DicDataVO();
				datavo.setActivateFlag(data.getActivateFlag());
				datavo.setDicCode(data.getId().toString());
				datavo.setDicName(data.getName());
				dataList.add(datavo);
			}
			return dataList;
		} else {
			return new ArrayList<DicDataVO>();
		}
	}

	public List<TaskInfoVO> getAllTaskInfoList(TaskInfoVO vo) {
		List<TaskDefine> tddatas = taskDefineRepository.getAll(vo.getTaskName());
		List<TaskInfoVO> vodatas = new ArrayList<>();
		if (tddatas != null && tddatas.size() > 0) {
			for (TaskDefine tddata : tddatas) {
				TaskInfoVO vodata = new TaskInfoVO();
				vodatas.add(vodata);
				vodata.setTaskId(tddata.getId());
				vodata.setTaskName(tddata.getName());
				vodata.setTaskType(tddata.getType());
				vodata.setNotifyCron(tddata.getNotifyCron());
				vodata.setProjectId(vo.getProjectId());
				List<TaskInfo> tdatas = taskInfoRepository.findByProjectIdAndTaskId(vo.getProjectId(), tddata.getId());
				if (tdatas != null && tdatas.size() > 0) {
					TaskInfo tdata = tdatas.get(0);
					vodata.setId(tdata.getId());
					vodata.setEmployeeId(tdata.getEmployeeId());
					vodata.setNotifyFlag(tdata.getNotifyFlag());
				} else {
					vodata.setNotifyFlag(UnieapConstants.NO);
				}
			}
			return vodatas;
		}
		return null;
	}

	public void updateTaskInfo(TaskInfoVO vo) throws Exception {
		TaskInfo pojo;
		if (vo.getId() == null) {
			pojo = new TaskInfo();
			pojo.setActivateFlag(UnieapConstants.YES);
			pojo.setTenantId(UnieapConstants.getTenantId());
			pojo.setId(UnieapConstants.getSeq(null));
			pojo.setNotifyFlag(vo.getNotifyFlag());
			pojo.setEmployeeId(vo.getEmployeeId());
			pojo.setProjectId(vo.getProjectId());
			pojo.setTaskId(vo.getTaskId());
			taskInfoRepository.save(pojo);
		} else {
			pojo = taskInfoRepository.getById(vo.getId());
			pojo.setNotifyFlag(vo.getNotifyFlag());
			pojo.setEmployeeId(vo.getEmployeeId());
			taskInfoRepository.save(pojo);
		}
	}

	public ProjectInfo getInfo(Long id) {
		ProjectInfo projectInfo = projectInfoRepository.getById(id);
		if (projectInfo.getParentId() != null) {
			ProjectInfo parent = projectInfoRepository.getById(projectInfo.getParentId());
			projectInfo.setParentId(parent.getId());
		}
		return projectInfo;
	}
	public ProjectInfo getParentInfo(Long id) {
		ProjectInfo projectInfo = projectInfoRepository.getById(id);
		if (projectInfo.getParentId() != null) {
			ProjectInfo parent = projectInfoRepository.getById(projectInfo.getParentId());
			return parent;
		}else {
			return null;
		}
	}
}
