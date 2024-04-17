package com.slipper.unieap.school.jiaowuguanli.pojo;

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
@Table(name = "school_weeking_plan_detail")
public class WeekingPlanDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false, length = 16, columnDefinition = "BIGINT(20) COMMENT 'ID'")
	private Long id;
	
	@Column(nullable = false, length = 16, columnDefinition = "BIGINT(20) COMMENT '序列'")
	private Long seq;
	
	@Column(nullable = false, length = 16, columnDefinition = "BIGINT(20) COMMENT '周计划 ID'")
	private Long planId;
	
	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '发布类型,T:文字版,P:图片版'")
	private String type;
	
	@Column(nullable = true, length = 255, columnDefinition = "varchar(32) COMMENT '图片文件 ID'")
	private String fileId;
	
	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '星期'")
	private String week;
	
	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '日期'")
	private String date;
	
	@Column(nullable = true, length = 1024, columnDefinition = "varchar(32) COMMENT '上午'")
	private String morning;
	
	@Column(nullable = true, length = 1024, columnDefinition = "varchar(32) COMMENT '下午'")
	private String afternoon;
	
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
