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
package com.wl4g.components.core.web.versions.reactive;

import static java.util.Objects.isNull;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Comparator;

import org.springframework.web.reactive.result.condition.RequestCondition;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.core.Ordered;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

import com.wl4g.components.core.web.versions.annotation.ApiVersionMapping;
import com.wl4g.components.core.web.versions.annotation.ApiVersionMappingWrapper;

//
// TODO
//

/**
 * API versions {@link RequestMapping} handler mapping. </br>
 * </br>
 * Notes: Only valid for all {@link RequestMapping} annotated beans (it will
 * override the spring default {@link RequestMappingHandlerMapping}(servlet)
 * instance).
 * 
 * @author Wangl.sir &lt;wanglsir@gmail.com, 983708408@qq.com&gt;
 * @version v1.0 2020-11-26
 * @sine v1.0
 * @see <a href=
 *      "https://blog.csdn.net/sinat_29508581/article/details/89392831">Case2</a>
 * @see <a href=
 *      "http://www.kailaisii.com/archives/%E8%87%AA%E5%AE%9A%E4%B9%89RequestMappingHandlerMapping,%E5%86%8D%E4%B9%9F%E4%B8%8D%E7%94%A8%E5%86%99url%E4%BA%86~">Case1</a>
 * @see <a href=
 *      "https://blog.csdn.net/chuantian3080/article/details/100873706">Case3</a>
 */
public class ApiVersionRequestHandlerMapping extends RequestMappingHandlerMapping {

	private String[] versionParams;
	private String[] groupParams;
	private Comparator<String> versionComparator;

	public ApiVersionRequestHandlerMapping() {
		setOrder(Ordered.HIGHEST_PRECEDENCE + 5);
	}

	public String[] getVersionParams() {
		return versionParams;
	}

	public void setVersionParams(String[] versionParams) {
		this.versionParams = versionParams;
	}

	public String[] getGroupParams() {
		return groupParams;
	}

	public void setGroupParams(String[] groupParams) {
		this.groupParams = groupParams;
	}

	public Comparator<String> getVersionComparator() {
		return versionComparator;
	}

	public void setVersionComparator(Comparator<String> versionComparator) {
		this.versionComparator = versionComparator;
	}

	// --- Request mapping condition. ---

	@Override
	protected RequestCondition<ApiVersionRequestCondition> getCustomTypeCondition(Class<?> handlerType) {
		return createCondition(handlerType);
	}

	@Override
	protected RequestCondition<ApiVersionRequestCondition> getCustomMethodCondition(Method method) {
		return createCondition(method);
	}

	private RequestCondition<ApiVersionRequestCondition> createCondition(AnnotatedElement annotatedElement) {
		ApiVersionMapping versionMapping = findAnnotation(annotatedElement, ApiVersionMapping.class);
		return isNull(versionMapping) ? null
				: new ApiVersionRequestCondition(
						ApiVersionMappingWrapper.build(getApplicationContext().getEnvironment(), versionMapping),
						getVersionComparator(), versionParams, groupParams);
	}

}