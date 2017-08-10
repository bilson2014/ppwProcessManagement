package com.paipianwang.activiti.task.java.service.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import com.paipianwang.activiti.mq.email.service.BaseMQService;

/**
 * 向客户发送项目确认函
 * 
 * @author jacky
 *
 */
public class SendProjectEmailToUser implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		System.err.println("向客户发送项目确认函");

		String projectId = execution.getProcessBusinessKey();
		ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		BaseMQService projectConfirmStartMQService = (BaseMQService) context.getBean("projectConfirmStartMQService");
		projectConfirmStartMQService.sendMessage(projectId);
	}

}
