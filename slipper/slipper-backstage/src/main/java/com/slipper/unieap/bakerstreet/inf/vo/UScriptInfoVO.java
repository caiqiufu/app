package com.slipper.unieap.bakerstreet.inf.vo;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.bakerstreet.playmgt.pojo.DmRoom;
import com.slipper.unieap.bakerstreet.playmgt.pojo.PlayerLike;

import lombok.Data;

@Data
public class UScriptInfoVO implements Serializable {
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

	// DM开本记录信息
	private DmRoom dmScriptInfo;

	// 玩家打本记录信息
	private DmRoom playerScriptInfo;

	// 剧本点赞
	private Map<String, Object> scriptLikeInfo;

	//玩家点赞列表
	private PlayerLike playerLikeInfo;

	// 点赞排行
	private Map<String, Object> likeListInfo;

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
