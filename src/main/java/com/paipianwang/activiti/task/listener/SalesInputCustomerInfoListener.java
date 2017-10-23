package com.paipianwang.activiti.task.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

/**
 * 【销售】填写客户预算信息
 * @author jacky
 *
 */
@Component("salesInputCustomerInfoListener")
public class SalesInputCustomerInfoListener implements TaskListener {

	private static final long serialVersionUID = 8740846913128457528L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// nothing to do
		
	}

}
