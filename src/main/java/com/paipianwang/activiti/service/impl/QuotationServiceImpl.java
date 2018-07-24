package com.paipianwang.activiti.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paipianwang.activiti.poi.QuotationPoiAdapter;
import com.paipianwang.activiti.service.OnlineDocService;
import com.paipianwang.activiti.service.QuotationService;
import com.paipianwang.activiti.utils.HttpUtil;
import com.paipianwang.pat.common.config.PublicConfig;
import com.paipianwang.pat.common.entity.PmsResult;
import com.paipianwang.pat.common.util.FileUtils;
import com.paipianwang.pat.common.util.PathFormatUtils;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.workflow.entity.PmsProjectUser;
import com.paipianwang.pat.workflow.entity.PmsQuotation;
import com.paipianwang.pat.workflow.entity.PmsQuotationItem;
import com.paipianwang.pat.workflow.entity.PmsQuotationType;
import com.paipianwang.pat.workflow.facade.PmsProjectUserFacade;
import com.paipianwang.pat.workflow.facade.PmsQuotationFacade;

@Service("quotationService")
public class QuotationServiceImpl implements QuotationService {

	@Autowired
	private PmsQuotationFacade pmsQuotationFacade;
//	@Autowired
//	private QuotationPoiAdapter quotationPoiAdapter;
	@Autowired
	private OnlineDocService onlineDocService;
	@Autowired
	private PmsProjectUserFacade pmsProjectUserFacade;
	

	/**
	 * 导出报价单
	 */
	@Override
	public void export(PmsQuotation quotation, OutputStream os, HttpServletRequest request) {
		// 创建文档
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
		
		String imgPath=request.getServletContext().getRealPath("/resources/images/excelTitle.png");
		
		// 创建一个新的页
		/*XSSFSheet sheet = xssfWorkbook.createSheet("项目报价单");

		int rowIndex = 0;
		// 生成项目信息
		rowIndex = quotationPoiAdapter.createProjectInfo(xssfWorkbook, sheet, rowIndex, quotation);
		// 分类
		Map<String, List<PmsQuotationItem>> types = new HashMap<>();
		for (PmsQuotationItem item : quotation.getItems()) {
			if (!types.containsKey(item.getTypeName())) {
				types.put(item.getTypeName(), new ArrayList<>());
			}
			types.get(item.getTypeName()).add(item);
		}
		// 各明细
		int num = 1;
		for (int i = 0; i < quotationPoiAdapter.head.length; i++) {
			if (types.containsKey(quotationPoiAdapter.head[i][0])) {
				rowIndex = quotationPoiAdapter.createHead(xssfWorkbook, sheet, rowIndex, i, num);
				rowIndex = quotationPoiAdapter.createDataItem(xssfWorkbook, sheet, rowIndex,
						types.get(quotationPoiAdapter.head[i][0]));
				//小计金额
				BigDecimal sum=new BigDecimal(0.0);
				for(PmsQuotationItem item:types.get(quotationPoiAdapter.head[i][0])){
					sum=sum.add(new BigDecimal(item.getSum()));
				}
				rowIndex = quotationPoiAdapter.createPriceTotal(xssfWorkbook, sheet, rowIndex, "小计", sum.toString());
				rowIndex++;
				num++;
				continue;
			}
		}

		rowIndex = rowIndex + 2;
		// 合计
		rowIndex = quotationPoiAdapter.createPriceTotal(xssfWorkbook, sheet, rowIndex, "不含税总价(元)",
				quotation.getSubTotal());
		
		rowIndex = quotationPoiAdapter.createTaxRate(xssfWorkbook, sheet, rowIndex, "税率", quotation.getTaxRate());
		rowIndex = quotationPoiAdapter.createPriceTotal(xssfWorkbook, sheet, rowIndex, "优惠(元)", quotation.getDiscount());
		rowIndex = quotationPoiAdapter.createPriceTotal(xssfWorkbook, sheet, rowIndex, "总价(元)", quotation.getTotal());
*/
		
		//客户信息
		String userName="";
		if(ValidateUtil.isValid(quotation.getProjectId())){
			PmsProjectUser user=pmsProjectUserFacade.getProjectUserByProjectId(quotation.getProjectId());
			if(user!=null){
				userName=user.getUserName();
			}
		}
		
		String projectName=quotation.getProjectName();
		if(!ValidateUtil.isValid(projectName) || projectName.equals("未命名项目") || projectName.equals("null")){
			projectName="";
		}
		String name=projectName+"报价单"+PathFormatUtils.parse("{yy}{mm}{dd}{hh}{ii}{ss}");
		String basePath=PublicConfig.DOC_TEMP_PATH+File.separator+"temp"+File.separator;
		
		QuotationPoiAdapter qa=new QuotationPoiAdapter(xssfWorkbook, imgPath);
		System.out.println("QuotationPoiAdapter");
		
		File sourceFile=new File(basePath+name+".xlsx");
		String pdfPath=basePath+name+".pdf";
		String destPath=basePath+name+".zip";
		File pdfFile=new File(pdfPath);
		File zipFile=new File(destPath);
		try {
			qa.createFile(quotation,userName, basePath+name+".xlsx");
			//转pdf
			onlineDocService.convertToPdf(sourceFile,pdfPath);
			//数据压缩
			FileUtils.zipFile(destPath, sourceFile,pdfFile);
			// 数据导出
			HttpUtil.saveTo(new FileInputStream(zipFile), os);
		} catch (Exception e1) {
			if(sourceFile.exists()){
				sourceFile.delete();
			}
			if(pdfFile.exists()){
				pdfFile.delete();
			}
			if(zipFile.exists()){
				zipFile.delete();
			}
		}

	}

	/**
	 * 保存
	 * @return 
	 */
	@Override
	public PmsResult saveOrUpdateQuotation(PmsQuotation pmsQuotation) {
		PmsResult result=new PmsResult();
		//校验
		if(!ValidateUtil.isValid(pmsQuotation.getProjectName())){
			result.setResult(false);
			result.setErr("项目名称不允许为空");
			return result;
		}
		
		if(!ValidateUtil.isValid(pmsQuotation.getTotal()) || !ValidateUtil.isValid(pmsQuotation.getSubTotal())){
			result.setResult(false);
			result.setErr("请录入价格信息");
			return result;
		}
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
		
		if(!ValidateUtil.isValid(pmsQuotation.getProjectId())){
			result.setResult(false);
			result.setErr("请选择项目");
			return result;
		}
		
		//对应项目id
		/*if(!ValidateUtil.isValid(pmsQuotation.getProjectId())){
			List<PmsProjectFlow> flows=pmsProjectFlowFacade.getByName(pmsQuotation.getProjectName());
			if(ValidateUtil.isValid(flows)){
				//存在
				pmsQuotation.setProjectId(flows.get(0).getProjectId());
			}
		}*/
		
		//项目报价单是否存在，存在则更新，否则插入
		/*if(!ValidateUtil.isValid(pmsQuotation.getProjectId())){
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
		}*/
		PmsQuotation old=pmsQuotationFacade.getByProjectId(pmsQuotation.getProjectId());
		if(old!=null){
			//项目作为唯一标记
			pmsQuotation.setQuotationId(old.getQuotationId());
		}
		if(ValidateUtil.isValid(pmsQuotation.getQuotationId())){
			result=pmsQuotationFacade.update(pmsQuotation);
		}else{
			result=pmsQuotationFacade.insert(pmsQuotation);
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
				sum=(item.getDays()==null?1:Integer.parseInt(item.getDays()))*Integer.parseInt(item.getQuantity())*item.getUnitPrice();
//				sum=(item.getDays()==null?1:item.getDays())*item.getQuantity()*item.getUnitPrice();

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
		if(ValidateUtil.isValid(pmsQuotation.getDiscount())) {
			total=total.subtract(new BigDecimal(pmsQuotation.getDiscount()));
		}
		if(new BigDecimal(pmsQuotation.getTotal()).compareTo(total)!=0){
			result.setResult(false);
			result.setErr("最终结果有误.");
			return result;
		}
		return null;
	}

}
