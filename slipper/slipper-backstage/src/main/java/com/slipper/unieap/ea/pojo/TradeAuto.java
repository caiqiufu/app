package com.slipper.unieap.ea.pojo;

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

@Data
@Entity // 生成相应的表
@EntityListeners(AuditingEntityListener.class)
@Table(name = "trade_auto")
public class TradeAuto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(unique = true, nullable = false, length = 16, columnDefinition = "BIGINT(20) COMMENT 'ID'")
	private Long id;

	@Column(nullable = false, length = 45, columnDefinition = "varchar(45) COMMENT '执行服务器IP'")
	private String executeIp;

	@Column(nullable = false, length = 45, columnDefinition = "varchar(45) COMMENT '策略服务器IP'")
	private String eaIp;

	@Column(nullable = false, length = 45, columnDefinition = "varchar(45) COMMENT '执行窗口编码'")
	private String formNo;

	@Column(nullable = false, length = 255, columnDefinition = "varchar(255) COMMENT '账户'")
	private String accountCode;

	@Column(nullable = true, length = 45, columnDefinition = "varchar(45) COMMENT '登录状态'")
	private String loginStatus;
	
	/**
	 * 自动交易标识,启动托管后,可以手工暂停自动交易
	 */
	@Column(nullable = true, length = 45, columnDefinition = "varchar(45) COMMENT '是否自动交易'")
	private String autoTrade;

	@Transient
	private String loginStatusDesc;

	public String getLoginStatusDesc() {
		this.loginStatusDesc = UnieapConstants.getDicData("ACTIVATE_FLAG", loginStatus).getDicName();
		return loginStatusDesc;
	}

	@Column(nullable = true, columnDefinition = "DATETIME COMMENT '登陆时间'")
	private Date loginDate;

	@Column(nullable = true, length = 45, columnDefinition = "varchar(45) COMMENT '托管状态'")
	private String autoStatus;

	@Transient
	private String autoStatusDesc;

	public String getAutoStatusDesc() {
		this.autoStatusDesc = UnieapConstants.getDicData("ACTIVATE_FLAG", autoStatus).getDicName();
		return autoStatusDesc;
	}

	@Column(nullable = true, columnDefinition = "DATETIME COMMENT '登陆时间'")
	private Date autoDate;

	@Column(nullable = true, length = 255, columnDefinition = "varchar(255) COMMENT '异常信息'")
	private String errorMsg;

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
