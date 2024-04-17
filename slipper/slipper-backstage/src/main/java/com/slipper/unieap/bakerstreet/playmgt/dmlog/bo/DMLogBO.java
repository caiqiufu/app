package com.slipper.unieap.bakerstreet.playmgt.dmlog.bo;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.bakerstreet.playmgt.pojo.DmShow;
import com.slipper.unieap.bakerstreet.playmgt.pojo.PlayerInfo;
import com.slipper.unieap.bakerstreet.playmgt.pojo.ScriptShow;
import com.slipper.unieap.bakerstreet.playmgt.repository.DmBrowseRepository;
import com.slipper.unieap.bakerstreet.playmgt.repository.DmPlayLogRepository;
import com.slipper.unieap.bakerstreet.playmgt.repository.DmShowRepository;
import com.slipper.unieap.bakerstreet.playmgt.repository.PlayerInfoRepository;
import com.slipper.unieap.bakerstreet.playmgt.repository.ScriptShowRepository;
import com.slipper.unieap.bakerstreet.playmgt.vo.DmPlayVO;
import com.slipper.unieap.bakerstreet.playmgt.vo.DmShowVO;
import com.slipper.unieap.bakerstreet.playmgt.vo.ScriptShowVO;
import com.slipper.unieap.bo.BaseBO;
import com.slipper.unieap.mdm.dic.vo.DicDataVO;
import com.slipper.unieap.vo.PaginationSupport;

@Service
public class DMLogBO extends BaseBO {

	@Resource
	public DmPlayLogRepository dmPlayLogRepository;

	@Resource
	private DmBrowseRepository dmBrowseRepository;

	@Resource
	private PlayerInfoRepository playerInfoRepository;

	@Resource
	private ScriptShowRepository scriptShowRepository;

	@Resource
	private DmShowRepository dmShowRepository;

	public PaginationSupport getDMPlayGridList(PaginationSupport page, PlayerInfo vo) throws Exception {
		Page<DmPlayVO> datas = dmPlayLogRepository.findDMPlayLogById(vo.getCode(), this.getPageable(page));
		page.items = datas.getContent();
		page.setTotalCount((int) datas.getTotalElements());
		return page;
	}

	public PaginationSupport getDMBrowseGridList(PaginationSupport page, PlayerInfo vo) throws Exception {
		Page<DmPlayVO> datas = dmBrowseRepository.findDMBrowseById(vo.getCode(), this.getPageable(page));
		page.items = datas.getContent();
		page.setTotalCount((int) datas.getTotalElements());
		return page;
	}

	public PlayerInfo getInfo(Long id) {
		return playerInfoRepository.findById(id).get();
	}

	public List<DicDataVO> getDMDicDataList(PlayerInfo vo) {
		List<PlayerInfo> datas = playerInfoRepository.getDMListByName(vo.getName());
		if (datas != null && datas.size() > 0) {
			List<DicDataVO> dataList = new ArrayList<DicDataVO>();
			for (PlayerInfo data : datas) {
				DicDataVO datavo = new DicDataVO();
				datavo.setActivateFlag(data.getActivateFlag());
				datavo.setDicCode(data.getCode());
				datavo.setDicName(data.getName());
				dataList.add(datavo);
			}
			return dataList;
		} else {
			return new ArrayList<DicDataVO>();
		}
	}

	public List<ScriptShowVO> getScriptShowList() {
		List<ScriptShowVO> showList = new ArrayList<ScriptShowVO>();
		List<ScriptShowVO> showData = scriptShowRepository.findByActivateFlag(UnieapConstants.YES);
		for (int i = 0; i < 6; i++) {
			if (i < showData.size()) {
				showList.add(showData.get(i));
			} else {
				ScriptShowVO scriptShowVO = new ScriptShowVO();
				scriptShowVO.setId(UnieapConstants.getSeq(null));
				scriptShowVO.setSeq(i + "");
				scriptShowVO.setBrief("");
				scriptShowVO.setPosterUrl("");
				scriptShowVO.setScriptId(Long.valueOf(0));
				scriptShowVO.setScriptName("");
				showList.add(scriptShowVO);
			}
		}
		return showList;
	}

	public void updateScriptShow(ScriptShowVO vo) {
		scriptShowRepository.deleteBySeq(vo.getSeq());
		ScriptShow pojo = new ScriptShow();
		pojo.setBrief(vo.getBrief());
		pojo.setId(UnieapConstants.getSeq(null));
		pojo.setScriptId(vo.getScriptId());
		pojo.setSeq(vo.getSeq());
		pojo.setActivateFlag(UnieapConstants.YES);
		pojo.setTenantId(UnieapConstants.getTenantId());
		scriptShowRepository.save(pojo);
	}

	public List<DmShowVO> getDmShowList() {
		List<DmShowVO> showList = new ArrayList<DmShowVO>();
		List<DmShowVO> showData = dmShowRepository.findByActivateFlag(UnieapConstants.YES);
		for (int i = 0; i < 3; i++) {
			if (i < showData.size()) {
				showList.add(showData.get(i));
			} else {
				DmShowVO dmShowVO = new DmShowVO();
				dmShowVO.setId(UnieapConstants.getSeq(null));
				dmShowVO.setSeq(i + "");
				dmShowVO.setBrief("");
				dmShowVO.setAvatarUrl("");
				dmShowVO.setPlayerId(Long.valueOf(0));
				dmShowVO.setPlayerName("");
				showList.add(dmShowVO);
			}
		}
		return showList;
	}

	public void updateDmShow(DmShowVO vo) {
		dmShowRepository.deleteBySeq(vo.getSeq());
		DmShow pojo = new DmShow();
		pojo.setBrief(vo.getBrief());
		pojo.setId(UnieapConstants.getSeq(null));
		pojo.setPlayerId(vo.getPlayerId());
		pojo.setSeq(vo.getSeq());
		pojo.setActivateFlag(UnieapConstants.YES);
		pojo.setTenantId(UnieapConstants.getTenantId());
		dmShowRepository.save(pojo);
	}
}
