package com.slipper.unieap.school.renyuanguanli.repository;

import org.springframework.stereotype.Repository;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.school.renyuanguanli.pojo.AddressInfo;

@Repository
public interface AddressInfoRepository extends UnieapRepository<AddressInfo> {

	AddressInfo findByRefId(Long refId);
}
