package com.paipianwang.activiti.task.java.service.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import com.paipianwang.activiti.mq.email.service.BaseMQService;
import com.paipianwang.pat.workflow.entity.PmsProjectMessage;
import com.paipianwang.pat.workflow.facade.PmsProjectMessageFacade;

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
		
		//留言
				PmsProjectMessageFacade pmsProjectMessageFacade = (PmsProjectMessageFacade) context.getBean("pmsProjectMessageFacade");
				PmsProjectMessage message=new PmsProjectMessage();
				message.setFromId("system");
				message.setProjectId(projectId);
				
				
				message.setContent("向客户发送验收函");
				pmsProjectMessageFacade.insert(message);
	}

}
