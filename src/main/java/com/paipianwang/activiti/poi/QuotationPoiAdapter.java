package com.paipianwang.activiti.poi;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.paipianwang.pat.common.util.DateUtils;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.workflow.entity.PmsQuotation;
import com.paipianwang.pat.workflow.entity.PmsQuotationItem;

@Component
@Aspect
public class QuotationPoiAdapter {

	// 大类名称 显示名称 head颜色
	public static String[][] head = new String[][] { { "创作团队", "创作团队费用", "14281213" }, { "拍摄设备", "拍摄设备费用", "15986394" },
			{ "后期制作", "后期制作费用", "15523812" }, { "配音配乐", "配音配乐费用", "14610923" }, { "场地拍摄", "场地拍摄费用", "14408946" } };

	// String[] headTitle=new String[]{"收费项","天数","数量","单价","价格","备注"};

	/**
	 * 生成项目信息
	 */
	public int createProjectInfo(XSSFWorkbook xssfWorkbook, XSSFSheet sheet, int rowIndex, PmsQuotation quotation) {
		// 样式
		XSSFCellStyle cs = getCenterCellStyle(xssfWorkbook);

		// 字体
		XSSFFont xssfFont = xssfWorkbook.createFont();
		xssfFont.setFontHeightInPoints((short) 16);
		xssfFont.setFontName("宋体");
		xssfFont.setBold(true);
		cs.setFont(xssfFont);

		XSSFRow xssfRow = sheet.createRow(rowIndex);
		xssfRow.setHeight((short) (21 * 20));
		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 5));

		XSSFCell xssfCell = xssfRow.createCell(0);
		xssfCell.setCellStyle(cs);
		Date updateDate = new Date();
		if (ValidateUtil.isValid(quotation.getUpdateDate())) {
			updateDate = DateUtils.getDateByFormat(quotation.getUpdateDate(), "yyyy-MM-dd");
		}
		String date = DateUtils.getDateByFormatStr(updateDate, "yyyy-MM-dd");
		String projectName=quotation.getProjectName();
		if(!ValidateUtil.isValid(projectName) || projectName.equals("未命名项目") || projectName.equals("null")){
			projectName="";
		}
		xssfCell.setCellValue(projectName + "视频制作报价单(" + date + ")");

		rowIndex = createProjectInfoDetail(xssfWorkbook, sheet, ++rowIndex, quotation);

		return rowIndex + 1;
	}

	private int createProjectInfoDetail(XSSFWorkbook xssfWorkbook, XSSFSheet sheet, int rowIndex,
			PmsQuotation quotation) {
		// 样式
		XSSFCellStyle cs = getLeftCellStyle(xssfWorkbook);
		//边框
		setCellBorder(cs);
		// 字体
		XSSFFont xssfFont = xssfWorkbook.createFont();
		xssfFont.setFontHeightInPoints((short) 9);
		cs.setFont(xssfFont);

		XSSFRow xssfRow = null;
		XSSFCell xssfCell = null;
		if(ValidateUtil.isValid(quotation.getProjectId()) || ValidateUtil.isValid(quotation.getProductName())){
			xssfRow = sheet.createRow(rowIndex++);
			xssfCell = xssfRow.createCell(0);
			xssfCell.setCellStyle(cs);
			xssfCell.setCellValue("项目编号:");
			xssfCell = xssfRow.createCell(1);
			xssfCell.setCellStyle(cs);
			xssfCell.setCellValue(quotation.getProjectId());

			xssfCell = xssfRow.createCell(2);
			xssfCell.setCellStyle(cs);
			xssfCell.setCellValue("视频产品线:");
			sheet.addMergedRegion(new CellRangeAddress(rowIndex-1, rowIndex-1, 2, 3));
			xssfCell = xssfRow.createCell(4);
			xssfCell.setCellStyle(cs);
			xssfCell.setCellValue(quotation.getProductName());
			sheet.addMergedRegion(new CellRangeAddress(rowIndex-1, rowIndex-1, 4, 5));
			
			this.createNullCell(xssfRow, cs, 3, 1);
			this.createNullCell(xssfRow, cs, 5, 1);
		}
		

		xssfRow = sheet.createRow(rowIndex++);
		xssfCell = xssfRow.createCell(0);
		xssfCell.setCellStyle(cs);
		xssfCell.setCellValue("评估人:");
		xssfCell = xssfRow.createCell(1);
		xssfCell.setCellStyle(cs);
		xssfCell.setCellValue(quotation.getUpdateUser());

		xssfCell = xssfRow.createCell(2);
		xssfCell.setCellStyle(cs);
		xssfCell.setCellValue("联系电话:");
		sheet.addMergedRegion(new CellRangeAddress(rowIndex-1, rowIndex-1, 2, 3));
		xssfCell = xssfRow.createCell(4);
		xssfCell.setCellStyle(cs);
		xssfCell.setCellValue(quotation.getUpdateUserTel());
		sheet.addMergedRegion(new CellRangeAddress(rowIndex-1, rowIndex-1, 4, 5));
		
		this.createNullCell(xssfRow, cs, 3, 1);
		this.createNullCell(xssfRow, cs, 5, 1);

		return rowIndex;
	}

	// 生成头部
	public int createHead(XSSFWorkbook xssfWorkbook, XSSFSheet sheet, int rowIndex, int headIndex,int num) {

		XSSFCellStyle cs = getLeftCellStyle(xssfWorkbook);
		// 字体
		XSSFFont xssfFont = xssfWorkbook.createFont();
		xssfFont.setFontHeightInPoints((short) 10);
		xssfFont.setBold(true);
		cs.setFont(xssfFont);

		XSSFRow xssfRow = sheet.createRow(rowIndex);

		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 1));

		XSSFCell xssfCell = xssfRow.createCell(0);
		xssfCell.setCellStyle(cs);
		xssfCell.setCellValue(num+"."+head[headIndex][1]);

		cs = getHeadCellStyle(xssfWorkbook, headIndex);
		cs.setBorderBottom(BorderStyle.MEDIUM); // 下边框
		cs.setBorderTop(BorderStyle.MEDIUM); // 上边框
		cs.setBorderLeft(BorderStyle.MEDIUM);//左边框
		
		xssfRow = sheet.createRow(++rowIndex);
		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 1));
		// 列抬头
		
		xssfCell = xssfRow.createCell(0);
		xssfCell.setCellStyle(cs);
		xssfCell.setCellValue("收费项");
		
		this.createNullCell(xssfRow, cs, 1, 1);
		
		cs = getHeadCellStyle(xssfWorkbook, headIndex);
		cs.setBorderBottom(BorderStyle.MEDIUM); // 下边框
		cs.setBorderTop(BorderStyle.MEDIUM); // 上边框

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
		xssfCell.setCellValue("价格");

		cs = getHeadCellStyle(xssfWorkbook, headIndex);
		cs.setBorderBottom(BorderStyle.MEDIUM); // 下边框
		cs.setBorderTop(BorderStyle.MEDIUM); // 上边框
		cs.setBorderRight(BorderStyle.MEDIUM);//右边框
		xssfCell = xssfRow.createCell(6);
		xssfCell.setCellStyle(cs);
		xssfCell.setCellValue("备注");

		// 设置行宽
		sheet.setColumnWidth(1, 20 * 256);
		sheet.setColumnWidth(2, 5 * 256);
		sheet.setColumnWidth(3, 5 * 256);
		sheet.setColumnWidth(6, 34 * 256);

		return rowIndex + 1;
	}

	// 报价单明细
	public int createDataItem(XSSFWorkbook xssfWorkbook, XSSFSheet sheet, int rowIndex, List<PmsQuotationItem> all) {
		// 样式
		XSSFCellStyle nameCs=getNameCellStyle(xssfWorkbook);
		XSSFCellStyle numberCs=getNumberCellStyle(xssfWorkbook);

		int beginMerge = rowIndex;
		for (int i = 0; i < all.size(); i++) {
			PmsQuotationItem type = all.get(i);

			XSSFRow xssfRow = sheet.createRow(rowIndex++);
			XSSFCell xssfCell = xssfRow.createCell(1);
			xssfCell.setCellStyle(nameCs);
			xssfCell.setCellValue(type.getDetailName());

			xssfCell = xssfRow.createCell(2);
			String days=type.getDays() == null ? "/" : ("-1".equals(type.getDays()) ? "整包" : type.getDays() + "");
			if(days.equals("整包")){
				xssfCell.setCellStyle(nameCs);
			}else{
				xssfCell.setCellStyle(numberCs);
			}
			xssfCell.setCellValue(days);

			xssfCell = xssfRow.createCell(3);
			String quantity="-1".equals(type.getQuantity()) ? "整包" : type.getQuantity() + "";
			if(days.equals("整包")){
				xssfCell.setCellStyle(nameCs);
			}else{
				xssfCell.setCellStyle(numberCs);
			}
			xssfCell.setCellValue(quantity);

			xssfCell = xssfRow.createCell(4);
			xssfCell.setCellStyle(numberCs);
			xssfCell.setCellValue(type.getUnitPrice());

			xssfCell = xssfRow.createCell(5);
			xssfCell.setCellStyle(numberCs);
			xssfCell.setCellValue(type.getSum());

			xssfCell = xssfRow.createCell(6);
			xssfCell.setCellStyle(nameCs);
			xssfCell.setCellValue(type.getDescription());
			int height=getRowHeight(type.getDescription());
			if(height>0){
				xssfRow.setHeight((short) (height*20));
				System.out.println(height);
			}

			// 子类名称
			if (i == all.size() - 1 || (type.getItemId() != all.get(i + 1).getItemId())) {
				xssfRow = sheet.getRow(beginMerge);
				xssfCell = xssfRow.createCell(0);
				xssfCell.setCellStyle(nameCs);
				xssfCell.setCellValue(type.getItemName());

				if (rowIndex - 1 > beginMerge) {
					// 合并至当前
					sheet.addMergedRegion(new CellRangeAddress(beginMerge, rowIndex - 1, 0, 0));
				}

				beginMerge = rowIndex;// 从下一行开始合并
			}
		}

		return rowIndex;
	}

	public int createPriceTotal(XSSFWorkbook xssfWorkbook, XSSFSheet sheet, int rowIndex, String name, String value) {
		return createDataInfo(xssfWorkbook, sheet, rowIndex, name, value, 1);
	}

	public int createTaxRate(XSSFWorkbook xssfWorkbook, XSSFSheet sheet, int rowIndex, String name, String value) {
		return createDataInfo(xssfWorkbook, sheet, rowIndex, name, value, 2);
	}

	// 报价单合计
	private int createDataInfo(XSSFWorkbook xssfWorkbook, XSSFSheet sheet, int rowIndex, String name, String value,
			int type) {
		XSSFCellStyle cs = getRightCellStyle(xssfWorkbook);
		setCellBorder(cs);
		XSSFFont xssfFont = xssfWorkbook.createFont();
		xssfFont.setBold(true);
		if(name.equals("小计")){
			xssfFont.setFontHeightInPoints((short) 8);
		}else{
			xssfFont.setFontHeightInPoints((short) 10);
		}
		cs.setFont(xssfFont);
		// 🐥
		Double dv=0d;
		if(ValidateUtil.isValid(value)){
			dv=Double.parseDouble(value);
		}
		
		XSSFDataFormat format = xssfWorkbook.createDataFormat();
		if (type == 1) {
			cs.setDataFormat(format.getFormat("_-¥ * #,##0_-;-¥ * #,##0_-;_-¥ * \"-\"_-;_-@_-"));
		} else {
			cs.setDataFormat(format.getFormat("0%"));
			BigDecimal   b   =   new   BigDecimal(dv);  
			dv   =   b.divide(new BigDecimal(100), 2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
		}

		XSSFRow xssfRow = sheet.createRow(rowIndex++);
		sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 0, 1));
		XSSFCell xssfCell = xssfRow.createCell(0);
		xssfCell.setCellStyle(cs);
		xssfCell.setCellValue(name);
		
		sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 2, 5));
		xssfCell = xssfRow.createCell(2);
		xssfCell.setCellStyle(cs);
		xssfCell.setCellValue(dv);
		
		this.createNullCell(xssfRow, cs, 1, 1);
		this.createNullCell(xssfRow, cs, 3, 3);
		
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
	 * 
	 * @param workbook
	 * @return
	 */
	public XSSFCellStyle getHeadCellStyle(XSSFWorkbook workbook, int color) {
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		switch (color) {
		case 0:
			cellStyle.setFillForegroundColor(new XSSFColor(new Color(253,233,217)));
			break;
		case 1:
			cellStyle.setFillForegroundColor(new XSSFColor(new Color(218,238,243)));
			break;
		case 2:
			cellStyle.setFillForegroundColor(new XSSFColor(new Color(228,223,236)));
			break;
		case 3:
			cellStyle.setFillForegroundColor(new XSSFColor(new Color(235,241,222)));
			break;
		case 4:
			cellStyle.setFillForegroundColor(new XSSFColor(new Color(242,220,219)));
			break;
		default:
			break;
		}

		XSSFFont xssfFont = workbook.createFont();
		xssfFont.setBold(true);
		xssfFont.setFontHeightInPoints((short) 10);
		xssfFont.setFontName("宋体");
		cellStyle.setFont(xssfFont);

		return cellStyle;
	}
	
	public XSSFCellStyle getNameCellStyle(XSSFWorkbook workbook) {
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setWrapText(true);//自动换行

		XSSFFont xssfFont = workbook.createFont();
		xssfFont.setFontHeightInPoints((short) 8);
		xssfFont.setFontName("宋体");
		cellStyle.setFont(xssfFont);
		
		setItemBorder(cellStyle);
		return cellStyle;
	}
	public XSSFCellStyle getNumberCellStyle(XSSFWorkbook workbook) {
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		XSSFFont xssfFont = workbook.createFont();
		xssfFont.setFontHeightInPoints((short) 12);
		cellStyle.setFont(xssfFont);
		
		setItemBorder(cellStyle);
		return cellStyle;
	}

	public XSSFCellStyle getCenterCellStyle(XSSFWorkbook workbook) {
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		return cellStyle;
	}

	public XSSFCellStyle getLeftCellStyle(XSSFWorkbook workbook) {
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		return cellStyle;
	}

	public XSSFCellStyle getRightCellStyle(XSSFWorkbook workbook) {
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		return cellStyle;
	}

	public void setCellBorder(final XSSFCellStyle cellStyle) {
		cellStyle.setBorderBottom(BorderStyle.MEDIUM); // 下边框
		cellStyle.setBorderLeft(BorderStyle.MEDIUM);// 左边框
		cellStyle.setBorderTop(BorderStyle.MEDIUM);// 上边框
		cellStyle.setBorderRight(BorderStyle.MEDIUM);// 右边框
	}
	
	public void setItemBorder(final XSSFCellStyle cellStyle) {
		/*cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框   
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框   
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框   
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
*/		
		cellStyle.setBorderBottom(BorderStyle.THIN); //下边框   
		cellStyle.setBorderLeft(BorderStyle.THIN);//左边框   
		cellStyle.setBorderTop(BorderStyle.THIN);//上边框   
		cellStyle.setBorderRight(BorderStyle.THIN);//右边框
		
		cellStyle.setBottomBorderColor(HSSFColor.GREY_40_PERCENT.index);
		cellStyle.setLeftBorderColor(HSSFColor.GREY_40_PERCENT.index);
		cellStyle.setTopBorderColor(HSSFColor.GREY_40_PERCENT.index);
		cellStyle.setRightBorderColor(HSSFColor.GREY_40_PERCENT.index);
	}
	
	public int getRowHeight(String content) {
		if (ValidateUtil.isValid(content)) {
			// 一行 11
			String[] firstLines = content.split("\n");
			int count = 0;
			for (String firstLine : firstLines) {
				// 计算各行长度
				String[] lines = firstLine.split("\r");
				for (String line : lines) {
					if (ValidateUtil.isValid(line)) {
						int eachSize = line.replaceAll(
								"[\\u4e00-\\u9fa5\u3002\uff1b\uff0c\uff1a\u201c\u201d\uff08\uff09\u3001\uff1f\u300a\u300b]",
								"aa").length();
						count += eachSize / 44;// 一行44个字节
						if (eachSize % 44 > 0) {
							count++;
						}
					}
				}
			}
			if (count > 1) {// 单行，走默认
				return (int) (count * 10.6);
			}
		}

		return 0;
	}

}
