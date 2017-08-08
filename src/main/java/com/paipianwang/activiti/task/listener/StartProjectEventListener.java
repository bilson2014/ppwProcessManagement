package com.paipianwang.activiti.task.listener;

import java.io.Serializable;

import org.activiti.engine.IdentityService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.springframework.stereotype.Component;

/**
 * 项目启动监听
 * @author jacky
 *
 */
@Component("startProjectEventListener")
public class StartProjectEventListener implements JavaDelegate,Serializable {

	private static final long serialVersionUID = -5886397723124115854L;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		final IdentityService identityService = execution.getEngineServices().getIdentityService();
		
		UserQuery userQuery = identityService.createUserQuery();
		// 销售总监 -- 张立虎
		User saleDirector = userQuery.memberOfGroup("saleDirector").singleResult();
		execution.setVariable("saleDirectorId", saleDirector.getId());
		
		// 创意总监 -- 刘超
		User creativityDirector = userQuery.memberOfGroup("creativityDirector").singleResult();
		execution.setVariable("creativityDirectorId", creativityDirector.getId());
		
		// 供应商管家 -- 王壹
		User teamProvider = userQuery.memberOfGroup("teamProvider").singleResult(); 
		execution.setVariable("teamProviderId", teamProvider.getId());
		
		// 财务 -- 郭芳
		User finance = userQuery.memberOfGroup("finance").singleResult(); 
		execution.setVariable("financeId", finance.getId());
		
		// 监制总监 -- 夏攀
		User superviseDirector = userQuery.memberOfGroup("superviseDirector").singleResult(); 
		execution.setVariable("superviseDirectorId", superviseDirector.getId());
		
		// 供应商采购 -- 陈景娜
		User teamPurchase = userQuery.memberOfGroup("teamPurchase").singleResult(); 
		execution.setVariable("teamPurchaseId", teamPurchase.getId());
		
		// 供应商总监 -- 刘峰
		User teamDirector = userQuery.memberOfGroup("teamDirector").singleResult(); 
		execution.setVariable("teamDirectorId", teamDirector.getId());
		
		// 财务总监 -- 杨巍
		User financeDirector = userQuery.memberOfGroup("financeDirector").singleResult(); 
		execution.setVariable("financeDirectorId", financeDirector.getId());
		
		// 客服总监 -- 闫雪琴
		User customerDirector = userQuery.memberOfGroup("customerDirector").singleResult(); 
		execution.setVariable("customerDirectorId", customerDirector.getId());
		
	}

}
