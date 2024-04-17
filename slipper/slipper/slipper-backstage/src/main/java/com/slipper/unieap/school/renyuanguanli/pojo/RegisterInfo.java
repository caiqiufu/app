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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.slipper.unieap.UnieapConstants;

import lombok.Data;

//当pojo当vo使用时使用该属性，避免序列化出错
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
@Data
@Entity // 生成相应的表
@EntityListeners(AuditingEntityListener.class)
@Table(name = "school_register_info")
public class RegisterInfo implements Serializable {

	/**
	 * 学籍信息
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false, length = 16, columnDefinition = "BIGINT(20) COMMENT 'ID'")
	private Long id;

	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '身份证件类型,居民身份证，香港特区护照/身份证明,澳门特区护照/身份证明,台湾居民往来大陆通行证,境外永久居住证,护照,其他'")
	private String idType;

	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '身份证件号码'")
	private String idNumber;

	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '血型：未知血型,A血型,B血型,AB血型,O血型,RH阳性血型,RH阴性血型,未定血型,其他血型'")
	private String bloodType;

	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '国籍'")
	private String nationality;

	@Column(nullable = true, length = 255, columnDefinition = "varchar(32) COMMENT '出生所在地'")
	private String birthplace;

	@Column(nullable = true, length = 255, columnDefinition = "varchar(32) COMMENT '籍贯'")
	private String nativeplace;

	@Column(nullable = true, length = 255, columnDefinition = "varchar(32) COMMENT '户口所在地'")
	private String hukouplace;

	@Column(nullable = true, length = 255, columnDefinition = "varchar(32) COMMENT '现住地址'")
	private String currentplace;

	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '就读方式：走读，住校'")
	private String stayType;

	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '健康状况:健康或良好，一般或较弱，有慢性病，残疾'")
	private String healthStatus;

	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '是否孤儿'")
	private String orphan;

	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '是否残疾幼儿'")
	private String deformity;

	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '监护人姓名'")
	private String guardian;

	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '监护人身份证类型'")
	private String guardianIdtype;

	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '监护人身份证件号码'")
	private String guardianIdnumber;

	@Transient
	private Long kidsId;

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
