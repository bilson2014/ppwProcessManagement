package com.paipianwang.activiti.task.listener;

import java.io.Serializable;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;

import com.paipianwang.pat.workflow.entity.PmsProjectSynergy;
import com.paipianwang.pat.workflow.enums.ProjectRoleType;
import com.paipianwang.pat.workflow.facade.PmsProjectSynergyFacade;

/**
 * 项目启动监听
 * 
 * @author jacky
 *
 */
@Component("startProjectEventListener")
public class StartProjectEventListener implements JavaDelegate, Serializable {

	private static final long serialVersionUID = -5886397723124115854L;

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		// 项目编号
		final String projectId = execution.getProcessBusinessKey();

		ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		PmsProjectSynergyFacade pmsProjectSynergyFacade = (PmsProjectSynergyFacade) context
				.getBean("pmsProjectSynergyFacade");

		// 前缀
		final String suffix = "employee_";

		// 查询协同人列表
		Map<String, PmsProjectSynergy> synergyMap = pmsProjectSynergyFacade.getSynergysByProjectId(projectId);

		// 销售总监
		String saleDirectorId = suffix + synergyMap.get(ProjectRoleType.saleDirector.getId()).getEmployeeId();
		execution.setVariable("saleDirectorId", saleDirectorId);

		// 创意总监
		String creativityDirectorId = suffix
				+ synergyMap.get(ProjectRoleType.creativityDirector.getId()).getEmployeeId();
		execution.setVariable("creativityDirectorId", creativityDirectorId);

		// 供应商管家
		String teamProviderId = suffix + synergyMap.get(ProjectRoleType.teamProvider.getId()).getEmployeeId();
		execution.setVariable("teamProviderId", teamProviderId);

		// 财务
		String financeId = suffix + synergyMap.get(ProjectRoleType.finance.getId()).getEmployeeId();
		execution.setVariable("financeId", financeId);

		// 监制总监
		String superviseDirectorId = suffix + synergyMap.get(ProjectRoleType.superviseDirector.getId()).getEmployeeId();
		execution.setVariable("superviseDirectorId", superviseDirectorId);

		// 供应商采购
		String teamPurchaseId = suffix + synergyMap.get(ProjectRoleType.teamPurchase.getId()).getEmployeeId();
		execution.setVariable("teamPurchaseId", teamPurchaseId);

		// 供应商总监
		String teamDirectorId = suffix + synergyMap.get(ProjectRoleType.teamDirector.getId()).getEmployeeId();
		execution.setVariable("teamDirectorId", teamDirectorId);

		// 财务总监
		String financeDirectorId = suffix + synergyMap.get(ProjectRoleType.financeDirector.getId()).getEmployeeId();
		execution.setVariable("financeDirectorId", financeDirectorId);

		// 客服总监
		String customerDirectorId = suffix + synergyMap.get(ProjectRoleType.customerDirector.getId()).getEmployeeId();
		execution.setVariable("customerDirectorId", customerDirectorId);

	}

}
