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
package com.wl4g.component.rpc.codec.wrap;

import com.wl4g.component.rpc.codec.CodecParameter;
import com.wl4g.component.rpc.codec.Decoder;
import com.wl4g.component.rpc.codec.Encoder;
import com.wl4g.component.rpc.codec.stream.BytesInputStream;
import com.wl4g.component.rpc.codec.stream.BytesOutputStream;
import com.wl4g.component.rpc.codec.type.OCInt16;
import com.wl4g.component.rpc.codec.type.OCInt32;
import com.wl4g.component.rpc.codec.type.OCInt8;
import com.wl4g.component.rpc.codec.type.OCInteger;

/**
 * Add head identify or tail identify for bytes arrays.
 * <p>
 * If you want to add head identify, You can do like this:
 * 
 * <pre>
 *  ObjectCoder coder = new ObjectCoder();
 *      ...
 *  coder.getCodecConfig().setEndianType(EndianType.LITTLE);
 *  coder.getCodecConfig().addWrap(new IdentifyWrapper(new OCInt16(0xfafb)));
 *      ...
 * </pre>
 * 
 * Bytes array will be "FB FA ...". If you want to add both head identify and
 * tail identify, You can do like this:
 * 
 * <pre>
 *  ObjectCoder coder = new ObjectCoder();
 *      ...
 *  coder.getCodecConfig().setEndianType(EndianType.LITTLE);
 *  coder.getCodecConfig().addWrap(new IdentifyWrapper(new OCInt16(0xfafb), new OCInt8(0xFF)));
 *      ...
 * </pre>
 * 
 * Bytes array will be "FB FA ... FF".
 * 
 * IdentifyWrapper.java
 * 
 * @version 1.0.0
 * @author Wanglsir
 */
public class IdentifyWrapper extends Wrapper {

	private OCInteger headIdentify;

	private OCInteger tailIdentify;

	public IdentifyWrapper(byte identify) {
		this.headIdentify = new OCInt8(identify);
	}

	public IdentifyWrapper(short identify) {
		this.headIdentify = new OCInt16(identify);
	}

	public IdentifyWrapper(int identify) {
		this.headIdentify = new OCInt32(identify);
	}

	public IdentifyWrapper(OCInteger headIdentify) {
		this.headIdentify = headIdentify;
	}

	public IdentifyWrapper(OCInteger headIdentify, OCInteger tailIdentify) {
		this.headIdentify = headIdentify;
		this.tailIdentify = tailIdentify;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void afterEncode(Encoder encoder, BytesOutputStream out, CodecParameter param) throws Exception {
		if (headIdentify != null) {
			out.newBufferHeadFirst(headIdentify.getLength()).put(headIdentify.getBytes(param.isLittleEndian()));
		}
		if (tailIdentify != null) {
			out.newBufferTailEnd(tailIdentify.getLength()).put(tailIdentify.getBytes(param.isLittleEndian()));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beforeDecode(Decoder decoder, BytesInputStream in, CodecParameter param) throws Exception {
		if (headIdentify != null) {
			in.offset(headIdentify.getLength(), 0);
		}
		if (tailIdentify != null) {
			in.offset(0, tailIdentify.getLength());
		}
		in.moveHead();
	}

}