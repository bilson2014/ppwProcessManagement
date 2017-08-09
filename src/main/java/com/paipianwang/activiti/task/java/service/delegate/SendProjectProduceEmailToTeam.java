package com.paipianwang.activiti.task.java.service.delegate;

import java.io.Serializable;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * 向供应商发送项目制作启动函
 * @author jacky
 *
 */
public class SendProjectProduceEmailToTeam implements JavaDelegate, Serializable {

	private static final long serialVersionUID = -7679118262039596764L;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.err.println("向供应商发送项目制作启动函");
	}

}
