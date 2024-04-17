package com.slipper.unieap.projectoa.delivery.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.slipper.unieap.UnieapConstants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity // 生成相应的表
@EntityListeners(AuditingEntityListener.class)
@Table(name = "projectoa_task_log")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class TaskLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false, length = 20, columnDefinition = "BIGINT(20) COMMENT 'ID'")
	private Long id;

	@Column(nullable = false, length = 20, columnDefinition = "BIGINT(20) COMMENT '项目ID'")
	private Long projectId;

	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '项目名称'")
	private String projectName;

	@Column(nullable = false, length = 20, columnDefinition = "BIGINT(20) COMMENT '员工ID'")
	private Long employeeId;

	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '员工名称'")
	private String employeeName;

	@Column(nullable = false, length = 20, columnDefinition = "BIGINT(20) COMMENT '任务ID'")
	private Long taskId;

	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '任务名称'")
	private String taskeName;

	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '任务状态 TASK_STATUS,1:未完成，2进行中，3已完成'")
	private String taskStatus;

	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '提醒状态 NOTIFY_STATUS,0：未提醒,1:已短信提醒，2:已微信提醒，'")
	private String notifyStatus;

	@Column(nullable = false, length = 512, columnDefinition = "varchar(512) COMMENT '提醒消息'")
	private String message;

	// 通用属性
		@Column(nullable = false, length = 1, columnDefinition = "varchar(1) COMMENT '启停标志:Y:启动,N:停用'")
		private String activateFlag;

		@Transient
		private String activateFlagDesc;

		public String getActivateFlagDesc() {
			this.activateFlagDesc = UnieapConstants.getDicData("ACTIVATE_FLAG", activateFlag).getDicName();
			return activateFlagDesc;
		}

		@CreatedDate
		@Temporal(TemporalType.TIMESTAMP)
		@Column(nullable = false, columnDefinition = "DATETIME COMMENT '该记录创建时间'")
		private Date createDate;

		@CreatedBy
		@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '该记录创建人'")
		private String createBy;

		@LastModifiedDate
		@Column(nullable = true, columnDefinition = "DATETIME COMMENT '该记录修改时间'")
		private Date modifyDate;

		@LastModifiedBy
		@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '该记录修改人'")
		private String modifyBy;

		@Column(nullable = false, length = 16, columnDefinition = "BIGINT(20) COMMENT '租户ID'")
		private Long tenantId;

		@Column(nullable = true, length = 255, columnDefinition = "varchar(255) COMMENT '备注'")
		private String remark;
}
