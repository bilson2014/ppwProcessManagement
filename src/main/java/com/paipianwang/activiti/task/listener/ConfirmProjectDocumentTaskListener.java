package com.paipianwang.activiti.task.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.springframework.stereotype.Component;

/**
 * 【供应商管家】工程文件审核
 */
@Component("confirmProjectDocumentTaskListener")
public class ConfirmProjectDocumentTaskListener extends BaseTaskListener {
	private static final long serialVersionUID = 76556537759654264L;

	@Override
	public void execute(DelegateTask delegateTask) {

	}

}
