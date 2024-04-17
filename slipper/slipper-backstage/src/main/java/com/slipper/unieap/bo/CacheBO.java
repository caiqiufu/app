package com.slipper.unieap.bo;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.slipper.unieap.ApplicationContextProvider;
import com.slipper.unieap.mdm.dic.pojo.UnieapDicData;
import com.slipper.unieap.mdm.dic.pojo.UnieapDicGroup;
import com.slipper.unieap.mdm.dic.repository.UnieapDicDataRepository;
import com.slipper.unieap.mdm.dic.repository.UnieapDicGroupRepository;
import com.slipper.unieap.mdm.dic.vo.DicDataVO;
import com.slipper.unieap.mdm.dic.vo.DicGroupVO;

@Service
public class CacheBO {

	@Autowired
	public CacheManager cacheManager;

	public void refreshCache() {
		Collection<String> names = cacheManager.getCacheNames();
		for (String name : names) {
			cacheManager.getCache(name).clear();
		}
	}

	public void refreshCache(String cacheName) {
		cacheManager.getCache(cacheName).clear();
	}

	@Cacheable(cacheNames = { "bean_props_cache" }, key = "#bean")
	public Map<String, PropertyDescriptor> getBeanprops(String beanName) throws Exception {
		Map<String, PropertyDescriptor> beanprops = new HashMap<String, PropertyDescriptor>();
		BeanInfo poInfo = Introspector.getBeanInfo(Class.forName(beanName));
		PropertyDescriptor[] props = poInfo.getPropertyDescriptors();
		String methodName, desc;
		for (int i = 0; i < props.length; i++) {
			if (!"class".equals(props[i].getName())) {
				Method getter = props[i].getReadMethod();
				if (getter != null) {
					methodName = getter.getName();
					if (methodName.length() > 4) {
						desc = methodName.substring(methodName.length() - 4);
						if (!"Desc".equals(desc)) {
							beanprops.put(props[i].getName(), props[i]);
						}
					} else {
						beanprops.put(props[i].getName(), props[i]);
					}
				}
			}
		}
		return beanprops;
	}

	@Value("${unieap.default-language}")
	String language;

	/**
	 * 缓存字典值，默认采用groupCode 作为key
	 * 
	 * @param groupCode
	 * @return
	 */
	@Cacheable(cacheNames = { "dic_group_cache" }, key = "#groupCode")
	public DicGroupVO getDicGroup(String groupCode) {
		UnieapDicGroupRepository unieapDicGroupRepository = (UnieapDicGroupRepository) ApplicationContextProvider
				.getBean("unieapDicGroupRepository");
		UnieapDicDataRepository unieapDicDataRepository = (UnieapDicDataRepository) ApplicationContextProvider
				.getBean("unieapDicDataRepository");
		DicGroupVO groupvo = new DicGroupVO();
		UnieapDicGroup dicGroup = unieapDicGroupRepository.findByGroupCode(groupCode);
		if (dicGroup != null) {
			// 获取多语言字典值数据
			List<UnieapDicData> datas = unieapDicDataRepository.findByGroupId(dicGroup.getId());
			groupvo.setActivateFlag(dicGroup.getActivateFlag());
			groupvo.setGroupCode(dicGroup.getGroupCode());
			groupvo.setGroupName(dicGroup.getGroupName());
			for (UnieapDicData data : datas) {
				if (StringUtils.equals(data.getLanguage(), language)) {
					DicDataVO datavo = new DicDataVO();
					datavo.setActivateFlag(data.getActivateFlag());
					datavo.setDicCode(data.getDicCode());
					datavo.setDicName(data.getDicName());
					datavo.setGroupCode(groupvo.getGroupCode());
					datavo.setGroupName(groupvo.getGroupName());
					datavo.setLanguage(data.getLanguage());
					datavo.setSort(data.getSort());
					datavo.setAttr1(data.getAttr1());
					datavo.setAttr2(data.getAttr2());
					datavo.setAttr3(data.getAttr3());
					datavo.setAttr4(data.getAttr4());
					datavo.setAttr5(data.getAttr5());
					datavo.setTenantId(data.getTenantId());
					groupvo.getDataList().add(datavo);
				}
				Map<String, DicDataVO> languageDicMap = groupvo.getLanguageMap().get(data.getLanguage());
				if (languageDicMap == null) {
					languageDicMap = new HashMap<String, DicDataVO>();
					groupvo.getLanguageMap().put(data.getLanguage(), languageDicMap);
				}
				List<DicDataVO> languageList = groupvo.getLanguageList().get(data.getLanguage());
				if (languageList == null) {
					languageList = new ArrayList<DicDataVO>();
					groupvo.getLanguageList().put(data.getLanguage(), languageList);
				}
				DicDataVO datavo = new DicDataVO();
				datavo.setActivateFlag(data.getActivateFlag());
				datavo.setDicCode(data.getDicCode());
				datavo.setDicName(data.getDicName());
				datavo.setGroupCode(groupvo.getGroupCode());
				datavo.setGroupName(groupvo.getGroupName());
				datavo.setLanguage(data.getLanguage());
				datavo.setSort(data.getSort());
				datavo.setTenantId(data.getTenantId());
				languageList.add(datavo);
				languageDicMap.put(data.getDicCode(), datavo);
			}
		}
		return groupvo;
	}
}
