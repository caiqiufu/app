package com.slipper.unieap.bakerstreet.playmgt.playing.bo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.bakerstreet.playmgt.pojo.ClueSwap;
import com.slipper.unieap.bakerstreet.playmgt.pojo.DmRoom;
import com.slipper.unieap.bakerstreet.playmgt.pojo.PlayerInfo;
import com.slipper.unieap.bakerstreet.playmgt.pojo.PlayingChapter;
import com.slipper.unieap.bakerstreet.playmgt.pojo.PlayingClue;
import com.slipper.unieap.bakerstreet.playmgt.pojo.PlayingPlayer;
import com.slipper.unieap.bakerstreet.playmgt.repository.ClueSwapRepository;
import com.slipper.unieap.bakerstreet.playmgt.repository.DmRoomRepository;
import com.slipper.unieap.bakerstreet.playmgt.repository.PlayerInfoRepository;
import com.slipper.unieap.bakerstreet.playmgt.repository.PlayerlikeRepository;
import com.slipper.unieap.bakerstreet.playmgt.repository.PlayingChapterRepository;
import com.slipper.unieap.bakerstreet.playmgt.repository.PlayingClueRepository;
import com.slipper.unieap.bakerstreet.playmgt.repository.PlayingPlayerRepository;
import com.slipper.unieap.bakerstreet.playmgt.vo.RoomInfoVO;
import com.slipper.unieap.bakerstreet.playmgt.vo.ScriptLikeVO;
import com.slipper.unieap.bakerstreet.scriptmgt.pojo.ChapterInfo;
import com.slipper.unieap.bakerstreet.scriptmgt.repository.ChapterInfoRepository;
import com.slipper.unieap.bakerstreet.scriptmgt.repository.ClueDetailRepository;
import com.slipper.unieap.bakerstreet.scriptmgt.repository.ClueInfoRepository;
import com.slipper.unieap.bakerstreet.scriptmgt.repository.RoleInfoRepository;
import com.slipper.unieap.bakerstreet.scriptmgt.scriptinfo.PictureInfoBO;
import com.slipper.unieap.bakerstreet.scriptmgt.vo.ChapterInfoVO;
import com.slipper.unieap.bakerstreet.scriptmgt.vo.ClueDetailVO;
import com.slipper.unieap.bakerstreet.scriptmgt.vo.RoleInfoVO;
import com.slipper.unieap.bo.BaseBO;
import com.slipper.unieap.vo.PaginationSupport;

@Service
public class PlayingBO extends BaseBO {

	@Resource
	public DmRoomRepository dmRoomRepository;

	@Resource
	public PlayingPlayerRepository playingPlayerRepository;

	@Resource
	public PlayingClueRepository playingClueRepository;

	@Resource
	public PlayingChapterRepository playingChapterRepository;

	@Resource
	private PlayerInfoRepository playerInfoRepository;

	@Resource
	public ClueSwapRepository clueSwapRepository;

	@Resource
	public PlayerlikeRepository playerlikeRepository;

	@Resource
	public RoleInfoRepository roleInfoRepository;

	@Resource
	ChapterInfoRepository chapterInfoRepository;

	@Resource
	ClueInfoRepository clueInfoRepository;

	@Resource
	ClueDetailRepository clueDetailRepository;

	@Autowired
	PictureInfoBO pictureInfoBO;

	// DM选本开房
	public DmRoom createDmRoom(DmRoom vo) {
		// 验证码为4位随机数
		String password = String.format("%04d", new Random().nextInt(9999));
		// 房间号为6位随机数
		String roomName = String.format("%06d", new Random().nextInt(999999));
		DmRoom pojo = new DmRoom();
		pojo.setId(UnieapConstants.getSeq(null));
		pojo.setPassword(password);
		pojo.setRoomName(roomName);
		pojo.setScriptId(vo.getScriptId());
		pojo.setPlayerId(vo.getPlayerId());
		pojo.setStatus("0");
		pojo.setActivateFlag(UnieapConstants.YES);
		pojo.setTenantId(UnieapConstants.getTenantId());
		dmRoomRepository.save(pojo);
		joinRoom(pojo.getPlayerId(), pojo.getId(), pojo.getPassword());
		vo.setPassword(pojo.getPassword());
		vo.setStatus(pojo.getStatus());
		vo.setId(pojo.getId());
		vo.setRoomName(pojo.getRoomName());
		vo.setStatus(pojo.getStatus());
		return vo;
	}

	// 玩家加入房间
	public Map<String, String> roomInfo(Long roomId) {
		Map<String, String> result = new HashMap<String, String>();
		DmRoom dmRoom = dmRoomRepository.getById(roomId);
		result.put("roomId", dmRoom.getId().toString());
		result.put("dmId", dmRoom.getPlayerId().toString());
		result.put("scriptId", dmRoom.getScriptId().toString());
		result.put("roomName", dmRoom.getRoomName().toString());
		result.put("status", dmRoom.getStatus().toString());
		result.put("startDate", dmRoom.getStartDate() == null ? "" : dmRoom.getStartDate().toString());
		result.put("endDate", dmRoom.getEndDate() == null ? "" : dmRoom.getEndDate().toString());
		return result;
	}

	// 玩家加入房间
	public Map<String, String> joinRoom(Long playerId, Long roomId, String verifyCode) {
		Map<String, String> result = new HashMap<String, String>();
		DmRoom dmRoom = dmRoomRepository.getById(roomId);
		if (StringUtils.equals(verifyCode, dmRoom.getPassword())) {
			PlayingPlayer pojo = new PlayingPlayer();
			pojo.setId(UnieapConstants.getSeq(null));
			pojo.setPlayerId(playerId);
			pojo.setRoomId(roomId);
			pojo.setActivateFlag(UnieapConstants.YES);
			pojo.setTenantId(UnieapConstants.getTenantId());
			playingPlayerRepository.save(pojo);
			result.put("result", "true");
			result.put("roomId", dmRoom.getId().toString());
			result.put("dmId", dmRoom.getPlayerId().toString());
			result.put("scriptId", dmRoom.getScriptId().toString());
			result.put("roomName", dmRoom.getRoomName().toString());
			result.put("status", dmRoom.getStatus().toString());
			result.put("startDate", dmRoom.getStartDate() == null ? "" : dmRoom.getStartDate().toString());
			result.put("endDate", dmRoom.getEndDate() == null ? "" : dmRoom.getEndDate().toString());
		} else {
			result.put("result", UnieapConstants.getMessage("bakerstreet.playmgt.playing.joinRommVerify", null));
		}
		return result;
	}

	// 玩家加入房间
	public Map<String, String> kickOutRoom(Long playerId, Long roomId) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("result", "true");
		playingPlayerRepository.deleteByRoomAndPlayerId(roomId, playerId);
		return result;
	}

	// 分配角色
	public Map<String, String> assignPlayerRole(Long roomId, Long playerId, Long roleId) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("result", "true");
		PlayingPlayer playingPlayer = playingPlayerRepository.findByRoomIdAndPlayerId(roomId, playerId);
		playingPlayer.setRoleId(roleId);
		playingPlayerRepository.save(playingPlayer);
		return result;
	}

	// 下发线索
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void assignPlayerClue(Long roomId, Long roleId, List<Long> clueDetailIdList) {
		List<PlayingPlayer> playingPlayerList = playingPlayerRepository.getPlayingPlayerList(roomId);
		if (playingPlayerList != null && playingPlayerList.size() > 0) {
			List<PlayingClue> playingClueList = new ArrayList<PlayingClue>();
			for (PlayingPlayer ppInfo : playingPlayerList) {
				if (ppInfo.getRoleId() != null && ppInfo.getRoleId().intValue() == roleId.intValue()) {
					for (Long clueDetailId : clueDetailIdList) {
						ClueDetailVO clueDetailVO = clueInfoRepository.getClueDetail(clueDetailId);
						PlayingClue playingClue = new PlayingClue();
						playingClueList.add(playingClue);
						playingClue.setId(UnieapConstants.getSeq(null));
						playingClue.setPlayingId(ppInfo.getId());
						playingClue.setClueId(clueDetailVO.getClueId());
						playingClue.setClueDetailId(clueDetailVO.getId());
						playingClue.setActivateFlag(UnieapConstants.YES);
						playingClue.setTenantId(UnieapConstants.getTenantId());
						playingClueRepository.deleteByPlayingIdAndClueDetailId(ppInfo.getId(), clueDetailId);
					}
				}
			}
			playingClueRepository.saveAll(playingClueList);
		}
	}

	// 收回线索
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void recoverClueDetail(Long roomId, Long roleId, List<Long> clueDetailIdList) {
		List<PlayingPlayer> playingPlayerList = playingPlayerRepository.getPlayingPlayerList(roomId);
		if (playingPlayerList != null && playingPlayerList.size() > 0) {
			for (PlayingPlayer ppInfo : playingPlayerList) {
				if (ppInfo.getRoleId() != null && ppInfo.getRoleId().intValue() == roleId.intValue()) {
					for (Long clueDetailId : clueDetailIdList) {
						playingClueRepository.deleteByPlayingIdAndClueDetailId(ppInfo.getId(), clueDetailId);
					}
				}
			}
		}
	}

	// 下发剧本
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void assignPlayerChapter(Long roomId, String chapterNumber) {
		List<PlayingPlayer> playingPlayerList = playingPlayerRepository.getPlayingPlayerList(roomId);
		if (playingPlayerList != null && playingPlayerList.size() > 0) {
			List<PlayingChapter> playingChapterList = new ArrayList<PlayingChapter>();
			for (PlayingPlayer ppInfo : playingPlayerList) {
				if (ppInfo.getRoleId() != null) {
					ChapterInfo chapterInfo = chapterInfoRepository.findByRoleIdAndChapterNumber(ppInfo.getRoleId(),
							chapterNumber);
					PlayingChapter playingChapter = new PlayingChapter();
					playingChapterList.add(playingChapter);
					playingChapter.setId(UnieapConstants.getSeq(null));
					playingChapter.setPlayingId(ppInfo.getId());
					playingChapter.setChapterId(chapterInfo.getId());
					playingChapter.setRoleId(ppInfo.getRoleId());
					playingChapter.setActivateFlag(UnieapConstants.YES);
					playingChapter.setTenantId(UnieapConstants.getTenantId());
				}
			}
			playingChapterRepository.saveAll(playingChapterList);
		}
	}

	// 回收剧本
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void recoverChapter(Long roomId, String chapterNumber) {
		List<PlayingPlayer> playingPlayerList = playingPlayerRepository.getPlayingPlayerList(roomId);
		if (playingPlayerList != null && playingPlayerList.size() > 0) {
			for (PlayingPlayer ppInfo : playingPlayerList) {
				if (ppInfo.getRoleId() != null) {
					ChapterInfo chapterInfo = chapterInfoRepository.findByRoleIdAndChapterNumber(ppInfo.getRoleId(),
							chapterNumber);
					playingChapterRepository.deleteByPlayingIdAndRoleIdAndChapterId(ppInfo.getId(), ppInfo.getRoleId(),
							chapterInfo.getId());
				}
			}
		}
	}

	// 发送线索给其他玩家
	public void playeRecoverClue(Long roomId, Long playerId, List<String> receiverList, List<Long> clueDetailIdList) {
		if (clueDetailIdList != null && clueDetailIdList.size() > 0) {
			for (String receiverID : receiverList) {
				Long receiverId = playerInfoRepository.getIdByCode(receiverID);
				for (Long clueDetailId : clueDetailIdList) {
					clueSwapRepository.delete(roomId, playerId, receiverId, clueDetailId);
				}
			}
		}
	}

	// 收回发送的线索
	public void playerSwapClue(Long roomId, Long playerId, List<String> receiverList, List<Long> clueDetailIdList) {
		if (clueDetailIdList != null && clueDetailIdList.size() > 0) {
			for (String receiverID : receiverList) {
				Long receiverId = playerInfoRepository.getIdByCode(receiverID);
				List<ClueSwap> clueSwapList = new ArrayList<ClueSwap>();
				for (Long clueDetailId : clueDetailIdList) {
					ClueDetailVO clueDetailVO = clueInfoRepository.getClueDetail(clueDetailId);
					ClueSwap clueSwap = new ClueSwap();
					clueSwapList.add(clueSwap);
					clueSwap.setId(UnieapConstants.getSeq(null));
					clueSwap.setRoomId(roomId);
					clueSwap.setGiverId(playerId);
					clueSwap.setReceiverId(receiverId);
					clueSwap.setClueId(clueDetailVO.getClueId());
					clueSwap.setClueDetailId(clueDetailVO.getId());
					clueSwap.setActivateFlag(UnieapConstants.YES);
					clueSwap.setTenantId(UnieapConstants.getTenantId());
				}
				clueSwapRepository.saveAll(clueSwapList);
			}
		}
	}

	// 开始游戏
	public void playingStart(Long roomId) {
		DmRoom dmRoom = dmRoomRepository.getById(roomId);
		dmRoom.setStatus("1");
		dmRoom.setStartDate(UnieapConstants.getDateTime());
		dmRoomRepository.save(dmRoom);
	}

	// 结束游戏
	public void playingEnd(Long roomId) {
		DmRoom dmRoom = dmRoomRepository.getById(roomId);
		dmRoom.setStatus("2");
		dmRoom.setEndDate(UnieapConstants.getDateTime());
		dmRoomRepository.save(dmRoom);
	}

	public String generateDMActivateCode(Long playerId) {
		// 验证码为4位随机数
		String password = String.format("%04d", new Random().nextInt(9999));
		PlayerInfo playerInfo = playerInfoRepository.getById(playerId);
		playerInfo.setActiveCode(password);
		playerInfoRepository.save(playerInfo);
		return password;
	}

	public void upgradeToDm(Long playerId) {
		PlayerInfo playerInfo = playerInfoRepository.getById(playerId);
		playerInfo.setPlayerType("2");
		playerInfoRepository.save(playerInfo);
	}

	public Long getScriptCount(Long playerId) {
		Map<String, Object> datas = playingPlayerRepository.getScriptCountByPlayerId(playerId);
		if (datas != null && datas.containsKey("playerId")) {
			return Long.decode(datas.get("scriptCount").toString());
		} else {
			return Long.decode("0");
		}
	}

	public Long getPlayCount(Long playerId) {
		Map<String, Object> datas = playingPlayerRepository.getPlayCountByPlayerId(playerId);
		if (datas != null && datas.containsKey("playerId")) {
			return Long.decode(datas.get("playCount").toString());
		} else {
			return Long.decode("0");
		}
	}

	public PaginationSupport getRoomGridList(PaginationSupport page) throws Exception {
		Page<RoomInfoVO> datas = dmRoomRepository.getRoomlist(this.getPageable(page));
		page.items = datas.getContent();
		page.setTotalCount((int) datas.getTotalElements());
		return page;
	}

	public PaginationSupport getScriptLikeGridList(PaginationSupport page) throws Exception {
		Page<ScriptLikeVO> datas = playerlikeRepository.getScriptLikeList(this.getPageable(page));
		page.items = datas.getContent();
		page.setTotalCount((int) datas.getTotalElements());
		return page;
	}

	public RoleInfoVO getRoleDetail(Long roomId, Long roleId) {
		RoleInfoVO rvo = roleInfoRepository.getOneById(roleId);
		List<ChapterInfoVO> chapterList = playingPlayerRepository.getChapterInfoList(roomId, roleId);
		if (chapterList != null && chapterList.size() > 0) {
			rvo.setChapterList(chapterList);
			for (ChapterInfoVO cvo : chapterList) {
				cvo.setFileList(pictureInfoBO.getPictureList(cvo.getId(), ChapterInfo.fileCategory));
			}
		}
		return rvo;
	}

	public List<String> convertKeys(String keys) {
		List<String> lkeys = null;
		if (StringUtils.isNotEmpty(keys)) {
			if (StringUtils.contains(keys, ",")) {
				String[] idsNoBlankArray = keys.split(",");
				String[] convert = (String[]) ConvertUtils.convert(idsNoBlankArray, String.class);
				// 然后转换成为 list
				lkeys = Arrays.asList(convert);
			} else {
				lkeys = new ArrayList<String>();
				lkeys.add(keys);
			}
		}
		return lkeys;
	}
}