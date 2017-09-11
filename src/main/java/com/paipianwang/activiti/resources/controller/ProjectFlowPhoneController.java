package com.paipianwang.activiti.resources.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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
	@RequestMapping("/todo/{taskId}/{projectId}/{processInstanceId}/{taskName}")
	public ModelAndView todoView(@PathVariable("taskId") final String taskId,
			@PathVariable("projectId") final String projectId,
			@PathVariable("processInstanceId") final String processInstanceId,
			@PathVariable("taskName") final String taskName) {
		ModelAndView mv = new ModelAndView("/phoneActiviti/pFlowInfoTask");
		mv.addObject("taskId", taskId);
		mv.addObject("projectId", projectId);
		mv.addObject("processInstanceId", processInstanceId);
		mv.addObject("taskName", taskName);

		return mv;
	}

	/**
	 * 跳转到 代办任务
	 * 
	 * @return
	 */
	@RequestMapping("/resource/{taskId}/{projectId}/{processInstanceId}/{taskName}")
	public ModelAndView resourceView(@PathVariable("taskId") final String taskId,
			@PathVariable("projectId") final String projectId,
			@PathVariable("processInstanceId") final String processInstanceId,
			@PathVariable("taskName") final String taskName) {
		ModelAndView mv = new ModelAndView("/phoneActiviti/pFlowFile");
		mv.addObject("taskId", taskId);
		mv.addObject("projectId", projectId);
		mv.addObject("processInstanceId", processInstanceId);
		mv.addObject("taskName", taskName);

		return mv;
	}

	/**
	 * 跳转到 留言页
	 * 
	 * @return
	 */
	@RequestMapping("/message/{taskId}/{projectId}/{processInstanceId}/{taskName}")
	public ModelAndView messageView(@PathVariable("taskId") final String taskId,
			@PathVariable("projectId") final String projectId,
			@PathVariable("processInstanceId") final String processInstanceId,
			@PathVariable("taskName") final String taskName) {
		ModelAndView mv = new ModelAndView("/phoneActiviti/pFlowMessage");
		mv.addObject("taskId", taskId);
		mv.addObject("projectId", projectId);
		mv.addObject("processInstanceId", processInstanceId);
		mv.addObject("taskName", taskName);

		return mv;
	}

	/**
	 * 跳转到 流程阶段页
	 * 
	 * @return
	 */
	@RequestMapping("/flow/{taskId}/{projectId}/{processInstanceId}/{taskName}")
	public ModelAndView flowView(@PathVariable("taskId") final String taskId,
			@PathVariable("projectId") final String projectId,
			@PathVariable("processInstanceId") final String processInstanceId,
			@PathVariable("taskName") final String taskName) {
		ModelAndView mv = new ModelAndView("/phoneActiviti/pFlowStep");
		mv.addObject("taskId", taskId);
		mv.addObject("projectId", projectId);
		mv.addObject("processInstanceId", processInstanceId);
		mv.addObject("taskName", taskName);

		return mv;
	}

	/**
	 * 跳转到 流程阶段页
	 * 
	 * @return
	 */
	@RequestMapping("/info/{taskId}/{projectId}/{processInstanceId}/{taskName}")
	public ModelAndView infoView(@PathVariable("taskId") final String taskId,
			@PathVariable("projectId") final String projectId,
			@PathVariable("processInstanceId") final String processInstanceId,
			@PathVariable("taskName") final String taskName) {
		ModelAndView mv = new ModelAndView("/phoneActiviti/pFlowStepInfo");
		mv.addObject("taskId", taskId);
		mv.addObject("projectId", projectId);
		mv.addObject("processInstanceId", processInstanceId);
		mv.addObject("taskName", taskName);

		return mv;
	}
}
