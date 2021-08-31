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
package com.yijianguanzhu.douyu.barrage.enums;

import java.net.URI;

/**
 * @author yijianguanzhu 2020年10月20日
 */
public interface BaseBarrageServerConnectionAddress {

	/**
	 * 获取服务器连接URI值
	 * 
	 * @return URI
	 */
	URI getAddress();

	/**
	 * 获取下一个地址
	 * 
	 */
	BaseBarrageServerConnectionAddress next();
}
