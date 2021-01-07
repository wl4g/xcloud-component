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
package com.wl4g.component.core.boot;

import static com.wl4g.component.common.lang.ClassUtils2.isPresent
import static org.springframework.boot.context.config.ConfigFileApplicationListener.*
import com.wl4g.component.core.boot.listener.ISpringLauncherConfigurer

/**
 * Default implementation of {@link ISpringLauncherConfigurer}
 */
class DefaultSpringLauncherConfigurer implements ISpringLauncherConfigurer {

	@Override
	public int getOrder() {
		return 0
	}

	@Override
	public Properties defaultProperties() {
		def defaultProperties = new Properties();
		// Preset spring.config.name
		defaultProperties.put(CONFIG_NAME_PROPERTY, """application,
application-data,application-data-dubbo,application-data-sbf,application-data-scf,
application-facade-dubbo,application-facade-sbf,application-facade-scf,
application-web-dubbo,application-web-sbf,application-web-scf""");

		// Preset spring.config.location
		def location = new StringBuffer("classpath:/");
		if (isPresent("org.springframework.cloud.openfeign.FeignClient") && isPresent("org.springframework.cloud.openfeign.FeignAutoConfiguration")) {
			location.append(",classpath:/scf/");
		} else if (isPresent("com.wl4g.component.rpc.springboot.feign.annotation.SpringBootFeignClient")) {
			location.append(",classpath:/sbf/");
		} else if (isPresent("com.alibaba.dubbo.rpc.Filter") && isPresent("com.alibaba.boot.dubbo.autoconfigure.DubboAutoConfiguration")) {
			location.append(",classpath:/dubbo/");
		}
		defaultProperties.put(CONFIG_ADDITIONAL_LOCATION_PROPERTY, location.toString());

		return defaultProperties;
	}

}