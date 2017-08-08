package com.paipianwang.activiti.task.java.service;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.paipianwang.pat.workflow.facade.WorkFlowFacade;

@Component
public class ServiceTask implements JavaDelegate{

	@Autowired
	private WorkFlowFacade workFlowFacade = null;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		System.err.println("Hello");
		System.err.println(workFlowFacade);
	}

}
