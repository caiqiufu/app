package com.slipper.unieap.exttools;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MappingRequestParamResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		if (parameter.hasParameterAnnotation(MappingRequestParam.class)) {
			return true;
		}
		return false;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		// 生成结果的对象
		String beanName = webRequest.getParameter("beanName");
		if (StringUtils.isEmpty(beanName)) {
			throw new RuntimeException("pojo name is null");
		}
		Object beanObj = Class.forName("com.slipper.unieap." + beanName).newInstance();
		BeanInfo beanInfo = Introspector.getBeanInfo(beanObj.getClass());
		PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < props.length; i++) {
			String prop = props[i].getName();
			Class<?> type = props[i].getPropertyType();
			if (props[i].getWriteMethod() != null) {
				String[] values = webRequest.getParameterValues(prop);
				if (values != null && StringUtils.isNotEmpty(values[0])) {
					// Object value = type.newInstance().cast(values[0]);
					try {
						props[i].getWriteMethod().invoke(beanObj, getParameterObject(type, values[0].toString()));
					} catch (Exception e) {
						throw new Exception("prop=" + prop + ",type=" + type + ",value=" + values[0].toString() + ","
								+ e.getMessage());
					}

				}
			}
		}
		return beanObj;
	}

	public Object getParameterObject(Class<?> type, String value) {
		if (String.class == type) {
			return value;
		}
		if (Long.class == type) {
			return new Long(value);
		}
		if (Integer.class == type) {
			return new Integer(value);
		}
		if (Date.class == type) {
			return strToDate(value);
		}
		if (Double.class == type) {
			return new Double(value);
		}
		return value;
	}

	/**
	 * 将短时间格式字符串转换为时间 yyyy-MM-dd
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDate(String strDate) {
		String format = "";
		if ("yyyy-MM-dd".length() == strDate.length()) {
			format = "yyyy-MM-dd";
		}
		if ("yyyy-MM-dd HH:mm:ss".length() == strDate.length()) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}
}
