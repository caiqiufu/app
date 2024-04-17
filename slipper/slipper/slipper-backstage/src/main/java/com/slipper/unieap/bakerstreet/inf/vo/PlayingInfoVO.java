package com.slipper.unieap.bakerstreet.inf.vo;

import java.io.Serializable;
import java.util.List;

import com.slipper.unieap.bakerstreet.scriptmgt.vo.ChapterInfoVO;
import com.slipper.unieap.bakerstreet.scriptmgt.vo.ClueDetailVO;

import lombok.Data;

@Data
public class PlayingInfoVO implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private String ID;
	private String name;
	private String avatarUrl;

	private String roleId;
	private String roleName;
	private String roleAvatarUrl;

	private List<ChapterInfoVO> assignedChapterList;
	private List<ChapterInfoVO> unassignedChapterList;
	private List<ChapterInfoVO> allChapterList;

	private List<ClueDetailVO> assignedClueList;
	private List<ClueDetailVO> unassignedClueList;
	private List<ClueDetailVO> allClueList;

}
