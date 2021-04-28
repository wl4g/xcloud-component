/*
 * Copyright 2013-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wl4g.component.integration.feign.core.annotation.mvc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;

import feign.MethodMetadata;

import org.springframework.web.bind.annotation.RequestPart;

import static feign.Util.checkState;
import static feign.Util.emptyToNull;

/**
 * {@link RequestPart} parameter processor.
 *
 * @author Aaron Whiteside
 * @see AnnotatedParameterProcessor
 */
public class RequestPartParameterProcessor implements AnnotatedParameterProcessor {

	private static final Class<RequestPart> ANNOTATION = RequestPart.class;

	@Override
	public Class<? extends Annotation> getAnnotationType() {
		return ANNOTATION;
	}

	@Override
	public boolean processArgument(AnnotatedParameterContext context, Annotation annotation, Method method) {
		int parameterIndex = context.getParameterIndex();
		MethodMetadata data = context.getMethodMetadata();

		String name = ANNOTATION.cast(annotation).value();
		checkState(emptyToNull(name) != null, "RequestPart.value() was empty on parameter %s", parameterIndex);
		context.setParameterName(name);

		data.formParams().add(name);
		Collection<String> names = context.setTemplateParameter(name, data.indexToName().get(parameterIndex));
		data.indexToName().put(parameterIndex, names);
		return true;
	}

}