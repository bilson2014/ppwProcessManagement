package com.paipianwang.activiti.resources.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.impl.form.TaskFormDataImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.paipianwang.activiti.service.ProjectWorkFlowService;
import com.paipianwang.activiti.utils.DataUtils;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.workflow.entity.PmsProjectFlow;
import com.paipianwang.pat.workflow.entity.PmsProjectFlowResult;
import com.paipianwang.pat.workflow.entity.PmsProjectSynergy;
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
	 * 新建项目页
	 * 
	 * @return
	 */
	@RequestMapping("/start/project")
	public ModelAndView createProjectFlowView(ModelMap model) {
		// 生成项目编号
		final String projectId = projectWorkFlowService.generateProjectId();
		model.addAttribute("pf_projectId", projectId);
		return new ModelAndView("activiti/createFlow", model);
	}

	/**
	 * 新建项目
	 * 
	 * @return
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

		ProcessInstance processInstance = projectWorkFlowService.startFormAndProcessInstance(null, formProperties,
				info, properties);
		redirectAttributes.addFlashAttribute("message", "启动成功，流程ID：" + processInstance.getId());

		return new ModelAndView("redirect:/project/running-task");
	}

	/**
	 * 查询正在进行的任务列表
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping("/running-task")
	public ModelAndView taskList(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/activiti/textFlow");
		SessionInfo info = getCurrentInfo(request);
		List<String> groups = info.getActivitGroups();
		// 判断身份
		if (groups.contains(ProjectRoleType.teamDirector.getId())
				|| groups.contains(ProjectRoleType.financeDirector.getId())
				|| groups.contains(ProjectRoleType.customerDirector.getId())) {
			// 供应商总监、财务总监、客服总监 应该看见所有项目
			// 查询参与的正在进行中的任务
			List<PmsProjectFlowResult> runnintTasks = projectWorkFlowService.getRunningTasks(null);
			mv.addObject("runningTasks", runnintTasks);
		} else {
			// 查询代办任务
			List<PmsProjectFlowResult> gTasks = projectWorkFlowService.getTodoTasks(info.getActivitiUserId());

			// 查询参与的正在进行中的任务
			List<PmsProjectFlowResult> runnintTasks = projectWorkFlowService.getRunningTasks(info.getActivitiUserId());

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
				mv.addObject("runningTasks", runningList);
			} else {
				mv.addObject("runningTasks", runnintTasks);
			}

			mv.addObject("gTasks", gTasks);
		}
		
		
		//当前登陆人信息
		mv.addObject("realName", info.getRealName());
		mv.addObject("photo", info.getPhoto());
		
		return mv;
	}
	
	
	@RequestMapping("/running-doing")
	public ModelAndView taskLists(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/activiti/doingFlow");
		SessionInfo info = getCurrentInfo(request);
		List<String> groups = info.getActivitGroups();
		// 判断身份
		if (groups.contains(ProjectRoleType.teamDirector.getId())
				|| groups.contains(ProjectRoleType.financeDirector.getId())
				|| groups.contains(ProjectRoleType.customerDirector.getId())) {
			// 供应商总监、财务总监、客服总监 应该看见所有项目
			// 查询参与的正在进行中的任务
			List<PmsProjectFlowResult> runnintTasks = projectWorkFlowService.getRunningTasks(null);
			mv.addObject("runningTasks", runnintTasks);
		} else {
			// 查询代办任务
			List<PmsProjectFlowResult> gTasks = projectWorkFlowService.getTodoTasks(info.getActivitiUserId());

			// 查询参与的正在进行中的任务
			List<PmsProjectFlowResult> runnintTasks = projectWorkFlowService.getRunningTasks(info.getActivitiUserId());

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
				mv.addObject("runningTasks", runningList);
			} else {
				mv.addObject("runningTasks", runnintTasks);
			}

			mv.addObject("gTasks", gTasks);
		}

		return mv;
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
	 * @param taskId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "get-form/task/{taskId}", method = RequestMethod.POST)
	public Map<String, Object> getTaskForm(@PathVariable("taskId") final String taskId) {
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
				if(formProperty.getId().equals("schemeId")){
					Map<String,String> scheme=projectWorkFlowService.getUserByRole(ProjectRoleType.scheme.getId());
					result.put("schemeId", scheme);
				}else if(formProperty.getId().equals("superviseId")){
					Map<String,String> supervise=projectWorkFlowService.getUserByRole(ProjectRoleType.supervise.getId());
					result.put("superviseId", supervise);
				}
				
			}
		}
		return result;
	}
	
	/**
	 * 详情页面
	 * @param taskId
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/task/{taskId}")
	public ModelAndView TaskFormView(@PathVariable("taskId") final String taskId, HttpServletRequest request) {
		// 获取可见数据
		SessionInfo info = getCurrentInfo(request);
		Map<String, Object> param = projectWorkFlowService.getReadableColumns(info.getActivitiUserId(), taskId);
		List<PmsProjectSynergy> synergyList = projectWorkFlowService.getSynergy(info.getActivitiUserId(), taskId);
		
		// 获取当前节点所在阶段 以及 备注信息
		Map<String, String> state = projectWorkFlowService.getTaskStateAndDescription(taskId);
		Map<String, Object> flowMap = (Map<String, Object>) param.get("PROJECT_FLOW");
		Map<String, Object> priceMap = (Map<String, Object>) param.get("PROJECT_PRICE");
		List<Map<String, Object>> teamPlanMap = (List<Map<String, Object>>) param.get("PROJECT_TEAMPLAN");
		List<Map<String, Object>> teamProductMap = (List<Map<String, Object>>) param.get("PROJECT_TEAMPRODUCT");
		Map<String, Object> userMap = (Map<String, Object>) param.get("PROJECT_USER");
		
		ModelAndView mv = new ModelAndView("/activiti/flowInfo");
		// 项目信息
		mv.addObject("flow_info", flowMap);
		// 策划供应商信息
		mv.addObject("teamPlan_info", teamPlanMap);
		// 制作供应商信息
		mv.addObject("teamProduct_info", teamProductMap);
		// 客户信息
		mv.addObject("user_info", userMap);
		// 协同人信息
		mv.addObject("synergyList", synergyList);
		// 当前任务所在阶段
		mv.addObject("taskStage", state.get("taskStage"));
		// 价格信息
		mv.addObject("price_info",priceMap);
		
		// 当前任务的描述信息
		mv.addObject("taskDescription", state.get("taskDescription"));
		mv.addObject("taskName", state.get("taskName"));
		mv.addObject("taskId",taskId);
		mv.addObject("projectId",param.get("PROJECT_ID"));
		mv.addObject("processInstanceId",param.get("INSTANCE_ID"));
		if(flowMap!=null){
			mv.addObject("projectName", flowMap.get("项目名称"));
			mv.addObject("projectGrade", flowMap.get("项目评级"));
		}
		if(userMap!=null){
			mv.addObject("userLevel", userMap.get("客户评级"));
		}
		return mv;
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
		projectWorkFlowService.completeTaskFromData(taskId, formProperties, info.getActivitiUserId(),info.getActivitGroups());

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
		List<PmsProjectFlowResult> list = projectWorkFlowService.getFinishedTask(info.getActivitiUserId());
		mv.addObject("finishedTasks", list);
		return mv;
	}

	// 挂起
	@RequestMapping("/suspendProcess/{processInstandeId}")
	public ModelAndView suspendProcess(@PathVariable("processInstandeId") final String processInstanceId) {
		if (StringUtils.isNotBlank(processInstanceId)) {
			// 挂起
			projectWorkFlowService.suspendProcess(processInstanceId);
		}
		return new ModelAndView("redirect:/project/running-doing");
	}

	/**
	 * 查询挂起列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/suspend-task")
	public ModelAndView suspend(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/activiti/pauseFlow");
		SessionInfo info = getCurrentInfo(request);
		List<String> groups = info.getActivitGroups();
		List<PmsProjectFlowResult> suspendTasks = null;
		// 判断身份
		if (groups.contains(ProjectRoleType.teamDirector.getId())
				|| groups.contains(ProjectRoleType.financeDirector.getId())
				|| groups.contains(ProjectRoleType.customerDirector.getId())) {
			// 供应商总监、财务总监、客服总监 应该看见所有项目
			suspendTasks = projectWorkFlowService.getSuspendTasks(null);
		}
		suspendTasks = projectWorkFlowService.getSuspendTasks(info.getActivitiUserId());
		mv.addObject("suspendTasks", suspendTasks);
		return mv;
	}

	/**
	 * 激活
	 * @param processInstanceId
	 *            流程实例ID
	 * @return
	 */
	@RequestMapping("/activateProcess/{processInstandeId}")
	public ModelAndView ActivateProcess(@PathVariable("processInstandeId") final String processInstanceId) {
//		ModelAndView mv = new ModelAndView("/activiti/doingFlow");
		if (StringUtils.isNotBlank(processInstanceId)) {
			// 激活
			projectWorkFlowService.activateProcess(processInstanceId);
		}
//		return mv;
		return new ModelAndView("redirect:/project/suspend-task");
	}
	@RequestMapping("/project-task/{projectId}")
	public Map<String, Object> getProjectTaskList(@PathVariable("projectId") final String projectId) {
		Map<String,Object> result=projectWorkFlowService.getProjectTaskList(projectId);
		return result;
	}
	@RequestMapping("/task-detail/{taskId}")
	public Map<String, Object> getTaskInfo(@PathVariable("taskId") final String taskId) {
		Map<String,Object> result=projectWorkFlowService.getTaskInfo(taskId);
		return result;
	}
}
