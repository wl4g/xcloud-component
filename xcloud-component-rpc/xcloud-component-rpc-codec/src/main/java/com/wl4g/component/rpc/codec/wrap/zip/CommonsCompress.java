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
package com.wl4g.component.rpc.codec.wrap.zip;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

import com.wl4g.component.rpc.codec.helper.IoHelper;

/**
 * Common compress. It needs common-compress 1.x support.
 * 
 * CommonsCompress.java
 * 
 * @version 1.0.0
 * @author Wanglsir
 */
public class CommonsCompress extends ZipCompress {

	private CompressorStreamFactory factory = new CompressorStreamFactory();

	private String type;

	public CommonsCompress() {
		this.type = CompressorStreamFactory.GZIP;
	}

	public CommonsCompress(String type) {
		this.type = type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void compress(InputStream input, OutputStream out) throws Exception {
		CompressorOutputStream cos = null;
		try {
			cos = factory.createCompressorOutputStream(type, out);
			byte[] buf = new byte[1024];
			int len;
			while ((len = input.read(buf)) > 0) {
				cos.write(buf, 0, len);
			}
			cos.flush();
		} catch (CompressorException e) {
			throw new Exception("Fail to compress data by commons compress. Cause " + e.getMessage(), e);
		} finally {
			IoHelper.closeIO(cos);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void uncompress(InputStream input, OutputStream out) throws Exception {
		CompressorInputStream cin = null;
		try {
			cin = factory.createCompressorInputStream(type, input);
			byte[] buf = new byte[1024];
			int len;
			while ((len = cin.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.flush();
		} catch (CompressorException e) {
			throw new Exception("Fail to decompress data by commons compress. Cause " + e.getMessage(), e);
		} finally {
			IoHelper.closeIO(cin);
		}

	}

}