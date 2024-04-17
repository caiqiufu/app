package com.slipper.unieap.bakerstreet.inf;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.bakerstreet.inf.vo.PlayerInfoVO;
import com.slipper.unieap.bakerstreet.inf.vo.PlayingInfoVO;
import com.slipper.unieap.bakerstreet.inf.vo.ScriptDetailInfoVO;
import com.slipper.unieap.bakerstreet.inf.vo.ScriptInfoVO;
import com.slipper.unieap.bakerstreet.inf.vo.UScriptInfoVO;
import com.slipper.unieap.bakerstreet.playmgt.player.PlayerInfoBO;
import com.slipper.unieap.bakerstreet.playmgt.playing.bo.PlayingBO;
import com.slipper.unieap.bakerstreet.playmgt.pojo.DmRoom;
import com.slipper.unieap.bakerstreet.playmgt.pojo.PlayerInfo;
import com.slipper.unieap.bakerstreet.playmgt.pojo.PlayerLike;
import com.slipper.unieap.bakerstreet.playmgt.pojo.PlayerLogin;
import com.slipper.unieap.bakerstreet.playmgt.pojo.PlayingPlayer;
import com.slipper.unieap.bakerstreet.playmgt.repository.DmRoomRepository;
import com.slipper.unieap.bakerstreet.playmgt.repository.DmShowRepository;
import com.slipper.unieap.bakerstreet.playmgt.repository.PlayerInfoRepository;
import com.slipper.unieap.bakerstreet.playmgt.repository.PlayerLoginRepository;
import com.slipper.unieap.bakerstreet.playmgt.repository.PlayerlikeRepository;
import com.slipper.unieap.bakerstreet.playmgt.repository.PlayingPlayerRepository;
import com.slipper.unieap.bakerstreet.playmgt.repository.ScriptShowRepository;
import com.slipper.unieap.bakerstreet.playmgt.vo.DmShowVO;
import com.slipper.unieap.bakerstreet.playmgt.vo.ScriptShowVO;
import com.slipper.unieap.bakerstreet.scriptmgt.pojo.ChapterInfo;
import com.slipper.unieap.bakerstreet.scriptmgt.pojo.ClueDetail;
import com.slipper.unieap.bakerstreet.scriptmgt.pojo.RoleInfo;
import com.slipper.unieap.bakerstreet.scriptmgt.pojo.ScriptInfo;
import com.slipper.unieap.bakerstreet.scriptmgt.repository.ClueInfoRepository;
import com.slipper.unieap.bakerstreet.scriptmgt.repository.PictureInfoRepository;
import com.slipper.unieap.bakerstreet.scriptmgt.repository.RoleInfoRepository;
import com.slipper.unieap.bakerstreet.scriptmgt.repository.ScriptDMRepository;
import com.slipper.unieap.bakerstreet.scriptmgt.repository.ScriptInfoRepository;
import com.slipper.unieap.bakerstreet.scriptmgt.scriptinfo.ScriptInfoBO;
import com.slipper.unieap.bakerstreet.scriptmgt.vo.ChapterInfoVO;
import com.slipper.unieap.bakerstreet.scriptmgt.vo.ClueDetailVO;
import com.slipper.unieap.bakerstreet.scriptmgt.vo.RoleInfoVO;
import com.slipper.unieap.bo.BaseBO;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

@Service
public class InfBO extends BaseBO {

	@Resource
	public ScriptShowRepository scriptShowRepository;

	@Resource
	public ScriptInfoRepository scriptInfoRepository;

	@Resource
	public ScriptDMRepository scriptDMRepository;

	@Resource
	public RoleInfoRepository roleInfoRepository;

	@Resource
	public ClueInfoRepository clueInfoRepository;

	@Resource
	public PictureInfoRepository pictureInfoRepository;

	@Resource
	private DmShowRepository dmShowRepository;

	@Resource
	private PlayerInfoRepository playerInfoRepository;

	@Resource
	public PlayerlikeRepository playerlikeRepository;

	@Resource
	private PlayingPlayerRepository playingPlayerRepository;

	@Resource
	private DmRoomRepository dmRoomRepository;

	@Resource
	private PlayerLoginRepository playerLoginRepository;

	@Autowired
	public ScriptInfoBO scriptInfoBO;

	@Autowired
	public PlayerInfoBO playerInfoBO;

	@Autowired
	public PlayingBO playingBO;

	public Long getIdByCode(String ID) {
		return playerInfoRepository.getIdByCode(ID);
	}

	public List<ScriptShowVO> getNewScriptList() {
		List<ScriptShowVO> datas = scriptShowRepository.findByActivateFlag(UnieapConstants.YES);
		return datas;
	}

	public List<ScriptInfoVO> getScriptList(String ID, ScriptDetailInfoVO vo) {
		Long playerId = this.getIdByCode(ID);
		List<com.slipper.unieap.bakerstreet.scriptmgt.vo.ScriptInfoVO> datas = scriptInfoRepository
				.getScriptList(vo.getScriptName(), vo.getTypeList(), vo.getCategory(), vo.getPlayerCount());
		if (datas != null && datas.size() > 0) {
			List<ScriptInfoVO> ndatas = new ArrayList<ScriptInfoVO>();
			for (com.slipper.unieap.bakerstreet.scriptmgt.vo.ScriptInfoVO dvo : datas) {
				ScriptInfoVO nvo = new ScriptInfoVO();
				ndatas.add(nvo);
				nvo.setScriptId(dvo.getId());
				nvo.setPosterUrl(dvo.getPosterUrl());
				nvo.setScriptName(dvo.getName());
				nvo.setTypeList(dvo.getTypeList());
				nvo.setPlayerCount(dvo.getPlayerCount());
				nvo.setDuration(dvo.getDuration());
				nvo.setScriptChapter(dvo.getScriptChapter());
				if (playerId != null) {
					nvo.setPlayFlag(scriptDMRepository.getPlayFlag(playerId));
				} else {
					nvo.setPlayFlag(UnieapConstants.NO);
				}
			}
			return ndatas;
		}
		return null;
	}

	public ScriptDetailInfoVO getScriptInfo(Long scriptId) {
		com.slipper.unieap.bakerstreet.scriptmgt.vo.ScriptInfoVO data = scriptInfoRepository.getOneById(scriptId);
		if (data == null) {
			return null;
		} else {
			ScriptDetailInfoVO dvo = new ScriptDetailInfoVO();
			dvo.setScriptId(data.getId());
			dvo.setCategory(data.getCategory());
			dvo.setDuration(data.getDuration());
			dvo.setPlayerCount(data.getPlayerCount());
			dvo.setPosterUrl(data.getPosterUrl());
			dvo.setScriptName(data.getName());
			dvo.setTypeList(data.getTypeList());
			dvo.setScriptChapter(data.getScriptChapter());
			dvo.setRoleList(scriptInfoBO.getRoleDetailListByScriptId(scriptId));
			dvo.setClueList(scriptInfoBO.getClueListByScriptId(data));
			dvo.setGuidebookList(pictureInfoRepository.getPictureList(scriptId, ScriptInfo.guidebookCategory, null));
			return dvo;
		}
	}

	public List<PlayerInfoVO> getPlayerList(String playerType) {
		List<PlayerInfoVO> pvoList = new ArrayList<PlayerInfoVO>();
		if (StringUtils.equals(playerType, "1") || StringUtils.equals(playerType, "2")) {
			List<PlayerInfo> pdata = playerInfoRepository.findByPlayerType(playerType);
			for (PlayerInfo p : pdata) {
				PlayerInfoVO pvo = new PlayerInfoVO();
				pvoList.add(pvo);
				pvo.setAvatarUrl(p.getAvatarUrl());
				pvo.setID(p.getCode());
				pvo.setName(p.getName());
				pvo.setRegisterDate(p.getRegisterDate());
				pvo.setWeixin(p.getWeixin());
			}

		} else if (StringUtils.equals(playerType, "3")) {
			List<DmShowVO> showData = dmShowRepository.findByActivateFlag(UnieapConstants.YES);
			if (showData != null && showData.size() > 0) {
				for (DmShowVO dvo : showData) {
					PlayerInfoVO pvo = new PlayerInfoVO();
					pvoList.add(pvo);
					pvo.setAvatarUrl(dvo.getAvatarUrl());
					pvo.setID(dvo.getPlayerCode());
					pvo.setName(dvo.getPlayerName());
				}
			}
		}
		return pvoList;
	}

	public List<PlayingInfoVO> getPlayingPlayerList(Long roomId) {
		List<com.slipper.unieap.bakerstreet.playmgt.vo.PlayerInfoVO> pdata = playingPlayerRepository
				.getPlayerListByRoomId(roomId);
		List<PlayingInfoVO> pvoList = new ArrayList<PlayingInfoVO>();
		if (pdata != null && pdata.size() > 0) {
			for (com.slipper.unieap.bakerstreet.playmgt.vo.PlayerInfoVO p : pdata) {
				PlayingInfoVO pvo = getPlayingInfo(roomId, p.getCode());
				pvoList.add(pvo);
			}
		}
		return pvoList;
	}

	public com.slipper.unieap.bakerstreet.inf.vo.PlayerInfoVO loginOrRegister(
			com.slipper.unieap.bakerstreet.inf.vo.PlayerInfoVO vo) {
		com.slipper.unieap.bakerstreet.playmgt.vo.PlayerInfoVO pvo = new com.slipper.unieap.bakerstreet.playmgt.vo.PlayerInfoVO();
		pvo.setCode(vo.getID());
		pvo.setAvatarUrl(vo.getAvatarUrl());
		pvo.setName(vo.getName());
		pvo.setWeixin(vo.getWeixin());
		pvo.setWxauId(vo.getWxauId());
		pvo.setSessionKey(vo.getSessionKey());
		if (StringUtils.isNotEmpty(pvo.getWxauId())) {
			PlayerInfo playerInfo = playerInfoRepository.findByWxauId(pvo.getWxauId());
			if (playerInfo == null) {
				playerInfoBO.register(pvo);
				playerInfo = playerInfoRepository.findByWxauId(pvo.getWxauId());
			}
			this.login(vo.getID(), vo.getWxauId(), vo.getSessionKey());
			return this.getPlayerInfo(playerInfo.getCode());
		} else {
			return null;
		}
	}

	@Value("${wxapi.appid}")
	String wxapi_appid;
	@Value("${wxapi.secret}")
	String wxapi_secret;
	@Value("${wxapi.url}")
	String wxapi_url;

	public com.slipper.unieap.bakerstreet.inf.vo.PlayerInfoVO loginWithWX(String code) {
		String url = MessageFormat.format(wxapi_url, code);
		//logger.debug(url);
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject(url, String.class);
		JSONObject exRsp = JSONUtil.parseObj(result);
		logger.debug(exRsp.toString());
		String openId = exRsp.getStr("openid");
		String sessionKey = exRsp.getStr("session_key");
		com.slipper.unieap.bakerstreet.inf.vo.PlayerInfoVO vo = new com.slipper.unieap.bakerstreet.inf.vo.PlayerInfoVO();
		if (StringUtils.isNotEmpty(openId)) {
			vo.setName(openId);
			vo.setWxauId(openId);
			vo.setSessionKey(sessionKey);
			return this.loginOrRegister(vo);
		} else {
			vo.setRemark(exRsp.toString());
			return vo;
		}
	}

	public PlayerInfoVO getPlayerInfo(String ID) {
		Long playerId = this.getIdByCode(ID);
		if (playerId != null) {
			PlayerInfo playerInfo = playerInfoRepository.getById(playerId);
			if (playerInfo != null) {
				PlayerInfoVO vo = new PlayerInfoVO();
				vo.setActiveCode(playerInfo.getActiveCode());
				vo.setActiveDate(playerInfo.getCreateDate());
				vo.setAvatarUrl(playerInfo.getAvatarUrl());
				vo.setID(playerInfo.getCode());
				vo.setName(playerInfo.getName());
				vo.setRegisterDate(playerInfo.getCreateDate());
				vo.setScriptFlag(playerInfo.getScriptFlag());
				vo.setChangeFlag(playerInfo.getChangeFlag());
				vo.setStatus(playerInfo.getStatus());
				vo.setWeixin(playerInfo.getWeixin());
				vo.setWxauId(playerInfo.getWxauId());
				vo.setPlayerType(playerInfo.getPlayerType());
				vo.setPlayerScriptList(playingPlayerRepository.getPlayeringListByPlayerId(playerId));
				vo.setPlayCount(vo.getPlayerScriptList() == null ? Long.decode("0") : vo.getPlayerScriptList().size());
				vo.setDmScriptList(playerInfoRepository.getPlayerScriptList(playerId));
				vo.setDmRoomList(playingPlayerRepository.getPlayeringListByDmId(playerId));
				vo.setDmScriptCount(vo.getDmScriptList() == null ? Long.decode("0") : vo.getDmScriptList().size());
				return vo;
			}
		}
		return null;
	}

	public PlayingInfoVO getPlayingInfo(Long roomId, String ID) {
		Long playerId = this.getIdByCode(ID);
		PlayingPlayer playingPlayer = playingPlayerRepository.getPlayingPlayer(roomId, playerId);
		PlayerInfo playerInfo = playerInfoRepository.getById(playerId);
		if (playerInfo != null) {
			DmRoom room = dmRoomRepository.getById(roomId);
			ScriptDetailInfoVO scriptDetailInfoVO = getScriptInfo(room.getScriptId());
			PlayingInfoVO vo = new PlayingInfoVO();
			vo.setAvatarUrl(playerInfo.getAvatarUrl());
			vo.setID(playerInfo.getCode());
			vo.setName(playerInfo.getName());
			vo.setAllClueList(scriptDetailInfoVO.getClueList());
			vo.setAssignedClueList(getPlayingClueList(roomId, playerId));
			if (playingPlayer.getRoleId() != null) {
				RoleInfo roleInfo = roleInfoRepository.getById(playingPlayer.getRoleId());
				if (roleInfo != null) {
					vo.setRoleId(roleInfo.getId().toString());
					vo.setRoleName(roleInfo.getName());
					vo.setRoleAvatarUrl(roleInfo.getAvatarUrl());
					List<RoleInfoVO> roleList = scriptDetailInfoVO.getRoleList();
					for (RoleInfoVO roleInfoVO : roleList) {
						if (StringUtils.equals(vo.getRoleId(), roleInfoVO.getId().toString())) {
							vo.setAllChapterList(roleInfoVO.getChapterList());
							vo.setAssignedChapterList(
									playingPlayerRepository.getChapterInfoList(roomId, playingPlayer.getRoleId()));
							if (vo.getAssignedChapterList() != null && vo.getAssignedChapterList().size() > 0) {
								for (ChapterInfoVO chapterInfoVO : vo.getAssignedChapterList()) {
									chapterInfoVO.setFileList(pictureInfoRepository
											.getPictureList(chapterInfoVO.getId(), ChapterInfo.fileCategory, null));
								}
							}
						}
					}
				}
			}
			return vo;
		}
		return null;
	}

	public List<ClueDetailVO> getPlayingClueList(Long roomId, Long playerId) {
		List<ClueDetailVO> clueList = playingPlayerRepository.getClueListByPlayerId(roomId, playerId);
		List<ClueDetailVO> swapClueList = playingPlayerRepository.getSwapClueListByPlayerId(roomId, playerId);
		if (clueList != null && swapClueList != null) {
			clueList.addAll(swapClueList);
		}
		if (clueList != null && clueList.size() > 0) {
			for (ClueDetailVO clueDetailVO : clueList) {
				clueDetailVO.setFileList(
						pictureInfoRepository.getPictureList(clueDetailVO.getId(), ClueDetail.fileCategory, null));
			}
		}
		return clueList;
	}

	public DmRoom createDmRoom(String ID, Long scriptId) {
		Long playerId = this.getIdByCode(ID);
		DmRoom vo = new DmRoom();
		vo.setPlayerId(playerId);
		vo.setScriptId(scriptId);
		return playingBO.createDmRoom(vo);
	}

	public Map<String, String> roomInfo(Long roomId) {
		Map<String, String> data = playingBO.roomInfo(roomId);
		List<PlayingInfoVO> players = getPlayingPlayerList(roomId);
		if (players != null) {
			data.put("playerCount", "" + players.size());
		} else {
			data.put("playerCount", "0");
		}
		return data;
	}

	public Map<String, String> joinRoom(String ID, Long roomId, String verifyCode) {
		Long playerId = this.getIdByCode(ID);
		PlayingPlayer playingPlayer = playingPlayerRepository.findByRoomIdAndPlayerId(roomId, playerId);
		if (playingPlayer == null) {
			return playingBO.joinRoom(playerId, roomId, verifyCode);
		} else {
			return this.roomInfo(roomId);
		}
	}

	public Map<String, String> kickOutRoom(String ID, Long roomId) {
		Long playerId = this.getIdByCode(ID);
		return playingBO.kickOutRoom(playerId, roomId);
	}

	public Map<String, String> assignPlayerRole(Long roomId, String ID, Long roleId) {
		Long playerId = this.getIdByCode(ID);
		return playingBO.assignPlayerRole(roomId, playerId, roleId);
	}

	public RoleInfoVO getPlayerRoleDetail(Long roomId, String ID) {
		Long playerId = this.getIdByCode(ID);
		PlayingPlayer pp = playingPlayerRepository.getPlayingPlayer(roomId, playerId);
		if (pp == null) {
			throw new RuntimeException("玩家未加入房间，检查参数");
		} else {
			return playingBO.getRoleDetail(roomId, pp.getRoleId());
		}
	}

	public void playerSwapClue(Long roomId, String ID, List<Long> clueDetailIdList, String receiverList) {
		Long playerId = this.getIdByCode(ID);
		playingBO.playerSwapClue(roomId, playerId, playingBO.convertKeys(receiverList), clueDetailIdList);
	}

	public void playeRecoverClue(Long roomId, String ID, List<Long> clueIdList, String receiverList) {
		Long playerId = this.getIdByCode(ID);
		playingBO.playeRecoverClue(roomId, playerId, playingBO.convertKeys(receiverList), clueIdList);
	}

	public void scriptLike(String ID, Long scriptId) {
		Long playerId = this.getIdByCode(ID);
		Map<String, String> flagObj = playerlikeRepository.isScriptLiked(playerId, scriptId);
		if (StringUtils.equals(flagObj.get("flag"), "N")) {
			PlayerLike playerLike = new PlayerLike();
			playerLike.setId(UnieapConstants.getSeq(null));
			playerLike.setPlayerId(playerId);
			playerLike.setScriptId(scriptId);
			playerLike.setActivateFlag(UnieapConstants.YES);
			playerLike.setTenantId(UnieapConstants.getTenantId());
			playerlikeRepository.save(playerLike);
		}
	}

	public void scriptUnLike(String ID, Long scriptId) {
		Long playerId = this.getIdByCode(ID);
		playerlikeRepository.deleteByPlayerIdAndScriptId(playerId, scriptId);
	}

	public UScriptInfoVO getScriptInfoById(Long scriptId) {
		com.slipper.unieap.bakerstreet.scriptmgt.vo.ScriptInfoVO vo = scriptInfoRepository.getOneById(scriptId);
		UScriptInfoVO uvo = new UScriptInfoVO();
		uvo.setCategory(vo.getCategory());
		uvo.setDuration(vo.getDuration());
		uvo.setPosterUrl(vo.getPosterUrl());
		uvo.setScriptChapter(vo.getScriptChapter());
		uvo.setScriptId(vo.getId());
		uvo.setScriptName(vo.getName());
		uvo.setTypeList(vo.getTypeList());
		return uvo;
	}

	public List<UScriptInfoVO> getMyPlayedScriptList(String ID) {
		Long playerId = this.getIdByCode(ID);
		List<DmRoom> datas = playingPlayerRepository.getPlayeringListByPlayerId(playerId);
		if (datas != null && datas.size() > 0) {
			List<UScriptInfoVO> vos = new ArrayList<UScriptInfoVO>();
			for (DmRoom data : datas) {
				Long scriptId = data.getScriptId();
				UScriptInfoVO vo = this.getScriptInfoById(scriptId);
				vos.add(vo);
				vo.setPlayerScriptInfo(data);
			}
			return vos;
		} else {
			return null;
		}
	}

	public List<UScriptInfoVO> getMyRoomList(String ID) {
		Long playerId = this.getIdByCode(ID);
		List<DmRoom> datas = playingPlayerRepository.getPlayeringListByDmId(playerId);
		if (datas != null && datas.size() > 0) {
			List<UScriptInfoVO> vos = new ArrayList<UScriptInfoVO>();
			for (DmRoom data : datas) {
				Long scriptId = data.getScriptId();
				UScriptInfoVO vo = this.getScriptInfoById(scriptId);
				vos.add(vo);
				vo.setDmScriptInfo(data);
			}
			return vos;
		} else {
			return null;
		}
	}

	public List<UScriptInfoVO> getMyLikeScriptList(String ID) {
		Long playerId = this.getIdByCode(ID);
		List<PlayerLike> datas = playingPlayerRepository.getPlayerLikeListByPlayerId(playerId);
		if (datas != null && datas.size() > 0) {
			List<UScriptInfoVO> vos = new ArrayList<UScriptInfoVO>();
			for (PlayerLike data : datas) {
				Long scriptId = data.getScriptId();
				UScriptInfoVO vo = this.getScriptInfoById(scriptId);
				vos.add(vo);
				vo.setPlayerLikeInfo(data);
			}
			return vos;
		} else {
			return null;
		}
	}

	public List<UScriptInfoVO> getScriptLikeList() {
		List<Map<String, Object>> datas = playingPlayerRepository.getScriptLikeList();
		if (datas != null && datas.size() > 0) {
			List<UScriptInfoVO> vos = new ArrayList<UScriptInfoVO>();
			for (Map<String, Object> data : datas) {
				Long scriptId = Long.valueOf(data.get("scriptId").toString());
				UScriptInfoVO vo = this.getScriptInfoById(scriptId);
				vos.add(vo);
				vo.setScriptLikeInfo(data);
			}
			return vos;
		} else {
			return null;
		}
	}

	public Map<String, String> generateDMActivateCode(String ID) {
		Long playerId = this.getIdByCode(ID);
		String activateCode = playingBO.generateDMActivateCode(playerId);
		Map<String, String> result = new HashMap<String, String>();
		result.put("activateCode", activateCode);
		return result;
	}

	public Map<String, Object> upgradeToDm(String ID, String activateCode) {
		Map<String, Object> result = new HashMap<String, Object>();
		Long playerId = this.getIdByCode(ID);
		PlayerInfo playerInfo = playerInfoRepository.getById(playerId);
		if (StringUtils.equals(activateCode, playerInfo.getActiveCode())) {
			playingBO.upgradeToDm(playerId);
			result.put("result", true);

		} else {
			result.put("result", UnieapConstants.getMessage("bakerstreet.playmgt.activeCodeError", null));
		}
		return result;
	}

	public Map<String, Object> login(String ID, String wxauId, String sessionKey) {
		PlayerLogin pojo = new PlayerLogin();
		pojo.setId(UnieapConstants.getSeq(null));
		pojo.setPlayerId(ID);
		pojo.setWxauId(wxauId);
		pojo.setSessionKey(sessionKey);
		pojo.setActivateFlag(UnieapConstants.YES);
		pojo.setTenantId(UnieapConstants.getTenantId());
		playerLoginRepository.save(pojo);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", true);
		return result;
	}

	public Map<String, Object> updatePlayerInfo(com.slipper.unieap.bakerstreet.inf.vo.PlayerInfoVO vo) {
		Long playerId = this.getIdByCode(vo.getID());
		PlayerInfo pojo = playerInfoRepository.getById(playerId);
		pojo.setAvatarUrl(vo.getAvatarUrl());
		pojo.setName(vo.getName());
		playerInfoRepository.save(pojo);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", true);
		return result;
	}

	public Map<String, Object> getPlayerPlayingRoomInfo(String ID) {
		Long playerId = this.getIdByCode(ID);
		List<DmRoom> dmRoom = playingPlayerRepository.getPlayerPlayingRoomInfo(playerId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("roomInfoList", dmRoom);
		return result;
	}
}
