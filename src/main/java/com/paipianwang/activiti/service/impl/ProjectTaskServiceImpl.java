package com.paipianwang.activiti.service.impl;

import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paipianwang.activiti.service.ProjectTaskService;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.workflow.entity.PmsProjectFlow;
import com.paipianwang.pat.workflow.entity.PmsProjectSynergy;
import com.paipianwang.pat.workflow.entity.ProjectCycleItem;
import com.paipianwang.pat.workflow.facade.PmsProjectFlowFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectSynergyFacade;
import com.paipianwang.pat.workflow.facade.WorkFlowFacade;

@Service
public class ProjectTaskServiceImpl implements ProjectTaskService {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private final PmsProjectFlowFacade facade = null;

	@Autowired
	private final PmsProjectSynergyFacade synergyFacade = null;

	@Autowired
	private WorkFlowFacade workFlowFacade = null;
	
	@Autowired
	private final TaskService taskService = null;

	@Autowired
	private final IdentityService indentityService = null;

	@Override
	public void updateAssignerAtCurrentTask(String projectId) {
		if (StringUtils.isNotBlank(projectId)) {
			PmsProjectFlow flow = facade.getProjectFlowByProjectId(projectId);
			if (flow != null) {
				final String proInstanceId = flow.getProcessInstanceId();
				if (StringUtils.isNotBlank(proInstanceId)) {
					List<Task> tasks = taskService.createTaskQuery().processInstanceId(proInstanceId).list();
					if (ValidateUtil.isValid(tasks)) {
						for (final Task task : tasks) {
							// 获取当前 task
							final String taskDefinitionKey = task.getTaskDefinitionKey();
							final String taskName = task.getName();
							
							// 获取当前阶段的 执行的角色
							ProjectCycleItem cycleItem = workFlowFacade.getCycleByTaskId(taskDefinitionKey);
							String groups = cycleItem.getGroups();
							// 当前代办人
							final String assignee = task.getAssignee();
							if (taskDefinitionKey.equals("auditTotalTask")) {
								// 联合内审
								// 查询 当前代办人的 身份
								Group group = indentityService.createGroupQuery().groupMember(assignee).singleResult();
								groups = group.getId();
							}

							if (StringUtils.isNotBlank(groups)) {
								if (StringUtils.isNotBlank(assignee)) {
									// 查找协同人
									List<PmsProjectSynergy> synergys = synergyFacade.getSynergys(projectId, groups);
									if (ValidateUtil.isValid(synergys)) {
										PmsProjectSynergy synergy = synergys.get(0);
										String synergyId = "employee_" + synergy.getEmployeeId();
										if (!assignee.equals(synergyId)) {
											task.setAssignee(synergyId);
											logger.info("projectId is " + projectId + " , taskName is " + taskName + " , change assignee 【" + assignee + "】 to " + synergyId);
										}
									}
								}
							}
						}
					}
				}
			}
		}

	}

}
