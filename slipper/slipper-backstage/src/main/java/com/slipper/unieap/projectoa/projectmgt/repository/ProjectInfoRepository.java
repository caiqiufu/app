package com.slipper.unieap.projectoa.projectmgt.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.projectoa.projectmgt.pojo.ProjectInfo;

@Repository
public interface ProjectInfoRepository extends UnieapRepository<ProjectInfo> {
	
	@Query(value = "select lpad(COALESCE((max(convert(e.code,signed))+1),1),4,0) as seq from projectoa_project_info e ", nativeQuery = true)
	String getCode();
	
	List<ProjectInfo> findByParentIdOrderByName(Long parentId);

	@Query(value = "select pbid.* from projectoa_projectbid_info pbid where pbid.id not in (select project_id from projectoa_projectbasic_info) and pbid.activate_flag = 'Y'", nativeQuery = true)
	List<ProjectInfo> findProjectInfo();

	@Query(value = "SELECT pi FROM ProjectInfo pi where (pi.name like CONCAT('%',?1,'%') or ?1 is null) and  (pi.jsdw like CONCAT('%',?2,'%') or ?2 is null)", countQuery = "SELECT count(1) FROM ProjectInfo pi where  (pi.name like CONCAT('%',?1,'%') or ?1 is null) and  (pi.jsdw like CONCAT('%',?2,'%') or ?2 is null)")
	Page<ProjectInfo> getProjectInfoPage(String projectName, String sgdw, Pageable pageable);
}
