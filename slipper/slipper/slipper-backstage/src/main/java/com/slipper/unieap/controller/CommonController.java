package com.slipper.unieap.controller;

import java.beans.PropertyEditor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slipper.common.utils.R;
import com.slipper.modules.AbstractController;
import com.slipper.service.annotation.Log;
import com.slipper.unieap.ApplicationContextProvider;
import com.slipper.unieap.LoadSystemData;
import com.slipper.unieap.bo.BaseBO;
import com.slipper.unieap.exttools.DateEditor;
import com.slipper.unieap.exttools.MappingRequestParam;
import com.slipper.unieap.vo.PaginationSupport;

@RestController
@RequestMapping("/backstage/common")
public class CommonController extends AbstractController {

	@Autowired
	BaseBO baseBO;

	@Autowired
	private HttpServletRequest request;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Date.class, (PropertyEditor) new DateEditor());
	}

	/**
	 * 获取字典值,返回默认语言的字典值
	 * 
	 * @param groupCode
	 * @param params
	 * @return
	 */
	@RequestMapping("/getDicData")
	public R getDicData(String groupCode, String params) {
		return R.success(baseBO.getDicGroup(groupCode).getDataList());
	}

	/**
	 * 获取字典值,返回HTTP中指定语言的字典值
	 * 
	 * @param groupCode
	 * @param params
	 * @return
	 */
	@RequestMapping("/getLanDicData")
	public R getLanDicData(String groupCode, String params) {
		LocaleContextHolder.setLocale(request.getLocale());
		return R.success(baseBO.getDicGroup(groupCode).getLanguageList().get(request.getLocale().toString()));
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
	 * 获取数据库序列号
	 * 
	 * @param groupCode
	 * @param params
	 * @return
	 */
	@RequestMapping("/refershSysConfigData")
	public R refershSysConfigData() {
		LoadSystemData loadSystemData = (LoadSystemData) ApplicationContextProvider.getBean("loadSystemData");
		loadSystemData.loadSystemConfigureData();
		return R.success();
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
	@Log("查看信息")
	@RequestMapping("/commPage")
	public R commPage(PaginationSupport page, @MappingRequestParam Object mappingObj, String repositoryName,
			String boName, String beanName) throws Exception {
		if (StringUtils.isNotEmpty(boName)) {
			Object bo = (Object) ApplicationContextProvider.getBean(boName);
			@SuppressWarnings("rawtypes")
			Class[] params = new Class[] { page.getClass(), mappingObj.getClass() };
			Method getGridList = ReflectionUtils.findMethod(bo.getClass(), "getGridList", params);
			page = (PaginationSupport) getGridList.invoke(bo, page, mappingObj);
			return R.success(page.getMap());
		} else {
			if (StringUtils.isEmpty(repositoryName)) {
				repositoryName = getRepositoryNameByBeanName(beanName);
			}
			return baseBO.getGridList(page, repositoryName, mappingObj);
		}
	}

	@RequestMapping("/commInfo")
	public R commInfo(String repositoryName, String boName, String beanName, Long id) throws Exception {
		if (StringUtils.isNotEmpty(boName)) {
			Object bo = (Object) ApplicationContextProvider.getBean(boName);
			Method getInfo = ReflectionUtils.findMethod(bo.getClass(), "getInfo", Long.class);
			return (R) getInfo.invoke(bo, id);
		} else {
			if (StringUtils.isEmpty(repositoryName)) {
				repositoryName = getRepositoryNameByBeanName(beanName);
			}
			return baseBO.getInfo(repositoryName, id);
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
	@Log("操作")
	@RequestMapping("/commDeal")
	public R commDeal(String operType, String repositoryName, String boName, String beanName,
			@MappingRequestParam Object mappingObj, String keys) throws Exception {
		List<Long> lkeys = convertKeys(keys);
		if (StringUtils.isEmpty(repositoryName)) {
			repositoryName = getRepositoryNameByBeanName(beanName);
		}
		if (StringUtils.isNotEmpty(boName)) {
			Object bo = (Object) ApplicationContextProvider.getBean(boName);
			@SuppressWarnings("rawtypes")
			Class[] params = new Class[] { String.class, String.class, Object.class, List.class };
			Method deal = ReflectionUtils.findMethod(bo.getClass(), "deal", params);
			Object result = deal.invoke(bo, repositoryName, operType, mappingObj, lkeys);
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

	/**
	 * 根據BeanName獲取對應的RepositoryName，需要符合规范的命名
	 * 
	 * @param beanName
	 * @return
	 */
	public String getRepositoryNameByBeanName(String beanName) {
		if (StringUtils.isNotEmpty(beanName)) {
			String[] datas = StringUtils.split(beanName, "\\.");
			return lowerFirst(datas[datas.length - 1]) + "Repository";
		} else {
			return null;
		}
	}

	/**
	 * 将字符串的首字母转小写
	 * 
	 * @param str 需要转换的字符串
	 * @return
	 */
	private static String lowerFirst(String str) {
		// 同理
		char[] cs = str.toCharArray();
		cs[0] += 32;
		return String.valueOf(cs);
	}
}
