package com.paipianwang.activiti.task.java.service.delegate;

import java.io.Serializable;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import com.paipianwang.activiti.mq.email.service.BaseMQService;

/**
 * 向供应商发送项目启动函
 * @author jacky
 *
 */
public class SendProjectStartEmailToTeam implements JavaDelegate, Serializable {

	private static final long serialVersionUID = -7815232611890095087L;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		System.err.println("向供应商发送项目启动函");
		String projectId = execution.getProcessBusinessKey();
		
		ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		BaseMQService projectPlanStartMQService = (BaseMQService) context
				.getBean("projectPlanStartMQService");
		projectPlanStartMQService.sendMessage(projectId);
	}

}
