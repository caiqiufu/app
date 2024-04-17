package com.slipper.unieap.projectoa.constructionmgt.pojo;

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
@Table(name = "projectoa_supervision_log")
public class SupervisionLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false, length = 20, columnDefinition = "BIGINT(20) COMMENT 'ID'")
	private Long id;
	
	@Column(nullable = false, columnDefinition = "DATETIME COMMENT '日志时间'")
	private Date logTime;

	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '项目名称'")
	private String projectName;
	
	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '最高温度'")
	private String tempMax;
	
	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '最低温度'")
	private String tempMin;
	
	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '天气上午'")
	private String weatherMorning;
	
	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '天气下午'")
	private String weatherAfternoon;
	
	@Column(nullable = true, length = 512, columnDefinition = "varchar(512) COMMENT '施工单位'")
	private String sgdw;
		
	@Column(nullable = true, length = 1024, columnDefinition = "varchar(1024) COMMENT '施工部位'")
	private String sgbw;
	
	@Column(nullable = true, length = 1024, columnDefinition = "varchar(1024) COMMENT '施工内容'")
	private String sglr;
	
	@Column(nullable = true, length = 1024, columnDefinition = "varchar(1024) COMMENT '施工情况'")
	private String sgqk;
	
	@Column(nullable = true, length = 1024, columnDefinition = "varchar(1024) COMMENT '巡检记录'")
	private String xjjl;
	
	@Column(nullable = true, length = 512, columnDefinition = "varchar(512) COMMENT '旁站项目及旁站记录情况'")
	private String pzjj;
	
	@Column(nullable = true, length = 512, columnDefinition = "varchar(512) COMMENT '见证项目及见证记录情况'")
	private String jzjl;
	
	@Column(nullable = true, length = 512, columnDefinition = "varchar(512) COMMENT '隐蔽工程验收情况'")
	private String ybgcys;
	
	@Column(nullable = true, length = 512, columnDefinition = "varchar(512) COMMENT '分项工程检验批验收情况'")
	private String fxgcjy;
		
	@Column(nullable = true, length = 512, columnDefinition = "varchar(512) COMMENT '工程（例会）会议情况'")
	private String gclh;
		
	@Column(nullable = true, length = 1024, columnDefinition = "varchar(1024) COMMENT '施工安全问题及处理后落实情况'")
	private String aqls;
	
	@Column(nullable = true, length = 1024, columnDefinition = "varchar(1024) COMMENT '监理书面指令、执行及回复情况'")
	private String jlzlzx;
	
	@Column(nullable = true, length = 1024, columnDefinition = "varchar(1024) COMMENT '建设单位口头指示及处理意见'")
	private String ktzscl;
	
	@Column(nullable = true, length = 1024, columnDefinition = "varchar(1024) COMMENT '施工单位汇报事项及处理意见'")
	private String hbsxcl;
	
	@Column(nullable = true, length = 1024, columnDefinition = "varchar(1024) COMMENT '其他事项'")
	private String qtsx;
	
	@Column(nullable = true, length = 1024, columnDefinition = "varchar(1024) COMMENT '总监理工程师意见'")
	private String zjlyj;
	
	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '总监理工程师'")
	private String zjlName;
	
	@Column(nullable = true, columnDefinition = "DATETIME COMMENT '总监理工程师意见时间'")
	private Date zjlDate;
	
	
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
