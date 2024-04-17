package com.slipper.unieap.projectoa.delivery.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.projectoa.delivery.pojo.FieldDefaultvalue;

@Repository
public interface FieldDefaultvalueRepository extends UnieapRepository<FieldDefaultvalue> {
	
	List<FieldDefaultvalue> findByProjectIdAndFieldCode(Long projectId,String fieldCode);

}
