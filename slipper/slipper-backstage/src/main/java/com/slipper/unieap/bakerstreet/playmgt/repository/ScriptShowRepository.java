package com.slipper.unieap.bakerstreet.playmgt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.slipper.unieap.bakerstreet.playmgt.pojo.ScriptShow;
import com.slipper.unieap.bakerstreet.playmgt.vo.ScriptShowVO;
import com.slipper.unieap.db.UnieapRepository;

@Repository
public interface ScriptShowRepository extends UnieapRepository<ScriptShow> {

	@Query(value = "select ss.id as id,ss.seq as seq,ss.scriptId as scriptId,si.name as scriptName,si.posterUrl as posterUrl,ss.brief as brief  from ScriptShow ss, ScriptInfo si where ss.scriptId =si.id and ss.activateFlag = ?1 order by ss.seq ")
	List<ScriptShowVO> findByActivateFlag(String activateFlag);

	@Modifying
	@Query(value = "update ScriptShow ss set ss.activateFlag = ?2 where ss.seq = ?1")
	List<ScriptShowVO> updateBySeq(String seq, String activateFlag);

	@Transactional
	@Modifying
	@Query(value = "delete from ScriptShow where seq =?1")
	void deleteBySeq(String seq);

}
