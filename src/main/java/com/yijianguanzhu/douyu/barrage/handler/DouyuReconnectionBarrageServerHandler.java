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
 * 斗鱼断线重连handler
 */
package com.yijianguanzhu.douyu.barrage.handler;

import com.yijianguanzhu.douyu.barrage.config.DefaultWebSocketClientConfiguration;
import com.yijianguanzhu.douyu.barrage.config.DouyuConfiguration;
import com.yijianguanzhu.douyu.barrage.enums.BaseBarrageServerConnectionAddress;
import com.yijianguanzhu.douyu.barrage.enums.BaseMessageTypeEnum;
import com.yijianguanzhu.douyu.barrage.function.BiConsumer;
import com.yijianguanzhu.douyu.barrage.model.BaseMessage;
import com.yijianguanzhu.douyu.barrage.model.DefaultPushMessageType;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author yijianguanzhu 2020年9月27日
 */
@Slf4j
public class DouyuReconnectionBarrageServerHandler extends ChannelDuplexHandler {

	private BaseBarrageServerConnectionAddress address;
	private long roomId;
	private DefaultPushMessageType defaultMessageType;
	private Map<BaseMessageTypeEnum, BiConsumer<String, BaseMessage, ChannelHandlerContext>> messageListener;

	public DouyuReconnectionBarrageServerHandler( BaseBarrageServerConnectionAddress address,
			Map<BaseMessageTypeEnum, BiConsumer<String, BaseMessage, ChannelHandlerContext>> messageListener, long roomId,
			DefaultPushMessageType messageType ) {
		this.address = address;
		this.messageListener = messageListener;
		this.roomId = roomId;
		this.defaultMessageType = messageType;
	}

	/**
	 * <p>
	 * 运行过程中，连接主动断开/远程断开连接，会触发这个方法
	 * </p>
	 * <p>
	 * 注意：开启断线重连，也就意味着通道出问题时，总是会重新连接，就算是主动关闭连接，也会重连。
	 * </p>
	 * <p>
	 * 如果需要断开，可在
	 * {@link com.yijianguanzhu.douyu.barrage.bootstrap.Douyu.DouyuMessageListener#registerMessageListener(Enum, BiConsumer)}
	 * 中， 注册对应处理监听器，将channel中的{@link DouyuReconnectionBarrageServerHandler}移除
	 * </p>
	 */
	@Override
	public void channelInactive( ChannelHandlerContext ctx ) throws Exception {
		log.info( "({})连接中断，{}秒后，尝试使用新地址重新连接. [roomId:{}]", address.getAddress().toString(),
				DouyuConfiguration.RECONNECTION_DELAY_SECONDS, roomId );
		ctx.executor().schedule(
				() -> DefaultWebSocketClientConfiguration.connect( address.next(), defaultMessageType, messageListener, true,
						roomId ),
				DouyuConfiguration.RECONNECTION_DELAY_SECONDS, TimeUnit.SECONDS );
		ctx.fireChannelInactive();
	}
}
