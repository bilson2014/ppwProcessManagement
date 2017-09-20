package com.paipianwang.activiti.task.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;

import com.paipianwang.pat.facade.right.entity.PmsEmployee;
import com.paipianwang.pat.facade.right.service.PmsEmployeeFacade;
import com.paipianwang.pat.workflow.entity.PmsProjectSynergy;
import com.paipianwang.pat.workflow.enums.ProjectRoleType;
import com.paipianwang.pat.workflow.facade.PmsProjectSynergyFacade;

/**
 * 【供应商总监】分配供应商管家
 * @author jacky
 *
 */
@Component("allotTeamProviderTaskListener")
public class AllotTeamProviderTaskListener implements TaskListener {

	private static final long serialVersionUID = -6622911630202090468L;

	@Override
	public void notify(DelegateTask delegateTask) {
		String teamProviderId = (String) delegateTask.getVariable("teamProviderId");
		String projectId = delegateTask.getExecution().getProcessBusinessKey();
		if (StringUtils.isNotBlank(teamProviderId)) {
			// 策划ID
			Integer synergyId = Integer.parseInt(teamProviderId.split("_")[1]);
			// 保存协同人信息
			ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
			PmsEmployeeFacade pmsEmployeeFacade = (PmsEmployeeFacade) context.getBean("pmsEmployeeFacade");

			PmsProjectSynergyFacade pmsProjectSynergyFacade = (PmsProjectSynergyFacade) context
					.getBean("pmsProjectSynergyFacade");

			// 获取人员信息
			PmsEmployee employee = pmsEmployeeFacade.findEmployeeById(synergyId);

			PmsProjectSynergy projectSynergy = new PmsProjectSynergy();
			final String groupId = ProjectRoleType.teamProvider.getId();
			projectSynergy.setEmployeeGroup(groupId);
			projectSynergy.setEmployeeId(synergyId);
			projectSynergy.setProjectId(projectId);
			if (employee != null) {
				projectSynergy.setEmployeeName(employee.getEmployeeRealName());
				projectSynergy.setTelephone(employee.getPhoneNumber());
				projectSynergy.setImgUrl(employee.getEmployeeImg());
			}
			
			pmsProjectSynergyFacade.deleteByProjectIdAndRoleId(projectId, groupId);
			pmsProjectSynergyFacade.insert(projectSynergy);
		}

	}

}
