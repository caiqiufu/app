package com.slipper.unieap.bakerstreet.playmgt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.slipper.unieap.bakerstreet.playmgt.pojo.DmShow;
import com.slipper.unieap.bakerstreet.playmgt.vo.DmShowVO;
import com.slipper.unieap.db.UnieapRepository;

@Repository
public interface DmShowRepository extends UnieapRepository<DmShow> {

	@Query(value = "select ds.id as id,ds.seq as seq,ds.playerId as playerId,pi.code as playerCode,pi.name as playerName,pi.avatarUrl as avatarUrl,ds.brief as brief  from DmShow ds, PlayerInfo pi where ds.playerId =pi.id and ds.activateFlag = ?1 order by ds.seq ")
	List<DmShowVO> findByActivateFlag(String activateFlag);

	@Modifying
	@Query(value = "update DmShow ds set ds.activateFlag = ?2 where ds.seq = ?1")
	List<DmShowVO> updateBySeq(String seq, String activateFlag);

	@Transactional
	@Modifying
	@Query(value = "delete from DmShow where seq =?1")
	void deleteBySeq(String seq);

}
