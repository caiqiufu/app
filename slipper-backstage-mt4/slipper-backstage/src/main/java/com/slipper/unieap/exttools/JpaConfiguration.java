package com.slipper.unieap.exttools;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;

import com.slipper.unieap.utils.DBUtils;

@Configuration
public class JpaConfiguration {
	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * 初始化注入@JpaConvert对应的Converter
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostConstruct
	public void init() {
		Map<String, Object> map = applicationContext.getBeansWithAnnotation(JpaConvert.class);
		for (Object o : map.values()) {
			Class c = o.getClass();
			GenericConversionService genericConversionService = ((GenericConversionService) DefaultConversionService
					.getSharedInstance());
			genericConversionService.addConverter(Map.class, c, m -> {
				try {
					Object obj = c.newInstance();
					// 这里可以扩展,注入的converter,实现sql查寻出的结果为数据库中带下划线的字段,通过程序转为驼峰命名再设置到实体中
					// 也可以做类型转换判断,这里未做类型判断,直接copy到dto中,类型不匹配的时候可能会出错
					return copyMapToObj(m, obj);
				} catch (Exception e) {
					throw new FatalBeanException("Jpa结果转换出错,class=" + c.getName(), e);
				}
			});
		}
	}

	/**
	 * 将map中的值copy到bean中对应的字段上
	 * 
	 * @author bazhandao
	 * @date 2020-03-26
	 * @param map
	 * @param target
	 * @return
	 */
	private Object copyMapToObj(Map<String, Object> map, Object bean) {

		BeanInfo beanInfo;
		try {
			beanInfo = Introspector.getBeanInfo(bean.getClass());
			PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
			for (int i = 0; i < props.length; i++) {
				Method getter = props[i].getReadMethod();
				Method setter = props[i].getWriteMethod();
				String property = props[i].getName();
				if ("class".equals(property)) {
					continue;
				}
				if (getter == null) {
					continue;
				}
				if (setter == null) {
					continue;
				}
				//查询结果中如果不包含字段，则采用驼峰命名再查看结果中是否包含该属性
				if (!map.containsKey(property)) {
					property = DBUtils.convertPropertyToColumnField(property);
				}
				if (map.containsKey(property)) {
					Class<?> retType = getter.getReturnType();
					Object value = map.get(property);
					String str = "";
					try {
						if (value != null) {
							// value.getClass().getCanonicalName();
							if (("byte[]".equals(value.getClass().getName())) && (String.class == retType)) {
								Blob blob = (Blob) map.get(property);
								int bolblen = (int) blob.length();
								byte[] data = blob.getBytes(1, bolblen);
								String content = new String(data, "utf-8");
								setter.invoke(bean, content);
							} else if (String.class == retType && value.toString() != null) {
								str = value.toString();
								setter.invoke(bean, str);
							} else {
								if ((java.util.Date.class == retType || Timestamp.class == retType
										|| java.sql.Date.class == retType)) {
									Date date = (Date) map.get(property);
									setter.invoke(bean, date);
								} else if (java.lang.Long.class == retType) {
									setter.invoke(bean, Long.valueOf(value.toString()));
								} else if (boolean.class == retType) {
									if (value.getClass() == Long.class) {
										setter.invoke(bean, ((Long) value).intValue() == 1);
									} else if (value.getClass() == Integer.class) {
										setter.invoke(bean, ((Integer) value).intValue() == 1);
									}
								} else if (java.lang.Integer.class == retType) {
									setter.invoke(bean, Integer.valueOf(value.toString()));
								} else if (java.lang.Double.class == retType) {
									setter.invoke(bean, Double.valueOf(value.toString()));
								} else {
									setter.invoke(bean, value);
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						throw new RuntimeException("Error to get value,error:" + e.getMessage());
					}
				}
			}
		} catch (IntrospectionException e1) {
			return null;
		}
		return bean;
	}
}