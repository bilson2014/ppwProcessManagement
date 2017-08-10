package com.paipianwang.activiti.task.java.service.delegate;

import java.io.Serializable;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import com.paipianwang.activiti.mq.email.service.BaseMQService;

/**
 * 向供应商发送修改表
 * 
 * @author jacky
 *
 */
public class SendSuggestionEmailToTeam implements JavaDelegate, Serializable {

	private static final long serialVersionUID = -7067696742401702796L;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		System.err.println("向供应商发送修改表");

		String projectId = execution.getProcessBusinessKey();
		ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		BaseMQService projectSampleMideaMQService = (BaseMQService) context.getBean("projectSampleMideaMQService");
		projectSampleMideaMQService.sendMessage(projectId);
	}

}
