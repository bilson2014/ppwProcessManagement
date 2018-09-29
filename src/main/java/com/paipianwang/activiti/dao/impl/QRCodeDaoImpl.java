package com.paipianwang.activiti.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.paipianwang.activiti.dao.QRCodeDao;
import com.paipianwang.pat.common.util.ValidateUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Repository(value = "qrCodeDao")
public class QRCodeDaoImpl implements QRCodeDao {

	@Autowired
	private final JedisPool pool = null;

	
	/**
	 * 保存缓存数据  带过期时间
	 */
	@Override
	public void setData(String key, int seconds, String data) {
		
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			if(ValidateUtil.isValid(data)){
				jedis.setex(key, seconds, data);	
			}			
		} catch (Exception e) {
			// do something for logger
			throw new RuntimeException(e);
		} finally {
			if(jedis != null){
				jedis.disconnect();
				jedis.close();
			}
		}

	}
	/**
	 * 获取缓存数据
	 */
	@Override
	public String getData(String key){
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			String str = jedis.get(key);
			return str;
		} catch (Exception e) {
			// do something for logger
			throw new RuntimeException(e);
		} finally {
			if(jedis != null){
				jedis.disconnect();
				jedis.close();
			}
		}
	}
	
	/**
	 * 设置过期时间
	 */
	@Override
	public void setExpire(String key,int expireSeconds) {
		
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.expire(key, expireSeconds);			
		} catch (Exception e) {
			// do something for logger
			throw new RuntimeException(e);
		} finally {
			if(jedis != null){
				jedis.disconnect();
				jedis.close();
			}
		}
	}
	@Override
	public Long deleteData(String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.del(key);
		} catch (Exception e) {
			// do something for logger
			throw new RuntimeException(e);
		} finally {
			if(jedis != null){
				jedis.disconnect();
				jedis.close();
			}
		}
	}
}
