package com.paipianwang.activiti.task.listener;

import java.util.ArrayList;
import java.util.Arrays;
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

import com.alibaba.fastjson.JSON;
import com.paipianwang.pat.facade.finance.entity.PmsDealLog;
import com.paipianwang.pat.facade.finance.service.PmsFinanceFacade;
import com.paipianwang.pat.workflow.entity.PmsProjectFlow;
import com.paipianwang.pat.workflow.entity.PmsProjectUser;
import com.paipianwang.pat.workflow.facade.PmsProjectFlowFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectUserFacade;

/**
 * 【财务】填写客户收款信息
 * @author jacky
 *
 */
@Component("inputCustomerDealLogTaskLisnter")
public class InputCustomerDealLogTaskLisnter implements TaskListener {

	private static final long serialVersionUID = -2204490453039201351L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// taskId
		final String taskId = delegateTask.getId();
		// processInstanceId
		final String processInstanceId = delegateTask.getProcessInstanceId();
		// projectId
		final String projectId = delegateTask.getExecution().getProcessBusinessKey();
		
		// 获取form表单信息
		FormService formService = delegateTask.getExecution().getEngineServices().getFormService();
		TaskFormDataImpl formData = (TaskFormDataImpl) formService.getTaskFormData(taskId);
		List<FormProperty> formList = formData.getFormProperties();
		if(formList != null && !formList.isEmpty()) {
			Map<String, Object> param = new HashMap<String, Object>();
			for (final FormProperty formProperty : formList) {
				String key = formProperty.getId();
				if(StringUtils.defaultString(key).startsWith("dl_")) {
					param.put(key.split("_")[1], formProperty.getValue());
				}
			}
			
			// 保存关于策划供应商的信息
			if(param != null && !param.isEmpty()) {
				ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
				PmsProjectFlowFacade flowFacade = (PmsProjectFlowFacade) context
						.getBean("pmsProjectFlowFacade");
				
				PmsProjectUserFacade projectUserFacade = (PmsProjectUserFacade) context
						.getBean("pmsProjectUserFacade");
				
				PmsFinanceFacade pmsFinanceFacade = (PmsFinanceFacade) context.getBean("pmsFinanceFacade");
				
				String json = JSON.toJSONString(param);
				PmsDealLog dealLog = JSON.parseObject(json, PmsDealLog.class);
				dealLog.setProjectId(Long.parseLong(projectId));
				// 预置信息
				dealLog.setDealStatus(1);
				dealLog.setPayChannel("线下转账");
				dealLog.setUserType("role_customer");
				dealLog.setDealLogSource(1);
				dealLog.setLogType(0); // 进账
				
				// 获取 项目名称
				PmsProjectFlow projectFlow = flowFacade.getProjectFlowByProcessInstanceId(
						new ArrayList<String>(Arrays.asList("projectName")), processInstanceId);
				dealLog.setProjectName(projectFlow.getProjectName());

				// 获取客户ID
				PmsProjectUser user = projectUserFacade.getProjectUserByProjectId(projectId);
				dealLog.setUserId(user.getProjectUserId());
				dealLog.setUserName(user.getUserName());
				
				pmsFinanceFacade.save(dealLog);
			}
		}
	}

}
