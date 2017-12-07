package com.paipianwang.activiti.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
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
import com.paipianwang.pat.workflow.entity.PmsQuotation;
import com.paipianwang.pat.workflow.entity.PmsQuotationItem;
import com.paipianwang.pat.workflow.facade.PmsQuotationFacade;

@Service("quotationService")
public class QuotationServiceImpl implements QuotationService {

	@Autowired
	private PmsQuotationFacade pmsQuotationFacade;
	@Autowired
	private QuotationPoiAdapter quotationPoiAdapter;

	/**
	 * 导出报价单
	 */
	@Override
	public void export(String projectId, OutputStream os,HttpServletRequest request) {
		PmsQuotation quotation = pmsQuotationFacade.getByProjectId(projectId);
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
		}
		
		//todo 排序：grade
		
		List<PmsQuotationItem> parent=new ArrayList<>();
		for(PmsQuotationItem type:items){
			if(type.getGrade()==1){
				parent.add(type);
			}
			if(type.getGrade()==1 || type.getGrade()==2){
				for(PmsQuotationItem item:items){
					if(type.getTypeId()==item.getParentId()){
						type.getChildren().add(item);
					}
				}
			}
		}
		pmsQuotation.setItems(parent);
		//校验计算结果
		PmsResult validate=vadalitCompute(pmsQuotation);
		if(validate!=null){
			return validate;
		}
		
		if(pmsQuotation.getQuotationId()==null){
			pmsQuotationFacade.insert(pmsQuotation);
		}else{
			pmsQuotationFacade.update(pmsQuotation);
		}
		return result;
	}
	
	private PmsResult vadalitCompute(PmsQuotation pmsQuotation){
		PmsResult result=new PmsResult();
		double subTotal=0.0;
//		BigDecimal subTotal=new BigDecimal(0.0)
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
