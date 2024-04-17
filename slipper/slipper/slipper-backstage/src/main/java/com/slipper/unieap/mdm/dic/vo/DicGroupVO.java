package com.slipper.unieap.mdm.dic.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.slipper.unieap.UnieapConstants;

import lombok.Data;

@Data
public class DicGroupVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String groupCode;
	private String groupName;
	private String activateFlag;
	private String activateFlagDesc;
	//默认字典值列表
	private List<DicDataVO> dataList = new ArrayList<DicDataVO>();
	//默认字典值列表
	private Map<String, DicDataVO> dataMap = new HashMap<String, DicDataVO>();
	//多语言字典值列表
	private Map<String, Map<String, DicDataVO>> languageMap = new HashMap<String, Map<String, DicDataVO>>();
	//多语言字典值列表
	private Map<String, List<DicDataVO>> languageList = new HashMap<String, List<DicDataVO>>();

	public String getActivateFlagDesc() {
		if (!StringUtils.isEmpty(this.activateFlag)) {
			this.activateFlagDesc = UnieapConstants.getDicData("ACTIVATE_FLAG", activateFlag).getDicName();
		}
		return activateFlagDesc;
	}
}
