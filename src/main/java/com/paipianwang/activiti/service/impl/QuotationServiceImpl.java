package com.paipianwang.activiti.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paipianwang.activiti.poi.QuotationPoiAdapter;
import com.paipianwang.activiti.service.QuotationService;
import com.paipianwang.pat.common.entity.PmsResult;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.workflow.entity.PmsProjectFlow;
import com.paipianwang.pat.workflow.entity.PmsQuotation;
import com.paipianwang.pat.workflow.entity.PmsQuotationItem;
import com.paipianwang.pat.workflow.entity.PmsQuotationType;
import com.paipianwang.pat.workflow.facade.PmsProjectFlowFacade;
import com.paipianwang.pat.workflow.facade.PmsQuotationFacade;

@Service("quotationService")
public class QuotationServiceImpl implements QuotationService {

	@Autowired
	private PmsQuotationFacade pmsQuotationFacade;
	@Autowired
	private QuotationPoiAdapter quotationPoiAdapter;
	@Autowired
	private PmsProjectFlowFacade pmsProjectFlowFacade;
	

	/**
	 * 导出报价单
	 */
	@Override
	public void export(PmsQuotation quotation, OutputStream os,HttpServletRequest request) {
//		PmsQuotation quotation = pmsQuotationFacade.getByProjectId(projectId);
		// 创建文档
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
		// 创建一个新的页
		XSSFSheet sheet = xssfWorkbook.createSheet("项目报价单");
		// 不显示网格线
		sheet.setDisplayGridlines(false);
		sheet.setDefaultColumnWidth(15 * 256);
		sheet.setDefaultRowHeight((short) (20 * 20));

		int rowIndex = 0;
		//生成图片头
		try {
			rowIndex=quotationPoiAdapter.createHeaderImg(xssfWorkbook, sheet, rowIndex, request.getServletContext().getRealPath("/resources/images/priceSheet.png"));
		} catch (Exception e) {
			return ;
		}
		// 生成项目信息
		rowIndex = quotationPoiAdapter.createProjectInfo(xssfWorkbook, sheet, rowIndex, quotation);

		// 生成头部信息
		rowIndex = quotationPoiAdapter.createHead(xssfWorkbook, sheet, rowIndex);
		// 生成数据
		rowIndex = quotationPoiAdapter.createDataItem(xssfWorkbook, sheet, rowIndex, quotation);
		// 生成合计
		rowIndex = quotationPoiAdapter.createDataInfo(xssfWorkbook, sheet, rowIndex, quotation);

		// 数据导出
		try {
			xssfWorkbook.write(os);
			xssfWorkbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存
	 * @return 
	 */
	@Override
	public PmsResult saveOrUpdateQuotation(PmsQuotation pmsQuotation) {
		PmsResult result=new PmsResult();
		//数据格式转换
		List<PmsQuotationItem> items=pmsQuotation.getItems();
		if(!ValidateUtil.isValid(items)){
			result.setResult(false);
			result.setErr("请选择报价明细");
			return result;
		}

		//校验计算结果
		PmsResult validate=vadalitCompute(pmsQuotation);
		if(validate!=null){
			return validate;
		}
		
		//对应项目id
		if(!ValidateUtil.isValid(pmsQuotation.getProjectId())){
			List<PmsProjectFlow> flows=pmsProjectFlowFacade.getByName(pmsQuotation.getProjectName());
			if(ValidateUtil.isValid(flows)){
				//存在
				pmsQuotation.setProjectId(flows.get(0).getProjectId());
			}
		}
		
		//项目报价单是否存在，存在则更新，否则插入
		if(!ValidateUtil.isValid(pmsQuotation.getProjectId())){
			//临时插入
			result=pmsQuotationFacade.insert(pmsQuotation);
		}else{
			PmsQuotation old=pmsQuotationFacade.getByProjectId(pmsQuotation.getProjectId());
			if(old!=null){
				//更新
				pmsQuotation.setQuotationId(old.getQuotationId());
				result=pmsQuotationFacade.update(pmsQuotation);
			}else{
				result=pmsQuotationFacade.insert(pmsQuotation);
			}
		}
		
		return result;
	}
	
	private PmsResult vadalitCompute(PmsQuotation pmsQuotation){
		PmsResult result=new PmsResult();
		BigDecimal subTotal=new BigDecimal(0.0);
		for(PmsQuotationItem item:pmsQuotation.getItems()){
			double sum=Double.parseDouble(item.getSum());
			if(item.getFullJob()==PmsQuotationType.FULLJOB_NO){
				//非整包
				sum=(item.getDays()==null?1:item.getDays())*item.getQuantity()*item.getUnitPrice();
				if(sum!=Double.parseDouble(item.getSum())){
					result.setResult(false);
					result.setErr(item.getTypeName()+"结果有误.");
					return result;
				}
			}
			
			subTotal=subTotal.add(new BigDecimal(sum));
		}
		if(subTotal.compareTo(new BigDecimal(pmsQuotation.getSubTotal()))!=0){
			result.setResult(false);
			result.setErr("合计结果有误.");
			return result;
		}
		
		
		BigDecimal total=subTotal.multiply((new BigDecimal(1).add(new BigDecimal(pmsQuotation.getTaxRate()).divide(new BigDecimal(100)))));
		total=total.subtract(new BigDecimal(pmsQuotation.getDiscount()));
		if(new BigDecimal(pmsQuotation.getTotal()).compareTo(total)!=0){
			result.setResult(false);
			result.setErr("最终结果有误.");
			return result;
		}
		return null;
	}
	
	/*private PmsResult vadalitCompute(PmsQuotation pmsQuotation){
		PmsResult result=new PmsResult();
		double subTotal=0.0;
		BigDecimal subTotal=new BigDecimal(0.0)
		for(PmsQuotationItem item:pmsQuotation.getItems()){
			double sum=(item.getDays()==null?1:item.getDays())*item.getQuantity()*item.getUnitPrice();
			if(sum!=Double.parseDouble(item.getSum())){
				result.setResult(false);
				result.setErr(item.getTypeName()+"结果有误.");
				return result;
			}
			subTotal+=sum;
		}
		if(subTotal!=Double.parseDouble(pmsQuotation.getSubTotal())){
			result.setResult(false);
			result.setErr("合计结果有误.");
			return result;
		}
		
		double total=subTotal*(1+Double.parseDouble(pmsQuotation.getTaxRate()))-Double.parseDouble(pmsQuotation.getDiscount());
		if(Double.parseDouble(pmsQuotation.getTotal())!=total){
			result.setResult(false);
			result.setErr("最终结果有误.");
			return result;
		}
		return null;
	}*/

}
