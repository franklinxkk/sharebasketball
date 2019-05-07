package com.bblanqiu.common.cache;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public abstract class RedisService {
	@SuppressWarnings("rawtypes")
	@Resource(name="stringRedisTemplate")
	private RedisTemplate redisTemplate;
	
	@SuppressWarnings("unchecked")
	public <T> ValueOperations<String,  T> getValueOperations(){
		return redisTemplate.opsForValue();
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @param timeout seconds
	 */
	public void addValue(String key, Object value, Long timeout){
		try {
			if(timeout != null){
				getValueOperations().set(key, value, timeout,TimeUnit.SECONDS);
			}else{
				getValueOperations().set(key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public Object getValue(String key){
		Object o = null;
		try {
			return getValueOperations().get(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void addHashValue(String key, Map<String, Object> m){
		redisTemplate.opsForHash().putAll(key, m);
	}
	public Object getHashValue(String key, String hashKey){
		return redisTemplate.opsForHash().get(key, hashKey);
	}
	
	@SuppressWarnings("unchecked")
	public void del(String key){
		redisTemplate.delete(key);
	}
	
}
