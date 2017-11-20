package com.paipianwang.activiti.task.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.springframework.stereotype.Component;

/**
 * 【销售】确认成片无误
 */
@Component("confirmFinalFilmTaskListener")
public class ConfirmFinalFilmTaskListener extends BaseTaskListener {
	private static final long serialVersionUID = -632584145046912537L;

	@Override
	public void execute(DelegateTask delegateTask) {

	}

}
