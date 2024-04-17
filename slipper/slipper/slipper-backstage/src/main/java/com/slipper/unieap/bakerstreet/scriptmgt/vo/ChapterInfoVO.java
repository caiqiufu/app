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
public class ChapterInfoVO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long scriptId;
	private String scriptName;
	private Long roleId;
	private String roleName;
	private String chapterNumber;
	private String chapterNumberDesc;
	private String code;
	private String name;
	private String brief;
	private String file;
	private List<PictureInfo> fileList;
	private String activateFlag;
	private Date createDate;
	private Date modifyDate;

	public String getChapterNumberDesc() {
		this.chapterNumberDesc = UnieapConstants.getMessage("bakerstreet.scriptmgt.chapterNumberDesc",
				new String[] { chapterNumber });
		return chapterNumberDesc;
	}
}
