package com.paipianwang.activiti.resources.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.form.FormPropertyHandler;
import org.activiti.engine.impl.form.FormPropertyImpl;
import org.activiti.engine.impl.form.StringFormType;
import org.activiti.engine.impl.form.TaskFormDataImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.paipianwang.activiti.service.ProjectWorkFlowService;
import com.paipianwang.activiti.utils.DataUtils;
import com.paipianwang.activiti.utils.UserUtil;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.workflow.entity.PmsProjectFlow;
import com.paipianwang.pat.workflow.entity.PmsProjectFlowResult;

/**
 * 项目流程控制器
 * @author jacky
 *
 */
@RestController
@RequestMapping("/project")
public class PorjectFlowController extends BaseController {

	private final Logger logger = LoggerFactory.getLogger(PorjectFlowController.class);

	@Autowired
	private ProjectWorkFlowService prjectWorkFlowService = null;
	
	/**
	 * 新建项目页
	 * @return
	 */
	@RequestMapping("/start/project")
	public ModelAndView createProjectFlowView(ModelMap model) {
		// 生成项目编号
		final String projectId = prjectWorkFlowService.generateProjectId();
		model.addAttribute("pf_projectId", projectId);
		return new ModelAndView("activiti/createFlow", model);
	}
	
	/**
	 * 新建项目
	 * @return
	 */
	@RequestMapping("/start-process")
	public ModelAndView submitStartFormAndStartProcessInstance(
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		
		// 获取数据
		Map<String, String> formProperties = new HashMap<String, String>();
		Map<String, String[]> parameterMap = request.getParameterMap();
		Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
		
		for (final Entry<String, String[]> entry : entrySet) {
			String key = entry.getKey();
			if(StringUtils.defaultString(key).contains("_")) {
				formProperties.put(key, entry.getValue()[0]);
			}
		}
		
		Map<String, Object> properties = DataUtils.divideFlowData(formProperties);

		logger.debug("start form parameters: {}", properties);
		SessionInfo info = getCurrentInfo(request);

		ProcessInstance processInstance = prjectWorkFlowService.startFormAndProcessInstance(null,
				formProperties, info.getActivitiUserId(), properties);
		redirectAttributes.addFlashAttribute("message", "启动成功，流程ID：" + processInstance.getId());

		return new ModelAndView("redirect:/project/running-task");
	}
	
	/**
	 * 查询正在进行的任务列表
	 * @param processType
	 * @param session
	 * @return
	 */
	@RequestMapping("/running-task")
	public ModelAndView taskList(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/activiti/textFlow");
		SessionInfo info = getCurrentInfo(request);
		// 查询代办任务
		List<PmsProjectFlowResult> gTasks = prjectWorkFlowService.getTodoTasks(info.getActivitiUserId());
		
		// 查询参与的正在进行中的任务
		List<PmsProjectFlowResult> runnintTasks = prjectWorkFlowService.getRunningTasks(info.getActivitiUserId());
		mv.addObject("gTasks", gTasks);
		mv.addObject("runningTasks", runnintTasks);
		return mv;
	}
	
	/**
	 * 认领任务
	 * @param taskId
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping("task/claim/{id}")
	public ModelAndView claim(@PathVariable("id") final String taskId, HttpSession session,
			HttpServletRequest request) {

		ModelAndView mv = new ModelAndView("redirect:/form/project/task/list?processType="
				+ StringUtils.defaultString(request.getParameter("processType")));
		User user = UserUtil.getUserFromSession(session);
		prjectWorkFlowService.claim(user.getId(), taskId);
		return mv;
	}
	
	/**
	 * 查询当前task的表单数据
	 * @param taskId
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("get-form/task/{taskId}")
	public ModelAndView findTaskForm(@PathVariable("taskId") final String taskId, HttpSession session) {
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

		// 获取可见数据
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

		taskFormData.setFormProperties(properties);
		return new ModelAndView("/task", result);
	}
	
	/**
	 * 完成当前阶段
	 * @param taskId
	 * @param processType
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@RequestMapping("task/complete/{taskId}")
	public ModelAndView completeTask(@PathVariable("taskId") final String taskId,
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
		return new ModelAndView("redirect:/project/task/list");
	}

	/**
	 * 查询已完成的任务列表
	 * @param processType
	 * @param session
	 * @return
	 */
	@RequestMapping("/finished/list")
	public ModelAndView finished(@RequestParam(value = "processType", required = false) String processType,
			HttpSession session) {
		ModelAndView mv = new ModelAndView("/project/finished-list",
				Collections.singletonMap("processType", processType));
		User user = UserUtil.getUserFromSession(session);
		List<HistoricProcessInstance> list = prjectWorkFlowService.getFinishedTask(user.getId());
		mv.addObject("list", list);
		return mv;
	}
}
