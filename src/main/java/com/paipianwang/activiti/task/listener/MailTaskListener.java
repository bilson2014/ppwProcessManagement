package com.paipianwang.activiti.task.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

@Component
public class MailTaskListener implements TaskListener{

	private static final long serialVersionUID = -1392935563989562836L;

	@Override
	public void notify(DelegateTask delegateTask) {
		System.err.println(delegateTask.getName());
	}

}
