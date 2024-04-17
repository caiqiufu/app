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
@Table(name = "school_work_schedule_ins")
public class WorkScheduleIns implements Serializable {

	/**
	 * 排班规则,国家法定节假日都是默认为放假的，有需要修改的可以在“特殊日期设置”中修改
	 * 注意：添加和编辑后次日生效
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false, length = 16, columnDefinition = "BIGINT(20) COMMENT 'ID'")
	private Long id;
	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '排班表名称'")
	private String name;

	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '添加方式,C:按班级添加,S：按学生添加,T:按教师添加一个班级或一个学生只能创建一个在用排班表；按班级添加创建一个在用排班表后，可再创建一个按学生添加的在用排班表，但优先走按学生添加的排班表'")
	private String type;
	
	@Column(nullable = true, length = 255, columnDefinition = "varchar(32) COMMENT '适用班级, 适用学生,适用员工,多个按照逗号(,)分割'")
	private String usedFor;
	
	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '星期一 考勤设置'")
	private String mondaySet;
	
	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '星期二考勤设置'")
	private String tuesdaySet;
	
	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '星期三考勤设置'")
	private String wednesdaySet;
	
	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '星期四考勤设置'")
	private String thursdaySet;
	
	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '星期五考勤设置'")
	private String fridaySet;
	
	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '星期六考勤设置'")
	private String saturdaySet;
	
	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '星期日考勤设置'")
	private String sundaySet;
	
	@Column(nullable = false,  length = 16, columnDefinition = "varchar(32) COMMENT '迟到规则,指打卡时间超过上课时间多少分钟'")
	private Long lated;
	
	@Column(nullable = false,  length = 16, columnDefinition = "varchar(32) COMMENT '早退规则,指打卡时间早于上课/下班时间多少分钟'")
	private Long leaveEarly;
	
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
