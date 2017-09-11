package com.paipianwang.activiti.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.form.TaskFormDataImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.paipianwang.activiti.domin.TaskVO;
import com.paipianwang.pat.common.entity.KeyValue;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.workflow.entity.PmsProjectFlowResult;
import com.paipianwang.pat.workflow.entity.PmsProjectSynergy;
import com.paipianwang.pat.workflow.entity.ProjectCycleItem;

public interface ProjectWorkFlowService {

	/**
	 * 流程启动
	 */
	public ProcessInstance startFormAndProcessInstance(String processDefinitionId, Map<String, String> formProperties,
			SessionInfo info, Map<String, Object> form);

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
	public List<PmsProjectFlowResult> getFinishedTask(String userId);

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
	 * @param userGroup 
	 * @param id
	 */
	public void completeTaskFromData(String taskId, Map<String, String> formProperties, String userId, List<String> userGroup);

	/**
	 * 生成项目ID
	 * @return
	 */
	public String generateProjectId();

	public Map<String, Object> getReadableColumns(String userId, String taskId, String projectId);

	/**
	 * 获取登陆人当前待办的任务
	 * @param activitiUserId 登陆人activiti身份ID
	 * @return
	 */
	public List<PmsProjectFlowResult> getTodoTasks(String activitiUserId);

	/**
	 * 挂起
	 * @param processInstanceId 流程ID
	 */
	public void suspendProcess(String processInstanceId, String projectId);

	/**
	 * 激活
	 * @param processInstanceId 流程ID
	 */
	public void activateProcess(String processInstanceId, String projectId);

	/**
	 * 查询挂起流程
	 * @param activitiUserId 当前登陆人ID
	 * @return
	 */
	public List<PmsProjectFlowResult> getSuspendTasks(String activitiUserId);
	
	/**
	 * 获取项目task节点周期
	 * @param taskId
	 * @return
	 */
	public ProjectCycleItem getCycleByTask(String taskId);
	
	/**
	 * 获取task预期结束时间
	 * @param taskId
	 * @return
	 */
	public Date getExpectDate(String taskId);

	public List<PmsProjectSynergy> getSynergy(String activitiUserId, String taskId, SessionInfo info);

	/**
	 * 获取当前节点所在阶段 以及 备注信息
	 * @param taskId
	 * @return
	 */
	public Map<String, String> getTaskStateAndDescription(String taskId);

	/**
	 * 根据角色获取协同人列表
	 * @param roleType
	 * @return
	 */
	public Map<String, String> getUserByRole(String roleType);

	public Map<String, Object> getProjectTaskList(String projectId, String taskStage);

	public Map<String, Object> getTaskInfo(String taskId);

	/**
	 * 获取可编辑的字段
	 * @param taskId
	 * @param projectId
	 * @param info
	 * @return
	 */
	public Map<String, Object> getEditParameter(String taskId, String projectId, String infoType, SessionInfo info);

	/**
	 * 修改项目信息、客户信息
	 * @param formProperties
	 */
	public void updateInformation(Map<String, String> formProperties, SessionInfo info);

	/**
	 * 修改供应商信息
	 * @param teamList
	 */
	public void updateTeamInformation(List<Map<String, Object>> teamList);
	
	/**
	 * 根据项目名称筛选
	 * @param flowName
	 * @param activitiUserId
	 * @return
	 */
	public List<TaskVO> getSearchTasks(String flowName, String activitiUserId);
	/**
	 * 根据阶段筛选进行中其他任务
	 * @param stage
	 * @param activitiUserId
	 * @param flag
	 * @return
	 */
	public List<TaskVO> getAgentTasksByStage(String stage, String activitiUserId,int flag);

	/**
	 * 获取可修改的文件列表
	 * @param info 身份验证
	 * @param taskId 当前taskId
	 * @param projectId 项目编号
	 * @return
	 */
	public List<KeyValue> getEditResourceList(SessionInfo info, String taskId, String projectId);

	/**
	 * 取消流程
	 * @param processInstanceId 实例ID
	 * @param projectId 项目流程ID
	 */
	public void cancelProcess(String processInstanceId, String projectId);

	/**
	 * 查询已取消的task
	 * @param activitiUserId 当前登陆人身份ID
	 * @return
	 */
	public List<PmsProjectFlowResult> getCancelTask(String activitiUserId);

	/**
	 * 根据阶段筛选进行中任务
	 * @param stage
	 * @param activitiUserId
	 * @param flag
	 * @return
	 */
	public List<TaskVO> getTasksByStage(String stage, String activitiUserId,int flag);

	/**
	 * 获取当前 task
	 * @return
	 */
	public Task getCurrentTaskName(final String taskId);
}
