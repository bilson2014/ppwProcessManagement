package com.paipianwang.activiti.resources.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.paipianwang.pat.workflow.entity.PmsProjectFlow;
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
		if(flow != null) {
			mv.addObject("projectName", flow.getProjectName());
		}

		return mv;
	}

	/**
	 * 跳转到 代办任务
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
		if(flow != null) {
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
		if(flow != null) {
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
		if(flow != null) {
			mv.addObject("projectName", flow.getProjectName());
			mv.addObject("projectStage", flow.getProjectStage());
			mv.addObject("projectStatus",flow.getProjectStatus());
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
		if(flow != null) {
			mv.addObject("projectName", flow.getProjectName());
		}

		return mv;
	}
}
