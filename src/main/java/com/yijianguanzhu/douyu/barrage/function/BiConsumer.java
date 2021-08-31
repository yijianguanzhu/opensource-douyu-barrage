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
package com.yijianguanzhu.douyu.barrage.function;

import java.util.Objects;

/**
 * @author yijianguanzhu 2020年10月20日
 */
@FunctionalInterface
public interface BiConsumer<T, U, R> {

  /**
   * Performs this operation on the given arguments.
   *
   * @param t the first input argument
   * @param u the second input argument
   * @param r the third input argument
   */
	void accept( T t, U u, R r );

  /**
   * Returns a composed {@code BiConsumer} that performs, in sequence, this
   * operation followed by the {@code after} operation. If performing either
   * operation throws an exception, it is relayed to the caller of the
   * composed operation.  If performing this operation throws an exception,
   * the {@code after} operation will not be performed.
   *
   * @param after the operation to perform after this operation
   * @return a composed {@code BiConsumer} that performs in sequence this
   * operation followed by the {@code after} operation
   * @throws NullPointerException if {@code after} is null
   */
	default BiConsumer<T, U, R> andThen( BiConsumer<? super T, ? super U, ? super R> after ) {
		Objects.requireNonNull( after );

		return ( t, u, r ) -> {
			accept( t, u, r );
			after.accept( t, u, r );
		};
	}
}
