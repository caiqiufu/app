package com.slipper.unieap.bakerstreet.playmgt.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.slipper.unieap.bakerstreet.playmgt.pojo.DmPlayLog;
import com.slipper.unieap.bakerstreet.playmgt.vo.DmPlayVO;
import com.slipper.unieap.db.UnieapRepository;

@Repository
public interface DmPlayLogRepository extends UnieapRepository<DmPlayLog> {

	@Query(value = "select dr.id as id,pp.playerId as playerId,pi.name as playerName,dr.scriptId as scriptId,si.name as scriptName,dr.startDate as startDate,dr.endDate as endDate from DmRoom dr, PlayingPlayer pp, ScriptInfo si,PlayerInfo pi where dr.id =pp.roomId and dr.scriptId = si.id and pp.playerId = pi.id and pi.code = ?1 ", countQuery = "select count(*) from DmRoom dr, PlayingPlayer pp, ScriptInfo si,PlayerInfo pi where dr.id =pp.roomId and dr.scriptId = si.id and pp.playerId = pi.id and pi.code = ?1 ")
	Page<DmPlayVO> findDMPlayLogById(String code, Pageable pageable);

}
