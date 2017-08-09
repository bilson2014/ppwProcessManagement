package com.paipianwang.activiti.mq.email.service;

public interface BaseMQService {

	// 发送邮件
	public void sendMessage(final String projectId);
}
