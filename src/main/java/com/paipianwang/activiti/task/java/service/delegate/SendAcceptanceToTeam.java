package com.paipianwang.activiti.task.java.service.delegate;

import java.io.Serializable;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import com.paipianwang.activiti.mq.email.service.BaseMQService;
import com.paipianwang.activiti.service.MessageService;

/**
 * 向供应商发送验收函
 * @author jacky
 *
 */
public class SendAcceptanceToTeam implements JavaDelegate, Serializable {

	private static final long serialVersionUID = 5921505231144830853L;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		System.err.println("向供应商发送验收函");
		
		String projectId = execution.getProcessBusinessKey();
		ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		BaseMQService projectAcceptLetterMQService = (BaseMQService) context.getBean("projectAcceptLetterMQService");
		projectAcceptLetterMQService.sendMessage(projectId);
		//留言
		MessageService messageService=(MessageService) context.getBean("messageService");
		messageService.insertSystemMessage(projectId, "向供应商发送验收函");
	}

}
