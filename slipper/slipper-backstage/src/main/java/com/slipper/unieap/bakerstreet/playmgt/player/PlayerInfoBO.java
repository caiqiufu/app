package com.slipper.unieap.bakerstreet.playmgt.player;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.bakerstreet.playmgt.playing.bo.PlayingBO;
import com.slipper.unieap.bakerstreet.playmgt.pojo.PlayerInfo;
import com.slipper.unieap.bakerstreet.playmgt.repository.PlayerInfoRepository;
import com.slipper.unieap.bakerstreet.playmgt.vo.PlayerInfoVO;
import com.slipper.unieap.bo.BaseBO;
import com.slipper.unieap.mdm.dic.vo.DicDataVO;
import com.slipper.unieap.vo.PaginationSupport;

@Service
public class PlayerInfoBO extends BaseBO {

	@Autowired
	public PlayerInfoRepository playerInfoRepository;

	@Autowired
	PlayingBO playingBO;

	public PaginationSupport getGridList(PaginationSupport page, PlayerInfoVO vo) throws Exception {
		Page<PlayerInfoVO> datas = playerInfoRepository.findByNameAndId(vo.getName(), vo.getCode(),
				this.getPageable(page));
		page.items = datas.getContent();
		if (page.items != null) {
			for (Object voObj : page.items) {
				PlayerInfoVO evo = (PlayerInfoVO) voObj;
				evo.setDmScriptCount(playingBO.getPlayCount(evo.getId()));
				evo.setPlayCount(playingBO.getScriptCount(evo.getId()));
			}
		}
		page.setTotalCount((int) datas.getTotalElements());
		return page;
	}

	public List<DicDataVO> getPlayerDicDataList(PlayerInfo vo) {
		List<PlayerInfo> datas = playerInfoRepository.findByPlayerType(vo.getPlayerType());
		if (datas != null && datas.size() > 0) {
			List<DicDataVO> dataList = new ArrayList<DicDataVO>();
			for (PlayerInfo data : datas) {
				DicDataVO datavo = new DicDataVO();
				datavo.setActivateFlag(data.getActivateFlag());
				datavo.setDicCode(data.getId().toString());
				datavo.setDicName(data.getName());
				dataList.add(datavo);
			}
			return dataList;
		} else {
			return new ArrayList<DicDataVO>();
		}
	}

	public void updateBlacklistFlag(PlayerInfoVO vo) {
		PlayerInfo pojo = playerInfoRepository.getById(vo.getId());
		pojo.setBlacklistFlag(vo.getBlacklistFlag());
		playerInfoRepository.save(pojo);
	}

	public void updatePlayerType(PlayerInfoVO vo) {
		PlayerInfo pojo = playerInfoRepository.getById(vo.getId());
		pojo.setPlayerType(vo.getPlayerType());
		playerInfoRepository.save(pojo);
	}

	public void updateScriptFlag(PlayerInfoVO vo) {
		PlayerInfo pojo = playerInfoRepository.getById(vo.getId());
		pojo.setScriptFlag(vo.getScriptFlag());
		playerInfoRepository.save(pojo);
	}

	public void updateChangeFlag(PlayerInfoVO vo) {
		PlayerInfo pojo = playerInfoRepository.getById(vo.getId());
		pojo.setChangeFlag(vo.getChangeFlag());
		playerInfoRepository.save(pojo);
	}

	public PaginationSupport getBlacklistGridList(PaginationSupport page, PlayerInfoVO vo) throws Exception {
		Page<PlayerInfo> datas = playerInfoRepository.getBlacklist(vo.getPlayerType(), this.getPageable(page));
		page.items = datas.getContent();
		page.setTotalCount((int) datas.getTotalElements());
		return page;
	}

	public PlayerInfoVO register(PlayerInfoVO vo) {
		PlayerInfo pojo = new PlayerInfo();
		pojo.setId(UnieapConstants.getSeq(null));
		pojo.setActiveCode("");
		pojo.setAvatarUrl(vo.getAvatarUrl());
		pojo.setChangeFlag(UnieapConstants.YES);
		pojo.setCode(playerInfoRepository.getSeq());
		pojo.setName(vo.getName());
		pojo.setPlayerType("1");
		pojo.setRegisterDate(UnieapConstants.getDateTime());
		pojo.setScriptFlag(UnieapConstants.YES);
		pojo.setStatus("2");
		pojo.setWeixin(vo.getWeixin());
		pojo.setWxauId(vo.getWxauId());
		pojo.setActivateFlag(UnieapConstants.YES);
		pojo.setTenantId(UnieapConstants.getTenantId());
		playerInfoRepository.save(pojo);
		vo.setCode(pojo.getCode());
		vo.setPlayerType(pojo.getPlayerType());
		return vo;
	}
}
