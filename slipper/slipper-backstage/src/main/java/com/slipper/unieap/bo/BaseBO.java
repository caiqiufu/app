package com.slipper.unieap.bo;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.slipper.common.utils.R;
import com.slipper.unieap.ApplicationContextProvider;
import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.db.DBManager;
import com.slipper.unieap.db.EntityRowMapper;
import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.mdm.dic.vo.DicDataVO;
import com.slipper.unieap.mdm.dic.vo.DicGroupVO;
import com.slipper.unieap.vo.PaginationSupport;
import com.slipper.unieap.vo.QueryDicVO;

/**
 * <p>
 * Description: [业务基类]
 * </p>
 * 
 * @author <a href="mailto: caiqiufu@sohu.com">蔡秋伏</a>
 * @version $Revision: 1.2 $
 */
@Service
public class BaseBO {
	/**
	 * <p>
	 * 描述:
	 * </p>
	 */
	public final Log logger = LogFactory.getLog(BaseBO.class);

	@Autowired
	public CacheBO cacheBO;

	/**
	 * @针对sql设计，只参与分页数据组装
	 * @param className
	 * @param querySql
	 * @param countSql
	 * @param parameters
	 * @param ps
	 * @param dsName
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public void getPaginationDataByMysql(Class<?> className, String querySql, String countSql, Object[] parameters,
			PaginationSupport page, String dsName) throws Exception {
		if (StringUtils.isNotEmpty(page.sort) && StringUtils.isNotEmpty(page.dir)) {
			querySql = querySql + " order by " + page.sort + " " + page.dir;
		}
		querySql = querySql + " limit " + page.getStartIndex() + "," + page.getPageSize();
		int totalCount = DBManager.getBizJT(dsName).queryForObject(countSql, parameters, Integer.class);
		page.setTotalCount(totalCount);
		List<Object> items;
		String classNameStr = className.getName();
		boolean convertFlag = false;
		if (StringUtils.equals("VO", classNameStr.substring(classNameStr.length() - 2))) {
			convertFlag = true;
		}
		if (parameters == null) {
			items = DBManager.getBizJT(dsName).query(querySql, new EntityRowMapper(className, convertFlag));
		} else {
			items = DBManager.getBizJT(dsName).query(querySql, parameters, new EntityRowMapper(className, convertFlag));
		}
		page.setItems(items);
	}

	/**
	 * @针对sql设计，只参与分页数据组装
	 * @param className
	 * @param querySql
	 * @param countSql
	 * @param parameters
	 * @param ps
	 * @throws Exception
	 */
	public void getPaginationDataByMysql(Class<?> className, String querySql, String countSql, Object[] parameters,
			PaginationSupport page) throws Exception {
		this.getPaginationDataByMysql(className, querySql, countSql, parameters, page, null);
	}

	public Pageable getPageable(PaginationSupport page) {
		Sort sort = null;
		Pageable pageable = null;
		if (StringUtils.isNotEmpty(page.getDir()) && StringUtils.isNotEmpty(page.getSort())) {
			if (page.ASC.equals(page.getDir().toUpperCase())) {
				sort = Sort.by(Direction.ASC, page.getSort());
			} else {
				sort = Sort.by(Sort.Direction.DESC, page.getSort());
			}
			pageable = PageRequest.of(page.getCurrentPage() - 1, page.getPageSize(), sort);
		} else {
			pageable = PageRequest.of(page.getCurrentPage() - 1, page.getPageSize());
		}
		return pageable;
	}

	public String getCurrentTime(String dsName) {
		return UnieapConstants.getCurrentTime();
	}

	public Date getDateTime(String dsName) {
		return UnieapConstants.getDateTime();
	}

	public void setCriteria(DetachedCriteria criteria, Object bean) throws Exception {
		if (bean != null) {
			Map<String, PropertyDescriptor> beanprops = cacheBO.getBeanprops(bean.getClass().getName());
			Iterator<String> iter = beanprops.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				PropertyDescriptor prop = beanprops.get(key);
				Method getter = prop.getReadMethod();
				Object value = getter.invoke(bean);
				if (value != null && StringUtils.isNotEmpty(value.toString()) && !StringUtils.equals("tenantId", key)) {
					Property pro = Property.forName(key);
					Class<?> retType = getter.getReturnType();
					if (String.class == retType) {
						criteria.add(pro.like(value.toString(), MatchMode.START));
					} else {
						criteria.add(pro.eq(value));
					}
				}
				// 所有包含TenantId字段的表，都需要该字段过滤
				if (beanprops.containsKey("tenantId")) {
					Property pro = Property.forName("tenantId");
					criteria.add(pro.eq(UnieapConstants.getTenantId()));
				}
			}
		}
	}

	public DicDataVO getDicData(String groupCode, String dicCode) {
		if (StringUtils.isEmpty(groupCode) || !cacheBO.getDicGroup(groupCode).getDataMap().containsKey(dicCode)) {
			DicDataVO vo = new DicDataVO();
			vo.setGroupCode(groupCode);
			vo.setDicCode(dicCode);
			vo.setDicName(dicCode);
			return vo;
		} else {
			return cacheBO.getDicGroup(groupCode).getDataMap().get(dicCode);
		}
	}

	public DicDataVO getDicData(String language, String groupCode, String dicCode) {
		if (StringUtils.isEmpty(language) || StringUtils.isEmpty(groupCode)
				|| !cacheBO.getDicGroup(groupCode).getLanguageMap().containsKey(language)) {
			DicDataVO vo = new DicDataVO();
			vo.setGroupCode(groupCode);
			vo.setDicCode(dicCode);
			vo.setDicName(dicCode);
			return vo;
		} else {
			Map<String, DicDataVO> dicDataList = cacheBO.getDicGroup(groupCode).getLanguageMap().get(language);
			if (!dicDataList.containsKey(dicCode)) {
				DicDataVO vo = new DicDataVO();
				vo.setGroupCode(groupCode);
				vo.setDicCode(dicCode);
				vo.setDicName(dicCode);
				return vo;
			} else {
				return dicDataList.get(dicCode);
			}
		}
	}

	public DicGroupVO getDicGroup(String groupCode) {
		if (StringUtils.isEmpty(groupCode) || cacheBO.getDicGroup(groupCode) == null) {
			return new DicGroupVO();
		} else {
			return cacheBO.getDicGroup(groupCode);
		}
	}

	public Long getDBSeq(String serialName) {
		return UnieapConstants.getSeq(serialName);
	}

	public DicGroupVO getDicGroupFromDB(QueryDicVO vo) {
		List<?> datas = DBManager.getBizJT(null).query(vo.getSql(), new EntityRowMapper(DicDataVO.class));
		DicGroupVO group = new DicGroupVO();
		String groupCode = vo.getGroupCode();
		group.setGroupCode(groupCode);
		group.setGroupName(vo.getGroupName());
		if (datas != null && datas.size() > 0) {
			for (Object data : datas) {
				DicDataVO dicData = (DicDataVO) data;
				group.getDataList().add(dicData);
				group.getDataMap().put(dicData.getDicCode(), dicData);
			}
		}
		return group;
	}

	/**
	 * 获取前端可显示格式数据
	 * 
	 * @param groupCode
	 * @param isOptional
	 * @return
	 * @throws Exception
	 */
	public String getCommDicList(String groupCode, String isOptional, String language) throws Exception {
		DicGroupVO group = cacheBO.getDicGroup(groupCode);
		JSONArray ja = new JSONArray();
		if (UnieapConstants.YES.equals(isOptional)) {
			JSONObject jac = new JSONObject();
			jac.put("dicCode", "");
			jac.put("dicName", UnieapConstants.getMessage("comm.combo.select", language, null));
			jac.put("parentCode", "");
			ja.add(jac);
		}
		List<DicDataVO> dataList = group.getDataList();
		for (DicDataVO data : dataList) {
			JSONObject jac = new JSONObject();
			jac.put("dicCode", data.getDicCode());
			jac.put("dicName", data.getDicName());
			jac.put("parentCode", data.getGroupCode());
			jac.put("attr1", data.getAttr1());
			jac.put("attr2", data.getAttr2());
			jac.put("attr3", data.getAttr3());
			jac.put("attr4", data.getAttr4());
			jac.put("attr5", data.getAttr5());
			ja.add(jac);
		}
		String dicString = ja.toString();
		return dicString;
	}

	public R getGridList(PaginationSupport page, String repositoryName, Object vo) throws Exception {
		UnieapRepository<?> repository = (UnieapRepository<?>) ApplicationContextProvider.getBean(repositoryName);
		repository.getPaginationDataByMysql(page, null, vo);
		return R.success(page.getMap());
	}

	public R getInfo(String repositoryName, Long id) throws Exception {
		UnieapRepository<?> repository = (UnieapRepository<?>) ApplicationContextProvider.getBean(repositoryName);
		Method getById = ReflectionUtils.findMethod(repository.getClass(), "getById", Object.class);
		Object data = getById.invoke(repository, id);
		return R.success(data);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public R commDeal(String repositoryName, String operType, Object vo, List<Long> ids) throws Exception {
		Object repository = ApplicationContextProvider.getBean(repositoryName);
		if (StringUtils.equals(operType, UnieapConstants.CREATE)) {
			return R.success(save(repository, vo));
		} else if (StringUtils.equals(operType, UnieapConstants.UPDATE)) {
			return R.success(update(repository, vo));
		} else if (StringUtils.equals(operType, UnieapConstants.DELETE)) {
			if (ids != null && ids.size() > 0) {
				deleteBatch(repository, ids);
			} else {
				Long id = this.getId(vo);
				delete(repository, id);
			}
			return R.success();
		} else if (StringUtils.equals(operType, UnieapConstants.MODIFYACTIVATEFLAG)) {
			if (ids != null && ids.size() > 0) {
				String activateFlag = getActivateFlag(vo);
				updateActivateFlagBatch(repository, ids, activateFlag);
			}
			return R.success();
		} else {
			throw new Exception(UnieapConstants.getMessage("comm.operation.error", null, new String[] { operType }));
		}
	}

	/**
	 * 自动设置Id
	 * 
	 * @param repository
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public Object save(Object repository, Object pojo) throws Exception {
		Method setId = pojo.getClass().getDeclaredMethod("setId", Long.class);
		Long id = UnieapConstants.getSeq(null);
		setId.invoke(pojo, id);
		if (isExistProperty(pojo, "code")) {
			Method setCode = pojo.getClass().getDeclaredMethod("setCode", String.class);
			Method getCode = getAllPubliAndInheritedMethod(repository, "getCode");
			String code = (String) getCode.invoke(repository);
			setCode.invoke(pojo, code);
		}
		Method getActivateFlag = pojo.getClass().getDeclaredMethod("getActivateFlag");
		String activateFlag = (String) getActivateFlag.invoke(pojo);
		if (StringUtils.isEmpty(activateFlag)) {
			activateFlag = UnieapConstants.YES;
		}
		// 设置默认值
		Method setActivateFlag = pojo.getClass().getDeclaredMethod("setActivateFlag", String.class);
		setActivateFlag.invoke(pojo, activateFlag);

		Method setTenantId = pojo.getClass().getDeclaredMethod("setTenantId", Long.class);
		Long tenantId = UnieapConstants.getTenantId();
		setTenantId.invoke(pojo, tenantId);

		Method save = getAllPubliAndInheritedMethod(repository, "save");
		save.invoke(repository, pojo);
		return pojo;
	}

	public Object update(Object repository, Object pojo) throws Exception {
		Long id = this.getId(pojo);
		Object oldPojo = this.getOneById(repository, id);
		copyPropertyValueToNewPojo(pojo, oldPojo);
		Method save = getAllPubliAndInheritedMethod(repository, "save");
		save.invoke(repository, pojo);
		return pojo;
	}

	public Object updateActivateFlag(Object repository, Long id, String activateFlag) throws Exception {
		Object pojo = this.getOneById(repository, id);
		Method setActivateFlag = pojo.getClass().getDeclaredMethod("setActivateFlag", String.class);
		setActivateFlag.invoke(pojo, activateFlag);
		Method save = getAllPubliAndInheritedMethod(repository, "save");
		save.invoke(repository, pojo);
		return pojo;
	}

	public void updateActivateFlagBatch(Object repository, List<Long> ids, String activateFlag) throws Exception {
		if (ids.size() > 0) {
			for (Long id : ids) {
				updateActivateFlag(repository, id, activateFlag);
			}
		}
	}

	/**
	 * 
	 * @param repository
	 * @param id
	 * @throws Exception
	 */
	public void delete(Object repository, Long id) throws Exception {
		Method deleteById = getAllPubliAndInheritedMethod(repository, "deleteById");
		deleteById.invoke(repository, id);
		// 由于获取不到父类的方法，暂时采用批量删除
		// List<Long> ids = new ArrayList<Long>();
		// ids.add(id);
		// deleteBatch(repository, ids);
	}

	/**
	 * 
	 * @param repository
	 * @param ids
	 * @throws Exception
	 */
	public void deleteBatch(Object repository, List<Long> ids) throws Exception {
		if (ids != null && ids.size() > 0) {
			Method deleteById = getAllPubliAndInheritedMethod(repository, "deleteAllByIdInBatch");
			deleteById.invoke(repository, ids);
		}
	}

	private Long getId(Object vo) throws Exception {
		Method getId = vo.getClass().getDeclaredMethod("getId");
		Object idObj = getId.invoke(vo);
		Long id = idObj == null ? null : Long.valueOf(idObj.toString());
		return id;
	}

	private String getActivateFlag(Object vo) throws Exception {
		Method getActivateFlag = vo.getClass().getDeclaredMethod("getActivateFlag");
		String activateFlag = (String) getActivateFlag.invoke(vo);
		return activateFlag;
	}

	private Object getFieldValue(Object vo, String fieldName) throws Exception {
		fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		Method getField = vo.getClass().getDeclaredMethod("get" + fieldName);
		Object fieldValue = getField.invoke(vo);
		return fieldValue;
	}

	private Object getOneById(Object repository, Long id) throws Exception {
		Method getOne = getAllPubliAndInheritedMethod(repository, "getOne");
		Object one = getOne.invoke(repository, id);
		return one;
	}

	public List<Long> convertIdToLong(String ids) {
		if (StringUtils.isNotEmpty(ids)) {
			return JSONArray.parseArray(ids, Long.class);
		} else {
			return null;
		}
	}

	public boolean checkExist(Object repository, Object vo, String codeName) throws Exception {
		Long id = this.getId(vo);
		Object codeValueObj = this.getFieldValue(vo, codeName);
		String codeValue = codeValueObj == null ? "" : codeValueObj.toString();
		Method checkExist = repository.getClass().getDeclaredMethod("checkExist", Long.class, String.class);
		return (boolean) checkExist.invoke(repository, id, codeValue);
	}

	public Method getAllPubliAndInheritedMethod(Object repository, String methodName) throws Exception {
		Method myMethod = null;
		for (Method aMethod : repository.getClass().getMethods()) {
			if (methodName.equals(aMethod.getName())) {
				myMethod = aMethod;
				break;
			}
		}
		if (myMethod != null) {
			return myMethod;
		} else {
			throw new Exception(repository.getClass().getName() + " not exist method " + methodName);
		}
	}

	/**
	 * 把数据库中已存在的对象属性copy到新的pojo中，新的pojo属性值为空时才copy,但是如果是修改值为空，不能使用该方法
	 * 
	 * @param newPojo
	 * @param oldPojo
	 * @throws Exception
	 */
	public void copyPropertyValueToNewPojo(Object newPojo, Object oldPojo) throws Exception {
		// BeanInfo poInfo = Introspector.getBeanInfo(oldPojo.getClass());
		// 不能使用JPA代理对象读取属性
		BeanInfo poInfo = Introspector.getBeanInfo(newPojo.getClass());
		PropertyDescriptor[] props = poInfo.getPropertyDescriptors();
		for (int i = 0; i < props.length; i++) {
			PropertyDescriptor prop = props[i];
			String fieldName = prop.getName();
			if (!StringUtils.equals("class", fieldName) && !StringUtils.equals("hibernateLazyInitializer", fieldName)) {
				if (fieldName.length() > 4) {
					String desc = StringUtils.substring(fieldName, fieldName.length() - 4);
					if ("Desc".equals(desc)) {
						continue;
					}
				}
				Method getter = prop.getReadMethod();
				Object oldValue = getter.invoke(oldPojo);
				Object newValue = getter.invoke(newPojo);

				if (newValue == null || StringUtils.isEmpty(newValue.toString())) {
					String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
					Method setter = newPojo.getClass().getDeclaredMethod(methodName, prop.getPropertyType());
					if (setter != null) {
						if (oldValue != null && StringUtils.isNotEmpty(oldValue.toString())) {
							setter.invoke(newPojo, oldValue);
						} else {
							if (!Arrays.asList(commonPropertys).contains(fieldName)) {
								setter.invoke(newPojo, new Object[] { null });
							}
						}
					}
				}
			}
		}
	}

	public boolean isExistProperty(Object pojo, String propertyName) throws Exception {
		BeanInfo poInfo = Introspector.getBeanInfo(pojo.getClass());
		PropertyDescriptor[] props = poInfo.getPropertyDescriptors();
		for (int i = 0; i < props.length; i++) {
			PropertyDescriptor prop = props[i];
			String fieldName = prop.getName();
			if (StringUtils.equals(fieldName, propertyName)) {
				return true;
			}
		}
		return false;
	}

	public List<Long> convertStringToLong(String ids) {
		String[] idsNoBlankArray = ids.split(",");
		Long[] convert = (Long[]) ConvertUtils.convert(idsNoBlankArray, Long.class);
		// 然后转换成为 list
		List<Long> keys = Arrays.asList(convert);
		return keys;
	}

	private String[] commonPropertys = new String[] { "activateFlag", "createDate", "createBy", "modifyDate",
			"modifyBy", "tenantId" };
}
