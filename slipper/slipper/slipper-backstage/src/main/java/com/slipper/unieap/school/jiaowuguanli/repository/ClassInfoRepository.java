package com.slipper.unieap.school.jiaowuguanli.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.school.jiaowuguanli.pojo.ClassInfo;

@Repository
public interface ClassInfoRepository extends UnieapRepository<ClassInfo> {

	List<ClassInfo> findByActivateFlag(String activateFlag);

	List<ClassInfo> findByGradeId(Long gradeId);

	@Query("select count(*)+1 from ClassInfo e")
	Long getSeq();

	Long countByGradeIdAndGraduationFlag(Long gradeId, String graduationFlag);
}
