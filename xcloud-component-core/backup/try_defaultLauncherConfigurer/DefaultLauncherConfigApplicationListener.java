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
package com.wl4g.component.core.boot.listener;

import static com.wl4g.component.common.collection.CollectionUtils2.safeArray;
import static com.wl4g.component.common.lang.StringUtils2.isTrue;
import static com.wl4g.component.common.log.SmartLoggerFactory.getLogger;
import static java.lang.System.getProperty;

import java.util.List;
import java.util.Properties;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;
import org.springframework.util.CollectionUtils;

import com.wl4g.component.common.log.SmartLogger;

import groovy.lang.GroovyClassLoader;

/**
 * Config bootstrap application listener. Before executing
 * {@link ConfigFileApplicationListener}, in order to set the boot
 * configuration.
 * 
 * @author Wangl.sir <wanglsir@gmail.com, 983708408@qq.com>
 * @version v1.0 2020年5月20日
 * @since
 */
public class DefaultLauncherConfigApplicationListener implements GenericApplicationListener {
	protected final SmartLogger log = getLogger(getClass());

	// Notes: If you need to customize boot configuration (override this kind of
	// logic), please inherit this class and rewrite this method, and set the
	// return value to be larger.
	//
	// 注：如果需要自定义启动引导配置（覆盖此类逻辑），请继承此类并重写此方法，设置返回值大于此值即可
	@Override
	public int getOrder() {
		return DEFAULT_ORDER;
	}

	@Override
	public boolean supportsEventType(ResolvableType resolvableType) {
		return isAssignableFrom(resolvableType.getRawClass(), ApplicationStartingEvent.class);
	}

	/**
	 * Refer to {@link LoggingApplicationListener} implemention
	 */
	@Override
	public boolean supportsSourceType(Class<?> sourceType) {
		return isAssignableFrom(sourceType, SpringApplication.class, ApplicationContext.class);
	}

	/**
	 * Refer to: </br>
	 * {@link org.springframework.boot.SpringApplication#run(String)} and
	 * {@link org.springframework.boot.SpringApplicationRunListeners#starting()}
	 */
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ApplicationStartingEvent) {
			try {
				presetApplicationAdditionalProfiles((ApplicationStartingEvent) event);
				presetApplicationDefaultProperties((ApplicationStartingEvent) event);
				presetApplicationOtherProperties((ApplicationStartingEvent) event);
			} catch (Exception e) {
				throw new IllegalStateException("Cannot preset application default properties", e);
			}
		}
	}

	/**
	 * Preset defaults additional profiles.
	 * 
	 * @param event
	 */
	protected void presetApplicationAdditionalProfiles(ApplicationStartingEvent event) throws Exception {
		event.getSpringApplication().setAdditionalProfiles(safeArray(String.class, loadApplicationAdditionalProfiles(event)));
	}

	/**
	 * Preset defaults configuration properties.
	 * 
	 * @param event
	 */
	protected void presetApplicationDefaultProperties(ApplicationStartingEvent event) throws Exception {
		event.getSpringApplication().setDefaultProperties(loadApplicationDefaultProperties(event));
	}

	/**
	 * Preset defaults other properties.
	 * 
	 * @param event
	 */
	protected void presetApplicationOtherProperties(ApplicationStartingEvent event) throws Exception {
	}

	/**
	 * Load application additional profiles.
	 * 
	 * @param event
	 */
	protected String[] loadApplicationAdditionalProfiles(ApplicationStartingEvent event) {
		return null;
	}

	/**
	 * Load application defaults properties.
	 * 
	 * @param event
	 */
	protected Properties loadApplicationDefaultProperties(ApplicationStartingEvent event) {
		Properties defaultProperties = new Properties();
		defaultProperties.put("spring.main.allow-bean-definition-overriding", "true");

		// Preset addidtion config properties
		if (!isTrue(getProperty("disable.default-launcher-config"), false)) {
			Properties properties = new ConfigurableLauncherConfigurer().resolveDefaultProperties();
			// Command line arguments are preferred
			ApplicationArguments args = new DefaultApplicationArguments(event.getArgs());
			for (String argName : args.getOptionNames()) {
				List<String> values = args.getOptionValues(argName);
				if (!CollectionUtils.isEmpty(values)) {
					properties.put(argName, values);
				}
			}
			defaultProperties.putAll(properties);
		}

		log.info("Preset application default properties: {}", defaultProperties);
		return defaultProperties;
	}

	/**
	 * Check type is assignable from supportedTypes
	 * 
	 * @param type
	 * @param supportedTypes
	 * @return
	 */
	private boolean isAssignableFrom(Class<?> type, Class<?>... supportedTypes) {
		if (type != null) {
			for (Class<?> supportedType : safeArray(Class.class, supportedTypes)) {
				if (supportedType.isAssignableFrom(type)) {
					return true;
				}
			}
		}
		return false;
	}

	public static final int DEFAULT_ORDER = Ordered.HIGHEST_PRECEDENCE + 5;
}