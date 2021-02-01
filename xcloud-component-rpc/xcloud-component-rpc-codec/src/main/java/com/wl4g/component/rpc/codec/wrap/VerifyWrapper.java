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

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

import static com.wl4g.component.common.log.SmartLoggerFactory.getLogger;
import com.wl4g.component.common.log.SmartLogger;
import com.wl4g.component.rpc.codec.CodecConfig;
import com.wl4g.component.rpc.codec.CodecParameter;
import com.wl4g.component.rpc.codec.Decoder;
import com.wl4g.component.rpc.codec.Encoder;
import com.wl4g.component.rpc.codec.CodecConfig.TotalLengthType;
import com.wl4g.component.rpc.codec.exception.VerifyException;
import com.wl4g.component.rpc.codec.helper.ByteHelper;
import com.wl4g.component.rpc.codec.helper.StringHelper;
import com.wl4g.component.rpc.codec.stream.BytesInputStream;
import com.wl4g.component.rpc.codec.stream.BytesOutputStream;
import com.wl4g.component.rpc.codec.wrap.verify.Adler32Verifier;
import com.wl4g.component.rpc.codec.wrap.verify.CRC16Verifier;
import com.wl4g.component.rpc.codec.wrap.verify.CRC32Verifier;
import com.wl4g.component.rpc.codec.wrap.verify.Verifier;
import com.wl4g.component.rpc.codec.wrap.verify.VerifyExtern;

/**
 * Add verify code to bytes arrays. It support CRC16, CRC32 and adler32. It will
 * generate verify code for current head, body and tail bytes, which were called
 * before verify wrapper.
 * <p>
 * Example:
 * 
 * <pre>
 *  ObjectCoder coder = new ObjectCoder();
 *      ...
 *  coder.getCodecConfig().addWrap(VerifyWrapper.CRC16());
 *      ...
 * </pre>
 * 
 * Or
 * 
 * <pre>
 *  ObjectCoder coder = new ObjectCoder();
 *      ...
 *  coder.getCodecConfig().addWrap(new VerifyWrapper(new CustomVerifier()));
 *      ...
 * </pre>
 * 
 * VerifyWrapper.java
 * 
 * @version 1.0.0
 * @author Wanglsir
 */
public class VerifyWrapper extends Wrapper {
	private final static SmartLogger log = getLogger(VerifyWrapper.class);

	Verifier verifier;

	public VerifyWrapper(Verifier verifier) {
		this.verifier = verifier;
	}

	/**
	 * Create CRC16 verify wrapper
	 * 
	 * @return VerifyWrapper object
	 */
	public static VerifyWrapper CRC16() {
		return new VerifyWrapper(new CRC16Verifier());
	}

	/**
	 * Create CRC32 verify wrapper
	 * 
	 * @return VerifyWrapper object
	 */
	public static VerifyWrapper CRC32() {
		return new VerifyWrapper(new CRC32Verifier());
	}

	/**
	 * Create ADLER32 verify wrapper
	 * 
	 * @return VerifyWrapper object
	 */
	public static VerifyWrapper ADLER32() {
		return new VerifyWrapper(new Adler32Verifier());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void afterEncode(Encoder encoder, BytesOutputStream out, CodecParameter param) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Pre-encode verify " + verifier + " length:" + verifier.verifyLength());
		}
		List<ByteBuffer> headList = null;
		List<ByteBuffer> tailList = null;
		CodecConfig cfg = param.getCodecConfig();
		if (cfg.getTotalLengthType() == TotalLengthType.AUTO) {
			computeTotalLength(out, verifier.verifyLength(), cfg);
		}
		if (out.getHead() != null) {
			headList = new LinkedList<ByteBuffer>();
			for (ByteBuffer buf : out.getHead()) {
				headList.add(buf);
			}
		}
		if (out.getTail() != null) {
			tailList = new LinkedList<ByteBuffer>();
			for (ByteBuffer buf : out.getTail()) {
				tailList.add(buf);
			}
		}
		int len = verifier.verifyLength();
		ByteBuffer verifyBuf = (ByteBuffer) out.newBufferTailEnd(len).position(len);
		param.getFinalQueue().addWrap(this, new VerifyExtern(headList, tailList, verifyBuf));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finalEncode(Encoder encoder, BytesOutputStream out, CodecParameter param, Object extern) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Final encode verify " + verifier);
		}
		VerifyExtern vExtern = (VerifyExtern) extern;
		Object code = null;
		if (vExtern.headList != null) {
			for (ByteBuffer buf : vExtern.headList) {
				code = verifier.update(code, buf.array());
			}
		}
		code = verifier.update(code, out.getDirectBytes(), 0, out.size());
		if (vExtern.tailList != null) {
			for (ByteBuffer buf : vExtern.tailList) {
				code = verifier.update(code, buf.array());
			}
		}
		byte[] codes = verifier.getVerifyCode(code, param.isLittleEndian());
		if (codes == null) {
			throw new VerifyException("Fail to get verify code by " + verifier);
		}
		vExtern.verifyBuf.flip();
		vExtern.verifyBuf.put(codes);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beforeDecode(Decoder decoder, BytesInputStream in, CodecParameter param) throws Exception {
		int codeLen = verifier.verifyLength();
		byte[] srcCode = new byte[codeLen];
		in.setCursor(in.getCount() - codeLen);
		in.read(srcCode);
		in.moveHead();
		byte[] bytes = in.getDirectBytes();
		in.offset(0, codeLen);
		Object code = verifier.update(null, bytes, in.position(), in.getCount());
		byte[] codes = verifier.getVerifyCode(code, param.isLittleEndian());
		if (codes == null) {
			throw new VerifyException("Fail to get verify code by " + verifier);
		}
		if (log.isDebugEnabled()) {
			log.debug(StringHelper.buffer("Verify source:", ByteHelper.toHexString(srcCode), " target:",
					ByteHelper.toHexString(codes), " by verifier ", verifier.getClass()));
		}
		for (int i = 0; i < codeLen; i++) {
			if (srcCode[i] != codes[i]) {
				throw new VerifyException("Verify wrapper failed.source:" + ByteHelper.toHexString(srcCode) + " current:"
						+ ByteHelper.toHexString(codes));
			}
		}
	}

}