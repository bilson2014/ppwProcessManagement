package com.paipianwang.activiti.task.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.impl.form.TaskFormDataImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;

import com.paipianwang.pat.workflow.enums.ProjectTeamType;
import com.paipianwang.pat.workflow.facade.PmsProjectTeamFacade;

/**
 * 【供应商管家】分配制作供应商
 * 
 * @author jacky
 *
 */
@Component("allotProductTeamTaskLisnter")
public class AllotProductTeamTaskLisnter implements TaskListener {

	private static final long serialVersionUID = -2204490453039201351L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// taskId
		final String taskId = delegateTask.getId();

		// projectId
		final String projectId = delegateTask.getExecution().getProcessBusinessKey();
		
		// 获取form表单信息
		FormService formService = delegateTask.getExecution().getEngineServices().getFormService();
		TaskFormDataImpl formData = (TaskFormDataImpl) formService.getTaskFormData(taskId);
		List<FormProperty> formList = formData.getFormProperties();
		if (formList != null && !formList.isEmpty()) {
			Map<String, Object> param = new HashMap<String, Object>();
			for (final FormProperty formProperty : formList) {
				String key = formProperty.getId();
				if (StringUtils.defaultString(key).startsWith("pt_")) {
					param.put(key.split("_")[1], formProperty.getValue());
				}
			}

			// 保存关于策划供应商的信息
			if (param != null && !param.isEmpty()) {
				// 预置字段
				param.put("teamType", ProjectTeamType.produce.getCode()); // 策划供应商
				param.put("projectId", projectId); // projectId
				
				ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
				PmsProjectTeamFacade pmsProjectTeamFacade = (PmsProjectTeamFacade) context
						.getBean("pmsProjectTeamFacade");
				long projectTeamId = pmsProjectTeamFacade.insert(param);
				delegateTask.setVariable("teamProductId", param.get("teamId")); // 设置供应商唯一ID
				delegateTask.setVariable("projectTeam_produce", projectTeamId); // 设置策划供应商的唯一ID
			}
		}
	}

}
