package com.paipianwang.activiti.service.impl;

import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.paipianwang.activiti.service.ProjectResourceService;
import com.paipianwang.activiti.utils.UserUtil;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.web.file.FastDFSClient;
import com.paipianwang.pat.workflow.entity.PmsProjectResource;
import com.paipianwang.pat.workflow.facade.PmsProjectResourceFacade;

@Service
public class ProjectResourceServiceImpl implements ProjectResourceService {

	@Autowired
	private PmsProjectResourceFacade pmsProjectResourceFacade;
	@Autowired
	private TaskService taskService = null;
	@Autowired
	private RuntimeService runtimeService = null;
	
	@Override
	public String addResource(String resourceName, String taskId, String resourceType, MultipartFile file, SessionInfo sessionInfo) {

		String fileId = FastDFSClient.uploadFile(file);
		if (StringUtils.isNotBlank(fileId)) {
			// 添加系统评论

			// 添加流程文件资源
			
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			String processInstanceId = task.getProcessInstanceId();

			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(processInstanceId).singleResult();
			String projectId = processInstance.getBusinessKey();
			
			PmsProjectResource resource=new PmsProjectResource();
			resource.setResourceName(resourceName);
			resource.setProjectId(projectId);//pmsProjectResource.getProjectId());
			resource.setResourcePath(fileId);
			resource.setResourceType(resourceType.substring(resourceType.indexOf("_")+1));
			resource.setTaskName(task.getName());//pmsProjectResource.getTaskName());//当前节点
			resource.setFlag(0);

			//当前登陆人
			resource.setUploaderId(sessionInfo.getActivitiUserId());
			List<String> groups=sessionInfo.getActivitGroups();
			String group="";
			for(String gro:groups){
				group=group+gro+",";
			}
			resource.setUploaderGroup(group);
			resource.setUploaderName(sessionInfo.getRealName());
			
//			resource.setPreviewPath("");
//			resource.setVersion();

			pmsProjectResourceFacade.insert(resource);
			// 转换文件
//			onlineDocService.convertFile(resource);
		}
		return true+"";

	}

	@Override
	public PmsProjectResource findIndentResource(long id) {
		return pmsProjectResourceFacade.getProjectResourceById(id);
	}

	@Override
	public List<PmsProjectResource> getResourceList(String projectId, List<String> roleTypes) {
		return pmsProjectResourceFacade.getValidResourceByProjectIdAndRole(projectId, roleTypes);
	}

	@Override
	public Map<String, List<PmsProjectResource>> getResourceListForVersion(String projectId, List<String> roleTypes) {
		return pmsProjectResourceFacade.getResourceByProjectIdAndRole(projectId, roleTypes);
	}

}
