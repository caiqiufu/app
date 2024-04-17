package com.slipper.unieap.bakerstreet.scriptmgt.pojo;

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
@Table(name = "bakerstreet_script_info")
public class ScriptInfo implements Serializable {

	/**
	 * 剧本基本信息
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false, length = 16, columnDefinition = "BIGINT(20) COMMENT 'ID'")
	private Long id;

	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '剧本ID'")
	private String code;

	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '剧本名称'")
	private String name;

	@Column(nullable = true, length = 512, columnDefinition = "varchar(512) COMMENT '剧本海报'")
	private String posterUrl;

	@Column(nullable = true, length = 512, columnDefinition = "varchar(512) COMMENT '剧本类型，多选，DIC名称为：SCRIPT_TYPE  情感、机制、内核'")
	private String typeList;

	@Column(nullable = true, length = 512, columnDefinition = "varchar(512) COMMENT '剧本类别，单选，DIC名称为：SCRIPT_CATEGORY  普通、原创、专属'")
	private String category;

	@Column(nullable = true, length = 512, columnDefinition = "varchar(512) COMMENT '时间，单选，DIC名称为：SCRIPT_DURATION'")
	private String duration;

	@Column(nullable = true, length = 512, columnDefinition = "varchar(512) COMMENT '人数，DIC名称为：SCRIPT_PLAYERCOUNT'")
	private String playerCount;

	@Column(nullable = true, length = 512, columnDefinition = "varchar(512) COMMENT '剧本线索，多选逗号分割，DIC名称为：SCRIPT_CLUE'")
	private String clueList;

	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '剧本幕数 SCRIPT_CHAPTER'")
	private String scriptChapter;

	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT ' SCRIPT_PUBLISHFLAG 上架状态:1:已上架,0:未上架'")
	private String publishFlag;

	@Column(nullable = true, columnDefinition = "DATETIME COMMENT '上架时间'")
	private Date publishDate;

	@Column(nullable = true, length = 512, columnDefinition = "varchar(512) COMMENT '剧本简介'")
	private String brief;
	
	@Transient
	public static String posterCategory = "poster_picture";
	@Transient
	public static String guidebookCategory = "guidebook_picture";

	// DM手册是多张有顺序的图片

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
