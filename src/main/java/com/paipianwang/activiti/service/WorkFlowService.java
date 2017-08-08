package com.paipianwang.activiti.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;

import com.paipianwang.pat.workflow.entity.PmsVacation;

public interface WorkFlowService {

	public ProcessInstance startWorkflow(PmsVacation entity, Map<String, Object> variables);

	public List<PmsVacation> findTodoTasks(String userId);

	public void claimTask(String taskId, String userId);

	public List<PmsVacation> findRunningProcessInstances(String userId);

	public List<PmsVacation> findFinishedProcessInstaces(String userId);

	public void complete(String taskId, Map<String, Object> variables);

	public void setAssert(String taskId, String string);
}
