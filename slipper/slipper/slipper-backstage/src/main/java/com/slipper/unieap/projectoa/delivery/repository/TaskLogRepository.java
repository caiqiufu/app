package com.slipper.unieap.projectoa.delivery.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.projectoa.delivery.pojo.TaskLog;

@Repository
public interface TaskLogRepository extends UnieapRepository<TaskLog> {

	@Query(value = "SELECT tl FROM TaskLog tl WHERE tl.projectId=?1 AND tl.employeeId=?2 AND tl.taskId = ?3 AND tl.taskStatus IN (?4) ")
	List<TaskLog> getEmployeeAllTaskList(Long projectId, Long employeeId, Long taskId, List<String> taskStatus);

	@Query(value = "SELECT tl FROM TaskLog tl WHERE tl.projectId=?1 AND tl.employeeId=?2 AND tl.taskId = ?3 AND tl.taskStatus IN (?4) AND tl.createDate>=?5 AND tl.createDate<=?6")
	List<TaskLog> getEmployeeDailyTaskList(Long projectId, Long employeeId, Long taskId, List<String> taskStatus,
			Date startTime, Date endTime);

}
