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
package com.yijianguanzhu.douyu.barrage.codec;

import com.yijianguanzhu.douyu.barrage.utils.EncodeMessageWrapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * @author yijianguanzhu 2020年9月8日
 *
 * @deprecated Use {@link DouyuMessageToMessageCodec}
 */
@Deprecated
@Sharable
public class DouyuMessageToByteEncoder extends MessageToMessageEncoder<String> {

	/**
	 * 将消息转成douyu可识别的
	 */
	@Override
	protected void encode( ChannelHandlerContext ctx, String msg, List<Object> out ) throws Exception {
		ByteBuffer encode = EncodeMessageWrapper.encode( msg );
		ByteBuf buf = ctx.alloc().buffer().writeBytes( encode );
		BinaryWebSocketFrame frame = new BinaryWebSocketFrame( buf );
		out.add( frame );
	}
}
