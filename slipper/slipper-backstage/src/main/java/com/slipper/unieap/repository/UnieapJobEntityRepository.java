package com.slipper.unieap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.slipper.unieap.pojo.UnieapJobEntity;
import com.slipper.unieap.vo.JobVO;

@Repository
public interface UnieapJobEntityRepository extends JpaRepository<UnieapJobEntity, Long> {

	@Query(value = "SELECT je.id as id,je.create_By as createBy,je.create_Date as createDate,je.cron as cron,je.job_Group as jobGroup,je.description as description,je.status as status,tc.activate_flag as activateFlag,"
			+ "je.parameters as parameters,je.remark as remark,tc.id as taskId,tc.task_Type as taskType,tc.task_Desc as taskDesc,tc.class_Name as className,tc.handler_Name as handlerName,tc.retry_Times as retryTimes "
			+ "FROM UNIEAP_JOB_ENTITY je,UNIEAP_TASK_CONFIG tc WHERE je.task_Id = tc.id AND tc.task_Type = ?1", nativeQuery = true)
	List<JobVO> getByTaskType(String taskType);

	@Query(value = "SELECT je.id as id,je.create_By as createBy,je.create_Date as createDate,je.cron as cron,je.job_Group as jobGroup,je.description as description,je.status as status,"
			+ "je.parameters as parameters,je.remark as remark,tc.id as taskId,tc.task_Type as taskType.tc.task_Desc as taskDesc,tc.class_Name as className,tc.handler_Name as handlerName,tc.retry_Times as retryTimes "
			+ "FROM UNIEAP_JOB_ENTITY je,UNIEAP_TASK_CONFIG tc WHERE je.task_Id = tc.id AND tc.id = ?1", nativeQuery = true)
	JobVO getJobById(Long id);

}
