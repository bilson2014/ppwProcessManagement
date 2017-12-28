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

import com.paipianwang.activiti.service.QuotationService;
import com.paipianwang.pat.common.entity.PmsResult;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.workflow.entity.PmsProjectFlow;
import com.paipianwang.pat.workflow.entity.PmsQuotation;
import com.paipianwang.pat.workflow.entity.PmsQuotationItem;
import com.paipianwang.pat.workflow.entity.PmsQuotationType;
import com.paipianwang.pat.workflow.facade.PmsProjectFlowFacade;
import com.paipianwang.pat.workflow.facade.PmsQuotationFacade;
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
	@RequestMapping("/export/{quotationId}")
	public void export(@PathVariable("quotationId")Long quotationId,HttpServletRequest request, final HttpServletResponse response){
		//导出
		OutputStream outputStream=null;
		PmsQuotation quotation = pmsQuotationFacade.getById(quotationId);
		try {
			if(quotation!=null){
				response.setCharacterEncoding("utf-8");
				response.setContentType("application/octet-stream");
				String filename ="《"+ quotation.getProjectName()+"》报价单.xlsx";
				
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
			if(quotation!=null && !ValidateUtil.isValid(quotation.getProjectId())){
//				List<PmsProjectFlow> flows=pmsProjectFlowFacade.getByName(quotation.getProjectName());
//				if(!ValidateUtil.isValid(flows)){
				pmsQuotationFacade.delete(quotationId);
//				}
			}
		}
		
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
		
//		quotation.getItems().sort(new Comparator<PmsQuotationItem>() {
//
//			@Override
//			public int compare(PmsQuotationItem o1, PmsQuotationItem o2) {
//				if(o1.getTypeId().equals(o2.getTypeId())){
//					//大类相同，子类排序
//					return o1.getItemName().compareTo(o2.getItemName());
//				}
//				return 0;
//			}
//		});
		
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
					}
				}else{
					break;
				}
			}
		}
		
		return quotation.getItems();
	}
	
}
