package com.slipper.unieap.bakerstreet.playmgt.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.slipper.unieap.bakerstreet.playmgt.pojo.ClueSwap;
import com.slipper.unieap.db.UnieapRepository;

@Repository
public interface ClueSwapRepository extends UnieapRepository<ClueSwap> {
     
	@Transactional
	@Modifying
	@Query(value = "delete from ClueSwap where roomId =?1 and giverId=?2 and receiverId=?3 and clueDetailId=?4")
	void delete(Long roomId,Long giverId,Long receiverId,Long clueDetailId);
}
