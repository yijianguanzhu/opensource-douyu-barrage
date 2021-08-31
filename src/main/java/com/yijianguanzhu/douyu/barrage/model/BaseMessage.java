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
 * 基础信息，包括所有内容
 */
package com.yijianguanzhu.douyu.barrage.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author yijianguanzhu 2020年7月8日
 */
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BaseMessage {

	/**
	 * 消息类型
	 */
	private String type;
	/**
	 * 弹幕唯一ID
	 */
	private String cid;
	/**
	 * 弹幕组ID
	 */
	private String gid;
	/**
	 * 房间ID
	 */
	private String rid;
	/**
	 * 发送者 uid //拉取弹幕
	 */
	private String uid;
	
	/**
	 * 发送者 userid //推送弹幕
	 */
	private String userid;
	
	/**
	 * 用户昵称
	 */
	private String nn;
	/**
	 * 消息内容
	 */
	private String txt;
	/**
	 * 用户等级
	 */
	private String level;
	/**
	 * 礼物头衔：默认值 0（表示没有头衔）
	 */
	private String gt;
	/**
	 * 消息颜色：默认值 0（表示默认颜色弹幕）
	 */
	private String col;
	/**
	 * 客户端类型：默认值 0
	 */
	private String ct;

	/**
	 * 房间权限组：默认值 1（表示普通权限用户）
	 */
	private String rg;

	/**
	 * 平台权限组：默认值 1（表示普通权限用户）
	 */
	private String pg;

	/**
	 * 弹幕具体类型: 默认值 0（普通弹幕）
	 */
	private String cmt;

	/**
	 * 用户头像
	 */
	private String ic;

	/**
	 * 贵族等级
	 */
	private String nl;

	/**
	 * 贵族弹幕标识,0-非贵族弹幕,1-贵族弹幕,默认值 0
	 */
	private String nc;

	/**
	 * 粉丝牌名称
	 */
	private String bnn;

	/**
	 * 粉丝牌等级
	 */
	private String bl;

	/**
	 * 粉丝牌关联房间号
	 */
	private String brid;

	/**
	 * 徽章信息校验码
	 */
	private String hc;

	/**
	 * 主播等级
	 */
	private String ol;

	/**
	 * 是否反向弹幕标记: 0-普通弹幕，1-反向弹幕, 默认值 0
	 */
	private String rev;

	/**
	 * 是否高亮弹幕标记: 0-普通，1-高亮, 默认值 0
	 */
	private String hl;

	/**
	 * 是否粉丝弹幕标记: 0-非粉丝弹幕，1-粉丝弹幕, 默认值 0
	 */
	private String ifs;

	/**
	 * 弹幕发送时间
	 */
	private String cst;

	/**
	 * 礼物ID
	 */
	private String gfid;

	/**
	 * 礼物显示样式
	 */
	private String gs;

	/**
	 * 大礼物标识：默认值为 0（表示是小礼物）
	 */
	private String bg;

	/**
	 * 礼物个数：默认值 1（表示 1 个礼物）
	 */
	private String gfcnt;

	/**
	 * 礼物连击次数：默认值 1（表示 1 连击）
	 */
	private String hits;

	/**
	 * 抢到的鱼丸数量
	 */
	private String sl;

	/**
	 * 礼包产生者 id
	 */
	private String sid;

	/**
	 * 抢礼包者 id
	 */
	private String did;

	/**
	 * 礼包产生者昵称
	 */
	private String snk;

	/**
	 * 抢礼包者昵称
	 */
	private String dnk;

	/**
	 * 礼包类型
	 */
	private String rpt;
	/**
	 * 赠送者昵称
	 */
	private String sn;

	/**
	 * 受赠者昵称
	 */
	private String dn;

	/**
	 * 礼物名称
	 */
	private String gn;

	/**
	 * 礼物数量
	 */
	private String gc;

	/**
	 * 赠送房间
	 */
	private String drid;

	/**
	 * 是否有礼包（0-无礼包，1-有礼包）
	 */
	private String gb;

	/**
	 * 广播展现样式（1-火箭，2-飞机）
	 */
	private String es;

	/**
	 * 特效 id
	 */
	private String eid;

	/**
	 * 超级弹幕ID
	 */
	private String sdid;

	/**
	 * 跳转房间ID
	 */
	private String trid;

	/**
	 * 超级弹幕内容
	 */
	private String content;

	/**
	 * 错误代码
	 */
	private String code;

	/**
	 * 错误描述
	 */
	private String desc;

}
