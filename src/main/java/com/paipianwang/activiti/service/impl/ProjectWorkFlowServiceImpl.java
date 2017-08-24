package com.paipianwang.activiti.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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
import com.paipianwang.pat.common.util.DateUtils;
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
import com.paipianwang.pat.workflow.enums.ProjectFlowStatus;
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
					if(employee != null) {
						synergy.setImgUrl(employee.getEmployeeImg());
						synergy.setTelephone(employee.getPhoneNumber());
						synergy.setEmployeeName(employee.getEmployeeRealName());
					}
					
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

		String sql = "";
		if (StringUtils.isBlank(userId)) {
			// 如果userId为空，那么查询所有的项目
			sql = "SELECT DISTINCT RES.ID_,RES.* FROM ACT_RU_EXECUTION RES LEFT JOIN ACT_HI_TASKINST ART ON ART.PROC_INST_ID_ = RES.PROC_INST_ID_ WHERE ACT_ID_ IS NOT NULL AND IS_ACTIVE_ = 1 AND SUSPENSION_STATE_ = 1 ORDER BY START_TIME_ DESC";
		} else {
			sql = "SELECT DISTINCT RES.ID_,RES.* FROM ACT_RU_EXECUTION RES LEFT JOIN ACT_HI_TASKINST ART ON ART.PROC_INST_ID_ = RES.PROC_INST_ID_ WHERE ART.ASSIGNEE_ = '"
					+ userId + "' AND ACT_ID_ IS NOT NULL AND IS_ACTIVE_ = 1 AND SUSPENSION_STATE_ = 1 ORDER BY START_TIME_ DESC";
		}
		
		return getRuningTaskBySql(sql, userId);
	}

	@Override
	public List<PmsProjectFlowResult> getFinishedTask(String userId) {
		List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().finished()
				.orderByProcessInstanceEndTime().desc().list();
		if (list != null && !list.isEmpty()) {
			List<PmsProjectFlowResult> resultList = new ArrayList<PmsProjectFlowResult>();
			for (final HistoricProcessInstance historicProcessInstance : list) {
				final String projectId = historicProcessInstance.getBusinessKey();
				if (StringUtils.isNotBlank(projectId)) {
					// 查询流程ID
					PmsProjectFlowResult result = new PmsProjectFlowResult();
					result.setHistoricProcessInstance(historicProcessInstance);
					PmsProjectFlow flow = flowFacade.getProjectFlowByProjectId(projectId);
					if(flow != null && flow.getPrincipal() != null) {
						result.setPmsProjectFlow(flow);
					}
					
					resultList.add(result);
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
			// 需要完成系统留言
			String taskName = task.getName();
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
	public Map<String, Object> getReadableColumns(String userId, String taskId, String projectId) {
		Map<String, Object> param = new HashMap<String, Object>();
		List<Group> groups = identityService.createGroupQuery().groupMember(userId).list();
		Map<String, List<String>> columns = shipFacade.getColumns(groups);
		List<String> flowList = columns.get("PROJECT_FLOW");
		List<String> teamList = columns.get("PROJECT_TEAM");
		List<String> userList = columns.get("PROJECT_USER");

		/*Task task = (TaskEntity) taskService.createTaskQuery().taskId(taskId).singleResult();
		String instanceId = task.getProcessInstanceId();
		ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId)
				.singleResult();
		param.put("PROJECT_ID", projectId);
		param.put("INSTANCE_ID",instanceId);*/

		if (flowList != null) {
			Map<String, Object> projectFlow = flowFacade.getProjectFlowColumnByProjectId(flowList, projectId);//TODO 后期整改，不使用map	
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
			
			// 遍历时间
			Object createDate = projectFlow.get("createDate");
			if(createDate != null) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
				try {
					Date date = format.parse(createDate.toString());
					projectFlow.put("createDate", date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			projectFlow=editFlowItem(projectFlow);
			
			param.put("PROJECT_FLOW", projectFlow);
		}

		if (teamList != null) {
			
			// 如果为 供应商管家、供应商采购、供应商总监 可以看见所有供应商信息
			List<String> teamGroup = new ArrayList<String>(Arrays.asList(ProjectRoleType.teamDirector.getId(),/*ProjectRoleType.teamPurchase.getId(),*/ ProjectRoleType.teamProvider.getId()));
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
					param.put("PROJECT_TEAMPLAN", projectTeamPlan);
					
					List<Map<String, Object>> projectTeamProduct = projectTeamFacade.getProjectsTeamColumnByProjectId(teamList,
							projectId, ProjectTeamType.produce.getCode());
					param.put("PROJECT_TEAMPRODUCT", projectTeamProduct);
				} else if(teamPlanGroup.contains(groupId)) {
					// 如果是 策划供应商可以看见 策划供应商信息
					List<Map<String, Object>> projectTeamPlan = projectTeamFacade.getProjectsTeamColumnByProjectId(teamList,
							projectId, ProjectTeamType.scheme.getCode());
					param.put("PROJECT_TEAMPLAN", projectTeamPlan);
				} else if(teamProudctGroup.contains(groupId)) {
					// 如果是制作供应商可以看见制作供应商信息
					List<Map<String, Object>> projectTeamProduct = projectTeamFacade.getProjectsTeamColumnByProjectId(teamList,
							projectId, ProjectTeamType.produce.getCode());
					param.put("PROJECT_TEAMPRODUCT", projectTeamProduct);
				}
			}
		}

		if (userList != null) {
			Map<String, Object> projectUser = projectUserFacade.getProjectUserColumnByProjectId(userList, projectId);
			param.put("PROJECT_USER", projectUser);
		}

		return param;
	}
	
	private Map<String,Object> editFlowItem(Map<String, Object> projectFlow){		
		
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
		if(projectFlow.get("createDate")!=null){
			projectFlow.put("createDate",DateUtils.getDateByFormatStr((Date)(projectFlow.get("createDate")), "yyyy-MM-dd HH:mm:ss"));
		}
		
		return projectFlow;
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
				if (processInstance == null) 
					continue;
				
				String projectId = processInstance.getBusinessKey();
				if (projectId == null) 
					continue;

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
	public void suspendProcess(String processInstanceId, String projectId) {
		runtimeService.suspendProcessInstanceById(processInstanceId);
		Map<String, Object> metaData = new HashMap<String, Object>();
		metaData.put("projectStatus", ProjectFlowStatus.suspend.getId());
		metaData.put("suspendDate", new Date());
		flowFacade.update(metaData, projectId, processInstanceId);
	}

	@Override
	public void activateProcess(String processInstanceId, String projectId) {
		runtimeService.activateProcessInstanceById(processInstanceId);
		Map<String, Object> metaData = new HashMap<String, Object>();
		metaData.put("projectStatus", null);
		flowFacade.update(metaData, projectId, processInstanceId);
	}

	@Override
	public List<PmsProjectFlowResult> getSuspendTasks(String userId) {
		
		String sql = "";
		if (StringUtils.isBlank(userId)) {
			// 如果userId为空，那么查询所有的项目
			sql = "SELECT DISTINCT RES.ID_,RES.* FROM ACT_RU_EXECUTION RES LEFT JOIN ACT_HI_TASKINST ART ON ART.PROC_INST_ID_ = RES.PROC_INST_ID_ WHERE ACT_ID_ IS NOT NULL AND IS_ACTIVE_ = 1 AND SUSPENSION_STATE_ = 2 ORDER BY START_TIME_ DESC";
		} else {
			sql = "SELECT DISTINCT RES.ID_,RES.* FROM ACT_RU_EXECUTION RES LEFT JOIN ACT_HI_TASKINST ART ON ART.PROC_INST_ID_ = RES.PROC_INST_ID_ WHERE ART.ASSIGNEE_ = '"
					+ userId + "' AND ACT_ID_ IS NOT NULL AND IS_ACTIVE_ = 1 AND SUSPENSION_STATE_ = 2 ORDER BY START_TIME_ DESC";
		}	
		
		return getRuningTaskBySql(sql, userId);
	}

	@Override
	public List<PmsProjectSynergy> getSynergy(String userId, String projectId, SessionInfo info) {
		if(StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(projectId)) {
			/*Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			String processInstanceId = task.getProcessInstanceId();
			ProcessInstance pIst = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
			final String projectId = pIst.getBusinessKey();*/
			
			Map<String, PmsProjectSynergy> synergyMap = synergyFacade.getSynergysByProjectId(projectId);
			List<PmsProjectSynergy> result = new ArrayList<PmsProjectSynergy>();
			
			String sessionType = info.getSessionType();// provider
			
			// 如果是非供应商，那么添加 主负责人
			if(!PmsConstant.ROLE_PROVIDER.equals(sessionType)) {
				// 查找主负责人
				PmsProjectFlow projectFlow = flowFacade.getProjectFlowByProjectId(projectId);
				Integer assigneeId = projectFlow.getPrincipal();
				if(assigneeId != null) {
					PmsProjectSynergy synergy = new PmsProjectSynergy();
					PmsEmployee assignee = employeeFacade.findEmployeeById(assigneeId);
					if(assignee != null) {
						synergy.setEmployeeName(assignee.getEmployeeRealName());
						synergy.setImgUrl(assignee.getEmployeeImg());
						synergy.setTelephone(assignee.getPhoneNumber());
					}
					synergy.setEmployeeGroup(ProjectRoleType.assignee.getId());
					synergy.setProjectId(projectId);
					result.add(synergy);
				}
			}
			
			if(synergyMap != null && !synergyMap.isEmpty()) {
				for (Entry<String,PmsProjectSynergy> entry : synergyMap.entrySet()) {
					PmsProjectSynergy synergy = entry.getValue();
					// 如果是供应商，那么只加载供应商管家和监制
					if(PmsConstant.ROLE_PROVIDER.equals(sessionType)) {
						String projectRole = entry.getKey();
						if(ProjectRoleType.teamProvider.getId().equals(projectRole)) {
							result.add(synergy);
						} else if(ProjectRoleType.teamProvider.getId().equals(projectRole)) {
							result.add(synergy);
						}
					} else {
						result.add(synergy);
					}
				}
			}
			
			return result;
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
		param.put("dueDate", task.getDueDate().toString());
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

	/*@Override
	public Map<String,Object> getProjectTaskList(String projectId, String taskStage) {
		//已进行任务节点
		List<HistoricTaskInstance> historyInstances = historyService.createHistoricTaskInstanceQuery().processInstanceBusinessKey(projectId).list();//.finished()
		
		Map<String,ProjectCycleItem> cycles=workFlowFacade.getAllCycleTask();
		if(cycles==null){
			return null;
		}
		List<User> users = identityService.createUserQuery().list();
		
		Map<String,Object> result=new HashMap<String,Object>();
		
		for(HistoricTaskInstance history : historyInstances){
			ProjectCycleItem cycle=cycles.get(history.getTaskDefinitionKey());
			if(!result.containsKey(cycle.getStage())){
				List<Object> list=new ArrayList<Object>();
				result.put(cycle.getStage(), list);
			}
			//操作人
			Map<String,Object> item=new HashMap<>();
			
			item.put("startTime", history.getCreateTime());//时间格式
			item.put("assigneeId",history.getAssignee());
			for(User user:users){
				if(user.getId().equals(history.getAssignee())){
					item.put("assignee",user.getFirstName());//人名
					break;
				}
			}
			item.put("taskName", history.getName());
			item.put("taskId",history.getId());
			item.put("taskStatus",history.getDeleteReason()==null?"running":history.getDeleteReason());//状态
			item.put("dueDate", history.getDueDate());
			
			((List<Object>) result.get(cycle.getStage())).add(item);
		}	
		//未来任务节点 futher
		
		//流程周期与创建时间
		List<String> flowList=new ArrayList<>();
		flowList.add("projectCycle");
		flowList.add("createDate");
		Map<String, Object> projectFlow = flowFacade.getProjectFlowColumnByProjectId(flowList, projectId);
		result.put("projectCycle",projectFlow.get("projectCycle"));
		result.put("createDate",projectFlow.get("createDate"));
		return result;
	}*/
	
	@Override
	public Map<String,Object> getProjectTaskList(String projectId, String taskStage) {
		//已进行任务节点
		List<HistoricTaskInstance> historyInstances = historyService.createHistoricTaskInstanceQuery().processInstanceBusinessKey(projectId).orderByTaskCreateTime().asc().list();//.finished()
		
		// 获取所有节点
		Map<String,ProjectCycleItem> cycles = workFlowFacade.getAllCycleTask();
		if(cycles==null){
			return null;
		}
		
		List<User> users = identityService.createUserQuery().list();
		
		Map<String,Object> result=new HashMap<String,Object>();
		
		for(HistoricTaskInstance history : historyInstances){
			ProjectCycleItem cycle=cycles.get(history.getTaskDefinitionKey());
			if(!result.containsKey(cycle.getStage())){
				List<Object> list=new ArrayList<Object>();
				result.put(cycle.getStage(), list);
			}
			//操作人
			Map<String,Object> item=new HashMap<>();
			
			item.put("startTime", history.getCreateTime());//时间格式
			item.put("assigneeId",history.getAssignee());
			for(User user:users){
				if(user.getId().equals(history.getAssignee())){
					item.put("assignee",user.getFirstName());//人名
					break;
				}
			}
			item.put("taskName", history.getName());
			item.put("taskId",history.getId());
			item.put("taskStatus",history.getDeleteReason()==null?"running":history.getDeleteReason());//状态
			item.put("dueDate", history.getDueDate());
			
			((List<Object>) result.get(cycle.getStage())).add(item);
		}	
		//未来任务节点 futher
		
		//流程周期与创建时间
		List<String> flowList=new ArrayList<>();
		flowList.add("projectCycle");
		flowList.add("createDate");
		Map<String, Object> projectFlow = flowFacade.getProjectFlowColumnByProjectId(flowList, projectId);
		result.put("projectCycle",projectFlow.get("projectCycle"));
		result.put("createDate",projectFlow.get("createDate"));
		return result;
	}

	@Override
	public Map<String, Object> getTaskInfo(String taskId) {
		HistoricTaskInstance task=historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
		Map<String,Object> item=new HashMap<>();
		item.put("taskId", task.getId());
		item.put("startTime",task.getCreateTime());
		item.put("endTime",task.getEndTime());
		item.put("taskName", task.getName());
		item.put("taskStatus",task.getDeleteReason()==null?"running":task.getDeleteReason());//状态
		item.put("dueDate", task.getDueDate());
		
		String taskDescription=getCycleByTask(task.getTaskDefinitionKey()).getDescription();
		
		item.put("taskDescription",taskDescription);
		return item;
	}

	public List<PmsProjectFlowResult> getRuningTaskBySql(String sql, String userId) {
		NativeExecutionQuery nativeExecutionQuery = runtimeService.createNativeExecutionQuery();
		List<Execution> executionList = nativeExecutionQuery.sql(sql).list();
		if (executionList != null && !executionList.isEmpty()) {
			Map<String, PmsProjectFlowResult> resultMap = new HashMap<String, PmsProjectFlowResult>();
			for (final Execution execution : executionList) {
				PmsProjectFlowResult result = new PmsProjectFlowResult();

				ExecutionEntity executionEntity = (ExecutionEntity) execution;
				String processInstanceId = executionEntity.getProcessInstanceId();
				String processDefinitionId = executionEntity.getProcessDefinitionId();
				
				ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
						.processInstanceId(processInstanceId).singleResult();
				
				final String projectId = processInstance.getBusinessKey();
				List<String> activitiIds = runtimeService.getActiveActivityIds(executionEntity.getId());

				// 获取 流程业务数据
				PmsProjectFlow pmsProjectFlow = flowFacade.getProjectFlowByProjectId(projectId);
				if (pmsProjectFlow != null && pmsProjectFlow.getPrincipal() != null) {
					PmsEmployee employee = employeeFacade.findEmployeeById(pmsProjectFlow.getPrincipal());
					pmsProjectFlow.setPrincipalName(employee.getEmployeeRealName());
					result.setPmsProjectFlow(pmsProjectFlow);
				}

				
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
				if(StringUtils.isNotBlank(userId) && pmsProjectFlow != null && ("employee_" + pmsProjectFlow.getPrincipal()).equals(userId)){
					//当前负责人
					result.setIsPrincipal(1);
				}else{
					result.setIsPrincipal(0);
				}
				resultMap.put(processInstanceId, result);
			}
			List<PmsProjectFlowResult> list = new ArrayList<PmsProjectFlowResult>(resultMap.values());
			return list;
		}
		return null;
	}
}
