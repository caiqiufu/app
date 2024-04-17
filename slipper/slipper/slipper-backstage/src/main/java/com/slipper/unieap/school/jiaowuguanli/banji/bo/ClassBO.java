package com.slipper.unieap.school.jiaowuguanli.banji.bo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.bo.BaseBO;
import com.slipper.unieap.db.Criteria;
import com.slipper.unieap.db.Restrictions;
import com.slipper.unieap.mdm.dic.vo.DicDataVO;
import com.slipper.unieap.school.jiaowuguanli.pojo.ClassInfo;
import com.slipper.unieap.school.jiaowuguanli.repository.ClassInfoRepository;
import com.slipper.unieap.school.jiaowuguanli.repository.GradeInfoRepository;
import com.slipper.unieap.school.jiaowuguanli.vo.ClassInfoVO;
import com.slipper.unieap.school.jiaowuguanli.vo.ClassStaffVO;
import com.slipper.unieap.school.renyuanguanli.pojo.StaffRelation;
import com.slipper.unieap.school.renyuanguanli.repository.KidsClassRepository;
import com.slipper.unieap.school.renyuanguanli.repository.StaffInfoRepository;
import com.slipper.unieap.school.renyuanguanli.repository.StaffRelationRepository;
import com.slipper.unieap.vo.PaginationSupport;

@Service
public class ClassBO extends BaseBO {

	@Autowired
	public ClassInfoRepository classInfoRepository;
	@Autowired
	public GradeInfoRepository gradeInfoRepository;
	@Autowired
	public StaffRelationRepository staffRelationRepository;
	@Autowired
	public KidsClassRepository kidsClassRepository;

	@Autowired
	public StaffInfoRepository saffInfoRepository;

	public List<DicDataVO> getClassDicDataList(Long gradeId) {
		List<ClassInfo> datas;
		if (gradeId == null) {
			datas = classInfoRepository.findByActivateFlag(UnieapConstants.YES);

		} else {
			datas = classInfoRepository.findByGradeId(gradeId);
		}
		if (datas != null && datas.size() > 0) {
			List<DicDataVO> dataList = new ArrayList<DicDataVO>();
			for (ClassInfo data : datas) {
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

	public PaginationSupport getGridList(PaginationSupport page, ClassInfoVO vo) throws Exception {

		Criteria<ClassInfo> criteria = new Criteria<>();
		criteria.add(Restrictions.eq("id", vo.getId(), true));
		criteria.add(Restrictions.eq("graduationFlag", UnieapConstants.NO, true));
		classInfoRepository.getPaginationDataByMysql(page, criteria);

		if (page.items != null && page.items.size() > 0) {
			List<ClassInfoVO> nItems = new ArrayList<ClassInfoVO>();
			for (Object obj : page.items) {
				ClassInfo classInfo = (ClassInfo) obj;
				ClassInfoVO classInfoVO = new ClassInfoVO();
				nItems.add(classInfoVO);
				classInfoVO.setId(classInfo.getId());
				classInfoVO.setClassName(classInfo.getName());
				classInfoVO.setGradeId(classInfo.getGradeId());
				classInfoVO.setGradeName(gradeInfoRepository.getById(classInfoVO.getGradeId()).getName());
				getMainTeacher(classInfo.getId(), classInfoVO);
				getNurse(classInfo.getId(), classInfoVO);
				getClassStaffs(classInfo.getId(), "AT", classInfoVO);
				getClassStaffs(classInfo.getId(), "OT", classInfoVO);
				classInfoVO.setGraduationFlag(classInfo.getGraduationFlag());
				getKidsCount(classInfo.getId(), classInfoVO);
				classInfoVO.setActivateFlag(classInfo.getActivateFlag());
				classInfoVO.setCreateDate(classInfo.getCreateDate());
				classInfoVO.setCreateBy(classInfo.getCreateBy());
				classInfoVO.setModifyDate(classInfo.getModifyDate());
				classInfoVO.setModifyBy(classInfo.getModifyBy());
				classInfoVO.setModifyBy(classInfo.getModifyBy());
				classInfoVO.setTenantId(classInfo.getTenantId());
				classInfoVO.setRemark(classInfo.getRemark());
			}
			page.items = nItems;
		}
		return page;
	}

	public void getMainTeacher(Long classId, ClassInfoVO vo) {
		List<ClassStaffVO> datas = this.getClassStaff(classId, "MT");
		if (datas != null && datas.size() > 0) {
			vo.setMainTeacherId(datas.get(0).getStaffId());
			vo.setMainTeacherName(datas.get(0).getStaffName());
		}
	}

	public void getNurse(Long classId, ClassInfoVO vo) {
		List<ClassStaffVO> datas = this.getClassStaff(classId, "NU");
		if (datas != null && datas.size() > 0) {
			vo.setNurseId(datas.get(0).getStaffId());
			vo.setNurseName(datas.get(0).getStaffName());
		}
	}

	public void getClassStaffs(Long classId, String staffType, ClassInfoVO vo) {
		List<ClassStaffVO> datas = this.getClassStaff(classId, staffType);
		if (datas != null && datas.size() > 0) {
			String ids = "";
			String names = "";
			for (ClassStaffVO cvo : datas) {
				ids = ids + cvo.getStaffId().toString() + ",";
				names = names + cvo.getStaffName() + ",";
			}
			if (ids.contains(",")) {
				ids = ids.substring(0, ids.length() - 1);
				names = names.substring(0, names.length() - 1);
			}
			switch (staffType) {
			case "AT": {
				vo.setAssistTeachers(ids);
				vo.setAssistTeachersName(names);
				break;
			}
			case "OT": {
				vo.setOthertTeachers(ids);
				vo.setOthertTeachersName(names);
			}
			}
		}
	}

	public void getKidsCount(Long classId, ClassInfoVO vo) {

		List<Map<String, Object>> datas = kidsClassRepository.findGroupByClass(classId);
		if (datas != null && datas.size() > 0) {
			long mc = 0, fc = 0;
			for (Map<String, Object> data : datas) {
				String gender = data.get("gender").toString();
				Long count = (Long) data.get("kidscount");
				if (StringUtils.equals(gender, "M")) {
					mc = count.longValue();
				} else {
					fc = count.longValue();
				}
			}
			String kidsCount = UnieapConstants.getMessage("unieap.school.jiaowuguanli.banji.kidscount",
					new String[] { Long.toString(mc + fc), Long.toString(mc), Long.toString(fc) });
			vo.setKidsCount(kidsCount);
		}
	}

	public List<ClassStaffVO> getClassStaff(Long classId, String staffType) {
		return saffInfoRepository.findStaffByClassIdAndStaffType(classId, staffType);
	}

	public ClassInfoVO getInfo(Long id) throws Exception {
		PaginationSupport page = new PaginationSupport();
		ClassInfoVO vo = new ClassInfoVO();
		vo.setId(id);
		this.getGridList(page, vo);
		vo = (ClassInfoVO) page.items.get(0);
		return vo;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public ClassInfoVO create(ClassInfoVO vo) throws Exception {
		ClassInfo classInfo = new ClassInfo();
		classInfo.setId(classInfoRepository.getSeq());
		classInfo.setName(vo.getClassName());
		classInfo.setGradeId(vo.getGradeId());
		classInfo.setGraduationFlag(UnieapConstants.NO);
		classInfo.setActivateFlag(UnieapConstants.YES);
		classInfo.setTenantId(UnieapConstants.getTenantId());
		classInfoRepository.save(classInfo);
		saveStaffInfo(classInfo.getId(), vo);
		return vo;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public ClassInfoVO update(ClassInfoVO vo) throws Exception {
		ClassInfo classInfo = classInfoRepository.getById(vo.getId());
		classInfo.setName(vo.getClassName());
		classInfo.setGradeId(vo.getGradeId());
		classInfo.setGraduationFlag(UnieapConstants.NO);
		classInfoRepository.save(classInfo);
		staffRelationRepository.deleteByClassId(classInfo.getId());
		saveStaffInfo(classInfo.getId(), vo);
		return vo;
	}

	public void saveStaffInfo(Long classId, ClassInfoVO vo) {
		if (vo.getMainTeacherId() != null) {
			StaffRelation mainTeacher = new StaffRelation();
			mainTeacher.setId(UnieapConstants.getSeq(null));
			mainTeacher.setClassId(classId);
			mainTeacher.setStaffId(vo.getMainTeacherId());
			mainTeacher.setStaffType("MT");
			mainTeacher.setActivateFlag(UnieapConstants.YES);
			mainTeacher.setTenantId(UnieapConstants.getTenantId());
			staffRelationRepository.save(mainTeacher);
		}
		if (vo.getNurseId() != null) {
			StaffRelation nurse = new StaffRelation();
			nurse.setId(UnieapConstants.getSeq(null));
			nurse.setClassId(classId);
			nurse.setStaffId(vo.getNurseId());
			nurse.setStaffType("NU");
			nurse.setActivateFlag(UnieapConstants.YES);
			nurse.setTenantId(UnieapConstants.getTenantId());
			staffRelationRepository.save(nurse);
		}

		if (StringUtils.isNotEmpty(vo.getAssistTeachers())) {
			String[] assistTeacherIds = vo.getAssistTeachers().split(",");
			List<StaffRelation> s1 = new ArrayList<StaffRelation>();
			for (String id : assistTeacherIds) {
				StaffRelation assistTeacher = new StaffRelation();
				s1.add(assistTeacher);
				assistTeacher.setId(UnieapConstants.getSeq(null));
				assistTeacher.setClassId(classId);
				assistTeacher.setStaffId(Long.valueOf(id));
				assistTeacher.setStaffType("AT");
				assistTeacher.setActivateFlag(UnieapConstants.YES);
				assistTeacher.setTenantId(UnieapConstants.getTenantId());
			}
			staffRelationRepository.saveAll(s1);
		}

		if (StringUtils.isNotEmpty(vo.getOthertTeachers())) {
			String[] othertTeacherIds = vo.getOthertTeachers().split(",");
			List<StaffRelation> s2 = new ArrayList<StaffRelation>();
			for (String id : othertTeacherIds) {
				StaffRelation othertTeacher = new StaffRelation();
				s2.add(othertTeacher);
				othertTeacher.setId(UnieapConstants.getSeq(null));
				othertTeacher.setClassId(classId);
				othertTeacher.setStaffId(Long.valueOf(id));
				othertTeacher.setStaffType("OT");
				othertTeacher.setActivateFlag(UnieapConstants.YES);
				othertTeacher.setTenantId(UnieapConstants.getTenantId());
			}
			staffRelationRepository.saveAll(s2);
		}
	}

	public void delete(Long id) throws Exception {
		classInfoRepository.deleteById(id);
		staffRelationRepository.deleteByClassId(id);
	}

	public void deleteBatch(List<Long> ids) throws Exception {
		classInfoRepository.deleteAllByIdInBatch(ids);
	}

	public void gradeUp(ClassInfoVO vo) {
		ClassInfo classInfo = classInfoRepository.getById(vo.getId());
		classInfo.setGradeId(vo.getGradeId());
		classInfo.setName(vo.getClassName());
		classInfoRepository.save(classInfo);
	}

	public boolean graduation(ClassInfoVO vo) {
		ClassInfo classInfo = classInfoRepository.getById(vo.getId());
		classInfo.setGraduationFlag(UnieapConstants.YES);
		classInfoRepository.save(classInfo);
		return true;
	}
}
