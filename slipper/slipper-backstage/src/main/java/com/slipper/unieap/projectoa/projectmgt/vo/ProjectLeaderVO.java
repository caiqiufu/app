package com.slipper.unieap.projectoa.projectmgt.vo;

import java.io.Serializable;

import com.slipper.unieap.exttools.JpaConvert;
import com.slipper.unieap.projectoa.projectmgt.pojo.ProjectLeader;

import lombok.Data;

@Data
@JpaConvert
public class ProjectLeaderVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long projectId;
	private ProjectLeader jsdw;
	private ProjectLeader kcdw;
	private ProjectLeader sjdw;
	private ProjectLeader jldw;
	private ProjectLeader sgdw;
}
