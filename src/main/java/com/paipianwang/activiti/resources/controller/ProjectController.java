package com.paipianwang.activiti.resources.controller;

import java.util.ArrayList;
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
import org.activiti.engine.form.AbstractFormType;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.form.FormPropertyHandler;
import org.activiti.engine.impl.form.FormPropertyImpl;
import org.activiti.engine.impl.form.StartFormDataImpl;
import org.activiti.engine.impl.form.StringFormType;
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

import com.paipianwang.activiti.service.ProjectWorkFlowService;
import com.paipianwang.activiti.utils.DataUtils;
import com.paipianwang.activiti.utils.UserUtil;

@RestController
@RequestMapping("/form/project")
public class ProjectController {

	private final Logger logger = LoggerFactory.getLogger(ProjectController.class);

	@Autowired
	private RepositoryService repositoryService = null;

	@Autowired
	private FormService formService = null;

	@Autowired
	private ProjectWorkFlowService prjectWorkFlowService = null;

	@RequestMapping(value = { "process-list", "" })
	public ModelAndView processList(Model model,
			@RequestParam(value = "processType", required = false) final String processType,
			final HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/form/project/dynamic-form-process-list",
				Collections.singletonMap("processType", processType));
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
				.processDefinitionKey("procedure-workflow-4").active().list();
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

		// TODO 获取数据
		Map<String, String> formProperties = new HashMap<String, String>();
		Map<String, String[]> parameterMap = request.getParameterMap();
		Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();

		Map<String, Object> properties = DataUtils.divideFlowData(initData());

		logger.debug("start form parameters: {}", formProperties);
		User user = UserUtil.getUserFromSession(request.getSession());

		// 用户未登录不能操作，实际应用使用权限框架实现，例如Spring Security、Shiro等
		if (user == null || StringUtils.isBlank(user.getId())) {
			return new ModelAndView("redirect:/login?timeout=true");
		}

		ProcessInstance processInstance = prjectWorkFlowService.startFormAndProcessInstance(processDefinitionId,
				formProperties, user.getId(), properties);
		redirectAttributes.addFlashAttribute("message", "启动成功，流程ID：" + processInstance.getId());

		return new ModelAndView("redirect:/form/project/process-list?processType=" + processType);
	}

	@RequestMapping("task/list")
	public ModelAndView taskList(@RequestParam(value = "processType", required = false) String processType,
			HttpSession session) {
		ModelAndView mv = new ModelAndView("/form/project/dynamic-form-task-list");
		User user = UserUtil.getUserFromSession(session);
		List<Task> list = prjectWorkFlowService.getRunningTasks(user.getId());
		mv.addObject("list", list);
		return mv;
	}

	@RequestMapping("/process-instance/running/list")
	public ModelAndView running(HttpSession session,
			@RequestParam(value = "processType", required = false) String processType) {
		ModelAndView mv = new ModelAndView("/form/project/running-list",
				Collections.singletonMap("processType", processType));
		User user = UserUtil.getUserFromSession(session);
		List<Task> list = prjectWorkFlowService.getRunningTasks(user.getId());
		mv.addObject("list", list);
		return mv;
	}

	@RequestMapping("task/claim/{id}")
	public ModelAndView claim(@PathVariable("id") final String taskId, HttpSession session,
			HttpServletRequest request) {

		ModelAndView mv = new ModelAndView("redirect:/form/project/task/list?processType="
				+ StringUtils.defaultString(request.getParameter("processType")));
		User user = UserUtil.getUserFromSession(session);
		prjectWorkFlowService.claim(user.getId(), taskId);
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("get-form/task/{taskId}")
	public Map<String, Object> findTaskForm(@PathVariable("taskId") final String taskId, HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();
		TaskFormDataImpl taskFormData = prjectWorkFlowService.getTaskFormData(taskId);
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

		// TODO 获取可见数据
		User user = UserUtil.getUserFromSession(session);
		Map<String, Object> param = prjectWorkFlowService.getReadableColumns(user, taskId);
		Map<String, Object> flowMap = (Map<String, Object>) param.get("PROJECT_FLOW");
		List<Map<String, Object>> teamMap = (List<Map<String, Object>>) param.get("PROJECT_TEAM");
		Map<String, Object> userMap = (Map<String, Object>) param.get("PROJECT_USER");

		if (flowMap != null && !flowMap.isEmpty()) {
			for (Entry<String, Object> entry : flowMap.entrySet()) {
				String name = entry.getKey();
				Object value = entry.getValue();
				FormPropertyHandler handler = new FormPropertyHandler();
				handler.setId(name);
				handler.setName("column:");
				handler.setType(new StringFormType());
				handler.setWritable(false);
				FormPropertyImpl pro = new FormPropertyImpl(handler);
				pro.setValue(value != null ? value.toString() : null);
				properties.add(pro);
			}
		}

		if (userMap != null && !userMap.isEmpty()) {
			for (Entry<String, Object> entry : userMap.entrySet()) {
				String name = entry.getKey();
				Object value = entry.getValue();
				FormPropertyHandler handler = new FormPropertyHandler();
				handler.setId(name);
				handler.setName("column:");
				handler.setType(new StringFormType());
				handler.setWritable(false);
				FormPropertyImpl pro = new FormPropertyImpl(handler);
				pro.setValue(value != null ? value.toString() : null);
				properties.add(pro);
			}
		}

		if (teamMap != null && !teamMap.isEmpty()) {
			for (Map<String, Object> map : teamMap) {
				for (Entry<String, Object> entry : map.entrySet()) {
					String name = entry.getKey();
					Object value = entry.getValue();
					FormPropertyHandler handler = new FormPropertyHandler();
					handler.setId(name);
					handler.setName("column:");
					handler.setWritable(false);
					handler.setType(new StringFormType());
					FormPropertyImpl pro = new FormPropertyImpl(handler);
					pro.setValue(value != null ? value.toString() : null);
					properties.add(pro);
				}
			}
		}

		/*
		 * List<FormProperty> list = new ArrayList<FormProperty>(); FormPropertyHandler
		 * handler = new FormPropertyHandler(); handler.setName(name);
		 * handler.setVariableName(variableName); handler.setType(new StringFormType());
		 * handler.setReadable(false); handler.setRequired(false);
		 * handler.setWritable(false); FormProperty pro = new FormPropertyImpl(handler);
		 * list.add(pro);
		 */
		taskFormData.setFormProperties(properties);
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

			formProperties.put(key, entry.getValue()[0]);
		}

		logger.debug("start form parameters: {}", formProperties);

		User user = UserUtil.getUserFromSession(request.getSession());

		// 用户未登录不能操作，实际应用使用权限框架实现，例如Spring Security、Shiro等
		if (user == null || StringUtils.isBlank(user.getId())) {
			return new ModelAndView("redirect:/login?timeout=true");
		}
		prjectWorkFlowService.completeTaskFromData(taskId, formProperties, user.getId());

		redirectAttributes.addFlashAttribute("message", "任务完成：taskId=" + taskId);
		return new ModelAndView("redirect:/form/project/task/list?processType=" + processType);
	}

	@RequestMapping("process-instance/finished/list")
	public ModelAndView finished(@RequestParam(value = "processType", required = false) String processType,
			HttpSession session) {
		ModelAndView mv = new ModelAndView("/form/project/finished-list",
				Collections.singletonMap("processType", processType));
		User user = UserUtil.getUserFromSession(session);
		List<HistoricProcessInstance> list = prjectWorkFlowService.getFinishedTask(user.getId());
		mv.addObject("list", list);
		return mv;
	}

	public Map<String, Object> initData() {
		Map<String, Object> map = new HashMap<String, Object>();

		// project_flow
		map.put("pf_projectId", prjectWorkFlowService.generateProjectId());
		map.put("pf_projectName", "项目流程（一）");
		map.put("pf_projectSource", "线上电销");
		map.put("pf_projectGrade", "A");
		map.put("pf_projectDescription", "创建第一个项目");
		map.put("pf_productId", 43);
		map.put("pf_productConfigLevelId", 101);
		map.put("pf_productConfigLevelName", "制作设备");
		map.put("pf_productConfigLength", "3分钟");
		map.put("productConfigAdditionalPackageIds", "1,2");
		map.put("productConfigAdditionalPackageName", "音乐包,导演包");
		map.put("pf_filmDestPath", "http://www.apaipian.com/play/9_7.html");
		map.put("pf_projectBudget", 59800);
		map.put("pf_estimatedPrice", 40000);

		// project_synergy
		map.put("ps_employeeId", 52);
		map.put("ps_employeeName", "liting");
		map.put("ps_employeeGroup", "scheme");

		// project_team
		map.put("pt_teamId", 774);
		map.put("pt_teamName", "拍片网");
		map.put("pt_linkman", "莉莉");
		map.put("pt_telephone", "13001288066");
		map.put("pt_email", "lily@paipianwang.cn");

		// project_user
		map.put("pu_userId", 214);
		map.put("pu_userName", "小王LOL有限公司");
		map.put("pu_linkman", "王留成");
		map.put("pu_telephone", "18511631610");
		map.put("pu_email", "wangliucheng@paipianwang.cn");

		return map;
	}
}
