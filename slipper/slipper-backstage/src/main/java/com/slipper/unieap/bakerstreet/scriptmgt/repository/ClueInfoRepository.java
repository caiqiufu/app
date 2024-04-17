package com.slipper.unieap.bakerstreet.scriptmgt.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.slipper.unieap.bakerstreet.scriptmgt.pojo.ClueInfo;
import com.slipper.unieap.bakerstreet.scriptmgt.vo.ClueDetailVO;
import com.slipper.unieap.db.UnieapRepository;

@Repository
public interface ClueInfoRepository extends UnieapRepository<ClueInfo> {

	@Query(value = "select COALESCE((max(convert(e.code,signed))+1),1) from ClueInfo e ")
	Long getSeq();

	@Query(value = "select cd.id as id,cd.code as code,cd.name as name,ci.id as clueId,ci.name as clueName,ci.scriptId as scriptId,pi.url as url,cd.createDate as createDate from ClueDetail cd, ClueInfo ci,PictureInfo pi where cd.clueId = ci.id and cd.id = pi.bizId and pi.bizType='clue_picture' and ci.scriptId = ?1 order by cd.code ", countQuery = "select count(*)  from ClueDetail cd, ClueInfo ci,PictureInfo pi where cd.clueId = ci.id and cd.id = pi.bizId and pi.bizType='clue_picture' and ci.scriptId = ?1")
	Page<ClueDetailVO> findByScriptId(Long scriptId, Pageable pageable);

	@Query(value = "select ci from  ClueInfo ci where ci.scriptId = ?1 ")
	List<ClueInfo> getClueInfoListByScriptId(Long scriptId);

	@Query(value = "select cd.id as id,cd.code as code,cd.name as name,ci.id as clueId,ci.name as clueName,ci.scriptId as scriptId,pi.url as url,cd.createDate as createDate from ClueDetail cd, ClueInfo ci,PictureInfo pi where cd.clueId = ci.id and cd.id = pi.bizId and pi.bizType='clue_picture' and cd.id = ?1 order by pi.seq")
	ClueDetailVO getClueDetailInfo(Long id);

	@Query(value = "select cd.id as id,cd.code as code,cd.name as name,ci.id as clueId,ci.name as clueName,ci.scriptId as scriptId,pi.url as url,cd.createDate as createDate from ClueDetail cd, ClueInfo ci,PictureInfo pi where cd.clueId = ci.id and cd.id = pi.bizId and pi.bizType='clue_picture' and ci.scriptId = ?1 order by pi.seq")
	List<ClueDetailVO> getClueDetailListByScriptId(Long scriptId);

	@Query("select cd.id as id,cd.code as code,cd.name as name,cd.clueId as clueId,ci.name as clueName,cd.url as url from ClueDetail cd,ClueInfo ci where cd.clueId = ci.id and cd.id = ?1")
	ClueDetailVO getClueDetail(Long clueDetailId);

	@Transactional
	@Modifying
	void deleteByScriptId(Long scriptId);

}
