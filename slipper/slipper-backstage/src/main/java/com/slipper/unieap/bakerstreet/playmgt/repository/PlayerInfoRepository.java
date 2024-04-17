package com.slipper.unieap.bakerstreet.playmgt.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.slipper.unieap.bakerstreet.playmgt.pojo.PlayerInfo;
import com.slipper.unieap.bakerstreet.playmgt.vo.PlayerInfoVO;
import com.slipper.unieap.bakerstreet.scriptmgt.pojo.ScriptInfo;
import com.slipper.unieap.db.UnieapRepository;

@Repository
public interface PlayerInfoRepository extends UnieapRepository<PlayerInfo> {

	@Query(value = "select lpad(COALESCE((max(convert(e.code,signed))+1),1),4,0) as seq from bakerstreet_player_info e ", nativeQuery = true)
	String getSeq();

	@Query(value = "select e.id from PlayerInfo e where e.code = ?1")
	Long getIdByCode(String code);

	@Query(value = "select e from PlayerInfo e where (e.code = ?1 or ?1 is null) and (e.wxauId = ?2 or ?2 is null)")
	PlayerInfo getPlayerInfo(String ID, String wxauId);

	@Query(value = "select t from PlayerInfo t where ( t.name like CONCAT('%',?1,'%') or ?1 is null) and t.playerType =2")
	List<PlayerInfo> getDMListByName(String name);

	@Query(value = "select pinfo.id as id,pinfo.code as code,pinfo.name as name,pinfo.avatarUrl as avatarUrl,pinfo.weixin as weixin,pinfo.registerDate as registerDate,pinfo.playerType as playerType,pinfo.scriptFlag as scriptFlag, pinfo.changeFlag as changeFlag, pinfo.status as status, pinfo.activeCode as activeCode, pinfo.blacklistFlag as blacklistFlag FROM PlayerInfo pinfo LEFT JOIN PictureInfo pi ON pinfo.id = pi.bizId and pi.bizType='player_picture' where (pinfo.name like CONCAT('%',?1,'%') or ?1 is null) and (pinfo.code like CONCAT('%',?2,'%') or ?2 is null)", countQuery = "select count(*) FROM PlayerInfo pinfo LEFT JOIN PictureInfo pi ON pinfo.id = pi.bizId and pi.bizType='player_picture' where (pinfo.name like CONCAT('%',?1,'%') or ?1 is null) and (pinfo.code like CONCAT('%',?2,'%') or ?2 is null)")
	Page<PlayerInfoVO> findByNameAndId(String name, String code, Pageable pageable);

	@Query(value = "select t from PlayerInfo t where t.playerType = ?1")
	List<PlayerInfo> findByPlayerType(String playerType);

	@Query(value = "select t from PlayerInfo t where t.blacklistFlag = 'Y' and t.playerType =?1", countQuery = "select count(*) from PlayerInfo t where t.blacklistFlag = 'Y' and t.playerType =?1")
	Page<PlayerInfo> getBlacklist(String playerType, Pageable pageable);

	PlayerInfo findByCode(String code);

	PlayerInfo findByWxauId(String wxauId);

	@Query(value = "select si from ScriptDM sdm,ScriptInfo si where si.id =sdm.scriptId and sdm.playerId = ?1")
	List<ScriptInfo> getPlayerScriptList(Long playerId);

}
