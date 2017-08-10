package com.paipianwang.activiti.resources.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paipianwang.activiti.domin.BaseMsg;

@RestController
@RequestMapping("/synergy")
public class SynergyController {
	
	@Autowired
	private IdentityService identityService = null;

	/**
	 * 客服总监
	 * 销售总监
	 * 创意总监
	 * 监制总监
	 * 供应商总监
	 * 供应商管家
	 * 供应商采购
	 * 财务主管
	 * 财务出纳
	 * @return
	 */
	@RequestMapping("/memberGroup")
	public BaseMsg getGroupMember() {
		BaseMsg msg = new BaseMsg();
		
		Map<String, Object> memberGroup = new HashMap<String, Object>();
		List<Group> groups = identityService.createGroupQuery().list();
		// 遍历groups 取出对应的角色
		if(groups != null && !groups.isEmpty()) {
			
		}
		List<Group> list = new ArrayList<Group>();
		
		
	}
}
