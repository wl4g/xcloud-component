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
package com.wl4g.component.data.cache;

import static com.wl4g.component.common.lang.Assert2.notNullOf;
import static com.wl4g.component.common.lang.TypeConverts.safeLongToInt;

import com.wl4g.component.support.redis.jedis.JedisService;

/**
 * {@link JedisDataCache}
 * 
 * @author Wangl.sir &lt;wanglsir@gmail.com, 983708408@qq.com&gt;
 * @version v1.0 2021-05-02
 * @sine v1.0
 * @see
 */
public class JedisDataCache implements IDataCache {

	protected final JedisService jedisService;

	public JedisDataCache(JedisService jedisService) {
		this.jedisService = notNullOf(jedisService, "jedisService");
	}

	@Override
	public <T> T get(String key, Class<T> valueType) {
		return jedisService.getObjectT(key, valueType);
	}

	@Override
	public void put(String key, Object value) {
		jedisService.setObjectT(key, value, 0);
	}

	@Override
	public void put(String key, Object value, long expireMs) {
		jedisService.setObjectT(key, value, safeLongToInt(expireMs));
	}

}
