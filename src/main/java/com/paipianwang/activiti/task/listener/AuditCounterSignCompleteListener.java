package com.paipianwang.activiti.task.listener;

import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.identity.Group;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;

import com.paipianwang.activiti.service.MessageService;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.workflow.entity.PmsProjectSynergy;
import com.paipianwang.pat.workflow.facade.PmsProjectSynergyFacade;

/**
 * 多人联合内审 监听器
 * @author jacky
 *
 */
@Component
public class AuditCounterSignCompleteListener extends BaseTaskListener {

	private static final long serialVersionUID = 6164221672207305686L;

	@Override
	public void execute(DelegateTask delegateTask) {
		// 审核判断
		String approved = (String) delegateTask.getVariable("approved");
        if (approved.equals("true")) {
            Long agreeCounter = (Long) delegateTask.getVariable("approvedCounter");
            delegateTask.setVariable("approvedCounter", agreeCounter + 1);
        }

        // 添加系统日志
		final String taskId = delegateTask.getId();
		final String taskName = delegateTask.getName();
		final String projectId = delegateTask.getExecution().getProcessBusinessKey();
		
		// 办理人
		final String assignee = delegateTask.getAssignee();
		IdentityService identityService = delegateTask.getExecution().getEngineServices().getIdentityService();
		
		// 查询办理人身份
		Group group = identityService.createGroupQuery().groupMember(assignee).singleResult();
		
		// 查询执行人的角色组以及真实姓名
		ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		PmsProjectSynergyFacade pmsProjectSynergyFacade = (PmsProjectSynergyFacade) context.getBean("pmsProjectSynergyFacade");
		MessageService messageService = (MessageService) context.getBean("messageService");

		List<PmsProjectSynergy> synergys = pmsProjectSynergyFacade.getSynergys(projectId, group.getId());
		
		String content = "";
		if ("true".equals(approved)) {
			content = "通过了 联合内审";
		} else if ("false".equals(approved)) {
			content = "驳回了 联合内审";
		}
		
		if(ValidateUtil.isValid(synergys)) {
			PmsProjectSynergy synergy = synergys.get(0);
			messageService.insertGageWayOperationLog(projectId, taskId, taskName, content, synergy);
		}
	}

}
