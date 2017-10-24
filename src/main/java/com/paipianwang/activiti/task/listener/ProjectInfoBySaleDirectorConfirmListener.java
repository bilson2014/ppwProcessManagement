package com.paipianwang.activiti.task.listener;

import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;

import com.paipianwang.activiti.service.MessageService;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.workflow.entity.PmsProjectSynergy;
import com.paipianwang.pat.workflow.facade.PmsProjectSynergyFacade;

/**
 * 【销售总监】确认项目信息（通过/驳回） 写入 Log 日志
 * 
 * @author jacky
 *
 */
@Component("projectInfoBySaleDirectorConfirmListener")
public class ProjectInfoBySaleDirectorConfirmListener extends BaseTaskListener  {

	private static final long serialVersionUID = 6209421499655450519L;

	@Override
	public void execute(DelegateTask delegateTask) {
		String confirm = (String) delegateTask.getVariable("condition_projectmarketPass");
		String group = "saleDirector";
		
		final String taskId = delegateTask.getId();
		final String taskName = delegateTask.getName();
		final String projectId = delegateTask.getExecution().getProcessBusinessKey();
		
		// 查询执行人的角色组以及真实姓名
		ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		PmsProjectSynergyFacade pmsProjectSynergyFacade = (PmsProjectSynergyFacade) context.getBean("pmsProjectSynergyFacade");
		MessageService messageService = (MessageService) context.getBean("messageService");

		List<PmsProjectSynergy> synergys = pmsProjectSynergyFacade.getSynergys(projectId, group);
		
		String content = "";
		if ("true".equals(confirm)) {
			content = "通过了 项目信息";
		} else if ("false".equals(confirm)) {
			content = "驳回 项目信息";
		}
		
		if(ValidateUtil.isValid(synergys)) {
			PmsProjectSynergy synergy = synergys.get(0);
			messageService.insertGageWayOperationLog(projectId, taskId, taskName, content, synergy);
		}
	}

}
