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

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yijianguanzhu 2020年7月8日
 */
@Getter
@Setter
public class DefaultPushMessageType2PushBarrage extends DefaultPushMessageType {

	public static final SimpleDateFormat SDF = new SimpleDateFormat( "yyyyMMdd" );

	public DefaultPushMessageType2PushBarrage() {
		// 2026-10 依据斗鱼网页最新版抓包更新：
		// 老格式(type@=keeplive/vbw@=0/cdn@=/tick@=%s/kd@=/)服务器已不再响应，连接约30秒后会被断开；
		// 新格式需携带 alg_ver/key_ver 字段，sig 校验值经实测可留空。
		this.keeplive = "type@=keeplive/vbw@=0/cdn@=/tick@=%s/kd@=/alg_ver@=1.0/key_ver@=1.1/sig@=/";
		// 新版登录消息：新增 dfl/cvr/tvr/apd/jwt 字段，协议版本号 ver@=20220825、aver@=218101901
		this.login = "type@=loginreq/roomid@=%d/dfl@=/username@=%s/password@=/ltkid@=%d/biz@=%d/"
				+ "stk@=%s/devid@=%s/"
				+ "ct@=0/pt@=2/cvr@=0/tvr@=7/apd@=/jwt@=/rt@=%d/vk@=%s/"
				+ "ver@=20220825/aver@=218101901/dmbt@=chrome/dmbv@=130/";
		this.joinGroup = "type@=h5ckreq/rid@=%d/ti@=2501" + SDF.format( new Date() );
		// 新版弹幕消息：新增 pe(表情)/dy(设备ID)/tts 字段，cst 为毫秒时间戳(13位)
		this.pushMessage = "pe@=0/content@=%s/col@=0/type@=chatmessage/"
				+ "dy@=%s/sender@=%d/ifs@=0/nc@=0/dat@=0/"
				+ "rev@=0/tts@=0/admzq@=0/cst@=%d/";
		this.logout = "type@=logout/";
		this.gid = 1;
	}

	public String getKeeplive() {
		return String.format( this.keeplive, System.currentTimeMillis() / 1000L );
	}
}
