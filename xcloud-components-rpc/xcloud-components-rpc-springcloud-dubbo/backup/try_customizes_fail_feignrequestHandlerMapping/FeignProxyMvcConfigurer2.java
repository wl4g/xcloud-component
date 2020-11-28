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
package com.wl4g.components.rpc.springcloud.feign;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.ConversionService;
import static org.springframework.core.annotation.AnnotatedElementUtils.hasAnnotation;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestAttributeMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestPartMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ServletCookieValueMethodArgumentResolver;
import org.springframework.web.util.UriComponentsBuilder;

import com.wl4g.components.core.framework.HierarchyParameterNameDiscoverer;
import com.wl4g.components.core.web.mapping.SmartHandlerMapping;

import static com.wl4g.components.rpc.springcloud.feign.FeignProxyMvcConfigurer2.FeignProxyRequestHandlerMapping.ORDER_VALUE;

import static com.wl4g.components.rpc.springcloud.util.FeignDubboUtils.isFeignProxyBean;

/**
 * {@link FeignProxyMvcConfigurer}
 * 
 * @author Wangl.sir &lt;wanglsir@gmail.com, 983708408@qq.com&gt;
 * @version v1.0 2019-11-20
 * @sine v1.0
 * @see Thannks refer: https://gitee.com/leecho/spring-cloud-feign-proxy
 */
public class FeignProxyMvcConfigurer2 implements InitializingBean {

	@Autowired
	private RequestMappingHandlerAdapter adapter;

	@Autowired
	private ConfigurableBeanFactory beanFactory;

	@Bean
	public FeignProxyRequestHandlerMapping feignProxyHandlerMapping() {
		return new FeignProxyRequestHandlerMapping();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		configureMethodArgumentResolvers();
	}

	/**
	 * Configuring handler method argument resolvers configuration.
	 * 
	 * @see {@link org.springframework.web.method.support.InvocableHandlerMethod#getMethodArgumentValues()}
	 */
	public void configureMethodArgumentResolvers() {
		List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>(adapter.getArgumentResolvers());

		// Supported for @PathVariable
		resolvers.add(0, new PathVariableMethodArgumentResolver() {
			@Override
			public boolean supportsParameter(MethodParameter parameter) {
				return super.supportsParameter(useInterfaceDefintionMethodParameter(parameter, PathVariable.class));
			}

			@Override
			protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
				return super.createNamedValueInfo(useInterfaceDefintionMethodParameter(parameter, PathVariable.class));
			}

			@Override
			public void contributeMethodArgument(MethodParameter parameter, Object value, UriComponentsBuilder builder,
					Map<String, Object> uriVariables, ConversionService conversionService) {
				super.contributeMethodArgument(useInterfaceDefintionMethodParameter(parameter, PathVariable.class), value,
						builder, uriVariables, conversionService);
			}
		});

		// Supported for @RequestHeader
		resolvers.add(0, new RequestHeaderMethodArgumentResolver(beanFactory) {
			@Override
			public boolean supportsParameter(MethodParameter parameter) {
				return super.supportsParameter(useInterfaceDefintionMethodParameter(parameter, RequestHeader.class));
			}

			@Override
			protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
				return super.createNamedValueInfo(useInterfaceDefintionMethodParameter(parameter, RequestHeader.class));
			}
		});

		// Supported for @CookieValue
		resolvers.add(0, new ServletCookieValueMethodArgumentResolver(beanFactory) {
			@Override
			public boolean supportsParameter(MethodParameter parameter) {
				return super.supportsParameter(useInterfaceDefintionMethodParameter(parameter, CookieValue.class));
			}

			@Override
			protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
				return super.createNamedValueInfo(useInterfaceDefintionMethodParameter(parameter, CookieValue.class));
			}
		});

		// Supported for @RequestBody/@Valid
		resolvers.add(0, new RequestResponseBodyMethodProcessor(adapter.getMessageConverters()) {
			@Override
			public boolean supportsParameter(MethodParameter parameter) {
				return super.supportsParameter(useInterfaceDefintionMethodParameter(parameter, RequestBody.class));
			}

			@Override
			protected void validateIfApplicable(WebDataBinder binder, MethodParameter methodParam) {
				super.validateIfApplicable(binder, useInterfaceDefintionMethodParameter(methodParam, Valid.class));
			}
		});

		// Supported for @RequestPart
		resolvers.add(0, new RequestPartMethodArgumentResolver(adapter.getMessageConverters()) {
			@Override
			public boolean supportsParameter(MethodParameter parameter) {
				return super.supportsParameter(useInterfaceDefintionMethodParameter(parameter, RequestPart.class));
			}

			@Override
			public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest request,
					WebDataBinderFactory binderFactory) throws Exception {
				return super.resolveArgument(useInterfaceDefintionMethodParameter(parameter, RequestPart.class), mavContainer,
						request, binderFactory);
			}
		});

		// Supported for @RequestParam
		resolvers.add(0, new RequestParamMethodArgumentResolver(beanFactory, true) {
			@Override
			public boolean supportsParameter(MethodParameter parameter) {
				return super.supportsParameter(useInterfaceDefintionMethodParameter(parameter, RequestParam.class));
			}
		});

		// Supported for @RequestAttribute
		resolvers.add(0, new RequestAttributeMethodArgumentResolver() {
			@Override
			public boolean supportsParameter(MethodParameter parameter) {
				return super.supportsParameter(useInterfaceDefintionMethodParameter(parameter, RequestAttribute.class));
			}
		});

		adapter.setArgumentResolvers(resolvers);
	}

	/**
	 * Wrap handler method parameter with interface class.
	 * 
	 * @param parameter
	 *            Proxyed method parameter, implements of
	 *            {@link FeignProxyController}
	 * @param annotationType
	 * @return
	 */
	private final MethodParameter useInterfaceDefintionMethodParameter(MethodParameter parameter,
			Class<? extends Annotation> annotationType) {
		if (!parameter.hasParameterAnnotation(annotationType)) {
			parameter.initParameterNameDiscovery(HierarchyParameterNameDiscoverer.DEFAULT);
		}
		return parameter;
	}

	/**
	 * {@link FeignProxyRequestHandlerMapping}
	 * 
	 * @author Wangl.sir &lt;wanglsir@gmail.com, 983708408@qq.com&gt;
	 * @version v1.0 2020-11-26
	 * @sine v1.0
	 * @see
	 */
	@Order(ORDER_VALUE)
	public static class FeignProxyRequestHandlerMapping extends RequestMappingHandlerMapping implements SmartHandlerMapping {

		@Override
		public void afterPropertiesSet() {
			System.out.println(11);
			System.out.println(11);
			System.out.println(11);
		}

		@Override
		public boolean isSupport(String beanName, Class<?> beanType) {
			// In order to solve the following problems, spring will scan
			// the @RequestMapping annotation class by default and inject
			// it into MapperRegistry, which will cause conflicts with the
			// dynamically generated $FeignProxyController class.
			if (hasAnnotation(beanType, FeignClient.class)) {
				return isFeignProxyBean(beanName);
			}
			return false;
		}

		@Override
		protected void detectHandlerMethods(Object handler) {
			if (handler instanceof String) { // beanName
				super.detectHandlerMethods(handler);
			}
		}

		public static final int ORDER_VALUE = 54321;

	}

}