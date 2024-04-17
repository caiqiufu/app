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
@Table(name = "school_kids_info")
public class KidsInfo implements Serializable {

	/**
	 * 说明： 1. 各个考勤时段内，最早一次入校打卡为上课签到，最晚一次离校计为该时段下班签退；4次和6次打卡有个分界点，目的是区分开上下考勤时段； 2.
	 * 在设定时段内打卡都按该类型标记，如第1段入园时段内无论打几次卡，都记入园 注意：添加和编辑后次日生效
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false, length = 16, columnDefinition = "BIGINT(20) COMMENT 'ID'")
	private Long id;

	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '编码'")
	private String code;

	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '宝贝名称'")
	private String name;
	
	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '宝贝昵称'")
	private String nickname;

	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '性别,M:男,F:女'")
	private String gender;

	@Column(nullable = true, length = 16, columnDefinition = "BIGINT(20) COMMENT '头像 '")
	private Long profileId;

	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '宝贝注册来源,SYS:后台录入,APP:APP录入'")
	private String sourceType;

	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '身高'")
	private String height;

	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '体重'")
	private String weight;

	@Temporal(TemporalType.DATE)
	@Column(nullable = false, columnDefinition = "DATETIME COMMENT '生日'")
	private Date birthday;

	@Temporal(TemporalType.DATE)
	@Column(nullable = false, columnDefinition = "DATETIME COMMENT '入园时间'")
	private Date registerDate;
	
	@Column(nullable = true, length = 16, columnDefinition = "BIGINT(20) COMMENT '学籍 ID'")
	private Long registerId;

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
