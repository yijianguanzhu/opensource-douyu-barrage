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
 * 斗鱼 Cookie
 */
package com.yijianguanzhu.douyu.barrage.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yijianguanzhu 2020年10月10日
 */
@Getter
@Setter
public class DouyuCookie {

	/**
	 * 设备ID
	 */
	private String acf_did;

	/**
	 * 用户ID
	 */
	private long acf_uid;

	/**
	 * 用户名
	 */
	private String acf_username;

	/**
	 * 用户昵称
	 */
	private String acf_nickname;

	private int acf_biz;

	private long acf_ltkid;

	private String acf_auth;

	private String dy_auth;

	private String acf_stk;

	/**
	 * 头像
	 */
	private String acf_avatar;

	private String acf_cnn;
}
