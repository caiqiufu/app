package com.slipper.netty.unieap.handler;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Resource;

import org.msgpack.MessagePack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.gson.Gson;
import com.slipper.netty.unieap.WebSocketUsers;
import com.slipper.netty.unieap.entity.MessageEntity;
import com.slipper.netty.unieap.entity.RequestEntity;
import com.slipper.netty.unieap.entity.ResponseEntity;
import com.slipper.netty.unieap.entity.WebSocketMessageEntity;
import com.slipper.service.modules.token.dao.TokenDao;
import com.slipper.service.modules.token.entity.TokenEntity;
import com.slipper.service.modules.token.service.TokenService;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.AttributeKey;
import io.netty.util.internal.PlatformDependent;

@Service
@ChannelHandler.Sharable
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

	@Resource
	private TokenService tokenService;
	
	@Resource
    TokenDao tokenDao;
	/**
	 * 日志控制器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketServerHandler.class);

	/**
	 * 用户集
	 */
	private static final ConcurrentMap<String, Channel> USERS = PlatformDependent.newConcurrentHashMap();

	/**
	 * 管道附属key
	 */
	private static final AttributeKey<TokenEntity> ATTRIBUTE_KEY = AttributeKey.valueOf("token");

	/**
	 * webSocketUrl
	 */
	private String webSocketUrl;
	
	

	public String getWebSocketUrl() {
		return webSocketUrl;
	}

	public void setWebSocketUrl(String webSocketUrl) {
		this.webSocketUrl = webSocketUrl;
	}

	/**
	 * 用于打开关闭握手
	 */
	private WebSocketServerHandshaker socketServerHandShaker;

	/**
	 * 存储通道
	 * 
	 * @param key     唯一键
	 * @param channel 通道
	 */
	public static void put(String key, Channel channel) {
		USERS.put(key, channel);
	}
	
	public TokenEntity queryTokenByToken(String token) {
        LambdaQueryWrapper<TokenEntity> wrapper = new LambdaQueryWrapper<TokenEntity>()
                .eq(TokenEntity::getToken, token);
        return tokenDao.selectOne(wrapper);
    }

	/**
	 * 异常
	 * 
	 * @param channelHandlerContext channelHandlerContext
	 * @param cause                 异常
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause) throws Exception {
		cause.printStackTrace();
		LOGGER.error("🤝[服务端捕捉异常]: {}", cause.getMessage());
		channelHandlerContext.close();
	}

	/**
	 * 当客户端主动链接服务端的链接后，调用此方法
	 * 
	 * @param channelHandlerContext ChannelHandlerContext
	 */
	@Override
	public void channelActive(ChannelHandlerContext channelHandlerContext) {
		LOGGER.info("🤝与客户端[{}]建立连接\n" + "🤝[服务器当前在线人数]: {}", channelHandlerContext.channel().remoteAddress(),
				WebSocketUsers.getUSERS().size() + 1);
	}

	/**
	 * 与客户端断开连接时 调用此方法
	 * 
	 * @param channelHandlerContext channelHandlerContext
	 */
	@Override
	public void channelInactive(ChannelHandlerContext channelHandlerContext) {
		Channel channel = channelHandlerContext.channel();
		LOGGER.info("🤝与客户端[{}]断开连接", channel.remoteAddress());
		// 从存储结构中移除通道，也可以用缓存来替代
		WebSocketUsers.remove(channel);
		// 关闭通道
		channel.close();
	}

	/**
	 * 读完之后调用的方法
	 * 
	 * @param channelHandlerContext ChannelHandlerContext
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {
		channelHandlerContext.flush();
	}

	/**
	 * @param channelHandlerContext
	 * @param o
	 * @throws Exception
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
		messageReceived(channelHandlerContext, msg);
	}

	/**
	 * 接收客户端发送的消息
	 * 
	 * @param channelHandlerContext ChannelHandlerContext
	 * @param receiveMessage        消息
	 */
	protected void messageReceived(ChannelHandlerContext channelHandlerContext, Object receiveMessage)
			throws Exception {
		// 传统http接入 第一次需要使用http建立握手
		if (receiveMessage instanceof FullHttpRequest) {
			FullHttpRequest fullHttpRequest = (FullHttpRequest) receiveMessage;
			LOGGER.info("🤝[握手]: {}", fullHttpRequest.uri());
			// 握手
			handlerHttpRequest(channelHandlerContext, fullHttpRequest);
			// 发送连接成功给客户端
			Gson gson = new Gson();
			ResponseEntity responseEntity = new ResponseEntity();
			responseEntity.setType(0);
			responseEntity.setMessage("success");
			String responseString = gson.toJson(responseEntity);
			channelHandlerContext.channel().write(new TextWebSocketFrame(responseString));
		}
		// WebSocket接入
		else if (receiveMessage instanceof WebSocketFrame) {
			WebSocketFrame webSocketFrame = (WebSocketFrame) receiveMessage;
			handlerWebSocketFrame(channelHandlerContext, webSocketFrame);
		}
	}

	/**
	 * 第一次握手 判断用户是否存在 验证token 是否过期
	 * 
	 * @param channelHandlerContext channelHandlerContext
	 * @param req                   请求
	 */
	private void handlerHttpRequest(ChannelHandlerContext channelHandlerContext, FullHttpRequest req) {
		// 构造握手响应返回
		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(webSocketUrl, null, false);
		// 从连接路径中截取连接用户名
		String uri = req.uri();
		int i = uri.lastIndexOf("/");
		String token = uri.substring(i + 1, uri.length());
		TokenEntity tokenEntity = this.queryTokenByToken(token);
		//TokenEntity tokenEntity = tokenService.queryByToken(token);
		// 判断凭证是否过期
		boolean bool = tokenEntity != null && tokenEntity.getExpiredAt().getTime() >= new Date().getTime();
		if (bool) {
			Channel connectChannel = channelHandlerContext.channel();
			// 加入在线用户
			WebSocketUsers.put(tokenEntity.getToken(), connectChannel);
			// 管道绑定用户信息
			connectChannel.attr(ATTRIBUTE_KEY).set(tokenEntity);
			socketServerHandShaker = wsFactory.newHandshaker(req);
			if (socketServerHandShaker == null) {
				// 发送版本错误
				WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(connectChannel);
			} else {
				// 握手响应
				socketServerHandShaker.handshake(connectChannel, req);
			}
		} else {
			// 断开连接
			channelHandlerContext.close();
		}
	}

	/**
	 * webSocket处理逻辑
	 * 
	 * @param channelHandlerContext channelHandlerContext
	 * @param frame                 webSocketFrame
	 */
	private void handlerWebSocketFrame(ChannelHandlerContext channelHandlerContext, WebSocketFrame frame)
			throws IOException {
		Channel channel = channelHandlerContext.channel();
		// region 纯文本消息
		if (frame instanceof TextWebSocketFrame) {
			String requestString = ((TextWebSocketFrame) frame).text();
			requestProcessing(requestString, channel);
			return;
		}
		// region 二进制消息
		if (frame instanceof BinaryWebSocketFrame) {
			BinaryWebSocketFrame binaryWebSocketFrame = (BinaryWebSocketFrame) frame;
			ByteBuf content = binaryWebSocketFrame.content();
			LOGGER.info("🤝[二进制数据]:{}", content);
			final int length = content.readableBytes();
			final byte[] array = new byte[length];
			content.getBytes(content.readerIndex(), array, 0, length);
			MessagePack messagePack = new MessagePack();
			WebSocketMessageEntity webSocketMessageEntity = messagePack.read(array, WebSocketMessageEntity.class);
			LOGGER.info("🤝[解码数据]: {}", webSocketMessageEntity);
			WebSocketUsers.sendMessageToUser(webSocketMessageEntity.getAcceptName(),
					webSocketMessageEntity.getContent());
		}
	}

	/**
	 * 请求处理
	 */
	private void requestProcessing(String requestString, Channel channel) {
		Gson gson = new Gson();
		RequestEntity request = gson.fromJson(requestString, RequestEntity.class);
		ResponseEntity response = new ResponseEntity();
		String responseString;
		switch (request.getType()) {
		case 0: // 心跳
			response.setType(0);
			responseString = gson.toJson(response);
			channel.write(new TextWebSocketFrame(responseString));
			break;
		case 1: // 通讯消息
			MessageEntity message = request.getRequestBody().getMessage();
			responseString = gson.toJson(response);
			// 发送给接收者
			response.getResponseBody().getMessage().setContent(responseString);
			response.setType(1);
			WebSocketUsers.sendMessageToUser(request.getRequestBody().getMessage().getTo(), gson.toJson(response));
			// 发送给发送者 确认消息
			ResponseEntity ackResponse = new ResponseEntity();
			ackResponse.getResponseBody().setMessage(message);
			ackResponse.setType(2);
			WebSocketUsers.sendMessageToUser(message.getFrom(), gson.toJson(ackResponse));
			break;
		}
	}
}
