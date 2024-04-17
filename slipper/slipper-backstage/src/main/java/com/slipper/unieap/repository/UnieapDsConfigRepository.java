package com.slipper.unieap.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.pojo.UnieapDsConfig;

@Repository
public interface UnieapDsConfigRepository extends UnieapRepository<UnieapDsConfig> {

	List<UnieapDsConfig> findByActivateFlag(String activateFlag);

	List<UnieapDsConfig> findByDsCode(String dsCode);
}
