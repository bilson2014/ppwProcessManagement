package com.paipianwang.activiti.task.listener;

import java.io.Serializable;

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
 * 【监制总监】分配监制
 * 
 * @author jacky
 *
 */

@Component("allotSuperviseTaskListener")
public class AllotSuperviseTaskListener implements TaskListener, Serializable {

	private static final long serialVersionUID = 8310410877334719308L;

	@Override
	public void notify(DelegateTask delegateTask) {
		String superviseId = (String) delegateTask.getVariable("superviseId");
		String projectId = delegateTask.getExecution().getProcessBusinessKey();
		if (StringUtils.isNotBlank(superviseId)) {
			// 策划ID
			Integer synergyId = Integer.parseInt(superviseId.split("_")[1]);
			// 保存协同人信息
			ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
			PmsEmployeeFacade pmsEmployeeFacade = (PmsEmployeeFacade) context.getBean("pmsEmployeeFacade");

			PmsProjectSynergyFacade pmsProjectSynergyFacade = (PmsProjectSynergyFacade) context
					.getBean("pmsProjectSynergyFacade");

			// 获取人员信息
			PmsEmployee employee = pmsEmployeeFacade.findEmployeeById(synergyId);

			PmsProjectSynergy projectSynergy = new PmsProjectSynergy();
			final String groupId = ProjectRoleType.supervise.getId();
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
