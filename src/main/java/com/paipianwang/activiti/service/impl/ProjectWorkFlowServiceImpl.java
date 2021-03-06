package com.paipianwang.activiti.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
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
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.history.NativeHistoricProcessInstanceQuery;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.form.TaskFormDataImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.SuspensionState;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.paipianwang.activiti.domin.TaskVO;
import com.paipianwang.activiti.mq.email.service.BaseMQService;
import com.paipianwang.activiti.service.MessageService;
import com.paipianwang.activiti.service.ProjectWorkFlowService;
import com.paipianwang.pat.common.constant.PmsConstant;
import com.paipianwang.pat.common.entity.KeyValue;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.enums.FileType;
import com.paipianwang.pat.common.util.DateUtils;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.finance.entity.PmsDealLog;
import com.paipianwang.pat.facade.finance.service.PmsFinanceFacade;
import com.paipianwang.pat.facade.indent.entity.IndentSource;
import com.paipianwang.pat.facade.right.entity.PmsEmployee;
import com.paipianwang.pat.facade.right.service.PmsEmployeeFacade;
import com.paipianwang.pat.workflow.entity.PmsProjectFlow;
import com.paipianwang.pat.workflow.entity.PmsProjectFlowResult;
import com.paipianwang.pat.workflow.entity.PmsProjectSynergy;
import com.paipianwang.pat.workflow.entity.PmsProjectTeam;
import com.paipianwang.pat.workflow.entity.PmsProjectUser;
import com.paipianwang.pat.workflow.entity.ProjectCycleItem;
import com.paipianwang.pat.workflow.entity.ProjectFlowConstant;
import com.paipianwang.pat.workflow.enums.ProjectFlowStatus;
import com.paipianwang.pat.workflow.enums.ProjectRoleType;
import com.paipianwang.pat.workflow.enums.ProjectTeamType;
import com.paipianwang.pat.workflow.facade.PmsProjectFlowFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectGroupColumnShipFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectGroupColumnUpdateShipFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectGroupResourceUpdateFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectSynergyFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectTeamFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectUserFacade;
import com.paipianwang.pat.workflow.facade.WorkFlowFacade;

@Service
public class ProjectWorkFlowServiceImpl implements ProjectWorkFlowService {

	private final Logger logger = LoggerFactory.getLogger(ProjectWorkFlowServiceImpl.class);

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
	// private PmsProjectMessageFacade pmsProjectMessageFacade;
	private MessageService messageService;

	@Autowired
	private PmsProjectGroupColumnUpdateShipFacade updateShipFacade = null;

	@Autowired
	private PmsFinanceFacade financeFacade = null;

	@Autowired
	private PmsProjectGroupResourceUpdateFacade resourceUpdateFacade = null;
	
	/*@Autowired
	@Qualifier("projectProductAddMQService")
	private BaseMQService projectProductAddMQService;

	@Autowired
	@Qualifier("projectProductCancelMQService")
	private BaseMQService projectProductCancelMQService;*/

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
					PmsEmployee employee = employeeFacade
							.findEmployeeById(Integer.parseInt(entry.getValue().toString().split("_")[1]));
					if (employee != null) {
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
					} else if (ProjectRoleType.finance.getId().equals(activitiRole)) {
						// 财务
						synergy.setEmployeeGroup(ProjectRoleType.finance.getId());
					}

					synergyFacade.insert(synergy);
				}

				// 保存销售信息
				if (info.getActivitGroups().contains(ProjectRoleType.sale.getId())) {
					PmsProjectSynergy synergy = new PmsProjectSynergy();
					synergy.setEmployeeId(Integer.parseInt(info.getReqiureId().toString()));
					synergy.setProjectId(projectId);
					synergy.setImgUrl(info.getPhoto());
					synergy.setTelephone(info.getTelephone());
					synergy.setEmployeeName(info.getRealName());
					synergy.setEmployeeGroup(ProjectRoleType.sale.getId());
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
			ProjectCycleItem item = getCycleByTask(taskDefinitionKey);

			taskService.setDueDate(nextTask.getId(), getExpectDate(taskDefinitionKey));
			taskService.setVariable(nextTask.getId(), "task_stage", item.getStage());
			taskService.setVariable(nextTask.getId(), "task_description", item.getDescription());
			flowFacade.updateProcessInstanceId(processInstance.getProcessInstanceId(), projectId);

			// TODO 添加任务启动的系统留言
			// 更新项目当前阶段
			if (item != null) {
				Map<String, Object> metaData = new HashMap<>();
				metaData.put("projectStage", item.getStageId());
				flowFacade.update(metaData, projectId, processInstance.getProcessInstanceId());
			}

			// 记录项目日志
			messageService.insertOperationLog(projectId, null, null, "创建了" + flowMap.get("projectName") + "项目", info);

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
		} else if (userId.indexOf("team_") > -1) {
			sql = "SELECT DISTINCT RES.ID_,RES.* FROM ACT_RU_EXECUTION RES "
					+ "LEFT JOIN ACT_HI_TASKINST ART ON ART.PROC_INST_ID_ = RES.PROC_INST_ID_ "
					+ " WHERE ART.ASSIGNEE_ = '" + userId + "'"
					+ " AND ACT_ID_ IS NOT NULL AND IS_ACTIVE_ = 1 AND SUSPENSION_STATE_ = 1 ORDER BY PROC_INST_ID_ DESC";
		} else {
			sql = "SELECT DISTINCT RES.ID_,RES.* FROM ACT_RU_EXECUTION RES "
					+ " LEFT JOIN pat.PROJECT_FLOW flow ON flow.PROCESSINSTANCEID = RES.PROC_INST_ID_ "
					+ " LEFT JOIN pat.PROJECT_SYNERGY sy ON sy.PROJECTID = flow.PROJECTID " + " WHERE sy.employeeId = "
					+ userId.split("_")[1]
					+ " AND ACT_ID_ IS NOT NULL AND IS_ACTIVE_ = 1 AND SUSPENSION_STATE_ = 1 ORDER BY PROC_INST_ID_ DESC";
		}

		return getRuningTaskBySql(sql, userId);
	}

	@Override
	public List<PmsProjectFlowResult> getFinishedTask(String userId) {
		String sql = "";
		if (StringUtils.isBlank(userId)) {
			sql = "SELECT DISTINCT PROC.ID_,PROC.* FROM ACT_HI_PROCINST PROC "
					+ " WHERE PROC.END_TIME_ IS NOT NULL AND PROC.END_ACT_ID_ IS NOT NULL ORDER BY PROC.END_TIME_ DESC";
		} else if (userId.indexOf("team_") > -1) {
			sql = "SELECT DISTINCT PROC.ID_,PROC.* FROM ACT_HI_PROCINST PROC "
					+ "LEFT JOIN ACT_HI_TASKINST ART ON ART.PROC_INST_ID_ = PROC.PROC_INST_ID_ WHERE ART.ASSIGNEE_ = '"
					+ userId + "'"
					+ " AND PROC.END_TIME_ IS NOT NULL AND PROC.END_ACT_ID_ IS NOT NULL ORDER BY PROC.END_TIME_ DESC";
		} else {
			sql = "SELECT DISTINCT PROC.ID_,PROC.* FROM ACT_HI_PROCINST PROC "
					+ "LEFT JOIN pat.PROJECT_SYNERGY sy ON sy.projectId = PROC.BUSINESS_KEY_ "
					+ "LEFT JOIN ACT_HI_TASKINST ART ON ART.PROC_INST_ID_ = PROC.PROC_INST_ID_ WHERE sy.employeeId = "
					+ userId.split("_")[1] + " AND ART.ASSIGNEE_ = '" + userId + "'"
					+ " AND PROC.END_TIME_ IS NOT NULL AND PROC.END_ACT_ID_ IS NOT NULL ORDER BY PROC.END_TIME_ DESC";
		}

		NativeHistoricProcessInstanceQuery nativeExecutionQuery = historyService
				.createNativeHistoricProcessInstanceQuery();
		List<HistoricProcessInstance> lists = nativeExecutionQuery.sql(sql).list();
		if (lists != null && !lists.isEmpty()) {
			List<PmsProjectFlowResult> resultList = new ArrayList<PmsProjectFlowResult>();
			for (HistoricProcessInstance historicProcessInstance : lists) {
				final String projectId = historicProcessInstance.getBusinessKey();
				if (StringUtils.isNotBlank(projectId)) {
					// 查询流程ID
					PmsProjectFlowResult result = new PmsProjectFlowResult();
					result.setHistoricProcessInstance(historicProcessInstance);
					PmsProjectFlow flow = flowFacade.getProjectFlowByProjectId(projectId);
					if (flow != null && flow.getPrincipal() != null) {

						if (flow.getPrincipal() == Integer.parseInt(userId.split("_")[1])) {
							result.setIsPrincipal(1);
						} else {
							result.setIsPrincipal(0);
						}
						result.setPmsProjectFlow(flow);
						// task使用空字符串
						result.setTaskId(" ");
						resultList.add(result);
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
	public String completeTaskFromData(String taskId, Map<String, String> formProperties, String userId,
			List<String> userGroup, String realName) {
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
			ProjectCycleItem taskItem = getCycleByTask(task.getTaskDefinitionKey());
			if (taskItem != null && taskItem.getNeedFinishLog().equals(1)) {
				String taskName = task.getName();
				messageService.insertDetailOperationLog(projectId, taskId, taskName, "完成了\"" + taskName + "\"任务",
						userId, realName, userGroup);
			}

			identityService.setAuthenticatedUserId(userId);
			formService.submitTaskFormData(taskId, formProperties);

			ProjectCycleItem item = null;

			List<Task> nextTasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
			if (nextTasks != null && !nextTasks.isEmpty()) {
				for (Task nextTask : nextTasks) {
					// 添加 最终日期
					String taskDefinitionKey = nextTask.getTaskDefinitionKey();
					String nextTaskId = nextTask.getId();
					item = getCycleByTask(taskDefinitionKey);
					taskService.setDueDate(nextTaskId, getExpectDate(taskDefinitionKey));

					taskService.setVariable(nextTaskId, "task_stage", item.getStage());
					taskService.setVariable(nextTaskId, "task_description", item.getDescription());

					// 分配下一阶段的办理人
					String groups = item.getGroups();
					if (StringUtils.isNotBlank(groups)) {
						// 查找协同人
						List<PmsProjectSynergy> synergys = synergyFacade.getSynergys(projectId, groups);
						if (ValidateUtil.isValid(synergys)) {
							final PmsProjectSynergy synergy = synergys.get(0);
							taskService.setAssignee(nextTaskId, "employee_" + synergy.getEmployeeId());
						}
					}
				}
			}

			// 更新项目当前阶段
			if (item != null) {
				Map<String, Object> metaData = new HashMap<>();
				metaData.put("projectStage", item.getStageId());
				flowFacade.update(metaData, projectId, processInstanceId);
			}

		} finally {
			identityService.setAuthenticatedUserId(null);
		}
		return projectId;

	}

	// 将数据分组
	private Map<String, Map<String, Object>> groupDataIntoMap(Map<String, String> formProperties) {
		if (formProperties != null) {
			Set<Entry<String, String>> entrySet = formProperties.entrySet();
			Map<String, Map<String, Object>> dataMap = new HashMap<String, Map<String, Object>>();
			Map<String, Object> flowMap = new HashMap<String, Object>(); // 项目信息数据集
			Map<String, Object> userMap = new HashMap<String, Object>(); // 用户数据集
			Map<String, Object> teamMap = new HashMap<String, Object>(); // 供应商数据集

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

				// project_team
				if (StringUtils.defaultString(key).startsWith("pt_")) {
					String value = entry.getValue();
					if (StringUtils.isNotBlank(value)) {
						teamMap.put(key.split("_")[1], value);
					}
				}

			}

			dataMap.put(ProjectFlowConstant.PROJECT_FLOW, flowMap);
			dataMap.put(ProjectFlowConstant.PROJECT_USER, userMap);
			dataMap.put(ProjectFlowConstant.PROJECT_TEAM, teamMap);
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

		if (flowList != null) {
			Map<String, Object> projectFlow = flowFacade.getProjectFlowColumnByProjectId(flowList, projectId);
			
			// 价格信息
			Map<String, Object> priceFlow = new HashMap<>();
			if (projectFlow.containsKey("estimatedPrice")) {
				priceFlow.put("estimatedPrice", projectFlow.get("estimatedPrice"));
				projectFlow.remove("estimatedPrice");
			}
			if (projectFlow.containsKey("projectBudget")) {
				priceFlow.put("projectBudget", projectFlow.get("projectBudget"));
				projectFlow.remove("projectBudget");
			}
			priceFlow = editFlowItem(priceFlow);
			param.put("PROJECT_PRICE", priceFlow);

			// 遍历时间
			Object createDate = projectFlow.get("createDate");
			if (createDate != null) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
				try {
					Date date = format.parse(createDate.toString());
					projectFlow.put("createDate", date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			projectFlow = editFlowItem(projectFlow);

			param.put("PROJECT_FLOW", projectFlow);
		}

		if (teamList != null) {
			// 如果为 供应商管家、供应商总监 可以看见所有供应商指定信息
			List<String> teamGroup = new ArrayList<String>(
					Arrays.asList(ProjectRoleType.teamDirector.getId(), ProjectRoleType.teamProvider.getId(),
							ProjectRoleType.sale.getId(), ProjectRoleType.saleDirector.getId()));
			// 如果是 策划供应商可以看见 策划供应商信息
			List<String> teamPlanGroup = new ArrayList<String>(Arrays.asList(ProjectRoleType.teamPlan.getId()));
			// 如果是制作供应商可以看见制作供应商信息
			List<String> teamProudctGroup = new ArrayList<String>(Arrays.asList(ProjectRoleType.teamProduct.getId(),
					ProjectRoleType.supervise.getId(), ProjectRoleType.superviseDirector.getId()));

			for (final Group group : groups) {
				String groupId = group.getId();
				if (teamGroup.contains(groupId)) {
					// 如果为 供应商管家、供应商采购、供应商总监 可以看见所有供应商指定信息
					List<Map<String, Object>> projectTeamPlan = projectTeamFacade
							.getProjectsTeamColumnByProjectId(teamList, projectId, ProjectTeamType.scheme.getCode());
					param.put("PROJECT_TEAMPLAN", projectTeamPlan);

					List<Map<String, Object>> projectTeamProduct = projectTeamFacade
							.getProjectsTeamColumnByProjectId(teamList, projectId, ProjectTeamType.produce.getCode());
					param.put("PROJECT_TEAMPRODUCT", projectTeamProduct);
				} else if (teamPlanGroup.contains(groupId)) {
					// 如果是 策划供应商可以看见 策划供应商信息
					List<Map<String, Object>> projectTeamPlan = projectTeamFacade
							.getProjectsTeamColumnByProjectId(teamList, projectId, ProjectTeamType.scheme.getCode());
					param.put("PROJECT_TEAMPLAN", projectTeamPlan);
				} else if (teamProudctGroup.contains(groupId)) {
					// 如果是制作供应商可以看见制作供应商信息
					List<Map<String, Object>> projectTeamProduct = projectTeamFacade
							.getProjectsTeamColumnByProjectId(teamList, projectId, ProjectTeamType.produce.getCode());
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

	private Map<String, Object> editFlowItem(Map<String, Object> projectFlow) {

		// 项目来源
		if (projectFlow.get("projectSource") != null) {
			String projectSource = (String) projectFlow.get("projectSource");
			for (IndentSource source : IndentSource.values()) {
				if ((source.getValue() + "").equals(projectSource)) {
					projectFlow.put("projectSource", source.getName());
				}
			}
		}

		// 项目评级
		if (projectFlow.get("projectGrade") != null) {
			String projectGrade = "";
			switch ((String) projectFlow.get("projectGrade")) {
			case "5":
				projectGrade = "S";
				break;
			case "4":
				projectGrade = "A";
				break;
			case "3":
				projectGrade = "B";
				break;
			case "2":
				projectGrade = "C";
				break;
			case "1":
				projectGrade = "D";
				break;
			case "0":
				projectGrade = "E";
				break;
			default:
				break;
			}
			projectFlow.put("projectGrade", projectGrade);
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
				if (project != null) {
					PmsEmployee employee = employeeFacade.findEmployeeById(project.getPrincipal());
					project.setPrincipalName(employee.getEmployeeRealName());
					PmsProjectFlowResult result = new PmsProjectFlowResult();
					result.setPmsProjectFlow(project);
					result.setTask(task);
					result.setProcessInstance(processInstance);
					result.setProcessDefinition(getProcessDefinition(processInstance.getProcessDefinitionId()));

					String taskStage = (String) taskService.getVariable(result.getTask().getId(), "task_stage");
					String taskDescription = (String) taskService.getVariable(result.getTask().getId(),
							"task_description");
					result.setTaskStage(taskStage);
					result.setTaskDescription(taskDescription);
					if (userId != null && project != null && userId.equals("employee_" + project.getPrincipal())) {
						// 当前负责人
						result.setIsPrincipal(1);
					} else {
						result.setIsPrincipal(0);
					}
					list.add(result);
				}
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
	public void suspendProcess(String processInstanceId, String projectId, SessionInfo sessionInfo, String remark) {
		runtimeService.suspendProcessInstanceById(processInstanceId);
		Map<String, Object> metaData = new HashMap<String, Object>();
		metaData.put("projectStatus", ProjectFlowStatus.suspend.getId());
		metaData.put("suspendDate", new Date());
		flowFacade.update(metaData, projectId, processInstanceId);
		// 记录项目日志
		List<String> flowData = new ArrayList<String>();
		flowData.add("projectName");
		Map<String, Object> flowInfo = flowFacade.getProjectFlowColumnByProjectId(flowData, projectId);
		messageService.insertOperationLog(projectId, null, null,
				"暂停了\"" + flowInfo.get("projectName") + "\"项目,暂停原因：" + remark, sessionInfo);
	}

	@Override
	public void activateProcess(String processInstanceId, String projectId, SessionInfo sessionInfo) {
		runtimeService.activateProcessInstanceById(processInstanceId);
		Map<String, Object> metaData = new HashMap<String, Object>();
		metaData.put("projectStatus", null);
		flowFacade.update(metaData, projectId, processInstanceId);
		// 记录项目日志
		List<String> flowData = new ArrayList<String>();
		flowData.add("projectName");
		Map<String, Object> flowInfo = flowFacade.getProjectFlowColumnByProjectId(flowData, projectId);
		messageService.insertOperationLog(projectId, null, null, "恢复了\"" + flowInfo.get("projectName") + "\"项目",
				sessionInfo);
	}

	@Override
	public List<PmsProjectFlowResult> getSuspendTasks(String userId) {

		String sql = "";
		if (StringUtils.isBlank(userId)) {
			// 如果userId为空，那么查询所有的项目
			sql = "SELECT DISTINCT RES.ID_,RES.* FROM pat.PROJECT_FLOW PF,ACT_RU_EXECUTION RES LEFT JOIN ACT_HI_TASKINST ART ON ART.PROC_INST_ID_ = RES.PROC_INST_ID_ WHERE PF.projectId = RES.BUSINESS_KEY_ AND PF.projectStatus = 'suspend' AND ACT_ID_ IS NOT NULL AND IS_ACTIVE_ = 1 AND SUSPENSION_STATE_ = 2 ORDER BY START_TIME_ DESC";
		} else if (userId.indexOf("team_") > -1) {
			sql = "SELECT DISTINCT RES.ID_,RES.* FROM pat.PROJECT_FLOW PF,ACT_RU_EXECUTION RES "
					+ " LEFT JOIN ACT_HI_TASKINST ART ON ART.PROC_INST_ID_ = RES.PROC_INST_ID_ "
					+ " WHERE PF.projectId = RES.BUSINESS_KEY_ " + " AND PF.projectStatus = 'suspend' "
					+ " AND ART.ASSIGNEE_ = '" + userId + "'"
					+ " AND ACT_ID_ IS NOT NULL AND IS_ACTIVE_ = 1 AND SUSPENSION_STATE_ = 2 ORDER BY PROC_INST_ID_ DESC";
		} else {
			sql = "SELECT DISTINCT RES.ID_,RES.* FROM pat.PROJECT_FLOW PF,ACT_RU_EXECUTION RES "
					+ "LEFT JOIN pat.PROJECT_SYNERGY SY ON RES.BUSINESS_KEY_ = SY.projectId"
					/*
					 * + " LEFT JOIN ACT_HI_TASKINST ART ON ART.PROC_INST_ID_ = RES.PROC_INST_ID_ "
					 */
					+ " WHERE PF.projectId = RES.BUSINESS_KEY_ " + " AND SY.employeeId = " + userId.split("_")[1]
					+ " AND PF.projectStatus = 'suspend' "
					/*
					 * + " AND ART.ASSIGNEE_ = '" + userId + "'"
					 */
					+ " AND ACT_ID_ IS NOT NULL AND IS_ACTIVE_ = 1 AND SUSPENSION_STATE_ = 2 ORDER BY PROC_INST_ID_ DESC";
		}

		return getRuningTaskBySql(sql, userId);
	}

	@Override
	public List<PmsProjectSynergy> getSynergy(String userId, String projectId, SessionInfo info) {
		if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(projectId)) {

			Map<String, PmsProjectSynergy> synergyMap = synergyFacade.getSynergysByProjectId(projectId);
			List<PmsProjectSynergy> result = new ArrayList<PmsProjectSynergy>();

			String sessionType = info.getSessionType();// provider

			// 如果是非供应商，那么添加 主负责人
			if (!PmsConstant.ROLE_PROVIDER.equals(sessionType)) {
				// 查找主负责人
				PmsProjectSynergy assignee = synergyMap.get(ProjectRoleType.sale.getId());
				if (assignee != null) {
					assignee.setEmployeeGroup(ProjectRoleType.assignee.getText());
					result.add(assignee);
				}
			}

			if (synergyMap != null && !synergyMap.isEmpty()) {
				// 删除主负责人
				synergyMap.remove(ProjectRoleType.sale.getId());

				for (Entry<String, PmsProjectSynergy> entry : synergyMap.entrySet()) {
					String projectRole = entry.getKey();
					PmsProjectSynergy synergy = entry.getValue();
					synergy.setEmployeeGroup(ProjectRoleType.getEnum(projectRole).getText());
					// 如果是供应商，那么只加载供应商管家和监制
					if (PmsConstant.ROLE_PROVIDER.equals(sessionType)) {
						if (ProjectRoleType.teamProvider.getId().equals(projectRole)) {
							result.add(synergy);
						} else if (ProjectRoleType.supervise.getId().equals(projectRole)) {
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
		ProjectCycleItem cycle = workFlowFacade.getCycleByTaskId(taskId);
		if (cycle == null || cycle.getDuration() == null) {
			// 数据错误
			return null;
		}
		return DateUtils.addHour(new Date(), cycle.getDuration());
	}

	@Override
	public Map<String, String> getTaskStateAndDescription(String taskId) {
		Map<String, String> param = new HashMap<String, String>();
		// 当前节点的阶段
		String taskStage = (String) taskService.getVariable(taskId, "task_stage");
		String taskDescription = (String) taskService.getVariable(taskId, "task_description");

		param.put("taskStage", taskStage);
		param.put("taskDescription", taskDescription);

		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		param.put("taskName", task.getName());
		if (task.getDueDate() != null)
			param.put("dueDate", task.getDueDate().toString());
		return param;
	}

	@Override
	public Map<String, String> getUserByRole(String roleType) {
		Map<String, String> param = new HashMap<String, String>();
		List<User> userList = identityService.createUserQuery().memberOfGroup(roleType).list();
		for (User user : userList) {
			param.put(user.getId(), user.getFirstName());
		}
		return param;
	}

	@Override
	public Map<String, Object> getProjectTaskList(String projectId, String taskStage) {
		// 已进行任务节点
		List<HistoricTaskInstance> historyInstances = historyService.createHistoricTaskInstanceQuery()
				.processInstanceBusinessKey(projectId).orderByTaskCreateTime().asc().list();// .finished()

		// 获取所有节点
		Map<String, ProjectCycleItem> cycles = workFlowFacade.getAllCycleTask();
		if (cycles == null) {
			return null;
		}

		List<User> users = identityService.createUserQuery().list();

		Map<String, Object> result = new HashMap<String, Object>();

		for (HistoricTaskInstance history : historyInstances) {
			ProjectCycleItem cycle = cycles.get(history.getTaskDefinitionKey());
			if (!result.containsKey(cycle.getStage())) {
				List<Object> list = new ArrayList<Object>();
				result.put(cycle.getStage(), list);
			}
			// 操作人
			Map<String, Object> item = new HashMap<>();

			item.put("startTime", history.getCreateTime());// 时间格式
			item.put("assigneeId", history.getAssignee());
			for (User user : users) {
				if (user.getId().equals(history.getAssignee())) {
					item.put("assignee", user.getFirstName());// 人名
					break;
				}
			}
			item.put("taskName", history.getName());
			item.put("taskId", history.getId());
			item.put("taskStatus", history.getDeleteReason() == null ? "running" : history.getDeleteReason());// 状态
			item.put("dueDate", history.getDueDate());

			((List<Object>) result.get(cycle.getStage())).add(item);
		}

		// 流程周期与创建时间
		List<String> flowList = new ArrayList<>();
		flowList.add("projectCycle");
		flowList.add("createDate");
		Map<String, Object> projectFlow = flowFacade.getProjectFlowColumnByProjectId(flowList, projectId);
		result.put("projectCycle", projectFlow.get("projectCycle"));
		result.put("createDate", projectFlow.get("createDate"));
		return result;
	}

	@Override
	public Map<String, Object> getTaskInfo(String taskId) {
		HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
		Map<String, Object> item = new HashMap<>();
		item.put("taskId", task.getId());
		item.put("startTime", task.getCreateTime());
		item.put("endTime", task.getEndTime());
		item.put("taskName", task.getName());
		item.put("taskStatus", task.getDeleteReason() == null ? "running" : task.getDeleteReason());// 状态
		item.put("dueDate", task.getDueDate());

		String taskDescription = getCycleByTask(task.getTaskDefinitionKey()).getDescription();

		item.put("taskDescription", taskDescription);
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
				if (StringUtils.isNotBlank(userId) && pmsProjectFlow != null
						&& ("employee_" + pmsProjectFlow.getPrincipal()).equals(userId)) {
					// 当前负责人
					result.setIsPrincipal(1);
				} else {
					result.setIsPrincipal(0);
				}
				resultMap.put(processInstanceId, result);
			}
			List<PmsProjectFlowResult> list = new ArrayList<PmsProjectFlowResult>(resultMap.values());
			return list;
		}
		return null;
	}

	/**
	 * 获取可编辑的字段
	 * 
	 * @param taskId
	 * @param projectId
	 * @param info
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getEditParameter(final String taskId, final String projectId, final String infoType,
			final SessionInfo info) {
		List<String> groupStrs = info.getActivitGroups();
		if (groupStrs != null && !groupStrs.isEmpty()) {
			List<Group> groups = new ArrayList<Group>();
			for (String str : groupStrs) {
				Group group = identityService.createGroupQuery().groupId(str).singleResult();
				if (group != null)
					groups.add(group);
			}

			// 数据提取
			Map<String, Object> varMap = taskService.getVariables(taskId);
			Map<String, List<String>> variablesMap = divideVariables(varMap);

			Map<String, List<String>> param = updateShipFacade.getColumns(groups, infoType);
			if (param != null && !param.isEmpty()) {
				Map<String, Object> result = new HashMap<String, Object>();
				for (Entry<String, List<String>> entry : param.entrySet()) {
					String tableName = entry.getKey();
					List<String> metaData = entry.getValue();
					if ("PROJECT_FLOW".equals(tableName)) {

						Map<String, Object> flow = flowFacade.getProjectFlowColumnByProjectId(metaData, projectId);
						Map<String, Object> flowMap = assembleData(variablesMap, metaData, flow, "PROJECT_FLOW");
						flowMap.put("pf_projectId", projectId);
						result.put("projectFlow", flowMap);
					} else if ("PROJECT_USER".equals(tableName)) {

						Map<String, Object> user = projectUserFacade.getProjectUserColumnByProjectId(metaData,
								projectId);
						Map<String, Object> userMap = assembleData(variablesMap, metaData, user, "PROJECT_USER");
						userMap.put("pu_projectUserId", user.get("projectUserId"));
						result.put("projectUser", userMap);
					} else if ("PROJECT_TEAM".equals(tableName)) {

						List<Map<String, Object>> tList = projectTeamFacade.getProjectsTeamColumnByProjectId(metaData,
								projectId);
						String produce = ProjectTeamType.produce.getCode();
						String scheme = ProjectTeamType.scheme.getCode();
						for (Map<String, Object> tMap : tList) {
							if (tMap != null) {
								// 组装
								List<String> teamVariables = variablesMap.get("PROJECT_TEAM");
								Map<String, Object> teamParam = new HashMap<String, Object>();

								for (String meta : metaData) {
									if (teamVariables.contains(meta)) {
										// 如果包含，则放入相应的map中
										teamParam.put("pt_" + meta, tMap.get(meta));
									}

								}

								teamParam.put("pt_projectTeamId", tMap.get("projectTeamId"));

								if (produce.equals(tMap.get("teamType"))) {
									List<Map<String, Object>> produces = (List<Map<String, Object>>) result
											.get("project_team_produce");
									if (produces != null && !produces.isEmpty())
										produces.add(teamParam);
									else {
										produces = new ArrayList<Map<String, Object>>();
										produces.add(teamParam);
										result.put("project_team_produce", produces);
									}
								} else if (scheme.equals(tMap.get("teamType"))) {
									List<Map<String, Object>> schemes = (List<Map<String, Object>>) result
											.get("project_team_scheme");
									if (schemes != null && !schemes.isEmpty())
										schemes.add(teamParam);
									else {
										schemes = new ArrayList<Map<String, Object>>();
										schemes.add(teamParam);
										result.put("project_team_scheme", schemes);
									}
								}
							}
						}
					}
				}

				return result;
			}
		}

		return null;
	}

	// 拆分业务数据
	public Map<String, List<String>> divideVariables(final Map<String, Object> varMap) {
		Map<String, List<String>> result = new HashMap<String, List<String>>();

		// 项目字段信息列表
		List<String> flowInfoList = new ArrayList<String>();
		// 项目供应商字段信息列表
		List<String> teamInfoList = new ArrayList<String>();
		// 项目客户字段信息列表
		List<String> userInfoList = new ArrayList<String>();
		// 项目付款字段信息列表
		List<String> dealInfoList = new ArrayList<String>();

		if (varMap != null && !varMap.isEmpty()) {
			for (Entry<String, Object> entry : varMap.entrySet()) {
				String key = entry.getKey();
				if (StringUtils.isNoneBlank(key)) {
					String[] keySplit = key.split("_");
					if (keySplit[0].equals("pf")) {
						flowInfoList.add(keySplit[1]);
					} else if (keySplit[0].equals("pt")) {
						teamInfoList.add(keySplit[1]);
					} else if (keySplit[0].equals("pu")) {
						userInfoList.add(keySplit[1]);
					} else if (keySplit[0].equals("dl")) {
						dealInfoList.add(entry.getKey());
					}
				}
			}
			result.put("PROJECT_FLOW", flowInfoList);
			result.put("PROJECT_USER", userInfoList);
			result.put("PROJECT_TEAM", teamInfoList);
			result.put("DEAL_LOG", dealInfoList);
			return result;
		}
		return null;
	}

	// 组装业务数据
	public Map<String, Object> assembleData(Map<String, List<String>> variablesMap, List<String> metaData,
			Map<String, Object> param, String type) {
		List<String> userVariables = variablesMap.get(type);
		Map<String, Object> resultParam = new HashMap<String, Object>();
		// 组装数据
		// 查询当前阶段存在的variable
		for (String meta : metaData) {
			if (userVariables.contains(meta)) {
				String prefix = "";
				if ("PROJECT_FLOW".equals(type))
					prefix = "pf_";
				else if ("PROJECT_USER".equals(type))
					prefix = "pu_";
				else if ("DEAL_LOG".equals(type))
					prefix = "";
				resultParam.put(prefix + meta, param.get(meta));
			}
		}
		return resultParam;
	}

	@Override
	public void updateInformation(Map<String, String> formProperties, SessionInfo info) {
		// 将数据分组
		Map<String, Map<String, Object>> dataMap = groupDataIntoMap(formProperties);

		if (dataMap != null && !dataMap.isEmpty()) {
			Map<String, Object> flowMap = dataMap.get(ProjectFlowConstant.PROJECT_FLOW); // 项目信息数据集
			Map<String, Object> userMap = dataMap.get(ProjectFlowConstant.PROJECT_USER); // 用户数据集

			// 日志内容
			StringBuffer content = new StringBuffer();
			// String activitiGroup = StringUtils.join(info.getActivitGroups(), ",");
			content
					// .append("【").append(activitiGroup).append("】").append(info.getRealName())
					.append("更新了 ");

			if (flowMap != null && !flowMap.isEmpty()) {
				// 更新项目信息
				final String projectId = (String) flowMap.get("projectId");
				if (StringUtils.isNotBlank(projectId)) {
					flowMap.remove("projectId");
					flowFacade.update(flowMap, projectId);

					// 写入日志
					messageService.insertOperationLog(projectId, null, null, content.append(" 项目信息").toString(), info);
				}
			}

			if (userMap != null && !userMap.isEmpty()) {
				// 更新客户信息
				final String projectUserId = (String) userMap.get("projectUserId");
				if (StringUtils.isNotBlank(projectUserId)) {
					userMap.remove("projectUserId");
					projectUserFacade.update(userMap, null, Long.parseLong(projectUserId));

					// 写入日志
					final String projectId = formProperties.get("projectId");
					messageService.insertOperationLog(projectId, null, null, content.append(" 客户信息").toString(), info);
				}
			}

		}
	}

	@Override
	public void updateTeamInformation(List<Map<String, Object>> teamList) {
		if (teamList != null && !teamList.isEmpty()) {
			for (Map<String, Object> teamMap : teamList) {
				if (teamMap != null && !teamMap.isEmpty()) {
					final String projectTeamId = (String) teamMap.get("projectTeamId");
					teamMap.remove("projectTeamId");
					projectTeamFacade.update(teamMap, Long.parseLong(projectTeamId));
				}
			}
		}
	}

	/**
	 * 根据项目名检索任务
	 */
	@Override
	public List<TaskVO> getSearchTasks(String flowName, String activitiUserId) {
		List<TaskVO> result = new ArrayList<>();
		// 根据名称模糊匹配flow
		Map<String, Object> params = new HashMap<>();
		params.put("projectName", flowName);
		if (ValidateUtil.isValid(activitiUserId)) {
			String idName = "";
			if (activitiUserId.startsWith("employee_")) {
				idName = "employeeId";
			} else if (activitiUserId.startsWith("team_")) {
				idName = "teamId";
			} else if (activitiUserId.startsWith("customer_")) {
				idName = "userId";
			} else {
				return result;
			}

			params.put(idName, activitiUserId.split("_")[1]);

		}

		List<PmsProjectFlow> flowList = flowFacade.getProjectFlowByCondition(params);

		if (!ValidateUtil.isValid(flowList)) {
			return result;
		}

		// 获取所有节点
		Map<String, ProjectCycleItem> cycles = workFlowFacade.getAllCycleTask();
		if (cycles == null) {
			cycles = new HashMap<>();
		}

		// 查询所有参与过的任务-流程
		List<String> nn = new ArrayList<>();
		HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery();
		// if (ValidateUtil.isValid(activitiUserId)) {
		// query = query.taskAssignee(activitiUserId);
		// }
		// if (!StringUtils.isBlank(flowName)) {
		for (PmsProjectFlow flow : flowList) {
			if (flow.getProcessInstanceId() != null) {
				nn.add(flow.getProcessInstanceId());
			}
		}
		query = query.processInstanceIdIn(nn);
		// }
		List<HistoricTaskInstance> allTasks = query.list();
		if (!ValidateUtil.isValid(allTasks)) {
			return result;
		}

		Set<String> processInstanceIds = new HashSet<>();
		for (HistoricTaskInstance ht : allTasks) {
			processInstanceIds.add(ht.getProcessInstanceId());
		}
		List<String> instanceIdList = new ArrayList<>(processInstanceIds);

		// 获取进行中任务
		List<Task> runningList = taskService.createTaskQuery().active().processInstanceIdIn(instanceIdList).list();
		// 获取挂起任务
		List<Task> suspendList = taskService.createTaskQuery().suspended().processInstanceIdIn(instanceIdList).list();

		// 处理已完成任务
		flowCheck: for (PmsProjectFlow flow : flowList) {
			if ("finished".equals(flow.getProjectStatus())) {
				for (String hitask : instanceIdList) {
					if (hitask.equals(flow.getProcessInstanceId())) {
						TaskVO each = new TaskVO();
						each.setProcessInstanceId(hitask);
						each.setTaskStatus("completed");
						if (ValidateUtil.isValid(activitiUserId)
								&& ("employee_" + flow.getPrincipal()).equals(activitiUserId)) {
							each.setIsPrincipal(1);
						} else {
							each.setIsPrincipal(0);
						}
						each.setProjectId(flow.getProjectId());
						each.setProjectName(flow.getProjectName());
						each.setPrincipalName(flow.getPrincipalName());
						each.setFinishedDate(flow.getFinishedDate());
						// 已完成的taskId取空
						each.setTaskId(" ");
						result.add(each);
					}
				}
			} else {
				// 处理进行中任务
				for (Task task : runningList) {
					if (task.getProcessInstanceId().equals(flow.getProcessInstanceId())) {
						TaskVO each = new TaskVO();
						taskToVO(task, each, flow, activitiUserId, cycles);
						each.setTaskStatus("running");
						result.add(each);
						continue flowCheck;
					}
				}
				// 处理挂起任务
				for (Task task : suspendList) {
					if (task.getProcessInstanceId().equals(flow.getProcessInstanceId())) {
						TaskVO each = new TaskVO();
						taskToVO(task, each, flow, activitiUserId, cycles);
						each.setTaskStatus("suspend");
						each.setAgent("0");
						result.add(each);

						// 取消任务
						if ("cancel".equals(flow.getProjectStatus())) {
							each.setTaskStatus("cancel");
							each.setCancelDate(DateUtils.getDateByFormat(flow.getUpdateDate(), "yyyy-MM-dd HH:mm:ss"));
						}

						continue flowCheck;
					}
				}
			}
		}

		// 排序
		result.sort(new Comparator<TaskVO>() {
			@Override
			public int compare(TaskVO o1, TaskVO o2) {
				Date o1Date = o1.getCreateTime() == null ? o1.getFinishedDate() : o1.getCreateTime();
				Date o2Date = o2.getCreateTime() == null ? o2.getFinishedDate() : o2.getCreateTime();
				return o2Date.compareTo(o1Date);
			}
		});

		return result;
	}

	private void taskToVO(Task task, TaskVO each, PmsProjectFlow flow, String activitiUserId,
			Map<String, ProjectCycleItem> cycles) {
		each.setTaskId(task.getId());
		each.setTaskName(task.getName());
		each.setAssignee(task.getAssignee());
		each.setDueDate(task.getDueDate());
		each.setCreateTime(task.getCreateTime());
		each.setProcessDefinitionId(task.getProcessDefinitionId());
		each.setProcessInstanceId(task.getProcessInstanceId());

		// for (PmsProjectFlow flow : flowList) {
		// if (task.getProcessInstanceId().equals(flow.getProcessInstanceId())) {
		// 是否是负责人
		if (ValidateUtil.isValid(activitiUserId) && ("employee_" + flow.getPrincipal()).equals(activitiUserId)) {
			each.setIsPrincipal(1);
		} else {
			each.setIsPrincipal(0);
		}
		each.setProjectId(flow.getProjectId());
		each.setProjectName(flow.getProjectName());
		each.setPrincipalName(flow.getPrincipalName());
		each.setSuspendDate(flow.getSuspendDate());
		// break;
		// }
		// }
		each.setTaskStage(cycles.get(task.getTaskDefinitionKey()).getStage());
		each.setTaskDescription(cycles.get(task.getTaskDefinitionKey()).getDescription());

		// 代办任务
		if (task.getAssignee().equals(activitiUserId)) {
			each.setAgent("1");
		} else {
			each.setAgent("0");// 其他任务
		}
	}

	/**
	 * 根据项目阶段筛选其他任务
	 */
	@Override
	public List<TaskVO> getAgentTasksByStage(String stage, String activitiUserId, int flag) {
		return getTasksByStage(stage, activitiUserId, flag, true);
	}

	public List<TaskVO> getTasksByStage(String stage, String activitiUserId, int flag, boolean forAgent) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT rt.* FROM ACT_RU_TASK rt ");

		// 当前人参与
		if (flag == 0) {
			// 筛选参与项目
			sql.append("LEFT JOIN ACT_HI_TASKINST ART  ON rt.PROC_INST_ID_ = ART.PROC_INST_ID_  ");
			sql.append("where ART.ASSIGNEE_='" + activitiUserId + "' ");
		} else {
			// 所有项目
			sql.append("where 1=1 ");
		}
		sql.append("and rt.SUSPENSION_STATE_ = " + SuspensionState.ACTIVE.getStateCode() + " ");
		if (forAgent) {
			sql.append("and rt.ASSIGNEE_!='" + activitiUserId + "' ");
		}

		// 筛选阶段
		if (!StringUtils.isBlank(stage) && !"0".equals(stage)) {
			Map<String, ProjectCycleItem> cycles = workFlowFacade.getAllCycleTask();
			if (cycles == null) {
				return null;
			}
			String taskKeys = "";
			for (ProjectCycleItem item : cycles.values()) {
				if (item.getStageId().equals(stage)) {
					taskKeys += ",'" + item.getTaskId() + "'";
				}
			}
			sql.append("and rt.TASK_DEF_KEY_ in(" + taskKeys.substring(1) + ") ");
		}
		sql.append("ORDER BY rt.CREATE_TIME_ DESC ");

		List<Task> taskList = taskService.createNativeTaskQuery().sql(sql.toString()).list();
		// 组装页面数据
		if (ValidateUtil.isValid(taskList)) {
			Map<String, TaskVO> resultMap = new LinkedHashMap<String, TaskVO>();
			for (final Task task : taskList) {
				TaskVO result = new TaskVO();
				result.setTaskId(task.getId());
				result.setTaskName(task.getName());
				result.setAssignee(task.getAssignee());
				result.setDueDate(task.getDueDate());
				result.setCreateTime(task.getCreateTime());
				if (task.getAssignee().equals(activitiUserId)) {
					result.setAgent("1");
				} else {
					result.setAgent("0");
				}

				String processInstanceId = task.getProcessInstanceId();
				String processDefinitionId = task.getProcessDefinitionId();

				ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
						.processInstanceId(processInstanceId).singleResult();

				result.setProcessDefinitionId(processDefinitionId);
				result.setProcessInstanceId(processInstanceId);

				final String projectId = processInstance.getBusinessKey();

				// 获取 流程业务数据
				PmsProjectFlow pmsProjectFlow = flowFacade.getProjectFlowByProjectId(projectId);
				if (pmsProjectFlow != null && pmsProjectFlow.getPrincipal() != null) {
					PmsEmployee employee = employeeFacade.findEmployeeById(pmsProjectFlow.getPrincipal());
					pmsProjectFlow.setPrincipalName(employee.getEmployeeRealName());
					result.setProjectId(pmsProjectFlow.getProjectId());
					result.setProjectName(pmsProjectFlow.getProjectName());
					result.setPrincipalName(pmsProjectFlow.getPrincipalName());
				}

				String taskStage = (String) taskService.getVariable(result.getTaskId(), "task_stage");
				String taskDescription = (String) taskService.getVariable(result.getTaskId(), "task_description");
				result.setTaskStage(taskStage);
				result.setTaskDescription(taskDescription);
				if (StringUtils.isNotBlank(activitiUserId) && pmsProjectFlow != null
						&& ("employee_" + pmsProjectFlow.getPrincipal()).equals(activitiUserId)) {
					// 当前负责人
					result.setIsPrincipal(1);
				} else {
					result.setIsPrincipal(0);
				}
				resultMap.put(processInstanceId, result);
			}
			List<TaskVO> list = new ArrayList<TaskVO>(resultMap.values());
			return list;
		}
		return null;
	}

	@Override
	public List<KeyValue> getEditResourceList(SessionInfo info, String taskId, String projectId) {
		List<String> groupStrs = info.getActivitGroups();
		if (groupStrs != null && !groupStrs.isEmpty() && StringUtils.isNotBlank(taskId)
				&& StringUtils.isNotBlank(projectId)) {
			List<Group> groups = new ArrayList<Group>();
			for (String groupId : groupStrs) {
				if (StringUtils.isNotBlank(groupId)) {
					Group group = identityService.createGroupQuery().groupId(groupId).singleResult();
					groups.add(group);
				}
			}
			// 获取可以上传的文件
			List<String> fileList = resourceUpdateFacade.getResources(groups);
			// 获取已经存在的文件
			Map<String, Object> variables = taskService.getVariables(taskId);

			List<KeyValue> param = new ArrayList<KeyValue>();
			for (String fileType : fileList) {
				if (variables.containsKey("file_" + fileType)) {
					KeyValue kv = new KeyValue();
					kv.setKey("file_" + fileType);
					kv.setValue(FileType.getEnum(fileType).getText());
					param.add(kv);
				}
			}
			return param;
		}
		return null;
	}

	@Override
	public void cancelProcess(String processInstanceId, String projectId, SessionInfo sessionInfo, String remark) {
		runtimeService.suspendProcessInstanceById(processInstanceId);
		Map<String, Object> metaData = new HashMap<String, Object>();
		metaData.put("projectStatus", ProjectFlowStatus.cancel.getId());
		metaData.put("finishedDate", new Date());
		flowFacade.update(metaData, projectId, processInstanceId);
		// 记录项目日志
		List<String> flowData = new ArrayList<String>();
		flowData.add("projectName");
		Map<String, Object> flowInfo = flowFacade.getProjectFlowColumnByProjectId(flowData, projectId);
		messageService.insertOperationLog(projectId, null, null,
				"取消了\"" + flowInfo.get("projectName") + "\"项目,取消原因：" + remark, sessionInfo);
	}

	@Override
	public List<PmsProjectFlowResult> getCancelTask(String userId) {
		String sql = "";
		if (StringUtils.isBlank(userId)) {
			// 如果userId为空，那么查询所有的项目
			sql = "SELECT DISTINCT RES.ID_,RES.* FROM pat.PROJECT_FLOW PF,ACT_RU_EXECUTION RES LEFT JOIN ACT_HI_TASKINST ART ON ART.PROC_INST_ID_ = RES.PROC_INST_ID_ WHERE PF.projectId = RES.BUSINESS_KEY_ AND PF.projectStatus = 'cancel' AND ACT_ID_ IS NOT NULL AND IS_ACTIVE_ = 1 AND SUSPENSION_STATE_ = 2 ORDER BY START_TIME_ DESC";
		} else if (userId.indexOf("team_") > -1) {
			sql = "SELECT DISTINCT RES.ID_,RES.* FROM pat.PROJECT_FLOW PF,ACT_RU_EXECUTION RES "
					+ "LEFT JOIN ACT_HI_TASKINST ART ON ART.PROC_INST_ID_ = RES.PROC_INST_ID_ "
					+ " WHERE PF.projectId = RES.BUSINESS_KEY_ " + " AND PF.projectStatus = 'cancel' "
					+ " AND ART.ASSIGNEE_ = '" + userId + "'"
					+ " AND ACT_ID_ IS NOT NULL AND IS_ACTIVE_ = 1 AND SUSPENSION_STATE_ = 2 ORDER BY PROC_INST_ID_ DESC";
		} else {
			sql = "SELECT DISTINCT RES.ID_,RES.* FROM pat.PROJECT_FLOW PF,ACT_RU_EXECUTION RES "
					+ "LEFT JOIN pat.PROJECT_SYNERGY SY ON RES.BUSINESS_KEY_ = SY.projectId "
					/*
					 * + "LEFT JOIN ACT_HI_TASKINST ART ON ART.PROC_INST_ID_ = RES.PROC_INST_ID_ "
					 */
					+ " WHERE PF.projectId = RES.BUSINESS_KEY_ " + " AND SY.employeeId = " + userId.split("_")[1]
					+ " AND PF.projectStatus = 'cancel' "
					/*
					 * + " AND ART.ASSIGNEE_ = '" + userId + "'"
					 */
					+ " AND ACT_ID_ IS NOT NULL AND IS_ACTIVE_ = 1 AND SUSPENSION_STATE_ = 2 ORDER BY PROC_INST_ID_ DESC";
		}

		return getRuningTaskBySql(sql, userId);
	}

	@Override
	public List<TaskVO> getTasksByStage(String stage, String activitiUserId, int flag) {
		return getTasksByStage(stage, activitiUserId, flag, false);
	}

	@Override
	public Task getCurrentTaskName(final String taskId) {
		if (StringUtils.isNotBlank(taskId)) {
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			return task;
		}
		return null;
	}

	// 获取 水印样片 以及 水印样片密码
	@Override
	public Map<String, Object> getFlowSample(String projectId) {
		// List<String> columns = new ArrayList<String>();
		String[] columns = new String[] { "sampleUrl", "samplePassword" };
		Map<String, Object> result = flowFacade.getProjectFlowColumnByProjectId(Arrays.asList(columns), projectId);
		return result;
	}

	// 获取 制作供应商 信息
	@Override
	public Map<String, Object> getProduceTeamInfo(String projectId) {
		String[] columns = new String[] { "teamName", "linkman", "telephone", "budget", "makeContent", "makeTime",
				"flag" };
		List<Map<String, Object>> list = projectTeamFacade.getProjectsTeamColumnByProjectId(Arrays.asList(columns),
				projectId, ProjectTeamType.produce.getCode());
		if (ValidateUtil.isValid(list)) {
			Map<String, Object> productTeamMap = list.get(0);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date makeTime = (Date) productTeamMap.get("makeTime");
			if (makeTime != null) {
				String makeTimeStr = formatter.format(makeTime);
				list.get(0).put("makeTime", makeTimeStr);
			}
			return list.get(0);
		}
		return null;
	}

	// 获取 策划供应商 信息
	@Override
	public Map<String, Object> getSchemeTeamInfo(String projectId) {
		String[] columns = new String[] { "teamName", "linkman", "telephone", "budget", "planContent", "planTime",
				"accessMan", "accessManTelephone" };
		List<Map<String, Object>> list = projectTeamFacade.getProjectsTeamColumnByProjectId(Arrays.asList(columns),
				projectId, ProjectTeamType.scheme.getCode());
		if (ValidateUtil.isValid(list)) {
			Map<String, Object> schemeTeamMap = list.get(0);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date planTime = (Date) schemeTeamMap.get("planTime");
			if (planTime != null) {
				String planTimeStr = formatter.format(planTime);
				list.get(0).put("planTime", planTimeStr);
			}
			return list.get(0);
		}
		return null;
	}

	/**
	 * 获取项目当前任务
	 */
	@Override
	public Map<String, Object> getCurentTask(String projectId, String activitiUserId) {
		PmsProjectFlow flow = this.flowFacade.getProjectFlowByProjectId(projectId);
		String processInstanceId = flow.getProcessInstanceId();

		Map<String, Object> result = new HashMap<>();
		result.put("projectId", projectId);
		result.put("processInstanceId", processInstanceId);
		// 项目特殊状态：已完成、取消、暂停
		if (ProjectFlowStatus.finished.getId().equals(flow.getProjectStatus())) {
			result.put("status", "status=finished");
		} else if (ProjectFlowStatus.cancel.getId().equals(flow.getProjectStatus())) {
			result.put("status", "cancel");
		} else if (ProjectFlowStatus.suspend.getId().equals(flow.getProjectStatus())) {
			result.put("status", "pause");
		}
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();

		if (ValidateUtil.isValid(tasks)) {
			Task currentTask = tasks.get(0);
			result.put("status", "doing");
			// 优先取负责任务
			for (Task task : tasks) {
				if (task.getAssignee().equals(activitiUserId)) {
					currentTask = task;
					result.put("status", "task");
				}
			}
			result.put("taskId", currentTask.getId());
		} else {
			result.put("taskId", " ");
		}

		return result;
	}

	@Override
	public List<Map<String, Object>> loadTeamFinanceInfo(final String projectId, final String taskId,
			final String teamType) {
		String[] columns = new String[] { "projectTeamId", "teamName", "actualPrice", "invoiceHead" };

		List<Map<String, Object>> list = projectTeamFacade.getProjectsTeamColumnByProjectId(Arrays.asList(columns),
				projectId, teamType);
		// 获取 流程中分配的唯一制作供应商
		Long projectTeamId = (Long) taskService.getVariable(taskId, "projectTeam_produce");

		return assembleAddTeam(list, "addpt_", projectTeamId);
	}

	public List<Map<String, Object>> assembleAddTeam(List<Map<String, Object>> list, String prefix,
			final Long exceptStr) {
		if (ValidateUtil.isValid(list) && StringUtils.isNotBlank(prefix)) {
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>(list.size());

			for (Map<String, Object> map : list) {
				if (map != null) {
					Integer projectTeamId = (Integer) map.get("projectTeamId");
					if (projectTeamId != null) {
						if (exceptStr.intValue() != projectTeamId.intValue()) {
							Set<Entry<String, Object>> entrySet = map.entrySet();
							final Map<String, Object> resultMap = new HashMap<String, Object>();
							for (Entry<String, Object> entry : entrySet) {
								resultMap.put(prefix + entry.getKey(), entry.getValue());
							}
							result.add(resultMap);
						}
					}
				}
			}

			return result;
		}
		return null;
	}

	@Override
	public void updateTeamInformation(Map<String, String[]> addTeamProperties) {
		if (addTeamProperties != null) {
			String[] teamIds = addTeamProperties.get("addpt_projectTeamId");
			List<Map<String, Object>> param = new ArrayList<Map<String, Object>>(teamIds.length);

			// 初始化 Map
			for (int i = 0; i < teamIds.length; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				param.add(map);
			}

			// 数据分组
			for (Map.Entry<String, String[]> entry : addTeamProperties.entrySet()) {
				String entryKey = entry.getKey();
				String[] values = entry.getValue();
				if (values != null) {
					for (int index = 0; index < values.length; index++) {
						param.get(index).put(entryKey.split("_")[1], values[index]);
					}
				}
			}

			for (Map<String, Object> metaData : param) {
				if (metaData != null) {
					final String projectTeamId = (String) metaData.get("projectTeamId");
					if (StringUtils.isNotBlank(projectTeamId)) {
						metaData.remove("projectTeamId");
						projectTeamFacade.update(metaData, Long.valueOf(projectTeamId));
					}
				}
			}
		}

	}

	@Override
	public List<Map<String, Object>> loadProduceTeamFinanceInfo(String projectId, String taskId, String teamType) {
		String[] columns = new String[] { "projectTeamId", "teamName", "flag", "projectId"};

		List<Map<String, Object>> list = projectTeamFacade.getProjectsTeamColumnByProjectId(Arrays.asList(columns),
				projectId, teamType);
		// 获取 流程中分配的唯一制作供应商
		Long projectTeamId = (Long) taskService.getVariable(taskId, "projectTeam_produce");

		return assembleAddTeam(list, "addft_", projectTeamId);
	}

	@Override
	public void saveFinaceByProduceTeam(Map<String, String[]> financeProperties) {
		if (financeProperties != null) {
			String[] teamIds = financeProperties.get("addft_projectId");
			List<Map<String, Object>> param = new ArrayList<Map<String, Object>>(teamIds.length);

			// 初始化 Map
			for (int i = 0; i < teamIds.length; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				param.add(map);
			}

			// 数据分组
			for (Map.Entry<String, String[]> entry : financeProperties.entrySet()) {
				String entryKey = entry.getKey();
				String[] values = entry.getValue();
				if (values != null) {
					for (int index = 0; index < values.length; index++) {
						if(entryKey.contains("teamName")){
							param.get(index).put("userName", values[index]);
						}else if(entryKey.contains("addft_projectTeamId")){
							param.get(index).put("userId", values[index]);
						}else{
							param.get(index).put(entryKey.split("_")[1], values[index]);
						}
						
					}
				}
			}

			for (Map<String, Object> metaData : param) {
				if (metaData != null) {
					String projectId = (String) metaData.get("projectId");
					if(StringUtils.isNotBlank(projectId)) {
						String json = JSON.toJSONString(metaData);
						PmsDealLog dealLog = JSON.parseObject(json, PmsDealLog.class);
						// 预置信息
						dealLog.setDealStatus(1);
						dealLog.setPayChannel("线下转账");
						dealLog.setUserType(PmsConstant.ROLE_PROVIDER);
						dealLog.setDealLogSource(1);
						dealLog.setLogType(1); // 出账
						
						// 获取 项目名称
						PmsProjectFlow projectFlow = flowFacade.getProjectFlowByProjectId(projectId);
						dealLog.setProjectName(projectFlow.getProjectName());

						// 获取客户ID
//						PmsProjectUser user = projectUserFacade.getProjectUserByProjectId(projectId);
//						dealLog.setUserId(user.getProjectUserId());
//						dealLog.setUserName(user.getUserName());
						
						financeFacade.save(dealLog);
					}
					
				}
			}
		}
		
	}

	@Override
	public Long saveProduceTeam(PmsProjectTeam team) {
		if(team != null) {
			String projectId = team.getProjectId();
			if(StringUtils.isNotBlank(projectId)) {
				team.setTeamType(ProjectTeamType.produce.getCode());
				long result = projectTeamFacade.insert(team);
				// TODO 发送邮件给供应商、销售总监、监制、监制总监、供应商、供应商总监
//				projectProductAddMQService.sendMessage(projectId,result+"");
				return result;
			}
		}
		return null;
	}

	@Override
	public boolean deleteProduceTeam(final PmsProjectTeam team) {
		if(team != null) {
			final Long projectTeamId = team.getProjectTeamId();
			if(projectTeamId != null) {
				Map<String, Object> metaData = new HashMap<String, Object>();
				metaData.put("flag", 1);
				metaData.put("projectTeamId", projectTeamId);
				metaData.put("reason", team.getReason());
				final long result = projectTeamFacade.update(metaData, projectTeamId);
				// TODO 发送邮件给供应商、销售总监、监制、监制总监、供应商、供应商总监
				if(result > -1){
//					projectProductCancelMQService.sendMessage(team.getProjectId(),projectTeamId+"");
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean checkProduceTeamIsOnly(final String projectId) {
		if(StringUtils.isNotBlank(projectId)) {
			Long count = projectTeamFacade.countProjectsTeamByProjectId(ProjectTeamType.produce.getCode(), projectId);
			return count > 1 ? true : false;
		}
		return false;
	}

	@Override
	public List<PmsProjectFlow> getSelfProjectByName(String projectName, Long reqiureId) {
		return flowFacade.getSynerteticProjectByName(reqiureId,projectName);
	}
}
