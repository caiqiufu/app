package com.slipper.unieap.projectoa.projectmgt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.projectoa.projectmgt.pojo.ProjectAttribute;
import com.slipper.unieap.projectoa.projectmgt.vo.ProjectAttributeVO;

@Repository
public interface ProjectAttributeRepository extends UnieapRepository<ProjectAttribute> {

	@Query(value = "select COALESCE((max(convert(e.seq,signed))+1),1) from ProjectAttribute e where projectId = ?1")
	Long getSeq(Long projectId);

	@Query(value = "SELECT ad.id as attributeId,ad.group_Code as groupCode,ad.group_Name as groupName,ad.display_Name as displayName,"
			+ "ad.attribute_Name as attributeName,ad.ui_Type as uiType,ad.dicgroup_Code as dicgroupCode,ad.data_Type as dataType,ad.unit as unit,"
			+ "pa.id as projectAttributeId,pa.project_Id as projectId,CASE WHEN (pa.id is null) THEN 'N' ELSE 'Y' END as assigned,pa.seq as seq"
			+ " FROM projectoa_attribute_define ad left join projectoa_project_attribute pa on ad.id = pa.attribute_Id  "
			+ "where (ad.display_name like CONCAT('%',?1,'%') or ?1 is null) order by pa.seq is null,pa.seq", nativeQuery = true)
	List<ProjectAttributeVO> getAllAttributeList(String attributeName);

	@Query(value = "SELECT ad.id as attributeId,ad.group_Code as groupCode,ad.group_Name as groupName,ad.display_Name as displayName,"
			+ " ad.attribute_Name as attributeName,ad.ui_Type as uiType,ad.dicgroup_Code as dicgroupCode,ad.data_Type as dataType,ad.unit as unit,"
			+ " pa.id as projectAttributeId,pa.project_Id as projectId,pa.attribute_Value as attributeValue,"
			+ " CASE WHEN (pa.id is null) THEN 'N' ELSE 'Y' END as assigned,pa.seq as seq "
			+ " FROM projectoa_project_attribute pa left join projectoa_attribute_define ad on pa.attribute_Id = ad.id "
			+ " WHERE pa.project_Id = ?1 and (ad.display_name like CONCAT('%',?2,'%') or ?2 is null) order by pa.seq", nativeQuery = true)
	List<ProjectAttributeVO> getProjectAttributeList(Long projectId, String attributeName);

}
