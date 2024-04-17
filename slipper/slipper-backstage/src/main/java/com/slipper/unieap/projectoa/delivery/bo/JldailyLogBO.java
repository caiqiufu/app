package com.slipper.unieap.projectoa.delivery.bo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.bo.BaseBO;
import com.slipper.unieap.mdm.dic.vo.DicDataVO;
import com.slipper.unieap.projectoa.delivery.pojo.ApproveLog;
import com.slipper.unieap.projectoa.delivery.pojo.DeliveryBuildingpart;
import com.slipper.unieap.projectoa.delivery.pojo.JldailyLog;
import com.slipper.unieap.projectoa.delivery.pojo.TaskLog;
import com.slipper.unieap.projectoa.delivery.repository.ApproveLogRepository;
import com.slipper.unieap.projectoa.delivery.repository.DeliveryBuildingpartRepository;
import com.slipper.unieap.projectoa.delivery.repository.JldailyLogRepository;
import com.slipper.unieap.projectoa.delivery.repository.TaskLogRepository;
import com.slipper.unieap.projectoa.delivery.vo.JldailyLogVO;
import com.slipper.unieap.projectoa.projectmgt.bo.ProjectInfoBO;
import com.slipper.unieap.projectoa.projectmgt.pojo.ProjectInfo;
import com.slipper.unieap.projectoa.projectmgt.repository.ProjectInfoRepository;
import com.slipper.unieap.projectoa.projectmgt.repository.TaskInfoRepository;
import com.slipper.unieap.vo.PaginationSupport;

@Service
public class JldailyLogBO extends BaseBO {

	@Autowired
	public JldailyLogRepository jldailyLogRepository;

	@Autowired
	public DeliveryBuildingpartRepository deliveryBuildingpartRepository;

	@Autowired
	public ApproveLogRepository approveLogRepository;

	@Autowired
	ProjectInfoBO projectInfoBO;

	@Autowired
	public TaskInfoRepository taskInfoRepository;

	@Autowired
	public TaskLogRepository taskLogRepository;

	@Autowired
	public ProjectInfoRepository projectInfoRepository;

	public PaginationSupport getGridList(PaginationSupport page, JldailyLogVO vo) throws Exception {
		Page<JldailyLogVO> datas = jldailyLogRepository.getJldailyLogPage(vo.getProjectName(), vo.getDateStart(),
				vo.getDateEnd(), vo.getStatus(), vo.getId(), vo.getWxauId(), this.getPageable(page));
		page.items = datas.getContent();
		page.setTotalCount((int) datas.getTotalElements());
		return page;
	}

	public JldailyLogVO getInfo(JldailyLogVO vo) {
		if (vo.getId() == null) {
			return vo;
		} else {
			JldailyLog pojo = jldailyLogRepository.getById(vo.getId());
			vo.setAqls(pojo.getAqls());
			vo.setCreateBy(pojo.getCreateBy());
			vo.setCreateDate(pojo.getCreateDate());
			vo.setFxgcjy(pojo.getFxgcjy());
			vo.setGclh(pojo.getGclh());
			vo.setHbsxcl(pojo.getHbsxcl());
			vo.setId(pojo.getId());
			vo.setJlzlzx(pojo.getJlzlzx());
			vo.setJzjl(pojo.getJzjl());
			vo.setKtzscl(pojo.getKtzscl());
			vo.setLogTime(pojo.getLogTime());
			vo.setModifyBy(pojo.getModifyBy());
			vo.setModifyDate(pojo.getModifyDate());
			vo.setProjectId(pojo.getProjectId());
			vo.setProjectName(pojo.getProjectName());
			vo.setPzjl(pojo.getPzjl());
			vo.setQtsx(pojo.getQtsx());
			vo.setRemark(pojo.getRemark());
			vo.setStatus(pojo.getStatus());
			vo.setSgdw(pojo.getSgdw());
			// vo.setSgbw(pojo.getSgbw());
			// vo.setSglr(pojo.getSglr());
			// vo.setSgqk(pojo.getSgqk());
			vo.setTempMax(pojo.getTempMax());
			vo.setTempMin(pojo.getTempMin());
			vo.setTenantId(pojo.getTenantId());
			vo.setWeatherAfternoon(pojo.getWeatherAfternoon());
			vo.setWeatherMorning(pojo.getWeatherMorning());
			vo.setXjjl(pojo.getXjjl());
			vo.setYbgcys(pojo.getYbgcys());
			vo.setZjlDate(pojo.getZjlDate());
			vo.setZjlName(pojo.getZjlName());
			vo.setZjlyj(pojo.getZjlyj());
			vo.setDeliveryBuildingpartList(this.getDeliveryBuildingpartList(vo.getId()));
			vo.setWxauId(pojo.getCreateBy());
			ProjectInfo projectInfo = projectInfoBO.getParentInfo(vo.getProjectId());
			if (projectInfo != null) {
				vo.setParentId(projectInfo.getId());
				vo.setParentName(projectInfo.getName());
			}
			return vo;
		}
	}

	public List<DeliveryBuildingpart> getDeliveryBuildingpartList(Long logId) {
		return deliveryBuildingpartRepository.findByLogIdOrderByCreateDate(logId);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void create(JldailyLogVO vo) throws Exception {
		saveJldailyLog(vo);
		saveBuildingPart(vo);
		if (StringUtils.equals(vo.getStatus(), "Pending")) {
			updateNotifyTaskStatus(vo);
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(JldailyLogVO vo) throws Exception {
		saveJldailyLog(vo);
		saveBuildingPart(vo);
		if (StringUtils.equals(vo.getStatus(), "Pending")) {
			updateNotifyTaskStatus(vo);
		}
	}

	public void saveBuildingPart(JldailyLogVO vo) {
		List<DeliveryBuildingpart> deliveryBuildingpartList = vo.getDeliveryBuildingpartList();
		if (deliveryBuildingpartList != null && deliveryBuildingpartList.size() > 0) {
			if (vo.getId() != null) {
				deliveryBuildingpartRepository.deleteByLogId(vo.getId());
				for (DeliveryBuildingpart pojo : deliveryBuildingpartList) {
					DicDataVO dicDataVO = projectInfoBO.addBuildingPart(vo.getProjectId(), pojo.getSgbw());
					pojo.setSgbw(dicDataVO.getDicCode());
					pojo.setId(UnieapConstants.getSeq(null));
					pojo.setLogId(vo.getId());
					pojo.setActivateFlag(UnieapConstants.YES);
					pojo.setTenantId(UnieapConstants.getTenantId());
				}
				deliveryBuildingpartRepository.saveAll(deliveryBuildingpartList);
			}
		}
	}

	public void saveJldailyLog(JldailyLogVO vo) {
		JldailyLog pojo;
		if (vo.getId() == null) {
			pojo = new JldailyLog();
			pojo.setId(UnieapConstants.getSeq(null));
			pojo.setActivateFlag(UnieapConstants.YES);
			pojo.setTenantId(UnieapConstants.getTenantId());
			vo.setId(pojo.getId());
		} else {
			pojo = jldailyLogRepository.getById(vo.getId());
		}
		pojo.setAqls(vo.getAqls());
		pojo.setFxgcjy(vo.getFxgcjy());
		pojo.setGclh(vo.getGclh());
		pojo.setHbsxcl(vo.getHbsxcl());
		pojo.setJlzlzx(vo.getJlzlzx());
		pojo.setJzjl(vo.getJzjl());
		pojo.setKtzscl(vo.getKtzscl());
		pojo.setLogTime(vo.getLogTime());
		pojo.setProjectId(vo.getProjectId());
		pojo.setProjectName(vo.getProjectName());
		pojo.setPzjl(vo.getPzjl());
		pojo.setQtsx(vo.getQtsx());
		pojo.setRemark(vo.getRemark());
		pojo.setStatus(vo.getStatus());
		ProjectInfo projectInfo = projectInfoRepository.getById(pojo.getProjectId());
		pojo.setSgdw(projectInfo.getName());
		// pojo.setSgbw(vo.getSgbw());
		// pojo.setSglr(vo.getSglr());
		// pojo.setSgqk(vo.getSgqk());
		pojo.setTempMax(vo.getTempMax());
		pojo.setTempMin(vo.getTempMin());
		pojo.setWeatherAfternoon(vo.getWeatherAfternoon());
		pojo.setWeatherMorning(vo.getWeatherMorning());
		pojo.setXjjl(vo.getXjjl());
		pojo.setYbgcys(vo.getYbgcys());
		pojo.setZjlDate(vo.getZjlDate());
		pojo.setZjlName(vo.getZjlName());
		pojo.setZjlyj(vo.getZjlyj());
		pojo.setCreateBy(StringUtils.isEmpty(vo.getWxauId()) ? UnieapConstants.getAdministrator().getUsername()
				: vo.getWxauId());
		jldailyLogRepository.save(pojo);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void approve(ApproveLog vo) throws Exception {
		ApproveLog pojo = new ApproveLog();
		pojo.setId(UnieapConstants.getSeq(null));
		pojo.setProjectId(vo.getProjectId());
		pojo.setLogId(vo.getLogId());
		pojo.setApproveResult(vo.getApproveResult());
		pojo.setApproveComment(vo.getApproveComment());
		pojo.setActivateFlag(UnieapConstants.YES);
		pojo.setTenantId(UnieapConstants.getTenantId());
		approveLogRepository.save(pojo);
		JldailyLog logPojo = jldailyLogRepository.getById(vo.getLogId());
		logPojo.setStatus(vo.getApproveResult());
		jldailyLogRepository.save(logPojo);
		JldailyLogVO jldailyLogVO = new JldailyLogVO();
		jldailyLogVO.setProjectId(logPojo.getProjectId());
		jldailyLogVO.setStatus(logPojo.getStatus());
		jldailyLogVO.setLogTime(logPojo.getLogTime());
		updateNotifyTaskStatus(jldailyLogVO);

	}

	public void updateNotifyTaskStatus(JldailyLogVO vo) {
		Date logTime = vo.getLogTime();
		Calendar cal = Calendar.getInstance();
		cal.setTime(logTime);
		// 设置当前时间的时分秒为0
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		// 获取当天的起始时间
		Date startTime = cal.getTime();
		// 设置下一天的起始时间
		cal.add(Calendar.DAY_OF_MONTH, 1);
		Date endTime = cal.getTime();
		List<String> statusList = new ArrayList<String>();
		if (StringUtils.equals(vo.getStatus(), "Pending")) {
			statusList.add("1");
			List<TaskLog> employeeDailyTaskList = taskLogRepository.getEmployeeDailyTaskList(vo.getProjectId(),
					UnieapConstants.getUserId(), Long.decode("1"), statusList, startTime, endTime);
			if (employeeDailyTaskList != null && employeeDailyTaskList.size() > 0) {
				for (TaskLog taskLog : employeeDailyTaskList) {
					taskLog.setTaskStatus("2");
				}
				taskLogRepository.saveAll(employeeDailyTaskList);
			}
		}
		if (StringUtils.equals(vo.getStatus(), "Pass")) {
			statusList.add("1");
			statusList.add("2");
			List<TaskLog> employeeDailyTaskList = taskLogRepository.getEmployeeDailyTaskList(vo.getProjectId(),
					UnieapConstants.getUserId(), Long.decode("1"), statusList, startTime, endTime);
			if (employeeDailyTaskList != null && employeeDailyTaskList.size() > 0) {
				for (TaskLog taskLog : employeeDailyTaskList) {
					taskLog.setTaskStatus("3");
				}
				taskLogRepository.saveAll(employeeDailyTaskList);
			}
		}
	}
}
