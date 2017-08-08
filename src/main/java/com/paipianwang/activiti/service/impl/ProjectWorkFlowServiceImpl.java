package com.paipianwang.activiti.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.form.TaskFormDataImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.paipianwang.activiti.service.ProjectWorkFlowService;
import com.paipianwang.pat.facade.finance.entity.PmsDealLog;
import com.paipianwang.pat.facade.finance.service.PmsFinanceFacade;
import com.paipianwang.pat.workflow.entity.PmsProjectFlow;
import com.paipianwang.pat.workflow.entity.PmsProjectSynergy;
import com.paipianwang.pat.workflow.entity.PmsProjectTeam;
import com.paipianwang.pat.workflow.entity.PmsProjectUser;
import com.paipianwang.pat.workflow.entity.ProjectFlowConstant;
import com.paipianwang.pat.workflow.facade.PmsProjectFlowFacade;
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
	private PmsFinanceFacade pmsFinanceFacade = null;

	@SuppressWarnings("unchecked")
	@Override
	public ProcessInstance startFormAndProcessInstance(String processDefinitionId, Map<String, String> formProperties,
			String userId, Map<String, Object> form) {
		ProcessInstance processInstance = null;
		try {
			// 数据存储
			Map<String, Object> flowMap = (Map<String, Object>) form.get(ProjectFlowConstant.PROJECT_FLOW);
			Map<String, Object> synergyMap = (Map<String, Object>) form.get(ProjectFlowConstant.PROJECT_SYNENGY);
			Map<String, Object> teamMap = (Map<String, Object>) form.get(ProjectFlowConstant.PROJECT_TEAM);
			Map<String, Object> userMap = (Map<String, Object>) form.get(ProjectFlowConstant.PROJECT_USER);
			
			String bussinessKey = null;
			if(flowMap != null) {
				PmsProjectFlow projectFlow = JSON.parseObject(JSON.toJSONString(flowMap), PmsProjectFlow.class);
				if(projectFlow != null && StringUtils.isNotBlank(projectFlow.getProjectId())) {
					bussinessKey = projectFlow.getProjectId();
					projectFlow.setPrincipal(123);
					flowFacade.insert(projectFlow);
				}
			}
			
			if(synergyMap != null) {
				PmsProjectSynergy synergy = JSON.parseObject(JSON.toJSONString(synergyMap), PmsProjectSynergy.class);
				if(synergy != null && synergy.getEmployeeId() != null) {
					synergy.setProjectId(bussinessKey);
					synergyFacade.insert(synergy);
				}
			}
			
			if(teamMap != null) {
				PmsProjectTeam team = JSON.parseObject(JSON.toJSONString(teamMap), PmsProjectTeam.class);
				if(team != null && team.getTeamId() != null) {
					team.setProjectId(bussinessKey);
					projectTeamFacade.insert(team);
				}
			}
			
			if(userMap != null) {
				PmsProjectUser user = JSON.parseObject(JSON.toJSONString(userMap), PmsProjectUser.class);
				if(user != null && user.getUserId() != null) {
					user.setProjectId(bussinessKey);
					projectUserFacade.insert(user);
				}
			}
			
			identityService.setAuthenticatedUserId(userId);
			processInstance = formService.submitStartFormData(processDefinitionId, String.valueOf(bussinessKey),
					formProperties);
			
			flowFacade.updateProcessInstanceId(processInstance.getProcessInstanceId(), bussinessKey);
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
		
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		String projectId = processInstance.getBusinessKey();
		
		// 将数据分组
		Map<String, Map<String, Object>> dataMap = groupDataIntoMap(formProperties);
		
		if(dataMap != null && !dataMap.isEmpty()) {
			Map<String, Object> flowMap = dataMap.get(ProjectFlowConstant.PROJECT_FLOW); // 项目信息数据集
			Map<String, Object> teamMap = dataMap.get(ProjectFlowConstant.PROJECT_TEAM); // 供应商数据集
			Map<String, Object> userMap = dataMap.get(ProjectFlowConstant.PROJECT_USER); // 用户数据集
			Map<String, Object> dealMap = dataMap.get(ProjectFlowConstant.DEAL_LOG); // 交易数据集
			
			if(flowMap != null && !flowMap.isEmpty()) {
				// 更新项目信息
				flowFacade.update(flowMap, projectId, processInstanceId);
			}
			
			if(teamMap != null && !teamMap.isEmpty()) {
				// 保存供应商信息
				teamMap.put("projectId", projectId);
				
				// 区分供应商类别
				String productTeamId = formProperties.get("teamProductId"); // 制作供应商
				String planTeamId = formProperties.get("teamPlanId"); // 策划供应商
				
				// 是否更新供应商信息
				// teamMap.put("teamId", StringUtils.isNotBlank(productTeamId) ? productTeamId : planTeamId);
				
				String ih = (String) teamMap.get("invoiceHead");
				if(StringUtils.isNotBlank(ih))
					projectTeamFacade.update(teamMap, Long.parseLong(productTeamId.split("_")[1]));
				else
					projectTeamFacade.insert(teamMap);
				
			}
			
			if(userMap != null && !userMap.isEmpty()) {
				// 更新客户信息
				projectUserFacade.update(userMap, projectId);
			}
			
			if(dealMap != null && !dealMap.isEmpty()) {
				// 新增/更新付款记录
				String json = JSON.toJSONString(dealMap);
				PmsDealLog dealLog = JSON.parseObject(json, PmsDealLog.class);
				dealLog.setProjectId(Long.parseLong(projectId));
				// 获取 项目名称
				PmsProjectFlow projectFlow = flowFacade.getProjectFlowByUniqueId(new ArrayList<String>(Arrays.asList("projectName")), processInstanceId);
				dealLog.setProjectName(projectFlow.getProjectName());
				
				// 获取客户ID
				PmsProjectUser user = projectUserFacade.getProjectUserByProjectId(projectId);
				dealLog.setUserId(user.getProjectUserId());
				dealLog.setUserName(user.getUserName());
				dealLog.setDealStatus(1);
				dealLog.setPayChannel("线下转账");
				dealLog.setUserType("role_customer");
				dealLog.setDealLogSource(1);
				dealLog.setLogType(0);
				pmsFinanceFacade.save(dealLog);
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
		if(formProperties != null) {
			Set<Entry<String, String>> entrySet = formProperties.entrySet();
			Map<String, Map<String, Object>> dataMap = new HashMap<String, Map<String, Object>>();
			Map<String, Object> flowMap = new HashMap<String, Object>(); // 项目信息数据集
			Map<String, Object> teamMap = new HashMap<String, Object>(); // 供应商数据集
			Map<String, Object> userMap = new HashMap<String, Object>(); // 用户数据集
			Map<String, Object> dealMap = new HashMap<String, Object>(); // 用户数据集
			
			for (Entry<String, String> entry : entrySet) {
				String key = entry.getKey();
				
				// project_flow
				if(StringUtils.defaultString(key).startsWith("pf_")) {
					String value = entry.getValue();
					if(StringUtils.isNotBlank(value)) {
						flowMap.put(key.split("_")[1], value);
					}
				}
				
				// project_team
				if(StringUtils.defaultString(key).startsWith("pt_")) {
					String value = entry.getValue();
					if(StringUtils.isNotBlank(value)) {
						teamMap.put(key.split("_")[1], value);
					}
				}
				
				// project_user
				if(StringUtils.defaultString(key).startsWith("pu_")) {
					String value = entry.getValue();
					if(StringUtils.isNotBlank(value)) {
						userMap.put(key.split("_")[1], value);
					}
				}
				
				// deal_log
				if(StringUtils.defaultString(key).startsWith("dl_")) {
					String value = entry.getValue();
					if(StringUtils.isNotBlank(value)) {
						dealMap.put(key.split("_")[1], value);
					}
				}
				
			}
			
			dataMap.put(ProjectFlowConstant.PROJECT_FLOW, flowMap);
			dataMap.put(ProjectFlowConstant.PROJECT_TEAM, teamMap);
			dataMap.put(ProjectFlowConstant.PROJECT_USER, userMap);
			dataMap.put(ProjectFlowConstant.DEAL_LOG, dealMap);
			return dataMap;
		}
		return null;
	}

	@Override
	public String generateProjectId() {
		return flowFacade.generateProjectId();
	}

	
}
