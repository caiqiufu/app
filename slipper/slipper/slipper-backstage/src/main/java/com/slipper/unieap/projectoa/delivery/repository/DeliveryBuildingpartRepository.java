package com.slipper.unieap.projectoa.delivery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.projectoa.delivery.pojo.DeliveryBuildingpart;

@Repository
public interface DeliveryBuildingpartRepository extends UnieapRepository<DeliveryBuildingpart> {

	@Transactional
	@Modifying
	void deleteByLogId(Long logId);

	List<DeliveryBuildingpart> findByLogIdOrderByCreateDate(Long logId);

}
