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

import com.wl4g.component.integration.codec.type.OCObject;

/**
 * Codec interface. Developers can implement it to customize codec.<br>
 * 
 * Codec.java
 * 
 * @version 1.0.0
 * @author Wanglsir
 */
public abstract class Codec {

	protected CodecConfig codecConfig;

	public Codec(CodecConfig codecConfig) {
		this.codecConfig = codecConfig;
	}

	/**
	 * Be called when first executing.
	 */
	public void activated() {
	}

	/**
	 * Encode {@linkplain com.wl4g.component.integration.codec.type.OCObject OCObject}
	 * object.
	 * 
	 * @param msg
	 *            Message object.
	 * @return If successful encoding, return bytes arrays. Otherwise return
	 *         null.
	 * @throws Exception
	 *             exception
	 */
	public abstract byte[] encode(OCObject msg) throws Exception;

	/**
	 * Decode {@linkplain com.wl4g.component.integration.codec.type.OCObject OCObject}
	 * object.
	 * 
	 * @param bytes
	 *            Bytes array.
	 * @param source
	 *            Target decoding object.
	 * @return If successful encoding, return
	 *         {@linkplain com.wl4g.component.integration.codec.type.OCObject OCObject}
	 *         object. Otherwise return null.
	 * @throws Exception
	 */
	public abstract OCObject decode(byte[] bytes, OCObject source) throws Exception;

	public CodecConfig getCodecConfig() {
		return codecConfig;
	}

	public void setCodecConfig(CodecConfig codecConfig) {
		this.codecConfig = codecConfig;
	}

}