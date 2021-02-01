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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.wl4g.component.rpc.codec.CodecParameter;
import com.wl4g.component.rpc.codec.Decoder;
import com.wl4g.component.rpc.codec.Encoder;
import com.wl4g.component.rpc.codec.annotations.CodecType;
import com.wl4g.component.rpc.codec.exception.DecodingException;
import com.wl4g.component.rpc.codec.exception.OCException;
import com.wl4g.component.rpc.codec.helper.ReflectHelper;
import com.wl4g.component.rpc.codec.helper.StringHelper;
import com.wl4g.component.rpc.codec.iostream.BytesInputStream;
import com.wl4g.component.rpc.codec.iostream.BytesOutputStream;

/**
 * ListMap can store value like HashMap. It also can store multiply values in
 * same key.
 * 
 * OCListMap.java
 * 
 * @version 1.0.0
 * @author Wanglsir
 * @param <K>
 * @param <E>
 */
@CodecType
public class OCListMap<K, E> extends OCBase implements Map<K, E> {

	Map<K, E> map = null;

	Map<K, List<E>> mapList = null;

	public OCListMap() {
		map = new HashMap<K, E>();
		mapList = new HashMap<K, List<E>>();
	}

	public OCListMap(Map<K, E> map) {
		this.map = map;
		mapList = new HashMap<K, List<E>>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return map.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E get(Object key) {
		return map.get(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E put(K key, E value) {
		List<E> list = mapList.get(key);
		if (list == null) {
			list = new ArrayList<E>();
			mapList.put(key, list);
		}
		list.add(value);
		return map.put(key, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E remove(Object key) {
		mapList.remove(key);
		return map.remove(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putAll(Map<? extends K, ? extends E> m) {
		map.putAll(m);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		map.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<K> keySet() {
		return map.keySet();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<E> values() {
		return map.values();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Entry<K, E>> entrySet() {
		return map.entrySet();
	}

	/**
	 * Get value list in same key.
	 * 
	 * @param key
	 *            Key object
	 * @return Value list
	 */
	public List<E> getKeyList(K key) {
		List<E> list = mapList.get(key);
		if (list == null) {
			list = new ArrayList<E>();
			mapList.put(key, list);
		}
		return list;
	}

	public Set<Map.Entry<K, List<E>>> getKeyListEntrySet() {
		return mapList.entrySet();
	}

	public Collection<List<E>> getKeyListValues() {
		return mapList.values();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeObject(Encoder encoder, BytesOutputStream out, CodecParameter param) throws Exception {
		if (map == null) {
			throw new OCException("OCMap collection is null");
		}
		writeAutoLength(encoder, out, param);
		out.moveLast();
		for (Entry<K, E> entry : map.entrySet()) {
			encoder.encodeObject(out, entry.getKey(), param);
			encoder.encodeObject(out, entry.getValue(), param);
		}
		writeDynamicLength(size(), encoder, out, param);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void readObject(Decoder decoder, BytesInputStream in, CodecParameter param) throws Exception {
		Type[] types = ReflectHelper.getGenericTypes(param.getCurrentfield());
		if (types == null) {
			throw new DecodingException("Map generic type is null");
		}
		readAutoLength(decoder, in, param);
		Class<K> keyType = (Class<K>) types[0];
		Class<E> valueType = (Class<E>) types[1];
		if (isDynamicLength()) {
			int len = getLenType().getValue();
			for (int i = 0; i < len && in.available() > 0; i++) {
				parse(decoder, in, keyType, valueType, param);
			}
		} else {
			while (in.available() > 0) {
				parse(decoder, in, keyType, valueType, param);
			}
		}
	}

	private void parse(Decoder decoder, BytesInputStream in, Class<K> keyType, Class<E> valueType, CodecParameter param)
			throws Exception {
		K key = getGenerticValue(decoder, in, keyType, param);
		E val = getGenerticValue(decoder, in, valueType, param);
		if (key != null && val != null) {
			put(key, val);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return StringHelper.buffer("OCListMap [map=", map, ']');
	}

}