package com.slipper.unieap.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
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
 * The persistent class for the mdm_task_execute database table.
 * 
 */
@Data
@Entity // 生成相应的表
@EntityListeners(AuditingEntityListener.class)
@Table(name = "unieap_order")
public class UnieapOrder implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(unique = true, nullable = false, length = 20, columnDefinition = "BIGINT(20) COMMENT 'ID'")
	private Long id;
	
	@Column(nullable = false, length = 45, columnDefinition = "varchar(45) COMMENT '订单类型'")
	private String orderType;
	
	@Column(nullable = true, length = 20, columnDefinition = "BIGINT(20) COMMENT '任务ID'")
	private Long pendingOrderId;

	@Column(nullable = true, length = 2000, columnDefinition = "varchar(2000) COMMENT '输入参数'")
	private String inputParameters;
	
	@Column(nullable = true, length = 2000, columnDefinition = "varchar(2000) COMMENT '输出参数'")
	private String outputParameters;
	
	@Column(nullable = false, length = 45, columnDefinition = "varchar(45) COMMENT '订单状态'")
	private String orderStatus;
	
	
	@Column(nullable = true, length = 255, columnDefinition = "varchar(255) COMMENT '订单执行时间'")
	private String executeSchedule;
	
	@Column(nullable = true, length = 255, columnDefinition = "varchar(255) COMMENT '订单执行类'")
	private String executeClass;
	
	@Column(nullable = true, length = 2000, columnDefinition = "varchar(2000) COMMENT '订单执行结果'")
	private String executeResult;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true, columnDefinition = "DATETIME COMMENT '执行结束时间'")
	private Date executeEndDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false, columnDefinition = "DATETIME COMMENT '执行开始时间'")
	private Date executeStartDate;


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