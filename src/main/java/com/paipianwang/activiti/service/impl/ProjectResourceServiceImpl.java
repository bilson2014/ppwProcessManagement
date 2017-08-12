package com.paipianwang.activiti.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.activiti.engine.identity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.paipianwang.activiti.service.ProjectResourceService;
import com.paipianwang.activiti.utils.UserUtil;
import com.paipianwang.pat.common.web.file.FastDFSClient;
import com.paipianwang.pat.workflow.entity.PmsProjectResource;
import com.paipianwang.pat.workflow.facade.PmsProjectResourceFacade;

@Service
public class ProjectResourceServiceImpl implements ProjectResourceService {

	@Autowired
	private PmsProjectResourceFacade pmsProjectResourceFacade;
	
	@Override
	public String addResource(PmsProjectResource pmsProjectResource, MultipartFile file, HttpSession httpSession) {

		String fileId = FastDFSClient.uploadFile(file);
		if (StringUtils.isNotBlank(fileId)) {
			// 添加系统评论

			// 添加流程文件资源
			PmsProjectResource resource=new PmsProjectResource();
			resource.setResourceName(pmsProjectResource.getResourceName());
			resource.setProjectId(pmsProjectResource.getProjectId());
			resource.setResourcePath(fileId);
			resource.setResourceType(pmsProjectResource.getResourceType());
			resource.setTaskName(pmsProjectResource.getTaskName());//当前节点
			resource.setFlag(0);

			//
			User user=UserUtil.getUserFromSession(httpSession);
			//当前登陆人
			resource.setUploaderId(Integer.parseInt(user.getId()));
//			resource.setUploaderGroup(httpSession.getAttribute(""));
			resource.setUploaderName(user.getLastName());
			
//			resource.setPreviewPath("");
//			resource.setVersion();

			pmsProjectResourceFacade.insert(resource);
			// 转换文件
//			onlineDocService.convertFile(resource);
		}
		return "true";

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
