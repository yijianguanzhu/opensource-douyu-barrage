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
 * 消息编码器
 */
package com.yijianguanzhu.douyu.barrage.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

/**
 * @author yijianguanzhu 2020年7月8日
 */
public final class EncodeMessageWrapper {

	// You can customize the message
	public static ByteBuffer encode( String msg ) {

		String sendMsg = msg + '\0';// 以 '\0'结尾
		int length = 4 + 4 + sendMsg.getBytes( StandardCharsets.UTF_8 ).length;

		ByteBuffer buf = ByteBuffer.wrap( new byte[length + 4] );// 4是额外一个消息的长度,
																															// 不计入消息长度，因此在这里添加;
		return buffer( buf, length, sendMsg );
	}

	private static ByteBuffer buffer( ByteBuffer buf, int length, String sendMsg ) {
		// 小端序
		buf.order( ByteOrder.LITTLE_ENDIAN );
		// 消息长度
		buf.putInt( length );
		// 消息长度
		buf.putInt( length );
		// 消息类型
		buf.putShort( ( short ) 689 );
		// 加密字段，默认为0
		buf.put( Byte.valueOf( ( byte ) 0 ) );
		// 保留字段，默认为0
		buf.put( Byte.valueOf( ( byte ) 0 ) );
		// 数据部分
		buf.put( sendMsg.getBytes( StandardCharsets.UTF_8 ) );
		buf.flip();
		return buf;
	}

	// 格式化打印发送字符串
	public static String printHexString( String msg ) {
		int i = 0;
		int j = 0;
		char[] cs = msg.toCharArray();
		StringBuilder builder = new StringBuilder();
		for ( char c : cs ) {
			if ( i == 4 ) {
				i = 0;
				builder.append( " " );
				j++;
			}
			if ( j == 8 ) {
				j = 0;
				builder.append( "\n" );
			}
			builder.append( c );
			i++;
		}
		return builder.toString();
	}

}
