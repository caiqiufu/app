package com.slipper.unieap.mdm.dic.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.mdm.dic.pojo.UnieapDicData;

@Repository
public interface UnieapDicDataRepository extends UnieapRepository<UnieapDicData> {

	List<UnieapDicData> findByActivateFlag(String activateFlag);

	List<UnieapDicData> findByGroupId(Long groupId);
	
	List<UnieapDicData> findByGroupIdOrderByDicName(Long groupId);
}
