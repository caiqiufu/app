package com.slipper.unieap.school.renyuanguanli.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.school.renyuanguanli.pojo.DistrictInfo;

@Repository
public interface DistrictInfoRepository extends UnieapRepository<DistrictInfo> {

	List<DistrictInfo> findByParentId(Long parentId);
}
