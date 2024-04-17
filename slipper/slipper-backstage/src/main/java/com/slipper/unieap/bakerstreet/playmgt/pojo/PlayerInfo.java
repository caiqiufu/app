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
import javax.persistence.UniqueConstraint;

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
@Table(name = "bakerstreet_player_info", uniqueConstraints = {
		@UniqueConstraint(name = "unique_code", columnNames = "code"),
		@UniqueConstraint(name = "unique_wxauId", columnNames = "wxauId") })
public class PlayerInfo implements Serializable {

	/**
	 * 玩家信息
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false, length = 16, columnDefinition = "BIGINT(20) COMMENT 'ID'")
	private Long id;

	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '玩家ID'")
	private String code;

	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '玩家名称'")
	private String name;

	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '微信唯一标识'")
	private String wxauId;

	@Column(nullable = true, length = 512, columnDefinition = "varchar(512) COMMENT '头像'")
	private String avatarUrl;

	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '微信号'")
	private String weixin;

	@Column(nullable = true, columnDefinition = "DATETIME COMMENT '注册时间'")
	private Date registerDate;

	@Column(nullable = true, length = 16, columnDefinition = "BIGINT(20) COMMENT '打本数量'")
	private Long playCount;

	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT 'PLAYER_TYPE 玩家类别:1:普通玩家,2:DM'")
	private String playerType;

	@Column(nullable = true, length = 16, columnDefinition = "BIGINT(20) COMMENT '开本数量'")
	private Long scriptCount;

	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '开本权限:Y:可以开本,N:不能开本'")
	private String scriptFlag;

	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '是否可以转为玩家:Y:可以,N:不可用'")
	private String changeFlag;

	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT 'PLAYER_STATUS 玩家状态,1:未激活,2:已激活,3:已注销'")
	private String status;

	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '临时激活码,成为DM时需要校验'")
	private String activeCode;

	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '是否拉黑:Y:已拉黑,N:正常'")
	private String blacklistFlag;

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
