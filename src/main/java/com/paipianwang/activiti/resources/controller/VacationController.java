package com.paipianwang.activiti.resources.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.paipianwang.activiti.service.WorkFlowService;
import com.paipianwang.pat.workflow.entity.PmsVacation;
import com.paipianwang.pat.workflow.entity.PmsVariable;

@RestController
@RequestMapping("/vacation")
public class VacationController {

	private Logger logger = LoggerFactory.getLogger(VacationController.class);
	
	@Autowired
	private WorkFlowService workflowService = null;
	
	@RequestMapping(value = "start", method = RequestMethod.POST)
	public ModelAndView startFlow(final PmsVacation vacation, final RedirectAttributes redirectAttributes) {
		
		vacation.setUserId("kafeitu");
		Map<String, Object> variables = new HashMap<String, Object>();
		ProcessInstance processInstance = workflowService.startWorkflow(vacation, variables);
		redirectAttributes.addFlashAttribute("message", "流程已启动，流程ID : " + processInstance.getId());
		logger.info("流程启动了，流程ID : " + processInstance.getId());
		return new ModelAndView("redirect:/");
	}
	
	/**
	 * 代办任务
	 * @param userId
	 * @param model
	 * @return
	 */
	@RequestMapping("todo/{userId}")
	public ModelAndView taskToDo(@PathVariable("userId") final String userId, final ModelMap model) {
		
		List<PmsVacation> vacates = workflowService.findTodoTasks(userId);
		model.addAttribute("list", vacates);
		model.addAttribute("author", userId);
		return new ModelAndView("task", model);
	}
	
	
	/**
	 * 签收任务
	 * @param userId
	 * @param taskId
	 * @return
	 */
	@RequestMapping("task/claim/{author}/{taskId}")
	public ModelAndView claimTask(@PathVariable("author") final String author, @PathVariable("taskId") final String taskId ) {
		workflowService.claimTask(taskId, author);
		return new ModelAndView("redirect:/vacation/todo/" + author);
	}
	
	/**
	 * 正在进行的项目（参与的）
	 * @param userId
	 * @return
	 */
	@RequestMapping("list/running/{author}")
	public ModelAndView runningTask(@PathVariable("author") final String author, final ModelMap model) {
		List<PmsVacation> list = workflowService.findRunningProcessInstances(author);
		model.addAttribute("list", list);
		model.addAttribute("author", author);
		return new ModelAndView("task",model);
	}
	
	/**
	 * 已完成的项目（参与的�?
	 * @param userId
	 * @return
	 */
	@RequestMapping("list/finished/{author}")
	public ModelAndView finishedTask(@PathVariable("author") final String author, final ModelMap model) {
		List<PmsVacation> list = workflowService.findFinishedProcessInstaces(author);
		model.addAttribute("list", list);
		model.addAttribute("author", author);
		return new ModelAndView("task",model);
	}
	
	/**
     * 完成任务
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "complete/{id}", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String complete(@PathVariable("id") String taskId, PmsVariable var) {
        try {
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put(var.getKey(), Boolean.parseBoolean(var.getValue()));
            workflowService.complete(taskId, variables);
            return "success";
        } catch (Exception e) {
            logger.error("error on complete task {}, variables={}", new Object[]{taskId, var.toString(), e});
            return "error";
        }
    }
}
