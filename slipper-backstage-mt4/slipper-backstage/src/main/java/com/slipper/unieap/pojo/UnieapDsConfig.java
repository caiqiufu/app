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
 * The persistent class for the mdm_task_config database table.
 * 
 */

@Data
@Entity // 生成相应的表
@EntityListeners(AuditingEntityListener.class)
@Table(name = "unieap_ds_config")

public class UnieapDsConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false, length = 16, columnDefinition = "BIGINT(20) COMMENT 'ID'")
	private Long id;
	
	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '数据源编码'")
	private String dsCode;
	
	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '数据源名称'")
	private String dsName;
	
	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '数据源类型:MYSQL,ORACLE,HIVEDB'")
	private String dsType;
	
	@Transient
	private String dsTypeDesc;
	
	@Column(nullable = false, length = 1024, columnDefinition = "varchar(1024) COMMENT '数据源配置'")
	private String config;

	// 通用属性
	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '启停标志:Y:启动,N:停用'")
	private String activateFlag;
	
	@Transient
	private String activateFlagDesc;
	
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

	public String getActivateFlagDesc() {
		this.activateFlagDesc = UnieapConstants.getDicData("ACTIVATE_FLAG", activateFlag).getDicName();
		return activateFlagDesc;
	}

}