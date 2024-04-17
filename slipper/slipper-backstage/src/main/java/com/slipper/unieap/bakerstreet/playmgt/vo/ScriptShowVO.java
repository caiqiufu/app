package com.slipper.unieap.bakerstreet.playmgt.vo;

import java.io.Serializable;

import com.slipper.unieap.exttools.JpaConvert;

import lombok.Data;

@Data
@JpaConvert
public class ScriptShowVO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private Long id;
	private String seq;
	private Long scriptId;
	private String scriptName;
	private String posterUrl;
	private String brief;
}
