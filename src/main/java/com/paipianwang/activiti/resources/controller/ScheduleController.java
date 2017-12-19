package com.paipianwang.activiti.resources.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.paipianwang.activiti.service.ScheduleService;
import com.paipianwang.pat.common.entity.PmsResult;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.workflow.entity.PmsProjectFlow;
import com.paipianwang.pat.workflow.entity.PmsSchedule;
import com.paipianwang.pat.workflow.facade.PmsProjectFlowFacade;
import com.paipianwang.pat.workflow.facade.PmsScheduleFacade;

/**
 * 排期表
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController extends BaseController {

	@Autowired
	private PmsScheduleFacade pmsScheduleFacade;
	@Autowired
	private PmsProjectFlowFacade pmsProjectFlowFacade;
	@Autowired
	private ScheduleService scheduleService;
	/**
	 * 排期表页面
	 * @param projectId
	 * @param model
	 * @return
	 */
	@RequestMapping("/info")
	public ModelAndView scheduleView(final String projectId,ModelMap model){
		List<String> metaData=new ArrayList<>();
		metaData.add("projectName");
		PmsProjectFlow flow=pmsProjectFlowFacade.getProjectFlowByProjectId(metaData, projectId);
		if(flow!=null){
			model.put("projectName", flow.getProjectName());
		}
		model.put("projectId", projectId);
		return new ModelAndView("activiti/schedule", model);
	}
	
	/**
	 * 获取项目排期表
	 * @param projectId
	 * @return
	 */
	@RequestMapping("/get/{projectId}")
	public PmsSchedule getByProjectId(@PathVariable("projectId")String projectId){
		return pmsScheduleFacade.getByProjectId(projectId);
	}
	
	/**
	 * 校验项目名称是否存在
	 * @param pmsSchedule
	 * @return
	 */
	@RequestMapping("/validate/project-name")
	public PmsResult validateProjectExist(@RequestBody final PmsSchedule pmsSchedule ){
		PmsResult result=new PmsResult();
		List<PmsProjectFlow> flows=pmsProjectFlowFacade.getByName(pmsSchedule.getProjectName());
		
		if(ValidateUtil.isValid(flows)){
			//存在
			return result;
		}
		//不存在
		result.setResult(false);
		return result;
	}
	
	/**
	 * 保存/更新排期表
	 * @param pmsSchedule
	 */
	@RequestMapping("/save")
	public PmsResult saveOrUpdateSchedule(@RequestBody final PmsSchedule pmsSchedule,HttpServletRequest request, final HttpServletResponse response){
		PmsResult result=new PmsResult();
		//数据保存
		result=scheduleService.saveOrUpdateSchedule(pmsSchedule);
		
		return result;
	}
	/**
	 * 导出
	 * @param scheduleId 排期表id
	 * @param request
	 * @param response
	 */
	@RequestMapping("/export/{scheduleId}")
	public void export(@PathVariable("scheduleId")Long scheduleId,HttpServletRequest request, final HttpServletResponse response){
		//导出
		OutputStream outputStream=null;
		PmsSchedule pmsSchedule = pmsScheduleFacade.getById(scheduleId);
		try {
			if(pmsSchedule!=null){
				response.setCharacterEncoding("utf-8");
				response.setContentType("application/octet-stream");
				String filename ="《"+ pmsSchedule.getProjectName()+"》排期表.pptx";
				
				//---处理文件名
				String userAgent = request.getHeader("User-Agent"); 
				//针对IE或者以IE为内核或Microsoft Edge的浏览器：
				if (userAgent.contains("MSIE")||userAgent.contains("Trident") ||userAgent.contains("Edge")) {
					filename = java.net.URLEncoder.encode(filename, "UTF-8");
				} else {
					filename = new String(filename.getBytes("UTF-8"),"ISO-8859-1");
				}
				
				response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"\r\n");
			
				outputStream = response.getOutputStream();
				
				scheduleService.export(pmsSchedule, outputStream,request);
				
				outputStream.flush();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//判断项目是否存在--不存在，则删除临时持久化数据
			if(pmsSchedule!=null && !ValidateUtil.isValid(pmsSchedule.getProjectId())){

			}
		}
		
	}
}
