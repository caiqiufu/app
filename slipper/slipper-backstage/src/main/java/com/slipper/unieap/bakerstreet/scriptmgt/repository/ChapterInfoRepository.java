package com.slipper.unieap.bakerstreet.scriptmgt.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.slipper.unieap.bakerstreet.scriptmgt.pojo.ChapterInfo;
import com.slipper.unieap.bakerstreet.scriptmgt.vo.ChapterInfoVO;
import com.slipper.unieap.db.UnieapRepository;

@Repository
public interface ChapterInfoRepository extends UnieapRepository<ChapterInfo> {

	@Query(value = "select COALESCE((max(convert(e.code,signed))+1),1) from ChapterInfo e ")
	Long getSeq();
	
	@Query(value = "select ci.id as id,ci.scriptId as scriptId,ci.roleId as roleId,ri.name as roleName,ci.chapterNumber as chapterNumber,ci.code as code,ci.name as name, ci.brief as brief, ci.createDate as createDate from ChapterInfo ci,RoleInfo ri where ci.roleId = ri.id and ci.scriptId = ?1 ", countQuery = "select count(*)  from ChapterInfo ci,RoleInfo ri where ci.roleId = ri.id and ci.scriptId = ?1")
	Page<ChapterInfoVO> getListByScriptId(Long scriptId, Pageable pageable);

	@Query(value = "select ci.id as id,ci.scriptId as scriptId,ci.roleId as roleId,ri.name as roleName,ci.chapterNumber as chapterNumber,ci.code as code,ci.name as name, ci.brief as brief, ci.createDate as createDate from ChapterInfo ci,RoleInfo ri where ci.roleId = ri.id and ci.id = ?1 ")
	ChapterInfoVO getOneById(Long id);
    
	@Query(value = "select ci.id as id,ci.scriptId as scriptId,ci.roleId as roleId,ri.name as roleName,ci.chapterNumber as chapterNumber,ci.code as code,ci.name as name, ci.brief as brief, ci.createDate as createDate from ChapterInfo ci,RoleInfo ri where ci.roleId = ri.id and ri.id = ?1 ")
	List<ChapterInfoVO> getChapterInfoList(Long roleId);
	
	ChapterInfo findByRoleIdAndChapterNumber(Long roleId,String chapterNumber);
}
