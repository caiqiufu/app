package com.slipper.unieap.projectoa.projectmgt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.projectoa.projectmgt.pojo.TaskInfo;
import com.slipper.unieap.projectoa.projectmgt.vo.TaskInfoVO;

@Repository
public interface TaskInfoRepository extends UnieapRepository<TaskInfo> {
	void deleteByProjectIdAndEmployeeIdAndTaskId(Long projectId, Long employeeId, Long taskId);

	@Query(value = "SELECT ti.id as id, td.id as taskId,td.name as taskName,ti.employee_Id as employeeId,ti.project_Id as projectId, (case when ti.id IS NULL then 'N' else 'Y' end) as notifyFlag "
			+ "FROM projectoa_task_define td left join projectoa_task_info ti on td.id = ti.task_Id "
			+ "WHERE ti.project_id = ?1 AND (td.name like CONCAT('%',?2,'%') or ?2 is null) order by td.name", nativeQuery = true)
	List<TaskInfoVO> getAllTaskInfoList(Long projectId,String taskName);

	List<TaskInfo> findByProjectIdAndTaskId(Long projectId,Long taskId);
	
	@Query(value = "SELECT ti.id as id, td.id as taskId,td.name as taskName,td.message_Template as messageTemplate,ti.employee_Id as employeeId,ti.project_Id as projectId, (case when ti.id IS NULL then 'N' else 'Y' end) as notifyFlag,"
			+ "pi.name as projectName "
			+ "FROM projectoa_task_define td, projectoa_task_info ti,projectoa_project_info pi"
			+ " WHERE td.id = ti.task_Id AND ti.project_Id = pi.id "
			+ "AND ti.task_Id = ?1 order by td.name", nativeQuery = true)
	List<TaskInfoVO> getByTaskIdList(Long taskId);
}
