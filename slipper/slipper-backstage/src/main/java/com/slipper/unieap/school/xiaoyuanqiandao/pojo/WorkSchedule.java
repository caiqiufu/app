package com.slipper.unieap.school.xiaoyuanqiandao.pojo;

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
@Table(name = "school_work_schedule")
public class WorkSchedule implements Serializable {

	/**
	 * 说明： 1. 各个考勤时段内，最早一次入校打卡为上课签到，最晚一次离校计为该时段下班签退；4次和6次打卡有个分界点，目的是区分开上下考勤时段； 2.
	 * 在设定时段内打卡都按该类型标记，如第1段入园时段内无论打几次卡，都记入园
	 * 注意：添加和编辑后次日生效
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false, length = 16, columnDefinition = "BIGINT(20) COMMENT 'ID'")
	private Long id;
	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '班次名称'")
	private String name;

	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '打卡方式,1:一天打卡两次,2:一天打开四次,3:一天打卡六次'")
	private String type;

	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '打卡时间段1,08:00-11:30'")
	private String worktimeDuration1;

	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '分界点1,12:00'")
	private String subsection1;

	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '打卡时间段2'")
	private String worktimeDuration2;

	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '分界点2'")
	private String subsection2;

	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '打卡时间段3'")
	private String worktimeDuration3;
	
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
