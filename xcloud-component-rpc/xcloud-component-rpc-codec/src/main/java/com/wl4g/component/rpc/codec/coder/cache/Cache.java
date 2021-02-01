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
package com.wl4g.component.rpc.codec.coder.cache;

import java.lang.reflect.Field;

import static com.wl4g.component.common.log.SmartLoggerFactory.getLogger;
import com.wl4g.component.common.log.SmartLogger;
import com.wl4g.component.rpc.codec.CodecConfig;
import com.wl4g.component.rpc.codec.CodecConfig.CacheType;
import com.wl4g.component.rpc.codec.helper.EnvHelper;
import com.wl4g.component.rpc.codec.helper.StringHelper;

/**
 * Indicate thar cache object fields and other data.
 * 
 * Cache.java
 * 
 * @version 1.0.0
 * @author Wanglsir
 */
public class Cache {

	private static final SmartLogger log = getLogger(Cache.class);

	private static Cache globalCache;

	private CacheStrategy strategy;

	/**
	 * Create or get cache object by configuration.
	 * 
	 * @param cfg
	 *            Codec configuration
	 * @return Cache object.
	 */
	public static Cache getCache(CodecConfig cfg) {
		if (cfg.getCacheType() == CacheType.GLOBAL) {
			return getGlobalCache();
		} else if (cfg.getCacheType() == CacheType.LOCAL) {
			return getLocalCache();
		} else {
			return null;
		}
	}

	private static synchronized Cache getGlobalCache() {
		if (log.isDebugEnabled()) {
			log.debug("Get codec cache from global cache.");
		}
		if (globalCache == null) {
			globalCache = new Cache();
		}
		return globalCache;
	}

	private static Cache getLocalCache() {
		if (log.isDebugEnabled()) {
			log.debug("Get codec cache from local cache.");
		}
		return new Cache();
	}

	private Cache() {
		if (EnvHelper.isAndroidEnv()) {
			strategy = new StrongRefStrategy();
		} else {
			strategy = new SoftRefStrategy();
		}
	}

	/**
	 * Get object's fields from cache.
	 * 
	 * @param clazz
	 *            Target class.
	 * @return Fields arrays.
	 */
	public Field[] getCacheFields(Class<?> clazz) {
		if (log.isDebugEnabled()) {
			log.debug(StringHelper.buffer("Get fields from cache:", strategy, " for ", clazz));
		}
		return strategy.getCacheFields(clazz);
	}

	/**
	 * Put object's fields to cache.
	 * 
	 * @param clazz
	 *            Target class
	 * @param fields
	 *            Class's mapping fields.
	 */
	public void putCacheFields(Class<?> clazz, Field[] fields) {
		if (log.isDebugEnabled()) {
			log.debug(StringHelper.buffer("Put fields to cache:", strategy, " for ", clazz));
		}
		strategy.putCacheFields(clazz, fields);
	}

}