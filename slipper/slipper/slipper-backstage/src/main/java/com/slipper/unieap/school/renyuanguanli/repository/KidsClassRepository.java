package com.slipper.unieap.school.renyuanguanli.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.school.jiaowuguanli.vo.ClassInfoVO;
import com.slipper.unieap.school.renyuanguanli.pojo.KidsClass;

@Repository
public interface KidsClassRepository extends UnieapRepository<KidsClass> {

	List<KidsClass> findByActivateFlag(String activateFlag);

	@Query("select ki.gender as gender,count(ki.id) as kidscount from KidsClass kc,KidsInfo ki where kc.kidsId = ki.id  and kc.classId =?1 group by ki.gender")
	List<Map<String, Object>> findGroupByClass(Long classId);
	
	@Query(value="select ci.id as id,ci.name as className,gi.id as gradeId,gi.name as gradeName from KidsClass kc,ClassInfo ci,GradeInfo gi where kc.classId = ci.id and ci.gradeId = gi.id and kc.kidsId = ?1", nativeQuery = false)
	ClassInfoVO findClassInfoByKidsId(Long kidsId);
}
