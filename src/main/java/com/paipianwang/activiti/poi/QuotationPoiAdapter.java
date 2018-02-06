package com.paipianwang.activiti.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.ClientAnchor.AnchorType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.paipianwang.activiti.poi.QuotationPoiStyleFactory.StyleName;
import com.paipianwang.activiti.poi.entity.QuotationTableCell;
import com.paipianwang.activiti.poi.entity.QuotationTableEntity;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.workflow.entity.PmsQuotation;
import com.paipianwang.pat.workflow.entity.PmsQuotationItem;

/*@Component
@Aspect*/
public class QuotationPoiAdapter {
	
	private XSSFWorkbook xssfWorkbook;
	
	private QuotationPoiStyleFactory quotationPoiStyleFactory;
	private byte[] bytes=null;
	
	
	public QuotationPoiAdapter(XSSFWorkbook xssfWorkbook,String imgPath){
		this.xssfWorkbook=xssfWorkbook;
		quotationPoiStyleFactory=new QuotationPoiStyleFactory(xssfWorkbook);
		
		try {
			FileInputStream stream = new FileInputStream(imgPath);
			bytes = new byte[(int) stream.getChannel().size()];
			// 读取图片到二进制数组
			stream.read(bytes);
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 创建图片表头
	 */
	public int createHeaderImg( XSSFSheet sheet, int rowIndex,int colLength)
			throws Exception {
		XSSFRow xssfRow = sheet.createRow(rowIndex);
		xssfRow.setHeight((short) (31 * 20));
		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, colLength-1));

		XSSFCellStyle cellStyle = quotationPoiStyleFactory.getStyle(StyleName.PAGE_HEADER);
		XSSFCell cell = xssfRow.createCell(0);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("费用报价单");

		// 边框
		cellStyle.setBorderBottom(BorderStyle.THIN); // 下边框
		for (int i = 1; i < colLength; i++) {
			cell = xssfRow.createCell(i);
			cell.setCellStyle(cellStyle);
		}

//		if(colLength==4){
			/*// 图片 TODO 读取图片放入属性中，只读取一次
			FileInputStream stream = new FileInputStream(imgPath);
			byte[] bytes = new byte[(int) stream.getChannel().size()];
			// 读取图片到二进制数组
			stream.read(bytes);*/
			if(bytes!=null && bytes.length>0){
				int pictureIdx = xssfWorkbook.addPicture(bytes, HSSFWorkbook.PICTURE_TYPE_PNG);
				//画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）  
				XSSFDrawing patriarch = sheet.createDrawingPatriarch();
				 //anchor主要用于设置图片的属性  
				XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 255, 255, (short) 0, rowIndex, (short) 1, rowIndex+1);
				anchor.setAnchorType(AnchorType.MOVE_DONT_RESIZE);
				//插入图片
				patriarch.createPicture(anchor, pictureIdx);
			}
			
			
//			stream.close();
//		}
		
		return rowIndex + 1;
	}

	public int createTable(XSSFSheet sheet, int rowIndex, QuotationTableEntity table) {
		
		boolean isSum=table.getType()==1;
		boolean isInfo=table.getType()==2;
		
		// Title
		XSSFRow xssfRow = sheet.createRow(rowIndex++);
		xssfRow.setHeight((short) (31 * 20));

		XSSFCell cell = xssfRow.createCell(0);
		cell.setCellStyle(quotationPoiStyleFactory.getStyle(StyleName.TITLE));
		cell.setCellValue(table.getTitle());
		for (int i = 1; i < table.getColSize(); i++) {
			cell = xssfRow.createCell(i);
		}
		if(isSum){
			sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 0, table.getColSize()));
		}else{
			sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 0, table.getColSize() - 1));
		}
		
		// head
		if (ValidateUtil.isValid(table.getHeadItems())) {
			xssfRow = sheet.createRow(rowIndex++);
			xssfRow.setHeight((short) (23 * 20));
			XSSFCellStyle style = null;
			if(isSum){//汇总
				style=quotationPoiStyleFactory.getStyle(StyleName.SUM_HEAD);
			}else{
				style=quotationPoiStyleFactory.getStyle(StyleName.HEAD);
			}

			for (int i = 0; i < table.getHeadItems().length; i++) {
				cell = xssfRow.createCell(i);
				cell.setCellStyle(style);
				cell.setCellValue(table.getHeadItems()[i]);
			}
			if(isSum){
				int index=table.getHeadItems().length;
				cell = xssfRow.createCell(index);
				cell.setCellStyle(style);
				sheet.addMergedRegion(new CellRangeAddress(rowIndex-1, rowIndex-1, index-1, index));
			}
		}
		// item
		if (table.getItems() != null && table.getItems().size() > 0) {

			XSSFCellStyle itemCell=null;
			for (int i = 0; i < table.getItems().size(); i++) {
				xssfRow = sheet.createRow(rowIndex++);
				xssfRow.setHeight((short) (23 * 20));
				
				boolean isGreen=i % 2 == table.getGreenCell();
				boolean isLast=i==table.getItems().size()-1;
				boolean isFirst=i==0;
				

				for (int j = 0; j < table.getItems().get(i).size(); j++) {
					QuotationTableCell item = table.getItems().get(i).get(j);
					cell = xssfRow.createCell(j);
					//行高			
					if(item.getType()==2){
						int height=getRowHeight(item.getValue()+"");
						if(height>0){
							xssfRow.setHeight((short) (height*20));
						}
					}
					Object value=table.getItems().get(i).get(j).getValue();
					if(value instanceof String){
						cell.setCellValue((String)value);
					}else if(value instanceof Double){
						cell.setCellValue((Double)value);
					}else if(value instanceof Integer){
						cell.setCellValue((Integer)value);
					}
					if(isSum){
						if(isLast){
							itemCell=quotationPoiStyleFactory.getStyle(StyleName.SUM_TOTAL);
						}else if(isFirst){
							itemCell=quotationPoiStyleFactory.getStyle(StyleName.SUM_GREEN_FIRST_ITEM);
						}else{
							if(isGreen){
								itemCell=quotationPoiStyleFactory.getStyle(StyleName.SUM_GREEN_ITEM);
							}else{
								itemCell=quotationPoiStyleFactory.getStyle(StyleName.SUM_ITEM);
							}
						}	
					}else if(isInfo){
//						if(isLast){
//							itemCell=quotationPoiStyleFactory.getStyle(StyleName.SUM_TOTAL);
////						}else if(isFirst){
////							itemCell=quotationPoiStyleFactory.getStyle(StyleName.SUM_GREEN_FIRST_ITEM);
//						}else{
							if(isGreen){
								itemCell=quotationPoiStyleFactory.getStyle(StyleName.INFO_GREEN_ITEM);
							}else{
								itemCell=quotationPoiStyleFactory.getStyle(StyleName.INFO_ITEM);
							}
//						}
					}else{
						if(isGreen){
							itemCell=quotationPoiStyleFactory.getStyle(StyleName.GREEN_ITEM);
						}else{
							itemCell=quotationPoiStyleFactory.getStyle(StyleName.ITEM);
						}
					}
					cell.setCellStyle(itemCell);
					/*boolean isMoney=1 == item.getType();
					
					if (isMoney) {//货币
						cell.setCellValue(Double.parseDouble(table.getItems().get(i).get(j).getValue() + ""));
					} else {
						cell.setCellValue(table.getItems().get(i).get(j).getValue() + "");
					}
					
					if(isSum){
						if(isLast){
							if(isMoney){
								cell.setCellStyle(quotationPoiStyleFactory.getStyle(StyleName.SUM_TOTAL_MONEY));
							}else{
								cell.setCellStyle(quotationPoiStyleFactory.getStyle(StyleName.SUM_TOTAL));
							}
						}else{
							if(isGreen && isMoney){
								cell.setCellStyle(quotationPoiStyleFactory.getStyle(StyleName.SUM_GREEN_MONEY));
							}else if(isGreen && !isMoney){
								cell.setCellStyle(quotationPoiStyleFactory.getStyle(StyleName.SUM_GREEN_ITEM));
							}else if(!isGreen && isMoney){
								cell.setCellStyle(quotationPoiStyleFactory.getStyle(StyleName.SUM_MONEY));
							}else if(!isGreen && !isMoney){
								cell.setCellStyle(quotationPoiStyleFactory.getStyle(StyleName.SUM_ITEM));
							}
						}
					}else{
						if(isGreen && isMoney){
							cell.setCellStyle(quotationPoiStyleFactory.getStyle(StyleName.GREEN_MONEY));
						}else if(isGreen && !isMoney){
							cell.setCellStyle(quotationPoiStyleFactory.getStyle(StyleName.GREEN_ITEM));
						}else if(!isGreen && isMoney){
							cell.setCellStyle(quotationPoiStyleFactory.getStyle(StyleName.MONEY));
						}else if(!isGreen && !isMoney){
							cell.setCellStyle(quotationPoiStyleFactory.getStyle(StyleName.ITEM));
						}
					}*/
				}
				//合并单元格
				if(isSum){
					int index=table.getItems().get(0).size();
					cell = xssfRow.createCell(index);
					cell.setCellStyle(itemCell);
					sheet.addMergedRegion(new CellRangeAddress(rowIndex-1, rowIndex-1, index-1, index));
				}
			}
		}
		return rowIndex;
	}

	/*public static void main(String[] args) {
		// 创建文档
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
		
		QuotationPoiAdapter ada = new QuotationPoiAdapter(xssfWorkbook,"F:\\workspace\\Activiti-Spring\\WebContent\\resources\\images\\excelTitle.png");

		// 数据
		PmsQuotation quotation = ada.getQ();
		LinkedHashMap<String, List<QuotationTableEntity>> sheets = ada.initDataInfo(quotation);
		List<QuotationTableEntity> sums = new ArrayList<>();
		sums.add(ada.initProjectInfo(quotation,"客户"));
		sums.addAll(sheets.get("项目预算-汇总"));
		sums.add(ada.initSumInfo(quotation));
		sheets.put("项目预算-汇总", sums);

		// 明细sheet
		for (String sheetName : sheets.keySet()) {
			// 创建一个新的页
			XSSFSheet sheet = xssfWorkbook.createSheet(sheetName);
			int colLength=0;
			// 设置行宽
			if (sheetName.equals("项目预算-汇总")) {
				sheet.setColumnWidth(0, 23 * 256);
				sheet.setColumnWidth(1, 22 * 256);
				sheet.setColumnWidth(2, 23 * 256);
				sheet.setColumnWidth(3, 22 * 256);
				colLength=4;
			} else {
				sheet.setColumnWidth(0, 21 * 256);
				sheet.setColumnWidth(1, 9 * 256);
				sheet.setColumnWidth(2, 5 * 256);
				sheet.setColumnWidth(3, 12 * 256);
				sheet.setColumnWidth(4, 12 * 256);
				sheet.setColumnWidth(5, 30 * 256);
				colLength=6;
			}

			// 不显示网格线
			sheet.setDisplayGridlines(false);
			int rowIndex = 2;
			try {//"F:\\workspace\\Activiti-Spring\\WebContent\\resources\\images\\pptTitle.png"
				rowIndex = ada.createHeaderImg( sheet, 2,colLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (QuotationTableEntity table : sheets.get(sheetName)) {
				rowIndex = ada.createTable( sheet, rowIndex, table);
				rowIndex++;
			}
		}

		try {
			xssfWorkbook.write(new FileOutputStream(new File("F:\\v3.xlsx")));
			xssfWorkbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		}*/
	
	public void  createFile(PmsQuotation quotation,String userName,String filePath) throws Exception{
		// 数据
		LinkedHashMap<String, List<QuotationTableEntity>> sheets = initDataInfo(quotation);
		List<QuotationTableEntity> sums = new ArrayList<>();
		sums.add(initProjectInfo(quotation,userName));
		sums.addAll(sheets.get("项目预算-汇总"));
		sums.add(initSumInfo(quotation));
		sheets.put("项目预算-汇总", sums);

		// 明细sheet
		for (String sheetName : sheets.keySet()) {
			// 创建一个新的页
			XSSFSheet sheet = xssfWorkbook.createSheet(sheetName);
			int colLength=0;
			// 设置行宽
			if (sheetName.equals("项目预算-汇总")) {
				sheet.setColumnWidth(0, 23 * 256);
				sheet.setColumnWidth(1, 22 * 256);
				sheet.setColumnWidth(2, 23 * 256);
				sheet.setColumnWidth(3, 22 * 256);
				colLength=4;
			} else {
				sheet.setColumnWidth(0, 21 * 256);
				sheet.setColumnWidth(1, 9 * 256);
				sheet.setColumnWidth(2, 5 * 256);
				sheet.setColumnWidth(3, 12 * 256);
				sheet.setColumnWidth(4, 12 * 256);
				sheet.setColumnWidth(5, 30 * 256);
				colLength=6;
			}

			// 不显示网格线
			sheet.setDisplayGridlines(false);
			int rowIndex = 2;
			try {//"F:\\workspace\\Activiti-Spring\\WebContent\\resources\\images\\pptTitle.png"
				rowIndex = createHeaderImg( sheet, 2,colLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (QuotationTableEntity table : sheets.get(sheetName)) {
				rowIndex = createTable( sheet, rowIndex, table);
				rowIndex++;
			}
		}

		try {
			xssfWorkbook.write(new FileOutputStream(new File(filePath)));
			xssfWorkbook.close();
		} catch (Exception e) {
			throw e;
		}
	
	}

	/*private PmsQuotation getQ() {
		PmsQuotation q = new PmsQuotation();
		q.setDiscount("22356.00");
		q.setItemContent(
				"[{\"typeId\":11,\"typeName\":\"创作团队\",\"itemId\":19,\"itemName\":\"导演组\",\"detailId\":39,\"detailName\":\"专业级电影导演\",\"description\":\"10年以上导演经验，有多部作品在省级以上部门获奖，或者有在省级以上电视台播出的。导演过百万以上的广告或宣传片。\",\"unitPrice\":20000.0,\"quantity\":\"1\",\"days\":\"1\",\"sum\":\"20000\",\"fullJob\":0},{\"typeId\":11,\"typeName\":\"创作团队\",\"itemId\":19,\"itemName\":\"导演组\",\"detailId\":131,\"detailName\":\"导演助理\",\"description\":\"从事导演助理工作2年以上。\",\"unitPrice\":2000.0,\"quantity\":\"1\",\"days\":\"1\",\"sum\":\"2000\",\"fullJob\":0},{\"typeId\":11,\"typeName\":\"创作团队\",\"itemId\":164,\"itemName\":\"制片组\",\"detailId\":298,\"detailName\":\"现场制片\",\"description\":\"\",\"unitPrice\":4000.0,\"quantity\":\"1\",\"days\":\"1\",\"sum\":\"4000\"},{\"typeId\":11,\"typeName\":\"创作团队\",\"itemId\":164,\"itemName\":\"制片组\",\"detailId\":168,\"detailName\":\"制片助理\",\"description\":\"\",\"unitPrice\":1500.0,\"quantity\":\"1\",\"days\":\"1\",\"sum\":\"1500\"},{\"typeId\":11,\"typeName\":\"创作团队\",\"itemId\":12,\"itemName\":\"摄影组\",\"detailId\":299,\"detailName\":\"资深摄影师\",\"description\":\"微电影基础版资深摄影师\",\"unitPrice\":5000.0,\"quantity\":\"1\",\"days\":\"1\",\"sum\":\"5000\"},{\"typeId\":11,\"typeName\":\"创作团队\",\"itemId\":12,\"itemName\":\"摄影组\",\"detailId\":132,\"detailName\":\"摄影助理（大）\",\"description\":\"从事摄影助理工作3年以上。\",\"unitPrice\":2000.0,\"quantity\":\"2\",\"days\":\"1\",\"sum\":\"4000\",\"fullJob\":0},{\"typeId\":11,\"typeName\":\"创作团队\",\"itemId\":76,\"itemName\":\"灯光组\",\"detailId\":40,\"detailName\":\"专业灯光师\",\"description\":\"主持过微电影、人物专访、广告的拍摄照明。\",\"unitPrice\":2000.0,\"quantity\":\"1\",\"days\":\"1\",\"sum\":\"2000\",\"fullJob\":0},{\"typeId\":11,\"typeName\":\"创作团队\",\"itemId\":76,\"itemName\":\"灯光组\",\"detailId\":134,\"detailName\":\"灯光助理\",\"description\":\"从事灯光工作3年以上\",\"unitPrice\":1500.0,\"quantity\":\"2\",\"days\":\"1\",\"sum\":\"3000\",\"fullJob\":0},{\"typeId\":11,\"typeName\":\"创作团队\",\"itemId\":76,\"itemName\":\"灯光组\",\"detailId\":149,\"detailName\":\"场工\",\"description\":\"临时场工\",\"unitPrice\":800.0,\"quantity\":\"2\",\"days\":\"1\",\"sum\":\"1600\",\"fullJob\":0},{\"typeId\":11,\"typeName\":\"创作团队\",\"itemId\":82,\"itemName\":\"录音组\",\"detailId\":129,\"detailName\":\"高级录音师\",\"description\":\"取得国家职业资格四级证书。从事录音工作3年以上。\",\"unitPrice\":2000.0,\"quantity\":\"1\",\"days\":\"1\",\"sum\":\"2000\",\"fullJob\":0},{\"typeId\":11,\"typeName\":\"创作团队\",\"itemId\":82,\"itemName\":\"录音组\",\"detailId\":203,\"detailName\":\"录音助理\",\"description\":\"录音助理\",\"unitPrice\":900.0,\"quantity\":\"1\",\"days\":\"1\",\"sum\":\"900\",\"fullJob\":0},{\"typeId\":11,\"typeName\":\"创作团队\",\"itemId\":121,\"itemName\":\"美术化妆组\",\"detailId\":125,\"detailName\":\"高级化妆师\",\"description\":\"1.取得本职业初级职业资格证书后，连续从事本职业工作2年以上。\\r\\n2.取得职业学校、艺术院校、普通中等专业学校相关专业中专以上毕（结）业证书。\\r\\n擅长普通宣传片等类型视频人物装。\",\"unitPrice\":2000.0,\"quantity\":\"1\",\"days\":\"1\",\"sum\":\"2000\",\"fullJob\":0},{\"typeId\":15,\"typeName\":\"拍摄设备\",\"itemId\":22,\"itemName\":\"摄影机\",\"detailId\":70,\"detailName\":\"RED  EPIC-X 6K 摄影机\",\"description\":\"RED 艾比克 6K 摄影机\",\"unitPrice\":5000.0,\"quantity\":\"1\",\"days\":\"1\",\"sum\":\"5000\",\"fullJob\":0},{\"typeId\":15,\"typeName\":\"拍摄设备\",\"itemId\":23,\"itemName\":\"镜头\",\"detailId\":81,\"detailName\":\"卡尔蔡司基本组镜头\",\"description\":\"Carl Zeiss（蔡司）镜头  T1.3  PL口\\r\\n18/25/35/50/85mm镜头\",\"unitPrice\":1500.0,\"quantity\":\"1\",\"days\":\"1\",\"sum\":\"1500\",\"fullJob\":0},{\"typeId\":15,\"typeName\":\"拍摄设备\",\"itemId\":103,\"itemName\":\"辅助设备\",\"detailId\":104,\"detailName\":\"重型轨道\",\"description\":\"重型轨道。包括直轨4节，弯轨3节，轨道车。\",\"unitPrice\":300.0,\"quantity\":\"-1\",\"days\":\"-1\",\"sum\":\"300\",\"fullJob\":1},{\"typeId\":15,\"typeName\":\"拍摄设备\",\"itemId\":103,\"itemName\":\"辅助设备\",\"detailId\":158,\"detailName\":\"易事背减震器\",\"description\":\"Easyrig易事背稳定器\",\"unitPrice\":600.0,\"quantity\":\"1\",\"days\":\"1\",\"sum\":\"600\",\"fullJob\":0},{\"typeId\":15,\"typeName\":\"拍摄设备\",\"itemId\":77,\"itemName\":\"照明设备\",\"detailId\":101,\"detailName\":\"KINO FLO 柔光灯4×40W 5600K\",\"description\":\"美国KINO FLO 柔光灯 3呎 4管×40W 色温5600K。包括：灯头、灯架、火牛、电源10--20米延长线等。\",\"unitPrice\":200.0,\"quantity\":\"2\",\"days\":\"1\",\"sum\":\"400\",\"fullJob\":0},{\"typeId\":15,\"typeName\":\"拍摄设备\",\"itemId\":77,\"itemName\":\"照明设备\",\"detailId\":98,\"detailName\":\"ARRI 聚光灯 650W 3200K\",\"description\":\"色温3200K，功率650W。包括：灯头、灯架、10--20米电源延长线等。\",\"unitPrice\":200.0,\"quantity\":\"1\",\"days\":\"1\",\"sum\":\"200\",\"fullJob\":0},{\"typeId\":15,\"typeName\":\"拍摄设备\",\"itemId\":77,\"itemName\":\"照明设备\",\"detailId\":99,\"detailName\":\"ARRI 聚光灯 1000W 3200K\",\"description\":\"色温3200K，功率1000W。包括：灯头、灯架、10--20米电源延长线等。\",\"unitPrice\":300.0,\"quantity\":\"1\",\"days\":\"1\",\"sum\":\"300\",\"fullJob\":0},{\"typeId\":15,\"typeName\":\"拍摄设备\",\"itemId\":77,\"itemName\":\"照明设备\",\"detailId\":85,\"detailName\":\"ARRI 聚光镝灯 2.5KW 5600K\",\"description\":\"包括：灯头、灯架、火牛、电源10--20米延长线等。\",\"unitPrice\":300.0,\"quantity\":\"1\",\"days\":\"1\",\"sum\":\"300\",\"fullJob\":0},{\"typeId\":15,\"typeName\":\"拍摄设备\",\"itemId\":77,\"itemName\":\"照明设备\",\"detailId\":74,\"detailName\":\"ARRI 聚光镝灯 1.2KW 5600K\",\"description\":\"包括：灯头、灯架、火牛、电源10--20米延长线等。\",\"unitPrice\":200.0,\"quantity\":\"2\",\"days\":\"1\",\"sum\":\"400\",\"fullJob\":0},{\"typeId\":15,\"typeName\":\"拍摄设备\",\"itemId\":77,\"itemName\":\"照明设备\",\"detailId\":73,\"detailName\":\"ARRI 聚光镝灯 575W 5600K\",\"description\":\"包括：灯头、灯架、火牛、电源10--20米延长线等。\",\"unitPrice\":150.0,\"quantity\":\"2\",\"days\":\"1\",\"sum\":\"300\",\"fullJob\":0},{\"typeId\":15,\"typeName\":\"拍摄设备\",\"itemId\":77,\"itemName\":\"照明设备\",\"detailId\":100,\"detailName\":\"ARRI 聚光灯 2000W 3200K\",\"description\":\"色温3200K，功率2000W。包括：灯头、灯架、电源10--20米延长线等。\",\"unitPrice\":400.0,\"quantity\":\"1\",\"days\":\"1\",\"sum\":\"400\",\"fullJob\":0},{\"typeId\":15,\"typeName\":\"拍摄设备\",\"itemId\":25,\"itemName\":\"录音设备\",\"detailId\":46,\"detailName\":\"无线麦（SONY UWP-V1）\",\"description\":\"无线麦（SONY UWP-V1）\",\"unitPrice\":200.0,\"quantity\":\"1\",\"days\":\"1\",\"sum\":\"200\",\"fullJob\":0},{\"typeId\":15,\"typeName\":\"拍摄设备\",\"itemId\":25,\"itemName\":\"录音设备\",\"detailId\":48,\"detailName\":\"罗兰R-44录音机 套装\",\"description\":\"罗兰R-44录音机+RODE NTG2话筒+猪笼+挑杆\",\"unitPrice\":1000.0,\"quantity\":\"1\",\"days\":\"1\",\"sum\":\"1000\",\"fullJob\":0},{\"typeId\":15,\"typeName\":\"拍摄设备\",\"itemId\":25,\"itemName\":\"录音设备\",\"detailId\":329,\"detailName\":\"录音机(sound device664)\",\"description\":\"\",\"unitPrice\":300.0,\"quantity\":\"1\",\"days\":\"1\",\"sum\":\"300\",\"fullJob\":0},{\"typeId\":15,\"typeName\":\"拍摄设备\",\"itemId\":25,\"itemName\":\"录音设备\",\"detailId\":272,\"detailName\":\"录音挑杆(森海塞尔MKH416)\",\"description\":\"1\",\"unitPrice\":150.0,\"quantity\":\"1\",\"days\":\"1\",\"sum\":\"150\",\"fullJob\":0},{\"typeId\":15,\"typeName\":\"拍摄设备\",\"itemId\":26,\"itemName\":\"交通工具\",\"detailId\":173,\"detailName\":\"小型设备车\",\"description\":\"小型剧组设备车，专业拉载设摄影及灯光材，（含司机1人）。\",\"unitPrice\":1500.0,\"quantity\":\"1\",\"days\":\"1\",\"sum\":\"1500\"},{\"typeId\":15,\"typeName\":\"拍摄设备\",\"itemId\":26,\"itemName\":\"交通工具\",\"detailId\":336,\"detailName\":\"摄制组专用车灯光（含司机）\",\"description\":\"\",\"unitPrice\":660.0,\"quantity\":\"1\",\"days\":\"1\",\"sum\":\"660\",\"fullJob\":0},{\"typeId\":15,\"typeName\":\"拍摄设备\",\"itemId\":26,\"itemName\":\"交通工具\",\"detailId\":330,\"detailName\":\"摄制组专用车人员（含司机）\",\"description\":\"\",\"unitPrice\":1500.0,\"quantity\":\"1\",\"days\":\"1\",\"sum\":\"1500\",\"fullJob\":0},{\"typeId\":17,\"typeName\":\"后期制作\",\"itemId\":27,\"itemName\":\"剪辑\",\"detailId\":56,\"detailName\":\"资深剪辑师\",\"description\":\"5年以上的剪辑工作经验，有影视剧剪辑经验作品，会多种后期制作软件。\",\"unitPrice\":5000.0,\"quantity\":\"1\",\"days\":\"5\",\"sum\":\"25000\",\"fullJob\":0},{\"typeId\":17,\"typeName\":\"后期制作\",\"itemId\":176,\"itemName\":\"包装\",\"detailId\":331,\"detailName\":\"电影级包装师\",\"description\":\"\",\"unitPrice\":8000.0,\"quantity\":\"1\",\"days\":\"1\",\"sum\":\"8000\",\"fullJob\":0},{\"typeId\":17,\"typeName\":\"后期制作\",\"itemId\":30,\"itemName\":\"调色\",\"detailId\":52,\"detailName\":\"专业达芬奇调色\",\"description\":\"初级达芬奇调色\",\"unitPrice\":5000.0,\"quantity\":\"1\",\"days\":\"2\",\"sum\":\"10000\",\"fullJob\":0},{\"typeId\":17,\"typeName\":\"后期制作\",\"itemId\":184,\"itemName\":\"机房\",\"detailId\":185,\"detailName\":\"专业剪辑机房\",\"description\":\"\",\"unitPrice\":2000.0,\"quantity\":\"1\",\"days\":\"5\",\"sum\":\"10000\",\"fullJob\":0},{\"typeId\":17,\"typeName\":\"后期制作\",\"itemId\":184,\"itemName\":\"机房\",\"detailId\":186,\"detailName\":\"专业调色机房\",\"description\":\"\",\"unitPrice\":2000.0,\"quantity\":\"1\",\"days\":\"1\",\"sum\":\"2000\",\"fullJob\":0},{\"typeId\":17,\"typeName\":\"后期制作\",\"itemId\":190,\"itemName\":\"字幕\",\"detailId\":191,\"detailName\":\"专业速记及字幕员\",\"description\":\"\",\"unitPrice\":1200.0,\"quantity\":\"1\",\"days\":\"1\",\"sum\":\"1200\"},{\"typeId\":18,\"typeName\":\"配音配乐\",\"itemId\":35,\"itemName\":\"配乐\",\"detailId\":58,\"detailName\":\"定制音乐\",\"description\":\"定制音乐\",\"unitPrice\":5000.0,\"quantity\":\"1\",\"days\":\"1\",\"sum\":\"5000\",\"fullJob\":0},{\"typeId\":18,\"typeName\":\"配音配乐\",\"itemId\":35,\"itemName\":\"配乐\",\"detailId\":189,\"detailName\":\"混音\",\"description\":\"专业混音\",\"unitPrice\":2000.0,\"quantity\":\"1\",\"days\":\"1\",\"sum\":\"2000\",\"fullJob\":0},{\"typeId\":18,\"typeName\":\"配音配乐\",\"itemId\":33,\"itemName\":\"配音\",\"detailId\":62,\"detailName\":\"广告级配音\",\"description\":\"广告级配音\",\"unitPrice\":3000.0,\"quantity\":\"1\",\"days\":\"1\",\"sum\":\"3000\",\"fullJob\":0},{\"typeId\":18,\"typeName\":\"配音配乐\",\"itemId\":187,\"itemName\":\"音棚\",\"detailId\":188,\"detailName\":\"音棚租赁\",\"description\":\"\",\"unitPrice\":1600.0,\"quantity\":\"1\",\"days\":\"1\",\"sum\":\"1600\",\"fullJob\":0},{\"typeId\":193,\"typeName\":\"差旅食宿及杂费\",\"itemId\":195,\"itemName\":\"餐费\",\"detailId\":198,\"detailName\":\"工作人员餐\",\"description\":\"工作日三次工餐及水\",\"unitPrice\":120.0,\"quantity\":\"15\",\"days\":\"1\",\"sum\":\"1800\",\"fullJob\":0},{\"typeId\":193,\"typeName\":\"差旅食宿及杂费\",\"itemId\":196,\"itemName\":\"保险\",\"detailId\":200,\"detailName\":\"一般保险\",\"description\":\"\",\"unitPrice\":100.0,\"quantity\":\"15\",\"days\":\"1\",\"sum\":\"1500\",\"fullJob\":0},{\"typeId\":274,\"typeName\":\"附加服务\",\"itemId\":275,\"itemName\":\"编剧\",\"detailId\":276,\"detailName\":\"病毒剧本编剧团队\",\"description\":\"\",\"unitPrice\":20000.0,\"quantity\":\"0\",\"days\":\"0\",\"sum\":\"0\",\"fullJob\":0},{\"typeId\":274,\"typeName\":\"附加服务\",\"itemId\":277,\"itemName\":\"航拍\",\"detailId\":281,\"detailName\":\"高清航拍\",\"description\":\"\",\"unitPrice\":3000.0,\"quantity\":\"0\",\"days\":\"0\",\"sum\":\"0\",\"fullJob\":0},{\"typeId\":274,\"typeName\":\"附加服务\",\"itemId\":278,\"itemName\":\"演员\",\"detailId\":284,\"detailName\":\"专业演员\",\"description\":\"\",\"unitPrice\":3000.0,\"quantity\":\"0\",\"days\":\"0\",\"sum\":\"0\",\"fullJob\":0},{\"typeId\":274,\"typeName\":\"附加服务\",\"itemId\":279,\"itemName\":\"外景地\",\"detailId\":287,\"detailName\":\"专业外景地\",\"description\":\"0\",\"unitPrice\":3000.0,\"quantity\":\"0\",\"days\":\"0\",\"sum\":\"0\",\"fullJob\":0},{\"typeId\":274,\"typeName\":\"附加服务\",\"itemId\":280,\"itemName\":\"外文配音\",\"detailId\":290,\"detailName\":\"广播级外文配音\",\"description\":\"每分钟1500，每增加1分钟，增加相应的费用\",\"unitPrice\":1500.0,\"quantity\":\"0\",\"days\":\"0\",\"sum\":\"0\",\"fullJob\":0}]");
		q.setProductName("企业宣传片-标准版");
		q.setProjectId("20170011214");
		q.setSubTotal("134110.00");
		q.setTaxRate("6");
		q.setTotal("119800.60");

		try {
			q.setItems(JsonUtil.fromJsonArray(q.getItemContent(), PmsQuotationItem.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return q;
	}*/

	// 项目信息
	public QuotationTableEntity initProjectInfo(PmsQuotation quotation,String userName) {

		QuotationTableCell[][] cells = new QuotationTableCell[6][4];
		cells[0][0] = new QuotationTableCell("Job No 项目编号");
		cells[0][1] = new QuotationTableCell(quotation.getProjectId());
		cells[0][2] = new QuotationTableCell("Product 商品");
		cells[0][3] = new QuotationTableCell(quotation.getProductName());

		cells[1][0] = new QuotationTableCell("Duration 秒数");
		cells[1][1] = new QuotationTableCell("");
		cells[1][2] = new QuotationTableCell("Title 篇名");
		cells[1][3] = new QuotationTableCell(quotation.getProjectName());
		cells[2][0] = new QuotationTableCell("Client 客户");
		cells[2][1] = new QuotationTableCell(userName);
		cells[2][2] = new QuotationTableCell("Executive Producer 监制");
		cells[2][3] = new QuotationTableCell("");
		
		cells[3][0] = new QuotationTableCell("Languange 语言");
		cells[3][1] = new QuotationTableCell("");
		cells[3][2] = new QuotationTableCell("Director 导演");
		cells[3][3] = new QuotationTableCell("");
		
		cells[4][0] = new QuotationTableCell("Shooting At 拍摄地点");
		cells[4][1] = new QuotationTableCell("");
		cells[4][2] = new QuotationTableCell("CG production 三维制作");
		cells[4][3] = new QuotationTableCell("");
		
		cells[5][0] = new QuotationTableCell("Post Production 后期地点");
		cells[5][1] = new QuotationTableCell("");
		cells[5][2] = new QuotationTableCell("Footage 资料片");
		cells[5][3] = new QuotationTableCell("");

		List<List<QuotationTableCell>> cellList = new ArrayList<>();
		for (QuotationTableCell[] cell : cells) {
			cellList.add(Arrays.asList(cell));
		}

		QuotationTableEntity table = new QuotationTableEntity("视频制作费清单", new String[] {}, cellList, 0,2);

		return table;
	}

	String[] sumTitle = new String[] { "Item", "Cost (￥)", "Remark" };

	// 费用汇总
	public QuotationTableEntity initSumInfo(PmsQuotation quotation) {

		// TODO main运行下为空的情况
		BigDecimal tax = new BigDecimal(quotation.getSubTotal()).multiply(new BigDecimal(quotation.getTaxRate()))
				.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
		BigDecimal sum = new BigDecimal(quotation.getTotal()).add(new BigDecimal(quotation.getDiscount()));

		List<List<QuotationTableCell>> cellList = new ArrayList<>();
		cellList.add(Arrays.asList(new QuotationTableCell("1,2项费用合计"),
				new QuotationTableCell(Double.parseDouble(quotation.getSubTotal()), 1), new QuotationTableCell("")));
		cellList.add(Arrays.asList(new QuotationTableCell(quotation.getTaxRate() + "%营业加值税"),
				new QuotationTableCell(tax.doubleValue(), 1), new QuotationTableCell("")));
		cellList.add(Arrays.asList(new QuotationTableCell("总计"), new QuotationTableCell(sum.doubleValue(), 1),
				new QuotationTableCell("")));
		cellList.add(Arrays.asList(new QuotationTableCell("最终优惠价格"), new QuotationTableCell(Double.parseDouble(quotation.getTotal()), 1),
				new QuotationTableCell("")));

		QuotationTableEntity table = new QuotationTableEntity("TOTAL COST 费用汇总", sumTitle, cellList, 0,1);

		return table;
	}

	// 报价信息
	public LinkedHashMap<String, List<QuotationTableEntity>> initDataInfo(PmsQuotation quotation) {
		List<PmsQuotationItem> items = quotation.getItems();
		// 报价明细层级整理
		List<PmsQuotationItem> table = null;
		LinkedHashMap<String, List<List<PmsQuotationItem>>> types = new LinkedHashMap<>();

		List<List<PmsQuotationItem>> typeItems = null;
		for (int i = 0; i < items.size(); i++) {
			PmsQuotationItem item = items.get(i);
			if (i == 0 || (item.getItemId() != items.get(i - 1).getItemId())) {
				table = new ArrayList<>();
				if (i == 0 || item.getTypeId() != items.get(i - 1).getTypeId()) {
					typeItems = new ArrayList<>();
					types.put(item.getTypeName(), typeItems);
				}
				typeItems.add(table);
			}
			table.add(item);
		}

		LinkedHashMap<String, List<QuotationTableEntity>> tables = new LinkedHashMap<>();

		// 前期制作表
		QuotationTableEntity earlyTable = new QuotationTableEntity("PRODUCTION 制作费", sumTitle, new ArrayList<>(), 0,1);
		// 后期制作表
		QuotationTableEntity laterTable = new QuotationTableEntity("POST PRODUCTION 后期制作", sumTitle, new ArrayList<>(),
				0,1);
		tables.put("项目预算-汇总", Arrays.asList(earlyTable, laterTable));

		// 计算各子类小计
		for (String typeName : types.keySet()) {
			List<List<PmsQuotationItem>> type = types.get(typeName);
			QuotationTableEntity gatherTable = laterTable;
			if (typeName.equals("创作团队") || typeName.equals("拍摄设备") || typeName.equals("拍摄场地")) {
				gatherTable = earlyTable;
			}
			// 一个大类一个sheet
			List<QuotationTableEntity> sheet = new ArrayList<>();
			tables.put("项目预算-" + typeName, sheet);

			for (int i = 0; i < type.size(); i++) {
				List<PmsQuotationItem> subType = type.get(i);
				// 一个子类一个table
				String subTypeName = subType.get(0).getItemName();
				BigDecimal sum = new BigDecimal(0);

				QuotationTableEntity subTable = new QuotationTableEntity();
				sheet.add(subTable);
				if (i == 0) {
					subTable.setTitle(typeName);
				}
				subTable.setGreenCell(0);
				subTable.setHeadItems(
						new String[] { subTypeName, "UNIT (￥)", "QTY", "TERMS (D/C)", "QUOTN (￥)", "REMARK" });
				subTable.setColSize(subTable.getHeadItems().length);
				subTable.setType(0);
				for (PmsQuotationItem item : subType) {
					sum = sum.add(new BigDecimal(item.getSum()));
					List<QuotationTableCell> itemRow = new ArrayList<>();
					itemRow.add(new QuotationTableCell(item.getDetailName()));
					itemRow.add(new QuotationTableCell(item.getUnitPrice()));
					itemRow.add(new QuotationTableCell("-1".equals(item.getQuantity())?"CASE":Integer.parseInt(item.getQuantity()), 1));
					itemRow.add(new QuotationTableCell("-1".equals(item.getDays())?"CASE":Integer.parseInt(item.getDays())));
					itemRow.add(new QuotationTableCell(Double.parseDouble(item.getSum()), 1));
					itemRow.add(new QuotationTableCell(item.getDescription(),2));
					subTable.getItems().add(itemRow);
				}
				// 子类合计
				List<QuotationTableCell> sumRow = new ArrayList<>();
				sumRow.add(new QuotationTableCell("小计"));
				sumRow.add(new QuotationTableCell(""));
				sumRow.add(new QuotationTableCell(""));
				sumRow.add(new QuotationTableCell(""));
				sumRow.add(new QuotationTableCell(sum.doubleValue(), 1));
				sumRow.add(new QuotationTableCell(""));
				subTable.getItems().add(sumRow);

				// 子类合计组新表
				List<QuotationTableCell> gatherRow = new ArrayList<>();
				gatherRow.add(new QuotationTableCell(subTypeName));
				gatherRow.add(new QuotationTableCell(sum.doubleValue(), 1));
				gatherRow.add(new QuotationTableCell(""));
				gatherTable.getItems().add(gatherRow);
			}
		}
		// 前期制作合计
		sumGatherTable(earlyTable, "制作费用合计");
		// 后期制作合计
		sumGatherTable(laterTable, "后期制作费用合计");

		return tables;
	}

	private void sumGatherTable(QuotationTableEntity gatherTable, String gatherName) {
		if (ValidateUtil.isValid(gatherTable.getItems())) {
			BigDecimal sum = new BigDecimal(0);
			for (List<QuotationTableCell> cells : gatherTable.getItems()) {
				sum = sum.add(new BigDecimal(cells.get(1).getValue() + ""));
			}
			List<QuotationTableCell> sumRow = new ArrayList<>();
			sumRow.add(new QuotationTableCell(gatherName));
			sumRow.add(new QuotationTableCell(sum.doubleValue(), 1));
			sumRow.add(new QuotationTableCell(""));
			gatherTable.getItems().add(sumRow);
		}
	}
	/*
	 * public static void main(String[] args) { try { Workbook
	 * wb=WorkbookFactory.create(new File("F://v3.xlsx")); if(wb instanceof
	 * XSSFWorkbook ){ XSSFSheet sheet = (XSSFSheet) wb.getSheetAt(0); for
	 * (POIXMLDocumentPart dr : sheet.getRelations()) { if (dr instanceof
	 * XSSFDrawing) { XSSFDrawing drawing = (XSSFDrawing) dr; List<XSSFShape>
	 * shapes = drawing.getShapes(); for (XSSFShape shape : shapes) {
	 * XSSFPicture pic = (XSSFPicture) shape; XSSFClientAnchor anchor =
	 * pic.getPreferredSize(); System.out.println(anchor.getDx1());
	 * System.out.println(anchor.getDx2()); System.out.println(anchor.getDy1());
	 * System.out.println(anchor.getDy2());
	 * System.out.println(anchor.getRow1()); } } } }
	 * 
	 * } catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } }
	 */



	public int getRowHeight(String content) {
		if (ValidateUtil.isValid(content)) {
			// 一行 11
			String[] firstLines = content.split("\n");
			int count = 0;
			String regex = "[\\u4e00-\\u9fa5\u3002\uff1b\uff0c\uff1a\u201c\u201d\uff08\uff09\u3001\uff1f\u300a\u300b]";
			Pattern pattern = Pattern.compile(regex);
			for (String firstLine : firstLines) {
				// 计算各行长度
				String[] lines = firstLine.split("\r");
				for (String line : lines) {
					if (ValidateUtil.isValid(line)) {
						Matcher matcher = pattern.matcher(line);
						int ccount = 0;
						while (matcher.find()) {
							ccount++;
						}

						int eachSize = line.length() - ccount + ccount * 39 / 21 + 1;
						count += eachSize / 39;// 一行39个字节
						if (eachSize % 39 > 0) {
							count++;
						}
					}
				}
			}
			if (count > 1) {// 单行，走默认
				return (int) (count * 13);
			}
		}

		return 0;
	}
}
