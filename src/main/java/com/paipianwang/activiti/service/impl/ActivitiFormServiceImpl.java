package com.paipianwang.activiti.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.form.TaskFormDataImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.paipianwang.activiti.service.ActivitiFormService;
import com.paipianwang.pat.workflow.entity.PmsVacation;
import com.paipianwang.pat.workflow.facade.WorkFlowFacade;

@Service
public class ActivitiFormServiceImpl implements ActivitiFormService {

	private final Logger logger = LoggerFactory.getLogger(ActivitiFormServiceImpl.class);

	@Autowired
	private FormService formService = null;

	@Autowired
	private IdentityService identityService = null;

	@Autowired
	private TaskService taskService = null;
	
	@Autowired
	private HistoryService historyService = null;

	@Autowired
	private WorkFlowFacade workFlowFacade = null;

	@Override
	@Transactional
	public ProcessInstance startFormAndProcessInstance(String processDefinitionId, Map<String, String> formProperties,
			String userId) {
		ProcessInstance processInstance = null;
		try {
			// 数据存储
			String json = JSON.toJSONString(formProperties);
			PmsVacation vacation = JSON.parseObject(json, PmsVacation.class);
			vacation.setUserId(userId);
			Long bussinessKey = workFlowFacade.insert(vacation);

			identityService.setAuthenticatedUserId(userId);
			processInstance = formService.submitStartFormData(processDefinitionId, String.valueOf(bussinessKey),
					formProperties);
			Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
			Map<String, String> param = new HashMap<String, String>();
			Map<String, String> condition = new HashMap<String, String>();
			param.put("processInstanceId", processInstance.getProcessInstanceId());
			condition.put("id", String.valueOf(bussinessKey));
			paramMap.put("param", param);
			paramMap.put("condition", condition);
			workFlowFacade.update(paramMap, null);
			logger.debug("start a processinstance: {}", processInstance);
		} finally {
			identityService.setAuthenticatedUserId(null);
		}

		return processInstance;
	}

	@Override
	public List<Task> getRunningTasks(String userId) {
		List<Task> list = taskService.createTaskQuery().taskCandidateOrAssigned(userId).active().list();
		return list;
	}

	@Override
	public void claim(String userId, String taskId) {
		taskService.claim(taskId, userId);
	}

	@Override
	public TaskFormDataImpl getTaskFormData(String taskId) {
		TaskFormDataImpl taskFormData = (TaskFormDataImpl) formService.getTaskFormData(taskId);
		taskFormData.setTask(null);
		return taskFormData;
	}

	@Override
	@Transactional
	public void completeTaskFromData(String taskId, Map<String, String> formProperties, String userId) {
		// TODO 完成节点时，需要保存业务数据
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
		paramMap.put("param", formProperties);
		paramMap.put("condition", new HashMap<String, String>());
		workFlowFacade.update(paramMap, task.getProcessInstanceId());
		try {
			identityService.setAuthenticatedUserId(userId);
			formService.submitTaskFormData(taskId, formProperties);
		} finally {
			identityService.setAuthenticatedUserId(null);
		}
	}

	@Override
	public List<HistoricProcessInstance> getFinishedTask(String id) {
		List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().finished().orderByProcessInstanceEndTime().desc().list();
		return list;
	}

}
