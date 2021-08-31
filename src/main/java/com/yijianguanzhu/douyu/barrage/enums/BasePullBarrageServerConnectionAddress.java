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
 * 拉取弹幕链接地址
 */
package com.yijianguanzhu.douyu.barrage.enums;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yijianguanzhu 2020年9月25日
 */
public interface BasePullBarrageServerConnectionAddress extends BaseBarrageServerConnectionAddress {

	BasePullBarrageServerConnectionAddress PULL_ADDRESS = () -> null;

	// 地址读指针
	AtomicInteger READERINDEX = new AtomicInteger( 0 );

	// 所有地址
	List<BasePullBarrageServerConnectionAddress> ADDRESSS = new CopyOnWriteArrayList<>();

	/**
	 * 获取下一个地址
	 * 
	 * <p>
	 * 使用均衡策略
	 * </p>
	 */
	default BaseBarrageServerConnectionAddress next() {
		if ( ADDRESSS.isEmpty() ) {
			Collections.addAll( ADDRESSS, DefaultPullConnectionAddress.values() );
		}
		return ADDRESSS.get( READERINDEX.getAndUpdate( operand -> {
			if ( operand >= ADDRESSS.size() ) {
				return operand % ADDRESSS.size() + 1;
			}
			return operand + 1;
		} ) % ADDRESSS.size() );
	}
}
