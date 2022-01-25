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
package com.yijianguanzhu.douyu.barrage;

import com.yijianguanzhu.douyu.barrage.bootstrap.Douyu;
import com.yijianguanzhu.douyu.barrage.config.DouyuConfiguration;
import com.yijianguanzhu.douyu.barrage.enums.MessageType;
import com.yijianguanzhu.douyu.barrage.model.DouyuCookie;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author yijianguanzhu 2020年9月12日
 */
public class Main {
	public static void main( String[] args ) {
		// System.setProperty( "org.slf4j.simpleLogger.defaultLogLevel", "debug" );

		/**
		 * 拉取弹幕示例代码(不需要登陆)
		 */
		Douyu.pull().registerMessageListener( MessageType.CHAT_MSG, ( jsonString, baseMessage, context ) -> {
			StringBuilder builder = new StringBuilder();
			builder.append( "[辛巴][lv" ).append( baseMessage.getLevel() )
					.append( "][" ).append( baseMessage.getNn() )
					.append( "]: " ).append( baseMessage.getTxt() );
			System.out.println( builder.toString() );
		} ).room().roomId( 3487376L )
				.login();
		Douyu.pull().registerMessageListener( MessageType.CHAT_MSG, ( jsonString, baseMessage, context ) -> {
			StringBuilder builder = new StringBuilder();
			builder.append( "[智勋][lv" ).append( baseMessage.getLevel() )
					.append( "][" ).append( baseMessage.getNn() )
					.append( "]: " ).append( baseMessage.getTxt() );
			System.out.println( builder.toString() );
		} ).room().roomId( 312212L )
				.login();

		/**
		 * 发送弹幕/挂机直播间示例代码(需要登陆)，登录信息可从网页里的cookie获取，
		 * 也可以使用{@link https://github.com/yijianguanzhu/douyu-hongbao}工具扫码登录后，从cookie.txt文件找到相关信息
		 */
		DouyuCookie cookie = new DouyuCookie();
		// 从cookie.txt文件中找到acf_biz对应的值
		cookie.setAcf_biz( 1 );
		// 从cookie.txt文件中找到acf_devid对应的值
		cookie.setAcf_did( "xxx" );
		// 从cookie.txt文件中找到acf_ltkid对应的值
		cookie.setAcf_ltkid( 10L );
		// 从cookie.txt文件中找到acf_username对应的值
		cookie.setAcf_username( "xxxx" );
		// 从cookie.txt文件中找到acf_stk对应的值
		cookie.setAcf_stk( "xxxx" );
		// 从cookie.txt文件中找到acf_uid对应的值
		cookie.setAcf_uid( 100L );

		AtomicReference<ChannelHandlerContext> context = new AtomicReference<>();
		// 获取直播间弹幕
		Douyu.pull().registerMessageListener( MessageType.CHAT_MSG, ( jsonString, baseMessage, c ) -> {
			StringBuilder builder = new StringBuilder();
			builder.append( "[智勋][lv" ).append( baseMessage.getLevel() )
					.append( "][" ).append( baseMessage.getNn() )
					.append( "]: " ).append( baseMessage.getTxt() );
			System.out.println( builder.toString() );
			// 弹幕跟读示例，应当设置合理间隔，否则会触发短信验证
			if ( context.get() != null ) {
				context.get().writeAndFlush( String
						.format( DouyuConfiguration.defaultPushMessageType().getPushMessage(), encode( baseMessage.getTxt() ),
								cookie.getAcf_uid(), System.currentTimeMillis() ) );
			}
		} ).room().roomId( 312212L ).login();
		Douyu.push( cookie ).registerMessageListener( MessageType.ALL, ( a, b, c ) -> {
			System.out.println( a );
			context.set( c );
		} ).room().roomId( 312212L ).login();
	}

	private static String encode( String txt ) {
		if ( !txt.isEmpty() && ( txt.contains( "@" ) || txt.contains( "/" ) ) ) {
			return txt.replaceAll( "@", "@A" ).replaceAll( "/", "@S" );
		}
		return txt;
	}
}
