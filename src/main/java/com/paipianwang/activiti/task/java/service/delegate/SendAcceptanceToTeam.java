package com.paipianwang.activiti.task.java.service.delegate;

import java.io.Serializable;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import com.paipianwang.activiti.mq.email.service.BaseMQService;
import com.paipianwang.pat.workflow.entity.PmsProjectMessage;
import com.paipianwang.pat.workflow.facade.PmsProjectMessageFacade;

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
		PmsProjectMessageFacade pmsProjectMessageFacade = (PmsProjectMessageFacade) context.getBean("pmsProjectMessageFacade");
		PmsProjectMessage message=new PmsProjectMessage();
		message.setFromId("system");
		message.setProjectId(projectId);
		
		
		message.setContent("向供应商发送验收函");
		pmsProjectMessageFacade.insert(message);
	}

}
