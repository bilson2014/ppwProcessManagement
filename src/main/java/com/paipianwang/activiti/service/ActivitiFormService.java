package com.paipianwang.activiti.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.form.TaskFormDataImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

public interface ActivitiFormService {

	/**
	 * 流程启动
	 */
	public ProcessInstance startFormAndProcessInstance(String processDefinitionId, Map<String, String> formProperties,
			String userId);

	/**
	 * 获取当前登陆人正在进行中的项目
	 * 
	 * @param id
	 * @return
	 */
	public List<Task> getRunningTasks(String userId);

	/**
	 * 签收任务
	 * 
	 * @param id
	 * @param taskId
	 */
	public void claim(String userId, String taskId);

	/**
	 * 获取流程节点的表单
	 * 
	 * @param taskId
	 * @return
	 */
	public TaskFormDataImpl getTaskFormData(String taskId);

	/**
	 * 提交表单，完成节点任务
	 * 
	 * @param taskId
	 * @param formProperties
	 * @param id
	 */
	public void completeTaskFromData(String taskId, Map<String, String> formProperties, String id);

	/**
	 * 查询已完成任务
	 * @param id
	 * @return
	 */
	public List<HistoricProcessInstance> getFinishedTask(String id);

}
