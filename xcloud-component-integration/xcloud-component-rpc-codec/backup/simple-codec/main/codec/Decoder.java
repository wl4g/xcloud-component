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
package com.wl4g.component.integration.codec;

import com.wl4g.component.integration.codec.stream.BytesInputStream;

/**
 * Decoder is the interface to decode message object in codec. Developer can
 * customize decoder in custom codec.<br>
 * Decoder.java
 * 
 * @version 1.0.0
 * @author Wanglsir
 */
public abstract class Decoder {

	/**
	 * Decode object
	 * 
	 * @param in
	 *            Decode bytes stream.
	 * @param obj
	 *            Target object.
	 * @param param
	 *            Codec parameter. It's available in current decode flow.
	 * @return If return not null, parent object will set the value to current
	 *         field.
	 * @throws Exception
	 *             exception
	 */
	public abstract Object decodeObject(BytesInputStream in, Object obj, CodecParameter param) throws Exception;

}