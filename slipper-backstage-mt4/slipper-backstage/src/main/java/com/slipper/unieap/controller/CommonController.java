package com.slipper.unieap.controller;

import java.beans.PropertyEditor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slipper.unieap.ApplicationContextProvider;
import com.slipper.unieap.bo.BaseBO;
import com.slipper.unieap.exttools.DateEditor;
import com.slipper.unieap.exttools.MappingRequestParam;
import com.slipper.unieap.utils.R;
import com.slipper.unieap.vo.PaginationSupport;

@RestController
@RequestMapping("/backstage/common")
public class CommonController {

	@Autowired
	BaseBO baseBO;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Date.class, (PropertyEditor) new DateEditor());
	}

	@RequestMapping("/getDicData")
	public R getDicData(String groupCode, String params, HttpServletRequest request, HttpServletResponse response) {
		return R.success(baseBO.getDicGroup(groupCode).getDataList());
	}

	/**
	 * 获取数据库序列号
	 * 
	 * @param groupCode
	 * @param params
	 * @return
	 */
	@RequestMapping("/getDBSeq")
	public R getDBSeq(String serialName) {
		return R.success(baseBO.getDBSeq(serialName));
	}

	/**
	 * 
	 * @param page
	 * @param mappingObj
	 * @param repositoryName
	 * @param boName
	 * @param beanName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/commPage")
	public R commPage(PaginationSupport page, @MappingRequestParam Object mappingObj, String repositoryName,
			String boName, String beanName) throws Exception {
		if (StringUtils.isNotEmpty(boName)) {
			Object bo = (Object) ApplicationContextProvider.getBean(boName);
			Method getGridList = bo.getClass().getDeclaredMethod("getGridList", Object.class, Object.class);
			return (R) getGridList.invoke(bo, page, mappingObj);
		} else {
			return baseBO.getGridList(page, repositoryName, mappingObj);
		}
	}

	@RequestMapping("/commInfo")
	public R commInfo(PaginationSupport page, @MappingRequestParam Object mappingObj, String repositoryName,
			String boName, String beanName) throws Exception {
		if (StringUtils.isNotEmpty(boName)) {
			Object bo = (Object) ApplicationContextProvider.getBean(boName);
			Method getInfo = bo.getClass().getDeclaredMethod("getInfo", Object.class, Object.class);
			return (R) getInfo.invoke(bo, page, mappingObj);
		} else {
			return baseBO.getInfo(page, repositoryName, mappingObj);
		}
	}

	/**
	 * 
	 * @param operType
	 * @param repositoryName
	 * @param boName
	 * @param beanName
	 * @param mappingObj
	 * @param keys
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/commDeal")
	public R commDeal(String operType, String repositoryName, String boName, String beanName,
			@MappingRequestParam Object mappingObj, String keys) throws Exception {
		List<Long> lkeys = convertKeys(keys);
		if (StringUtils.isNotEmpty(boName)) {
			Object bo = (Object) ApplicationContextProvider.getBean(boName);
			Method deal = bo.getClass().getDeclaredMethod("deal", String.class, Object.class, String.class);
			Object result = deal.invoke(bo, operType, mappingObj, lkeys);
			return R.success(result);
		} else {
			return baseBO.commDeal(repositoryName, operType, mappingObj, lkeys);
		}
	}

	/**
	 * convert id String to List<Long>
	 * 
	 * @param keys
	 * @return
	 */
	public List<Long> convertKeys(String keys) {
		List<Long> lkeys = null;
		if (StringUtils.isNotEmpty(keys)) {
			if (StringUtils.contains(keys, ",")) {
				String[] idsNoBlankArray = keys.split(",");
				Long[] convert = (Long[]) ConvertUtils.convert(idsNoBlankArray, Long.class);
				// 然后转换成为 list
				lkeys = Arrays.asList(convert);
			} else {
				lkeys = new ArrayList<Long>();
				lkeys.add(Long.decode(keys));
			}
		}
		return lkeys;
	}
}
