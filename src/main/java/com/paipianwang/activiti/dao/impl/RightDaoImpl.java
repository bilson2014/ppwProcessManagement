/*package com.paipianwang.activiti.dao.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.paipianwang.activiti.dao.RightDao;
import com.paipianwang.activiti.utils.RedisUtils;
import com.paipianwang.pat.common.constant.PmsConstant;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.right.entity.PmsRight;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Repository(value = "rightDao")
public class RightDaoImpl implements RightDao {

	@Autowired
	private final JedisPool pool = null;
	
	public PmsRight getRightFromRedis(final String uri) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			String str = jedis.hget(PmsConstant.CONTEXT_RIGHT_MAP, uri);
			final PmsRight right = RedisUtils.fromJson(str,PmsRight.class);
			return right;
		} catch (Exception e) {
			// do something for logger
		} finally {
			if(jedis != null){
				jedis.disconnect();
				jedis.close();
			}
		}
		
		return null;
	}

	public Map<String, PmsRight> getRightsFromRedis() {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			Map<String,String> map = jedis.hgetAll(PmsConstant.CONTEXT_RIGHT_MAP);
			if(ValidateUtil.isValid(map)){
				final Map<String,PmsRight> rightMap = RedisUtils.fromJson(map);
				return rightMap;
			}
			
			return null;
		} catch (Exception e) {
			// do something for logger
		} finally {
			if(jedis != null){
				jedis.disconnect();
				jedis.close();
			}
		}
		
		return null;
	}


}
*/