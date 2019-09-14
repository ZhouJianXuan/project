package com.sise.graduation.common.redis.cache;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@SuppressWarnings({"rawtypes","unchecked"})
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;
    
    public RedisConnection getRedisConnection() {
    	return redisTemplate.getConnectionFactory().getConnection();
    }
    
    /**
     * 不建议的方法，会导致redis卡顿。
     * 尽量少用，不用，替代方法用一个key来保存
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern) {
        return (Set<String>) redisTemplate.keys(pattern);
    }

    public long incrementAndGet(final String key,long value) {
       try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            return operations.increment(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public long incrementAndGet(final String key,long value,long expireTime) {
    	try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            long incrementNum = operations.increment(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            return incrementNum;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public boolean setnx(final String key, Object value, Date expireDate) {
    	boolean result = false;
    	try {
    		ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
    		operations.setIfAbsent(key, value);
    		redisTemplate.expireAt(key, expireDate);
    		result = true;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return result;
	}
    
    public boolean setnx(final String key,Object value,long second) {
    	boolean result = false;
    	try {
    		ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
    		result = operations.setIfAbsent(key, value);
    		if (result) {
    			redisTemplate.expire(key, second, TimeUnit.SECONDS);
    		}
    	} catch (Exception e) {
    		log.error("", e);
    	}
    	return result;
    }
    
    public boolean expire(final String key, Long expireTime) {
    	return redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
    }

    /**
     * 写入缓存设置时效时间
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 批量删除对应的value
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     * @param pattern
     */
    public void removePattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if ((keys != null ? keys.size() : 0) > 0) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 删除对应的value
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否有对应的value
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     * @param key
     * @return
     */
    public Object get(final String key) {
        Object result;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    public <T> T get(final String key, Class<T> clazz) {
        Object result = get(key);
        if (null == result) {
            return null;
        }
        String objStr = result.toString();
        if (StringUtils.isEmpty(objStr)) {
            return null;
        }
        return JSONObject.parseObject(objStr, clazz);
    }

    /**
     * 哈希 添加
     * @param key
     * @param hashKey
     * @param value
     */
    public void hmSet(String key, Object hashKey, Object value) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.put(key, hashKey, value);
    }
    
    public void hputAll(String key,Map<? extends Object, ? extends Object> entries) {
    	HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.putAll(key, entries);
    }

    public void hputAll(String key,Map<? extends Object, ? extends Object> entries, long expireTime) {
    	HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.putAll(key, entries);
        redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
    }


    public Boolean hmSetnx(String key, Object hashKey, Object value) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.putIfAbsent(key, hashKey, value);
    }
    
	public Boolean hmSetnx(String key, Object hashKey, Object value, long expireTime) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        boolean putSuccess = hash.putIfAbsent(key, hashKey, value);
        if (putSuccess) {
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        }
        return putSuccess;
    }
	
    /**
     * 哈希 添加
     * @param key
     * @param hashKey
     * @param value
     * @param expireTime
     */
    public void hmSet(String key,Object hashKey,Object value,long expireTime) {
    	HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.put(key, hashKey, value);
        redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
    }


    public Long hincrBy(String key, Object hashKey, long value) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.increment(key, hashKey, value);
    }

    /**
     * 哈希获取数据
     * @param key
     * @param hashKey
     * @return
     */
    public Object hmGet(String key, Object hashKey) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.get(key, hashKey);
    }
    
    public boolean hexists(String key, Object hashKey) {
    	HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
    	return hash.hasKey(key, hashKey);
    }
    
    public long hsize(String key) {
    	HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
    	return hash.size(key);
    }
    
    public void hremove(String key,Object hashKey) {
    	 HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
    	 hash.delete(key, hashKey);
    }
    
    public Map<Object,Object> entries(String key) {
    	HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
    	return hash.entries(key);
    }
    
    public void hremoveAll(String redisKey,Map<Object,Object> map ) {
    	if (CollectionUtils.isEmpty(map)) {
            return;
        }
    	HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
    	Object[] array = new Object[map.size()];
    	int i = 0;
    	for (Map.Entry<Object, Object> entry : map.entrySet()) {
    		array[i] = entry.getKey();
    		i++;
    	}
    	hash.delete(redisKey, array);
    }

    /**
     * 列表添加
     * @param k
     * @param v
     */
    public void lPush(String k, Object v) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.leftPush(k, v);
    }
    
    public Object rpop(String k) {
    	ListOperations<String, Object> list = redisTemplate.opsForList();
    	return list.rightPop(k);
    }
    
    public Object rpop(String k,long second) {
    	ListOperations<String, Object> list = redisTemplate.opsForList();
    	return list.rightPop(k, second, TimeUnit.SECONDS);
    }
    
    public long listSize(String key) {
    	ListOperations<String, Object> list = redisTemplate.opsForList();
    	return list.size(key);
    }
    
    /**
     * 列表获取
     * @param k key
     * @param l start
     * @param l1 end
     * @return
     */
    public List<Object> range(String k, long l, long l1) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.range(k, l, l1);
    }

    /**
     * 判断是否处于集合中
     * @param key
     * @param value
     * @return
     */
    public boolean isMember(String key,String value) {
    	SetOperations<String, String> set = redisTemplate.opsForSet();
    	return set.isMember(key, value);
    }
    
    /**
     * 集合添加
     * @param key
     * @param value
     */
    public void sadd(String key, String value) {
        SetOperations<String, String> set = redisTemplate.opsForSet();
        set.add(key, value);
    }

    /**
     * 集合获取
     * @param key
     * @return
     */
    public Set<Object> smembers(String key) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.members(key);
    }
    
    public void sremove(String key,Object...values) {
    	SetOperations<String, Object> set = redisTemplate.opsForSet();
    	set.remove(key, values);
    }

    public void sremove(String key, String value) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        set.remove(key, value);
    }

    /**
     * 有序集合添加
     * @param key
     * @param value
     * @param score
     */
    public void zAdd(String key, Object value, double score) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.add(key, value, score);
    }

    /**
     * 有序集合获取
     * @param key
     * @param score
     * @param score1
     * @return
     */
    public Set<Object> zrangeByScore(String key, double score, double score1) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rangeByScore(key, score, score1);
    }
    
    public Set<Object> zreverseRange(String key,long start,long end) {
    	ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
    	return zset.reverseRange(key, start, end);
    }
    
    public Set<Object> zrange(String key,long start,long end) {
    	ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
    	return zset.range(key, start, end);
    }
    
    public void zremove(String key, Object...values ) {
    	ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
    	zset.remove(key, values);
    }
    
    public void zremoveRange(String key,long start,long end) {
    	ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
    	zset.removeRange(key, start, end);
    }
    
    public long zcard(String key) {
    	ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
    	return zset.zCard(key);
    }
}
