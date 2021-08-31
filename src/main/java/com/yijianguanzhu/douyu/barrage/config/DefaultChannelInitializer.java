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
 * 默认的链路处理器
 */
package com.yijianguanzhu.douyu.barrage.config;

import com.yijianguanzhu.douyu.barrage.codec.DouyuByteToMessageDecoder;
import com.yijianguanzhu.douyu.barrage.codec.DouyuMessageToMessageCodec;
import com.yijianguanzhu.douyu.barrage.handler.DouyuByteToMessageHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolConfig;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.net.URI;

/**
 * @author yijianguanzhu 2020年9月12日
 */
public class DefaultChannelInitializer extends ChannelInitializer<SocketChannel> {

	private URI uri;
	private WebSocketClientProtocolConfig config;
	private boolean isSsl;
	private static final DouyuMessageToMessageCodec CODEC = new DouyuMessageToMessageCodec();
	private static final DouyuByteToMessageHandler HANDLER = new DouyuByteToMessageHandler();

	public DefaultChannelInitializer( URI uri ) {
		this( uri, true );
	}

	public DefaultChannelInitializer( URI uri, boolean isSsl ) {
		this.uri = uri;
		this.config = WebSocketClientProtocolConfig.newBuilder()
				.webSocketUri( this.uri )
				.absoluteUpgradeUrl( true )
				.handshakeTimeoutMillis( 5000L )
				.build();
		this.isSsl = isSsl;
	}

	@Override
	protected void initChannel( SocketChannel ch ) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		if ( this.isSsl ) {
			SSLContext sslContext = SSLContext.getInstance( "TLSv1.2" );
			sslContext.init( null, null, null );
			SSLEngine engine = sslContext.createSSLEngine();
			engine.setUseClientMode( true );
			pipeline.addLast( new SslHandler( engine ) );
		}
		pipeline.addLast( new HttpClientCodec() );
		pipeline.addLast( new HttpObjectAggregator( 65536 ) );
		pipeline.addLast( new WebSocketClientProtocolHandler( this.config ) );
		pipeline.addLast( CODEC );
		pipeline.addLast( new DouyuByteToMessageDecoder() );
		pipeline.addLast( HANDLER );
	}
}
