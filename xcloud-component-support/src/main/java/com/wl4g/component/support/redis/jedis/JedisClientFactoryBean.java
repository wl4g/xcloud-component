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
package com.wl4g.component.support.redis.jedis;

import static com.wl4g.component.common.collection.CollectionUtils2.safeList;
import static com.wl4g.component.common.lang.Assert2.notEmpty;
import static com.wl4g.component.common.lang.Assert2.notNull;
import static com.wl4g.component.common.lang.Assert2.notNullOf;
import static com.wl4g.component.common.log.SmartLoggerFactory.getLogger;
import static java.lang.String.format;
import static java.util.Objects.nonNull;

import java.util.Set;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import com.wl4g.component.common.log.SmartLogger;
import com.wl4g.component.support.redis.jedis.cluster.JedisClusterJedisClient;
import com.wl4g.component.support.redis.jedis.JedisClientAutoConfiguration.JedisProperties;
import com.wl4g.component.support.redis.jedis.cluster.ConfigurableJedisClusterJedisClient;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

/**
 * Bean factory of {@link JedisClient}.
 */
public class JedisClientFactoryBean implements FactoryBean<JedisClient>, InitializingBean {
	protected final SmartLogger log = getLogger(getClass());

	/**
	 * {@link JedisProperties}
	 */
	protected final JedisProperties config;

	/**
	 * {@link JedisCluster}
	 */
	protected final JedisCluster jedisCluster;

	/**
	 * {@link JedisPool}
	 */
	protected final JedisPool jedisPool;

	/**
	 * {@link JedisClient}
	 */
	private JedisClient jedisClient;

	public JedisClientFactoryBean(JedisCluster jedisCluster, JedisPool jedisPool) {
		this.config = null;
		this.jedisCluster = notNullOf(jedisCluster, "jedisCluster");
		this.jedisPool = notNullOf(jedisPool, "jedisPool");
	}

	public JedisClientFactoryBean(JedisProperties config) {
		this.config = notNullOf(config, "jedisProperties");
		this.jedisCluster = null;
		this.jedisPool = null;
	}

	public JedisClientFactoryBean(JedisProperties config, JedisCluster jedisCluster, JedisPool jedisPool) {
		// notNullOf(config, "jedisProperties");
		// notNullOf(jedisCluster, "jedisCluster");
		// notNullOf(jedisPool, "jedisPool");
		this.config = config;
		this.jedisCluster = jedisCluster;
		this.jedisPool = jedisPool;
	}

	@Override
	public JedisClient getObject() throws Exception {
		return jedisClient;
	}

	@Override
	public Class<?> getObjectType() {
		return JedisClient.class;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		init();
		log.info("Instantiated jedis client: {}", jedisClient);
	}

	/**
	 * Build a {@link JedisClient} with existing jedis or via configuration.
	 * 
	 * @throws Exception
	 */
	private void init() throws Exception {
		// Wrapper with existing jedis.
		if (nonNull(jedisCluster)) {
			jedisClient = new JedisClusterJedisClient(jedisCluster);
			log.info("Instantiated JedisClient: {} via existing JedisCluster: {}", jedisClient, jedisCluster);
		} else if (nonNull(jedisPool)) {
			jedisClient = new StandaloneJedisClient(jedisPool, false);
			log.info("Instantiated JedisClient: {} via existing JedisPool: {}", jedisClient, jedisPool);
		}
		// New instantiate via configuration.
		else {
			notNull(config,
					"Cannot to automatically instantiate the %s. One of %s, %s and %s, expected at least 1 bean which qualifies as autowire candidate",
					JedisClient.class.getSimpleName(), JedisPool.class.getSimpleName(), JedisCluster.class.getSimpleName(),
					JedisProperties.class.getSimpleName());
			createWithConfiguration(config);
		}
	}

	/**
	 * New instantiate {@link JedisClient} via {@link JedisProperties}
	 * 
	 * @param config
	 * @throws Exception
	 */
	private void createWithConfiguration(JedisProperties config) throws Exception {
		Set<HostAndPort> nodes = config.parseHostAndPort();
		notEmpty(nodes, "Redis nodes configuration is requires, must contain at least 1 node");
		// nodes.forEach(n -> log.info("Connecting to redis node: {}", n));
		log.info("Connecting to redis nodes..., config: {}", config.toString());

		try {
			// Nodes config is cluster?
			if (safeList(config.getNodes()).size() > 1) {// auto
				jedisClient = new ConfigurableJedisClusterJedisClient(nodes, config.getConnTimeout(), config.getSoTimeout(),
						config.getMaxAttempts(), config.getPasswd(), config.getPoolConfig(), config.isSafeMode());
			} else { // standalone
				HostAndPort hap = nodes.iterator().next();
				JedisPool pool = new JedisPool(config.getPoolConfig(), hap.getHost(), hap.getPort(), config.getConnTimeout(),
						config.getSoTimeout(), config.getPasswd(), 0, config.getClientName(), false, null, null, null);
				jedisClient = new StandaloneJedisClient(pool, config.isSafeMode());
			}
			log.info("Instantiated jedis client via configuration. {}", jedisClient);
		} catch (Exception e) {
			throw new IllegalStateException(format("Cannot connect to redis nodes: %s", nodes), e);
		}
	}

}
