package com.slipper.unieap.demo.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.demo.pojo.UnieapDemo;

@Repository
public interface UnieapDemoRepository extends UnieapRepository<UnieapDemo> {

	List<UnieapDemo> findByActivateFlag(String activateFlag);

	List<UnieapDemo> findByGroupId(Long groupId);
}
