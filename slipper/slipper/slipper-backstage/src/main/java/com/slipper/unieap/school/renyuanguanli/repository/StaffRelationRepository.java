package com.slipper.unieap.school.renyuanguanli.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.school.renyuanguanli.pojo.StaffRelation;

@Repository
public interface StaffRelationRepository extends UnieapRepository<StaffRelation> {

	List<StaffRelation> findByActivateFlag(String activateFlag);

	@Modifying
	void deleteByClassId(Long classId);

}
