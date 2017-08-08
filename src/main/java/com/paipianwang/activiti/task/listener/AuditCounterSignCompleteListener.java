package com.paipianwang.activiti.task.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

/**
 * 多人联合内审 监听器
 * @author jacky
 *
 */
@Component
public class AuditCounterSignCompleteListener implements TaskListener {

	private static final long serialVersionUID = 6164221672207305686L;

	@Override
	public void notify(DelegateTask delegateTask) {
		String approved = (String) delegateTask.getVariable("approved");
        if (approved.equals("true")) {
            Long agreeCounter = (Long) delegateTask.getVariable("approvedCounter");
            delegateTask.setVariable("approvedCounter", agreeCounter + 1);
        }

	}

}
