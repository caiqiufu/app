package com.slipper.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web mvc config
 *
 * @author gumingchen
 * @email 1240235512@qq.com
 * @date 1995-08-30 00:00:00
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	/**
	 * 页面跨域访问Controller过滤
	 * 
	 * @param corsRegistry
	 */
	@Override
	public void addCorsMappings(CorsRegistry corsRegistry) {
		// 设置允许跨域的路由
		corsRegistry.addMapping("/**")
				// 设置允许跨域请求的域名
				.allowedOriginPatterns("*")
				// 是否允许证书（cookies）
				.allowCredentials(true)
				// 设置允许的方法
				.allowedMethods("*")
				// 设置允许的请求头
				.allowedHeaders("*");
	}

	@Value("${unieap.sharefolder-path}")
	String sharefolder_path;

	/**
	 * 上传的图片在F盘下的file目录下，访问路径如：http://localhost:8080/file/d3cf0281-bb7f-40e0-ab77-406db95ccf2c.jpg
	 * 其中file表示访问的前缀。"file:F:/file/"是文件真实的存储路径
	 * 
	 * @param registry
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/file/**").addResourceLocations("file:" + sharefolder_path);
	}
}
