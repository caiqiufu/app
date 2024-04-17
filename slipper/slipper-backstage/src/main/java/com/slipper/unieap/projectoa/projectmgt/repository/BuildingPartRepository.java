package com.slipper.unieap.projectoa.projectmgt.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.projectoa.projectmgt.pojo.BuildingPart;

@Repository
public interface BuildingPartRepository extends UnieapRepository<BuildingPart> {

	List<BuildingPart> findByProjectIdOrderByNameAsc(Long projectId);
	
}
