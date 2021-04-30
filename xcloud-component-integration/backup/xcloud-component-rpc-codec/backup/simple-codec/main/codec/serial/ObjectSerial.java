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
package com.wl4g.component.integration.codec.serial;

import com.wl4g.component.integration.codec.ObjectCoder;

/**
 * Serial JAVA object through simple protocol.
 * 
 * ObjectSerial.java
 * 
 * @version 1.0.0
 * @author Wanglsir
 */
public class ObjectSerial {

	private static SerialHandler serialhandler;

	static {
		serialhandler = new SimpleSerialHandler();
	}

	/**
	 * Encode object to serial java object to bytes
	 * 
	 * @param coder
	 *            ObjectCoder
	 * @param obj
	 *            Target object
	 * @return Object serial bytes
	 * @throws Exception
	 *             Exception
	 */
	public static byte[] encode(ObjectCoder coder, Object obj) throws Exception {
		if (serialhandler != null && coder != null) {
			return serialhandler.encode(coder, obj);
		}
		return null;
	}

	/**
	 * Decode bytes to get JAVA object
	 * 
	 * @param coder
	 *            ObjectCoder
	 * @param bytes
	 *            Serial bytes
	 * @return JAVA object
	 * @throws Exception
	 */
	public static Object decode(ObjectCoder coder, byte[] bytes) throws Exception {
		if (serialhandler != null && coder != null) {
			return serialhandler.decode(coder, bytes);
		}
		return null;
	}

	public static void setSerialhandler(SerialHandler serialhandler) {
		ObjectSerial.serialhandler = serialhandler;
	}

}