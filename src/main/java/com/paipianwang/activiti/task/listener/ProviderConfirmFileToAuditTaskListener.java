package com.paipianwang.activiti.task.listener;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;

import com.paipianwang.pat.workflow.entity.PmsProjectSynergy;
import com.paipianwang.pat.workflow.enums.ProjectRoleType;
import com.paipianwang.pat.workflow.facade.PmsProjectSynergyFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectTeamFacade;

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
		final String projectId = delegateTask.getExecution().getProcessBusinessKey();
		final List<String> users = new ArrayList<String>();
		
		ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		PmsProjectSynergyFacade pmsProjectSynergyFacade = (PmsProjectSynergyFacade) context
				.getBean("pmsProjectSynergyFacade");
		
		// 销售总监
		List<PmsProjectSynergy> saleDirectors = pmsProjectSynergyFacade.getSynergys(projectId, ProjectRoleType.saleDirector.getId());
		if(saleDirectors != null && !saleDirectors.isEmpty())
			users.add("employee_" + saleDirectors.get(0).getEmployeeId());
		
		// 监制总监
		List<PmsProjectSynergy> superviseDirectors = pmsProjectSynergyFacade.getSynergys(projectId, ProjectRoleType.superviseDirector.getId());
		if(superviseDirectors != null && !superviseDirectors.isEmpty())
			users.add("employee_" + superviseDirectors.get(0).getEmployeeId());
		
		// 项目负责人
		String applyUserId = (String) delegateTask.getVariable("applyUserId");
		if(StringUtils.isNotBlank(applyUserId)) 
			users.add(applyUserId);
		
		delegateTask.setVariable("users", users);
	}

}
