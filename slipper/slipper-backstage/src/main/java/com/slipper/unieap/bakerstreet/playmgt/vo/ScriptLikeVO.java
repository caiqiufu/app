package com.slipper.unieap.bakerstreet.playmgt.vo;

import java.io.Serializable;

import com.slipper.unieap.exttools.JpaConvert;

import lombok.Data;

@Data
@JpaConvert
public class ScriptLikeVO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long scriptId;
	private String scriptName;
	private Long likeCount;
}
