package com.paipianwang.activiti.service;

import java.util.List;

import com.paipianwang.pat.workflow.entity.PmsProjectMessage;

public interface MessageService {

	List<PmsProjectMessage> getDefaultProjectMessage(String projectId);

	List<PmsProjectMessage> getAllProjectMessage(String projectId);

	List<PmsProjectMessage> getProjectTaskMessage(String projectId, String taskName);

}
