package com.paipianwang.activiti.task.java.service.delegate;

import java.io.Serializable;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import com.paipianwang.activiti.mq.email.service.BaseMQService;
import com.paipianwang.activiti.service.MessageService;

/**
 * 向客户发送项目启动函
 * @author jacky
 *
 */
public class SendProjectStartEmailToUser implements JavaDelegate, Serializable {

	private static final long serialVersionUID = 4146202871425208996L;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		System.err.println("向客户发送项目启动函");
		
		String projectId = execution.getProcessBusinessKey();
		ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		BaseMQService projectInfoLetterMQService = (BaseMQService) context.getBean("projectInfoLetterMQService");
		projectInfoLetterMQService.sendMessage(projectId);
		
		//留言
		MessageService messageService=(MessageService) context.getBean("messageService");
		messageService.insertSystemMessage(projectId, "向客户发送项目启动函");
	}

}
