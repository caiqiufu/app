package com.slipper.netty.unieap;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "netty")
@Data
public class NettyConfig {

	// netty监听的端口
	private int port;
	// websocket访问路径
	private String url;
}
