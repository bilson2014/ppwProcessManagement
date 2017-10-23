package com.paipianwang.activiti.task.java.service.delegate;

import java.io.Serializable;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import com.paipianwang.activiti.mq.email.service.BaseMQService;
import com.paipianwang.activiti.service.MessageService;

/**
 * 向监制发送通知邮件
 * @author jacky
 *
 */
public class SendMailToScheme implements JavaDelegate, Serializable {

	private static final long serialVersionUID = -3120317489553962672L;

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		System.out.println("向监制发送通知邮件");
		String projectId = execution.getProcessBusinessKey();
		ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		BaseMQService emailInformationMQService = (BaseMQService) context.getBean("emailInformationMQService");
		emailInformationMQService.sendMessage(projectId);
		
		//留言
		MessageService messageService=(MessageService) context.getBean("messageService");
		messageService.insertSystemMessage(projectId, "向监制发送通知邮件");
	}

}
