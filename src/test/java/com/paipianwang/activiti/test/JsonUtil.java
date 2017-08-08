package com.paipianwang.activiti.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.paipianwang.pat.workflow.entity.PmsVacation;

public class JsonUtil {

	@Test
	public void test() {
		Map<String, String> param = new HashMap<>();
		param.put("userId", 1 + "");
		param.put("startDate", "2017-08-01");
		param.put("endDate", "2017-08-05");
		param.put("reason", "调休");
		param.put("pId", "p12");
		
		String json = JSON.toJSONString(param);
		System.err.println(json);
		PmsVacation va = JSON.parseObject(json, PmsVacation.class);
		System.err.println(va);
		
		// 取出key value
		Set<String> strs = param.keySet();
		List<String> list = new ArrayList<String>(strs);
		System.err.println(list);
		
		Collection<String> values = param.values();
		System.err.println(values);
	}
}
