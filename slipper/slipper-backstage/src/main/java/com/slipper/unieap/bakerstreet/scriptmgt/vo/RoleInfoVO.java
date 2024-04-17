package com.slipper.unieap.bakerstreet.scriptmgt.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.bakerstreet.scriptmgt.pojo.PictureInfo;
import com.slipper.unieap.exttools.JpaConvert;

import lombok.Data;

@Data
@JpaConvert
public class RoleInfoVO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long scriptId;
	private String code;
	private String name;
	private String sex;
	private String brief;
	private String avatarUrl;
	private String file;
	private List<PictureInfo> fileList;
	private List<ChapterInfoVO> chapterList;

	private String sexDesc;

	public String getSexDesc() {
		this.sexDesc = UnieapConstants.getDicData("GENDER", sex).getDicName();
		return sexDesc;
	}

	private String activateFlag;
	private Date createDate;
	private Date modifyDate;
}
