/*
 * Copyright 2017 ~ 2025 the original author or authors. <wanglsir@gmail.com, 983708408@qq.com>
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
 */
package com.wl4g.component.support.cache.jedis.cluster;

import static org.springframework.util.Assert.notNull;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.wl4g.component.support.cache.jedis.JedisClient;

import java.util.Set;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.BitOP;
import redis.clients.jedis.BitPosParams;
import redis.clients.jedis.DebugParams;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.ZParams;
import redis.clients.jedis.params.geo.GeoRadiusParam;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;

/**
 * {@link JedisCluster} jedis client wrapper.
 *
 * @author Wangl.sir <wanglsir@gmail.com, 983708408@qq.com>
 * @version v1.0 2020-08-03
 * @since
 */
@SuppressWarnings("deprecation")
public class JedisClusterJedisClient implements JedisClient {

	/** {@link JedisCluster} */
	protected final JedisCluster jedisCluster;

	public JedisClusterJedisClient(JedisCluster jedisCluster) {
		notNull(jedisCluster, "jedisCluster");
		this.jedisCluster = jedisCluster;
	}

	@Override
	public Map<String, JedisPool> getClusterNodes() {
		return jedisCluster.getClusterNodes();
	}

	@Override
	public void close() throws IOException {
		jedisCluster.close();
	}

	@Override
	public Object eval(byte[] script, byte[] keyCount, byte[]... params) {
		return jedisCluster.eval(script, keyCount, params);
	}

	@Override
	public Object eval(byte[] script, int keyCount, byte[]... params) {
		return jedisCluster.eval(script, keyCount, params);
	}

	@Override
	public Object eval(byte[] script, List<byte[]> keys, List<byte[]> args) {
		return jedisCluster.eval(script, keys, args);
	}

	@Override
	public Object eval(byte[] script, byte[] key) {
		return jedisCluster.eval(script, key);
	}

	@Override
	public Object evalsha(byte[] script, byte[] key) {
		return jedisCluster.evalsha(script, key);
	}

	@Override
	public Object evalsha(byte[] sha1, List<byte[]> keys, List<byte[]> args) {
		return jedisCluster.evalsha(sha1, keys, args);
	}

	@Override
	public Object evalsha(byte[] sha1, int keyCount, byte[]... params) {
		return jedisCluster.evalsha(sha1, keyCount, params);
	}

	@Override
	public List<Long> scriptExists(byte[] key, byte[][] sha1) {
		return jedisCluster.scriptExists(key, sha1);
	}

	@Override
	public byte[] scriptLoad(byte[] script, byte[] key) {
		return jedisCluster.scriptLoad(script, key);
	}

	@Override
	public String scriptFlush(byte[] key) {
		return jedisCluster.scriptFlush(key);
	}

	@Override
	public String scriptKill(byte[] key) {
		return jedisCluster.scriptKill(key);
	}

	@Override
	public Long exists(byte[]... keys) {
		return jedisCluster.exists(keys);
	}

	@Override
	public Long del(byte[]... keys) {
		return jedisCluster.del(keys);
	}

	@Override
	public List<byte[]> blpop(int timeout, byte[]... keys) {
		return jedisCluster.blpop(timeout, keys);
	}

	@Override
	public List<byte[]> brpop(int timeout, byte[]... keys) {
		return jedisCluster.brpop(timeout, keys);
	}

	@Override
	public List<byte[]> mget(byte[]... keys) {
		return jedisCluster.mget(keys);
	}

	@Override
	public String mset(byte[]... keysvalues) {
		return jedisCluster.mset(keysvalues);
	}

	@Override
	public Long msetnx(byte[]... keysvalues) {
		return jedisCluster.msetnx(keysvalues);
	}

	@Override
	public String rename(byte[] oldkey, byte[] newkey) {
		return jedisCluster.rename(oldkey, newkey);
	}

	@Override
	public Long renamenx(byte[] oldkey, byte[] newkey) {
		return jedisCluster.renamenx(oldkey, newkey);
	}

	@Override
	public byte[] rpoplpush(byte[] srckey, byte[] dstkey) {
		return jedisCluster.rpoplpush(srckey, dstkey);
	}

	@Override
	public Set<byte[]> sdiff(byte[]... keys) {
		return jedisCluster.sdiff(keys);
	}

	@Override
	public Long sdiffstore(byte[] dstkey, byte[]... keys) {
		return jedisCluster.sdiffstore(dstkey, keys);
	}

	@Override
	public Set<byte[]> sinter(byte[]... keys) {
		return jedisCluster.sinter(keys);
	}

	@Override
	public Long sinterstore(byte[] dstkey, byte[]... keys) {
		return jedisCluster.sinterstore(dstkey, keys);
	}

	@Override
	public Long smove(byte[] srckey, byte[] dstkey, byte[] member) {
		return jedisCluster.smove(srckey, dstkey, member);
	}

	@Override
	public Long sort(byte[] key, SortingParams sortingParameters, byte[] dstkey) {
		return jedisCluster.sort(key, sortingParameters, dstkey);
	}

	@Override
	public Long sort(byte[] key, byte[] dstkey) {
		return jedisCluster.sort(key, dstkey);
	}

	@Override
	public Set<byte[]> sunion(byte[]... keys) {
		return jedisCluster.sunion(keys);
	}

	@Override
	public Long sunionstore(byte[] dstkey, byte[]... keys) {
		return jedisCluster.sunionstore(dstkey, keys);
	}

	@Override
	public Long zinterstore(byte[] dstkey, byte[]... sets) {
		return jedisCluster.zinterstore(dstkey, sets);
	}

	@Override
	public Long zinterstore(byte[] dstkey, ZParams params, byte[]... sets) {
		return jedisCluster.zinterstore(dstkey, params, sets);
	}

	@Override
	public Long zunionstore(byte[] dstkey, byte[]... sets) {
		return jedisCluster.zunionstore(dstkey, sets);
	}

	@Override
	public Long zunionstore(byte[] dstkey, ZParams params, byte[]... sets) {
		return jedisCluster.zunionstore(dstkey, params, sets);
	}

	@Override
	public byte[] brpoplpush(byte[] source, byte[] destination, int timeout) {
		return jedisCluster.brpoplpush(source, destination, timeout);
	}

	@Override
	public Long publish(byte[] channel, byte[] message) {
		return jedisCluster.publish(channel, message);
	}

	@Override
	public void subscribe(BinaryJedisPubSub jedisPubSub, byte[]... channels) {
		jedisCluster.subscribe(jedisPubSub, channels);
	}

	@Override
	public void psubscribe(BinaryJedisPubSub jedisPubSub, byte[]... patterns) {
		jedisCluster.psubscribe(jedisPubSub, patterns);
	}

	@Override
	public Long bitop(BitOP op, byte[] destKey, byte[]... srcKeys) {
		return jedisCluster.bitop(op, destKey, srcKeys);
	}

	@Override
	public String pfmerge(byte[] destkey, byte[]... sourcekeys) {
		return jedisCluster.pfmerge(destkey, sourcekeys);
	}

	@Override
	public Long pfcount(byte[]... keys) {
		return jedisCluster.pfcount(keys);
	}

	@Override
	public String set(byte[] key, byte[] value) {
		return jedisCluster.set(key, value);
	}

	@Override
	public String set(byte[] key, byte[] value, byte[] nxxx, byte[] expx, long time) {
		return jedisCluster.set(key, value, nxxx, expx, time);
	}

	@Override
	public byte[] get(byte[] key) {
		return jedisCluster.get(key);
	}

	@Override
	public Boolean exists(byte[] key) {
		return jedisCluster.exists(key);
	}

	@Override
	public Long persist(byte[] key) {
		return jedisCluster.persist(key);
	}

	@Override
	public String type(byte[] key) {
		return jedisCluster.type(key);
	}

	@Override
	public Long expire(byte[] key, int seconds) {
		return jedisCluster.expire(key, seconds);
	}

	@Override
	public Long pexpire(byte[] key, long milliseconds) {
		return jedisCluster.pexpire(key, milliseconds);
	}

	@Override
	public Long expireAt(byte[] key, long unixTime) {
		return jedisCluster.expireAt(key, unixTime);
	}

	@Override
	public Long pexpireAt(byte[] key, long millisecondsTimestamp) {
		return jedisCluster.pexpireAt(key, millisecondsTimestamp);
	}

	@Override
	public Long ttl(byte[] key) {
		return jedisCluster.ttl(key);
	}

	@Override
	public Boolean setbit(byte[] key, long offset, boolean value) {
		return jedisCluster.setbit(key, offset, value);
	}

	@Override
	public Boolean setbit(byte[] key, long offset, byte[] value) {
		return jedisCluster.setbit(key, offset, value);
	}

	@Override
	public Boolean getbit(byte[] key, long offset) {
		return jedisCluster.getbit(key, offset);
	}

	@Override
	public Long setrange(byte[] key, long offset, byte[] value) {
		return jedisCluster.setrange(key, offset, value);
	}

	@Override
	public byte[] getrange(byte[] key, long startOffset, long endOffset) {
		return jedisCluster.getrange(key, startOffset, endOffset);
	}

	@Override
	public byte[] getSet(byte[] key, byte[] value) {
		return jedisCluster.getSet(key, value);
	}

	@Override
	public Long setnx(byte[] key, byte[] value) {
		return jedisCluster.setnx(key, value);
	}

	@Override
	public String setex(byte[] key, int seconds, byte[] value) {
		return jedisCluster.setex(key, seconds, value);
	}

	@Override
	public Long decrBy(byte[] key, long integer) {
		return jedisCluster.decrBy(key, integer);
	}

	@Override
	public Long decr(byte[] key) {
		return jedisCluster.decr(key);
	}

	@Override
	public Long incrBy(byte[] key, long integer) {
		return jedisCluster.incrBy(key, integer);
	}

	@Override
	public Double incrByFloat(byte[] key, double value) {
		return jedisCluster.incrByFloat(key, value);
	}

	@Override
	public Long incr(byte[] key) {
		return jedisCluster.incr(key);
	}

	@Override
	public Long append(byte[] key, byte[] value) {
		return jedisCluster.append(key, value);
	}

	@Override
	public byte[] substr(byte[] key, int start, int end) {
		return jedisCluster.substr(key, start, end);
	}

	@Override
	public Long hset(byte[] key, byte[] field, byte[] value) {
		return jedisCluster.hset(key, field, value);
	}

	@Override
	public byte[] hget(byte[] key, byte[] field) {
		return jedisCluster.hget(key, field);
	}

	@Override
	public Long hsetnx(byte[] key, byte[] field, byte[] value) {
		return jedisCluster.hsetnx(key, field, value);
	}

	@Override
	public String hmset(byte[] key, Map<byte[], byte[]> hash) {
		return jedisCluster.hmset(key, hash);
	}

	@Override
	public List<byte[]> hmget(byte[] key, byte[]... fields) {
		return jedisCluster.hmget(key, fields);
	}

	@Override
	public Long hincrBy(byte[] key, byte[] field, long value) {
		return jedisCluster.hincrBy(key, field, value);
	}

	@Override
	public Double hincrByFloat(byte[] key, byte[] field, double value) {
		return jedisCluster.hincrByFloat(key, field, value);
	}

	@Override
	public Boolean hexists(byte[] key, byte[] field) {
		return jedisCluster.hexists(key, field);
	}

	@Override
	public Long hdel(byte[] key, byte[]... field) {
		return jedisCluster.hdel(key, field);
	}

	@Override
	public Long hlen(byte[] key) {
		return jedisCluster.hlen(key);
	}

	@Override
	public Set<byte[]> hkeys(byte[] key) {
		return jedisCluster.hkeys(key);
	}

	@Override
	public Collection<byte[]> hvals(byte[] key) {
		return jedisCluster.hvals(key);
	}

	@Override
	public Map<byte[], byte[]> hgetAll(byte[] key) {
		return jedisCluster.hgetAll(key);
	}

	@Override
	public Long rpush(byte[] key, byte[]... args) {
		return jedisCluster.rpush(key, args);
	}

	@Override
	public Long lpush(byte[] key, byte[]... args) {
		return jedisCluster.lpush(key, args);
	}

	@Override
	public Long llen(byte[] key) {
		return jedisCluster.llen(key);
	}

	@Override
	public List<byte[]> lrange(byte[] key, long start, long end) {
		return jedisCluster.lrange(key, start, end);
	}

	@Override
	public String ltrim(byte[] key, long start, long end) {
		return jedisCluster.ltrim(key, start, end);
	}

	@Override
	public byte[] lindex(byte[] key, long index) {
		return jedisCluster.lindex(key, index);
	}

	@Override
	public String lset(byte[] key, long index, byte[] value) {
		return jedisCluster.lset(key, index, value);
	}

	@Override
	public Long lrem(byte[] key, long count, byte[] value) {
		return jedisCluster.lrem(key, count, value);
	}

	@Override
	public byte[] lpop(byte[] key) {
		return jedisCluster.lpop(key);
	}

	@Override
	public byte[] rpop(byte[] key) {
		return jedisCluster.rpop(key);
	}

	@Override
	public Long sadd(byte[] key, byte[]... member) {
		return jedisCluster.sadd(key, member);
	}

	@Override
	public Set<byte[]> smembers(byte[] key) {
		return jedisCluster.smembers(key);
	}

	@Override
	public Long srem(byte[] key, byte[]... member) {
		return jedisCluster.srem(key, member);
	}

	@Override
	public byte[] spop(byte[] key) {
		return jedisCluster.spop(key);
	}

	@Override
	public Set<byte[]> spop(byte[] key, long count) {
		return jedisCluster.spop(key, count);
	}

	@Override
	public Long scard(byte[] key) {
		return jedisCluster.scard(key);
	}

	@Override
	public Boolean sismember(byte[] key, byte[] member) {
		return jedisCluster.sismember(key, member);
	}

	@Override
	public byte[] srandmember(byte[] key) {
		return jedisCluster.srandmember(key);
	}

	@Override
	public List<byte[]> srandmember(byte[] key, int count) {
		return jedisCluster.srandmember(key, count);
	}

	@Override
	public Long strlen(byte[] key) {
		return jedisCluster.strlen(key);
	}

	@Override
	public Long zadd(byte[] key, double score, byte[] member) {
		return jedisCluster.zadd(key, score, member);
	}

	@Override
	public Long zadd(byte[] key, Map<byte[], Double> scoreMembers) {
		return jedisCluster.zadd(key, scoreMembers);
	}

	@Override
	public Long zadd(byte[] key, double score, byte[] member, ZAddParams params) {
		return jedisCluster.zadd(key, score, member, params);
	}

	@Override
	public Long zadd(byte[] key, Map<byte[], Double> scoreMembers, ZAddParams params) {
		return jedisCluster.zadd(key, scoreMembers, params);
	}

	@Override
	public Set<byte[]> zrange(byte[] key, long start, long end) {
		return jedisCluster.zrange(key, start, end);
	}

	@Override
	public Long zrem(byte[] key, byte[]... member) {
		return jedisCluster.zrem(key, member);
	}

	@Override
	public Double zincrby(byte[] key, double score, byte[] member) {
		return jedisCluster.zincrby(key, score, member);
	}

	@Override
	public Double zincrby(byte[] key, double score, byte[] member, ZIncrByParams params) {
		return jedisCluster.zincrby(key, score, member, params);
	}

	@Override
	public Long zrank(byte[] key, byte[] member) {
		return jedisCluster.zrank(key, member);
	}

	@Override
	public Long zrevrank(byte[] key, byte[] member) {
		return jedisCluster.zrevrank(key, member);
	}

	@Override
	public Set<byte[]> zrevrange(byte[] key, long start, long end) {
		return jedisCluster.zrevrange(key, start, end);
	}

	@Override
	public Set<Tuple> zrangeWithScores(byte[] key, long start, long end) {
		return jedisCluster.zrangeWithScores(key, start, end);
	}

	@Override
	public Set<Tuple> zrevrangeWithScores(byte[] key, long start, long end) {
		return jedisCluster.zrevrangeWithScores(key, start, end);
	}

	@Override
	public Long zcard(byte[] key) {
		return jedisCluster.zcard(key);
	}

	@Override
	public Double zscore(byte[] key, byte[] member) {
		return jedisCluster.zscore(key, member);
	}

	@Override
	public List<byte[]> sort(byte[] key) {
		return jedisCluster.sort(key);
	}

	@Override
	public List<byte[]> sort(byte[] key, SortingParams sortingParameters) {
		return jedisCluster.sort(key, sortingParameters);
	}

	@Override
	public Long zcount(byte[] key, double min, double max) {
		return jedisCluster.zcount(key, min, max);
	}

	@Override
	public Long zcount(byte[] key, byte[] min, byte[] max) {
		return jedisCluster.zcount(key, min, max);
	}

	@Override
	public Set<byte[]> zrangeByScore(byte[] key, double min, double max) {
		return jedisCluster.zrangeByScore(key, min, max);
	}

	@Override
	public Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max) {
		return jedisCluster.zrangeByScore(key, min, max);
	}

	@Override
	public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
		return jedisCluster.zrevrangeByScore(key, max, min);
	}

	@Override
	public Set<byte[]> zrangeByScore(byte[] key, double min, double max, int offset, int count) {
		return jedisCluster.zrangeByScore(key, min, max, offset, count);
	}

	@Override
	public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min) {
		return jedisCluster.zrevrangeByScore(key, max, min);
	}

	@Override
	public Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max, int offset, int count) {
		return jedisCluster.zrangeByScore(key, min, max, offset, count);
	}

	@Override
	public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min, int offset, int count) {
		return jedisCluster.zrevrangeByScore(key, max, min, offset, count);
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
		return jedisCluster.zrangeByScoreWithScores(key, min, max);
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min) {
		return jedisCluster.zrevrangeByScoreWithScores(key, max, min);
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max, int offset, int count) {
		return jedisCluster.zrangeByScoreWithScores(key, min, max, offset, count);
	}

	@Override
	public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min, int offset, int count) {
		return jedisCluster.zrevrangeByScore(key, max, min, offset, count);
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max) {
		return jedisCluster.zrangeByScoreWithScores(key, min, max);
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min) {
		return jedisCluster.zrevrangeByScoreWithScores(key, max, min);
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max, int offset, int count) {
		return jedisCluster.zrangeByScoreWithScores(key, min, max, offset, count);
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min, int offset, int count) {
		return jedisCluster.zrevrangeByScoreWithScores(key, max, min, offset, count);
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min, int offset, int count) {
		return jedisCluster.zrevrangeByScoreWithScores(key, max, min, offset, count);
	}

	@Override
	public Long zremrangeByRank(byte[] key, long start, long end) {
		return jedisCluster.zremrangeByRank(key, start, end);
	}

	@Override
	public Long zremrangeByScore(byte[] key, double start, double end) {
		return jedisCluster.zremrangeByScore(key, start, end);
	}

	@Override
	public Long zremrangeByScore(byte[] key, byte[] start, byte[] end) {
		return jedisCluster.zremrangeByScore(key, start, end);
	}

	@Override
	public Long zlexcount(byte[] key, byte[] min, byte[] max) {
		return jedisCluster.zlexcount(key, min, max);
	}

	@Override
	public Set<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max) {
		return jedisCluster.zrangeByLex(key, min, max);
	}

	@Override
	public Set<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max, int offset, int count) {
		return jedisCluster.zrangeByLex(key, min, max, offset, count);
	}

	@Override
	public Set<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min) {
		return jedisCluster.zrevrangeByLex(key, max, min);
	}

	@Override
	public Set<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min, int offset, int count) {
		return jedisCluster.zrevrangeByLex(key, max, min, offset, count);
	}

	@Override
	public Long zremrangeByLex(byte[] key, byte[] min, byte[] max) {
		return jedisCluster.zremrangeByLex(key, min, max);
	}

	@Override
	public Long linsert(byte[] key, LIST_POSITION where, byte[] pivot, byte[] value) {
		return jedisCluster.linsert(key, where, pivot, value);
	}

	@Override
	public Long lpushx(byte[] key, byte[]... arg) {
		return jedisCluster.lpushx(key, arg);
	}

	@Override
	public Long rpushx(byte[] key, byte[]... arg) {
		return jedisCluster.rpushx(key, arg);
	}

	@Override
	public Long del(byte[] key) {
		return jedisCluster.del(key);
	}

	@Override
	public byte[] echo(byte[] arg) {
		return jedisCluster.echo(arg);
	}

	@Override
	public Long bitcount(byte[] key) {
		return jedisCluster.bitcount(key);
	}

	@Override
	public Long bitcount(byte[] key, long start, long end) {
		return jedisCluster.bitcount(key, start, end);
	}

	@Override
	public Long pfadd(byte[] key, byte[]... elements) {
		return jedisCluster.pfadd(key, elements);
	}

	@Override
	public long pfcount(byte[] key) {
		return jedisCluster.pfcount(key);
	}

	@Override
	public Long geoadd(byte[] key, double longitude, double latitude, byte[] member) {
		return jedisCluster.geoadd(key, longitude, latitude, member);
	}

	@Override
	public Long geoadd(byte[] key, Map<byte[], GeoCoordinate> memberCoordinateMap) {
		return jedisCluster.geoadd(key, memberCoordinateMap);
	}

	@Override
	public Double geodist(byte[] key, byte[] member1, byte[] member2) {
		return jedisCluster.geodist(key, member1, member2);
	}

	@Override
	public Double geodist(byte[] key, byte[] member1, byte[] member2, GeoUnit unit) {
		return jedisCluster.geodist(key, member1, member2, unit);
	}

	@Override
	public List<byte[]> geohash(byte[] key, byte[]... members) {
		return jedisCluster.geohash(key, members);
	}

	@Override
	public List<GeoCoordinate> geopos(byte[] key, byte[]... members) {
		return jedisCluster.geopos(key, members);
	}

	@Override
	public List<GeoRadiusResponse> georadius(byte[] key, double longitude, double latitude, double radius, GeoUnit unit) {
		return jedisCluster.georadius(key, longitude, latitude, radius, unit);
	}

	@Override
	public List<GeoRadiusResponse> georadius(byte[] key, double longitude, double latitude, double radius, GeoUnit unit,
			GeoRadiusParam param) {
		return jedisCluster.georadius(key, longitude, latitude, radius, unit, param);
	}

	@Override
	public List<GeoRadiusResponse> georadiusByMember(byte[] key, byte[] member, double radius, GeoUnit unit) {
		return jedisCluster.georadiusByMember(key, member, radius, unit);
	}

	@Override
	public List<GeoRadiusResponse> georadiusByMember(byte[] key, byte[] member, double radius, GeoUnit unit,
			GeoRadiusParam param) {
		return jedisCluster.georadiusByMember(key, member, radius, unit, param);
	}

	@Override
	public ScanResult<byte[]> scan(byte[] cursor, ScanParams params) {
		return jedisCluster.scan(cursor, params);
	}

	@Override
	public ScanResult<Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor) {
		return jedisCluster.hscan(key, cursor);
	}

	@Override
	public ScanResult<Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor, ScanParams params) {
		return jedisCluster.hscan(key, cursor, params);
	}

	@Override
	public ScanResult<byte[]> sscan(byte[] key, byte[] cursor) {
		return jedisCluster.sscan(key, cursor);
	}

	@Override
	public ScanResult<byte[]> sscan(byte[] key, byte[] cursor, ScanParams params) {
		return jedisCluster.sscan(key, cursor, params);
	}

	@Override
	public ScanResult<Tuple> zscan(byte[] key, byte[] cursor) {
		return jedisCluster.zscan(key, cursor);
	}

	@Override
	public ScanResult<Tuple> zscan(byte[] key, byte[] cursor, ScanParams params) {
		return jedisCluster.zscan(key, cursor, params);
	}

	@Override
	public List<byte[]> bitfield(byte[] key, byte[]... arguments) {
		return jedisCluster.bitfield(key, arguments);
	}

	@Override
	public String ping() {
		return jedisCluster.ping();
	}

	@Override
	public String quit() {
		return jedisCluster.quit();
	}

	@Override
	public String flushDB() {
		return jedisCluster.flushDB();
	}

	@Override
	public Long dbSize() {
		return jedisCluster.dbSize();
	}

	@Override
	public String select(int index) {
		return jedisCluster.select(index);
	}

	@Override
	public String flushAll() {
		return jedisCluster.flushAll();
	}

	@Override
	public String auth(String password) {
		return jedisCluster.auth(password);
	}

	@Override
	public String save() {
		return jedisCluster.save();
	}

	@Override
	public String bgsave() {
		return jedisCluster.bgsave();
	}

	@Override
	public String bgrewriteaof() {
		return jedisCluster.bgrewriteaof();
	}

	@Override
	public Long lastsave() {
		return jedisCluster.lastsave();
	}

	@Override
	public String shutdown() {
		return jedisCluster.shutdown();
	}

	@Override
	public String info() {
		return jedisCluster.info();
	}

	@Override
	public String info(String section) {
		return jedisCluster.info(section);
	}

	@Override
	public String slaveof(String host, int port) {
		return jedisCluster.slaveof(host, port);
	}

	@Override
	public String slaveofNoOne() {
		return jedisCluster.slaveofNoOne();
	}

	@Override
	public Long getDB() {
		return jedisCluster.getDB();
	}

	@Override
	public String debug(DebugParams params) {
		return jedisCluster.debug(params);
	}

	@Override
	public String configResetStat() {
		return jedisCluster.configResetStat();
	}

	@Override
	public Long waitReplicas(int replicas, long timeout) {
		return jedisCluster.waitReplicas(replicas, timeout);
	}

	@Override
	public Object eval(String script, int keyCount, String... params) {
		return jedisCluster.eval(script, keyCount, params);
	}

	@Override
	public Object eval(String script, List<String> keys, List<String> args) {
		return jedisCluster.eval(script, keys, args);
	}

	@Override
	public Object eval(String script, String key) {
		return jedisCluster.eval(script, key);
	}

	@Override
	public Object evalsha(String script, String key) {
		return jedisCluster.evalsha(script, key);
	}

	@Override
	public Object evalsha(String sha1, List<String> keys, List<String> args) {
		return jedisCluster.evalsha(sha1, keys, args);
	}

	@Override
	public Object evalsha(String sha1, int keyCount, String... params) {
		return jedisCluster.evalsha(sha1, keyCount, params);
	}

	@Override
	public Boolean scriptExists(String sha1, String key) {
		return jedisCluster.scriptExists(sha1, key);
	}

	@Override
	public List<Boolean> scriptExists(String key, String... sha1) {
		return jedisCluster.scriptExists(key, sha1);
	}

	@Override
	public String scriptLoad(String script, String key) {
		return jedisCluster.scriptLoad(script, key);
	}

	@Override
	public Long exists(String... keys) {
		return jedisCluster.exists(keys);
	}

	@Override
	public Long del(String... keys) {
		return jedisCluster.del(keys);
	}

	@Override
	public List<String> blpop(int timeout, String... keys) {
		return jedisCluster.blpop(timeout, keys);
	}

	@Override
	public List<String> brpop(int timeout, String... keys) {
		return jedisCluster.brpop(timeout, keys);
	}

	@Override
	public List<String> mget(String... keys) {
		return jedisCluster.mget(keys);
	}

	@Override
	public String mset(String... keysvalues) {
		return jedisCluster.mset(keysvalues);
	}

	@Override
	public Long msetnx(String... keysvalues) {
		return jedisCluster.msetnx(keysvalues);
	}

	@Override
	public String rename(String oldkey, String newkey) {
		return jedisCluster.rename(oldkey, newkey);
	}

	@Override
	public Long renamenx(String oldkey, String newkey) {
		return jedisCluster.renamenx(oldkey, newkey);
	}

	@Override
	public String rpoplpush(String srckey, String dstkey) {
		return jedisCluster.rpoplpush(srckey, dstkey);
	}

	@Override
	public Set<String> sdiff(String... keys) {
		return jedisCluster.sdiff(keys);
	}

	@Override
	public Long sdiffstore(String dstkey, String... keys) {
		return jedisCluster.sdiffstore(dstkey, keys);
	}

	@Override
	public Set<String> sinter(String... keys) {
		return jedisCluster.sinter(keys);
	}

	@Override
	public Long sinterstore(String dstkey, String... keys) {
		return jedisCluster.sinterstore(dstkey, keys);
	}

	@Override
	public Long smove(String srckey, String dstkey, String member) {
		return jedisCluster.smove(srckey, dstkey, member);
	}

	@Override
	public Long sort(String key, SortingParams sortingParameters, String dstkey) {
		return jedisCluster.sort(key, sortingParameters, dstkey);
	}

	@Override
	public Long sort(String key, String dstkey) {
		return jedisCluster.sort(key, dstkey);
	}

	@Override
	public Set<String> sunion(String... keys) {
		return jedisCluster.sunion(keys);
	}

	@Override
	public Long sunionstore(String dstkey, String... keys) {
		return jedisCluster.sunionstore(dstkey, keys);
	}

	@Override
	public Long zinterstore(String dstkey, String... sets) {
		return jedisCluster.zinterstore(dstkey, sets);
	}

	@Override
	public Long zinterstore(String dstkey, ZParams params, String... sets) {
		return jedisCluster.zinterstore(dstkey, params, sets);
	}

	@Override
	public Long zunionstore(String dstkey, String... sets) {
		return jedisCluster.zunionstore(dstkey, sets);
	}

	@Override
	public Long zunionstore(String dstkey, ZParams params, String... sets) {
		return jedisCluster.zunionstore(dstkey, params, sets);
	}

	@Override
	public String brpoplpush(String source, String destination, int timeout) {
		return jedisCluster.brpoplpush(source, destination, timeout);
	}

	@Override
	public Long publish(String channel, String message) {
		return jedisCluster.publish(channel, message);
	}

	@Override
	public void subscribe(JedisPubSub jedisPubSub, String... channels) {
		jedisCluster.subscribe(jedisPubSub, channels);
	}

	@Override
	public void psubscribe(JedisPubSub jedisPubSub, String... patterns) {
		jedisCluster.psubscribe(jedisPubSub, patterns);
	}

	@Override
	public Long bitop(BitOP op, String destKey, String... srcKeys) {
		return jedisCluster.bitop(op, destKey, srcKeys);
	}

	@Override
	public String pfmerge(String destkey, String... sourcekeys) {
		return jedisCluster.pfmerge(destkey, sourcekeys);
	}

	@Override
	public long pfcount(String... keys) {
		return jedisCluster.pfcount(keys);
	}

	@Override
	public ScanResult<String> scan(String cursor, ScanParams params) {
		return jedisCluster.scan(cursor, params);
	}

	@Override
	public String set(String key, String value) {
		return jedisCluster.set(key, value);
	}

	@Override
	public String set(String key, String value, String nxxx, String expx, long time) {
		return jedisCluster.set(key, value, nxxx, expx, time);
	}

	@Override
	public String set(String key, String value, String nxxx) {
		return jedisCluster.set(key, value, nxxx);
	}

	@Override
	public String get(String key) {
		return jedisCluster.get(key);
	}

	@Override
	public Boolean exists(String key) {
		return jedisCluster.exists(key);
	}

	@Override
	public Long persist(String key) {
		return jedisCluster.persist(key);
	}

	@Override
	public String type(String key) {
		return jedisCluster.type(key);
	}

	@Override
	public Long expire(String key, int seconds) {
		return jedisCluster.expire(key, seconds);
	}

	@Override
	public Long pexpire(String key, long milliseconds) {
		return jedisCluster.pexpire(key, milliseconds);
	}

	@Override
	public Long expireAt(String key, long unixTime) {
		return jedisCluster.expireAt(key, unixTime);
	}

	@Override
	public Long pexpireAt(String key, long millisecondsTimestamp) {
		return jedisCluster.pexpireAt(key, millisecondsTimestamp);
	}

	@Override
	public Long ttl(String key) {
		return jedisCluster.ttl(key);
	}

	@Override
	public Long pttl(String key) {
		return jedisCluster.pttl(key);
	}

	@Override
	public Boolean setbit(String key, long offset, boolean value) {
		return jedisCluster.setbit(key, offset, value);
	}

	@Override
	public Boolean setbit(String key, long offset, String value) {
		return jedisCluster.setbit(key, offset, value);
	}

	@Override
	public Boolean getbit(String key, long offset) {
		return jedisCluster.getbit(key, offset);
	}

	@Override
	public Long setrange(String key, long offset, String value) {
		return jedisCluster.setrange(key, offset, value);
	}

	@Override
	public String getrange(String key, long startOffset, long endOffset) {
		return jedisCluster.getrange(key, startOffset, endOffset);
	}

	@Override
	public String getSet(String key, String value) {
		return jedisCluster.getSet(key, value);
	}

	@Override
	public Long setnx(String key, String value) {
		return jedisCluster.setnx(key, value);
	}

	@Override
	public String setex(String key, int seconds, String value) {
		return jedisCluster.setex(key, seconds, value);
	}

	@Override
	public String psetex(String key, long milliseconds, String value) {
		return jedisCluster.psetex(key, milliseconds, value);
	}

	@Override
	public Long decrBy(String key, long integer) {
		return jedisCluster.decrBy(key, integer);
	}

	@Override
	public Long decr(String key) {
		return jedisCluster.decr(key);
	}

	@Override
	public Long incrBy(String key, long integer) {
		return jedisCluster.incrBy(key, integer);
	}

	@Override
	public Double incrByFloat(String key, double value) {
		return jedisCluster.incrByFloat(key, value);
	}

	@Override
	public Long incr(String key) {
		return jedisCluster.incr(key);
	}

	@Override
	public Long append(String key, String value) {
		return jedisCluster.append(key, value);
	}

	@Override
	public String substr(String key, int start, int end) {
		return jedisCluster.substr(key, start, end);
	}

	@Override
	public Long hset(String key, String field, String value) {
		return jedisCluster.hset(key, field, value);
	}

	@Override
	public String hget(String key, String field) {
		return jedisCluster.hget(key, field);
	}

	@Override
	public Long hsetnx(String key, String field, String value) {
		return jedisCluster.hsetnx(key, field, value);
	}

	@Override
	public String hmset(String key, Map<String, String> hash) {
		return jedisCluster.hmset(key, hash);
	}

	@Override
	public List<String> hmget(String key, String... fields) {
		return jedisCluster.hmget(key, fields);
	}

	@Override
	public Long hincrBy(String key, String field, long value) {
		return jedisCluster.hincrBy(key, field, value);
	}

	@Override
	public Double hincrByFloat(String key, String field, double value) {
		return jedisCluster.hincrByFloat(key, field, value);
	}

	@Override
	public Boolean hexists(String key, String field) {
		return jedisCluster.hexists(key, field);
	}

	@Override
	public Long hdel(String key, String... field) {
		return jedisCluster.hdel(key, field);
	}

	@Override
	public Long hlen(String key) {
		return jedisCluster.hlen(key);
	}

	@Override
	public Set<String> hkeys(String key) {
		return jedisCluster.hkeys(key);
	}

	@Override
	public List<String> hvals(String key) {
		return jedisCluster.hvals(key);
	}

	@Override
	public Map<String, String> hgetAll(String key) {
		return jedisCluster.hgetAll(key);
	}

	@Override
	public Long rpush(String key, String... string) {
		return jedisCluster.rpush(key, string);
	}

	@Override
	public Long lpush(String key, String... string) {
		return jedisCluster.lpush(key, string);
	}

	@Override
	public Long llen(String key) {
		return jedisCluster.llen(key);
	}

	@Override
	public List<String> lrange(String key, long start, long end) {
		return jedisCluster.lrange(key, start, end);
	}

	@Override
	public String ltrim(String key, long start, long end) {
		return jedisCluster.ltrim(key, start, end);
	}

	@Override
	public String lindex(String key, long index) {
		return jedisCluster.lindex(key, index);
	}

	@Override
	public String lset(String key, long index, String value) {
		return jedisCluster.lset(key, index, value);
	}

	@Override
	public Long lrem(String key, long count, String value) {
		return jedisCluster.lrem(key, count, value);
	}

	@Override
	public String lpop(String key) {
		return jedisCluster.lpop(key);
	}

	@Override
	public String rpop(String key) {
		return jedisCluster.rpop(key);
	}

	@Override
	public Long sadd(String key, String... member) {
		return jedisCluster.sadd(key, member);
	}

	@Override
	public Set<String> smembers(String key) {
		return jedisCluster.smembers(key);
	}

	@Override
	public Long srem(String key, String... member) {
		return jedisCluster.srem(key, member);
	}

	@Override
	public String spop(String key) {
		return jedisCluster.spop(key);
	}

	@Override
	public Set<String> spop(String key, long count) {
		return jedisCluster.spop(key, count);
	}

	@Override
	public Long scard(String key) {
		return jedisCluster.scard(key);
	}

	@Override
	public Boolean sismember(String key, String member) {
		return jedisCluster.sismember(key, member);
	}

	@Override
	public String srandmember(String key) {
		return jedisCluster.srandmember(key);
	}

	@Override
	public List<String> srandmember(String key, int count) {
		return jedisCluster.srandmember(key, count);
	}

	@Override
	public Long strlen(String key) {
		return jedisCluster.strlen(key);
	}

	@Override
	public Long zadd(String key, double score, String member) {
		return jedisCluster.zadd(key, score, member);
	}

	@Override
	public Long zadd(String key, double score, String member, ZAddParams params) {
		return jedisCluster.zadd(key, score, member, params);
	}

	@Override
	public Long zadd(String key, Map<String, Double> scoreMembers) {
		return jedisCluster.zadd(key, scoreMembers);
	}

	@Override
	public Long zadd(String key, Map<String, Double> scoreMembers, ZAddParams params) {
		return jedisCluster.zadd(key, scoreMembers, params);
	}

	@Override
	public Set<String> zrange(String key, long start, long end) {
		return jedisCluster.zrange(key, start, end);
	}

	@Override
	public Long zrem(String key, String... member) {
		return jedisCluster.zrem(key, member);
	}

	@Override
	public Double zincrby(String key, double score, String member) {
		return jedisCluster.zincrby(key, score, member);
	}

	@Override
	public Double zincrby(String key, double score, String member, ZIncrByParams params) {
		return jedisCluster.zincrby(key, score, member, params);
	}

	@Override
	public Long zrank(String key, String member) {
		return jedisCluster.zrank(key, member);
	}

	@Override
	public Long zrevrank(String key, String member) {
		return jedisCluster.zrevrank(key, member);
	}

	@Override
	public Set<String> zrevrange(String key, long start, long end) {
		return jedisCluster.zrevrange(key, start, end);
	}

	@Override
	public Set<Tuple> zrangeWithScores(String key, long start, long end) {
		return jedisCluster.zrangeWithScores(key, start, end);
	}

	@Override
	public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
		return jedisCluster.zrevrangeWithScores(key, start, end);
	}

	@Override
	public Long zcard(String key) {
		return jedisCluster.zcard(key);
	}

	@Override
	public Double zscore(String key, String member) {
		return jedisCluster.zscore(key, member);
	}

	@Override
	public List<String> sort(String key) {
		return jedisCluster.sort(key);
	}

	@Override
	public List<String> sort(String key, SortingParams sortingParameters) {
		return jedisCluster.sort(key, sortingParameters);
	}

	@Override
	public Long zcount(String key, double min, double max) {
		return jedisCluster.zcount(key, min, max);
	}

	@Override
	public Long zcount(String key, String min, String max) {
		return jedisCluster.zcount(key, min, max);
	}

	@Override
	public Set<String> zrangeByScore(String key, double min, double max) {
		return jedisCluster.zrangeByScore(key, min, max);
	}

	@Override
	public Set<String> zrangeByScore(String key, String min, String max) {
		return jedisCluster.zrangeByScore(key, min, max);
	}

	@Override
	public Set<String> zrevrangeByScore(String key, double max, double min) {
		return jedisCluster.zrevrangeByScore(key, max, min);
	}

	@Override
	public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
		return jedisCluster.zrangeByScore(key, min, max, offset, count);
	}

	@Override
	public Set<String> zrevrangeByScore(String key, String max, String min) {
		return jedisCluster.zrevrangeByScore(key, max, min);
	}

	@Override
	public Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {
		return jedisCluster.zrangeByScore(key, min, max, offset, count);
	}

	@Override
	public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
		return jedisCluster.zrevrangeByScore(key, max, min, offset, count);
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
		return jedisCluster.zrangeByScoreWithScores(key, min, max);
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
		return jedisCluster.zrevrangeByScoreWithScores(key, max, min);
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
		return jedisCluster.zrangeByScoreWithScores(key, min, max, offset, count);
	}

	@Override
	public Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {
		return jedisCluster.zrevrangeByScore(key, max, min, offset, count);
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
		return jedisCluster.zrangeByScoreWithScores(key, min, max);
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min) {
		return jedisCluster.zrevrangeByScoreWithScores(key, max, min);
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max, int offset, int count) {
		return jedisCluster.zrangeByScoreWithScores(key, min, max, offset, count);
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
		return jedisCluster.zrevrangeByScoreWithScores(key, max, min, offset, count);
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) {
		return jedisCluster.zrevrangeByScoreWithScores(key, max, min, offset, count);
	}

	@Override
	public Long zremrangeByRank(String key, long start, long end) {
		return jedisCluster.zremrangeByRank(key, start, end);
	}

	@Override
	public Long zremrangeByScore(String key, double start, double end) {
		return jedisCluster.zremrangeByScore(key, start, end);
	}

	@Override
	public Long zremrangeByScore(String key, String start, String end) {
		return jedisCluster.zremrangeByScore(key, start, end);
	}

	@Override
	public Long zlexcount(String key, String min, String max) {
		return jedisCluster.zlexcount(key, min, max);
	}

	@Override
	public Set<String> zrangeByLex(String key, String min, String max) {
		return jedisCluster.zrangeByLex(key, min, max);
	}

	@Override
	public Set<String> zrangeByLex(String key, String min, String max, int offset, int count) {
		return jedisCluster.zrangeByLex(key, min, max, offset, count);
	}

	@Override
	public Set<String> zrevrangeByLex(String key, String max, String min) {
		return jedisCluster.zrevrangeByLex(key, max, min);
	}

	@Override
	public Set<String> zrevrangeByLex(String key, String max, String min, int offset, int count) {
		return jedisCluster.zrevrangeByLex(key, max, min, offset, count);
	}

	@Override
	public Long zremrangeByLex(String key, String min, String max) {
		return jedisCluster.zremrangeByLex(key, min, max);
	}

	@Override
	public Long linsert(String key, LIST_POSITION where, String pivot, String value) {
		return jedisCluster.linsert(key, where, pivot, value);
	}

	@Override
	public Long lpushx(String key, String... string) {
		return jedisCluster.lpushx(key, string);
	}

	@Override
	public Long rpushx(String key, String... string) {
		return jedisCluster.rpushx(key, string);
	}

	@Override
	public List<String> blpop(String arg) {
		return jedisCluster.blpop(arg);
	}

	@Override
	public List<String> blpop(int timeout, String key) {
		return jedisCluster.blpop(timeout, key);
	}

	@Override
	public List<String> brpop(String arg) {
		return jedisCluster.brpop(arg);
	}

	@Override
	public List<String> brpop(int timeout, String key) {
		return jedisCluster.brpop(timeout, key);
	}

	@Override
	public Long del(String key) {
		return jedisCluster.del(key);
	}

	@Override
	public String echo(String string) {
		return jedisCluster.echo(string);
	}

	@Override
	public Long move(String key, int dbIndex) {
		return jedisCluster.move(key, dbIndex);
	}

	@Override
	public Long bitcount(String key) {
		return jedisCluster.bitcount(key);
	}

	@Override
	public Long bitcount(String key, long start, long end) {
		return jedisCluster.bitcount(key, start, end);
	}

	@Override
	public Long bitpos(String key, boolean value) {
		return jedisCluster.bitpos(key, value);
	}

	@Override
	public Long bitpos(String key, boolean value, BitPosParams params) {
		return jedisCluster.bitpos(key, value, params);
	}

	@Override
	public ScanResult<Entry<String, String>> hscan(String key, int cursor) {
		return jedisCluster.hscan(key, cursor);
	}

	@Override
	public ScanResult<String> sscan(String key, int cursor) {
		return jedisCluster.sscan(key, cursor);
	}

	@Override
	public ScanResult<Tuple> zscan(String key, int cursor) {
		return jedisCluster.zscan(key, cursor);
	}

	@Override
	public ScanResult<Entry<String, String>> hscan(String key, String cursor) {
		return jedisCluster.hscan(key, cursor);
	}

	@Override
	public ScanResult<Entry<String, String>> hscan(String key, String cursor, ScanParams params) {
		return jedisCluster.hscan(key, cursor, params);
	}

	@Override
	public ScanResult<String> sscan(String key, String cursor) {
		return jedisCluster.sscan(key, cursor);
	}

	@Override
	public ScanResult<String> sscan(String key, String cursor, ScanParams params) {
		return jedisCluster.sscan(key, cursor, params);
	}

	@Override
	public ScanResult<Tuple> zscan(String key, String cursor) {
		return jedisCluster.zscan(key, cursor);
	}

	@Override
	public ScanResult<Tuple> zscan(String key, String cursor, ScanParams params) {
		return jedisCluster.zscan(key, cursor, params);
	}

	@Override
	public Long pfadd(String key, String... elements) {
		return jedisCluster.pfadd(key, elements);
	}

	@Override
	public long pfcount(String key) {
		return jedisCluster.pfcount(key);
	}

	@Override
	public Long geoadd(String key, double longitude, double latitude, String member) {
		return jedisCluster.geoadd(key, longitude, latitude, member);
	}

	@Override
	public Long geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap) {
		return jedisCluster.geoadd(key, memberCoordinateMap);
	}

	@Override
	public Double geodist(String key, String member1, String member2) {
		return jedisCluster.geodist(key, member1, member2);
	}

	@Override
	public Double geodist(String key, String member1, String member2, GeoUnit unit) {
		return jedisCluster.geodist(key, member1, member2, unit);
	}

	@Override
	public List<String> geohash(String key, String... members) {
		return jedisCluster.geohash(key, members);
	}

	@Override
	public List<GeoCoordinate> geopos(String key, String... members) {
		return jedisCluster.geopos(key, members);
	}

	@Override
	public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit) {
		return jedisCluster.georadius(key, longitude, latitude, radius, unit);
	}

	@Override
	public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit,
			GeoRadiusParam param) {
		return jedisCluster.georadius(key, longitude, latitude, radius, unit, param);
	}

	@Override
	public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit) {
		return jedisCluster.georadiusByMember(key, member, radius, unit);
	}

	@Override
	public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit,
			GeoRadiusParam param) {
		return jedisCluster.georadiusByMember(key, member, radius, unit, param);
	}

	@Override
	public List<Long> bitfield(String key, String... arguments) {
		return jedisCluster.bitfield(key, arguments);
	}

}
