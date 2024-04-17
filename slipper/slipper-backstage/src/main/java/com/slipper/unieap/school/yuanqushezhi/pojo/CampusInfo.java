package com.slipper.unieap.school.yuanqushezhi.pojo;

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
@Table(name = "school_campus_info")
public class CampusInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false, length = 16, columnDefinition = "BIGINT(20) COMMENT 'ID'")
	private Long id;
	
	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '角色名称'")
	private String name;
	
	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '简称，幼儿园简称可显示在APP首页、微官网、考勤机'")
	private String  abbreviation;

	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '幼儿园类型,GB:私营,SY:私营,PH:普惠'")
	private String type;
	
	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '联系人'")
	private String contactPerson;
	
	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '联系电话'")
	private String phoneNumber;
	
	@Column(nullable = true, length = 16, columnDefinition = "BIGINT(20) COMMENT '班级数量'")
	private Long classCount;
	
	@Column(nullable = true, length = 16, columnDefinition = "BIGINT(20) COMMENT '宝贝数量'")
	private Long kidsCount;
	
	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '所在国家'")
	private String country;
	
	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '省'")
	private String provice;
	
	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '市'")
	private String city;
	
	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '市'")
	private String district;
	
	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '详细地址'")
	private String address;
	
	@Column(nullable = false, length = 16, columnDefinition = "BIGINT(20) COMMENT '幼儿园门头照,文件 ID 该图片会显示于掌心宝贝APP首页'")
	private Long appLogo;
	
	@Column(nullable = true, length = 255, columnDefinition = "varchar(32) COMMENT '园所证件，图片文件 ID，多个逗号(,)分割'")
	private String licence;
	
	@Column(nullable = true, length = 255, columnDefinition = "varchar(32) COMMENT '园所介绍图片,图片文件 ID，多个逗号(,)分割'")
	private String introduction;
	
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
