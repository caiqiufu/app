package com.slipper.unieap.school.jiaowuguanli.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.school.jiaowuguanli.pojo.GradeInfo;

@Repository
public interface GradeInfoRepository extends UnieapRepository<GradeInfo> {

	List<GradeInfo> findByActivateFlag(String activateFlag);

	@Query("select count(e)+1 from GradeInfo e")
	Long getSeq();

	default boolean checkGradeIdExist(Long gradeId) {
		GradeInfo gradeInfo = this.getById(gradeId);
		if (gradeInfo == null) {
			return false;
		} else {
			return true;
		}
	}
}
