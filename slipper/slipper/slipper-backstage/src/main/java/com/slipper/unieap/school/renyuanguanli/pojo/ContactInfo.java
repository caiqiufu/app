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
@Table(name = "school_contact_info")
public class ContactInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false, length = 16, columnDefinition = "BIGINT(20) COMMENT 'ID'")
	private Long id;
	
	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '联系人名称'")
	private String name;
	
	@Column(nullable = false, length = 255, columnDefinition = "varchar(32) COMMENT '密码'")
	private String password;

	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '手机号码'")
	private String phoneNumber;
	
	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '证件类型,居民身份证,军官证,士兵证,文职干部证,部队退休证,香港特区护照/身份证明,澳门特区护照/身份证明,台湾居民往来大陆通行证,境外永久居住证,护照,户口簿,其他'")
	private String idType;
	
	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '证件号码'")
	private String idNumber;
	
	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '民族'")
	private String nation;
	
	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '教育程度,初中,高中,中专,大专,本科,硕士,博士'")
	private String education ;
	
	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '职业'")
	private String occupation ;
	
	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '电子邮箱'")
	private String email ;
	
	@Column(nullable = true, length = 16, columnDefinition = "BIGINT(20) COMMENT '头像 fileId'")
	private Long profileId;

	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '宝贝注册来源,SYS:后台录入,APP:APP录入'")
	private String sourceType;
	
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
