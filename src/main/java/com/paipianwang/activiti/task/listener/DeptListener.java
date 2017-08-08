package com.paipianwang.activiti.task.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class DeptListener implements TaskListener {

	private static final long serialVersionUID = 7875713849576257379L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		delegateTask.setAssignee("leaderuser");
	}

}
