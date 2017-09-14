package com.paipianwang.activiti.resources.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.paipianwang.activiti.service.ProjectWorkFlowService;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.workflow.entity.PmsProjectFlow;
import com.paipianwang.pat.workflow.entity.PmsProjectFlowResult;
import com.paipianwang.pat.workflow.entity.PmsProjectSynergy;
import com.paipianwang.pat.workflow.enums.ProjectFlowStatus;
import com.paipianwang.pat.workflow.facade.PmsProjectFlowFacade;

/**
 * 手机端 项目控制器
 * 
 * @author jacky
 *
 */
@RestController
@RequestMapping("/project/phone")
public class ProjectFlowPhoneController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(ProjectFlowPhoneController.class);

	@Autowired
	private PmsProjectFlowFacade projectFlowFacade = null;

	@Autowired
	private ProjectWorkFlowService projectWorkFlowService = null;

	@Autowired
	private TaskService taskService = null;

	/**
	 * 跳转到 项目页
	 * 
	 * @return
	 */
	@RequestMapping("/projectFlow")
	public ModelAndView projectView() {
		ModelAndView mv = new ModelAndView("/phoneActiviti/pFlowList");
		return mv;
	}

	/**
	 * 跳转到 代办任务
	 * 
	 * @return
	 */
	@RequestMapping("/todo/{taskId}/{projectId}/{processInstanceId}")
	public ModelAndView todoView(@PathVariable("taskId") final String taskId,
			@PathVariable("projectId") final String projectId,
			@PathVariable("processInstanceId") final String processInstanceId) {
		ModelAndView mv = new ModelAndView("/phoneActiviti/pFlowInfoTask");
		mv.addObject("taskId", taskId);
		mv.addObject("projectId", projectId);
		mv.addObject("processInstanceId", processInstanceId);

		final PmsProjectFlow flow = projectFlowFacade.getProjectFlowByProjectId(projectId);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		mv.addObject("taskName", task.getName());
		mv.addObject("dueDate", task.getDueDate());

		if (flow != null) {
			mv.addObject("projectName", flow.getProjectName());
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
	@RequestMapping("completeTask/{taskId}")
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
				info.getActivitGroups(), info.getRealName());

		redirectAttributes.addFlashAttribute("message", "任务完成：taskId=" + taskId);
		return new ModelAndView("redirect:/project/phone/projectFlow");
	}

	/**
	 * 跳转到 文件页
	 * 
	 * @return
	 */
	@RequestMapping("/resource/{taskId}/{projectId}/{processInstanceId}")
	public ModelAndView resourceView(@PathVariable("taskId") final String taskId,
			@PathVariable("projectId") final String projectId,
			@PathVariable("processInstanceId") final String processInstanceId) {
		ModelAndView mv = new ModelAndView("/phoneActiviti/pFlowFile");
		mv.addObject("taskId", taskId);
		mv.addObject("projectId", projectId);
		mv.addObject("processInstanceId", processInstanceId);

		final PmsProjectFlow flow = projectFlowFacade.getProjectFlowByProjectId(projectId);
		if (flow != null) {
			mv.addObject("projectName", flow.getProjectName());
		}

		return mv;
	}

	/**
	 * 跳转到 留言页
	 * 
	 * @return
	 */

	@RequestMapping("/message/{taskId}/{projectId}/{processInstanceId}")
	public ModelAndView messageView(@PathVariable("taskId") final String taskId,
			@PathVariable("projectId") final String projectId,
			@PathVariable("processInstanceId") final String processInstanceId) {
		ModelAndView mv = new ModelAndView("/phoneActiviti/pFlowMessage");
		mv.addObject("taskId", taskId);
		mv.addObject("projectId", projectId);
		mv.addObject("processInstanceId", processInstanceId);

		final PmsProjectFlow flow = projectFlowFacade.getProjectFlowByProjectId(projectId);
		if (flow != null) {
			mv.addObject("projectName", flow.getProjectName());
		}
		return mv;
	}

	/**
	 * 跳转到 流程阶段页
	 * 
	 * @return
	 */
	@RequestMapping("/flow/{taskId}/{projectId}/{processInstanceId}")
	public ModelAndView flowView(@PathVariable("taskId") final String taskId,
			@PathVariable("projectId") final String projectId,
			@PathVariable("processInstanceId") final String processInstanceId) {
		ModelAndView mv = new ModelAndView("/phoneActiviti/pFlowStep");
		mv.addObject("taskId", taskId);
		mv.addObject("projectId", projectId);
		mv.addObject("processInstanceId", processInstanceId);

		final PmsProjectFlow flow = projectFlowFacade.getProjectFlowByProjectId(projectId);

		if (flow != null) {
			mv.addObject("projectName", flow.getProjectName());
			mv.addObject("projectStage", flow.getProjectStage());
			mv.addObject("projectStatus", flow.getProjectStatus());
		}

		return mv;
	}

	/**
	 * 跳转到 流程阶段页
	 * 
	 * @return
	 */
	@RequestMapping("/info/{taskId}/{projectId}/{processInstanceId}")
	public ModelAndView infoView(@PathVariable("taskId") final String taskId,
			@PathVariable("projectId") final String projectId,
			@PathVariable("processInstanceId") final String processInstanceId) {
		ModelAndView mv = new ModelAndView("/phoneActiviti/pFlowStepInfo");
		mv.addObject("taskId", taskId);
		mv.addObject("projectId", projectId);
		mv.addObject("processInstanceId", processInstanceId);

		final PmsProjectFlow flow = projectFlowFacade.getProjectFlowByProjectId(projectId);
		if (flow != null) {
			mv.addObject("projectName", flow.getProjectName());
		}
		return mv;
	}

	/**
	 * 项目信息
	 * @param taskId
	 * @param projectId
	 * @param processInstanceId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/flowinfo/{taskId}/{projectId}/{processInstanceId}")
	public ModelAndView flowInfoView(@PathVariable("taskId") final String taskId,
			@PathVariable("projectId") final String projectId,
			@PathVariable("processInstanceId") final String processInstanceId, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/phoneActiviti/pFlowItem");
		mv.addObject("taskId", taskId);
		mv.addObject("projectId", projectId);
		mv.addObject("processInstanceId", processInstanceId);
		SessionInfo info = getCurrentInfo(request);

		// 获取可见数据
		Map<String, Object> param = projectWorkFlowService.getReadableColumns(info.getActivitiUserId(), taskId,
				projectId);
		List<PmsProjectSynergy> synergyList = projectWorkFlowService.getSynergy(info.getActivitiUserId(), projectId,
				info);

		Map<String, Object> flowMap = (Map<String, Object>) param.get("PROJECT_FLOW");
		Map<String, Object> priceMap = (Map<String, Object>) param.get("PROJECT_PRICE");
		List<Map<String, Object>> teamPlanMap = (List<Map<String, Object>>) param.get("PROJECT_TEAMPLAN");
		List<Map<String, Object>> teamProductMap = (List<Map<String, Object>>) param.get("PROJECT_TEAMPRODUCT");
		Map<String, Object> userMap = (Map<String, Object>) param.get("PROJECT_USER");

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
		// 价格信息
		mv.addObject("price_info", priceMap);

		return mv;
	}
	/**
	 * 修改项目信息页
	 * @param taskId
	 * @param projectId
	 * @param processInstanceId
	 * @param request
	 * @return
	 */
	@RequestMapping("/editInfo/{taskId}/{projectId}/{processInstanceId}")
	public ModelAndView editInfoView(@PathVariable("taskId") final String taskId,
			@PathVariable("projectId") final String projectId,
			@PathVariable("processInstanceId") final String processInstanceId) {
		ModelAndView mv = new ModelAndView("/phoneActiviti/pFlowItproject");
		mv.addObject("taskId", taskId);
		mv.addObject("projectId", projectId);
		mv.addObject("processInstanceId", processInstanceId);
		return mv;
	}
	/**
	 * 修改客户信息页
	 * @param taskId
	 * @param projectId
	 * @param processInstanceId
	 * @param request
	 * @return
	 */
	@RequestMapping("/editUser/{taskId}/{projectId}/{processInstanceId}")
	public ModelAndView editUserView(@PathVariable("taskId") final String taskId,
			@PathVariable("projectId") final String projectId,
			@PathVariable("processInstanceId") final String processInstanceId) {
		ModelAndView mv = new ModelAndView("/phoneActiviti/pFlowItclient");
		mv.addObject("taskId", taskId);
		mv.addObject("projectId", projectId);
		mv.addObject("processInstanceId", processInstanceId);
		return mv;
	}
	/**
	 * 修改价格信息页
	 * @param taskId
	 * @param projectId
	 * @param processInstanceId
	 * @param request
	 * @return
	 */
	@RequestMapping("/editPrice/{taskId}/{projectId}/{processInstanceId}")
	public ModelAndView editPriceView(@PathVariable("taskId") final String taskId,
			@PathVariable("projectId") final String projectId,
			@PathVariable("processInstanceId") final String processInstanceId) {
		ModelAndView mv = new ModelAndView("/phoneActiviti/pFlowItprice");
		mv.addObject("taskId", taskId);
		mv.addObject("projectId", projectId);
		mv.addObject("processInstanceId", processInstanceId);
		return mv;
	}
	/**
	 * 修改供应商信息页
	 * @param taskId
	 * @param projectId
	 * @param processInstanceId
	 * @return
	 */
	@RequestMapping("/editTeam/{taskId}/{projectId}/{processInstanceId}")
	public ModelAndView editTeamView(@PathVariable("taskId") final String taskId,
			@PathVariable("projectId") final String projectId,
			@PathVariable("processInstanceId") final String processInstanceId) {
		ModelAndView mv = new ModelAndView("/phoneActiviti/pFlowItprovider");
		mv.addObject("taskId", taskId);
		mv.addObject("projectId", projectId);
		mv.addObject("processInstanceId", processInstanceId);
		return mv;
	}

}
