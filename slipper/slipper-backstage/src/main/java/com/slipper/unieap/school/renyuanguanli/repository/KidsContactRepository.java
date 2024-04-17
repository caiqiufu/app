package com.slipper.unieap.school.renyuanguanli.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.school.renyuanguanli.pojo.KidsContact;

@Repository
public interface KidsContactRepository extends UnieapRepository<KidsContact> {

	List<KidsContact> findByActivateFlag(String activateFlag);

	List<KidsContact> findByKidsId(Long kidsId);

}
