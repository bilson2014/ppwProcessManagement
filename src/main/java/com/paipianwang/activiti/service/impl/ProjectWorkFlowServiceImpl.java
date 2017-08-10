package com.paipianwang.activiti.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.form.TaskFormDataImpl;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.paipianwang.activiti.service.ProjectWorkFlowService;
import com.paipianwang.pat.workflow.entity.PmsProjectFlow;
import com.paipianwang.pat.workflow.entity.PmsProjectSynergy;
import com.paipianwang.pat.workflow.entity.PmsProjectUser;
import com.paipianwang.pat.workflow.entity.ProjectFlowConstant;
import com.paipianwang.pat.workflow.enums.ProjectRoleType;
import com.paipianwang.pat.workflow.facade.PmsEmployeeSynergyFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectFlowFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectGroupColumnShipFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectSynergyFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectTeamFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectUserFacade;

@Service
public class ProjectWorkFlowServiceImpl implements ProjectWorkFlowService {

	private final Logger logger = LoggerFactory.getLogger(ActivitiFormServiceImpl.class);

	@Autowired
	private RuntimeService runtimeService = null;

	@Autowired
	private FormService formService = null;

	@Autowired
	private IdentityService identityService = null;

	@Autowired
	private TaskService taskService = null;

	@Autowired
	private HistoryService historyService = null;

	@Autowired
	private PmsProjectFlowFacade flowFacade = null;

	@Autowired
	private PmsProjectSynergyFacade synergyFacade = null;

	@Autowired
	private PmsProjectTeamFacade projectTeamFacade = null;

	@Autowired
	private PmsProjectUserFacade projectUserFacade = null;

	@Autowired
	private PmsEmployeeSynergyFacade employeeSynergyFacade = null;

	@Autowired
	private PmsProjectGroupColumnShipFacade shipFacade = null;

	@SuppressWarnings("unchecked")
	@Override
	public ProcessInstance startFormAndProcessInstance(String processDefinitionId, Map<String, String> formProperties,
			String userId, Map<String, Object> form) {
		ProcessInstance processInstance = null;
		try {
			// 数据存储
			Map<String, Object> flowMap = (Map<String, Object>) form.get(ProjectFlowConstant.PROJECT_FLOW);
			Map<String, Object> synergyMap = (Map<String, Object>) form.get(ProjectFlowConstant.PROJECT_SYNENGY);
			Map<String, Object> userMap = (Map<String, Object>) form.get(ProjectFlowConstant.PROJECT_USER);

			String projectId = null;
			if (flowMap != null) {
				PmsProjectFlow projectFlow = JSON.parseObject(JSON.toJSONString(flowMap), PmsProjectFlow.class);
				if (projectFlow != null && StringUtils.isNotBlank(projectFlow.getProjectId()) && StringUtils.isNotEmpty(userId)) {
					projectId = projectFlow.getProjectId();
					projectFlow.setPrincipal(Integer.parseInt(userId.split("_")[1]));
					flowFacade.insert(projectFlow);
				}
			}

			if (synergyMap != null && !synergyMap.isEmpty()) {
				for (Entry<String, Object> entry : synergyMap.entrySet()) {
					String activitiRole = entry.getKey();
					PmsProjectSynergy synergy = new PmsProjectSynergy();
					synergy.setEmployeeId(Integer.getInteger(entry.getValue().toString().split("_")[1]));
					synergy.setProjectId(projectId);
					if (ProjectRoleType.customerDirector.getId().equals(activitiRole)) {
						// 客服总监
						synergy.setEmployeeGroup(ProjectRoleType.customerDirector.getId());
					}

					if (ProjectRoleType.saleDirector.getId().equals(activitiRole)) {
						// 销售总监
						synergy.setEmployeeGroup(ProjectRoleType.saleDirector.getId());
					}

					if (ProjectRoleType.superviseDirector.getId().equals(activitiRole)) {
						// 监制总监
						synergy.setEmployeeGroup(ProjectRoleType.superviseDirector.getId());
					}

					if (ProjectRoleType.teamDirector.getId().equals(activitiRole)) {
						// 供应商总监
						synergy.setEmployeeGroup(ProjectRoleType.teamDirector.getId());
					}

					if (ProjectRoleType.teamProvider.getId().equals(activitiRole)) {
						// 供应商管家
						synergy.setEmployeeGroup(ProjectRoleType.teamProvider.getId());
					}

					if (ProjectRoleType.teamPurchase.getId().equals(activitiRole)) {
						// 供应商采购
						synergy.setEmployeeGroup(ProjectRoleType.teamPurchase.getId());
					}

					if (ProjectRoleType.financeDirector.getId().equals(activitiRole)) {
						// 财务主管
						synergy.setEmployeeGroup(ProjectRoleType.financeDirector.getId());
					}

					if (ProjectRoleType.finance.getId().equals(activitiRole)) {
						// 财务
						synergy.setEmployeeGroup(ProjectRoleType.finance.getId());
					}

					synergyFacade.insert(synergy);
				}
				
			}

			if (userMap != null) {
				PmsProjectUser user = JSON.parseObject(JSON.toJSONString(userMap), PmsProjectUser.class);
				if (user != null && user.getUserId() != null) {
					user.setProjectId(projectId);
					projectUserFacade.insert(user);
				}
			}

			identityService.setAuthenticatedUserId(userId);
			processInstance = formService.submitStartFormData(processDefinitionId, String.valueOf(projectId),
					formProperties);

			flowFacade.updateProcessInstanceId(processInstance.getProcessInstanceId(), projectId);
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
	public List<HistoricProcessInstance> getFinishedTask(String id) {
		List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().finished()
				.orderByProcessInstanceEndTime().desc().list();
		return list;
	}

	@Override
	public TaskFormDataImpl getTaskFormData(String taskId) {
		TaskFormDataImpl taskFormData = (TaskFormDataImpl) formService.getTaskFormData(taskId);
		taskFormData.setTask(null);
		return taskFormData;
	}

	@Override
	public void claim(String userId, String taskId) {
		taskService.claim(taskId, userId);
	}

	@Override
	public void completeTaskFromData(String taskId, Map<String, String> formProperties, String userId) {
		// TODO 完成节点时，需要保存业务数据
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();

		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		String projectId = processInstance.getBusinessKey();

		// 将数据分组
		Map<String, Map<String, Object>> dataMap = groupDataIntoMap(formProperties);

		if (dataMap != null && !dataMap.isEmpty()) {
			Map<String, Object> flowMap = dataMap.get(ProjectFlowConstant.PROJECT_FLOW); // 项目信息数据集
			Map<String, Object> userMap = dataMap.get(ProjectFlowConstant.PROJECT_USER); // 用户数据集

			if (flowMap != null && !flowMap.isEmpty()) {
				// 更新项目信息
				flowFacade.update(flowMap, projectId, processInstanceId);
			}

			if (userMap != null && !userMap.isEmpty()) {
				// 更新客户信息
				projectUserFacade.update(userMap, projectId);
			}

		}

		try {
			identityService.setAuthenticatedUserId(userId);
			formService.submitTaskFormData(taskId, formProperties);
		} finally {
			identityService.setAuthenticatedUserId(null);
		}

	}

	// 将数据分组
	private Map<String, Map<String, Object>> groupDataIntoMap(Map<String, String> formProperties) {
		if (formProperties != null) {
			Set<Entry<String, String>> entrySet = formProperties.entrySet();
			Map<String, Map<String, Object>> dataMap = new HashMap<String, Map<String, Object>>();
			Map<String, Object> flowMap = new HashMap<String, Object>(); // 项目信息数据集
			Map<String, Object> userMap = new HashMap<String, Object>(); // 用户数据集

			for (Entry<String, String> entry : entrySet) {
				String key = entry.getKey();

				// project_flow
				if (StringUtils.defaultString(key).startsWith("pf_")) {
					String value = entry.getValue();
					if (StringUtils.isNotBlank(value)) {
						flowMap.put(key.split("_")[1], value);
					}
				}

				// project_user
				if (StringUtils.defaultString(key).startsWith("pu_")) {
					String value = entry.getValue();
					if (StringUtils.isNotBlank(value)) {
						userMap.put(key.split("_")[1], value);
					}
				}

			}

			dataMap.put(ProjectFlowConstant.PROJECT_FLOW, flowMap);
			dataMap.put(ProjectFlowConstant.PROJECT_USER, userMap);
			return dataMap;
		}
		return null;
	}

	@Override
	public String generateProjectId() {
		return flowFacade.generateProjectId();
	}

	@Override
	public Map<String, Object> getReadableColumns(User user, String taskId) {
		Map<String, Object> param = new HashMap<String, Object>();
		String userId = user.getId();
		List<Group> groups = identityService.createGroupQuery().groupMember(userId).list();
		Map<String, List<String>> columns = shipFacade.getColumns(groups);
		List<String> flowList = columns.get("PROJECT_FLOW");
		List<String> teamList = columns.get("PROJECT_TEAM");
		List<String> userList = columns.get("PROJECT_USER");

		Task task = (TaskEntity) taskService.createTaskQuery().taskId(taskId).singleResult();
		String instanceId = task.getProcessInstanceId();
		ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId)
				.singleResult();
		String projectId = instance.getBusinessKey();

		if (flowList != null) {
			Map<String, Object> projectFlow = flowFacade.getProjectFlowColumnByProjectId(flowList, projectId);
			param.put("PROJECT_FLOW", projectFlow);
		}

		if (teamList != null) {
			List<Map<String, Object>> projectTeam = projectTeamFacade.getProjectsTeamColumnByProjectId(teamList,
					projectId);
			param.put("PROJECT_TEAM", projectTeam);
		}

		if (userList != null) {
			Map<String, Object> projectUser = projectUserFacade.getProjectUserColumnByProjectId(userList, projectId);
			param.put("PROJECT_USER", projectUser);
		}

		return param;
	}

}
