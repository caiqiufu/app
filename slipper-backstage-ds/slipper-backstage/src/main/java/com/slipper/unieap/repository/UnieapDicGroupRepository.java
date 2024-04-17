package com.slipper.unieap.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.pojo.UnieapDicGroup;

@Repository
public interface UnieapDicGroupRepository extends UnieapRepository<UnieapDicGroup> {

	List<UnieapDicGroup> findByActivateFlag(String activateFlag);

	UnieapDicGroup findByGroupCode(String groupCode);
}
