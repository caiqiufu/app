package com.slipper.unieap.bakerstreet.playmgt.repository;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.slipper.unieap.bakerstreet.playmgt.pojo.PlayerLike;
import com.slipper.unieap.bakerstreet.playmgt.vo.ScriptLikeVO;
import com.slipper.unieap.db.UnieapRepository;

@Repository
public interface PlayerlikeRepository extends UnieapRepository<PlayerLike> {
	
	@Query(value = "select  IF(count(1)>0, 'Y', 'N') as flag from bakerstreet_player_like where player_Id =?1 and script_Id=?2", nativeQuery = true)
	Map<String,String> isScriptLiked(Long playerId,Long scriptId);

	@Query(value = "select si.id as scriptId, si.name as scriptName,count(1) as likeCount from PlayerLike pl, ScriptInfo si where pl.scriptId =si.id group by pl.scriptId order by likeCount ", countQuery = "select count(*) from PlayerLike pl, ScriptInfo si where pl.scriptId =si.id group by pl.scriptId ")
	Page<ScriptLikeVO> getScriptLikeList(Pageable pageable);

	@Transactional
	@Modifying
	@Query(value = "delete from PlayerLike where playerId =?1 and scriptId=?2")
	void deleteByPlayerIdAndScriptId(Long playerId,Long scriptId);
	
}
