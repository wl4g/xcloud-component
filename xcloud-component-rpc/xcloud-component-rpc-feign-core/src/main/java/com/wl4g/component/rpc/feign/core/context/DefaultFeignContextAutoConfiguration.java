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
package com.wl4g.component.rpc.feign.core.context;

import java.util.Map;
import java.util.TreeMap;

import org.springframework.context.annotation.Bean;

import com.wl4g.component.rpc.feign.core.context.RpcContextHolder;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import static java.lang.ThreadLocal.withInitial;

/**
 * Default springboot-feign {@link RpcContextHolder} auto configuration. (Both
 * the consumer side and the production side should be configured)
 * 
 * @author Wangl.sir &lt;wanglsir@gmail.com, 983708408@qq.com&gt;
 * @version v1.0 2021-01-01
 * @sine v1.0
 * @see
 */
public class DefaultFeignContextAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean // Lower priority
	public RpcContextHolder springBootFeignRpcContextHolder() {
		return new SpringBootFeignRpcContextHolder();
	}

	static class SpringBootFeignRpcContextHolder extends RpcContextHolder {
		private static final ThreadLocal<SpringBootFeignRpcContextHolder> LOCAL = withInitial(
				() -> new SpringBootFeignRpcContextHolder());

		// Notes: Since feignclient ignores case when setting header, it should
		// be
		// unified here.
		/**
		 * Feign request context attachments store. Thread isolation, thread
		 * safety
		 */
		private final Map<String, String> attachments = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

		@Override
		public String getAttachment(String key) {
			return attachments.get(key);
		}

		@Override
		public Map<String, String> getAttachments() {
			return attachments;
		}

		@Override
		public void setAttachment(String key, String value) {
			attachments.put(key, value);
		}

		@Override
		public void removeAttachment(String key) {
			attachments.remove(key);
		}

		@Override
		public void clearAttachments() {
			attachments.clear();
		}

		@Override
		protected RpcContextHolder current() {
			return LOCAL.get();
		}
	}

}
