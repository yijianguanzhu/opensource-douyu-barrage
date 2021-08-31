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
 * 消息解码器
 */
package com.yijianguanzhu.douyu.barrage.utils;

import com.yijianguanzhu.douyu.barrage.model.Byte;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.LinkedList;
import java.util.List;

/**
 * @author yijianguanzhu 2020年7月8日
 *
 * @deprecated Use {@link com.yijianguanzhu.douyu.barrage.codec.DouyuByteToMessageDecoder}
 */
@Deprecated
public final class DecodeMessageWrapper {

	/**
	 * 获取单条总数据长度,可用于识别是否粘包。
	 */
	public static int getMessageLength( byte[] src, boolean perfect ) {

		/**
		 * <p>
		 * 1.如果字节数小于4，说明连消息长度的数据也被拆包了，没办法直接判断 单条数据长度，
		 * 那么我们直接返回-1，告诉调用者，你可以直接把这条数据加入粘包记录器里， 等待下一次数据到来时，你再拼接起来。
		 * 2.但是这通常来说很难发生(条件比较苛刻)，至少我在拉取弹幕时，没见过。
		 * 3.如果不要求精准处理每条消息，那么当数据恰好满足1的条件时，将会抛出异常。
		 * </p>
		 */
		if ( perfect && src.length < 4 ) {
			return -1;
		}

		ByteBuffer buffer = ByteBuffer.wrap( new byte[4] );
		buffer.order( ByteOrder.LITTLE_ENDIAN );
		buffer.put( src, 0, 4 );
		buffer.flip();
		return buffer.getInt() + 4;
	}

	// perfect
	public static List<ByteBuffer> decode( byte[] src, Byte byt ) {

		final List<ByteBuffer> list = new LinkedList<ByteBuffer>();
		if ( byt.getBytes() != null ) {
			byte[] newSrc = new byte[byt.getBytes().length + src.length];
			System.arraycopy( byt.getBytes(), 0, newSrc, 0, byt.getBytes().length );
			System.arraycopy( src, 0, newSrc, byt.getBytes().length, src.length );
			src = newSrc;
			byt.setBytes( null );
		}
		while ( true ) {
			int totalLength = src.length;
			int msgLength = 0;
			if ( ( msgLength = getMessageLength( src, true ) ) == -1 ) {
				// 将消息加入粘包记录器
				byt.setBytes( src );
				break;
			}
			else {
				/**
				 * <p>
				 * 1.判断这条消息是否是完整的， 如：出现 "@type@=mrk"时，这条消息依然不完整，因此我们需要将它加入粘包寄存器。
				 * 2.怎么判断？如果当前消息长度小于求得的消息长度，那么可以判断当前消息是不完整的消息。
				 * </p>
				 * 
				 */
				if ( totalLength < msgLength ) {
					byt.setBytes( src );
					break;
				}
			}
			if ( ( src = byteBufHandler( list, msgLength, src, totalLength ) ) == null ) {
				break;
			}
		}
		return list;
	}

	// imperfect
	public static List<ByteBuffer> decode( byte[] src ) {

		final List<ByteBuffer> list = new LinkedList<ByteBuffer>();
		while ( true ) {
			int totalLength = src.length;
			// 可能会抛异常，那么这条数据将丢失。
			int msgLength = getMessageLength( src, false );
			if ( ( src = byteBufHandler( list, msgLength, src, totalLength ) ) == null ) {
				break;
			}

		}
		return list;
	}

	private static byte[] byteBufHandler( List<ByteBuffer> list, int msgLength, byte[] src, int totalLength ) {

		// 去掉[消息长度 + 消息长度 + 消息类型 + 加密字段 + 保留字段 + '\0']的字节长度
		int newArrLength = msgLength - 13;
		ByteBuffer buffer = ByteBuffer.wrap( new byte[newArrLength] );
		buffer.order( ByteOrder.LITTLE_ENDIAN );
		buffer.put( src, 12, newArrLength );
		buffer.flip();
		list.add( buffer );

		// 有剩余（粘包了）
		if ( ( totalLength - msgLength ) > 0 ) {
			byte[] old = src;
			src = new byte[totalLength - msgLength];
			System.arraycopy( old, msgLength, src, 0, src.length );
			return src;
		}

		// 没有剩余了
		return null;
	}
}
