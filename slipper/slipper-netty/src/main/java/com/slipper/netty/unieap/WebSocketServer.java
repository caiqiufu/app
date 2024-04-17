package com.slipper.netty.unieap;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.slipper.netty.unieap.handler.WebSocketChildHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * SpringBoot应用程序在启动后，会从容器中遍历实现了CommandLineRunner接口的实例并运行它们的run方法,所以实现CommandLineRunner的类需要添加@Component等注解。如果有多个类实现了CommandLineRunner接口的话,也可以利用@Order注解来规定所有CommandLineRunner实例的运行顺序。
 * 
 * @author Chai
 *
 */
@Component
public class WebSocketServer implements CommandLineRunner {
	/**
	 * 日志控制器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketServer.class);
	/**
	 * Netty配置类
	 */
	@Resource
	public NettyConfig nettyConfig;

	@Autowired
	public WebSocketChildHandler webSocketChildHandler;
	private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
	private EventLoopGroup workerGroup = new NioEventLoopGroup();

	/**
	 * 资源关闭，在容器销毁时关闭
	 */
	@PreDestroy
	public void close() {
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
	}

	/**
	 * 启动服务端的方法 推荐的线程数量计算公式： 1. 线程数量 = （线程总时间/瓶颈资源时间） * 瓶颈资源的线程并行数 2. QPS =
	 * 1000/线程总时间 * 线程数
	 * 
	 * @param port         服务器监听的端口号
	 * @param webSocketUrl webSocket url
	 *
	 * @throws Exception exception
	 */
	public void run(int port, String webSocketUrl) throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			webSocketChildHandler.setWebSocketUrl(webSocketUrl);
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(webSocketChildHandler);
			Channel ch = bootstrap.bind(port).sync().channel();
			LOGGER.info("====================================================================");
			LOGGER.info("***websocket访问地址***");
			LOGGER.info(webSocketUrl);
			LOGGER.info("====================================================================");
			ch.closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	@Override
	public void run(String... args) throws Exception {
		new Thread(() -> {
			try {
				run(nettyConfig.getPort(), nettyConfig.getUrl());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();
	}
}
