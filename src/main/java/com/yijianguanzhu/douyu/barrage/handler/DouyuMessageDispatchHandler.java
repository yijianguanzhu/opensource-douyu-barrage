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
 * 消息分发处理器
 */
package com.yijianguanzhu.douyu.barrage.handler;

import com.yijianguanzhu.douyu.barrage.config.DouyuConfiguration;
import com.yijianguanzhu.douyu.barrage.enums.BaseMessageTypeEnum;
import com.yijianguanzhu.douyu.barrage.enums.MessageType;
import com.yijianguanzhu.douyu.barrage.function.BiConsumer;
import com.yijianguanzhu.douyu.barrage.model.BaseMessage;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author yijianguanzhu 2020年9月11日
 */
@Slf4j
public class DouyuMessageDispatchHandler extends ChannelDuplexHandler {

	// 消息监听器
	private Map<BaseMessageTypeEnum, BiConsumer<String, BaseMessage, ChannelHandlerContext>> messageListener;

	public DouyuMessageDispatchHandler(
			Map<BaseMessageTypeEnum, BiConsumer<String, BaseMessage, ChannelHandlerContext>> messageListener ) {
		this.messageListener = messageListener;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void channelRead( ChannelHandlerContext ctx, Object msgs ) throws Exception {
		if ( msgs instanceof List ) {
			final List<Object> msg = ( List<Object> ) msgs;

			// 获取JsonString
			String jsonString = ( String ) msg.get( 1 );
			// 获取 BaseMessage
			BaseMessage baseMessage = ( BaseMessage ) msg.get( 2 );

			// 消息分发
			messageDispatch( messageListener, jsonString, baseMessage, ctx );
		}
	}

	// 当Channel和远程服务断开连接后，仍然发送数据，会抛异常，该方法被调用。
	@Override
	public void exceptionCaught( ChannelHandlerContext ctx, Throwable cause ) throws Exception {
		if ( ctx.channel().isOpen() ) {
			log.error( "Un-Excepted Error occured.", cause );
			ctx.close();
		}
	}

	static void messageDispatch(
			Map<BaseMessageTypeEnum, BiConsumer<String, BaseMessage, ChannelHandlerContext>> messageListener,
			String jsonString, BaseMessage baseMessage, ChannelHandlerContext ctx ) {
		if ( messageListener != null && !messageListener.isEmpty() ) {
			Iterator<Entry<BaseMessageTypeEnum, BiConsumer<String, BaseMessage, ChannelHandlerContext>>> iterator = messageListener
					.entrySet()
					.iterator();
			boolean releaseAll = false;
			while ( iterator.hasNext() ) {
				Entry<BaseMessageTypeEnum, BiConsumer<String, BaseMessage, ChannelHandlerContext>> next = iterator.next();
				BaseMessageTypeEnum key = next.getKey();
				// 监听所有消息
				if ( !releaseAll && MessageType.ALL.getType().equals( key.getType() ) ) {
					releaseAll = true;
					executor( next.getValue(), jsonString, baseMessage, ctx );
				}
				// 监听指定消息类型
				if ( key.getType().equals( baseMessage.getType() ) ) {
					executor( next.getValue(), jsonString, baseMessage, ctx );
					break;
				}
			}
		}
	}

	private static void executor( BiConsumer<String, BaseMessage, ChannelHandlerContext> value, String jsonString,
			BaseMessage baseMessage, ChannelHandlerContext ctx ) {
		DouyuConfiguration.executor().execute( () -> value.accept( jsonString, baseMessage, ctx ) );
	}
}
