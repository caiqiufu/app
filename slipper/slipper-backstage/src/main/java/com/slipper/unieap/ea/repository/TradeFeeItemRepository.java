package com.slipper.unieap.ea.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.ea.pojo.TradeFeeItem;

@Repository
public interface TradeFeeItemRepository extends UnieapRepository<TradeFeeItem> {

	/**
	 * 获取费用项
	 * 
	 * @param feeType
	 * @return
	 */
	List<TradeFeeItem> findByFeeType(String feeType);

}
