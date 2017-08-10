package com.paipianwang.activiti.resources.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paipianwang.activiti.domin.BaseMsg;
import com.paipianwang.pat.workflow.enums.ProjectRoleType;

/**
 * 协同人控制器
 * @author jacky
 *
 */
@RestController
@RequestMapping("/synergy")
public class SynergyController extends BaseController{
	
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
	public BaseMsg getGroupMember(HttpServletRequest request) {
		BaseMsg msg = new BaseMsg();
		System.err.println(getCurrentInfo(request));
		Map<String, Object> memberGroup = new HashMap<String, Object>();
		
		// 客户总监
		List<User> customerDirectors = identityService.createUserQuery().memberOfGroup(ProjectRoleType.customerDirector.getId()).list();
		memberGroup.put(ProjectRoleType.customerDirector.getId(), customerDirectors);
		
		// 销售总监
		List<User> saleDirectors = identityService.createUserQuery().memberOfGroup(ProjectRoleType.saleDirector.getId()).list();
		memberGroup.put(ProjectRoleType.saleDirector.getId(), saleDirectors);
		
		// 创意总监
		List<User> creativityDirectors = identityService.createUserQuery().memberOfGroup(ProjectRoleType.creativityDirector.getId()).list();
		memberGroup.put(ProjectRoleType.creativityDirector.getId(), creativityDirectors);
		
		// 监制总监
		List<User> superviseDirectors = identityService.createUserQuery().memberOfGroup(ProjectRoleType.superviseDirector.getId()).list();
		memberGroup.put(ProjectRoleType.superviseDirector.getId(), superviseDirectors);
		
		// 供应商总监
		List<User> teamDirectors = identityService.createUserQuery().memberOfGroup(ProjectRoleType.teamDirector.getId()).list();
		memberGroup.put(ProjectRoleType.teamDirector.getId(), teamDirectors);
		
		// 供应商管家
		List<User> teamProviders = identityService.createUserQuery().memberOfGroup(ProjectRoleType.teamProvider.getId()).list();
		memberGroup.put(ProjectRoleType.teamProvider.getId(), teamProviders);
		
		// 供应商采购
		List<User> teamPurchases = identityService.createUserQuery().memberOfGroup(ProjectRoleType.teamPurchase.getId()).list();
		memberGroup.put(ProjectRoleType.teamPurchase.getId(), teamPurchases);
		
		// 财务总监
		List<User> financeDirectors = identityService.createUserQuery().memberOfGroup(ProjectRoleType.financeDirector.getId()).list();
		memberGroup.put(ProjectRoleType.financeDirector.getId(), financeDirectors);
		
		// 财务出纳
		List<User> finances = identityService.createUserQuery().memberOfGroup(ProjectRoleType.finance.getId()).list();
		memberGroup.put(ProjectRoleType.finance.getId(), finances);
		
		msg.setResult(memberGroup);
		return msg;
	}
}
