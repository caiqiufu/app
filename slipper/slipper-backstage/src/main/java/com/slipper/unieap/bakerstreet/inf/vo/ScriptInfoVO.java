package com.slipper.unieap.bakerstreet.inf.vo;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.slipper.unieap.UnieapConstants;

import lombok.Data;


@Data
public class ScriptInfoVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 剧本ID
	 */
	private Long scriptId;
	/**
	 * 剧本名称
	 */
	private String scriptName;
	/**
	 * 剧本海报
	 */
	private String posterUrl;
	/**
	 * 幕数
	 */
	private String scriptChapter;

	/**
	 * 类型
	 */
	private String typeList;

	/**
	 * 类别
	 */

	private String category;
	/**
	 * 时间
	 */

	private String duration;
	/**
	 * 人数
	 */

	private String playerCount;

	/**
	 * 是否可以开本
	 */
	private String playFlag;

	/**
	 * 点赞数
	 */
	private Long likeNum;

	// 游戏时间
	private Date playTime;

	private String typeListDesc;

	private String categoryDesc;

	private String durationDesc;

	private String playerCountDesc;

	private String scriptChapterDesc;

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

	public String getPlayerCountDesc() {
		this.playerCountDesc = UnieapConstants.getDicData("SCRIPT_PLAYERCOUNT", playerCount).getDicName();
		return playerCountDesc;
	}

	public String getScriptChapterDesc() {
		this.scriptChapterDesc = UnieapConstants.getDicData("SCRIPT_CHAPTER", scriptChapter).getDicName();
		return scriptChapterDesc;
	}
}
