package com.slipper.unieap.bakerstreet.scriptmgt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.slipper.unieap.bakerstreet.scriptmgt.pojo.ScriptDM;
import com.slipper.unieap.bakerstreet.scriptmgt.vo.ScriptDMVO;
import com.slipper.unieap.db.UnieapRepository;

@Repository
public interface ScriptDMRepository extends UnieapRepository<ScriptDM> {

	@Query(value = "select sdm.id as id,sdm.scriptId as scriptId,si.name as scriptName,sdm.playerId as playerId ,pi.name as playerName from ScriptDM sdm,PlayerInfo pi,ScriptInfo si where sdm.scriptId = si.id and sdm.playerId =pi.id and sdm.scriptId = ?1")
	List<ScriptDMVO> findByScriptId(Long scriptId);

	@Transactional
	@Modifying
	void deleteByScriptId(Long scriptId);
	
	@Query(value = "select (case pp.pf when 0 then 'N' else 'Y' end) as playflag from (SELECT count(1) as pf FROM bakerstreet_script_dm where player_id = 111) pp", nativeQuery = true)
	String getPlayFlag(Long playerId);
}
