package com.paipianwang.activiti.resources.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.form.TaskFormDataImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.paipianwang.activiti.domin.TaskVO;
import com.paipianwang.activiti.service.ProjectWorkFlowService;
import com.paipianwang.activiti.utils.DataUtils;
import com.paipianwang.pat.common.entity.KeyValue;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.workflow.entity.PmsProjectFlow;
import com.paipianwang.pat.workflow.entity.PmsProjectFlowResult;
import com.paipianwang.pat.workflow.entity.PmsProjectSynergy;
import com.paipianwang.pat.workflow.enums.ProjectFlowStatus;
import com.paipianwang.pat.workflow.enums.ProjectRoleType;

/**
 * 项目流程控制器
 * 
 * @author jacky
 *
 */
@RestController
@RequestMapping("/project")
public class ProjectFlowController extends BaseController {
	private final Logger logger = LoggerFactory.getLogger(ProjectFlowController.class);
	@Autowired
	private ProjectWorkFlowService projectWorkFlowService = null;
	/**
	 * 新建项目页跳转
	 */
	@RequestMapping("/start/project")
	public ModelAndView createProjectFlowView(ModelMap model) {
		// 生成项目编号
		final String projectId = projectWorkFlowService.generateProjectId();
		model.addAttribute("pf_projectId", projectId);
		return new ModelAndView("activiti/createFlow", model);
	}

	/**
	 * 项目新建
	 */
	@RequestMapping("/start-process")
	public ModelAndView submitStartFormAndStartProcessInstance(RedirectAttributes redirectAttributes,
			HttpServletRequest request) {

		// 获取数据
		Map<String, String> formProperties = new HashMap<String, String>();
		Map<String, String[]> parameterMap = request.getParameterMap();
		Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();

		for (final Entry<String, String[]> entry : entrySet) {
			String key = entry.getKey();
			if (StringUtils.defaultString(key).contains("_")) {
				formProperties.put(key, entry.getValue()[0]);
			}
		}

		Map<String, Object> properties = DataUtils.divideFlowData(formProperties);

		logger.debug("start form parameters: {}", properties);
		SessionInfo info = getCurrentInfo(request);

		ProcessInstance processInstance = projectWorkFlowService.startFormAndProcessInstance(null, formProperties, info,
				properties);
		redirectAttributes.addFlashAttribute("message", "启动成功，流程ID：" + processInstance.getId());

		return new ModelAndView("redirect:/project/running-task");
	}

	/**
	 * 查询正在进行中的项目列表（包含代办、参与过的正在进行中项目）
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/running-doing")
	public ModelAndView taskLists(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/activiti/doingFlow");
		SessionInfo info = getCurrentInfo(request);

		Map<String, List<PmsProjectFlowResult>> result = loadRunningDoingTasks(info);
		mv.addObject("runningTasks", result.get("runningTasks"));
		mv.addObject("gTasks", result.get("gTasks"));
		return mv;
	}

	/**
	 * ajax 专属 获取 正在进行中的项目列表（代办、参与过的正在进行中的项目）
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/ajax/loadRuntasks")
	public Map<String, List<PmsProjectFlowResult>> loadRunningDoingTasksByAjax(final HttpServletRequest request) {
		SessionInfo info = getCurrentInfo(request);
		Map<String, List<PmsProjectFlowResult>> result = loadRunningDoingTasks(info);

		// 将task 设置为null
		if (ValidateUtil.isValid(result)) {
			for (Entry<String, List<PmsProjectFlowResult>> entry : result.entrySet()) {
				List<PmsProjectFlowResult> flowResult = entry.getValue();
				if (ValidateUtil.isValid(flowResult)) {
					for (PmsProjectFlowResult pmsProjectFlowResult : flowResult) {
						Task task = pmsProjectFlowResult.getTask();
						ProcessInstance pIs = pmsProjectFlowResult.getProcessInstance();
						ProcessDefinition pd = pmsProjectFlowResult.getProcessDefinition();
						if (task != null) {
							pmsProjectFlowResult.setTaskId(task.getId());
							pmsProjectFlowResult.setTaskName(task.getName());
							pmsProjectFlowResult.setDueDate(task.getDueDate());
							pmsProjectFlowResult.setTask(null);
						}
						if (pIs != null) {
							pmsProjectFlowResult.setProcessInstanceId(pIs.getId());
							pmsProjectFlowResult.setProcessInstance(null);
						}
						if (pd != null) {
							pmsProjectFlowResult.setProcessDefinition(null);
						}
					}
				}
			}
		}
		return result;
	}

	// 获取 正在进行中的项目列表（代办、参与过的正在进行中的项目）
	private Map<String, List<PmsProjectFlowResult>> loadRunningDoingTasks(final SessionInfo info) {
		List<String> groups = info.getActivitGroups();
		if (groups != null && !groups.isEmpty()) {

			Map<String, List<PmsProjectFlowResult>> results = new HashMap<String, List<PmsProjectFlowResult>>();
			// 判断身份
			if (groups.contains(ProjectRoleType.teamDirector.getId())
					|| groups.contains(ProjectRoleType.financeDirector.getId())
					|| groups.contains(ProjectRoleType.customerDirector.getId())) {
				// 供应商总监、财务总监、客服总监 应该看见所有项目
				// 查询参与的正在进行中的任务
				List<PmsProjectFlowResult> runnintTasks = projectWorkFlowService.getRunningTasks(null);
				results.put("runningTasks", runnintTasks);
				return results;
			} else {
				// 查询代办任务
				List<PmsProjectFlowResult> gTasks = projectWorkFlowService.getTodoTasks(info.getActivitiUserId());

				// 查询参与的正在进行中的任务
				List<PmsProjectFlowResult> runnintTasks = projectWorkFlowService
						.getRunningTasks(info.getActivitiUserId());

				// 去除代办任务
				if (gTasks != null && !gTasks.isEmpty() && runnintTasks != null && !runnintTasks.isEmpty()) {

					List<String> todoProjectList = new ArrayList<String>();
					for (final PmsProjectFlowResult result : gTasks) {
						todoProjectList.add(result.getPmsProjectFlow().getProjectId());
					}
					List<PmsProjectFlowResult> runningList = new ArrayList<PmsProjectFlowResult>();
					for (PmsProjectFlowResult result : runnintTasks) {
						PmsProjectFlow flow = result.getPmsProjectFlow();
						if (flow != null && StringUtils.isNotBlank(flow.getProjectId())) {
							if (!todoProjectList.contains(result.getPmsProjectFlow().getProjectId())) {
								runningList.add(result);
							}
						}
					}
					results.put("runningTasks", runningList);
				} else {
					results.put("runningTasks", runnintTasks);
				}

				results.put("gTasks", gTasks);
			}

			return results;
		}
		return null;
	}

	/**
	 * 认领任务
	 * 
	 * @param taskId
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping("task/claim/{id}")
	public ModelAndView claim(@PathVariable("id") final String taskId, HttpServletRequest request) {

		ModelAndView mv = new ModelAndView("redirect:/form/project/task/list?processType="
				+ StringUtils.defaultString(request.getParameter("processType")));
		SessionInfo info = getCurrentInfo(request);
		projectWorkFlowService.claim(info.getActivitiUserId(), taskId);
		return mv;
	}

	/**
	 * 查询当前task的表单数据
	 * 
	 * @param taskId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "get-form/task/{taskId}/{projectId}", method = RequestMethod.POST)
	public Map<String, Object> getTaskForm(@PathVariable("taskId") final String taskId,
			@PathVariable("projectId") final String projectId) {
		Map<String, Object> result = new HashMap<String, Object>();
		TaskFormDataImpl taskFormData = projectWorkFlowService.getTaskFormData(taskId);
		result.put("taskFormData", taskFormData);

		List<FormProperty> properties = taskFormData.getFormProperties();
		if (properties != null) {
			for (final FormProperty formProperty : properties) {
				// enum
				Map<String, String> values = (Map<String, String>) formProperty.getType().getInformation("values");
				if (values != null) {
					result.put(formProperty.getId(), values);
				}
				if (formProperty.getId().equals("schemeId")) {
					Map<String, String> scheme = projectWorkFlowService.getUserByRole(ProjectRoleType.scheme.getId());
					result.put("schemeId", scheme);
				} else if (formProperty.getId().equals("superviseId")) {
					Map<String, String> supervise = projectWorkFlowService
							.getUserByRole(ProjectRoleType.supervise.getId());
					result.put("superviseId", supervise);
				}

			}
		}
		return result;
	}

	/**
	 * 详情页面
	 * 
	 * @param taskId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/task/{taskId}/{projectId}/{processInstanceId}")
	public ModelAndView TaskFormView(@PathVariable("taskId") final String taskId,
			@PathVariable("projectId") final String projectId,
			@PathVariable("processInstanceId") final String processInstanceId, final String status,
			HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/activiti/flowInfo");

		SessionInfo info = getCurrentInfo(request);

		if (StringUtils.isNotBlank(taskId) && StringUtils.isNotBlank(projectId)) {

			Map<String, Object> result = loadInformation(taskId, projectId, status, info);

			// 项目信息
			Map<String, Object> flowMap = (Map<String, Object>) result.get("flowMap");
			mv.addObject("flow_info", flowMap);
			// 策划供应商信息
			mv.addObject("teamPlan_info", result.get("teamPlanMap"));
			// 制作供应商信息
			mv.addObject("teamProduct_info", result.get("teamProductMap"));
			// 客户信息
			Map<String, Object> userMap = (Map<String, Object>) result.get("userMap");
			mv.addObject("user_info", userMap);
			// 协同人信息
			mv.addObject("synergyList", result.get("synergyList"));

			// 当前任务所在阶段
			Map<String, String> state = (Map<String, String>) result.get("state");
			mv.addObject("taskStage", state != null ? state.get("taskStage") : null);
			// 价格信息
			mv.addObject("price_info", result.get("priceMap"));

			// 当前任务的描述信息
			mv.addObject("taskDescription", state != null ? state.get("taskDescription") : null);
			mv.addObject("taskName", state != null ? state.get("taskName") : null);
			mv.addObject("dueDate", state != null ? state.get("dueDate") : null);
			mv.addObject("taskId", taskId);
			mv.addObject("projectId", projectId);
			mv.addObject("processInstanceId", processInstanceId);
			if (flowMap != null) {
				mv.addObject("projectName", flowMap.get("projectName"));
				mv.addObject("projectGrade", flowMap.get("projectGrade"));
			}
			if (userMap != null) {
				mv.addObject("userLevel", userMap.get("userLevel"));
			}
		}
		return mv;
	}

	/**
	 * ajax 专用 获取项目中可以查看的信息（团队信息、项目信息、用户信息、供应商信息）
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/ajax/loadInformation/{taskId}/{projectId}/{processInstanceId}")
	public Map<String, Object> getInformationFromProjectByAjax(@PathVariable("taskId") final String taskId,
			@PathVariable("projectId") final String projectId,
			@PathVariable("processInstanceId") final String processInstanceId, final String status,
			final HttpServletRequest request) {

		SessionInfo info = getCurrentInfo(request);

		if (StringUtils.isNotBlank(taskId) && StringUtils.isNotBlank(projectId)) {
			Map<String, Object> result = loadInformation(taskId, projectId, status, info);
			Map<String, String> state = (Map<String, String>) result.get("state");
			Map<String, Object> flowMap = (Map<String, Object>) result.get("flowMap");
			Map<String, Object> userMap = (Map<String, Object>) result.get("userMap");

			// 当前任务的描述信息
			result.put("taskId", taskId);
			result.put("projectId", projectId);
			result.put("processInstanceId", processInstanceId);
			result.put("taskDescription", state != null ? state.get("taskDescription") : null);
			result.put("taskName", state != null ? state.get("taskName") : null);
			result.put("dueDate", state != null ? state.get("dueDate") : null);

			if (flowMap != null) {
				result.put("projectName", flowMap.get("projectName"));
				result.put("projectGrade", flowMap.get("projectGrade"));
			}
			if (userMap != null) {
				result.put("userLevel", userMap.get("userLevel"));
			}

			return result;
		}
		return null;

	}

	// 获取项目信息
	@SuppressWarnings("unchecked")
	private Map<String, Object> loadInformation(final String taskId, final String projectId, final String status,
			final SessionInfo info) {
		if (StringUtils.isNotBlank(taskId) && StringUtils.isNotBlank(projectId)) {

			Map<String, Object> result = new HashMap<String, Object>();

			// 获取可见数据
			Map<String, Object> param = projectWorkFlowService.getReadableColumns(info.getActivitiUserId(), taskId,
					projectId);
			List<PmsProjectSynergy> synergyList = projectWorkFlowService.getSynergy(info.getActivitiUserId(), projectId,
					info);

			// 获取当前节点所在阶段 以及 备注信息
			Map<String, String> state = null;
			if (!ProjectFlowStatus.finished.getId().equals(status)) {
				state = projectWorkFlowService.getTaskStateAndDescription(taskId);
			}

			Map<String, Object> flowMap = (Map<String, Object>) param.get("PROJECT_FLOW");
			Map<String, Object> priceMap = (Map<String, Object>) param.get("PROJECT_PRICE");
			List<Map<String, Object>> teamPlanMap = (List<Map<String, Object>>) param.get("PROJECT_TEAMPLAN");
			List<Map<String, Object>> teamProductMap = (List<Map<String, Object>>) param.get("PROJECT_TEAMPRODUCT");
			Map<String, Object> userMap = (Map<String, Object>) param.get("PROJECT_USER");

			result.put("flowMap", flowMap);
			result.put("priceMap", priceMap);
			result.put("teamPlanMap", teamPlanMap);
			result.put("teamProductMap", teamProductMap);
			result.put("userMap", userMap);
			result.put("synergyList", synergyList);
			result.put("state", state);
			return result;
		}
		return null;
	}

	/**
	 * 完成当前阶段
	 * 
	 * @param taskId
	 * @param processType
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@RequestMapping("task/complete/{taskId}")
	public ModelAndView completeTask(@PathVariable("taskId") final String taskId, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {

		Map<String, String> formProperties = new HashMap<String, String>();
		// 从request中读取参数然后转换
		Map<String, String[]> parameterMap = request.getParameterMap();
		Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
		for (Entry<String, String[]> entry : entrySet) {
			String key = entry.getKey();
			formProperties.put(key, entry.getValue()[0]);
		}

		logger.debug("start form parameters: {}", formProperties);

		SessionInfo info = getCurrentInfo(request);
		projectWorkFlowService.completeTaskFromData(taskId, formProperties, info.getActivitiUserId(),
				info.getActivitGroups(),info.getRealName());

		redirectAttributes.addFlashAttribute("message", "任务完成：taskId=" + taskId);
		return new ModelAndView("redirect:/project/running-doing");
	}

	/**
	 * 查询已完成的任务列表
	 * 
	 * @param processType
	 * @param session
	 * @return
	 */
	@RequestMapping("/finished/list")
	public ModelAndView finished(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/activiti/finishFlow");
		SessionInfo info = getCurrentInfo(request);
		// 加载数据
		mv.addObject("finishedTasks", loadFinishedProjectList(info));
		return mv;
	}

	/**
	 * ajax 专用 获取完成/取消 项目列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/ajax/loadFinishedList")
	public List<PmsProjectFlowResult> loadFinishedProjectByAjax(HttpServletRequest request) {
		SessionInfo info = getCurrentInfo(request);
		// 加载数据
		List<PmsProjectFlowResult> list = loadFinishedProjectList(info);

		if (ValidateUtil.isValid(list)) {
			for (PmsProjectFlowResult pmsProjectFlowResult : list) {
				Task task = pmsProjectFlowResult.getTask();
				HistoricProcessInstance hPs = pmsProjectFlowResult.getHistoricProcessInstance();
				ProcessInstance pIs = pmsProjectFlowResult.getProcessInstance();
				if (task != null) {
					pmsProjectFlowResult.setTaskId(task.getId());
					pmsProjectFlowResult.setTaskName(task.getName());
					pmsProjectFlowResult.setTask(null);
				}
				if (hPs != null) {
					pmsProjectFlowResult.setEndTime(hPs.getEndTime());
					pmsProjectFlowResult.setProcessInstanceId(hPs.getId());
					pmsProjectFlowResult.setHistoricProcessInstance(null);
				}

				if (pIs != null) {
					pmsProjectFlowResult.setProcessInstanceId(pIs.getId());
					pmsProjectFlowResult.setProcessInstance(null);
				}

			}
		}
		return list;
	}

	// 获取 完成/取消 项目列表
	private List<PmsProjectFlowResult> loadFinishedProjectList(final SessionInfo info) {

		List<String> groups = info.getActivitGroups();
		List<PmsProjectFlowResult> list = new ArrayList<PmsProjectFlowResult>();
		List<PmsProjectFlowResult> cancelList = new ArrayList<PmsProjectFlowResult>();
		// 判断身份
		if (groups.contains(ProjectRoleType.teamDirector.getId())
				|| groups.contains(ProjectRoleType.financeDirector.getId())
				|| groups.contains(ProjectRoleType.customerDirector.getId())) {
			// 供应商总监、财务总监、客服总监 应该看见所有项目
			
			// 查询已完成的项目
			list = projectWorkFlowService.getFinishedTask(null);
			// 查询已取消的项目
			cancelList = projectWorkFlowService.getCancelTask(null);
		} else {
			// 查询已完成的项目
			list = projectWorkFlowService.getFinishedTask(info.getActivitiUserId());
			// 查询已取消的项目
			cancelList = projectWorkFlowService.getCancelTask(info.getActivitiUserId());
		}
		// 混合数据
		if (cancelList != null && !cancelList.isEmpty())
			list.addAll(cancelList);

		return list;
	}

	// 挂起
	@RequestMapping("/suspendProcess/{processInstandeId}/{projectId}")
	public ModelAndView suspendProcess(@PathVariable("processInstandeId") final String processInstanceId,
			@PathVariable("projectId") final String projectId) {
		if (StringUtils.isNotBlank(processInstanceId) && StringUtils.isNotBlank(projectId)) {
			// 挂起
			projectWorkFlowService.suspendProcess(processInstanceId, projectId);
		}
		return new ModelAndView("redirect:/project/running-doing");
	}

	/**
	 * 查询挂起列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/suspend-task")
	public ModelAndView suspendList(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/activiti/pauseFlow");
		SessionInfo info = getCurrentInfo(request);

		mv.addObject("suspendTasks", loadSuspendList(info));
		return mv;
	}

	/**
	 * ajax 专用 获取 挂起项目列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/ajax/loadSuspendList")
	public List<PmsProjectFlowResult> loadSuspendListByAjax(HttpServletRequest request) {

		SessionInfo info = getCurrentInfo(request);

		List<PmsProjectFlowResult> result = loadSuspendList(info);
		// 将task 设置为null
		if (ValidateUtil.isValid(result)) {
			for (PmsProjectFlowResult pmsProjectFlowResult : result) {
				Task task = pmsProjectFlowResult.getTask();
				ProcessInstance pIs = pmsProjectFlowResult.getProcessInstance();
				if (task != null) {
					pmsProjectFlowResult.setTaskId(task.getId());
					pmsProjectFlowResult.setTaskName(task.getName());
					pmsProjectFlowResult.setTask(null);
				}
				if (pIs != null) {
					pmsProjectFlowResult.setProcessInstanceId(pIs.getId());
					pmsProjectFlowResult.setProcessInstance(null);
				}
			}
		}
		return result;
	}

	// 获取 挂起列表
	private List<PmsProjectFlowResult> loadSuspendList(final SessionInfo info) {
		List<String> groups = info.getActivitGroups();
		List<PmsProjectFlowResult> suspendTasks = null;
		if (groups != null && !groups.isEmpty()) {
			// 判断身份
			if (groups.contains(ProjectRoleType.teamDirector.getId())
					|| groups.contains(ProjectRoleType.financeDirector.getId())
					|| groups.contains(ProjectRoleType.customerDirector.getId())) {
				// 供应商总监、财务总监、客服总监 应该看见所有项目
				suspendTasks = projectWorkFlowService.getSuspendTasks(null);
			} else {
				suspendTasks = projectWorkFlowService.getSuspendTasks(info.getActivitiUserId());
			}
		}
		return suspendTasks;
	}

	/**
	 * 激活
	 * 
	 * @param processInstanceId
	 *            流程实例ID
	 * @return
	 */
	@RequestMapping("/activateProcess/{processInstandeId}/{projectId}")
	public ModelAndView ActivateProcess(@PathVariable("processInstandeId") final String processInstanceId,
			@PathVariable("projectId") final String projectId) {
		if (StringUtils.isNotBlank(processInstanceId) && StringUtils.isNotBlank(projectId)) {
			// 激活
			projectWorkFlowService.activateProcess(processInstanceId, projectId);
		}
		return new ModelAndView("redirect:/project/suspend-task");
	}

	/**
	 * task列表集合
	 * 
	 * @param projectId
	 * @param flow
	 * @return
	 */
	@RequestMapping(value = "/project-task/{projectId}", method = RequestMethod.POST)
	public Map<String, Object> getProjectTaskList(@PathVariable("projectId") final String projectId,
			@RequestBody PmsProjectFlow flow) {
		Map<String, Object> result = projectWorkFlowService.getProjectTaskList(projectId, flow.getProjectName());
		return result;
	}

	@RequestMapping("/task-detail/{taskId}")
	public Map<String, Object> getTaskInfo(@PathVariable("taskId") final String taskId) {
		Map<String, Object> result = projectWorkFlowService.getTaskInfo(taskId);
		return result;
	}

	// 查找可以被修改的属性
	@RequestMapping("/task/edit/parameter/{taskId}/{projectId}/{infoType}")
	public Map<String, Object> editParameter(@PathVariable("taskId") final String taskId,
			@PathVariable("projectId") final String projectId, @PathVariable("infoType") final String infoType,
			HttpServletRequest request) {

		SessionInfo info = getCurrentInfo(request);
		Map<String, Object> result = projectWorkFlowService.getEditParameter(taskId, projectId, infoType, info);
		return result;
	}

	// 更新 项目信息、用户信息
	@RequestMapping(value = "/edit/information", method = RequestMethod.POST)
	public ModelAndView updateInformation(final HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("redirect:/project/running-doing");
		// 从request中读取参数然后转换
		Map<String, String[]> paramMap = request.getParameterMap();
		if (paramMap != null && !paramMap.isEmpty()) {
			SessionInfo info = getCurrentInfo(request);
			Map<String, String> formProperties = new HashMap<String, String>();
			for (Entry<String, String[]> entry : paramMap.entrySet()) {
				String key = entry.getKey();
				formProperties.put(key, entry.getValue()[0]);
			}
			projectWorkFlowService.updateInformation(formProperties, info);
		}

		return mv;
	}

	// 更新团队信息
	@RequestMapping(value = "/edit/teamInformation", method = RequestMethod.POST)
	public ModelAndView updateTeamInformation(final HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("redirect:/project/running-doing");

		// 从request中读取参数然后转换
		Map<String, String[]> paramMap = request.getParameterMap();
		String[] teamIds = request.getParameterValues("pt_projectTeamId");
		if (teamIds != null) {
			List<Map<String, Object>> teamList = new ArrayList<Map<String, Object>>(teamIds.length);
			for (int i = 0; i < teamIds.length; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				teamList.add(map);
			}

			for (Entry<String, String[]> entry : paramMap.entrySet()) {
				String key = entry.getKey();
				String[] valueArr = entry.getValue();
				if (key.startsWith("pt_")) {
					if (valueArr != null) {
						for (int index = 0; index < valueArr.length; index++) {
							teamList.get(index).put(key.split("_")[1], valueArr[index]);
						}
					}
				}
			}

			projectWorkFlowService.updateTeamInformation(teamList);
		}

		return mv;
	}

	// 根据项目名称全局检索
	@RequestMapping("/search")
	public List<TaskVO> searchList(HttpServletRequest request, @RequestBody final TaskVO taskVO) {
		SessionInfo info = getCurrentInfo(request);
		List<String> groups = info.getActivitGroups();
		List<TaskVO> tasks = null;
		// 判断身份
		if (groups.contains(ProjectRoleType.teamDirector.getId())
				|| groups.contains(ProjectRoleType.financeDirector.getId())
				|| groups.contains(ProjectRoleType.customerDirector.getId())) {
			// 供应商总监、财务总监、客服总监 应该看见所有项目
			tasks = projectWorkFlowService.getSearchTasks(taskVO.getProjectName(), null);
		} else {
			tasks = projectWorkFlowService.getSearchTasks(taskVO.getProjectName(), info.getActivitiUserId());
		}

		return tasks;
	}

	// 根据项目阶段筛选其他任务
	@RequestMapping("/agent/search")
	public List<TaskVO> agentListByStage(HttpServletRequest request, @RequestBody final TaskVO taskVO) {
		SessionInfo info = getCurrentInfo(request);
		List<String> groups = info.getActivitGroups();
		List<TaskVO> suspendTasks = null;

		// 判断身份
		if (groups.contains(ProjectRoleType.teamDirector.getId())
				|| groups.contains(ProjectRoleType.financeDirector.getId())
				|| groups.contains(ProjectRoleType.customerDirector.getId())) {
			// 供应商总监、财务总监、客服总监 应该看见所有项目
			suspendTasks = projectWorkFlowService.getAgentTasksByStage(taskVO.getTaskStage(), info.getActivitiUserId(),
					1);
		} else {
			suspendTasks = projectWorkFlowService.getAgentTasksByStage(taskVO.getTaskStage(), info.getActivitiUserId(),
					0);
		}
		return suspendTasks;
	}

	// 根据项目阶段筛选其他任务
	@RequestMapping("/search/stage")
	public List<TaskVO> listByStage(HttpServletRequest request, @RequestBody final TaskVO taskVO) {
		SessionInfo info = getCurrentInfo(request);
		List<String> groups = info.getActivitGroups();
		List<TaskVO> suspendTasks = null;

		// 判断身份
		if (groups.contains(ProjectRoleType.teamDirector.getId())
				|| groups.contains(ProjectRoleType.financeDirector.getId())
				|| groups.contains(ProjectRoleType.customerDirector.getId())) {
			// 供应商总监、财务总监、客服总监 应该看见所有项目
			suspendTasks = projectWorkFlowService.getTasksByStage(taskVO.getTaskStage(), info.getActivitiUserId(), 1);
		} else {
			suspendTasks = projectWorkFlowService.getTasksByStage(taskVO.getTaskStage(), info.getActivitiUserId(), 0);
		}
		return suspendTasks;
	}

	// 查找可以修改的文件
	@RequestMapping("/edit/resource/{taskId}/{projectId}")
	public List<KeyValue> editResourceList(final HttpServletRequest request,
			@PathVariable("taskId") final String taskId, @PathVariable("taskId") final String projectId) {
		SessionInfo info = getCurrentInfo(request);
		return projectWorkFlowService.getEditResourceList(info, taskId, projectId);
	}

	// 取消
	@RequestMapping("/cancelProcess/{processInstandeId}/{projectId}")
	public ModelAndView cancelProcess(@PathVariable("processInstandeId") final String processInstanceId,
			@PathVariable("projectId") final String projectId) {
		if (StringUtils.isNotBlank(processInstanceId) && StringUtils.isNotBlank(projectId)) {
			// 挂起
			projectWorkFlowService.cancelProcess(processInstanceId, projectId);
		}
		return new ModelAndView("redirect:/project/running-doing");
	}

}
