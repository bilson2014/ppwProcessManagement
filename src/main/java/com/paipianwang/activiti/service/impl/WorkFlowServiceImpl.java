package com.paipianwang.activiti.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paipianwang.activiti.service.WorkFlowService;
import com.paipianwang.pat.workflow.entity.PmsVacation;
import com.paipianwang.pat.workflow.entity.ProjectCycleItem;
import com.paipianwang.pat.workflow.facade.WorkFlowFacade;

@Service("workFlowFacade")
@Transactional
public class WorkFlowServiceImpl implements WorkFlowService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private WorkFlowFacade workFlowFacade = null;

	@Autowired
	private TaskService taskService = null;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private IdentityService identityService;

	@Autowired
	private HistoryService historyService;

	@Override
	public ProcessInstance startWorkflow(PmsVacation pmsVacation, Map<String, Object> variables) {
		Long businessKey = workFlowFacade.insert(pmsVacation);
		logger.debug("save PmsVacation : {}", pmsVacation);

		ProcessInstance processInstance = null;

		try {
			// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator�?
			identityService.setAuthenticatedUserId(pmsVacation.getUserId());
			processInstance = runtimeService.startProcessInstanceByKey("vacation", String.valueOf(businessKey),
					variables);
			String processInstanceId = processInstance.getId();
			logger.debug("start process of {key={}, bkey={}, pid={}, variables={}}",
					new Object[] { "leave", businessKey, processInstanceId, variables });
		} finally {
			identityService.setAuthenticatedUserId(null);
		}
		return processInstance;
	}

	/**
	 * 个人代办项目
	 */
	@Override
	public List<PmsVacation> findTodoTasks(String userId) {

		List<PmsVacation> list = new ArrayList<PmsVacation>();

		// 获取当前人的任务列表
		List<Task> tasks = taskService.createTaskQuery().processDefinitionKey("vacation")
				.taskCandidateOrAssigned(userId).active().list();
		if (!tasks.isEmpty()) {
			for (Task task : tasks) {
				String processInstanceId = task.getProcessInstanceId();
				ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
						.processInstanceId(processInstanceId).active().singleResult();
				String businessKey = processInstance.getBusinessKey();
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("id", businessKey);
				PmsVacation vacate = workFlowFacade.listBy(paramMap) == null ? null
						: workFlowFacade.listBy(paramMap).get(0);
				vacate.setProcessInstance(processInstance);
				vacate.setTask(task);
				list.add(vacate);
			}
		}
		return list;
	}

	/**
	 * 签收
	 */
	@Override
	public void claimTask(String taskId, String userId) {
		taskService.claim(taskId, userId);
	}

	/**
	 * 正在进行的项�?
	 */
	@Override
	public List<PmsVacation> findRunningProcessInstances(String userId) {

		List<PmsVacation> list = new ArrayList<PmsVacation>();

		List<HistoricProcessInstance> ins = historyService.createHistoricProcessInstanceQuery().involvedUser(userId)
				.orderByProcessInstanceId().desc().list();

		if (!ins.isEmpty()) {
			for (HistoricProcessInstance hpi : ins) {
				String processInstanceId = hpi.getId();
				ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
						.processInstanceId(processInstanceId).active().singleResult();
				if (processInstance != null) {
					String businessKey = processInstance.getBusinessKey();
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("id", businessKey);
					PmsVacation vacate = workFlowFacade.listBy(paramMap) == null ? null
							: workFlowFacade.listBy(paramMap).get(0);
					vacate.setProcessInstance(processInstance);
					vacate.setHpi(hpi);
					list.add(vacate);
				}
			}
		}
		return list;
	}

	/**
	 * 正在进行的项目
	 */
	@Override
	public List<PmsVacation> findFinishedProcessInstaces(String userId) {

		List<PmsVacation> list = new ArrayList<PmsVacation>();

		List<HistoricProcessInstance> ins = historyService.createHistoricProcessInstanceQuery().finished()
				.involvedUser(userId).orderByProcessInstanceId().desc().list();

		if (!ins.isEmpty()) {
			for (HistoricProcessInstance hpi : ins) {
				String businessKey = hpi.getBusinessKey();
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("id", businessKey);
				PmsVacation vacate = workFlowFacade.listBy(paramMap) == null ? null
						: workFlowFacade.listBy(paramMap).get(0);
				vacate.setHpi(hpi);
				list.add(vacate);
			}
		}
		return list;
	}

	/**
	 * 完成项目节点
	 */
	@Override
	public void complete(String taskId, Map<String, Object> variables) {
		taskService.complete(taskId, variables);
	}

	@Override
	public void setAssert(String taskId, String userId) {
		taskService.setAssignee(taskId, userId);
	}

	@Override
	public ProjectCycleItem getCycleByTask(String taskId) {
		return workFlowFacade.getCycleByTaskId(taskId);
	}

}
