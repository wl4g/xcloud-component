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
package com.wl4g.component.rpc.codec.type;

import com.wl4g.component.rpc.codec.CodecParameter;
import com.wl4g.component.rpc.codec.Decoder;
import com.wl4g.component.rpc.codec.Encoder;
import com.wl4g.component.rpc.codec.annotations.CodecType;
import com.wl4g.component.rpc.codec.stream.BytesInputStream;
import com.wl4g.component.rpc.codec.stream.BytesOutputStream;

/**
 * Codec type object.
 * 
 * OCType.java
 * 
 * @see CodecType
 * @version 1.0.0
 * @author Wanglsir
 */
@CodecType
public interface OCType {

	/**
	 * Write object to encoding IO stream.
	 * 
	 * @param encoder
	 *            Encoding object.
	 * @param out
	 *            Encoding IO stream.
	 * @param param
	 *            Codec parameter object.
	 * @throws Exception
	 *             exception.
	 */
	public void writeObject(Encoder encoder, BytesOutputStream out, CodecParameter param) throws Exception;

	/**
	 * Read object from decoding IO stream.
	 * 
	 * @param decoder
	 *            Decoding object,
	 * @param in
	 *            Decoding IO stream.
	 * @param param
	 *            Codec parameter object.
	 * @throws Exception
	 *             exception.
	 */
	public void readObject(Decoder decoder, BytesInputStream in, CodecParameter param) throws Exception;
}