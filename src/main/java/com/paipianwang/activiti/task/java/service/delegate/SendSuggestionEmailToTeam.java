package com.paipianwang.activiti.task.java.service.delegate;

import java.io.Serializable;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * 向供应商发送修改表
 * @author jacky
 *
 */
public class SendSuggestionEmailToTeam implements JavaDelegate, Serializable {

	private static final long serialVersionUID = -7067696742401702796L;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.err.println("向供应商发送修改表");
	}

}
