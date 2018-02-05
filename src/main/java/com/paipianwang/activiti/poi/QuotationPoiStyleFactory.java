package com.paipianwang.activiti.poi;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class QuotationPoiStyleFactory {

	private XSSFWorkbook xssfWorkbook;

	public QuotationPoiStyleFactory(XSSFWorkbook xssfWorkbook) {
		this.xssfWorkbook = xssfWorkbook;
	}

	private Map<StyleName, XSSFCellStyle> styles = new HashMap<>();

	enum StyleName {
		PAGE_HEADER,TITLE, HEAD, ITEM, GREEN_ITEM, MONEY, GREEN_MONEY,
		SUM_HEAD, SUM_GREEN_FIRST_ITEM,SUM_ITEM, SUM_GREEN_ITEM, SUM_MONEY, SUM_GREEN_MONEY, SUM_TOTAL,SUM_TOTAL_MONEY,
		INFO_ITEM,INFO_GREEN_ITEM
	};

	public XSSFCellStyle getStyle(StyleName style) {
		if (styles.containsKey(style)) {
			return styles.get(style);
		}
		XSSFCellStyle cellStyle = null;

		switch (style) {
		case PAGE_HEADER:{
			//页头
			cellStyle = getHeaderCellStyle();
			styles.put(StyleName.PAGE_HEADER, cellStyle);
			break;
		}
		case TITLE: {
			//报价单标题
			cellStyle = getTableTitleCellStyle();
			styles.put(StyleName.TITLE, cellStyle);
			break;
		}
		case HEAD: {
			//表头
			cellStyle = getTableHeaderCellStyle();
			setBorder(cellStyle);
			styles.put(StyleName.HEAD, cellStyle);
			break;
		}
		case ITEM: {
			//表格
			cellStyle = getTableItemCellStyle((short) 8);//9
			setBorder(cellStyle);
			styles.put(StyleName.ITEM, cellStyle);
			break;
		}
		case GREEN_ITEM: {
			//绿色表格
			cellStyle = getTableGreenCellStyle((short) 8);//9
			setBorder(cellStyle);
			styles.put(StyleName.GREEN_ITEM, cellStyle);
			break;
		}
		case MONEY: {
			//货币表格
			cellStyle = getMoneyCellStyle();
			setBorder(cellStyle);
			styles.put(StyleName.MONEY, cellStyle);
			break;
		}
		case GREEN_MONEY: {
			//绿色货币表格
			cellStyle = getGreenMoneyCellStyle();
			setBorder(cellStyle);
			styles.put(StyleName.GREEN_MONEY, cellStyle);
			break;
		}
		//统计表格
		case SUM_HEAD: {
			cellStyle = getTableHeaderCellStyle();
			setSumTitleBorder(cellStyle);
			styles.put(StyleName.SUM_HEAD, cellStyle);
			break;
		}
		case SUM_GREEN_FIRST_ITEM:{
			cellStyle = getTableGreenCellStyle((short) 8);//9
			setFirstSumItemBorder(cellStyle);
			styles.put(StyleName.SUM_GREEN_FIRST_ITEM, cellStyle);
			break;
		}
		case SUM_ITEM: {
			cellStyle = getTableItemCellStyle((short) 8);//9
			setSumItemBorder(cellStyle);
			styles.put(StyleName.SUM_ITEM, cellStyle);
			break;
		}
		case SUM_GREEN_ITEM: {
			cellStyle = getTableGreenCellStyle((short) 8);//9
			setSumItemBorder(cellStyle);
			styles.put(StyleName.SUM_GREEN_ITEM, cellStyle);
			break;
		}
		case SUM_MONEY: {
			cellStyle = getMoneyCellStyle();
			setSumItemBorder(cellStyle);
			styles.put(StyleName.SUM_MONEY, cellStyle);
			break;
		}
		case SUM_GREEN_MONEY: {
			cellStyle = getGreenMoneyCellStyle();
			setSumItemBorder(cellStyle);
			styles.put(StyleName.SUM_GREEN_MONEY, cellStyle);
			break;
		}
		//汇总-合计
		case SUM_TOTAL: {
			cellStyle = getTableItemCellStyle((short) 9);//10
			setSumBorder(cellStyle);
			styles.put(StyleName.SUM_TOTAL, cellStyle);
			break;
		}
		//汇总-合计金额
		case SUM_TOTAL_MONEY:{
			cellStyle = getMoneyCellStyle();
			setSumBorder(cellStyle);
			styles.put(StyleName.SUM_TOTAL_MONEY, cellStyle);
			break;
		}
		//信息
		case INFO_ITEM: {
			cellStyle = getTableItemCellStyle((short) 8);//10
			setSumItemBorder(cellStyle);
			styles.put(StyleName.INFO_ITEM, cellStyle);
			break;
		}
		//信息-绿色背景
		case INFO_GREEN_ITEM: {
			cellStyle = getTableGreenCellStyle((short) 8);//10
			setSumItemBorder(cellStyle);
			styles.put(StyleName.INFO_GREEN_ITEM, cellStyle);
			break;
		}

		default:
			break;
		}
		return cellStyle;
	}

	public XSSFCellStyle getHeaderCellStyle() {
		return getCellStyle(HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, (short) 16, "微软雅黑", false);//Helvetica 18
	}

	public XSSFCellStyle getTableTitleCellStyle() {
		return getCellStyle(HorizontalAlignment.CENTER, VerticalAlignment.CENTER, (short) 11, "微软雅黑", false);//Helvetica 12
	}

	// 表头
	public XSSFCellStyle getTableHeaderCellStyle() {
		XSSFCellStyle style = getCellStyle(HorizontalAlignment.LEFT, VerticalAlignment.CENTER, (short) 9,//10
				"微软雅黑", false);//Helvetica
		return style;                     //Arial
	}

	public XSSFCellStyle getTableItemCellStyle(short fontSize) {
		XSSFCellStyle style = getCellStyle(HorizontalAlignment.LEFT, VerticalAlignment.CENTER, fontSize,
				"微软雅黑", false);//Avenir Next
		style.setWrapText(true);// 自动换行
		return style;
	}
	
	public XSSFCellStyle getTableGreenCellStyle(short fontSize) {
		XSSFCellStyle style = getCellStyle(HorizontalAlignment.LEFT, VerticalAlignment.CENTER, fontSize,
				"微软雅黑", false);//Avenir Next
		style.setWrapText(true);// 自动换行
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setFillForegroundColor(new XSSFColor(new Color(244, 249, 247)));
		return style;
	}

	public XSSFCellStyle getMoneyCellStyle() {
		XSSFCellStyle style = getTableItemCellStyle((short) 9);
		XSSFDataFormat format = xssfWorkbook.createDataFormat();
//		style.setDataFormat(format.getFormat("¥#,##0"));
		return style;
	}

	public XSSFCellStyle getGreenMoneyCellStyle() {
		XSSFCellStyle style = getMoneyCellStyle();
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setFillForegroundColor(new XSSFColor(new Color(244, 249, 247)));
		return style;
	}


	// 费用表格
	public XSSFCellStyle setBorder(XSSFCellStyle style) {
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);

		style.setBottomBorderColor(new XSSFColor(new Color(220, 220, 220)));
		style.setLeftBorderColor(new XSSFColor(new Color(220, 220, 220)));
		style.setTopBorderColor(new XSSFColor(new Color(220, 220, 220)));
		style.setRightBorderColor(new XSSFColor(new Color(220, 220, 220)));
		return style;
	}

	// 汇总title
	public XSSFCellStyle setSumTitleBorder(XSSFCellStyle style) {
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);

		style.setBottomBorderColor(new XSSFColor(new Color(146, 146, 146)));
		style.setLeftBorderColor(new XSSFColor(new Color(227, 227, 227)));//new Color(227, 227, 227)));
		style.setTopBorderColor(new XSSFColor(new Color(227, 227, 227)));
		style.setRightBorderColor(new XSSFColor(new Color(227, 227, 227)));
		return style;
	}

	// 汇总item
	public XSSFCellStyle setSumItemBorder(XSSFCellStyle style) {
		style.setBorderBottom(BorderStyle.THIN);//BorderStyle.DASHED);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);//BorderStyle.DASHED);

		style.setBottomBorderColor(new XSSFColor(new Color(227, 227, 227)));
		style.setLeftBorderColor(new XSSFColor(new Color(227, 227, 227)));//173, 173, 173 中
		style.setTopBorderColor(new XSSFColor(new Color(227, 227, 227)));//146, 146, 146))); 深
		style.setRightBorderColor(new XSSFColor(new Color(227, 227, 227)));//173, 173, 173 中
		return style;
	}
	public XSSFCellStyle setFirstSumItemBorder(XSSFCellStyle style) {
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);

		style.setBottomBorderColor(new XSSFColor(new Color(227, 227, 227)));
		style.setLeftBorderColor(new XSSFColor(new Color(227, 227, 227)));
		style.setTopBorderColor(new XSSFColor(new Color(146, 146, 146)));//146, 146, 146
		style.setRightBorderColor(new XSSFColor(new Color(227, 227, 227)));
		return style;
	}

	// 汇总最终价格
	public XSSFCellStyle setSumBorder(XSSFCellStyle style) {
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);

		style.setBottomBorderColor(new XSSFColor(new Color(227, 227, 227)));
		style.setLeftBorderColor(new XSSFColor(new Color(227, 227, 227)));
		style.setTopBorderColor(new XSSFColor(new Color(146, 146, 146)));
		style.setRightBorderColor(new XSSFColor(new Color(227, 227, 227)));
		return style;
	}

	private XSSFCellStyle getCellStyle(HorizontalAlignment horizontal, VerticalAlignment vertical, short fontSize,
			String fontName, boolean isBlod) {
		XSSFCellStyle cellStyle = xssfWorkbook.createCellStyle();
		if (horizontal != null) {
			cellStyle.setAlignment(horizontal);
		}
		if (vertical != null) {
			cellStyle.setVerticalAlignment(vertical);
//			cellStyle.setIndention((short) 1);//缩进
		}
		// 字体
		XSSFFont xssfFont = xssfWorkbook.createFont();
		xssfFont.setFontHeightInPoints(fontSize);
		xssfFont.setFontName(fontName);
		xssfFont.setBold(isBlod);
		cellStyle.setFont(xssfFont);

		return cellStyle;
	}
}
