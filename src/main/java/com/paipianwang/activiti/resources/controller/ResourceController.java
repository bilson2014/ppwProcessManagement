package com.paipianwang.activiti.resources.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.identity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.paipianwang.activiti.service.ProjectResourceService;
import com.paipianwang.pat.common.constant.PmsConstant;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.common.web.file.FastDFSClient;
import com.paipianwang.pat.workflow.entity.PmsProjectResource;
import com.paipianwang.pat.workflow.facade.PmsProjectResourceFacade;

@RestController
@RequestMapping("/resource")
public class ResourceController  extends BaseController{

	@Autowired
	private ProjectResourceService projectResourceService;
	@Autowired
	private PmsProjectResourceFacade pmsProjectResourceFacade;

	/**
	 * 上传文件资源
	 * 
	 * @param request
	 * @param response
	 * @param file
	 * @param pmsProjectResource
	 * @return
	 */
	@RequestMapping(value = "/addResource", method = RequestMethod.POST)
	public String addResource(final HttpServletRequest request, final HttpServletResponse response,
			final MultipartFile file,String resourceName,String taskId,String resourceType) {
		SessionInfo info = getCurrentInfo(request);
		return projectResourceService.addResource(resourceName,taskId,resourceType, file,info) + "";
	}

	/**
	 * 文件下载
	 * @param id
	 * @param response
	 * @param request
	 */
	@RequestMapping("/getDFSFile/{id}")
	public void getDFSFile(@PathVariable final long id, final HttpServletResponse response,
			final HttpServletRequest request) {
		PmsProjectResource pmsProjectResource = projectResourceService.findIndentResource(id);

		InputStream in = FastDFSClient.downloadFile(pmsProjectResource.getResourcePath());

		try {
			ServletOutputStream ouputStream = response.getOutputStream();
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/octet-stream");
			String filename = URLEncoder.encode(pmsProjectResource.getResourcePath(), "UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"\r\n");
			// send file
			saveTo(in, ouputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void saveTo(InputStream in, OutputStream out) throws Exception {
		byte[] data = new byte[1024];
		int index = 0;
		while ((index = in.read(data)) != -1) {
			out.write(data, 0, index);
		}
		out.flush();
		in.close();
		out.close();
	}
	
	/**
	 * 获取项目文件资源
	 * @param projectId
	 * @return
	 */
	@RequestMapping(value="/list/{projectId}")
	@ResponseBody
	public List<PmsProjectResource> getResourceList(@PathVariable String projectId,final HttpServletRequest request){
		//获取当前登陆用户项目角色
		List<String> groupList =((SessionInfo)request.getSession().getAttribute(PmsConstant.SESSION_INFO)).getActivitGroups();
		if(ValidateUtil.isValid(groupList)){
			return projectResourceService.getResourceList(projectId,groupList);
		}
		return new ArrayList<>();
	}
	
	/**
	 * 版本控制文件集合
	 * @param projectId
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/version/{projectId}")
	@ResponseBody
	public Map<String,List<PmsProjectResource>> getResourceForVersion(@PathVariable String projectId,final HttpServletRequest request){
		//获取当前登陆用户项目角色
		List<String> groupList =((SessionInfo)request.getSession().getAttribute(PmsConstant.SESSION_INFO)).getActivitGroups();
		if(ValidateUtil.isValid(groupList)){
			return projectResourceService.getResourceListForVersion(projectId,groupList);
		}
		List<String> roleTypes=new ArrayList<>();
		roleTypes.add("sale");
		return projectResourceService.getResourceListForVersion(projectId,roleTypes);
//		return new HashMap<>();
	}
	
	/**
	 * 文件版本设置
	 * @param projectResourceId
	 * @return
	 */
	@RequestMapping(value="/setValid/{projectResourceId}")
	public boolean setResourceValid(@PathVariable long projectResourceId){
		pmsProjectResourceFacade.updateValidFlag(projectResourceId);
		return true;
	}
}
