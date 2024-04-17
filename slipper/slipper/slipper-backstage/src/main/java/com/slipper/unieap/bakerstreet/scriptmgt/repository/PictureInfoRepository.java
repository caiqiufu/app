package com.slipper.unieap.bakerstreet.scriptmgt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.slipper.unieap.bakerstreet.scriptmgt.pojo.PictureInfo;
import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.file.pojo.UnieapFileArchive;

@Repository
public interface PictureInfoRepository extends UnieapRepository<PictureInfo> {

	@Query("select pi from PictureInfo pi where (pi.bizId =?1 or ?1 is null) and (pi.bizType =?2 or ?2 is null) and (pi.fileId =?3 or ?3 is null) order by seq")
	List<PictureInfo> getPictureList(Long bizId, String bizType, Long fileId);

	@Query("select e from UnieapFileArchive e where e.id = ?1")
	UnieapFileArchive getFileById(Long fileId);

	@Transactional
	@Modifying
	void deleteByBizId(Long bizId);

	@Transactional
	@Modifying
	void deleteByBizIdAndBizType(Long bizId, String bizType);
}
