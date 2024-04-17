package com.slipper.unieap.vo;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.slipper.unieap.UnieapConstants;

import lombok.Data;

@Data
public class TreeVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String id;
	public String parentId;
	public String text;
	public String iconCls;
	public String qtip;
	public boolean leaf;
	public String leafFlag;
	public boolean expanded;
	public List<TreeVO> children;
	public Map<String, Object> extendAttri;
	public String attr1;
	public String attr2;
	public String attr3;
	public String attr4;
	public String attr5;
	public String attr6;
	public String attr7;
	public String attr8;
	public String attr9;
	public String attr10;

	public boolean checkBoxTree = false;
	public boolean checked;

	public void setLeafFlag(String leafFlag) {
		if (UnieapConstants.YES.equals(leafFlag)) {
			this.leaf = true;
		} else {
			this.leaf = false;
		}
		this.leafFlag = leafFlag;
	}

	public void putExtendAttri(Map<String, Object> exAttri, JSONObject jsonObj) throws JSONException {
		if (exAttri != null) {
			Iterator<String> iter = exAttri.keySet().iterator();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				Object val = exAttri.get(key);
				if (val == null) {
					jsonObj.put(key, null);
					continue;
				} else {
					jsonObj.put(key, val);
				}
			}

		}
	}

}
