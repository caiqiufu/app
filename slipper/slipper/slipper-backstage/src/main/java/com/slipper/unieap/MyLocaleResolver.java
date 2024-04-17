package com.slipper.unieap;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
/**
 * 自定义 语言环境解析器
 */
public class MyLocaleResolver  implements LocaleResolver {
	//解析请求
    @Override
    public Locale resolveLocale(HttpServletRequest httpServletRequest) {
        //获取请求中的语言参数
        Locale locale = null;
        final String language = httpServletRequest.getParameter("Language"); //例如 zh_CN  en_US
        System.out.println("------->"+language);
		if (!StringUtils.isEmpty(language)){ // return target == null || target.length() == 0;
            final String[] s = language.split("_");
            // s[0]:国家 s[1]:地区
            locale = new Locale(s[0],s[1]);
            return locale;
        }
        locale = Locale.getDefault(); // 如果没有请求参数就是默认的
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {

    }
}
