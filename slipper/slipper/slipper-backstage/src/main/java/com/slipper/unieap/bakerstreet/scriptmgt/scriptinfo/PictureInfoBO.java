package com.slipper.unieap.bakerstreet.scriptmgt.scriptinfo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.bakerstreet.scriptmgt.pojo.PictureInfo;
import com.slipper.unieap.bakerstreet.scriptmgt.repository.PictureInfoRepository;
import com.slipper.unieap.bo.BaseBO;
import com.slipper.unieap.file.pojo.UnieapFileArchive;

@Service
public class PictureInfoBO extends BaseBO {

	@Autowired
	public PictureInfoRepository pictureInfoRepository;

	public void savePictureInfo(Long bizId, String bizType, List<PictureInfo> pictureList) {
		if (pictureList != null && pictureList.size() > 0) {
			for (int i = 0; i < pictureList.size(); i++) {
				PictureInfo pojo = pictureList.get(i);
				pojo.setId(UnieapConstants.getSeq(null));
				pojo.setBizId(bizId);
				pojo.setBizType(bizType);
				pojo.setSeq(pojo.getName().split("\\.")[0]);
				pojo.setActivateFlag(UnieapConstants.YES);
				pojo.setTenantId(UnieapConstants.getTenantId());
			}
			pictureInfoRepository.saveAll(pictureList);
		}
	}

	public void updatePictureInfo(List<PictureInfo> pictureList) {
		if (pictureList != null && pictureList.size() > 0) {
			for (PictureInfo pojo : pictureList) {
				UnieapFileArchive file = pictureInfoRepository.getFileById(pojo.getFileId());
				PictureInfo opojo = pictureInfoRepository.getById(pojo.getId());
				opojo.setUrl(file.getUrl());
				opojo.setBrief(pojo.getBrief());
				opojo.setName(pojo.getName());
				opojo.setSeq(pojo.getName().split("\\.")[0]);
				pictureInfoRepository.save(opojo);
			}
		}
	}

	public List<PictureInfo> getPictureList(Long bizId, String bizType) {
		return pictureInfoRepository.getPictureList(bizId, bizType, null);
	}
}
