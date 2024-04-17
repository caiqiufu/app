package com.slipper.unieap.bakerstreet.playmgt.pojo;

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
@Table(name = "bakerstreet_play_log")
public class PlayLog implements Serializable {

	/**
	 * 玩家日志
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false, length = 16, columnDefinition = "BIGINT(16) COMMENT 'ID'")
	private Long id;
	
	@Column(nullable = true, length = 16, columnDefinition = "BIGINT(16) COMMENT '玩家ID'")
	private Long playerId;
	
	@Column(nullable = true, length = 16, columnDefinition = "BIGINT(16) COMMENT '房间ID'")
	private Long roomId;
	
	@Column(nullable = true, length = 16, columnDefinition = "BIGINT(16) COMMENT '剧本ID'")
	private Long scriptId;
	
	@Column(nullable = true, length = 16, columnDefinition = "BIGINT(16) COMMENT '角色ID'")
	private Long roleId;
	
	@Column(nullable = true, length = 512, columnDefinition = "varchar(512) COMMENT '本次获得的剧本线索，多选逗号分割，DIC名称为：SCRIPT_CLUE'")
	private String clueList;
	
	
	@Column(nullable = true, columnDefinition = "DATETIME COMMENT '开本时间'")
	private Date startDate;
	
	@Column(nullable = true, columnDefinition = "DATETIME COMMENT '结束时间'")
	private Date endDate;
		
	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '开本密码'")
	private String password;


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
