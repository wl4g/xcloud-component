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
package com.wl4g.component.rpc.codec.wrap.cipher;

import java.security.Key;

import javax.crypto.Cipher;

import com.wl4g.component.rpc.codec.CodecParameter;
import com.wl4g.component.rpc.codec.exception.CipherException;

/**
 * 
 * AbstractCipher.java
 * 
 * @version 1.0.0
 * @author Wanglsir
 */
public abstract class AbstractCipher extends OCCipher {

	private Key cipherKey;

	private String algorithm;

	/**
	 * Construct cipher
	 * 
	 * @param algorithm
	 *            Cipher algorithm.
	 * @param key
	 *            Cipher key.
	 */
	public AbstractCipher(String algorithm, String key) {
		this(algorithm, key.getBytes(), 0);
	}

	/**
	 * Construct cipher
	 * 
	 * @param algorithm
	 *            Cipher algorithm.
	 * @param key
	 *            Cipher key bytes.
	 */
	public AbstractCipher(String algorithm, byte[] key, int keySize) {
		this.algorithm = algorithm;
		cipherKey = initSecretKey(key, keySize);
	}

	/**
	 * Initialize cipher secret key.
	 * 
	 * @param key
	 *            Key bytes
	 * @param keySize
	 *            Key size
	 * @return If initializing successfully, return secret key object. Otherwise
	 *         return null or CipherException will be throw out.
	 */
	protected abstract Key initSecretKey(byte[] key, int keySize);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] encrypt(byte[] data, int offset, int length, CodecParameter param) {
		if (cipherKey == null) {
			throw new CipherException("Fail to encrypt " + algorithm + ". Cause fail to initialize secret key.");
		}
		try {
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.ENCRYPT_MODE, cipherKey);
			return cipher.doFinal(data, offset, length);
		} catch (Exception e) {
			throw new CipherException("Fail to encrypt " + algorithm + ". Cause " + e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] decrypt(byte[] data, int offset, int length, CodecParameter param) {
		if (cipherKey == null) {
			throw new CipherException("Fail to decrypt " + algorithm + ". Cause fail to initialize secret key.");
		}
		try {
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.DECRYPT_MODE, cipherKey);
			return cipher.doFinal(data, offset, length);
		} catch (Exception e) {
			throw new CipherException("Fail to decrypt " + algorithm + ". Cause " + e.getMessage(), e);
		}
	}

}