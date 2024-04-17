package com.slipper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.slipper.unieap.*")
//支持在字段或方法上进行注解 @CreateDate、@CreatedBy、@LastModifiedDate、@LastModifiedBy
@EnableJpaAuditing
//开启缓存
@EnableCaching
public class SlipperApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlipperApplication.class, args);
	}
}
