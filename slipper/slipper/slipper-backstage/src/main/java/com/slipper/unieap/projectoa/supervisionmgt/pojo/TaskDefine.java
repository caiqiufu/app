package com.slipper.unieap.projectoa.supervisionmgt.pojo;

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
import javax.persistence.UniqueConstraint;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.slipper.unieap.UnieapConstants;

import lombok.Data;

@Data
@Entity // 生成相应的表
@EntityListeners(AuditingEntityListener.class)
@Table(name = "projectoa_task_define", uniqueConstraints = {
		@UniqueConstraint(name = "unique_name", columnNames = "name") })
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TaskDefine implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false, length = 20, columnDefinition = "BIGINT(20) COMMENT 'ID'")
	private Long id;

	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '监理月报'")
	private String name;

	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '任务类型 TASK_TYPE,1:提交提醒，2:审阅提醒'")
	private String type;
	@Transient
	private String typeDesc;
	
	public String getTypeDesc() {
		this.typeDesc = UnieapConstants.getDicData("TASK_TYPE", type).getDicName();
		return typeDesc;
	}
	
	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '优先级'")
	private String priority;
	
	@Column(nullable = false, length = 512, columnDefinition = "varchar(512) COMMENT '提醒周期'")
	private String notifyCron;

	@Column(nullable = true, length = 512, columnDefinition = "varchar(512) COMMENT '提醒消息模版'")
	private String messageTemplate;


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
