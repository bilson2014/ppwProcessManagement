package com.paipianwang.activiti.service;

public interface ProjectTaskService {

	/**
	 * 更新当前 Task 的代办人
	 * @param projectId
	 *            项目ID
	 */
	public void updateAssignerAtCurrentTask(final String projectId);

}
