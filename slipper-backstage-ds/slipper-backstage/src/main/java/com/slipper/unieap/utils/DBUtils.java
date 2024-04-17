package com.slipper.unieap.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;

import com.slipper.unieap.db.Criteria;
import com.slipper.unieap.db.Restrictions;

public class DBUtils {
	/**
	 * 将blob转化为byte[],可以转化二进制流的
	 * 
	 * @param blob
	 * @return
	 */
	public static byte[] blobToBytes(Blob blob) {
		InputStream is = null;
		byte[] b = null;
		try {
			is = blob.getBinaryStream();
			b = new byte[(int) blob.length()];
			is.read(b);
			return b;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				is = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return b;
	}

	public static <T> void setCriteria(Criteria<T> criteria, Object bean) throws Exception {
		if (bean != null) {
			Map<String, PropertyDescriptor> beanprops = getBeanProps(bean);
			Iterator<String> iter = beanprops.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				if (beanprops.containsKey(key)) {
					if (!isFieldDesc(beanprops, key)) {
						PropertyDescriptor prop = beanprops.get(key);
						Method getter = prop.getReadMethod();
						Object value = getter.invoke(bean);
						if (value != null && StringUtils.isNotEmpty(value.toString())) {
							Class<?> retType = getter.getReturnType();
							if ("id".equals(key)) {
								criteria.add(Restrictions.eq("id", value, true));
							} else if (String.class == retType) {
								criteria.add(Restrictions.like(key, value.toString(), true));
							} else {
								criteria.add(Restrictions.eq(key, value, true));
							}
						}
					}
				}
			}
		}
	}

	private static boolean isFieldDesc(Map<String, PropertyDescriptor> beanprops, String fieldName) {
		if (fieldName.length() > 4 && "Desc".equals(fieldName.substring(fieldName.length() - 4))) {
			String originalFieldName = fieldName.substring(fieldName.length() - 4);
			if (beanprops.containsKey(originalFieldName)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public static String convertPropertyToColumnField(String propertyName) {
		String ss = "";
		char[] charArray = propertyName.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			if (charArray[i] >= 'A' && charArray[i] <= 'Z') {
				ss += "_" + charArray[i];
			} else {
				ss += charArray[i];
			}
		}
		return ss;
	}

	@SuppressWarnings("rawtypes")
	public static String getCriteriaSql(Criteria criteria, Pageable pageable, String tableName) {
		String orderBy = pageable.getSort().toString() + " limit " + pageable.getPageNumber() + " "
				+ pageable.getPageSize();
		if (criteria == null) {
			return "select * from " + tableName + " order by " + orderBy;
		} else {
			return criteria.toString(tableName) + " order by " + orderBy;
		}
	}

	public static String getTableName(String jpaClass) {
		return convertPropertyToColumnField(jpaClass);
	}

	@Cacheable(cacheNames = "CriteriaBeanProps")
	public static Map<String, PropertyDescriptor> getBeanProps(Object bean) throws Exception {
		return generateBeanprops(bean);
	}

	public static Map<String, PropertyDescriptor> generateBeanprops(Object bean) throws IntrospectionException {
		Map<String, PropertyDescriptor> beanprops = new HashMap<String, PropertyDescriptor>();
		BeanInfo poInfo = Introspector.getBeanInfo(bean.getClass());
		PropertyDescriptor[] props = poInfo.getPropertyDescriptors();
		String methodName, desc;
		for (int i = 0; i < props.length; i++) {
			if (!"class".equals(props[i].getName())) {
				Method getter = props[i].getReadMethod();
				if (getter != null) {
					methodName = getter.getName();
					if (methodName.length() > 4) {
						desc = StringUtils.substring(methodName, methodName.length() - 4);
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
}
