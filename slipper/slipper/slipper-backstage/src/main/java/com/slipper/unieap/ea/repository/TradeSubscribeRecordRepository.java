package com.slipper.unieap.ea.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.ea.pojo.TradeSubscribeRecord;

@Repository
public interface TradeSubscribeRecordRepository extends UnieapRepository<TradeSubscribeRecord> {

	/**
	 * 查询账户订阅记录
	 * 
	 * @param accountCode
	 * @return
	 */
	@Query(value = "SELECT e FROM TradeSubscribeRecord e WHERE e.accountCode = ?1 AND e.activateFlag =?")
	List<TradeSubscribeRecord> getSubscribeRecordList(String accountCode,String activateFlag);

}
