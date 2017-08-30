package com.paipianwang.activiti.domin;

import java.io.Serializable;
import java.util.Date;

public class TaskVO implements Serializable{
	private static final long serialVersionUID = 7385263203872558671L;
	
	private String processInstanceId;
	private String processDefinitionId;
	
	//是否是负责人 1-是 0-不是
	private int isPrincipal;
	//task阶段
	private String taskStage;
	//task描述
	private String taskDescription;
	//是否是代办任务 1-是
	private String agent;	
	
	//task
	private String taskId;
	private String taskName;
	private String taskStatus;//任务状态：suspend-挂起 running-进行中 completed-已完成
	private String assignee;
	private Date dueDate;//预期完成时间
	private Date createTime;
		
	//父流程
	private String projectId;
	private String projectName;
	private String principalName;//负责人名称
	private Date suspendDate;//暂停日期
	private Date finishedDate;//完成日期
	
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}
	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}
	public int getIsPrincipal() {
		return isPrincipal;
	}
	public void setIsPrincipal(int isPrincipal) {
		this.isPrincipal = isPrincipal;
	}
	public String getTaskStage() {
		return taskStage;
	}
	public void setTaskStage(String taskStage) {
		this.taskStage = taskStage;
	}
	public String getTaskDescription() {
		return taskDescription;
	}
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getPrincipalName() {
		return principalName;
	}
	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}
	public Date getSuspendDate() {
		return suspendDate;
	}
	public void setSuspendDate(Date suspendDate) {
		this.suspendDate = suspendDate;
	}
	public Date getFinishedDate() {
		return finishedDate;
	}
	public void setFinishedDate(Date finishedDate) {
		this.finishedDate = finishedDate;
	}
}
