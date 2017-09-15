package com.paipianwang.activiti.service;

import java.util.List;

import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.workflow.entity.PmsProjectMessage;

public interface MessageService {

	List<PmsProjectMessage> getDefaultProjectMessage(String projectId);

	List<PmsProjectMessage> getAllProjectMessage(String projectId);

	List<PmsProjectMessage> getProjectTaskMessage(String taskId);
	
	void insertSystemMessage(String projectId,String content);
	
	void insertOperationLog(String projectId,String taskId,String taskName,String content,SessionInfo info);
	
	void insertDetailOperationLog(String projectId,String taskId,String taskName,String content,String fromId,String fromName, List<String> activitiGroup);

}
