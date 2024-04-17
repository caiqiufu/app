package com.slipper.unieap.verifycode.pojo;

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

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@Data
@Entity // 生成相应的表
@EntityListeners(AuditingEntityListener.class)
@Table(name = "unieap_verifycode")
public class UnieapVerifycode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(unique = true, nullable = false, length = 16, columnDefinition = "BIGINT(20) COMMENT 'ID'")
	private Long id;
	@Column(nullable = false, length = 45, columnDefinition = "varchar(45) COMMENT '应用编码'")
	private String appCode;

	@Column(nullable = false, length = 45, columnDefinition = "varchar(45) COMMENT '用户编码'")
	private String userCode;
	
	@Column(nullable = true, length = 255, columnDefinition = "varchar(255) COMMENT 'SESSIONID'")
	private String sessionId;

	@Column(nullable = false, length = 45, columnDefinition = "varchar(45) COMMENT '验证业务类型'")
	private String verifyType;

	@Column(nullable = false, length = 45, columnDefinition = "varchar(45) COMMENT '验证码'")
	private String verifyCode;

	// 通用属性
	@Column(nullable = false, length = 1, columnDefinition = "varchar(1) COMMENT '启停标志:Y:启动,N:停用'")
	private String activateFlag;

	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false, columnDefinition = "DATETIME COMMENT '该记录创建时间'")
	private Date createDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false, columnDefinition = "DATETIME COMMENT '失效时间'")
	private Date expireyDate;

	@Column(nullable = false, length = 16, columnDefinition = "BIGINT(20) COMMENT '租户ID'")
	private Long tenantId;

	@Column(nullable = true, length = 255, columnDefinition = "varchar(255) COMMENT '备注'")
	private String remark;
}
