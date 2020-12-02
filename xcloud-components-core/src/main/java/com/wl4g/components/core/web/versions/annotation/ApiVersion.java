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
package com.wl4g.components.core.web.versions.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.stereotype.Indexed;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.RetentionPolicy;

/**
 * Version annotation and control for API.
 * 
 * @author Wangl.sir &lt;wanglsir@gmail.com, 983708408@qq.com&gt;
 * @version v1.0 2020-11-27
 * @sine v1.0
 * @see
 */
@Target({ ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Indexed
public @interface ApiVersion {

	/**
	 * Client platform type name. for example: (Android/iOS/iPad/WebPC/
	 * NativePC/WechatMp/WechatApplet/BaiduApplet/AliApplet/...) </br>
	 * 
	 * Notes: Optional, when empty, the matching request ignores this condition.
	 * 
	 * @return
	 */
	String[] clients() default {};

	/**
	 * API version numbers string, equivalent to:
	 * {@link #major()}.{@link #minor()}.{@link #revision()}.{@link #extension()}
	 * 
	 * <p>
	 * for example: {major}.{minor}.{revision}.{extension} => 1.0.2.10b </br>
	 * </p>
	 * 
	 * @return
	 */
	String value();

}