package com.slipper.unieap.projectoa.projectmgt.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.projectoa.projectmgt.pojo.AttributeDefine;

@Repository
public interface AttributeDefineRepository extends UnieapRepository<AttributeDefine> {

	List<AttributeDefine> findByActivateFlag(String activateFlag);	
}
