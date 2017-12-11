package com.paipianwang.activiti.resources.controller;

import java.io.IOException;
import java.io.OutputStream;
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
import com.paipianwang.pat.workflow.entity.PmsQuotation;
import com.paipianwang.pat.workflow.entity.PmsQuotationType;
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
	
	/**
	 * 报价单生成器
	 * @param projectId
	 * @param model
	 * @return
	 */
	@RequestMapping("/info")
	public ModelAndView quotationView(ModelMap model){
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
		try {
			PmsQuotation quotation = pmsQuotationFacade.getById(quotationId);
			if(quotation==null){
				
			}
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
		
//		return result;
	}
}