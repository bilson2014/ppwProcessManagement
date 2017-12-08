package com.paipianwang.activiti.poi;

import java.awt.Color;
import java.io.FileInputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.workflow.entity.PmsQuotation;
import com.paipianwang.pat.workflow.entity.PmsQuotationItem;

@Component
@Aspect
public class QuotationPoiAdapter {

	/**
	 * 创建图片表头
	 */
	public int createHeaderImg(XSSFWorkbook xssfWorkbook, XSSFSheet sheet, int rowIndex, String imgPath)
			throws Exception {
		XSSFRow xssfRow = sheet.createRow(rowIndex);
		xssfRow.setHeight((short) (50 * 20));
		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 6));

		// XSSFCell xssfCell = xssfRow.createCell(0);

		FileInputStream stream = new FileInputStream(imgPath);
		byte[] bytes = new byte[(int) stream.getChannel().size()];
		// 读取图片到二进制数组
		stream.read(bytes);

		int pictureIdx = xssfWorkbook.addPicture(bytes, HSSFWorkbook.PICTURE_TYPE_JPEG);
		XSSFDrawing patriarch = sheet.createDrawingPatriarch();
		XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 255, 255, (short) 0, 0, (short) 7, 1);
		// anchor.setAnchorType(anchorType);
		patriarch.createPicture(anchor, pictureIdx);
		return rowIndex+1;
	}
	/**
	 * 生成项目信息
	 */
	public int createProjectInfo(XSSFWorkbook xssfWorkbook,XSSFSheet sheet,int rowIndex,PmsQuotation quotation){
		// 样式
		XSSFCellStyle cs=getCenterCellStyle(xssfWorkbook);
		
//		XSSFRow xssfRow = sheet.createRow(rowIndex++);
//		XSSFCell xssfCell = xssfRow.createCell(0);
//		xssfCell.setCellStyle(cs);
//		xssfCell.setCellValue(quotation.getProjectName());
//		
//		xssfRow = sheet.createRow(rowIndex++);
//		xssfCell = xssfRow.createCell(0);
//		xssfCell.setCellStyle(cs);
//		xssfCell.setCellValue(quotation.getCreateDate());
		
		//字体
		XSSFFont xssfFont = xssfWorkbook.createFont();
		xssfFont.setFontHeightInPoints((short) 12);
		cs.setFont(xssfFont);
		
		XSSFRow xssfRow = sheet.createRow(rowIndex);
		xssfRow.setHeight((short)(27 * 20));
		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 6));
		
		XSSFCell xssfCell = xssfRow.createCell(0);
		xssfCell.setCellStyle(cs);
		xssfCell.setCellValue(quotation.getProjectName()+"("+quotation.getUpdateDate()+")");
		
		return rowIndex+1;
	}
	
	//生成头部
	public int createHead(XSSFWorkbook xssfWorkbook,XSSFSheet sheet,int rowIndex){
		
		XSSFCellStyle cs = getHeadCellStyle(xssfWorkbook);
		XSSFRow xssfRow = sheet.createRow(rowIndex);
		// 设置头部的高度
		xssfRow.setHeight((short)(23 * 20));
		//头部冻结
//		sheet.createFreezePane(1, rowIndex+1, 1, rowIndex+1);//一行+一列
		sheet.createFreezePane(0, rowIndex+1, 0, rowIndex+1);
		//合并项目
		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 1));

		XSSFCell xssfCell = xssfRow.createCell(0);
		xssfCell.setCellStyle(cs);
		xssfCell.setCellValue("项目");
		
		xssfCell = xssfRow.createCell(2);
		xssfCell.setCellStyle(cs);
		xssfCell.setCellValue("天数");
		
		xssfCell = xssfRow.createCell(3);
		xssfCell.setCellStyle(cs);
		xssfCell.setCellValue("数量");
		
		xssfCell = xssfRow.createCell(4);
		xssfCell.setCellStyle(cs);
		xssfCell.setCellValue("单价");
		
		xssfCell = xssfRow.createCell(5);
		xssfCell.setCellStyle(cs);
		xssfCell.setCellValue("标价/元");
		
		xssfCell = xssfRow.createCell(6);
		xssfCell.setCellStyle(cs);
		xssfCell.setCellValue("描述");
		
		// 设置行宽
		sheet.setColumnWidth(0, 20 * 256);
		sheet.setColumnWidth(1, 30 * 256);
		sheet.setColumnWidth(2, 10 * 256);
		sheet.setColumnWidth(3, 10 * 256);
		sheet.setColumnWidth(4, 12 * 256);
		sheet.setColumnWidth(5, 15 * 256);
		sheet.setColumnWidth(6, 40 * 256);

		return rowIndex+1;
	}
	
	//报价单明细
	public int createDataItem(XSSFWorkbook xssfWorkbook, XSSFSheet sheet, int rowIndex, PmsQuotation quotation) {
		// 样式
		/*XSSFCellStyle cs = getCenterCellStyle(xssfWorkbook);

		XSSFCellStyle typeCs = getTypeCellStyle(xssfWorkbook);

		List<PmsQuotationItem> all = quotation.getItems();

		int beginMerge = 0;
		for (int i = 0; i < all.size(); i++) {
			PmsQuotationItem type = all.get(i);

			if (i == 0 || type.getTypeId() != all.get(i - 1).getTypeId()) {
				// 大类名称
				XSSFRow xssfRow = sheet.createRow(rowIndex++);
				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 0, 6));

				XSSFCell xssfCell = xssfRow.createCell(0);
				xssfCell.setCellStyle(typeCs);
				xssfCell.setCellValue(type.getTypeName());
				
				beginMerge=rowIndex;//从大类后开始合并
			}
			XSSFRow xssfRow = sheet.createRow(rowIndex++);
			XSSFCell xssfCell = xssfRow.createCell(1);
			xssfCell.setCellStyle(cs);
			xssfCell.setCellValue(type.getDetailName());

			xssfCell = xssfRow.createCell(2);
			xssfCell.setCellStyle(cs);
			xssfCell.setCellValue(type.getDays() == null ? "/" : type.getDays() + "");

			xssfCell = xssfRow.createCell(3);
			xssfCell.setCellStyle(cs);
			xssfCell.setCellValue(type.getQuantity());

			xssfCell = xssfRow.createCell(4);
			xssfCell.setCellStyle(cs);
			xssfCell.setCellValue(type.getUnitPrice());

			xssfCell = xssfRow.createCell(5);
			xssfCell.setCellStyle(cs);
			xssfCell.setCellValue(type.getSum());

			xssfCell = xssfRow.createCell(6);
			xssfCell.setCellStyle(cs);
			xssfCell.setCellValue(type.getDescription());

			// 设置行高
			xssfRow.setHeightInPoints(20);

			// 子类名称
			if (i == all.size()-1 || (type.getItemId() != all.get(i + 1).getItemId())) {
				 xssfRow = sheet.getRow(beginMerge);
				xssfCell = xssfRow.createCell(0);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellValue(type.getItemName());

				if(rowIndex-1>beginMerge){
					// 合并至当前
					sheet.addMergedRegion(new CellRangeAddress(beginMerge, rowIndex - 1, 0, 0));
				}

				beginMerge = rowIndex;//从下一行开始合并
			}
<<<<<<< HEAD
			//大类名称
			XSSFRow xssfRow = sheet.getRow(rowIndex-itemRows);
			XSSFCell xssfCell = xssfRow.createCell(0);
			xssfCell.setCellStyle(typeCs);
			xssfCell.setCellValue(type.getTypeName());
			
			
		}*/

		return rowIndex;
	}
	//报价单合计信息
	public int createDataInfo(XSSFWorkbook xssfWorkbook,XSSFSheet sheet,int rowIndex,PmsQuotation quotation){
		XSSFCellStyle cs = getCenterCellStyle(xssfWorkbook);

		XSSFCellStyle typeCs=getItemCellStyle(xssfWorkbook);
		//空一行
		rowIndex++;
		//
		XSSFRow xssfRow = sheet.createRow(rowIndex++);
		XSSFCell xssfCell = xssfRow.createCell(0);
		xssfCell.setCellStyle(typeCs);
		xssfCell.setCellValue("合计");
		createNullCell(xssfRow, cs, 1, 4);
		
		xssfCell = xssfRow.createCell(5);
		xssfCell.setCellStyle(cs);
		xssfCell.setCellValue(quotation.getSubTotal());
		createNullCell(xssfRow, cs, 6, 1);
		//设置行高
		xssfRow.setHeightInPoints(20);
		//
		xssfRow = sheet.createRow(rowIndex++);
		xssfCell = xssfRow.createCell(0);
		xssfCell.setCellStyle(typeCs);
		xssfCell.setCellValue("税率");
		createNullCell(xssfRow, cs, 1, 4);
		
		xssfCell = xssfRow.createCell(5);
		xssfCell.setCellStyle(cs);
		if(ValidateUtil.isValid(quotation.getTaxRate())){
			xssfCell.setCellValue(quotation.getTaxRate()+"%");
		}
		
		createNullCell(xssfRow, cs, 6, 1);
		//设置行高
		xssfRow.setHeightInPoints(20);
		//
		xssfRow = sheet.createRow(rowIndex++);
		xssfCell = xssfRow.createCell(0);
		xssfCell.setCellStyle(typeCs);
		xssfCell.setCellValue("折扣金额");
		createNullCell(xssfRow, cs, 1, 4);
		
		xssfCell = xssfRow.createCell(5);
		xssfCell.setCellStyle(cs);
		xssfCell.setCellValue(quotation.getDiscount());
		createNullCell(xssfRow, cs, 6, 1);
		//设置行高
		xssfRow.setHeightInPoints(20);
		//
		xssfRow = sheet.createRow(rowIndex++);
		xssfCell = xssfRow.createCell(0);
		xssfCell.setCellStyle(typeCs);
		xssfCell.setCellValue("最终价格");
		createNullCell(xssfRow, cs, 1, 4);
		
		xssfCell = xssfRow.createCell(5);
		xssfCell.setCellStyle(cs);
		xssfCell.setCellValue(quotation.getTotal());
		createNullCell(xssfRow, cs, 6, 1);
		//设置行高
		xssfRow.setHeightInPoints(20);
		
		return rowIndex;
	}
	
	public void createNullCell(XSSFRow xssfRow,XSSFCellStyle cs,int begin,int size){
		for(int i=begin;i<(begin+size);i++){
			XSSFCell xssfCell;
			xssfCell = xssfRow.createCell(i);
			xssfCell.setCellStyle(cs);
		}
	}
	
	/**
	 * 头部样式
	 * @param workbook
	 * @return
	 */
	public XSSFCellStyle getHeadCellStyle(XSSFWorkbook workbook) {
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		
		XSSFFont xssfFont = workbook.createFont();
		xssfFont.setBold(true);
		cellStyle.setFont(xssfFont);
		
		setCellBorder(cellStyle);
		return cellStyle;
	}
	/**
	 * 大类样式
	 * @param workbook
	 * @return
	 */
	public XSSFCellStyle getTypeCellStyle(XSSFWorkbook workbook) {
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		
		cellStyle.setFillForegroundColor(new XSSFColor(new Color(222, 222, 222)));
		
		
		XSSFFont xssfFont = workbook.createFont();
		xssfFont.setBold(true);
		cellStyle.setFont(xssfFont);
		
		setCellBorder(cellStyle);
		return cellStyle;
	}
	
	public XSSFCellStyle getItemCellStyle(XSSFWorkbook workbook) {
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		
		XSSFFont xssfFont = workbook.createFont();
		xssfFont.setBold(true);
		cellStyle.setFont(xssfFont);
		
		setCellBorder(cellStyle);
		return cellStyle;
	}
	
	public XSSFCellStyle getCenterCellStyle(XSSFWorkbook workbook) {
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		
		setCellBorder(cellStyle);
		return cellStyle;
	}
	
	public void setCellBorder(final XSSFCellStyle cellStyle) {
		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框   
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框   
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框   
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
		
		cellStyle.setBottomBorderColor(HSSFColor.GREY_40_PERCENT.index);
		cellStyle.setLeftBorderColor(HSSFColor.GREY_40_PERCENT.index);
		cellStyle.setTopBorderColor(HSSFColor.GREY_40_PERCENT.index);
		cellStyle.setRightBorderColor(HSSFColor.GREY_40_PERCENT.index);
	}
}
