package com.paipianwang.activiti.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

import com.paipianwang.pat.workflow.entity.PmsProjectResource;

public interface ProjectResourceService {

	String addResource(PmsProjectResource pmsProjectResource, MultipartFile file, HttpSession httpSession);

	PmsProjectResource findIndentResource(long id);

	List<PmsProjectResource> getResourceList(String projectId, List<String> roleTypes);

	Map<String, List<PmsProjectResource>> getResourceListForVersion(String projectId, List<String> roleTypes);

}
