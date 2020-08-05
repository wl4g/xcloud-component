/*
 * Copyright 2017 ~ 2025 the original author or authors. <wanglsir@gmail.com, 983708408@qq.com>
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
 */
package com.wl4g.components.data.config;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.wl4g.components.core.annotation.conditional.ConditionalOnJdwpDebug;
import com.wl4g.components.data.mybatis.loader.SqlSessionMapperHotspotLoader;
import com.wl4g.components.data.mybatis.loader.SqlSessionMapperHotspotLoader.HotspotLoaderProperties;

/**
 * {@link SqlSessionMapperHotspotLoader} auto configuration.
 * 
 * @author Wangl.sir <wanglsir@gmail.com, 983708408@qq.com>
 * @version v1.0 2019年11月14日
 * @since
 */
public class MybatisAutoConfiguration {

	@Bean
	@ConfigurationProperties(prefix = KEY_MYBATIS_PREFIX)
	public MybatisProperties mybatisProperties() {
		return new MybatisProperties();
	}

	// --- SqlSession hotspot loader. ---

	@Bean
	@ConditionalOnJdwpDebug(enableProperty = KEY_HOTSPOT_LOADER_PREFIX + ".enable")
	@ConfigurationProperties(prefix = KEY_HOTSPOT_LOADER_PREFIX)
	@ConditionalOnBean(SqlSessionFactoryBean.class)
	public HotspotLoaderProperties hotspotLoaderProperties() {
		return new HotspotLoaderProperties();
	}

	@Bean
	@ConditionalOnBean(value = { HotspotLoaderProperties.class })
	public SqlSessionMapperHotspotLoader sqlSessionMapperHotspotLoader(SqlSessionFactoryBean sessionFactory,
			HotspotLoaderProperties config) {
		return new SqlSessionMapperHotspotLoader(sessionFactory, config);
	}

	final public static String KEY_MYBATIS_PREFIX = "mybatis";
	final public static String KEY_HOTSPOT_LOADER_PREFIX = "spring.cloud.devops.support.devel.mybatis-loader";

}