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
 * 
 */
package com.yijianguanzhu.douyu.barrage.config;

import com.yijianguanzhu.douyu.barrage.model.DefaultPushMessageType;
import com.yijianguanzhu.douyu.barrage.model.DefaultPushMessageType2PullBarrage;
import com.yijianguanzhu.douyu.barrage.model.DefaultPushMessageType2PushBarrage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;

/**
 * @author yijianguanzhu 2020年9月24日
 */
public final class DouyuConfiguration {

	private static volatile ExecutorService executor;
	private static volatile NioEventLoopGroup group;
	private static volatile DefaultPushMessageType defaultPullMessageType;
	private static volatile DefaultPushMessageType defaultPushMessageType;
	public static volatile long RECONNECTION_DELAY_SECONDS = 1800L;

	/**
	 * 获取消息处理线程池
	 */
	public static ExecutorService executor() {
		if ( executor == null ) {
			synchronized ( DouyuConfiguration.class ) {
				if ( executor == null ) {
					executor = new ThreadPoolExecutor(
							Runtime.getRuntime().availableProcessors() * 2, 100, 60L, TimeUnit.SECONDS,
							new LinkedBlockingQueue<>( 99999 ), new CallerRunsPolicy() );
				}
			}
		}
		return executor;
	}

	/**
	 * 设置消息处理线程池
	 */
	public static synchronized ExecutorService executor( ExecutorService exec ) {
		checkInitial( executor, "ExecutorService has been initialized." );
		return executor = exec;
	}

	/**
	 * 获取netty线程组
	 */
	public static NioEventLoopGroup group() {
		if ( group == null ) {
			synchronized ( DouyuConfiguration.class ) {
				if ( group == null ) {
					group = new NioEventLoopGroup();
					group.setIoRatio( 80 );
				}
			}
		}
		return group;
	}

	/**
	 * 设置netty线程组
	 */
	public static synchronized NioEventLoopGroup group( NioEventLoopGroup gro ) {
		checkInitial( group, "NioEventLoopGroup has been initialized." );
		return group = gro;
	}

	/**
	 * 获取消息类型
	 */
	public static DefaultPushMessageType defaultPullMessageType() {
		if ( defaultPullMessageType == null ) {
			synchronized ( DouyuConfiguration.class ) {
				if ( defaultPullMessageType == null ) {
					defaultPullMessageType = new DefaultPushMessageType2PullBarrage();
				}
			}
		}
		return defaultPullMessageType;
	}

	/**
	 * 获取消息类型
	 */
	public static DefaultPushMessageType defaultPushMessageType() {
		if ( defaultPushMessageType == null ) {
			synchronized ( DouyuConfiguration.class ) {
				if ( defaultPushMessageType == null ) {
					defaultPushMessageType = new DefaultPushMessageType2PushBarrage();
				}
			}
		}
		try {
			return defaultPushMessageType.clone();
		}
		catch ( CloneNotSupportedException e ) {
			throw new RuntimeException( e );
		}
	}

	/**
	 * 设置消息类型
	 */
	public static synchronized DefaultPushMessageType defaultPullMessageType( DefaultPushMessageType type ) {
		checkInitial( defaultPullMessageType, "DefaultPullMessageType has been initialized." );
		return defaultPullMessageType = type;
	}

	/**
	 * 设置消息类型
	 */
	public static synchronized DefaultPushMessageType defaultPushMessageType( DefaultPushMessageType type ) {
		checkInitial( defaultPushMessageType, "DefaultPushMessageType has been initialized." );
		return defaultPushMessageType = type;
	}

	/**
	 * 获取netty 客户端启动辅助类
	 */
	public static Bootstrap bootstrap() {
		return new Bootstrap().group( group() ).channel( NioSocketChannel.class )
				.option( ChannelOption.TCP_NODELAY, false )
				.option( ChannelOption.SO_KEEPALIVE, true )
				.option( ChannelOption.SO_REUSEADDR, false );
	}

	private static void checkInitial( Object obj, String message ) {
		if ( obj != null ) {
			throw new UnsupportedOperationException( message );
		}
	}
}
