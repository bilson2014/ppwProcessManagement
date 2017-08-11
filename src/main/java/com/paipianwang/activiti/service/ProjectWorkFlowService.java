package com.paipianwang.activiti.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.form.TaskFormDataImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.paipianwang.pat.workflow.entity.PmsProjectFlow;
import com.paipianwang.pat.workflow.entity.PmsProjectFlowResult;

public interface ProjectWorkFlowService {

	/**
	 * 流程启动
	 */
	public ProcessInstance startFormAndProcessInstance(String processDefinitionId, Map<String, String> formProperties,
			String userId, Map<String, Object> form);

	/**
	 * 获取当前登陆人当前参与的任务
	 * 
	 * @param id
	 * @return
	 */
	public List<PmsProjectFlowResult> getRunningTasks(String userId);

	/**
	 * 查询已完成任务
	 * 
	 * @param id
	 * @return
	 */
	public List<HistoricProcessInstance> getFinishedTask(String userId);

	/**
	 * 获取流程节点的表单
	 * 
	 * @param taskId
	 * @return
	 */
	public TaskFormDataImpl getTaskFormData(String taskId);

	/**
	 * 签收任务
	 * 
	 * @param id
	 * @param taskId
	 */
	public void claim(String userId, String taskId);

	/**
	 * 提交表单，完成节点任务
	 * 
	 * @param taskId
	 * @param formProperties
	 * @param id
	 */
	public void completeTaskFromData(String taskId, Map<String, String> formProperties, String userId);

	/**
	 * 生成项目ID
	 * @return
	 */
	public String generateProjectId();

	public Map<String, Object> getReadableColumns(User user, String taskId);

	/**
	 * 获取登陆人当前待办的任务
	 * @param activitiUserId 登陆人activiti身份ID
	 * @return
	 */
	public List<PmsProjectFlowResult> getTodoTasks(String activitiUserId);

}
