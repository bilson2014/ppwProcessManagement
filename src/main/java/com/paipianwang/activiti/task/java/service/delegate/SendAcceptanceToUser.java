package com.paipianwang.activiti.task.java.service.delegate;

import java.io.Serializable;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * 向客户发送验收函
 * @author jacky
 *
 */
public class SendAcceptanceToUser implements JavaDelegate, Serializable {

	private static final long serialVersionUID = 5921505231144830853L;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.err.println("向客户发送验收函");
	}

}
