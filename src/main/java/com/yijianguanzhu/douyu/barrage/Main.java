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
import com.yijianguanzhu.douyu.barrage.enums.MessageType;

/**
 * @author yijianguanzhu 2020年9月12日
 */
public class Main {
	public static void main( String[] args ) {
		// System.setProperty( "org.slf4j.simpleLogger.defaultLogLevel", "debug" );
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
			builder.append( "[余小C][lv" ).append( baseMessage.getLevel() )
					.append( "][" ).append( baseMessage.getNn() )
					.append( "]: " ).append( baseMessage.getTxt() );
			System.out.println( builder.toString() );
		} ).room().roomId( 1126960L )
				.login();
		Douyu.pull().registerMessageListener( MessageType.CHAT_MSG, ( jsonString, baseMessage, context ) -> {
			StringBuilder builder = new StringBuilder();
			builder.append( "[智勋][lv" ).append( baseMessage.getLevel() )
					.append( "][" ).append( baseMessage.getNn() )
					.append( "]: " ).append( baseMessage.getTxt() );
			System.out.println( builder.toString() );
		} ).room().roomId( 312212L )
				.login();
		Douyu.pull().registerMessageListener( MessageType.CHAT_MSG, ( jsonString, baseMessage, context ) -> {
			StringBuilder builder = new StringBuilder();
			builder.append( "[基伦][lv" ).append( baseMessage.getLevel() )
					.append( "][" ).append( baseMessage.getNn() )
					.append( "]: " ).append( baseMessage.getTxt() );
			System.out.println( builder.toString() );
		} ).room().roomId( 149985L )
				.login();
		Douyu.pull().registerMessageListener( MessageType.CHAT_MSG, ( jsonString, baseMessage, context ) -> {
			StringBuilder builder = new StringBuilder();
			builder.append( "[芜湖大司马][lv" ).append( baseMessage.getLevel() )
					.append( "][" ).append( baseMessage.getNn() )
					.append( "]: " ).append( baseMessage.getTxt() );
			System.out.println( builder.toString() );
		} ).room().roomId( 606118L )
				.login();
		Douyu.pull().registerMessageListener( MessageType.CHAT_MSG, ( jsonString, baseMessage, context ) -> {
			StringBuilder builder = new StringBuilder();
			builder.append( "[王纪超][lv" ).append( baseMessage.getLevel() )
					.append( "][" ).append( baseMessage.getNn() )
					.append( "]: " ).append( baseMessage.getTxt() );
			System.out.println( builder.toString() );
		} ).room().roomId( 3567314L )
				.login();
		Douyu.pull().registerMessageListener( MessageType.CHAT_MSG, ( jsonString, baseMessage, context ) -> {
			StringBuilder builder = new StringBuilder();
			builder.append( "[兰林汉][lv" ).append( baseMessage.getLevel() )
					.append( "][" ).append( baseMessage.getNn() )
					.append( "]: " ).append( baseMessage.getTxt() );
			System.out.println( builder.toString() );
		} ).room().roomId( 475252L )
				.login();
	}
}
