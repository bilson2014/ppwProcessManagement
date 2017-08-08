package com.paipianwang.activiti.resources.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.activiti.engine.FormService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.form.StartFormDataImpl;
import org.activiti.engine.impl.form.TaskFormDataImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.paipianwang.activiti.service.ActivitiFormService;
import com.paipianwang.activiti.utils.UserUtil;

@RestController
@RequestMapping("/form/dynamic")
public class FormController {

	private final Logger logger = LoggerFactory.getLogger(FormController.class);

	@Autowired
	private RepositoryService repositoryService = null;

	@Autowired
	private FormService formService = null;

	@Autowired
	private ActivitiFormService activitiFormService = null;

	@RequestMapping(value = { "process-list", "" })
	public ModelAndView processList(Model model,
			@RequestParam(value = "processType", required = false) final String processType,
			final HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/form/dynamic/dynamic-form-process-list",
				Collections.singletonMap("processType", processType));
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
				.processDefinitionKey("vacation-dynamic-form-service").active().list();
		mv.addObject("list", list);
		return mv;
	}

	@RequestMapping("/get-form/start/{processDefinitionId}")
	public Map<String, Object> findStartForm(@PathVariable("processDefinitionId") final String processDefinitionId) {
		Map<String, Object> result = new HashMap<String, Object>();

		StartFormDataImpl startFormData = (StartFormDataImpl) formService.getStartFormData(processDefinitionId);
		startFormData.setProcessDefinition(null);

		List<FormProperty> forms = startFormData.getFormProperties();
		for (final FormProperty formProperty : forms) {
			@SuppressWarnings("unchecked")
			Map<String, String> values = (Map<String, String>) formProperty.getType().getInformation("values");
			if (values != null) {
				for (Entry<String, String> enumEntry : values.entrySet()) {
					logger.debug("enum, key: {}, value: {}", enumEntry.getKey(), enumEntry.getValue());
				}
				result.put("enum_" + formProperty.getId(), values);
			}
		}

		result.put("form", startFormData);
		return result;
	}

	@RequestMapping("/start-process/{processDefinitionId}")
	public ModelAndView submitStartFormAndStartProcessInstance(
			@PathVariable("processDefinitionId") final String processDefinitionId,
			@RequestParam(value = "processType", required = false) String processType,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {

		Map<String, String> formProperties = new HashMap<String, String>();
		Map<String, String[]> parameterMap = request.getParameterMap();
		Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();

		for (Entry<String, String[]> entry : entrySet) {
			String key = entry.getKey();
			if (StringUtils.defaultString(key).startsWith("fp_")) {
				formProperties.put(key.split("_")[1], entry.getValue()[0]);
			}
		}

		logger.debug("start form parameters: {}", formProperties);
		User user = UserUtil.getUserFromSession(request.getSession());

		// 用户未登录不能操作，实际应用使用权限框架实现，例如Spring Security、Shiro等
		if (user == null || StringUtils.isBlank(user.getId())) {
			return new ModelAndView("redirect:/login?timeout=true");
		}

		ProcessInstance processInstance = activitiFormService.startFormAndProcessInstance(processDefinitionId,
				formProperties, user.getId());
		redirectAttributes.addFlashAttribute("message", "启动成功，流程ID：" + processInstance.getId());

		return new ModelAndView("redirect:/form/dynamic/process-list?processType=" + processType);
	}

	@RequestMapping("task/list")
	public ModelAndView taskList(@RequestParam(value = "processType", required = false) String processType,
			HttpSession session) {
		ModelAndView mv = new ModelAndView("/form/dynamic/dynamic-form-task-list");
		User user = UserUtil.getUserFromSession(session);
		List<Task> list = activitiFormService.getRunningTasks(user.getId());
		mv.addObject("list", list);
		return mv;
	}

	@RequestMapping("/process-instance/running/list")
	public ModelAndView running(HttpSession session,
			@RequestParam(value = "processType", required = false) String processType) {
		ModelAndView mv = new ModelAndView("/form/running-list", Collections.singletonMap("processType", processType));
		User user = UserUtil.getUserFromSession(session);
		List<Task> list = activitiFormService.getRunningTasks(user.getId());
		mv.addObject("list", list);
		return mv;
	}

	@RequestMapping("task/claim/{id}")
	public ModelAndView claim(@PathVariable("id") final String taskId, HttpSession session,
			HttpServletRequest request) {

		ModelAndView mv = new ModelAndView("redirect:/form/dynamic/task/list?processType="
				+ StringUtils.defaultString(request.getParameter("processType")));
		User user = UserUtil.getUserFromSession(session);
		activitiFormService.claim(user.getId(), taskId);
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("get-form/task/{taskId}")
	public Map<String, Object> findTaskForm(@PathVariable("taskId") final String taskId) {
		Map<String, Object> result = new HashMap<String, Object>();
		TaskFormDataImpl taskFormData = activitiFormService.getTaskFormData(taskId);
		result.put("taskFormData", taskFormData);

		List<FormProperty> properties = taskFormData.getFormProperties();
		if (properties != null) {
			for (final FormProperty formProperty : properties) {
				Map<String, String> values = (Map<String, String>) formProperty.getType().getInformation("values");
				if (values != null) {
					result.put(formProperty.getId(), values);
				}
			}
		}
		return result;
	}

	@RequestMapping("task/complete/{taskId}")
	public ModelAndView completeTask(@PathVariable("taskId") final String taskId,
			@RequestParam(value = "processType", required = false) String processType,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {

		Map<String, String> formProperties = new HashMap<String, String>();
		// 从request中读取参数然后转换
		Map<String, String[]> parameterMap = request.getParameterMap();
		Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
		for (Entry<String, String[]> entry : entrySet) {
			String key = entry.getKey();

			// fp_的意思是form paremeter
			if (StringUtils.defaultString(key).startsWith("fp_")) {
				formProperties.put(key.split("_")[1], entry.getValue()[0]);
			}
		}

		logger.debug("start form parameters: {}", formProperties);

		User user = UserUtil.getUserFromSession(request.getSession());

		// 用户未登录不能操作，实际应用使用权限框架实现，例如Spring Security、Shiro等
		if (user == null || StringUtils.isBlank(user.getId())) {
			return new ModelAndView("redirect:/login?timeout=true");
		}
		activitiFormService.completeTaskFromData(taskId, formProperties, user.getId());

		redirectAttributes.addFlashAttribute("message", "任务完成：taskId=" + taskId);
		return new ModelAndView("redirect:/form/dynamic/task/list?processType=" + processType);
	}

	@RequestMapping("process-instance/finished/list")
	public ModelAndView finished(@RequestParam(value = "processType", required = false) String processType,
			HttpSession session) {
		ModelAndView mv = new ModelAndView("/form/finished-list", Collections.singletonMap("processType", processType));
		User user = UserUtil.getUserFromSession(session);
		List<HistoricProcessInstance> list = activitiFormService.getFinishedTask(user.getId());
		mv.addObject("list", list);
		return mv;
	}
}
