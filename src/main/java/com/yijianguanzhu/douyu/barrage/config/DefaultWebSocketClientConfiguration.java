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
	public static ChannelFuture login( long roomId,
			Map<BaseMessageTypeEnum, BiConsumer<String, BaseMessage, ChannelHandlerContext>> messageListener,
			boolean retry ) {
		BaseBarrageServerConnectionAddress address = BasePullBarrageServerConnectionAddress.PULL_ADDRESS.next();
		return connect( address, DouyuConfiguration.defaultPullMessageType(), messageListener, retry, roomId, null );
	}

	/**
	 * 挂机直播间入口
	 */
	public static ChannelFuture login( long roomId,
			Map<BaseMessageTypeEnum, BiConsumer<String, BaseMessage, ChannelHandlerContext>> messageListener,
			DouyuCookie cookie, boolean retry ) {
		BaseBarrageServerConnectionAddress address = BasePushBarrageServerConnectionAddress.PUSH_ADDRESS.next();
		DefaultPushMessageType pushBarrage = DouyuConfiguration.defaultPushMessageType();
		// rt 与 vk 必须使用同一秒时间戳(网页版 gatewayLogin 源码：vk = md5(rt + secret + devid))，
		// 分别调用 currentTimeMillis() 在跨秒边界会导致 vk 与 rt 不匹配
		long rt = System.currentTimeMillis() / 1000L;
		pushBarrage
				.setLogin( String.format( pushBarrage.getLogin(), roomId, cookie.getAcf_username(), cookie.getAcf_ltkid(),
						cookie.getAcf_biz(), cookie.getAcf_stk(), cookie.getAcf_did(), rt,
						vk( cookie.getAcf_did(), rt ) ) );
		// login 已是最终报文，处理器直接发送，不再二次 String.format
		pushBarrage.setPreFormatted( true );
		/**
		 * 挂机直播间不允许断线重连，如需要这个功能，由上层实现。
		 */
		return connect( address, pushBarrage, messageListener, retry, roomId, cookie );
	}

	public static ChannelFuture connect( BaseBarrageServerConnectionAddress address, DefaultPushMessageType messageType,
			Map<BaseMessageTypeEnum, BiConsumer<String, BaseMessage, ChannelHandlerContext>> messageListener,
			boolean retry, long roomId, DouyuCookie cookie ) {
		final URI uri = address.getAddress();
		final ChannelFuture connect = bootstrap( uri ).connect( uri.getHost(), uri.getPort() );
		connect.addListener( connectFuture -> {
			if ( connectFuture.isSuccess() ) {
				log.info( "({})地址连接成功.", uri.toString() );
				Channel channel = connect.channel();
				DouyuAutoJoinBarrageServerHandler handler =
						new DouyuAutoJoinBarrageServerHandler( messageType, roomId, messageListener, address, retry );
				handler.setDouyuCookie( cookie );
				channel.pipeline().addLast( handler );

				channel.closeFuture().addListener( closeFuture -> {
					log.info( "({})连接已关闭. [roomId：{}]", uri, roomId );
				} );
				return;
			}
			log.error( "({})地址连接失败. Cause By \n", uri.toString(), connectFuture.cause() );
			if ( retry ) {
				log.info( "尝试使用新地址重新连接." );
				connect( address.next(), messageType, messageListener, true, roomId, cookie );
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
	 * 挂机直播间配置。vk 秘钥与拼接顺序已源码级验证(2026-10 网页版 room_common_*.js gatewayLogin)：
	 * {@code vk = md5(rt + secret + devid)}，网页版至今仍使用该老秘钥。
	 */
	public static String vk_secret = "r5*^5;}2#${XF[h+;'./.Q'1;,-]f'p[";

	private static String vk( String devid, long rt ) {
		return DigestUtils.md5Hex( rt + vk_secret + devid );
	}

	/**
	 * <p>
	 * 构建一条可直接 {@code ctx.writeAndFlush(...)} 发送的弹幕消息(推送连接)，格式与斗鱼网页最新版(2026-10)抓包一致：
	 * {@code pe@=0/content@=%s/col@=0/type@=chatmessage/dy@=%s/sender@=%d/ifs@=0/nc@=0/dat@=0/rev@=0/tts@=0/admzq@=0/cst@=%d/}
	 * </p>
	 * <p>
	 * 弹幕内容转义与网页版行为一致(源码逆向自 room_common_*.js 的 chatmessage 预处理 + STT 序列化器)：
	 * <ol>
	 * <li>反斜杠 {@code \} 先转为全角 {@code ＼}，连续两个再合并为一个(网页版 {@code replace(/\\/g,"＼").replace(/＼＼/g,"＼")} 的行为)；</li>
	 * <li>{@code @} 与 {@code /} 按斗鱼 STT 协议转义为 {@code @A} / {@code @S}(key 与 value 均转义)。</li>
	 * </ol>
	 * 调用方无需再处理。
	 * </p>
	 *
	 * @param cookie 登录Cookie，需正确设置 acf_did(设备ID) 与 acf_uid(用户ID)
	 * @param content 弹幕内容
	 * @return 可直接发送的 STT 消息
	 */
	public static String pushMessage( DouyuCookie cookie, String content ) {
		String escaped = content == null ? "" : content
				.replace( "\\", "＼" )
				.replace( "＼＼", "＼" )
				.replace( "@", "@A" )
				.replace( "/", "@S" );
		return String.format( DouyuConfiguration.defaultPushMessageType().getPushMessage(),
				escaped, cookie.getAcf_did(), cookie.getAcf_uid(), System.currentTimeMillis() );
	}
}
