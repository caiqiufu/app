package com.slipper.unieap.school.renyuanguanli.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.school.renyuanguanli.pojo.BindTimecard;

@Repository
public interface TimecardInfoRepository extends UnieapRepository<BindTimecard> {

	List<BindTimecard> findByActivateFlag(String activateFlag);

}
