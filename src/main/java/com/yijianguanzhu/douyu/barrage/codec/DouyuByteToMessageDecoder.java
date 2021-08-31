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
 * douyu消息读写半包处理
 */
package com.yijianguanzhu.douyu.barrage.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author yijianguanzhu 2020年9月8日
 */
@Slf4j
public class DouyuByteToMessageDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode( ChannelHandlerContext ctx, ByteBuf in, List<Object> out ) throws Exception {
		final Object decoded = decode( ctx, in );
		if ( decoded != null ) {
			out.add( decoded );
		}
	}

	// 实际解码工作
	protected Object decode( final ChannelHandlerContext ctx, final ByteBuf in ) throws Exception {
		int messageLength = getMessageLength( in );
		if ( messageLength == -1 ) {
			// 可查看是否拆包
			if ( log.isDebugEnabled() ) {
				log.debug( "netty 拆包， 消息总长度：{}，本次读取消息长度：{}", in.capacity(), messageLength );
			}
			return null;
		}

		// 可查看是否粘包
		if ( log.isDebugEnabled() ) {
			if ( messageLength - 4 != in.readableBytes() ) {
				log.debug( "netty 粘包， 消息总长度：{}，本次读取消息长度：{}", in.capacity(), messageLength );
			}
			else {
				if ( messageLength == in.capacity() ) {
					log.debug( "netty 未粘包， 消息总长度：{}，本次读取消息长度：{}", in.capacity(), messageLength );
				}
				else {
					log.debug( "netty 粘包处理完成， 消息总长度：{}，本次读取消息长度：{}", in.capacity(), messageLength );
				}
			}
		}

		// extract frame
		int readerIndex = in.readerIndex();
		/**
		 * <p>
		 * {readerIndex + 8} 去掉[消息长度 + 消息类型 + 加密字段 + 保留字段]的字节长度，，，，，，，，，，， ，，，，，这里不是 [+
		 * 12]是因为在获取消息总长度时，{@link ByteBuf}的读指针已经前进了4个字节
		 * </p>
		 * <p>
		 * {messageLength - 13} 去掉[消息长度 + 消息长度 + 消息类型 + 加密字段 + 保留字段 + '\0']的字节长度
		 * </p>
		 */
		ByteBuf frame = extractFrame( ctx, in, readerIndex + 8, messageLength - 13 );
		/**
		 * <p>
		 * 代码执行到这，说明已经获取到单条数据了，那么我们需要让{@link ByteBuf}的读指针前进到我们已读的位置，防止重复读取。
		 * </p>
		 * <p>
		 * 在获取消息总长度时，{@link ByteBuf}的读指针已经前进了4个字节 {@link ByteBuf#readIntLE()}
		 * ，所以这里需要减去4字节。
		 * </p>
		 */
		in.readerIndex( readerIndex + messageLength - 4 );
		return frame;
	}

	/**
	 * 获取单条消息总长度
	 * 
	 * @see {@link com.yijianguanzhu.douyu.barrage.utils.DecodeMessageWrapper#getMessageLength(byte[], boolean)}
	 */
	protected int getMessageLength( ByteBuf in ) {
		// 剩余可读字节数
		int remainingReadableBytes = in.readableBytes();
		if ( remainingReadableBytes < 4 ) {
			return -1;
		}

		// 标记未读取ByteBuf前的readerIndex
		in.markReaderIndex();
		int messageLength = in.readIntLE() + 4;
		// 消息拆包了，因为剩余可读字节数小于消息长度
		if ( remainingReadableBytes < messageLength ) {
			// 恢复读指针到标记位置
			in.resetReaderIndex();
			return -1;
		}
		return messageLength;
	}

	/**
	 * 获取装载真实数据的ByteBuf
	 */
	protected ByteBuf extractFrame( ChannelHandlerContext ctx, ByteBuf buffer, int index, int length ) {
		// copy direct bytebuf convert heap bytebuf.
		ByteBuf buf = buffer.retainedSlice( index, length );
		try {
			return Unpooled.copiedBuffer( buf );
		}
		finally {
			if ( buf != null ) {
				// release direct bytebuf
				buf.release();
			}
		}
	}
}
