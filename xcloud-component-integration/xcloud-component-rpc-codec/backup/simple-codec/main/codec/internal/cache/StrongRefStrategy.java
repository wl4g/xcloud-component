/*
 * Copyright (C) 2017 ~ 2025 the original author or authors.
 * <Wanglsir@gmail.com, 983708408@qq.com> Technology CO.LTD.
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Reference to website: http://wl4g.com
 */
package com.wl4g.component.integration.codec.internal.cache;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * StrongRefStrategy.java
 * 
 * @version 1.0.0
 * @author Wanglsir
 */
public class StrongRefStrategy implements CacheStrategy {

	private Map<Class<?>, Field[]> fieldsMap = new ConcurrentHashMap<Class<?>, Field[]>();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field[] getCacheFields(Class<?> clazz) {
		return fieldsMap.get(clazz);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putCacheFields(Class<?> clazz, Field[] fields) {
		fieldsMap.put(clazz, fields);
	}

}