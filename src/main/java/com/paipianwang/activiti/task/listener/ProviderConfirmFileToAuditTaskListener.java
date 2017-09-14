package com.paipianwang.activiti.task.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;

import com.paipianwang.pat.workflow.entity.PmsProjectSynergy;
import com.paipianwang.pat.workflow.enums.ProjectRoleType;
import com.paipianwang.pat.workflow.facade.PmsProjectSynergyFacade;

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
		
		final Map<String, PmsProjectSynergy> synergyMap = pmsProjectSynergyFacade.getSynergysByProjectId(projectId);
		
		// 销售总监
		PmsProjectSynergy saleDirector = synergyMap.get(ProjectRoleType.saleDirector.getId());
		if(saleDirector != null)
			users.add("employee_" + saleDirector.getEmployeeId());
		
		// 监制总监
		PmsProjectSynergy superviseDirector = synergyMap.get(ProjectRoleType.superviseDirector.getId());
		if(superviseDirector != null)
			users.add("employee_" + superviseDirector.getEmployeeId());
		
		// 项目负责人
		PmsProjectSynergy sale = synergyMap.get(ProjectRoleType.sale.getId());
		if(sale != null)
			users.add("employee_" + sale.getEmployeeId());
		
		delegateTask.setVariable("users", users);
	}

}
