/*
 * Copyright (c) 2021-2031, yijianguanzhu (yijianguanzhu@gmail.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * 自动加入弹幕服务器
 */
package com.yijianguanzhu.douyu.barrage.handler;

import com.yijianguanzhu.douyu.barrage.config.DefaultPingPongRunnable;
import com.yijianguanzhu.douyu.barrage.enums.BaseBarrageServerConnectionAddress;
import com.yijianguanzhu.douyu.barrage.enums.BaseMessageTypeEnum;
import com.yijianguanzhu.douyu.barrage.enums.MessageType;
import com.yijianguanzhu.douyu.barrage.function.BiConsumer;
import com.yijianguanzhu.douyu.barrage.model.BaseMessage;
import com.yijianguanzhu.douyu.barrage.model.DefaultPushMessageType;
import com.yijianguanzhu.douyu.barrage.model.DouyuCookie;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler.ClientHandshakeStateEvent;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * @author yijianguanzhu 2020年9月11日
 */
@Slf4j
public class DouyuAutoJoinBarrageServerHandler extends ChannelDuplexHandler {

	private DefaultPushMessageType defaultMessageType;
	private long roomId;
	private Map<BaseMessageTypeEnum, BiConsumer<String, BaseMessage, ChannelHandlerContext>> messageListener;
	private boolean retry;
	private BaseBarrageServerConnectionAddress address;
	@Setter
	private DouyuCookie douyuCookie;

	public DouyuAutoJoinBarrageServerHandler( DefaultPushMessageType messageType, long roomId,
			Map<BaseMessageTypeEnum, BiConsumer<String, BaseMessage, ChannelHandlerContext>> messageListener,
			BaseBarrageServerConnectionAddress address, boolean retry ) {
		this.defaultMessageType = messageType;
		this.roomId = roomId;
		this.messageListener = messageListener;
		this.retry = retry;
		this.address = address;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void channelRead( ChannelHandlerContext ctx, Object msgs ) throws Exception {
		if ( msgs instanceof List ) {
			List<Object> msg = ( List<Object> ) msgs;

			// 获取 BaseMessage
			BaseMessage baseMessage = ( BaseMessage ) msg.get( 2 );

			// 服务器端接收到登录请求的响应，我们这时候需要加入组。
			if ( MessageType.LOGIN_RES.getType().equals( baseMessage.getType() ) ) {
				log.info( "加入斗鱼(房间：{})弹幕分组", roomId );
				ctx.writeAndFlush( String.format( defaultMessageType.getJoinGroup(), roomId, defaultMessageType.getGid() ) );
			}

			// 服务器端希望我们返回一个心跳
			if ( MessageType.PING_REQ.getType().equals( baseMessage.getType() )
					|| MessageType.MSG_REPEATER_PROXY_LIST.getType().equals( baseMessage.getType() ) ) {
				DefaultPingPongRunnable runnable = new DefaultPingPongRunnable( ctx, defaultMessageType, roomId,
						url( ctx.pipeline() ) );
				// 定时任务
				runnable.schedule();
				runnable.run();

				// 删除handler，在成功加入弹幕服务器后，这些handler没用了
				remove( ctx.pipeline() );
				// 添加handler，处理消息
				add( ctx.pipeline() );
			}

			// 获取JsonString
			String jsonString = ( String ) msg.get( 1 );
			// 消息分发
			DouyuMessageDispatchHandler.messageDispatch( messageListener, jsonString, baseMessage, ctx );
		}
		// 消息透传
		ctx.fireChannelRead( msgs );
	}

	// 握手通知
	@Override
	public void userEventTriggered( ChannelHandlerContext ctx, Object evt ) throws Exception {

		// 处理握手事件
		if ( evt instanceof ClientHandshakeStateEvent ) {
			ClientHandshakeStateEvent status = ( ClientHandshakeStateEvent ) evt;
			// 握手超时事件
			if ( status == ClientHandshakeStateEvent.HANDSHAKE_TIMEOUT ) {
				log.error( "The Handshake was timed out: roomId({}), The channel is about to close.", roomId );
				ctx.close().addListener( future -> {
					if ( future.isSuccess() ) {
						log.info( "This channel has been closed successfully, roomdId({})", roomId );
					}
					else {
						log.warn( "This channel has not been closed successfully, roomdId({}). Cause By \n", roomId,
								future.cause() );
					}
				} );
			}

			// 服务未响应事件
			if ( status == ClientHandshakeStateEvent.HANDSHAKE_ISSUED ) {
				log.debug( "The Handshake was started but the server did not response yet to the request, roomId({})", roomId );
			}

			// 握手成功事件
			if ( status == ClientHandshakeStateEvent.HANDSHAKE_COMPLETE ) {
				log.debug( "The Handshake was complete succesful and so the channel was upgraded to websockets, roomId({})",
						roomId );
				log.info( "登录斗鱼(房间：{})弹幕服务器", roomId );
				ctx.writeAndFlush( String.format( defaultMessageType.getLogin(), roomId ) );
			}
		}
		ctx.fireUserEventTriggered( evt );
	}

	@Override
	public void exceptionCaught( ChannelHandlerContext ctx, Throwable cause ) throws Exception {
		ctx.fireExceptionCaught( cause );
		if ( ctx.channel().isOpen() ) {
			log.error( "Un-Excepted Error occured.", cause );
			ctx.close();
		}
	}

	// 加入弹幕服务器后，移除不用的handler
	private void remove( ChannelPipeline pipeline ) {
		HttpClientCodec codec = pipeline.get( HttpClientCodec.class );
		if ( codec != null ) {
			pipeline.remove( codec );
		}
		HttpObjectAggregator aggregator = pipeline.get( HttpObjectAggregator.class );
		if ( aggregator != null ) {
			pipeline.remove( aggregator );
		}
		pipeline.remove( this );
	}

	// 添加消息处理handler
	private void add( ChannelPipeline pipeline ) {
		pipeline.addLast( new DouyuMessageDispatchHandler( this.messageListener ) );
		if ( this.retry ) {
			DouyuReconnectionBarrageServerHandler handler =
					new DouyuReconnectionBarrageServerHandler( address, messageListener, roomId, defaultMessageType );
			handler.setDouyuCookie( douyuCookie );
			pipeline.addLast( handler );
		}
	}

	// 获取握手地址
	private String url( ChannelPipeline pipeline ) {
		final WebSocketClientProtocolHandler webSocketClient = pipeline.get( WebSocketClientProtocolHandler.class );
		return webSocketClient.handshaker().uri().toString();
	}
}
