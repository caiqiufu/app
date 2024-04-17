package com.slipper.unieap.bakerstreet.playmgt.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.slipper.unieap.bakerstreet.playmgt.pojo.DmRoom;
import com.slipper.unieap.bakerstreet.playmgt.pojo.PlayerLike;
import com.slipper.unieap.bakerstreet.playmgt.pojo.PlayingPlayer;
import com.slipper.unieap.bakerstreet.playmgt.vo.PlayerInfoVO;
import com.slipper.unieap.bakerstreet.scriptmgt.vo.ChapterInfoVO;
import com.slipper.unieap.bakerstreet.scriptmgt.vo.ClueDetailVO;
import com.slipper.unieap.db.UnieapRepository;

@Repository
public interface PlayingPlayerRepository extends UnieapRepository<PlayingPlayer> {

	PlayingPlayer findByRoomIdAndPlayerId(Long roomId, Long playerId);

	@Query(value = "select tt.player_id as playerId,sum(tt.scriptcount) as scriptCount from ("
			+ "select tplayercount.player_id,tplayercount.room_id, trolecount.rolecount,tplayercount.playercount,"
			+ "(case when trolecount.rolecount = tplayercount.playercount then 1  else 0 end) as scriptcount "
			+ " from (select si.id as script_id, count(*) rolecount "
			+ " from bakerstreet_script_info si,bakerstreet_role_info ri where si.id = ri.script_id group by si.id) trolecount,"
			+ "(select dr.id as room_id, dr.player_id, dr.script_id, count(*) playercount "
			+ "from bakerstreet_dm_room dr ,bakerstreet_playing_player pp where dr.id = pp.room_id "
			+ "group by dr.id,dr.player_id, dr.script_id) tplayercount "
			+ "where trolecount.script_id = tplayercount.script_id)  tt "
			+ "where tt.player_id= ?1 group by tt.player_id", nativeQuery = true)
	Map<String, Object> getScriptCountByPlayerId(Long playerId);
	
	/**
	 *  玩家打本数量
	 * @param playerId
	 * @return
	 */
	@Query(value = "select pp.player_id as playerId, count(1) as playCount from bakerstreet_playing_player pp where pp.player_id =?1  group by pp.player_id", nativeQuery = true)
	Map<String, Object> getPlayCountByPlayerId(Long playerId);
	
	/**
	 *   玩家打本记录
	 * @param playerId
	 * @return
	 */
	@Query(value = "select dr from PlayingPlayer pp,DmRoom dr where dr.status = '2' and pp.playerId =dr.playerId and pp.playerId = ?1")
	List<DmRoom> getPlayeringListByPlayerId(Long playerId);
	
	/**
	 *  DM 开本记录
	 * @param playerId
	 * @return
	 */
	@Query(value = "select dr from DmRoom dr where dr.status ='2' and dr.playerId = ?1")
	List<DmRoom> getPlayeringListByDmId(Long playerId);
	
	/**
	 *     玩家剧本点赞记录
	 * @param playerId
	 * @return
	 */
	@Query(value = "select pl from PlayerLike pl where pl.playerId = ?1")
	List<PlayerLike> getPlayerLikeListByPlayerId(Long playerId);
	
	/**
	 *   剧本点赞排行
	 * @return
	 */
	@Query(value = "select * from (select si.id as scriptId, count(pl.id) as likeCount from bakerstreet_player_like pl,bakerstreet_script_info si where pl.script_Id = si.id group by si.id) tt order by tt.likeCount desc ",nativeQuery = true)
	List<Map<String,Object>> getScriptLikeList();
	
	@Transactional
	@Modifying
	@Query(value = "delete from PlayingPlayer where roomId =?1 and playerId=?2")
	void deleteByRoomAndPlayerId(Long roomId,Long playerId);
	
	@Query(value = "select t from PlayingPlayer t where t.roomId =?1 and t.playerId=?2")
	PlayingPlayer getPlayingPlayer(Long roomId,Long playerId);
	
	@Query(value = "select t from PlayingPlayer t where t.roomId =?1")
	List<PlayingPlayer> getPlayingPlayerList(Long roomId);
	
	@Query(value = "select pi.id as id, pi.code as code,pi.name as name, pi.avatar_Url as avatarUrl,pi.wxau_Id as wxauId,IFNULL(pp1.role_Id,'-1') as roleId,pp1.role_Name as roleName from (select pp.*,ri.name as role_Name from bakerstreet_playing_player pp left join bakerstreet_role_info ri on pp.role_Id = ri.id) pp1,bakerstreet_player_info pi where pp1.player_Id = pi.id and pp1.room_Id = ?1",nativeQuery = true)
	List<PlayerInfoVO> getPlayerListByRoomId(Long roomId);
	
	@Query(value = "select cd.id as id,cd.code as code,cd.name as name,ci.id as clueId,ci.name as clueName,ci.scriptId as scriptId,cd.url as url,cd.createDate as createDate from PlayingPlayer pp, PlayingClue pc, ClueDetail cd, ClueInfo ci where pp.id = pc.playingId and pc.clueId = ci.id and pc.clueDetailId = cd.id and pp.roomId = ?1 and pp.playerId = ?2")
	List<ClueDetailVO> getClueListByPlayerId(Long roomId,Long playerId);
	
	@Query(value = "select cd.id as id,cd.code as code,cd.name as name,ci.id as clueId,ci.name as clueName,ci.scriptId as scriptId,cd.url as url,cd.createDate as createDate from  ClueSwap cs, ClueDetail cd, ClueInfo ci where cs.clueId = ci.id and cs.clueDetailId = cd.id and cs.roomId = ?1 and cs.receiverId = ?2")
	List<ClueDetailVO> getSwapClueListByPlayerId(Long roomId,Long playerId);
	
	@Query(value = "select ci.id as id,ci.scriptId as scriptId,ci.roleId as roleId,ri.name as roleName,ci.chapterNumber as chapterNumber,ci.code as code,ci.name as name, ci.brief as brief, ci.createDate as createDate from  PlayingChapter pc, ChapterInfo ci,RoleInfo ri,PlayingPlayer pp where pc.roleId =ri.id and pc.chapterId = ci.id and pc.playingId = pp.id and pp.roomId = ?1 and pc.roleId = ?2 ")
	List<ChapterInfoVO> getChapterInfoList(Long roomId,Long roleId);
	
	@Query(value = "select dr from PlayingPlayer pp,DmRoom dr where pp.roomId = dr.id and pp.playerId =?1 and dr.status = '1'")
	List<DmRoom> getPlayerPlayingRoomInfo(Long playerId);
}
