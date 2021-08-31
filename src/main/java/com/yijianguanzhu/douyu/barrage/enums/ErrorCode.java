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
 * 远程断开连接错误码
 */
package com.yijianguanzhu.douyu.barrage.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yijianguanzhu 2020年10月27日
 */
@AllArgsConstructor
@Getter
public enum ErrorCode {

	ERROR_51(51, "通道关闭信号"), ERROR_59(59, "在其他地方登录直播间"), ERROR_207(207, "参数校验不通过"), ERROR_267(267,
			"同一个ip，账号打开的房间过多"), ERROR_4202(4202, "cookie过期"), ERROR_4207(4207, "参数校验不通过 || cookie过期");

	private int code;

	private String desc;
}
