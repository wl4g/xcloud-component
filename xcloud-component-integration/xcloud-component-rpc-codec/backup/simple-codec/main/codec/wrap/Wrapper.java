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
package com.wl4g.component.integration.codec.wrap;

import com.wl4g.component.integration.codec.CodecConfig;
import com.wl4g.component.integration.codec.CodecParameter;
import com.wl4g.component.integration.codec.Decoder;
import com.wl4g.component.integration.codec.Encoder;
import com.wl4g.component.integration.codec.stream.BytesInputStream;
import com.wl4g.component.integration.codec.stream.BytesOutputStream;

/**
 * Coding wrapper will be called before and after encoding or decoding.
 * <p>
 * You can customize wrapper like:
 * 
 * <pre>
 * class CustomWrapper extends Wrapper
 * {
 *      ...
 *      
 *      &#064;Override
 *      public void afterEncode(Encoder encoder, BytesOutputStream out,
 *              CodecParameter param) throws Exception
 *      {
 *          ...
 *      }
 *      
 *      &#064;Override
 *      public void beforeDecode(Decoder decoder, BytesInputStream in,
 *              CodecParameter param) throws Exception
 *      {
 *          ...
 *      }
 *      
 *      ...
 * }
 * 
 * ...
 * 
 *  ObjectCoder coder = new ObjectCoder();
 *      ...
 *  coder.getCodecConfig().addWrap(new CustomWrapper(...));
 * </pre>
 * 
 * Wrapper.java
 * 
 * @version 1.0.0
 * @author Wanglsir
 */
public abstract class Wrapper {
	protected static final int TOTAL_LEN_BITS = 4;

	Wrapper next;

	Wrapper prev;

	/**
	 * Be called before encoding.
	 * 
	 * @param encoder
	 *            Encoding object.
	 * @param out
	 *            Encoding IO stream.
	 * @param param
	 *            Codec parameters.
	 * @throws Exception
	 *             IO exception.
	 */
	public void beforeEncode(Encoder encoder, BytesOutputStream out, CodecParameter param) throws Exception {
	}

	/**
	 * Be called after encoding.
	 * 
	 * @param encoder
	 *            Encoding object.
	 * @param out
	 *            Encoding IO stream.
	 * @param param
	 *            Codec parameters.
	 * @throws Exception
	 *             IO exception.
	 */
	public void afterEncode(Encoder encoder, BytesOutputStream out, CodecParameter param) throws Exception {
	}

	/**
	 * Be called on final encoding handle.
	 * 
	 * @param encoder
	 *            Encoding object.
	 * @param out
	 *            Encoding IO stream.
	 * @param param
	 *            Codec parameters.
	 * @throws Exception
	 *             IO exception.
	 */
	public void finalEncode(Encoder encoder, BytesOutputStream out, CodecParameter param, Object extern) throws Exception {
	}

	/**
	 * Be called before decoding.
	 * 
	 * @param decoder
	 *            Decoding object.
	 * @param in
	 *            Decoding IO stream.
	 * @param param
	 *            Codec parameters.
	 * @throws Exception
	 *             IO exception.
	 */
	public void beforeDecode(Decoder decoder, BytesInputStream in, CodecParameter param) throws Exception {
	}

	/**
	 * Be called before decoding.
	 * 
	 * @param decoder
	 *            Decoding object.
	 * @param in
	 *            Decoding IO stream.
	 * @param param
	 *            Codec parameters.
	 * @throws Exception
	 *             IO exception.
	 */
	public void afterDecode(Decoder decoder, BytesInputStream in, CodecParameter param) throws Exception {
	}

	/**
	 * Add total length by offset parameters.
	 * 
	 * @param encoder
	 *            Encoding object.
	 * @param offset
	 *            length offset
	 * @param codecConfig
	 *            Codec configuration.
	 * @throws Exception
	 *             IO exception.
	 */
	protected void computeTotalLength(BytesOutputStream out, int offset, CodecConfig codecConfig) throws Exception {
		if (offset == 0) {
			return;
		}
		if (codecConfig.isHasTotalLength()) {
			out.incInt(0, offset);
		}
	}
}