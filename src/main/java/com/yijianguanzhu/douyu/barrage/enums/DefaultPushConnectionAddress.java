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
 * 斗鱼弹幕服务器默认推送地址
 */
package com.yijianguanzhu.douyu.barrage.enums;

import lombok.AllArgsConstructor;

import java.net.URI;

/**
 * @author yijianguanzhu 2020年10月10日
 */
@AllArgsConstructor
public enum DefaultPushConnectionAddress implements BasePushBarrageServerConnectionAddress {
	/**
	 * 6671推送地址
	 */
	ADDRESS_6671(URI.create( "wss://wsproxy.douyu.com:6671/" )),
	/**
	 * 6672推送地址
	 */
	ADDRESS_6672(URI.create( "wss://wsproxy.douyu.com:6672/" )),
	/**
	 * 6673推送地址
	 */
	ADDRESS_6673(URI.create( "wss://wsproxy.douyu.com:6673/" )),
	/**
	 * 6674推送地址
	 */
	ADDRESS_6674(URI.create( "wss://wsproxy.douyu.com:6674/" )),
	/**
	 * 6675推送地址
	 */
	ADDRESS_6675(URI.create( "wss://wsproxy.douyu.com:6675/" ));

	private URI address;

	@Override
	public URI getAddress() {
		return this.address;
	}
}
