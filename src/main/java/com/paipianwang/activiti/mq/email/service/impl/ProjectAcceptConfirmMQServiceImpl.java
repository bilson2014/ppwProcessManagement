package com.paipianwang.activiti.mq.email.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.paipianwang.activiti.mq.email.service.BaseMQService;

/**
 * 项目验收确认函
 * @author jacky
 *
 */
@Service("projectAcceptConfirmMQService")
public class ProjectAcceptConfirmMQServiceImpl implements BaseMQService {

	@Autowired
	private final JmsTemplate projectAcceptConfirmTemplate = null;
	
	@Override
	public void sendMessage(String projectId) {
		projectAcceptConfirmTemplate.send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				final Map<String,Object> resultMap = new HashMap<String,Object>();
				resultMap.put("projectId", projectId);
				return session.createTextMessage(JSON.toJSONString(resultMap));
			}
		});

	}

}
