package com.slipper.unieap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson2.JSONObject;

import lombok.Data;

@Data
public class PaginationSupport implements Serializable {
	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;

	/**
	 * 公共参数，不能修改
	 */
	public String sort;
	public String dir;
	public int pageSize = 15;
	public int currentPage = 1;
	public int startIndex = 0;
	public List<?> items;
	public int totalCount = 0;
	public String dsName = null;
	/**
	 * sort ASC
	 */
	public String ASC = "ASC";
	/**
	 * sort DESC
	 */
	public String DESC = "DESC";
	/**
	 * 总记录数
	 */
	private String TOTAL = "total";
	/**
	 * 分页大小
	 */
	private String SIZE = "size";
	/**
	 * 当前页
	 */
	private String CURRENT = "current";
	/**
	 * 总页数
	 */
	private String PAGES = "pages";
	/**
	 * 数据
	 */
	private String DATA = "list";

	public JSONObject getJson() throws Exception {
		JSONObject data = new JSONObject();
		try {
			data.put(TOTAL, this.getTotalCount());
			data.put(SIZE, this.getPageSize());
			data.put(CURRENT, this.getCurrentPage());
			if (this.items != null) {
				data.put(DATA, this.items);
			} else {
				data.put(DATA, null);
			}

		} catch (Exception e) {
			throw new Exception("items data conver to json error:" + e.getMessage(), e);
		}
		return data;
	}

	public Map<String, Object> getMap() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			data.put(TOTAL, this.getTotalCount());
			data.put(SIZE, this.getPageSize());
			data.put(CURRENT, this.getCurrentPage());
			if (this.items != null) {
				data.put(DATA, this.items);
			} else {
				data.put(DATA, null);
			}

		} catch (Exception e) {
			throw new Exception("items data conver to json error:" + e.getMessage(), e);
		}
		return data;
	}

	public int getNextIndex() {
		int nextIndex = getStartIndex() + pageSize;
		if (nextIndex >= totalCount) {
			return getStartIndex();
		} else {
			return nextIndex;
		}
	}

	public int getPreviousIndex() {
		int previousIndex = getStartIndex() - pageSize;
		if (previousIndex < 0) {
			return 0;
		} else {
			return previousIndex;
		}
	}

	public String getSortStr() {
		return sort + " " + dir;
	}

	public void po2vo(Class<?> object) throws Exception {
		List<Object> vos = new ArrayList<Object>();
		for (Object r : this.items) {
			Object vo = object.newInstance();
			BeanUtils.copyProperties(r, vo);
			vos.add(vo);
		}
		this.items = vos;
	}
}
