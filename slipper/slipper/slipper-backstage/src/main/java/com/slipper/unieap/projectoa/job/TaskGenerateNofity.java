package com.slipper.unieap.projectoa.job;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.projectoa.delivery.pojo.TaskLog;
import com.slipper.unieap.projectoa.delivery.repository.TaskLogRepository;
import com.slipper.unieap.projectoa.projectmgt.repository.TaskInfoRepository;
import com.slipper.unieap.projectoa.projectmgt.vo.TaskInfoVO;
import com.slipper.unieap.projectoa.supervisionmgt.pojo.EmployeeInfo;
import com.slipper.unieap.projectoa.supervisionmgt.repository.EmployeeInfoRepository;
import com.slipper.unieap.task.job.DynamicJob;

/**
 * 生成提醒
 * 
 * @author caiqi
 *
 */
@Service
public class TaskGenerateNofity extends DynamicJob {

	@Autowired
	public TaskInfoRepository taskInfoRepository;

	@Autowired
	public TaskLogRepository taskLogRepository;

	@Autowired
	public EmployeeInfoRepository employeeInfoRepository;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		generateNotify(Long.decode("3251"));
	}

	/**
	 * 生成提醒
	 */
	public void generateNotify(Long taskId) {

		List<TaskInfoVO> taskInfoList = taskInfoRepository.getByTaskIdList(taskId);
		if (taskInfoList != null && taskInfoList.size() > 0) {
			List<TaskLog> logList = new ArrayList<TaskLog>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 EEE");
			Date date = new Date();
			String datetime = sdf.format(date);
			for (TaskInfoVO vo : taskInfoList) {
				String[] employeeIds = vo.getEmployeeId().split(",");
				for (String employeeId : employeeIds) {
					TaskLog log = new TaskLog();
					logList.add(log);
					log.setId(UnieapConstants.getSeq(null));
					log.setEmployeeId(Long.decode(employeeId));
					EmployeeInfo employeeInfo = employeeInfoRepository.getById(Long.decode(employeeId));
					log.setEmployeeName(employeeInfo.getName());
					log.setMessage(MessageFormat.format(vo.getMessageTemplate(), datetime));
					log.setNotifyStatus("0");
					log.setProjectId(vo.getProjectId());
					log.setProjectName(vo.getProjectName());
					log.setTaskeName(vo.getTaskName());
					log.setTaskId(vo.getTaskId());
					log.setTaskStatus("1");
					log.setActivateFlag(UnieapConstants.YES);
					log.setTenantId(Long.decode("1"));
				}
			}
			taskLogRepository.saveAll(logList);
		}
	}
}
