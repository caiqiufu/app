package com.slipper.unieap.pojo;

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

import com.slipper.unieap.UnieapConstants;

import lombok.Data;

/**
 * The persistent class for the mdm_task_instance database table.
 * 
 */
@Data
@Entity // 生成相应的表
@EntityListeners(AuditingEntityListener.class)
@Table(name = "UNIEAP_JOB_ENTITY")
public class UnieapJobEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false, length = 16, columnDefinition = "BIGINT(20) COMMENT 'ID'")
	private Long id;

	@Column(nullable = false, length = 255, columnDefinition = "varchar(255) COMMENT '执行策略'")
	private String cron;

	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '组'")
	private String jobGroup; // job组名

	@Column(nullable = true, length = 255, columnDefinition = "varchar(255) COMMENT '任务描述'")
	private String description;

	@Column(nullable = true, length = 255, columnDefinition = "varchar(255) COMMENT '任务参数'")
	private String parameters;

	@Column(nullable = false, length = 20, columnDefinition = "BIGINT(20) COMMENT '任务ID'")
	private Long taskId;

	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '状态 0:草稿,1:已发布,2:已撤回,3:废弃'")
	private String status;

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