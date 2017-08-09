package com.paipianwang.activiti.task.listener;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 供应商上传水印样片监听
 * 	确认多方联合内审参与人
 * @author jacky
 *
 */
@Component("providerConfirmFileToAuditTaskListener")
public class ProviderConfirmFileToAuditTaskListener implements TaskListener {

	private static final long serialVersionUID = -9037097182002871515L;

	@Override
	public void notify(DelegateTask delegateTask) {
		IdentityService identityService = delegateTask.getExecution().getEngineServices().getIdentityService();
		UserQuery userQuery = identityService.createUserQuery();
		final List<String> users = new ArrayList<String>();
		// 销售总监
		User saleDirector = userQuery.memberOfGroup("saleDirector").singleResult();
		if(saleDirector != null)
			users.add(saleDirector.getId());
		// 监制总监
		User superviseDirector = userQuery.memberOfGroup("superviseDirector").singleResult(); 
		if(superviseDirector != null)
			users.add(superviseDirector.getId());
		
		// 项目负责人
		String applyUserId = (String) delegateTask.getVariable("applyUserId");
		if(StringUtils.isNotBlank(applyUserId)) 
			users.add(applyUserId);
		
		delegateTask.setVariable("users", users);
	}

}
