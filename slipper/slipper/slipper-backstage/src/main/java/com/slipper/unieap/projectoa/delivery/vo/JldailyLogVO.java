package com.slipper.unieap.projectoa.delivery.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import com.alibaba.druid.util.StringUtils;
import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.ApplicationContextProvider;
import com.slipper.unieap.exttools.JpaConvert;
import com.slipper.unieap.projectoa.delivery.pojo.DeliveryBuildingpart;
import com.slipper.unieap.projectoa.projectmgt.bo.ProjectInfoBO;

import lombok.Data;

@Data
@JpaConvert
public class JldailyLogVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private Long parentId;

	private String parentName;
	
	private Long projectId;

	private String projectName;

	private Date logTime;

	private String status;
	@Transient
	private String statusDesc;

	public String getStatusDesc() {
		this.statusDesc = UnieapConstants.getDicData("JLLOG_STATUS", status).getDicName();
		return statusDesc;
	}

	private Date dateStart;

	private Date dateEnd;

	private String tempMax;

	private String tempMin;

	private String weatherMorning;

	private String weatherAfternoon;

	private String sgdw;

	private List<DeliveryBuildingpart> deliveryBuildingpartList;

	private String xjjl;

	private String pzjl;

	private String jzjl;

	private String ybgcys;

	private String fxgcjy;

	private String gclh;

	public String getGclh() {
		if (StringUtils.isEmpty(gclh)) {
			ProjectInfoBO projectInfoBO = (ProjectInfoBO) ApplicationContextProvider.getBean("projectInfoBO");
			this.gclh = projectInfoBO.getFieldDefaultValue(projectId, "gclh", null);
		}
		return gclh;
	}

	private String aqls;

	private String jlzlzx;

	private String ktzscl;

	/*
	 * public String getKtzscl() { if (StringUtils.isEmpty(ktzscl)) { ProjectInfoBO
	 * projectInfoBO = (ProjectInfoBO)
	 * ApplicationContextProvider.getBean("projectInfoBO"); this.ktzscl =
	 * projectInfoBO.getFieldDefaultValue(projectId, "ktzscl", new Object[] { "张"
	 * }); } return ktzscl; }
	 */

	private String hbsxcl;

	public String getHbsxcl() {
		if (StringUtils.isEmpty(hbsxcl)) {
			ProjectInfoBO projectInfoBO = (ProjectInfoBO) ApplicationContextProvider.getBean("projectInfoBO");
			this.hbsxcl = projectInfoBO.getFieldDefaultValue(projectId, "hbsxcl", null);
		}
		return hbsxcl;
	}

	private String qtsx;

	private String zjlyj;

	private String zjlName;
	
	//微信授权ID,作为提交者唯一ID
	private String wxauId;

	private Date zjlDate;

	private Date createDate;

	private String createBy;

	private Date modifyDate;

	private String modifyBy;

	private Long tenantId;

	private String remark;
}
