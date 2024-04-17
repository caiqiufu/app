package com.slipper.unieap.bakerstreet.scriptmgt.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.slipper.unieap.bakerstreet.scriptmgt.pojo.ScriptInfo;
import com.slipper.unieap.bakerstreet.scriptmgt.vo.ScriptInfoVO;
import com.slipper.unieap.db.UnieapRepository;

@Repository
public interface ScriptInfoRepository extends UnieapRepository<ScriptInfo> {

	/*
	 * 1. 直接查询pojo不能获取数据 2.需要字段别名
	 */
	
	@Query(value = "select COALESCE((max(convert(e.code,signed))+1),1) from ScriptInfo e ")
	Long getSeq();

	@Query(value = "select si.id as id,si.code as code,si.name as name,si.typeList as typeList,pi.url as posterUrl,si.category as category,si.duration as duration,si.playerCount as playerCount,si.clueList as clueList,si.scriptChapter as scriptChapter,si.publishFlag as publishFlag,si.publishDate as publishDate,si.brief as brief,si.createDate as createDate from ScriptInfo si left join PictureInfo pi on si.id = pi.bizId where (pi.bizType='poster_picture' or pi.bizType is null) and (si.name like CONCAT('%',?1,'%') or ?1 is null) and (si.typeList like CONCAT('%',?2,'%') or ?2 is null) and (si.category like CONCAT('%',?3,'%') or ?3 is null)", countQuery = "select count(*)  from ScriptInfo si left join PictureInfo pi on si.id = pi.bizId where (pi.bizType='poster_picture' or pi.bizType is null) and (si.name like CONCAT('%',?1,'%') or ?1 is null) and (si.typeList like CONCAT('%',?2,'%') or ?2 is null) and (si.category like CONCAT('%',?3,'%') or ?3 is null)")
	Page<ScriptInfoVO> getScriptPage(String scriptName, String typeList, String category, Pageable pageable);

	@Query(value = "select si.id as id,si.code as code,si.name as name,si.typeList as typeList,pi.url as posterUrl,si.category as category,si.duration as duration,si.playerCount as playerCount,si.clueList as clueList,si.scriptChapter as scriptChapter,si.publishFlag as publishFlag,si.publishDate as publishDate,si.brief as brief,si.createDate as createDate from ScriptInfo si left join PictureInfo pi on si.id = pi.bizId where si.publishFlag=1 and (pi.bizType='poster_picture' or pi.bizType is null) and (si.name like CONCAT('%',?1,'%') or ?1 is null) and (si.typeList like CONCAT('%',?2,'%') or ?2 is null) and (si.category like CONCAT('%',?3,'%') or ?3 is null) and (si.playerCount like CONCAT('%',?4,'%') or ?4 is null)")
	List<ScriptInfoVO> getScriptList(String scriptName, String typeList, String category,String playerCount);

	List<ScriptInfo> findByActivateFlag(String activateFlag);

	@Query(value = "select si.id as id,si.code as code,si.name as name,si.typeList as typeList,pi.url as posterUrl,si.category as category,si.duration as duration,si.playerCount as playerCount,si.clueList as clueList,si.scriptChapter as scriptChapter,si.publishFlag as publishFlag,si.publishDate as publishDate,si.brief as brief,si.createDate as createDate from ScriptInfo si left join PictureInfo pi on si.id = pi.bizId where (pi.bizType='poster_picture' or pi.bizType is null) and si.id = ?1")
	ScriptInfoVO getOneById(Long id);
}
