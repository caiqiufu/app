package com.slipper.unieap.school.renyuanguanli.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.school.jiaowuguanli.vo.ClassStaffVO;
import com.slipper.unieap.school.renyuanguanli.pojo.StaffInfo;

@Repository
public interface StaffInfoRepository extends UnieapRepository<StaffInfo> {

	List<StaffInfo> findByActivateFlag(String activateFlag);

	@Query("select ci.id as classId, ci.name as className,si.id as staffId,si.name as staffName,sr.staffType from StaffRelation sr,StaffInfo si,ClassInfo ci where sr.staffId = si.id and sr.classId = ci.id and sr.classId = ?1 and sr.staffType = ?2")
	List<ClassStaffVO> findStaffByClassIdAndStaffType(Long classId, String staffType);

}
