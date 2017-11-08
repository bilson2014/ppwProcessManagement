package com.paipianwang.activiti.task.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.impl.form.TaskFormDataImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;

import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.workflow.entity.PmsProjectSynergy;
import com.paipianwang.pat.workflow.enums.ProjectRoleType;
import com.paipianwang.pat.workflow.facade.PmsProjectFlowFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectSynergyFacade;

/**
 * 供应商上传水印样片监听 确认多方联合内审参与人
 * 
 * @author jacky
 *
 */
@Component("providerConfirmFileToAuditTaskListener")
public class ProviderConfirmFileToAuditTaskListener extends BaseTaskListener{

	private static final long serialVersionUID = -9037097182002871515L;

	@Override
	public void execute(DelegateTask delegateTask) {
		final String projectId = delegateTask.getExecution().getProcessBusinessKey();
		final String taskId = delegateTask.getId();
		final List<String> users = new ArrayList<String>();

		ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		PmsProjectSynergyFacade pmsProjectSynergyFacade = (PmsProjectSynergyFacade) context
				.getBean("pmsProjectSynergyFacade");

		PmsProjectFlowFacade flowFacade = (PmsProjectFlowFacade) context.getBean("pmsProjectFlowFacade");

		final Map<String, PmsProjectSynergy> synergyMap = pmsProjectSynergyFacade.getSynergysByProjectId(projectId);

		// 销售总监
		PmsProjectSynergy saleDirector = synergyMap.get(ProjectRoleType.saleDirector.getId());
		if (saleDirector != null)
			users.add("employee_" + saleDirector.getEmployeeId());

		// 监制总监
		PmsProjectSynergy superviseDirector = synergyMap.get(ProjectRoleType.superviseDirector.getId());
		if (superviseDirector != null)
			users.add("employee_" + superviseDirector.getEmployeeId());

		// 项目负责人
		PmsProjectSynergy sale = synergyMap.get(ProjectRoleType.sale.getId());
		if (sale != null)
			users.add("employee_" + sale.getEmployeeId());

		delegateTask.setVariable("users", users);

		// 更新 样片地址 以及 密码
		FormService formService = delegateTask.getExecution().getEngineServices().getFormService();
		TaskFormDataImpl formData = (TaskFormDataImpl) formService.getTaskFormData(taskId);
		List<FormProperty> formList = formData.getFormProperties();
		if (formList != null && !formList.isEmpty()) {
			Map<String, Object> param = new HashMap<String, Object>();
			for (final FormProperty formProperty : formList) {
				String key = formProperty.getId();
				if (StringUtils.defaultString(key).startsWith("pf_")) {
					param.put(key.split("_")[1], formProperty.getValue());
				}
			}
			if(ValidateUtil.isValid(param)) {
				flowFacade.update(param, projectId);
			}
		}
		
	}

}
