package com.paipianwang.activiti.resources.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
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

import com.paipianwang.activiti.domin.QuotationTemplateSelectVO;
import com.paipianwang.activiti.service.QuotationService;
import com.paipianwang.pat.common.entity.KeyValue;
import com.paipianwang.pat.common.entity.PmsResult;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.JsonUtil;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.workflow.entity.PmsProjectFlow;
import com.paipianwang.pat.workflow.entity.PmsQuotation;
import com.paipianwang.pat.workflow.entity.PmsQuotationItem;
import com.paipianwang.pat.workflow.entity.PmsQuotationTemplate;
import com.paipianwang.pat.workflow.entity.PmsQuotationType;
import com.paipianwang.pat.workflow.enums.ProductionResource;
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
		PmsQuotation quotation= pmsQuotationFacade.getByProjectId(projectId);
		
		if(ValidateUtil.isValid(quotation.getItems())) {
			List<PmsQuotationType> all=pmsQuotationTypeFacade.findAll();
			for(PmsQuotationItem item:quotation.getItems()) {
				int flag=3;
				for(PmsQuotationType type:all) {
					if(item.getItemId().equals(type.getTypeId())) {
						item.setItemDate(type.getCreateDate());
						flag--;
					}else if(item.getDetailId().equals(type.getTypeId())) {
						item.setDetailDate(type.getCreateDate());
						flag--;
					}else if(item.getTypeId().equals(type.getTypeId())) {
						item.setTypeDate(type.getCreateDate());
						flag--;	
					}
					if(flag<=0) {
						break;
					}
				}
			}
		}
			
		return quotation;
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
		try {
			if(quotation!=null){
				response.setCharacterEncoding("utf-8");
				response.setContentType("application/octet-stream");
				
				String filename ="《"+ quotation.getProjectName()+"》报价单.zip";
				if(!ValidateUtil.isValid(quotation.getProjectName()) || quotation.getProjectName().equals("未命名项目")){
					filename ="项目报价单.zip";
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
				
				//评估人信息
				SessionInfo sessionInfo=this.getCurrentInfo(request);
				quotation.setUpdateUser(sessionInfo.getRealName());
				quotation.setUpdateUserTel(sessionInfo.getTelephone());
				//项目信息
				if(ValidateUtil.isValid(quotation.getProjectId())){
					List<String> metaData=new ArrayList<>();
					metaData.add("productName");
					metaData.add("productConfigLevelName");
					PmsProjectFlow flow=pmsProjectFlowFacade.getProjectFlowByProjectId(metaData, quotation.getProjectId());
					if(flow!=null){
						quotation.setProductName(flow.getProductName()+flow.getProductConfigLevelName());
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
		LinkedHashSet<QuotationTemplateSelectVO> chanpin=new LinkedHashSet<>();
		
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
		//校验
		if(!ValidateUtil.isValid(template.getTemplateName())){
			result.setResult(false);
			result.setErr("模板名称不允许为空");
			return result;
		}
		if(!ValidateUtil.isValid(template.getItems())){
			result.setResult(false);
			result.setErr("请选择报价明细");
			return result;
		}
		if(!ValidateUtil.isValid(template.getTotal()) || !ValidateUtil.isValid(template.getSubTotal())){
			result.setResult(false);
			result.setErr("请录入价格信息");
			return result;
		}
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
		template.setCreateId(info.getReqiureId());
		
		if(template.getTemplateId()!=null){
			//校验数据过期
			PmsQuotationTemplate old=pmsQuotationTemplateFacade.getById(template.getTemplateId());
			if(old==null){
				result.setResult(false);
				result.setErr("数据已过期");
				return result;
			}
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
	
	/**
	 * 校验模板过期情况
	 * @param template
	 * @param request
	 * @return
	 */
	@RequestMapping("/temp/validate")
	public PmsResult validateTemplate(@RequestBody final PmsQuotationTemplate template,HttpServletRequest request){
		return validateTemplate(template);
	}
	@RequestMapping("/temp/validate/{id}")
	public PmsResult validateTemplate(@PathVariable("id")Long id,HttpServletRequest request){
		PmsQuotationTemplate template=pmsQuotationTemplateFacade.getById(id);
		if(template==null) {
			PmsResult result=new PmsResult();
			result.setCode("1");
			result.setMsg("数据已过期，请刷新重试");
			return result;
		}
		
		return validateTemplate(template);
	}
	
	private PmsResult validateTemplate(final PmsQuotationTemplate template){
		PmsResult result=new PmsResult();
		result.setCode("0");
		
		List<PmsQuotationItem> items=template.getItems();
		
		if(!ValidateUtil.isValid(items)) {
			return result;
		}
		
		List<PmsQuotationType> types=pmsQuotationTypeFacade.findAll();
		
		int reDate=0;
		int reName=0;
		
		for(PmsQuotationItem item:items) {
			for(PmsQuotationType type:types) {
				if(item.getDetailId().equals(type.getTypeId())) {
					if(item.getDetailDate()!=null && !item.getDetailDate().equals(type.getCreateDate())){
						reDate++;
					}
					if(item.getDetailName()!=null && !item.getDetailName().equals(type.getTypeName())) {
						reName++;
					}
				}
				if(item.getItemId().equals(type.getTypeId())) {
					if(item.getItemDate()!=null && !item.getItemDate().equals(type.getCreateDate())){
						reDate++;
					}
					if(item.getItemName()!=null && !item.getItemName().equals(type.getTypeName())) {
						reName++;
					}
				}
				if(item.getTypeId().equals(type.getTypeId())) {
					if(item.getTypeDate()!=null && !item.getTypeDate().equals(type.getCreateDate())){
						reDate++;
					}
					if(item.getTypeName()!=null && !item.getTypeName().equals(type.getTypeName())) {
						reName++;
					}
				}
			}
		}
		result.setMsg("");
		if(reDate>0) {
			result.setResult(false);
			result.setMsg(reDate+"项顺序变更;");
		}
		if(reName>0) {
			result.setResult(false);
			result.setMsg(result.getMsg()+reName+"项名称变更;");
		}
		if(!result.isResult()) {
			result.setCode("2");
			result.setMsg("报价单类型发生了调整，此模板中"+result.getMsg());
		}		
		return result;
	}
	
	/**
	 * 获取制片工具资源对应报价单类型
	 * 			配置类型及其所有下级节点
	 * @param typeId
	 * @return
	 */
	@RequestMapping("/production/select")
	public List<PmsQuotationType> listByProduction(String productionType,String subType){
		List<PmsQuotationType> result=new ArrayList<>();
		
		Long[] typeIds=new Long[0];
				
		if(ProductionResource.device.getKey().equals(productionType) && ValidateUtil.isValid(subType)) {
			typeIds=new Long[] {Long.parseLong(subType)};//同getChildren
		}else {
			ProductionResource relation=ProductionResource.getEnum(productionType);
			if(relation!=null) {
				typeIds=relation.getQuotationType();
			}
		}
		
		for(Long typeId:typeIds) {
			PmsQuotationType self=pmsQuotationTypeFacade.getById(typeId);
			if(self!=null) {
				List<PmsQuotationType> types= pmsQuotationTypeFacade.findByParent(typeId);
				//如果对应顶级节点，则只取其下的两级节点
				if(!ProductionResource.device.getKey().equals(productionType) && PmsQuotationType.GRADE_TYPE.equals(self.getGrade())) {
					result.addAll(types);
				}else {
					self.setChildren(types);
					result.add(self);
				}	
			}
		}
	
		return result;
	}

	/**
	 * 获取下一级子节点
	 * @param typeId
	 * @return
	 */
	@RequestMapping("/production/children")
	public List<KeyValue> getChildren(String productionType,Long typeId){
		Long[] typeIds;
		
		if(ValidateUtil.isValid(productionType)) {
			ProductionResource relation=ProductionResource.getEnum(productionType);
			typeIds=relation.getQuotationType();
		}else {
			typeIds=new Long[] {typeId};
		}
		
		List<KeyValue> result=new ArrayList<KeyValue>();
		for(Long id:typeIds) {
			List<PmsQuotationType> types= pmsQuotationTypeFacade.findByParent(id);
			for(PmsQuotationType type:types){
				KeyValue keyValue=new KeyValue();
				keyValue.setKey(type.getTypeId()+"");
				keyValue.setValue(type.getTypeName());
				result.add(keyValue);		
			}
		}	
		return result;
	}
	
	
}
