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
 * 连接斗鱼弹幕
 */
package com.yijianguanzhu.douyu.barrage.config;

import com.yijianguanzhu.douyu.barrage.enums.BaseBarrageServerConnectionAddress;
import com.yijianguanzhu.douyu.barrage.enums.BaseMessageTypeEnum;
import com.yijianguanzhu.douyu.barrage.enums.BasePullBarrageServerConnectionAddress;
import com.yijianguanzhu.douyu.barrage.enums.BasePushBarrageServerConnectionAddress;
import com.yijianguanzhu.douyu.barrage.function.BiConsumer;
import com.yijianguanzhu.douyu.barrage.handler.DouyuAutoJoinBarrageServerHandler;
import com.yijianguanzhu.douyu.barrage.model.BaseMessage;
import com.yijianguanzhu.douyu.barrage.model.DefaultPushMessageType;
import com.yijianguanzhu.douyu.barrage.model.DouyuCookie;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yijianguanzhu 2020年9月25日
 */
@Slf4j
public class DefaultWebSocketClientConfiguration {

	private final static Map<URI, Bootstrap> BOOTSTRAP = new ConcurrentHashMap<>();

	/**
	 * 拉取弹幕入口
	 */
	public static void login( long roomId,
			Map<BaseMessageTypeEnum, BiConsumer<String, BaseMessage, ChannelHandlerContext>> messageListener,
			boolean retry ) {
		BaseBarrageServerConnectionAddress address = BasePullBarrageServerConnectionAddress.PULL_ADDRESS.next();
		connect( address, DouyuConfiguration.defaultPullMessageType(), messageListener, retry, roomId );
	}

	/**
	 * 挂机直播间入口
	 */
	public static void login( long roomId,
			Map<BaseMessageTypeEnum, BiConsumer<String, BaseMessage, ChannelHandlerContext>> messageListener,
			DouyuCookie cookie, boolean retry ) {
		BaseBarrageServerConnectionAddress address = BasePushBarrageServerConnectionAddress.PUSH_ADDRESS.next();
		DefaultPushMessageType pushBarrage = DouyuConfiguration.defaultPushMessageType();
		pushBarrage
				.setLogin( String.format( pushBarrage.getLogin(), roomId, cookie.getAcf_username(), cookie.getAcf_ltkid(),
						cookie.getAcf_biz(), cookie.getAcf_stk(), cookie.getAcf_did(), System.currentTimeMillis() / 1000L,
						vk( cookie.getAcf_did() ) ) );
		/**
		 * 挂机直播间不允许断线重连，如需要这个功能，由上层实现。
		 */
		connect( address, pushBarrage, messageListener, retry, roomId );
	}

	public static ChannelFuture connect( BaseBarrageServerConnectionAddress address, DefaultPushMessageType messageType,
			Map<BaseMessageTypeEnum, BiConsumer<String, BaseMessage, ChannelHandlerContext>> messageListener,
			boolean retry, long roomId ) {
		final URI uri = address.getAddress();
		final ChannelFuture connect = bootstrap( uri ).connect( uri.getHost(), uri.getPort() );
		connect.addListener( connectFuture -> {
			if ( connectFuture.isSuccess() ) {
				log.info( "({})地址连接成功.", uri.toString() );
				Channel channel = connect.channel();
				channel.pipeline()
						.addLast( new DouyuAutoJoinBarrageServerHandler( messageType, roomId, messageListener, address, retry ) );

				channel.closeFuture().addListener( closeFuture -> {
					log.info( "({})连接已关闭. [roomId：{}]", uri, roomId );
				} );
				return;
			}
			log.error( "({})地址连接失败. Cause By \n", uri.toString(), connectFuture.cause() );
			if ( retry ) {
				log.info( "尝试使用新地址重新连接." );
				connect( address.next(), messageType, messageListener, retry, roomId );
			}
		} );
		return connect;
	}

	public static Bootstrap bootstrap( URI uri ) {
		Bootstrap defaultBootstrap = BOOTSTRAP.get( uri );
		if ( defaultBootstrap == null ) {
			Bootstrap bootstrap = DouyuConfiguration.bootstrap().handler( new DefaultChannelInitializer( uri ) );
			defaultBootstrap = BOOTSTRAP.putIfAbsent( uri, bootstrap );
			if ( defaultBootstrap == null ) {
				defaultBootstrap = bootstrap;
			}
		}
		return defaultBootstrap;
	}

	/**
	 * 挂机直播间配置
	 */
	public static String vk_secret = "r5*^5;}2#${XF[h+;'./.Q'1;,-]f'p[";

	private static String vk( String devid ) {
		return DigestUtils.md5Hex( System.currentTimeMillis() / 1000L + vk_secret + devid );
	}
}
