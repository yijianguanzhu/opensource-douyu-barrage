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
package com.yijianguanzhu.douyu.barrage.bootstrap;

import com.yijianguanzhu.douyu.barrage.config.DefaultWebSocketClientConfiguration;
import com.yijianguanzhu.douyu.barrage.enums.BaseMessageTypeEnum;
import com.yijianguanzhu.douyu.barrage.function.BiConsumer;
import com.yijianguanzhu.douyu.barrage.model.BaseMessage;
import com.yijianguanzhu.douyu.barrage.model.DouyuCookie;
import io.netty.channel.ChannelHandlerContext;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author yijianguanzhu 2020年9月23日
 */
public class Douyu {

	// 获取弹幕入口
	public static DouyuMessageListener pull() {
		return new DouyuMessageListener();
	}

	// 直播间挂机入口
	public static DouyuMessageListener push( DouyuCookie cookie ) {
		return new DouyuMessageListener( cookie );
	}

	public static class DouyuMessageListener {
		private Map<BaseMessageTypeEnum, BiConsumer<String, BaseMessage, ChannelHandlerContext>> messageListener = new LinkedHashMap<>();
		private DouyuCookie cookie;
		private boolean isPush;

		public DouyuMessageListener() {
		}

		public DouyuMessageListener( DouyuCookie cookie ) {
			this.cookie = cookie;
			this.isPush = true;
		}

		public <T extends Enum<T> & BaseMessageTypeEnum> DouyuMessageListener registerMessageListener( T messageType,
				BiConsumer<String, BaseMessage, ChannelHandlerContext> messageHandler ) {
			this.messageListener.put( messageType, messageHandler );
			return this;
		}

		public DouyuRoom room() {
			if ( isPush ) {
				return new DouyuRoom( this.messageListener, cookie );
			}
			return new DouyuRoom( this.messageListener );
		}
	}

	public static class DouyuRoom {
		private Map<BaseMessageTypeEnum, BiConsumer<String, BaseMessage, ChannelHandlerContext>> messageListener;
		private long roomId;
		/**
		 * 当与服务端断开连接时，是否重连服务端，默认值 false
		 */
		private boolean retry = false;
		private DouyuCookie cookie;
		private boolean isPush;

		public DouyuRoom(
				Map<BaseMessageTypeEnum, BiConsumer<String, BaseMessage, ChannelHandlerContext>> messageListener ) {
			this.messageListener = messageListener;
		}

		public DouyuRoom( Map<BaseMessageTypeEnum, BiConsumer<String, BaseMessage, ChannelHandlerContext>> messageListener,
				DouyuCookie cookie ) {
			this.messageListener = messageListener;
			this.cookie = cookie;
			this.isPush = true;
		}

		public DouyuRoom retry( boolean retry ) {
			this.retry = retry;
			return this;
		}

		public DouyuBootstrap roomId( long roomId ) {
			this.roomId = roomId;
			if ( isPush ) {
				return new DouyuBootstrap( this.messageListener, this.roomId, this.cookie, this.retry );
			}
			return new DouyuBootstrap( this.messageListener, this.roomId, this.retry );
		}
	}

	public static class DouyuBootstrap {
		private Map<BaseMessageTypeEnum, BiConsumer<String, BaseMessage, ChannelHandlerContext>> messageListener;
		private long roomId;
		private boolean retry;
		private DouyuCookie cookie;
		private boolean isPush;

		public DouyuBootstrap(
				Map<BaseMessageTypeEnum, BiConsumer<String, BaseMessage, ChannelHandlerContext>> messageListener, long roomId,
				boolean retry ) {
			this.messageListener = messageListener;
			this.roomId = roomId;
			this.retry = retry;
		}

		public DouyuBootstrap(
				Map<BaseMessageTypeEnum, BiConsumer<String, BaseMessage, ChannelHandlerContext>> messageListener, long roomId,
				DouyuCookie cookie, boolean retry ) {
			this.messageListener = messageListener;
			this.roomId = roomId;
			this.cookie = cookie;
			this.isPush = true;
			this.retry = retry;
		}

		public void login() {
			if ( this.isPush ) {
				DefaultWebSocketClientConfiguration.login( roomId, messageListener, cookie, retry );
				return;
			}
			DefaultWebSocketClientConfiguration.login( roomId, messageListener, retry );
		}
	}
}
