package com.paipianwang.activiti.dao;

import java.util.Map;

import com.paipianwang.pat.facade.right.entity.PmsRight;

public interface RightDao {

	public PmsRight getRightFromRedis(final String uri);
	
	public Map<String,PmsRight> getRightsFromRedis();
}
