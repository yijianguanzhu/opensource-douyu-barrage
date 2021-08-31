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
 * 默认心跳处理器
 */
package com.yijianguanzhu.douyu.barrage.config;

import com.yijianguanzhu.douyu.barrage.model.DefaultPushMessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author yijianguanzhu 2020年9月24日
 */
@Slf4j
public final class DefaultPingPongRunnable implements Runnable {
	private ChannelHandlerContext ctx;
	private long roomId;
	private DefaultPushMessageType defaultMessageType;
	private ScheduledFuture<?> scheduleAtFixedRate;
	private String url;

	public DefaultPingPongRunnable( ChannelHandlerContext ctx, DefaultPushMessageType defaultMessageType, long roomId,
			String url ) {
		this.ctx = ctx;
		this.roomId = roomId;
		this.defaultMessageType = defaultMessageType;
		this.url = url;
	}

	@Override
	public void run() {
		if ( ctx.channel().isOpen() ) {
			log.info( "保持斗鱼(房间：{})弹幕心跳，连接地址[{}]", roomId, url );
			ctx.writeAndFlush( defaultMessageType.getKeeplive() );
		}
		else {
			log.warn( "斗鱼(房间：{})连接已关闭，关闭心跳任务，连接地址[{}]", roomId, url );
			scheduleAtFixedRate.cancel( true );
		}
	}

	public void schedule() {
		this.scheduleAtFixedRate = ctx.executor().scheduleAtFixedRate( this, 45L, 45L, TimeUnit.SECONDS );
	}
}
