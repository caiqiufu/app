package com.slipper.unieap.bakerstreet.scriptmgt.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.slipper.unieap.bakerstreet.scriptmgt.pojo.RoleInfo;
import com.slipper.unieap.bakerstreet.scriptmgt.vo.RoleInfoVO;
import com.slipper.unieap.db.UnieapRepository;

@Repository
public interface RoleInfoRepository extends UnieapRepository<RoleInfo> {
	
	@Query(value = "select COALESCE((max(convert(e.code,signed))+1),1) from RoleInfo e ")
	Long getSeq();

	@Query(value = "select ri.id as id,ri.scriptId as scriptId,ri.code as code,ri.name as name,ri.sex as sex,ri.brief as brief,pi.url as avatarUrl,ri.activateFlag as activateFlag,ri.createDate as createDate,ri.modifyDate as modifyDate from RoleInfo ri, PictureInfo pi where ri.id = pi.bizId and pi.bizType='role_picture' and ri.scriptId = ?1", countQuery = "select count(*) from RoleInfo ri, PictureInfo pi where ri.id = pi.bizId and pi.bizType='role_picture' and ri.scriptId = ?1")
	Page<RoleInfoVO> getListByScriptId(Long scriptId, Pageable pageable);

	@Query(value = "select ri.id as id,ri.scriptId as scriptId,ri.code as code,ri.name as name,ri.sex as sex,ri.brief as brief,pi.url as avatarUrl,ri.activateFlag as activateFlag,ri.createDate as createDate,ri.modifyDate as modifyDate from RoleInfo ri, PictureInfo pi where ri.id = pi.bizId and pi.bizType='role_picture' and ri.id = ?1")
	RoleInfoVO getOneById(Long id);

	List<RoleInfo> findByScriptId(Long scriptId);

	@Query(value = "select ri.id as id,ri.scriptId as scriptId,ri.code as code,ri.name as name,ri.sex as sex,ri.brief as brief,pi.url as avatarUrl,ri.activateFlag as activateFlag,ri.createDate as createDate,ri.modifyDate as modifyDate from RoleInfo ri, PictureInfo pi where ri.id = pi.bizId and pi.bizType='role_picture' and ri.scriptId = ?1")
	List<RoleInfoVO> getRoleList(Long scriptId);
}
