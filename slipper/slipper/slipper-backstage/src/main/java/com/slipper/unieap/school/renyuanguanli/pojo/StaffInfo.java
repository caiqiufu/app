package com.slipper.unieap.school.renyuanguanli.pojo;

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

@Data
@Entity // 生成相应的表
@EntityListeners(AuditingEntityListener.class)
@Table(name = "school_staff_info")
public class StaffInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false, length = 16, columnDefinition = "BIGINT(20) COMMENT 'ID'")
	private Long id;

	@Column(nullable = false, length = 16, columnDefinition = "varchar(32) COMMENT 'user id，用于登录系统'")
	private Long userId;

	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '员工名称'")
	private String name;

	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '性别,M:男,F:女'")
	private String gender;

	@Column(nullable = false, length = 16, columnDefinition = "varchar(32) COMMENT '角色 roleId,园长,行政主管,教师,招生教师,保育员,财务,保健医生,普通职工,其他'")
	private Long roleId;

	@Temporal(TemporalType.DATE)
	@Column(nullable = true, columnDefinition = "DATETIME COMMENT '生日'")
	private Date birthday;

	@Temporal(TemporalType.DATE)
	@Column(nullable = false, columnDefinition = "DATETIME COMMENT '入职时间'")
	private Date employmentDate;

	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '联系电话'")
	private String phoneNumber;

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
