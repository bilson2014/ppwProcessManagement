package com.paipianwang.activiti.mq.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.stereotype.Component;

import com.paipianwang.activiti.service.ProjectTaskService;

/**
 * 项目协同人变更监听器
 * @author jacky
 *
 */
@Component
public class ProjectSynergyConsumerAwareMessageListener implements SessionAwareMessageListener<Message> {

	private static final Log logger = LogFactory.getLog(ProjectSynergyConsumerAwareMessageListener.class);

	@Autowired
	private ProjectTaskService taskService = null;

	@Override
	public void onMessage(Message message, Session session) throws JMSException {
		ActiveMQTextMessage msg = (ActiveMQTextMessage) message;
		final String projectId = msg.getText();
		logger.info("==>receive project synergy change message, projectId is " + projectId);

		try {
			if (StringUtils.isNotBlank(projectId)) {
				taskService.updateAssignerAtCurrentTask(projectId);
				logger.info("==>complete project synergy change message, projectId is " + projectId);
			} else {
				logger.error("ProjectSynergyConsumerAwareMessageListener error : projectId is null");
			}
			Thread.sleep(400);
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.error("ProjectSynergyConsumerAwareMessageListener error : " + e.getMessage());
		}
	}

}
