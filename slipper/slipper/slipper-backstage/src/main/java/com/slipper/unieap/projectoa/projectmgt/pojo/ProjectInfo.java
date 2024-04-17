package com.slipper.unieap.projectoa.projectmgt.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.slipper.unieap.UnieapConstants;
import com.slipper.common.validator.group.Create;
import com.slipper.common.validator.group.Update;

import lombok.Data;

@Data
@Entity // name默认是此实体类的名字，全局唯一
@EntityListeners(AuditingEntityListener.class)
@Table(name = "projectoa_project_info", uniqueConstraints = {
		@UniqueConstraint(name = "unique_code", columnNames = "code"),
		@UniqueConstraint(name = "unique_name", columnNames = "name") })
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProjectInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false, length = 20, columnDefinition = "BIGINT(20) COMMENT 'ID'")
	private Long id;

	@NotEmpty(message = "{unieap.comm.notEmpty,'项目编码'}", groups = { Create.class, Update.class })
	@Column(unique = true, nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '项目编码'")
	private String code;

	@NotEmpty(message = "{unieap.comm.notEmpty,'项目名称'}", groups = { Create.class, Update.class })
	@Column(unique = true, nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '项目名称'")
	private String name;

	@Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '项目类型,PROJECT_TYPE'")
	private String type;

	@Column(nullable = true, length = 20, columnDefinition = "BIGINT(20) COMMENT '父项目ID'")
	private Long parentId;

	@Column(nullable = true, length = 20, columnDefinition = "BIGINT(20) COMMENT '中标⾦额'")
	private Double bidAmount;

	@Column(nullable = false, columnDefinition = "DATETIME COMMENT '中标时间'")
	private Date bidDate;

	@Column(nullable = true, length = 512, columnDefinition = "varchar(512) COMMENT '建设单位'")
	private String jsdw;

	@Column(nullable = true, length = 512, columnDefinition = "varchar(512) COMMENT '施工单位'")
	private String sgdw;

	@Column(nullable = true, columnDefinition = "varchar(512) COMMENT '计划开工时间'")
	private Date planstartDate;

	@Column(nullable = true, columnDefinition = "varchar(512) COMMENT '计划结束时间'")
	private Date planfinishDate;

	@Column(nullable = true, columnDefinition = "varchar(512) COMMENT '工期'")
	private String duration;

	@OneToMany(targetEntity = ProjectAttribute.class, orphanRemoval = true)
	@JoinColumn(name = "projectId", referencedColumnName = "id")
	private List<ProjectAttribute> projectAttributeList;

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
