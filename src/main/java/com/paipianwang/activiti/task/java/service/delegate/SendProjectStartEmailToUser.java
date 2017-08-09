package com.paipianwang.activiti.task.java.service.delegate;

import java.io.Serializable;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * 向客户发送项目启动函
 * @author jacky
 *
 */
public class SendProjectStartEmailToUser implements JavaDelegate, Serializable {

	private static final long serialVersionUID = 4146202871425208996L;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.err.println("向客户发送项目启动函");
	}

}
