package com.slipper.unieap;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.slipper.unieap.exttools.MappingRequestParamResolver;

@Configuration
public class MyWebmvcConfiguration implements WebMvcConfigurer {
	
	@Bean
	public HttpMessageConverter<String> responseBodyStringConverter() {
		StringHttpMessageConverter converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
		return converter;
	}

	/**
	 * 修改StringHttpMessageConverter默认配置
	 * 
	 * @param converters
	 */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(responseBodyStringConverter());
	}

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

	/**
	 * 配置外部HandlerMethodArgumentResolver
	 */
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new MappingRequestParamResolver());
	}
	
	//自定义国际化组件  重写了 WebMvcAutoConfiguration 类中的  localeResolver 方法 
    @Bean
    public LocaleResolver localeResolver(){
        return new MyLocaleResolver();
    }
}
