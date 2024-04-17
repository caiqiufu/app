package com.slipper.unieap.bakerstreet.scriptmgt.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.slipper.unieap.bakerstreet.scriptmgt.pojo.PictureInfo;
import com.slipper.unieap.exttools.JpaConvert;

import lombok.Data;

@Data
@JpaConvert
public class ClueDetailVO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private Long id;
	private String code;
	private String name;
	private Long clueId;
	private String clueName;
	private Long scriptId;
	private Date createDate;
	private String activateFlag;
	private String url;
	private String file;
	private List<PictureInfo> fileList;
}
