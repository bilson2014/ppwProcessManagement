package com.paipianwang.activiti.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.workflow.entity.PmsProjectResource;

public interface ProjectResourceService {

	PmsProjectResource findIndentResource(long id);

	List<PmsProjectResource> getResourceList(String projectId, List<String> roleTypes);

	Map<String, List<PmsProjectResource>> getResourceListForVersion(String projectId, List<String> roleTypes);

	String addResource(String resourceName, String taskId, String resourceType, MultipartFile file, SessionInfo info,
			Integer flag);

	void updatePreview(PmsProjectResource pmsProjectResource);

}
