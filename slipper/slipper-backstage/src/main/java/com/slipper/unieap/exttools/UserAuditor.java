package com.slipper.unieap.exttools;

import java.util.Optional;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import com.slipper.service.modules.administrator.model.dto.AdministratorRoleDTO;

@Configuration
public class UserAuditor implements AuditorAware<String> {

	/**
	 * 自定义配置类实现AuditorAware接口，重写getCurrentAuditor方法，给CreatedBy和LastModifiedBy赋值
	 */

	@Override
	public Optional<String> getCurrentAuditor() {
		try {
			AdministratorRoleDTO ad = (AdministratorRoleDTO) SecurityUtils.getSubject().getPrincipal();
			if (ad != null) {
				return Optional.ofNullable(ad.getUsername());
			} else {
				return Optional.empty();
			}
		} catch (Exception e) {
			return Optional.of("system-default");
		}

	}
}
