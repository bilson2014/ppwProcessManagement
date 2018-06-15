package com.paipianwang.activiti.resources.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.paipianwang.pat.common.entity.PmsResult;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.DateUtils;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.common.web.file.FastDFSClient;
import com.paipianwang.pat.common.web.file.utils.GenerateFileByTemplate;
import com.paipianwang.pat.workflow.entity.PmsContinuity;
import com.paipianwang.pat.workflow.entity.PmsProjectFlow;
import com.paipianwang.pat.workflow.enums.ContinuityPictureRatio;
import com.paipianwang.pat.workflow.enums.ContinuityScriptType;
import com.paipianwang.pat.workflow.enums.ContinuityTimeLength;
import com.paipianwang.pat.workflow.enums.ContinuityVideoStyle;
import com.paipianwang.pat.workflow.facade.PmsContinuityFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectFlowFacade;


/**
 * 分镜头脚本
 * 
 * @author rui
 *
 */
@RestController
@RequestMapping("/continuity")
public class ContinuityController extends BaseController {

	@Autowired
	private PmsContinuityFacade pmsContinuityFacade;
	@Autowired
	private PmsProjectFlowFacade pmsProjectFlowFacade;

	/**
	 * 分镜头脚本制作主页
	 * 
	 * @param projectId
	 * @param model
	 * @return
	 */
	@RequestMapping("/info")
	public ModelAndView quotationView(final String projectId, ModelMap model) {
		model.put("projectId", projectId);
		return new ModelAndView("/flow/storyBoard", model);
	}

	/**
	 * 获取用户参与过的进行中的项目分镜头脚本
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/list/synergetic")
	public List<PmsContinuity> getSynergeticContinuity(final HttpServletRequest request) {
		SessionInfo info = getCurrentInfo(request);
		return pmsContinuityFacade.getSynergeticContinuityProject(info.getReqiureId());
	}

	/**
	 * 获取项目分镜头脚本 TODO 修改此种类型为PmsResult封装
	 * 
	 * @param projectId
	 * @return
	 */
	@RequestMapping("/get/{projectId}")
	public PmsContinuity getByProjectId(@PathVariable("projectId") String projectId) {
		return pmsContinuityFacade.getByProjectId(projectId);
	}
	
	/**
	 * 校验项目分镜脚本是否存在
	 * @param projectId
	 * @return
	 */
	@RequestMapping("/validate/{projectId}")
	public PmsResult validateByProjectId(@PathVariable("projectId") String projectId) {
		long count=pmsContinuityFacade.countByProjectId(projectId);
		PmsResult pmsResult=new PmsResult();
		if(count>0) {
			//项目已存在分镜脚本
			pmsResult.setResult(false);
		}
		return pmsResult;
	}

	/**
	 * 保存/更新分镜头脚本
	 * 
	 * @param pmsContinuity
	 */
	@RequestMapping("/save")
	public PmsResult saveOrUpdateQuotation(@RequestBody final PmsContinuity pmsContinuity,
			final HttpServletRequest request, final HttpServletResponse response) {
		PmsResult result = new PmsResult();
		// 数据保存
		if (pmsContinuity.getId() != null) {
			result = pmsContinuityFacade.update(pmsContinuity);
		} else {
			PmsContinuity old=pmsContinuityFacade.getByProjectId(pmsContinuity.getProjectId());
			if(old!=null) {
				//覆盖
				pmsContinuity.setId(old.getId());
				result = pmsContinuityFacade.update(pmsContinuity);
				//old 图片删除
				if(ValidateUtil.isValid(old.getScripts())) {
					for(PmsContinuity.ShootingScript script:old.getScripts()) {
						if(ValidateUtil.isValid(script.getPicture())) {
							FastDFSClient.deleteFile(script.getPicture());
						}
					}
				}
				
			}else {
				List<String> metaData = new ArrayList<>();
				metaData.add("projectName");
				Map<String, Object> projectName = pmsProjectFlowFacade.getProjectFlowColumnByProjectId(metaData,
						pmsContinuity.getProjectId());
				if (ValidateUtil.isValid(projectName)) {
					pmsContinuity.setProjectName(projectName.get("projectName") + "");
				}

				result = pmsContinuityFacade.insert(pmsContinuity);
			}
		}
		//删除图片
		String delImgs=pmsContinuity.getDelImgs();
		if(ValidateUtil.isValid(delImgs)) {
			String[] delImgList=delImgs.split(";");
			for(String delImg:delImgList) {
				FastDFSClient.deleteFile(delImg);
			}
		}

		return result;
	}

	/**
	 * 导出
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/export")
	public void export(final PmsContinuity pmsContinuity, HttpServletRequest request,
			final HttpServletResponse response) {	
		OutputStream outputStream = null;
		try {
			if (pmsContinuity != null) {
				// 组装数据
				Map<String, Object> data = new HashMap<String, Object>();
				initData(pmsContinuity, data);

				response.setCharacterEncoding("utf-8");
				response.setContentType("application/octet-stream");

				String filename = "《" + data.get("videoTypeName") + "》分镜脚本.pdf";

				// ---处理文件名
				String userAgent = request.getHeader("User-Agent");
				// 针对IE或者以IE为内核或Microsoft Edge的浏览器：
				if (userAgent.contains("MSIE") || userAgent.contains("Trident") || userAgent.contains("Edge")) {
					filename = java.net.URLEncoder.encode(filename, "UTF-8");
				} else {
					filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
				}
				response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"\r\n");
				
				//导出
				outputStream = response.getOutputStream();
				GenerateFileByTemplate.generatePdf(data, "continuityTemplate.ftl", outputStream);
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
			
		}
	}
	
	private void initData(PmsContinuity pmsContinuity, Map<String, Object> data) {
		// 项目信息
		if (ValidateUtil.isValid(pmsContinuity.getProjectId())) {
			List<String> metaData = new ArrayList<>();
			metaData.add("projectName");
			PmsProjectFlow flow = pmsProjectFlowFacade.getProjectFlowByProjectId(metaData,
					pmsContinuity.getProjectId());
			if (flow != null) {
				pmsContinuity.setProjectName(flow.getProjectName());
			}
		}

		// 第一页
		data.put("videoTypeName",pmsContinuity.getProjectName());
		data.put("projectId", ValidateUtil.isValid(pmsContinuity.getProjectId())?"项目ID："+pmsContinuity.getProjectId():"");	
		data.put("year", DateUtils.getYear(null)+"");
		data.put("month", DateUtils.getMonth(null)+"");
		data.put("day", DateUtils.getDay(null)+"");
		data.put("week", DateUtils.getWeekDay(null)+"");

		// 表格
//		data.put("name", pmsContinuity.getName());
		
		ContinuityPictureRatio ratio=ContinuityPictureRatio.getEnum(pmsContinuity.getPictureRatio());
		data.put("pictureRatioName", ratio==null?"":ratio.getRatio());
		data.put("resolutionName", ratio==null?"":ratio.getResolution());
		
		data.put("dimensionValue", pmsContinuity.getDimensionId()==null?"":pmsContinuity.getDimensionId()+"秒");		

		Date createTime=null;
		if(ValidateUtil.isValid(pmsContinuity.getCreateTime())) {
			createTime=DateUtils.getDateByFormat(pmsContinuity.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
		}
		data.put("cyear", DateUtils.getYear(createTime)+"");
		data.put("cmonth", DateUtils.getMonth(createTime)+"");
		data.put("cday",  DateUtils.getDay(createTime)+"");

		// 分镜
		List<Map<String, Object>> questions = new ArrayList<>();
		data.put("scripts", questions);

		if (!ValidateUtil.isValid(pmsContinuity.getScriptContent())) {
			throw new RuntimeException("分镜脚本不存在");
		}
		pmsContinuity.scriptToEntity();

		for (int i = 0; i < pmsContinuity.getScripts().size(); i++) {
			PmsContinuity.ShootingScript script = pmsContinuity.getScripts().get(i);
			Map<String, Object> item = new HashMap<>();
			item.put("num", i + 1);
			item.put("title",script.getType()==null?"": ContinuityScriptType.getEnum(script.getType()).getText());
			item.put("des", script.getDescription());
			
			item.put("img",ValidateUtil.isValid(script.getPicture())?GenerateFileByTemplate.getFullImgPath(script.getPicture()):"defaultImg.jpg");
			questions.add(item);
		}
		

		// 影调风格
		ContinuityVideoStyle style = ContinuityVideoStyle.getEnum(pmsContinuity.getVideoStyle());
		try {
			data.put("videoStyle", style == null ? "" :style.getPic());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		data.put("videoStyleName", style == null ? "" : style.getText());
		
		//处理一遍空值
		for(String each:data.keySet()) {
			if(data.get(each)==null) {
				data.put(each, "");
			}
		}

	}

	@RequestMapping("/synergetic/listByName")
	public List<PmsProjectFlow> getByName(@RequestBody final PmsProjectFlow pmsProjectFlow,
			final HttpServletRequest request) {
		SessionInfo session = getCurrentInfo(request);
		return pmsProjectFlowFacade.getSynerteticProjectByName(session.getReqiureId(), pmsProjectFlow.getProjectName());
	}

}
