package com.slipper.unieap.bakerstreet.scriptmgt.scriptinfo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.bakerstreet.scriptmgt.pojo.ChapterInfo;
import com.slipper.unieap.bakerstreet.scriptmgt.pojo.ClueDetail;
import com.slipper.unieap.bakerstreet.scriptmgt.pojo.ClueInfo;
import com.slipper.unieap.bakerstreet.scriptmgt.pojo.PictureInfo;
import com.slipper.unieap.bakerstreet.scriptmgt.pojo.RoleInfo;
import com.slipper.unieap.bakerstreet.scriptmgt.pojo.ScriptDM;
import com.slipper.unieap.bakerstreet.scriptmgt.pojo.ScriptInfo;
import com.slipper.unieap.bakerstreet.scriptmgt.repository.ChapterInfoRepository;
import com.slipper.unieap.bakerstreet.scriptmgt.repository.ClueDetailRepository;
import com.slipper.unieap.bakerstreet.scriptmgt.repository.ClueInfoRepository;
import com.slipper.unieap.bakerstreet.scriptmgt.repository.PictureInfoRepository;
import com.slipper.unieap.bakerstreet.scriptmgt.repository.RoleInfoRepository;
import com.slipper.unieap.bakerstreet.scriptmgt.repository.ScriptDMRepository;
import com.slipper.unieap.bakerstreet.scriptmgt.repository.ScriptInfoRepository;
import com.slipper.unieap.bakerstreet.scriptmgt.vo.ChapterInfoVO;
import com.slipper.unieap.bakerstreet.scriptmgt.vo.ClueDetailVO;
import com.slipper.unieap.bakerstreet.scriptmgt.vo.RoleInfoVO;
import com.slipper.unieap.bakerstreet.scriptmgt.vo.ScriptDMVO;
import com.slipper.unieap.bakerstreet.scriptmgt.vo.ScriptInfoVO;
import com.slipper.unieap.bo.BaseBO;
import com.slipper.unieap.file.repository.FileArchiveRepository;
import com.slipper.unieap.mdm.dic.vo.DicDataVO;
import com.slipper.unieap.vo.PaginationSupport;

@Service
public class ScriptInfoBO extends BaseBO {

	@Autowired
	public ScriptInfoRepository scriptInfoRepository;

	@Autowired
	public ScriptDMRepository scriptDMRepository;

	@Autowired
	public RoleInfoRepository roleInfoRepository;

	@Autowired
	public ChapterInfoRepository chapterInfoRepository;

	@Autowired
	public ClueInfoRepository clueInfoRepository;

	@Autowired
	public PictureInfoRepository pictureInfoRepository;

	@Autowired
	public ClueDetailRepository clueDetailRepository;

	@Autowired
	public FileArchiveRepository fileArchiveRepository;

	@Autowired
	PictureInfoBO pictureInfoBO;

	public PaginationSupport getGridList(PaginationSupport page, ScriptInfoVO vo) throws Exception {
		Page<ScriptInfoVO> datas = scriptInfoRepository.getScriptPage(vo.getName(), vo.getTypeList(), vo.getCategory(),
				this.getPageable(page));
		page.items = datas.getContent();
		if (page.items != null) {
			for (Object voObj : page.items) {
				ScriptInfoVO scriptInfoVO = (ScriptInfoVO) voObj;
				scriptInfoVO.setClueInfoList(clueInfoRepository.getClueInfoListByScriptId(vo.getId()));
				scriptInfoVO.setClueDetailInfoList(clueInfoRepository.getClueDetailListByScriptId(vo.getId()));
			}
		}
		page.setTotalCount((int) datas.getTotalElements());
		return page;
	}

	public ScriptInfoVO getScriptInfo(ScriptInfoVO vo) {
		ScriptInfoVO svo = scriptInfoRepository.getOneById(vo.getId());
		svo.setClueInfoList(clueInfoRepository.getClueInfoListByScriptId(vo.getId()));
		svo.setClueDetailInfoList(clueInfoRepository.getClueDetailListByScriptId(vo.getId()));
		svo.setPosterList(pictureInfoBO.getPictureList(vo.getId(), ScriptInfo.posterCategory));
		svo.setGuidebookList(pictureInfoBO.getPictureList(vo.getId(), ScriptInfo.guidebookCategory));
		return svo;
	}

	public List<ClueDetailVO> getClueListByScriptId(ScriptInfoVO vo) {
		return clueInfoRepository.getClueDetailListByScriptId(vo.getId());
	}

	public PaginationSupport getRoleGridList(PaginationSupport page, ScriptInfoVO vo) throws Exception {
		Page<RoleInfoVO> datas = roleInfoRepository.getListByScriptId(vo.getId(), this.getPageable(page));
		page.items = datas.getContent();
		page.setTotalCount((int) datas.getTotalElements());
		return page;
	}

	public List<DicDataVO> getScriptDicDataList(ScriptInfoVO vo) {
		List<ScriptInfo> datas = scriptInfoRepository.findByActivateFlag(UnieapConstants.YES);
		if (datas != null && datas.size() > 0) {
			List<DicDataVO> dataList = new ArrayList<DicDataVO>();
			for (ScriptInfo data : datas) {
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

	public List<DicDataVO> getScriptDMDicDataList(ScriptInfoVO vo) {
		List<ScriptDMVO> datas = scriptDMRepository.findByScriptId(vo.getId());
		if (datas != null && datas.size() > 0) {
			List<DicDataVO> dataList = new ArrayList<DicDataVO>();
			for (ScriptDMVO data : datas) {
				DicDataVO datavo = new DicDataVO();
				datavo.setDicCode(data.getPlayerId().toString());
				datavo.setDicName(data.getPlayerName());
				dataList.add(datavo);
			}
			return dataList;
		} else {
			return new ArrayList<DicDataVO>();
		}
	}

	public List<DicDataVO> getRoleDicDataList(ScriptInfoVO vo) {
		List<RoleInfo> datas = roleInfoRepository.findByScriptId(vo.getId());
		if (datas != null && datas.size() > 0) {
			List<DicDataVO> dataList = new ArrayList<DicDataVO>();
			for (RoleInfo data : datas) {
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

	public List<DicDataVO> getChapterNumberDicDataList(ScriptInfoVO vo) {
		if (vo.getId() != null) {
			ScriptInfo info = scriptInfoRepository.findById(vo.getId()).get();
			long scriptChapter = Long.parseLong(info.getScriptChapter());
			List<DicDataVO> dataList = new ArrayList<DicDataVO>();
			for (int i = 0; i < scriptChapter; i++) {
				DicDataVO datavo = new DicDataVO();
				datavo.setActivateFlag(UnieapConstants.YES);
				datavo.setDicCode(Integer.toString(i + 1));
				datavo.setDicName(UnieapConstants.getMessage("bakerstreet.scriptmgt.chapterNumberDesc",
						new String[] { datavo.getDicCode() }));
				dataList.add(datavo);
			}
			return dataList;
		} else {
			return new ArrayList<DicDataVO>();
		}
	}

	public List<DicDataVO> getClueDicDataList(ScriptInfoVO vo) {
		List<ClueInfo> datas = clueInfoRepository.getClueInfoListByScriptId(vo.getId());
		if (datas != null && datas.size() > 0) {
			List<DicDataVO> dataList = new ArrayList<DicDataVO>();
			for (ClueInfo data : datas) {
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

	public void updatePublishFlag(ScriptInfoVO vo) {
		ScriptInfo pojo = scriptInfoRepository.findById(vo.getId()).get();
		pojo.setPublishFlag(vo.getPublishFlag());
		pojo.setPublishDate(UnieapConstants.getDateTime());
		scriptInfoRepository.save(pojo);
	}

	public void updateScriptDM(ScriptInfoVO vo, List<Long> playerIds) {
		scriptDMRepository.deleteByScriptId(vo.getId());
		if (playerIds != null && playerIds.size() > 0) {
			List<ScriptDM> pojos = new ArrayList<ScriptDM>();
			for (Long playerId : playerIds) {
				ScriptDM pojo = new ScriptDM();
				pojo.setId(UnieapConstants.getSeq(null));
				pojo.setScriptId(vo.getId());
				pojo.setPlayerId(playerId);
				pojo.setActivateFlag(UnieapConstants.YES);
				pojo.setTenantId(UnieapConstants.getTenantId());
				pojos.add(pojo);
			}
			scriptDMRepository.saveAll(pojos);
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void create(ScriptInfoVO vo) throws Exception {
		saveScriptInfo(vo);
		saveClueInfo(vo);
		pictureInfoBO.savePictureInfo(vo.getId(), ScriptInfo.posterCategory, vo.getPosterList());
		pictureInfoBO.savePictureInfo(vo.getId(), ScriptInfo.guidebookCategory, vo.getGuidebookList());
	}

	public void saveScriptInfo(ScriptInfoVO vo) {
		ScriptInfo pojo = new ScriptInfo();
		pojo.setId(vo.getId());
		pojo.setTypeList(vo.getTypeList());
		pojo.setBrief(vo.getBrief());
		if (vo.getPosterList() != null && vo.getPosterList().size() > 0) {
			pojo.setPosterUrl(vo.getPosterList().get(0).getUrl());
		}
		pojo.setCategory(vo.getCategory());
		pojo.setCode(scriptInfoRepository.getSeq().toString());
		pojo.setDuration(vo.getDuration());
		pojo.setName(vo.getName());
		pojo.setPlayerCount(vo.getPlayerCount());
		pojo.setPublishFlag("0");
		pojo.setRemark(vo.getRemark());
		pojo.setScriptChapter(vo.getScriptChapter());
		pojo.setActivateFlag(UnieapConstants.YES);
		pojo.setTenantId(UnieapConstants.getTenantId());
		scriptInfoRepository.save(pojo);

	}

	public void saveClueInfo(ScriptInfoVO vo) {
		List<ClueInfo> clueInfoList = vo.getClueInfoList();
		if (clueInfoList != null && clueInfoList.size() > 0) {
			List<ClueInfo> clueList = new ArrayList<ClueInfo>();
			for (ClueInfo clueInfo : clueInfoList) {
				if (StringUtils.isNotEmpty(clueInfo.getName())) {
					ClueInfo cpojo = new ClueInfo();
					clueList.add(cpojo);
					cpojo.setId(UnieapConstants.getSeq(null));
					cpojo.setName(clueInfo.getName());
					cpojo.setCode(clueInfo.getCode());
					cpojo.setScriptId(vo.getId());
					cpojo.setActivateFlag(UnieapConstants.YES);
					cpojo.setTenantId(UnieapConstants.getTenantId());
				}
			}
			clueInfoRepository.saveAll(clueList);
		}
	}

	public void updateClueInfo(ScriptInfoVO vo) {
		List<ClueInfo> clueInfoList = vo.getClueInfoList();
		if (clueInfoList != null && clueInfoList.size() > 0) {
			List<ClueInfo> clueListAdd = new ArrayList<ClueInfo>();
			List<ClueInfo> clueListUpdate = new ArrayList<ClueInfo>();
			List<ClueInfo> clueListDelete = new ArrayList<ClueInfo>();
			List<ClueInfo> allClueInfoList = clueInfoRepository.getClueInfoListByScriptId(vo.getId());
			for (ClueInfo clueInfo : clueInfoList) {
				if (StringUtils.isNotEmpty(clueInfo.getName())) {
					if (clueInfo.getId() == null) {
						ClueInfo cpojo = new ClueInfo();
						clueListAdd.add(cpojo);
						cpojo.setId(UnieapConstants.getSeq(null));
						cpojo.setName(clueInfo.getName());
						cpojo.setCode(clueInfo.getCode());
						cpojo.setScriptId(vo.getId());
						cpojo.setActivateFlag(UnieapConstants.YES);
						cpojo.setTenantId(UnieapConstants.getTenantId());
					} else {
						ClueInfo pojo = clueInfoRepository.getById(clueInfo.getId());
						pojo.setName(clueInfo.getName());
						clueListUpdate.add(pojo);
					}
				}
			}
			if (allClueInfoList != null && allClueInfoList.size() > 0) {
				for (ClueInfo clueInfo : allClueInfoList) {
					boolean isExist = false;
					for (ClueInfo upojo : clueListUpdate) {
						if (StringUtils.equals(clueInfo.getId().toString(), upojo.getId().toString())) {
							isExist = true;
						}
					}
					if (!isExist) {
						clueListDelete.add(clueInfo);
					}
				}
			}
			clueInfoRepository.saveAll(clueListAdd);
			clueInfoRepository.saveAll(clueListUpdate);
			clueInfoRepository.deleteAll(clueListDelete);
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(ScriptInfoVO vo) throws Exception {
		ScriptInfo pojo = scriptInfoRepository.getById(vo.getId());
		pojo.setBrief(vo.getBrief());
		pojo.setCategory(vo.getCategory());
		// pojo.setClueList(vo.getClueList());
		// pojo.setCode(vo.getCode());
		pojo.setDuration(vo.getDuration());
		pojo.setName(vo.getName());
		pojo.setPlayerCount(vo.getPlayerCount());
		pojo.setScriptChapter(vo.getScriptChapter());
		pojo.setTypeList(vo.getTypeList());
		pojo.setRemark(vo.getRemark());
		if (vo.getPosterList() != null && vo.getPosterList().size() > 0) {
			pojo.setPosterUrl(vo.getPosterList().get(0).getUrl());
		}
		scriptInfoRepository.save(pojo);
		updateClueInfo(vo);
		updatePicture(vo.getId(), ScriptInfo.posterCategory, vo.getPosterList());
		updatePicture(vo.getId(), ScriptInfo.guidebookCategory, vo.getGuidebookList());
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updatePicture(Long bizId, String bizType, List<PictureInfo> fileList) {
		pictureInfoRepository.deleteByBizIdAndBizType(bizId, bizType);
		pictureInfoBO.savePictureInfo(bizId, bizType, fileList);
	}

	public RoleInfoVO getRoleInfo(RoleInfoVO vo) {
		RoleInfoVO rvo = roleInfoRepository.getOneById(vo.getId());
		rvo.setFileList(pictureInfoBO.getPictureList(vo.getId(), RoleInfo.fileCategory));
		return rvo;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void createRole(RoleInfoVO vo) throws Exception {
		RoleInfo pojo = new RoleInfo();
		pojo.setId(vo.getId());
		pojo.setBrief(vo.getBrief());
		pojo.setName(vo.getName());
		pojo.setScriptId(vo.getScriptId());
		pojo.setSex(vo.getSex());
		pojo.setCode(roleInfoRepository.getSeq().toString());
		if (vo.getFileList() != null && vo.getFileList().size() > 0) {
			pojo.setAvatarUrl(vo.getFileList().get(0).getUrl());
		}
		pojo.setActivateFlag(UnieapConstants.YES);
		pojo.setTenantId(UnieapConstants.getTenantId());
		roleInfoRepository.save(pojo);
		pictureInfoBO.savePictureInfo(vo.getId(), RoleInfo.fileCategory, vo.getFileList());
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void editRole(RoleInfoVO vo) throws Exception {
		RoleInfo pojo = roleInfoRepository.getById(vo.getId());
		pojo.setBrief(vo.getBrief());
		pojo.setName(vo.getName());
		pojo.setScriptId(vo.getScriptId());
		pojo.setSex(vo.getSex());
		roleInfoRepository.save(pojo);
		pictureInfoBO.updatePictureInfo(vo.getFileList());
		updatePicture(vo.getId(), RoleInfo.fileCategory, vo.getFileList());
	}

	public PaginationSupport getChapterGridList(PaginationSupport page, ScriptInfo vo) throws Exception {
		Page<ChapterInfoVO> datas = chapterInfoRepository.getListByScriptId(vo.getId(), this.getPageable(page));
		page.items = datas.getContent();
		page.setTotalCount((int) datas.getTotalElements());
		return page;
	}

	public ChapterInfoVO getChapterInfo(ChapterInfoVO vo) {
		ChapterInfoVO cvo = chapterInfoRepository.getOneById(vo.getId());
		cvo.setFileList(pictureInfoBO.getPictureList(vo.getId(), ChapterInfo.fileCategory));
		return cvo;
	}

	public List<RoleInfoVO> getRoleDetailListByScriptId(Long scriptId) {
		List<RoleInfoVO> rvoList = roleInfoRepository.getRoleList(scriptId);
		if (rvoList != null && rvoList.size() > 0) {
			for (RoleInfoVO rvo : rvoList) {
				List<ChapterInfoVO> chapterList = chapterInfoRepository.getChapterInfoList(rvo.getId());
				if (chapterList != null && chapterList.size() > 0) {
					rvo.setChapterList(chapterList);
					for (ChapterInfoVO cvo : chapterList) {
						cvo.setFileList(pictureInfoBO.getPictureList(cvo.getId(), ChapterInfo.fileCategory));
					}
				}
			}
		}
		return rvoList;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void createChapter(ChapterInfoVO vo) throws Exception {
		ChapterInfo pojo = new ChapterInfo();
		pojo.setId(vo.getId());
		pojo.setCode(chapterInfoRepository.getSeq().toString());
		pojo.setBrief(vo.getBrief());
		pojo.setChapterNumber(vo.getChapterNumber());
		pojo.setName(vo.getName());
		pojo.setRoleId(vo.getRoleId());
		pojo.setScriptId(vo.getScriptId());
		pojo.setActivateFlag(UnieapConstants.YES);
		pojo.setTenantId(UnieapConstants.getTenantId());
		chapterInfoRepository.save(pojo);
		pictureInfoBO.savePictureInfo(vo.getId(), ChapterInfo.fileCategory, vo.getFileList());
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void editChapter(ChapterInfoVO vo) throws Exception {
		ChapterInfo pojo = chapterInfoRepository.getById(vo.getId());
		pojo.setBrief(vo.getBrief());
		pojo.setChapterNumber(vo.getChapterNumber());
		pojo.setName(vo.getName());
		pojo.setRoleId(vo.getRoleId());
		pojo.setScriptId(vo.getScriptId());
		chapterInfoRepository.save(pojo);
		updatePicture(vo.getId(), ChapterInfo.fileCategory, vo.getFileList());
	}

	public PaginationSupport getClueDetailGridList(PaginationSupport page, ScriptInfo vo) throws Exception {
		Page<ClueDetailVO> datas = clueInfoRepository.findByScriptId(vo.getId(), this.getPageable(page));
		page.items = datas.getContent();
		page.setTotalCount((int) datas.getTotalElements());
		return page;
	}

	public ClueDetailVO getClueDetailInfo(ClueDetailVO vo) {
		ClueDetailVO cvo = clueInfoRepository.getClueDetailInfo(vo.getId());
		cvo.setFileList(pictureInfoBO.getPictureList(vo.getId(), ClueDetail.fileCategory));
		return cvo;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void createClueDetailByBatch(ClueDetailVO vo) throws Exception {
		if (vo.getFileList() != null && vo.getFileList().size() > 0) {
			List<ClueDetail> detailList = new ArrayList<ClueDetail>();
			Long seq = clueDetailRepository.getSeq(vo.getScriptId());
			for (int i = 0; i < vo.getFileList().size(); i++) {
				PictureInfo pictureInfo = vo.getFileList().get(i);
				ClueDetail pojo = new ClueDetail();
				detailList.add(pojo);
				pojo.setId(UnieapConstants.getSeq(null));
				pojo.setClueId(vo.getClueId());
				String name = pictureInfo.getName().split("\\.")[0];
				if (StringUtils.isNumeric(name)) {
					pojo.setCode(pad(2, name));
				} else {
					if (StringUtils.contains(name, " ")) {
						name = name.split(" ")[0];
						if (StringUtils.isNumeric(name)) {
							pojo.setCode(pad(2, name));
						} else {
							pojo.setCode(pad(2, Long.toString((seq.intValue()))));
						}
					} else {
						pojo.setCode(pad(2, Long.toString((seq.intValue()))));
					}
				}

				// pojo.setCode(pictureInfo.getName().split(" ")[0]);
				// String name = pad(2,Long.parseLong(pictureInfo.getName().split("\\.")[0]));
				pojo.setName(pictureInfo.getName().split("\\.")[0]);
				pojo.setUrl(pictureInfo.getUrl());
				pojo.setActivateFlag(UnieapConstants.YES);
				pojo.setTenantId(UnieapConstants.getTenantId());
				List<PictureInfo> pp = new ArrayList<PictureInfo>();
				pp.add(pictureInfo);
				pictureInfoBO.savePictureInfo(pojo.getId(), ClueDetail.fileCategory, pp);
			}
			clueDetailRepository.saveAll(detailList);
		}
	}

	public String generateCode(String fileName) {
		if (StringUtils.contains(fileName, " ")) {
			return fileName.split(" ")[0];
		} else {
			return fileName.split("\\.")[0];
		}
	}

	public String pad(int length, String num) {
		if (StringUtils.isNumeric(num)) {
			return String.format("%0".concat(String.valueOf(length)).concat("d"), Long.decode(num).longValue());
		} else {
			return num;
		}

	}

	public void editClueDetail(ClueDetailVO vo) throws Exception {
		ClueDetail pojo = clueDetailRepository.getById(vo.getId());
		pojo.setName(vo.getName());
		clueDetailRepository.save(pojo);
		updatePicture(vo.getId(), ClueDetail.fileCategory, vo.getFileList());
	}

}
