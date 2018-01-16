package com.paipianwang.activiti.resources.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.paipianwang.activiti.domin.QuotationTemplateSelectVO;
import com.paipianwang.activiti.service.QuotationService;
import com.paipianwang.pat.common.entity.PmsResult;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.JsonUtil;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.workflow.entity.PmsProjectFlow;
import com.paipianwang.pat.workflow.entity.PmsQuotation;
import com.paipianwang.pat.workflow.entity.PmsQuotationItem;
import com.paipianwang.pat.workflow.entity.PmsQuotationTemplate;
import com.paipianwang.pat.workflow.entity.PmsQuotationType;
import com.paipianwang.pat.workflow.facade.PmsProjectFlowFacade;
import com.paipianwang.pat.workflow.facade.PmsQuotationFacade;
import com.paipianwang.pat.workflow.facade.PmsQuotationTemplateFacade;
import com.paipianwang.pat.workflow.facade.PmsQuotationTypeFacade;

/**
 * 报价单
 */
@RestController
@RequestMapping("/quotation")
public class QuotationController extends BaseController {

	@Autowired
	private PmsQuotationFacade pmsQuotationFacade;
	@Autowired
	private PmsQuotationTypeFacade pmsQuotationTypeFacade;
	@Autowired
	private QuotationService quotationService;
	@Autowired
	private PmsProjectFlowFacade pmsProjectFlowFacade;
	@Autowired
	private PmsQuotationTemplateFacade pmsQuotationTemplateFacade;
	
	/**
	 * 报价单生成器
	 * @param projectId
	 * @param model
	 * @return
	 */
	@RequestMapping("/info")
	public ModelAndView quotationView(final String projectId,ModelMap model){
		List<String> metaData=new ArrayList<>();
		metaData.add("projectName");
		PmsProjectFlow flow=pmsProjectFlowFacade.getProjectFlowByProjectId(metaData, projectId);
		if(flow!=null){
			model.put("projectName", flow.getProjectName());
		}
		model.put("projectId", projectId);
		return new ModelAndView("activiti/quotation", model);
	}
	
	/**
	 * 根据id获取下级子节点
	 * 		即根据选项获取下级报价单类型下拉框值
	 * @param typeId
	 * @return
	 */
	@RequestMapping("/select/type")
	public List<PmsQuotationType> getPmsQuotationTypeChildren(Long typeId){
		return pmsQuotationTypeFacade.findByParent(typeId);
	}
	
	/**
	 * 获取项目报价单
	 * @param projectId
	 * @return
	 */
	@RequestMapping("/get/{projectId}")
	public PmsQuotation getByProjectId(@PathVariable("projectId")String projectId){
		return pmsQuotationFacade.getByProjectId(projectId);
	}
	
	/**
	 * 校验
	 * @param pmsQuotation
	 * @return
	 */
	@RequestMapping("/validate/project-name")
	public PmsResult validateProjectExist(@RequestBody final PmsQuotation pmsQuotation ){
		PmsResult result=new PmsResult();
		List<PmsProjectFlow> flows=pmsProjectFlowFacade.getByName(pmsQuotation.getProjectName());
		
		if(ValidateUtil.isValid(flows)){
			//存在
			return result;
		}
		//不存在
		result.setResult(false);
		return result;
	}
	
	/**
	 * 保存/更新报价单
	 * @param pmsQuotation
	 */
	@RequestMapping("/save")
	public PmsResult saveOrUpdateQuotation(@RequestBody final PmsQuotation pmsQuotation,HttpServletRequest request, final HttpServletResponse response){
		PmsResult result=new PmsResult();
		//数据保存
		result=quotationService.saveOrUpdateQuotation(pmsQuotation);
		
		return result;
	}
	/**
	 * 导出
	 * @param quotationId 报价单id
	 * @param request
	 * @param response
	 */
	@RequestMapping("/export")
	public void export(final PmsQuotation quotation,HttpServletRequest request, final HttpServletResponse response){
		//导出
		OutputStream outputStream=null;
//		PmsQuotation quotation = pmsQuotationFacade.getById(quotationId);
		try {
			if(quotation!=null){
				response.setCharacterEncoding("utf-8");
				response.setContentType("application/octet-stream");
				String filename ="报价单.xlsx";
				if(ValidateUtil.isValid(quotation.getProjectName())){
					filename ="《"+ quotation.getProjectName()+"》报价单.xlsx";
				}
				
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
				
				if(!ValidateUtil.isValid(quotation.getItems())){
					try {
						quotation.setItems(JsonUtil.fromJsonArray(quotation.getItemContent(), PmsQuotationItem.class));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				quotationService.export(quotation, outputStream,request);
				
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
			/*if(quotation!=null && !ValidateUtil.isValid(quotation.getProjectId())){
//				List<PmsProjectFlow> flows=pmsProjectFlowFacade.getByName(quotation.getProjectName());
//				if(!ValidateUtil.isValid(flows)){
				pmsQuotationFacade.delete(quotationId);
//				}
			}*/
		}
		
	}
	/**
	 * 获取用户参与过的进行中的项目的报价单
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/list/synergetic")
	public List<PmsQuotation> getSynergeticQuotation(HttpServletRequest request, final HttpServletResponse response){
		SessionInfo info = getCurrentInfo(request);
		return pmsQuotationFacade.getSynergeticQuotationProject(info.getReqiureId());
	}
	
	/**
	 * 排序：保留原大类、子类顺序；子类相同的排序归一起
	 * @param quotation
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/order")
	public List<PmsQuotationItem> orderQuotation(@RequestBody final PmsQuotation quotation,HttpServletRequest request, final HttpServletResponse response){
		
		List<PmsQuotationItem> items=quotation.getItems();
		PmsQuotationItem item=new PmsQuotationItem();
		
		int size=items.size();
		for(int i=0;i<size;i++){
			if(i<2){
				continue;
			}
			item=items.get(i);
			//向上找到跟他相同的 typeId+itemId 插入
			for(int j=i-2;j>=0;j--){
				if(item.getTypeId().equals(items.get(j).getTypeId())){
					if(item.getItemId().equals(items.get(j).getItemId())){
						//插入
						items.remove(item);
						items.add(j+1, item);
						continue;
					}
				}else{
					break;
				}
			}
		}
		
		return quotation.getItems();
	}
	
	/**
	 * 获取报价单模板列表
	 * 	个人：集合
	 * 	产品线：树形集合
	 * @return
	 */
	@RequestMapping("/temp/list")
	public Map<String,Object> listTemplate(HttpServletRequest request){
		Map<String,Object> result=new HashMap<>();
		
		SessionInfo sessionInfo=getCurrentInfo(request);
		
		List<PmsQuotationTemplate> list=pmsQuotationTemplateFacade.listSelect(sessionInfo.getReqiureId());
		
		List<QuotationTemplateSelectVO> person=new ArrayList<>();
		Set<QuotationTemplateSelectVO> chanpin=new HashSet<>();
		
		result.put("person", person);
		result.put("chanpin", chanpin);
	
		for(PmsQuotationTemplate temp:list){
			//个人
			if(temp.getType()==PmsQuotationTemplate.TYPE_SELF){
				person.add(new QuotationTemplateSelectVO(temp.getTemplateId()+"", temp.getTemplateName()));
			}else{
				//产品线
				chanpin.add(new QuotationTemplateSelectVO(temp.getTemplateId()+"", temp.getTemplateName()));
			}
		}
		
		return result;
	}
	/**
	 * 校验名称唯一性
	 * @param template
	 * @return
	 */
	@RequestMapping("/temp/validate-name")
	public PmsResult validateName(@RequestBody final PmsQuotationTemplate template,HttpServletRequest request){
		//个人类型 名称是否存在
		SessionInfo info = getCurrentInfo(request);
		long count=pmsQuotationTemplateFacade.checkExistName(template.getTemplateName(), template.getTemplateId(),PmsQuotationTemplate.TYPE_SELF,info.getReqiureId());
		PmsResult result=new PmsResult();
		if(count>0){
			result.setResult(false);
			result.setErr("模板名称已存在");
		}
		return result;
	}
	/**
	 * 保存/更新
	 * @param template
	 * @return
	 */
	@RequestMapping("/temp/save")
	public PmsResult saveTemplate(@RequestBody final PmsQuotationTemplate template,HttpServletRequest request){
		PmsResult result=new PmsResult();
		SessionInfo info = getCurrentInfo(request);
		//个人
		template.setType(PmsQuotationTemplate.TYPE_SELF);
		//TODO 校验
		long count=pmsQuotationTemplateFacade.checkExistName(template.getTemplateName(), template.getTemplateId(),template.getType(),info.getReqiureId());
		if(count>0){
			if(template.getTemplateId()!=null){
				result.setResult(false);
				result.setErr("模板名称已存在");
				return result;
			}else{
				//更新
				List<PmsQuotationTemplate> exists=pmsQuotationTemplateFacade.listByName(template.getTemplateName(), template.getTemplateId(),template.getType(),info.getReqiureId());
				template.setTemplateId(exists.get(0).getTemplateId());
			}
		}
		
		if(template.getTemplateId()!=null){
			pmsQuotationTemplateFacade.update(template);
		}else{
			pmsQuotationTemplateFacade.insert(template);
		}
		
		result.setMsg(template.getTemplateId()+"");
		return result;
	}
	/**
	 * 删除模板
	 * @param templateId
	 * @return
	 */
	@RequestMapping("/temp/delete/{templateId}")
	public PmsResult deleteTemplate(@PathVariable final long templateId){
		PmsResult result=new PmsResult();
		PmsQuotationTemplate template=pmsQuotationTemplateFacade.getById(templateId);
		if(template!=null && template.getType()==PmsQuotationTemplate.TYPE_PRODUCT){
			result.setResult(false);
			result.setErr("只允许删除个人模板");
		}
		long count=pmsQuotationTemplateFacade.delete(templateId);
		
		return result;
	}
	/**
	 * 获取模板
	 * @param templateId
	 * @return
	 */
	@RequestMapping("/temp/get/{templateId}")
	public PmsQuotationTemplate getTemplate(@PathVariable final long templateId){
		return pmsQuotationTemplateFacade.getById(templateId);
	}
	/**
	 * 根据名称模糊查询个人模板
	 * @param pmsQuotationTemplate
	 * @param request
	 * @return
	 */
	@RequestMapping("/temp/listByName")
	public List<PmsQuotationTemplate> listTemplateByName(@RequestBody PmsQuotationTemplate pmsQuotationTemplate,HttpServletRequest request){
		
		SessionInfo sessionInfo=getCurrentInfo(request);
		
		List<PmsQuotationTemplate> list=pmsQuotationTemplateFacade.listLikeName(pmsQuotationTemplate.getTemplateName(), PmsQuotationTemplate.TYPE_SELF, sessionInfo.getReqiureId());
		
		return list;
	}
	
}
