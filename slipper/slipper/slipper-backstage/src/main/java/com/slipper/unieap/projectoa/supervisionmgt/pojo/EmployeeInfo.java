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
@Table(name = "projectoa_employee_info", uniqueConstraints = {
		@UniqueConstraint(name = "unique_name", columnNames = "name") })
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EmployeeInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false, length = 20, columnDefinition = "BIGINT(20) COMMENT 'ID'")
	private Long id;

	@Column(nullable = false, length = 20, columnDefinition = "BIGINT(20) COMMENT '公司ID'")
	private Long companyId;

	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '员工姓名'")
	private String name;

	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '性别 GENDER'")
	private String gender;
	
	@Column(nullable = true, length = 512, columnDefinition = "varchar(512) COMMENT '头像'")
	private String avatarUrl;

	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '员工岗位类型 EMPLOYEE_POSITION,1:工程师,2:项目经理,3:项目负责人'")
	private String position;

	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '员工身份证号码'")
	private String idNumber;

	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '员工电话号码'")
	private String phone;

	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '微信'")
	private String wx;
	
	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '邮箱'")
	private String email;

	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '微信唯一标识'")
	private String wxauId;
	
	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '执业资格 CERTIFICATE_TYPE'")
	private String certificateType;
	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '执业资格证号'")
	private String certificateLicense;

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
