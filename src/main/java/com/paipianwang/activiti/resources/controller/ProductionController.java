package com.paipianwang.activiti.resources.controller;

import java.io.IOException;
import java.io.OutputStream;
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

import com.paipianwang.activiti.service.ProductionService;
import com.paipianwang.pat.common.entity.PmsResult;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.DateUtils;
import com.paipianwang.pat.common.util.JsonUtil;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.workflow.entity.PmsProductionInfo;
import com.paipianwang.pat.workflow.entity.PmsProjectFlow;
import com.paipianwang.pat.workflow.entity.ProductionConstants;
import com.paipianwang.pat.workflow.facade.PmsProductionInfoFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectFlowFacade;

@RestController
@RequestMapping("/production")
public class ProductionController extends BaseController{

	@Autowired
	private PmsProductionInfoFacade pmsProductionInfoFacade;
	@Autowired
	private PmsProjectFlowFacade pmsProjectFlowFacade;	
	@Autowired
	private ProductionService productionService;

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
		return new ModelAndView("/flow/makeProduct", model);
	}

	/**
	 * 获取用户参与过的进行中的项目分镜头脚本
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/list/synergetic")
	public List<PmsProductionInfo> getSynergeticProductionInfo(final HttpServletRequest request) {
		SessionInfo info = getCurrentInfo(request);
		return pmsProductionInfoFacade.getSynergeticProductionInfoProject(info.getReqiureId());
	}

	/**
	 * 获取项目分镜头脚本 TODO 修改此种类型为PmsResult封装
	 * 
	 * @param projectId
	 * @return
	 */
	@RequestMapping("/get/{projectId}")
	public PmsProductionInfo getByProjectId(@PathVariable("projectId") String projectId) {
		return productionService.getByProjectId(projectId);
	}
	
	/**
	 * 校验项目分镜脚本是否存在
	 * @param projectId
	 * @return
	 */
	@RequestMapping("/validate/{projectId}")
	public PmsResult validateByProjectId(@PathVariable("projectId") String projectId) {
		long count=pmsProductionInfoFacade.countByProjectId(projectId);
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
	 * @param pmsProductionInfo
	 */
	@RequestMapping("/save")
	public PmsResult saveOrUpdateProduction(@RequestBody final PmsProductionInfo pmsProductionInfo,
			final HttpServletRequest request, final HttpServletResponse response) {
		
		return productionService.saveOrUpdate(pmsProductionInfo);
	}

	/**
	 * 导出
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/export")
	public void export(final PmsProductionInfo pmsProductionInfo,HttpServletRequest request,
			final HttpServletResponse response) {	
		 
		OutputStream outputStream = null;
		try {
			if (pmsProductionInfo != null) {
				// 组装数据
				Map<String, Object> data = new HashMap<String, Object>();
				

				response.setCharacterEncoding("utf-8");
				response.setContentType("application/octet-stream");

				String filename = data.get("videoTypeName") + "分镜脚本.pdf";

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
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("导出异常");
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


	/**
	 * 获取当前用于参与过的进行中的项目列表
	 * @param pmsProjectFlow
	 * @param request
	 * @return
	 */
	@RequestMapping("/synergetic/listByName")
	public List<PmsProjectFlow> getByName(@RequestBody final PmsProjectFlow pmsProjectFlow,
			final HttpServletRequest request) {
		SessionInfo session = getCurrentInfo(request);
		return pmsProjectFlowFacade.getSynerteticProjectByName(session.getReqiureId(), pmsProjectFlow.getProjectName());
	}
	
	/**
	 * 获取制作资源类别树形结构数据
	 * @return
	 */
	@RequestMapping("/typeList")
	public List getResourceTypeTree() {
		//TODO 新写
		/*ProductionResource[] resources=ProductionResource.values();
		for(ProductionResource resource:resources) {
			Long[] typeIds=resource.getQuotationType();
			for(Long typeId:typeIds) {
				
			}
		}*/
		return null;
	}
	/**
	 * 根据条件检索制作资源
	 * @param paramMap
	 * @return
	 */
	@RequestMapping("/resource/list")
	public PmsProductionInfo listResource(@RequestBody final Map<String, Object> paramMap){
		
		Object category=paramMap.get("category");
		if(category!=null) {
			//处理参数
			if(paramMap.containsKey("beginAge") && ValidateUtil.isValid((String)paramMap.get("beginAge"))) {
				paramMap.put("beginBirthDay", DateUtils.getAgeByYear(Integer.parseInt((String)paramMap.get("beginAge")))+"");
				paramMap.remove("beginAge");
			}
			if(paramMap.containsKey("endAge") && ValidateUtil.isValid((String)paramMap.get("endAge"))) {
				paramMap.put("endBirthDay", DateUtils.getAgeByYear(Integer.parseInt((String)paramMap.get("endAge")))+"");
				paramMap.remove("endAge");
			}
			
			String type=(String) category;
			paramMap.remove("category");
			
			//只检索审核通过的
			paramMap.put("status", Integer.parseInt(ProductionConstants.statusList[1].getValue()));
			
			
			return productionService.listResourceByParam(paramMap,type);
		}

		return new PmsProductionInfo();
	}
	
	/**
	 * 获取制作资源下拉框list
	 * @param type
	 * @return
	 */
	@RequestMapping("/{type}/parameter")
	public Map<String, Object> getParameter(@PathVariable("type") final String type) {
		Map<String, Object> result = new HashMap<String, Object>();

		switch (type) {
		case "actor":
			// 演员
			ProductionConstants[] zoneList = ProductionConstants.zoneList;
			result.put("zoneList", zoneList);// JsonUtil.toJson(zoneList));
			break;
		case "director":
			// 导演
			ProductionConstants[] specialtyList = ProductionConstants.specialtyList;
			result.put("specialtyList", specialtyList);
			break;
		case "device":
			// 设备			
			break;
		case "studio":
			// 场地
			break;
		case "cameraman":
			//摄影师
			ProductionConstants[] specialSkillList = ProductionConstants.specialSkillList;
			result.put("specialSkillList", specialSkillList);
			break;
		case "clothing":
			//服装
			ProductionConstants[] clothingTypeList = ProductionConstants.clothingTypeList;
			result.put("clothingTypeList", clothingTypeList);
			result.put("accreditList", ProductionConstants.accreditList);
			break;
		case "props":
			//道具
			result.put("accreditList", ProductionConstants.accreditList);
			ProductionConstants[] propsTypeList = ProductionConstants.propsTypeList;
			result.put("propsTypeList", propsTypeList);
			break;
		default:
			break;
		}

		return result;
	}
	
	@RequestMapping("/get")
	public PmsResult getInfoById(Long id,String type) {
		PmsResult result=new PmsResult();
		
		Object info=productionService.getInfoById(id, type);
		if(info!=null) {
			result.setMsg(JsonUtil.toJson(info));
		}else {
			result.setResult(false);
		}
		
		return result;
	}
}
