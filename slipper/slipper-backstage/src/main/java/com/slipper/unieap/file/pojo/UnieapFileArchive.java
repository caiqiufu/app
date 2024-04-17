package com.slipper.unieap.file.pojo;

import java.io.Serializable;
import java.math.BigInteger;
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

/**
 * The persistent class for the mdm_file_archive database table.
 * 
 */

@Data
@Entity // 生成相应的表
@EntityListeners(AuditingEntityListener.class)
@Table(name = "unieap_file_archive")
public class UnieapFileArchive implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false, length = 16, columnDefinition = "BIGINT(20) COMMENT 'ID'")
	private Long id;

	@Temporal(TemporalType.DATE)
	@Column(nullable = false, columnDefinition = "DATETIME COMMENT '文件归档日期'")
	private Date archiveDate;

	@Column(nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '外部关联ID'")
	private String extKey;

	@Column(nullable = true, length = 255, columnDefinition = "varchar(255) COMMENT '文件目录名称'")
	private String category;

	@Column(nullable = false, length = 255, columnDefinition = "varchar(255) COMMENT '原始文件名称'")
	private String originalName;

	@Column(nullable = false, length = 255, columnDefinition = "varchar(255) COMMENT '实际文件名称'")
	private String actualName;

	@Column(nullable = false, length = 1024, columnDefinition = "varchar(1024) COMMENT '文件路径'")
	private String path;

	@Column(nullable = false, length = 1024, columnDefinition = "varchar(1024) COMMENT '文件访问地址'")
	private String url;

	@Column(nullable = false, length = 32, columnDefinition = "BIGINT(32) COMMENT '文件字节大小'")
	private BigInteger size;

	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '文件类型'")
	private String type;

	@Column(nullable = true, length = 255, columnDefinition = "varchar(255) COMMENT '处理类名称'")
	private String handlerName;

	@Column(nullable = true, length = 512, columnDefinition = "varchar(512) COMMENT '处理参数'")
	private String param;

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