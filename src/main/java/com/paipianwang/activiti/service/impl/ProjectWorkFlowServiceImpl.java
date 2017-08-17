package com.paipianwang.activiti.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.form.TaskFormDataImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.NativeExecutionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.paipianwang.activiti.service.ProjectWorkFlowService;
import com.paipianwang.pat.common.constant.PmsConstant;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.enums.UserLevel;
import com.paipianwang.pat.common.util.DateUtils;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.indent.entity.IndentSource;
import com.paipianwang.pat.facade.right.entity.PmsEmployee;
import com.paipianwang.pat.facade.right.service.PmsEmployeeFacade;
import com.paipianwang.pat.workflow.entity.PmsProjectFlow;
import com.paipianwang.pat.workflow.entity.PmsProjectFlowResult;
import com.paipianwang.pat.workflow.entity.PmsProjectMessage;
import com.paipianwang.pat.workflow.entity.PmsProjectSynergy;
import com.paipianwang.pat.workflow.entity.PmsProjectUser;
import com.paipianwang.pat.workflow.entity.ProjectCycleItem;
import com.paipianwang.pat.workflow.entity.ProjectFlowConstant;
import com.paipianwang.pat.workflow.enums.ProjectRoleType;
import com.paipianwang.pat.workflow.enums.ProjectTeamType;
import com.paipianwang.pat.workflow.facade.PmsProjectFlowFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectGroupColumnShipFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectMessageFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectSynergyFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectTeamFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectUserFacade;
import com.paipianwang.pat.workflow.facade.WorkFlowFacade;

@Service
public class ProjectWorkFlowServiceImpl implements ProjectWorkFlowService {

	private final Logger logger = LoggerFactory.getLogger(ActivitiFormServiceImpl.class);

	final String processDefintionKey = "procedure-workflow";

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
	private RepositoryService repositoryService = null;

	@Autowired
	private PmsProjectFlowFacade flowFacade = null;

	@Autowired
	private PmsProjectSynergyFacade synergyFacade = null;

	@Autowired
	private PmsProjectTeamFacade projectTeamFacade = null;

	@Autowired
	private PmsProjectUserFacade projectUserFacade = null;

	@Autowired
	private PmsEmployeeFacade employeeFacade = null;

	@Autowired
	private PmsProjectGroupColumnShipFacade shipFacade = null;
	
	@Autowired
	private WorkFlowFacade workFlowFacade = null;
	@Autowired
	private PmsProjectMessageFacade pmsProjectMessageFacade;
	
	@SuppressWarnings("unchecked")
	@Override
	public ProcessInstance startFormAndProcessInstance(String processDefinitionId, Map<String, String> formProperties,
			SessionInfo info, Map<String, Object> form) {
		ProcessInstance processInstance = null;
		try {
			String userId = info.getActivitiUserId();
			String realName = info.getRealName();
			// 数据存储
			Map<String, Object> flowMap = (Map<String, Object>) form.get(ProjectFlowConstant.PROJECT_FLOW);
			Map<String, Object> synergyMap = (Map<String, Object>) form.get(ProjectFlowConstant.PROJECT_SYNENGY);
			Map<String, Object> userMap = (Map<String, Object>) form.get(ProjectFlowConstant.PROJECT_USER);

			String projectId = null;
			if (flowMap != null) {
				PmsProjectFlow projectFlow = JSON.parseObject(JSON.toJSONString(flowMap), PmsProjectFlow.class);
				if (projectFlow != null && StringUtils.isNotBlank(projectFlow.getProjectId())
						&& StringUtils.isNotEmpty(userId)) {
					projectId = projectFlow.getProjectId();
					projectFlow.setPrincipal(Integer.parseInt(userId.split("_")[1]));
					projectFlow.setPrincipalName(realName);
					flowFacade.insert(projectFlow);
				}
			}

			if (synergyMap != null && !synergyMap.isEmpty()) {
				for (Entry<String, Object> entry : synergyMap.entrySet()) {
					String activitiRole = entry.getKey();
					PmsProjectSynergy synergy = new PmsProjectSynergy();
					synergy.setEmployeeId(Integer.parseInt(entry.getValue().toString().split("_")[1]));
					synergy.setProjectId(projectId);
					
					// 查询员工电话
					PmsEmployee employee = employeeFacade.findEmployeeById(Integer.parseInt(entry.getValue().toString().split("_")[1]));
					synergy.setTelephone(employee.getPhoneNumber());
					
					if (ProjectRoleType.customerDirector.getId().equals(activitiRole)) {
						// 客服总监
						synergy.setEmployeeGroup(ProjectRoleType.customerDirector.getId());
					} else if (ProjectRoleType.saleDirector.getId().equals(activitiRole)) {
						// 销售总监
						synergy.setEmployeeGroup(ProjectRoleType.saleDirector.getId());
					} else if (ProjectRoleType.superviseDirector.getId().equals(activitiRole)) {
						// 监制总监
						synergy.setEmployeeGroup(ProjectRoleType.superviseDirector.getId());
					} else if (ProjectRoleType.creativityDirector.getId().equals(activitiRole)) {
						// 创意总监
						synergy.setEmployeeGroup(ProjectRoleType.creativityDirector.getId());
					} else if (ProjectRoleType.teamDirector.getId().equals(activitiRole)) {
						// 供应商总监
						synergy.setEmployeeGroup(ProjectRoleType.teamDirector.getId());
					} else if (ProjectRoleType.teamProvider.getId().equals(activitiRole)) {
						// 供应商管家
						synergy.setEmployeeGroup(ProjectRoleType.teamProvider.getId());
					} else if (ProjectRoleType.teamPurchase.getId().equals(activitiRole)) {
						// 供应商采购
						synergy.setEmployeeGroup(ProjectRoleType.teamPurchase.getId());
					} else if (ProjectRoleType.financeDirector.getId().equals(activitiRole)) {
						// 财务主管
						synergy.setEmployeeGroup(ProjectRoleType.financeDirector.getId());
					} else if (ProjectRoleType.finance.getId().equals(activitiRole)) {
						// 财务
						synergy.setEmployeeGroup(ProjectRoleType.finance.getId());
					}

					synergyFacade.insert(synergy);
				}

			}

			if (userMap != null && !userMap.isEmpty()) {
				PmsProjectUser user = JSON.parseObject(JSON.toJSONString(userMap), PmsProjectUser.class);
				if (user != null && user.getUserId() != null) {
					user.setProjectId(projectId);
					projectUserFacade.insert(user);

					// 查看activiti 权限表中是否存在该客户
					Integer customerId = user.getUserId();
					if (customerId != null) {
						final String activitiUserId = "customer_" + customerId;
						User activitiUser = identityService.createUserQuery().userId(activitiUserId).singleResult();
						if (activitiUser == null) {
							// 不存在，则保存
							User customer = identityService.newUser(activitiUserId);
							customer.setEmail(user.getEmail());
							customer.setFirstName(user.getUserName());
							customer.setPassword("000000");
							identityService.saveUser(customer);

							// 保存关系
							identityService.createMembership(activitiUserId, ProjectRoleType.customer.getId());
						}
					} else
						throw new RuntimeException("新建项目时，客户ID不存在");

				}
			}

			identityService.setAuthenticatedUserId(userId);
			// 获取当前 流程定义ID
			ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
					.processDefinitionKey(processDefintionKey).latestVersion().orderByProcessDefinitionVersion().desc()
					.singleResult();
			processInstance = formService.submitStartFormData(definition.getId(), projectId, formProperties);
			
			// 添加 最终日期
			Task nextTask = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
			String taskDefinitionKey = nextTask.getTaskDefinitionKey();
			taskService.setDueDate(nextTask.getId(), getExpectDate(taskDefinitionKey));
			taskService.setVariable(nextTask.getId(), "task_stage", getCycleByTask(taskDefinitionKey).getStage());
			taskService.setVariable(nextTask.getId(), "task_description", getCycleByTask(taskDefinitionKey).getDescription());
			flowFacade.updateProcessInstanceId(processInstance.getProcessInstanceId(), projectId);
			
			// TODO 添加任务启动的系统留言
			
			logger.debug("start a processinstance: {}", processInstance);
		} finally {
			identityService.setAuthenticatedUserId(null);
		}

		return processInstance;
	}

	// 获取当前登陆人参与的任务
	@Override
	public List<PmsProjectFlowResult> getRunningTasks(String userId) {

		NativeExecutionQuery nativeExecutionQuery = runtimeService.createNativeExecutionQuery();
		String sql = "";
		if (StringUtils.isNotBlank(userId)) {
			// 如果userId为空，那么查询所有的项目
			sql = "SELECT DISTINCT RES.ID_,RES.* FROM ACT_RU_EXECUTION RES LEFT JOIN ACT_HI_TASKINST ART ON ART.PROC_INST_ID_ = RES.PROC_INST_ID_ WHERE ACT_ID_ IS NOT NULL AND IS_ACTIVE_ = 1 ORDER BY START_TIME_ DESC";
		} else {
			sql = "SELECT DISTINCT RES.ID_,RES.* FROM ACT_RU_EXECUTION RES LEFT JOIN ACT_HI_TASKINST ART ON ART.PROC_INST_ID_ = RES.PROC_INST_ID_ WHERE ART.ASSIGNEE_ = '"
					+ userId + "' AND ACT_ID_ IS NOT NULL AND IS_ACTIVE_ = 1 ORDER BY START_TIME_ DESC";
		}
		List<Execution> executionList = nativeExecutionQuery.sql(sql).list();
		if (executionList != null && !executionList.isEmpty()) {
			List<PmsProjectFlowResult> list = new ArrayList<PmsProjectFlowResult>();
			for (final Execution execution : executionList) {
				PmsProjectFlowResult result = new PmsProjectFlowResult();

				ExecutionEntity executionEntity = (ExecutionEntity) execution;
				String processInstanceId = executionEntity.getProcessInstanceId();
				String processDefinitionId = executionEntity.getProcessDefinitionId();
				final String projectId = executionEntity.getBusinessKey();
				List<String> activitiIds = runtimeService.getActiveActivityIds(executionEntity.getId());

				// 获取 流程业务数据
				PmsProjectFlow pmsProjectFlow = flowFacade.getProjectFlowByProjectId(projectId);
				if (pmsProjectFlow != null && pmsProjectFlow.getPrincipal() != null) {
					PmsEmployee employee = employeeFacade.findEmployeeById(pmsProjectFlow.getPrincipal());
					pmsProjectFlow.setPrincipalName(employee.getEmployeeRealName());
					result.setPmsProjectFlow(pmsProjectFlow);
				}

				ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
						.superProcessInstanceId(processInstanceId).singleResult();
				if (activitiIds != null && !activitiIds.isEmpty()) {
					for (String activitiId : activitiIds) {
						// 查询处于活动状态的任务
						Task task = taskService.createTaskQuery().taskDefinitionKey(activitiId)
								.executionId(execution.getId()).singleResult();
						if (task == null) {
							task = taskService.createTaskQuery()
									.processInstanceId(processInstance.getProcessInstanceId()).singleResult();
						}
						result.setTask(task);
					}
				}
				result.setProcessInstance(processInstance);
				result.setProcessDefinitionId(processDefinitionId);
				
				String taskStage = (String) taskService.getVariable(result.getTask().getId(), "task_stage");
				String taskDescription = (String) taskService.getVariable(result.getTask().getId(), "task_description");
				result.setTaskStage(taskStage);
				result.setTaskDescription(taskDescription);
				PmsProjectFlow project = flowFacade.getProjectFlowByProjectId(projectId);
				if(userId!=null && project!=null && userId.equals("employee_"+project.getPrincipal())){
					//当前负责人
					result.setIsPrincipal(1);
				}else{
					result.setIsPrincipal(0);
				}
				list.add(result);
			}
			return list;
		}
		return null;
	}

	@Override
	public List<PmsProjectFlowResult> getFinishedTask(String userId) {
		List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().finished()
				.orderByProcessInstanceEndTime().desc().list();
		if (list != null && !list.isEmpty()) {
			List<PmsProjectFlowResult> resultList = new ArrayList<PmsProjectFlowResult>();
			for (final HistoricProcessInstance historicProcessInstance : list) {
				String processInstanceId = historicProcessInstance.getId();
				List<HistoricTaskInstance> hisTaskList = historyService.createHistoricTaskInstanceQuery()
						.processInstanceId(processInstanceId).taskAssignee(userId).finished().list();
				if (hisTaskList != null && !hisTaskList.isEmpty()) {
					for (final HistoricTaskInstance historicTaskInstance : hisTaskList) {
						String pinstId = historicTaskInstance.getId();

						final String projectId = historyService.createHistoricProcessInstanceQuery()
								.processInstanceId(pinstId).singleResult().getBusinessKey();
						if (StringUtils.isNotBlank(projectId)) {
							// 查询流程ID
							PmsProjectFlowResult result = new PmsProjectFlowResult();
							PmsProjectFlow flow = flowFacade.getProjectFlowByProjectId(projectId);
							if(flow != null && flow.getPrincipal() != null) {
								PmsEmployee employee = employeeFacade.findEmployeeById(flow.getPrincipal());
								flow.setPrincipalName(employee.getEmployeeRealName());
								result.setPmsProjectFlow(flow);
							}
							
							resultList.add(result);
						}
					}
				}
			}
			return resultList;
		}
		return null;
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
	public void completeTaskFromData(String taskId, Map<String, String> formProperties, String userId, List<String> userGroup) {
		// 完成节点时，需要保存业务数据
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
			// TODO 需要完成系统留言
			// 节点名称
			String taskName = task.getName();
			// 办理人 employee_36
			String activitiUserId = task.getAssignee();
			Date date = new Date();
			
			PmsProjectMessage message=new PmsProjectMessage();
			message.setFromId(userId);
			message.setFromGroup(StringUtils.join(userGroup, ","));
			message.setProjectId(projectId);
			message.setTaskName(taskName);
			message.setContent("我完成了\""+taskName+"\"任务");
			pmsProjectMessageFacade.insert(message);
			
			
			identityService.setAuthenticatedUserId(userId);
			formService.submitTaskFormData(taskId, formProperties);

			List<Task> nextTasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
			if(nextTasks != null && !nextTasks.isEmpty()) {
				for (Task nextTask : nextTasks) {
					// 添加 最终日期
					String taskDefinitionKey = nextTask.getTaskDefinitionKey();
					taskService.setDueDate(nextTask.getId(), getExpectDate(taskDefinitionKey));
					//TODO 异常处理、事务处理
					taskService.setVariable(nextTask.getId(), "task_stage", getCycleByTask(taskDefinitionKey).getStage());
					taskService.setVariable(nextTask.getId(), "task_description", getCycleByTask(taskDefinitionKey).getDescription());
				}
			}
			
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
	public Map<String, Object> getReadableColumns(String userId, String taskId) {
		Map<String, Object> param = new HashMap<String, Object>();
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
		param.put("PROJECT_ID", projectId);

		if (flowList != null) {
			Map<String, Object> projectFlow = flowFacade.getProjectFlowColumnByProjectId(flowList, projectId);	
			//价格信息
			Map<String,Object> priceFlow=new HashMap<>();
			if(projectFlow.containsKey("estimatedPrice")){
				priceFlow.put("estimatedPrice", projectFlow.get("estimatedPrice"));
				projectFlow.remove("estimatedPrice");
			}
			if(projectFlow.containsKey("projectBudget")){
				priceFlow.put("projectBudget", projectFlow.get("projectBudget"));
				projectFlow.remove("projectBudget");
			}
			priceFlow=editFlowItem(priceFlow);
			param.put("PROJECT_PRICE", priceFlow);			
			
			projectFlow=editFlowItem(projectFlow);
			param.put("PROJECT_FLOW", projectFlow);
		}

		if (teamList != null) {
			
			// 如果为 供应商管家、供应商采购、供应商总监 可以看见所有供应商信息
			List<String> teamGroup = new ArrayList<String>(Arrays.asList(ProjectRoleType.teamDirector.getId(),ProjectRoleType.teamPurchase.getId(), ProjectRoleType.teamProvider.getId()));
			// 如果是 策划供应商可以看见 策划供应商信息
			List<String> teamPlanGroup = new ArrayList<String>(Arrays.asList(ProjectRoleType.teamPlan.getId()));
			// 如果是制作供应商可以看见制作供应商信息
			List<String> teamProudctGroup = new ArrayList<String>(Arrays.asList(ProjectRoleType.teamProduct.getId()));
			
			for (final Group group : groups) {
				String groupId = group.getId();
				if(teamGroup.contains(groupId)) {
					// 如果为 供应商管家、供应商采购、供应商总监 可以看见所有供应商信息
					List<Map<String, Object>> projectTeamPlan = projectTeamFacade.getProjectsTeamColumnByProjectId(teamList,
							projectId, ProjectTeamType.scheme.getCode());
					projectTeamPlan=editTeamItem(projectTeamPlan);
					param.put("PROJECT_TEAMPLAN", projectTeamPlan);
					
					List<Map<String, Object>> projectTeamProduct = projectTeamFacade.getProjectsTeamColumnByProjectId(teamList,
							projectId, ProjectTeamType.produce.getCode());
					projectTeamProduct=editTeamItem(projectTeamProduct);
					param.put("PROJECT_TEAMPRODUCT", projectTeamProduct);
				} else if(teamPlanGroup.contains(groupId)) {
					// 如果是 策划供应商可以看见 策划供应商信息
					List<Map<String, Object>> projectTeamPlan = projectTeamFacade.getProjectsTeamColumnByProjectId(teamList,
							projectId, ProjectTeamType.scheme.getCode());
					projectTeamPlan=editTeamItem(projectTeamPlan);
					param.put("PROJECT_TEAMPLAN", projectTeamPlan);
				} else if(teamProudctGroup.contains(groupId)) {
					// 如果是制作供应商可以看见制作供应商信息
					List<Map<String, Object>> projectTeamProduct = projectTeamFacade.getProjectsTeamColumnByProjectId(teamList,
							projectId, ProjectTeamType.produce.getCode());
					projectTeamProduct=editTeamItem(projectTeamProduct);
					param.put("PROJECT_TEAMPRODUCT", projectTeamProduct);
				}
			}
		}

		if (userList != null) {
			Map<String, Object> projectUser = projectUserFacade.getProjectUserColumnByProjectId(userList, projectId);
			projectUser=editUserItem(projectUser);
			param.put("PROJECT_USER", projectUser);
		}

		return param;
	}
	
	private Map<String,Object> editFlowItem(Map<String, Object> projectFlow){		
		Map<String,String> projectMap=new HashMap<>();
		projectMap.put("projectId", "项目编号");
		projectMap.put("projectName", "项目名称");
		projectMap.put("projectSource", "项目来源");
		projectMap.put("projectGrade", "项目评级");
		projectMap.put("projectDescription", "项目描述");
		projectMap.put("productName", "产品线名称");
		projectMap.put("productConfigLevelName", "产品线配置等级");
		projectMap.put("filmDestPath", "对标影片");
		projectMap.put("createDate", "创建时间");
		projectMap.put("projectCycle","项目周期");
		projectMap.put("principalName", "其他");
		projectMap.put("estimatedPrice", "预估价格");
		projectMap.put("projectBudget", "项目预算");
		
		//项目来源
		if(projectFlow.get("projectSource")!=null){
			String projectSource=(String) projectFlow.get("projectSource");
			for(IndentSource source:IndentSource.values()){
				if((source.getValue()+"").equals(projectSource)){
					projectFlow.put("projectSource", source.getName());
				}
			}
		}
		
		//项目评级
		if(projectFlow.get("projectGrade")!=null){
			String projectGrade="";
			switch ((String)projectFlow.get("projectGrade")) {
			case "5":
				projectGrade="S";
				break;
			case "4":
				projectGrade="A";
				break;
			case "3":
				projectGrade="B";
				break;
			case "2":
				projectGrade="C";
				break;
			case "1":
				projectGrade="D";
				break;
			case "0":
				projectGrade="E";
				break;
			default:
				break;
			}
			projectFlow.put("projectGrade",projectGrade);
		}
		
		Map<String,Object> result=new HashMap<>();
		
		Iterator<String> i=projectFlow.keySet().iterator();
		while(i.hasNext()){
			String key=i.next();
			result.put(projectMap.get(key), projectFlow.get(key));
		}
		return result;
	}
	
	private List<Map<String,Object>> editTeamItem(List<Map<String, Object>> projectTeam){		
		Map<String,String> projectMap=new HashMap<>();
		projectMap.put("teamName","供应商名称");
		projectMap.put("linkman","供应商联系人");
		projectMap.put("telephone","联系人电话");
		projectMap.put("budget","预算价格");
		projectMap.put("actualPrice","支付价格");
		
		List<Map<String,Object>> result=new ArrayList<>();
		if(ValidateUtil.isValid(projectTeam)){
			for(Map<String,Object> team:projectTeam){
				Map<String,Object> dest=new HashMap<>();
				
				Iterator<String> i=team.keySet().iterator();
				while(i.hasNext()){
					String key=i.next();
					dest.put(projectMap.get(key), team.get(key));
				}
				result.add(dest);
			}
		}
		return result;
	}
	
	private Map<String,Object> editUserItem(Map<String, Object> projectUser){		
		Map<String,String> projectMap=new HashMap<>();
		projectMap.put("userName", "客户名称");
		projectMap.put("linkman", "联系人");
		projectMap.put("telephone", "联系人电话");
		projectMap.put("userLevel", "客户评级");
		
		//客户评级值
//		if(projectUser.get("userLevel")!=null){
//			Integer userLevel=(Integer) projectUser.get("userLevel");
//			for(UserLevel level:UserLevel.values()){
//				if(level.getId().equals(userLevel+"")){
//					projectUser.put("userLevel", level.getText());
//				}
//			}
//		}
		
		Map<String,Object> result=new HashMap<>();
		
		Iterator<String> i=projectUser.keySet().iterator();
		while(i.hasNext()){
			String key=i.next();
			result.put(projectMap.get(key), projectUser.get(key));
		}
		return result;
	}

	// 获取登陆人当前待办的任务
	@Override
	public List<PmsProjectFlowResult> getTodoTasks(String userId) {

		if (StringUtils.isNotBlank(userId)) {
			List<PmsProjectFlowResult> list = new ArrayList<PmsProjectFlowResult>();
			// 根据当前人的ID查询
			TaskQuery taskQuery = taskService.createTaskQuery().taskCandidateOrAssigned(userId)
					.processDefinitionKey(processDefintionKey).active();
			List<Task> tasks = taskQuery.list();
			// 根据流程的业务ID查询实体并关联
			for (Task task : tasks) {
				String processInstanceId = task.getProcessInstanceId();
				ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
						.processInstanceId(processInstanceId).active().singleResult();
				if (processInstance == null) {
					continue;
				}
				String projectId = processInstance.getBusinessKey();
				if (projectId == null) {
					continue;
				}

				PmsProjectFlow project = flowFacade.getProjectFlowByProjectId(projectId);
				PmsEmployee employee = employeeFacade.findEmployeeById(project.getPrincipal());
				project.setPrincipalName(employee.getEmployeeRealName());
				PmsProjectFlowResult result = new PmsProjectFlowResult();
				result.setPmsProjectFlow(project);
				result.setTask(task);
				result.setProcessInstance(processInstance);
				result.setProcessDefinition(getProcessDefinition(processInstance.getProcessDefinitionId()));
				
				String taskStage = (String) taskService.getVariable(result.getTask().getId(), "task_stage");
				String taskDescription = (String) taskService.getVariable(result.getTask().getId(), "task_description");
				result.setTaskStage(taskStage);
				result.setTaskDescription(taskDescription);
				if(userId!=null && project!=null && userId.equals("employee_"+project.getPrincipal())){
					//当前负责人
					result.setIsPrincipal(1);
				}else{
					result.setIsPrincipal(0);
				}
				list.add(result);
			}

			return list;
		}
		return null;
	}

	/**
	 * 查询流程定义对象
	 *
	 * @param processDefinitionId
	 *            流程定义ID
	 * @return
	 */
	protected ProcessDefinition getProcessDefinition(String processDefinitionId) {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		return processDefinition;
	}

	@Override
	public void suspendProcess(String processInstanceId) {
		runtimeService.suspendProcessInstanceById(processInstanceId);
	}

	@Override
	public void activateProcess(String processInstanceId) {
		runtimeService.activateProcessInstanceById(processInstanceId);
	}

	@Override
	public List<PmsProjectFlowResult> getSuspendTasks(String userId) {
		List<PmsProjectFlowResult> list = new ArrayList<PmsProjectFlowResult>();
		// 根据当前人的ID查询
		TaskQuery taskQuery = null;
		if (StringUtils.isNotBlank(userId)) {
			taskQuery = taskService.createTaskQuery().taskCandidateOrAssigned(userId)
					.processDefinitionKey(processDefintionKey).suspended();
		} else {
			taskQuery = taskService.createTaskQuery().processDefinitionKey(processDefintionKey).suspended();
		}

		List<Task> tasks = taskQuery.list();
		if (tasks != null && !tasks.isEmpty()) {

			// 根据流程的业务ID查询实体并关联
			for (Task task : tasks) {
				String processInstanceId = task.getProcessInstanceId();
				ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
						.processInstanceId(processInstanceId).singleResult();
				if (processInstance == null) {
					continue;
				}
				String projectId = processInstance.getBusinessKey();
				if (projectId == null) {
					continue;
				}

				PmsProjectFlow project = flowFacade.getProjectFlowByProjectId(projectId);
				PmsEmployee employee = employeeFacade.findEmployeeById(project.getPrincipal());
				project.setPrincipalName(employee.getEmployeeRealName());
				PmsProjectFlowResult result = new PmsProjectFlowResult();
				result.setPmsProjectFlow(project);
				result.setTask(task);
				result.setProcessInstance(processInstance);
				result.setProcessDefinition(getProcessDefinition(processInstance.getProcessDefinitionId()));
				
				String taskStage = (String) taskService.getVariable(task.getId(), "task_stage");
				String taskDescription = (String) taskService.getVariable(task.getId(), "task_description");
				result.setTaskStage(taskStage);
				result.setTaskDescription(taskDescription);
				if(userId!=null && project!=null && userId.equals("employee_"+project.getPrincipal())){
					//当前负责人
					result.setIsPrincipal(1);
				}else{
					result.setIsPrincipal(0);
				}
				
				list.add(result);
			}
			return list;
		}

		return null;
	}

	@Override
	public List<PmsProjectSynergy> getSynergy(String userId, String taskId) {
		if(StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(taskId)) {
			// 
			List<PmsProjectSynergy> list = new ArrayList<PmsProjectSynergy>();
			Map<String, Object> variables = taskService.getVariables(taskId);
			for (Entry<String,Object> entry : variables.entrySet()) {
				String key = entry.getKey();
				if(key.endsWith("Id") && entry.getValue().toString().startsWith("employee_")){//协同人
					ProjectRoleType type = ProjectRoleType.getEnum(key.substring(0,key.indexOf("Id")));
					if(type != null) {
						String activitiUserId = entry.getValue().toString();
						PmsEmployee employee = employeeFacade.findEmployeeById(Long.parseLong(activitiUserId.split("_")[1]));
						PmsProjectSynergy synergy = new PmsProjectSynergy();
						synergy.setEmployeeName(employee.getEmployeeRealName());
						synergy.setImgUrl(employee.getEmployeeImg());
						synergy.setTelephone(employee.getPhoneNumber());
						synergy.setEmployeeGroup(type.getText());
						list.add(synergy);
					}
				}
				
			}
			return list;
		}
		return null;
	}
	@Override
	public ProjectCycleItem getCycleByTask(String taskId) {
		return workFlowFacade.getCycleByTaskId(taskId);
	}

	@Override
	public Date getExpectDate(String taskId) {
		ProjectCycleItem cycle=workFlowFacade.getCycleByTaskId(taskId);
		if(cycle==null || cycle.getDuration()==null){
			//数据错误
			return null;
		}
		return DateUtils.addHour(new Date(), cycle.getDuration());
	}

	@Override
	public Map<String, String> getTaskStateAndDescription(String taskId) {
		Map<String , String> param = new HashMap<String, String>();
		// 当前节点的阶段
		String taskStage = (String) taskService.getVariable(taskId, "task_stage");
		String taskDescription = (String) taskService.getVariable(taskId, "task_description");
		param.put("taskStage", taskStage);
		param.put("taskDescription",taskDescription);
		//TODO 暂时放这
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		param.put("taskName", task.getName());
		return param;
	}

	@Override
	public Map<String, String> getUserByRole(String roleType) {
		Map<String , String> param = new HashMap<String, String>();
		List<User> userList=identityService.createUserQuery().memberOfGroup(roleType).list();
		for(User user:userList){
			param.put(user.getId(), user.getFirstName());
		}
		return param;
	}

}
