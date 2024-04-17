package com.slipper.unieap.bakerstreet.playmgt.vo;

import java.io.Serializable;

import com.slipper.unieap.exttools.JpaConvert;

import lombok.Data;

@Data
@JpaConvert
public class DmShowVO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private Long id;
	private String seq;
	private Long playerId;
	private String playerCode;
	private String playerName;
	private String avatarUrl;
	private String brief;
}
