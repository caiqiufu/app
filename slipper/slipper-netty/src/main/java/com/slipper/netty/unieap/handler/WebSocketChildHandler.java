package com.slipper.netty.unieap.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.slipper.common.utils.Constant;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * webSocketChildHandler
 *
 * @author gumingchen
 * @email 1240235512@qq.com
 * @date 1995-08-30 00:00:00
 */

@Service
public class WebSocketChildHandler extends ChannelInitializer<SocketChannel> {

	/**
	 * WebSocketUrl
	 */
	private String webSocketUrl;

	public String getWebSocketUrl() {
		return webSocketUrl;
	}
	

	public void setWebSocketUrl(String webSocketUrl) {
		this.webSocketUrl = webSocketUrl;
	}


	@Autowired
	public WebSocketServerHandler webSocketServerHandler;

	/**
	 * 初始化Channel
	 * 
	 * @param socketChannel socketChannel
	 */
	@Override
	protected void initChannel(SocketChannel socketChannel) {
		ChannelPipeline pipeline = socketChannel.pipeline();
		// 将请求与应答消息编码或者解码为HTTP消息
		pipeline.addLast("http-codec", new HttpServerCodec());
		// 将http消息的多个部分组合成一条完整的HTTP消息
		pipeline.addLast("aggregator", new HttpObjectAggregator(Constant.MAX_CONTENT_LENGTH));
		// 向客户端发送HTML5文件。主要用于支持浏览器和服务端进行WebSocket通信
		pipeline.addLast("http-chunked", new ChunkedWriteHandler());
		// 服务端Handler
		webSocketServerHandler.setWebSocketUrl(webSocketUrl);
		pipeline.addLast("handler", webSocketServerHandler);
	}
}
