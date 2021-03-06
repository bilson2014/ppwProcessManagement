package com.paipianwang.activiti.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.paipianwang.activiti.service.MessageService;
import com.paipianwang.activiti.service.OnlineDocService;
import com.paipianwang.activiti.service.ProjectResourceService;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.enums.FileType;
import com.paipianwang.pat.common.util.DateUtils;
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

	@Autowired
	private MessageService messageService;
	
	@Autowired
	private OnlineDocService onlineDocService;

	@Override
	public String addResource(String resourceName, String taskId, String resourceType, MultipartFile file,
			SessionInfo sessionInfo, Integer flag) {

		String fileId = FastDFSClient.uploadFile(file);
		String name = file.getOriginalFilename();

		if (StringUtils.isNotBlank(fileId)) {
			// 添加流程文件资源
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			String processInstanceId = task.getProcessInstanceId();

			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(processInstanceId).singleResult();
			String projectId = processInstance.getBusinessKey();

			PmsProjectResource resource = new PmsProjectResource();
			final String taskName = task.getName();
			resource.setResourceName(resourceName + name.substring(name.lastIndexOf(".")));
			resource.setProjectId(projectId);// pmsProjectResource.getProjectId());
			resource.setResourcePath(fileId);
			resource.setResourceType(resourceType.substring(resourceType.indexOf("_") + 1));
			resource.setTaskName(taskName);// pmsProjectResource.getTaskName());//当前节点
			resource.setFlag(flag == null ? 0 : 1);

			// 当前登陆人
			resource.setUploaderId(sessionInfo.getActivitiUserId());
			List<String> groups = sessionInfo.getActivitGroups();
			String group = "";
			for (String gro : groups) {
				group = group + gro + ",";
			}
			resource.setUploaderGroup(group);// TODO 上传时身份
			resource.setUploaderName(sessionInfo.getRealName());

			long result = pmsProjectResourceFacade.insert(resource);
			// 根据ID查找 文件实体
			PmsProjectResource res = pmsProjectResourceFacade.getProjectResourceById(result);

			// 写入日志
			StringBuffer content = new StringBuffer();
			content.append("上传了 《");
			if(res != null) {
				String resName = res.getResourceName();
				content.append(resName.substring(0, resName.lastIndexOf(".")));
			} else {
				content.append(taskName + "-").append(resource.getResourceName());
			}
			content.append("》 文件");
			
			messageService.insertOperationLog(projectId, taskId, taskName, content.toString(), sessionInfo);
			
			if (result == -1) {
				return false + "";
			}
			// 转换文件
			onlineDocService.convertFile(res);
		}
		return true + "";

	}

	@Override
	public PmsProjectResource findIndentResource(long id) {
		return pmsProjectResourceFacade.getProjectResourceById(id);
	}

	@Override
	public List<PmsProjectResource> getResourceList(String projectId, List<String> roleTypes) {
		List<PmsProjectResource> resources = pmsProjectResourceFacade.getValidResourceByProjectIdAndRole(projectId,
				roleTypes);
		if(resources==null){
			return new ArrayList<PmsProjectResource>();
		}
		for (PmsProjectResource resource : resources) {
			// 资源类型
			for (FileType type : FileType.values()) {
				if (type.getId().equals(resource.getResourceType())) {
					resource.setResourceType(type.getText());
				}
			}
			// 时间格式
			if (resource.getCreateDate() != null) {
				resource.setCreateDate(
						DateUtils.getDateByFormat(resource.getCreateDate(), "yyyy-MM-dd HH:mm:ss").toString());
			}
		}
		return resources;
	}

	@Override
	public Map<String, List<PmsProjectResource>> getResourceListForVersion(String projectId, List<String> roleTypes) {
		Map<String, List<PmsProjectResource>> resources = pmsProjectResourceFacade
				.getResourceByProjectIdAndRole(projectId, roleTypes);
		Map<String, List<PmsProjectResource>> result = new HashMap<>();
		Iterator<String> iterator = resources.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			// 资源类型
			for (FileType type : FileType.values()) {
				if (type.getId().equals(key)) {
					result.put(type.getText(), resources.get(key));
					break;
				}
			}
			// 时间格式
			for (PmsProjectResource resource : resources.get(key)) {
				if (resource.getCreateDate() != null) {
					resource.setCreateDate(
							DateUtils.getDateByFormat(resource.getCreateDate(), "yyyy-MM-dd HH:mm:ss").toString());
				}
			}
		}

		return result;
	}

	/**
	 * 更新预览文件路径
	 */
	@Override
	public void updatePreview(PmsProjectResource pmsProjectResource) {
		pmsProjectResourceFacade.updateViewPath(pmsProjectResource.getPreviewPath(), pmsProjectResource.getProjectResourceId());
	}

}
