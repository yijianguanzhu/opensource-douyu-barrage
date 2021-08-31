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
 * 斗鱼弹幕服务器默认连接地址
 */
package com.yijianguanzhu.douyu.barrage.enums;

import lombok.AllArgsConstructor;

import java.net.URI;

/**
 * @author yijianguanzhu 2020年9月25日
 */
@AllArgsConstructor
public enum DefaultPullConnectionAddress implements BasePullBarrageServerConnectionAddress {
	/**
	 * 8501连接地址
	 */
	ADDRESS_8501(URI.create( "wss://danmu.douyu.com:8501/" )),
	/**
	 * 8502连接地址
	 */
	ADDRESS_8502(URI.create( "wss://danmu.douyu.com:8502/" )),
	/**
	 * 8503连接地址
	 */
	ADDRESS_8503(URI.create( "wss://danmu.douyu.com:8503/" )),
	/**
	 * 8504连接地址
	 */
	ADDRESS_8504(URI.create( "wss://danmu.douyu.com:8504/" )),
	/**
	 * 8505连接地址
	 */
	ADDRESS_8505(URI.create( "wss://danmu.douyu.com:8505/" )),
	/**
	 * 8506连接地址
	 */
	ADDRESS_8506(URI.create( "wss://danmu.douyu.com:8506/" ));

	private URI address;

	@Override
	public URI getAddress() {
		return this.address;
	}
}
