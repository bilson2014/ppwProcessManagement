package com.paipianwang.activiti.task.listener;

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

import com.paipianwang.pat.workflow.facade.PmsProjectTeamFacade;

/**
 * 【供应商采购】填写供应商结算信息 更新制作供应商
 * 
 * @author jacky
 *
 */
@Component("inputProduceTeamPayInfoListener")
public class InputProduceTeamPayInfoListener extends BaseTaskListener {

	private static final long serialVersionUID = 2547325293519236559L;

	@Override
	public void execute(DelegateTask delegateTask) {
		// taskId
		final String taskId = delegateTask.getId();

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

			// 保存关于制作供应商的信息
			if (param != null && !param.isEmpty()) {
				// 预置字段
				ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
				PmsProjectTeamFacade pmsProjectTeamFacade = (PmsProjectTeamFacade) context
						.getBean("pmsProjectTeamFacade");
				// 获取制作供应商唯一ID
				Long teamProductId = (Long) delegateTask.getVariable("projectTeam_produce");
				if (teamProductId != null) {
					pmsProjectTeamFacade.update(param, teamProductId);
				}
			}
		}

	}
}
