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

import lombok.AllArgsConstructor;

/**
 * @author yijianguanzhu 2020年7月8日
 */
@AllArgsConstructor
public enum MessageType implements BaseMessageTypeEnum {

	// 接收所有消息
	ALL("all"),

	// 登录响应
	LOGIN_RES("loginres"),

	// 服务要求心跳
	PING_REQ("pingreq"),

	// 服务心跳响应
	KEEPLIVE("mrkl"),

	KEEP_LIVE("keeplive"),

	// 读取普通弹幕消息
	CHAT_MSG("chatmsg"),

	// 推送弹幕消息
	CHAT_MESSAGE("chatmessage"),

	// 领取鱼丸暴击
	ONLINE_GIFT("onlinegift"),

	// 赠送礼物消息
	DGB("dgb"),

	// 用户进入房间
	UENTER("uenter"),

	// 房间开播关播
	RSS("rss"),

	// 房间贡献排行榜更新广播
	RANK_LIST("ranklist"),

	// 超级弹幕
	SSD("ssd"),

	// 房间内礼物广播
	SPBC("spbc"),

	// 房间用户抢红包
	GGBB("ggbb"),

	// 房间分区排名变化消息
	RANK_UP("rankup"),

	// 鱼丸竞猜
	ERQUIZISN("erquizisn"),

	// 未知，会返回json字符串，特殊处理
	GBROADCAST("gbroadcast"),

	// 拉取弹幕地址。
	MSG_REPEATER_PROXY_LIST("msgrepeaterproxylist"),

	SCVF("scvf"),

	H5GKCRES("h5gkcres"),

	// 错误信息
	ERROR("error");

	private String type;

	@Override
	public String getType() {
		return this.type;
	}

}
