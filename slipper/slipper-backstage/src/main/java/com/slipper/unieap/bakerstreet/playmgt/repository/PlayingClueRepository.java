package com.slipper.unieap.bakerstreet.playmgt.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.slipper.unieap.bakerstreet.playmgt.pojo.PlayingClue;
import com.slipper.unieap.db.UnieapRepository;

@Repository
public interface PlayingClueRepository extends UnieapRepository<PlayingClue> {

	@Transactional
	@Modifying
	@Query(value = "delete from PlayingClue where playingId =?1 and clueDetailId=?2")
	void deleteByPlayingIdAndClueDetailId(Long playingId,Long clueDetailId);
	
}
