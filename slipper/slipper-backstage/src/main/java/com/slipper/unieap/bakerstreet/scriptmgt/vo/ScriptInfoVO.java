package com.slipper.unieap.bakerstreet.scriptmgt.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.bakerstreet.scriptmgt.pojo.ClueInfo;
import com.slipper.unieap.bakerstreet.scriptmgt.pojo.PictureInfo;
import com.slipper.unieap.exttools.JpaConvert;

import lombok.Data;

@Data
@JpaConvert
public class ScriptInfoVO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private Long id;
	private String code;
	private String name;
	private String posterUrl;
	private String poster;
	private String guidebook;
	private List<PictureInfo> posterList;
	private List<PictureInfo> guidebookList;
	private String typeList;
	private String typeListDesc;
	private String category;
	private String categoryDesc;
	private String duration;
	private String durationDesc;
	private String playerCount;
	private String playerCountDesc;
	private String scriptChapter;
	private String scriptChapterDesc;
	private String publishFlag;
	private String publishFlagDesc;
	private Date publishDate;
	private String brief;
	private Date createDate;
	private String activateFlag;
	private String activateFlagDesc;

	private String remark;

	private String clueList;
	private List<ClueInfo> clueInfoList = new ArrayList<ClueInfo>();

	private String clueDetailList;
	private List<ClueDetailVO> clueDetailInfoList = new ArrayList<ClueDetailVO>();

	public String getTypeListDesc() {
		String[] tt;
		if (StringUtils.isNotEmpty(typeList)) {
			if (StringUtils.contains(typeList, ",")) {
				tt = typeList.split(",");
			} else {
				tt = new String[] { typeList };
			}
			String ll = "";
			for (String type : tt) {
				ll = ll + UnieapConstants.getDicData("SCRIPT_TYPE", type).getDicName() + ",";
			}
			this.typeListDesc = StringUtils.substring(ll, 0, ll.length() - 1);
			return this.typeListDesc;
		} else {
			return typeListDesc;
		}
	}

	public String getCategoryDesc() {
		String[] tt;
		if (StringUtils.isNotEmpty(category)) {
			if (StringUtils.contains(category, ",")) {
				tt = category.split(",");
			} else {
				tt = new String[] { category };
			}
			String ll = "";
			for (String type : tt) {
				ll = ll + UnieapConstants.getDicData("SCRIPT_CATEGORY", type).getDicName() + ",";
			}
			this.categoryDesc = StringUtils.substring(ll, 0, ll.length() - 1);
			return this.categoryDesc;
		} else {
			return categoryDesc;
		}
	}

	public String getDurationDesc() {
		this.durationDesc = UnieapConstants.getDicData("SCRIPT_DURATION", duration).getDicName();
		return durationDesc;
	}

	public String getPublishFlagDesc() {
		this.publishFlagDesc = UnieapConstants.getDicData("SCRIPT_PUBLISHFLAG", publishFlag).getDicName();
		return publishFlagDesc;
	}

	public String getPlayerCountDesc() {
		this.playerCountDesc = UnieapConstants.getDicData("SCRIPT_PLAYERCOUNT", playerCount).getDicName();
		return playerCountDesc;
	}

	public String getScriptChapterDesc() {
		this.scriptChapterDesc = UnieapConstants.getDicData("SCRIPT_CHAPTER", scriptChapter).getDicName();
		return scriptChapterDesc;
	}

	public String getActivateFlagDesc() {
		this.activateFlagDesc = UnieapConstants.getDicData("ACTIVATE_FLAG", activateFlag).getDicName();
		return activateFlagDesc;
	}
}
