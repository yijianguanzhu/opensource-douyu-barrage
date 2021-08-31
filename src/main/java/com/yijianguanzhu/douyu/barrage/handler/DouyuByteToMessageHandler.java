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
 * 把msg转成 List
 */
package com.yijianguanzhu.douyu.barrage.handler;

import com.yijianguanzhu.douyu.barrage.model.BaseMessage;
import com.yijianguanzhu.douyu.barrage.utils.EncodeMessageWrapper;
import com.yijianguanzhu.douyu.barrage.utils.Message2BeanUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yijianguanzhu 2020年9月9日
 */
@Sharable
@Slf4j
public class DouyuByteToMessageHandler extends ChannelDuplexHandler {

	@Override
	public void channelRead( ChannelHandlerContext ctx, Object msg ) throws Exception {
		if ( msg instanceof ByteBuf ) {
			try {
				List<Object> attach = new ArrayList<>( 3 );
				String jsonStr = toJsonString( ( ByteBuf ) msg );
				BaseMessage baseMessage = toBaseMessage( jsonStr, ( ByteBuf ) msg );
				attach.add( msg );
				attach.add( jsonStr );
				attach.add( baseMessage );
				msg = attach;

				// 将数据输出到堆栈
				if ( log.isTraceEnabled() ) {
					log.trace( jsonStr );
				}
			}
			catch ( Exception ignore ) {
				// ignore
				log.error( "Un-Excepted Error occured. Cause By: \n", ignore );
			}
		}
		// 消息透传
		ctx.fireChannelRead( msg );
	}

	protected String toJsonString( ByteBuf msg ) {
		try {
			return Message2BeanUtil.toJsonString( msg.array() );
		}
		catch ( Exception ex ) {
			log.error( "[ByteBuf to JsonString] Un-Excepted Error occured. " +
					"\n ByteBufHexString: \n{}" +
					" \n txt: \n{}" +
					EncodeMessageWrapper.printHexString( ByteBufUtil.hexDump( msg ) ),
					new String( msg.array(), StandardCharsets.UTF_8 ) );
			throw ex;
		}
	}

	protected BaseMessage toBaseMessage( String jsonStr, ByteBuf msg ) throws Exception {
		try {
			return Message2BeanUtil.bean( jsonStr, BaseMessage.class );
		}
		catch ( Exception ex ) {
			log.error( "[JsonString to BaseMessage] Un-Excepted Error occured. " +
					"\n ByteBufHexString: \n{}" +
					" \n txt: \n{}" +
					" \n jsonString: \n{}",
					EncodeMessageWrapper.printHexString( ByteBufUtil.hexDump( msg ) ),
					new String( msg.array(), StandardCharsets.UTF_8 ), jsonStr );
			throw ex;
		}
	}
}
