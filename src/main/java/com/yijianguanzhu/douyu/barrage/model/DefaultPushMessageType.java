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
 * 斗鱼弹幕连接器的默认推送消息
 */
package com.yijianguanzhu.douyu.barrage.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yijianguanzhu 2020年7月8日
 */
@Getter
@Setter
public class DefaultPushMessageType implements Cloneable {

	protected String keeplive;
	protected String login;
	protected String joinGroup;
	protected String logout;
	protected String pushMessage;
	protected int gid;

	@Override
	public DefaultPushMessageType clone() throws CloneNotSupportedException {
		return ( DefaultPushMessageType ) super.clone();
	}
}
