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
package com.yijianguanzhu.douyu.barrage.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yijianguanzhu 2020年7月8日
 */
@Getter
@Setter
public class DefaultPushMessageType2PushBarrage extends DefaultPushMessageType {

	public DefaultPushMessageType2PushBarrage() {
		this.keeplive = "type@=keeplive/vbw@=0/cdn@=/tick@=%s/kd@=/";
		this.login = "type@=loginreq/roomid@=%d/"
				+ "dfl@=sn@AA=105@ASss@AA=1/"
				+ "username@=%s/password@=/ltkid@=%d/biz@=%d/"
				+ "stk@=%s/devid@=%s/"
				+ "/ct@=0/pt@=2/cvr@=0/tvr@=7/apd@=/"
				+ "rt@=%d/"
				+ "vk@=%s/"
				+ "ver@=20190610/aver@=218101901/dmbt@=chrome/dmbv@=86/";
		this.joinGroup = "type@=h5ckreq/rid@=%d/ti@=220120201016";
		this.pushMessage = "content@=%s/col@=3/type@=chatmessage/"
				+ "dy@=%s/"
				+ "sender@=%d/ifs@=1/nc@=0/dat@=0/"
				+ "rev@=0/admzq@=0/cst@=%d/dmt@=9/dmvv@=015c1db564";
		this.logout = "type@=logout/";
		this.gid = 1;
	}

	public String getKeeplive() {
		return String.format( this.keeplive, System.currentTimeMillis() / 1000L );
	}
}
