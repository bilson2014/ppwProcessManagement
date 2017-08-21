package com.paipianwang.activiti.task.java.service.delegate;

import java.io.Serializable;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import com.paipianwang.activiti.mq.email.service.BaseMQService;
import com.paipianwang.pat.workflow.entity.PmsProjectMessage;
import com.paipianwang.pat.workflow.facade.PmsProjectMessageFacade;

/**
 * 向供应商发送项目制作启动函
 * 
 * @author jacky
 *
 */
public class SendProjectProduceEmailToTeam implements JavaDelegate, Serializable {

	private static final long serialVersionUID = -7679118262039596764L;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		System.err.println("向供应商发送项目制作启动函");
		
		String projectId = execution.getProcessBusinessKey();
		ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		BaseMQService projectProductStartMQService = (BaseMQService) context.getBean("projectProductStartMQService");
		projectProductStartMQService.sendMessage(projectId);
		
		//留言
				PmsProjectMessageFacade pmsProjectMessageFacade = (PmsProjectMessageFacade) context.getBean("pmsProjectMessageFacade");
				PmsProjectMessage message=new PmsProjectMessage();
				message.setFromId("system");
				message.setFromGroup("system");
				message.setProjectId(projectId);
				
				
				message.setContent("向供应商发送项目制作启动函");
				pmsProjectMessageFacade.insert(message);
	}

}
