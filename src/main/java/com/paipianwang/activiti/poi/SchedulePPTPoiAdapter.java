package com.paipianwang.activiti.poi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.sl.usermodel.PictureData.PictureType;
import org.apache.poi.sl.usermodel.TableCell.BorderEdge;
import org.apache.poi.sl.usermodel.TextParagraph.TextAlign;
import org.apache.poi.hslf.usermodel.HSLFPictureData;
import org.apache.poi.hslf.usermodel.HSLFShape;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hslf.usermodel.HSLFTable;
import org.apache.poi.hslf.usermodel.HSLFTableCell;
import org.apache.poi.hslf.usermodel.HSLFTextBox;
import org.apache.poi.hslf.usermodel.HSLFTextParagraph;
import org.apache.poi.hslf.usermodel.HSLFTextRun;
import org.apache.poi.sl.draw.DrawTableShape;
import org.apache.poi.sl.usermodel.VerticalAlignment;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTable;
import org.apache.poi.xslf.usermodel.XSLFTableCell;
import org.apache.poi.xslf.usermodel.XSLFTableRow;
import org.apache.poi.xslf.usermodel.XSLFTextBox;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.paipianwang.pat.common.config.PublicConfig;
import com.paipianwang.pat.common.util.JsonUtil;
import com.paipianwang.pat.common.util.PathFormatUtils;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.workflow.entity.PmsSchedule;
import com.paipianwang.pat.workflow.entity.PmsScheduleItem;

@Component
@Aspect
public class SchedulePPTPoiAdapter {

	private String imgPath;
	
	public void createTitle(HSLFSlide slide, PmsSchedule schedule) {
		// 创建文本框
//		TextBox textBox = slide.addTitle();//标题
		
		HSLFTextBox textBox=new HSLFTextBox();//slide.createTextBox();//此种总会居中
		// 4个参数分别为 x , y , width , height
		textBox.setAnchor(new Rectangle2D.Double(50, 20, 30 * 40, 2 * 40));
		// 添加文本进文本段落
		String projectName=schedule.getProjectName();
		if(!ValidateUtil.isValid(projectName) || projectName.equals("未命名项目") || projectName.equals("null")){
			projectName="";
		}
		textBox.setText("《" + schedule.getProjectName() + "》制作周期预估表");
		
		HSLFTextParagraph tp=textBox.getTextParagraphs().get(0);
		tp.setLeftMargin(0.);
		
		HSLFTextRun textReturn=tp.getTextRuns().get(0);
		// 样式
		textReturn.setFontColor(Color.DARK_GRAY);
		textReturn.setFontSize(48.0);
		textReturn.setFontFamily("Lato");
		
		slide.addShape(textBox);
		
		
//		slide.addShape(textBox);
		//横线
		
		/*XSLFShape shape;
		Line line = new Line();

		line.setAnchor(new java.awt.Rectangle(x,y,x1,y1); line.setLineColor(new Color(0, 128, 0));
		line.setLineStyle(Line.LINE_DOUBLE);
		slide.addShape(shape);
		XSLFAutoShape autoShape=new XSLFShapeContainer();*/
	}
	
	public void createMsg(HSLFSlide slide) {
		// 创建文本框
		HSLFTextBox textBox = new HSLFTextBox();
		// 4个参数分别为 x , y , width , height
		textBox.setAnchor(new Rectangle2D.Double(100, 1020, 1700, 40));
		// 添加文本进文本段落
		HSLFTextRun textRun = textBox.getTextParagraphs().get(0).getTextRuns().get(0);
		textRun.setText("本时间表为预排，基于每一环节的按时确认方可顺利执行，否则制作进度都会受确认环节或其他不可抗因素的变化而相应变化。");
		// 样式
		textRun.setFontColor(Color.LIGHT_GRAY);
		textRun.setFontSize(30.0);
		textRun.setFontFamily("Lato");
		
		slide.addShape(textBox);
	}

	public void createPic(HSLFSlideShow ppt, HSLFSlide slide) throws Exception {
		// 插入图片
		byte[] pictureData = IOUtils.toByteArray(new FileInputStream(imgPath));
				//"F://paipianwang.png"));
		/** 添加图片,返回索引 **/

		/*XSLFPictureData pictureIndex = ppt.addPicture(pictureData, PictureType.PNG);
		*//** 创建图片 **//*
		XSLFPictureShape pictureShape = slide.createPicture(pictureIndex);
		*//** 设置图片的位置 四个参数分别为 x y width height **//*
		pictureShape.setAnchor(new java.awt.Rectangle(1640, 50, 213, 71));*/
		
		HSLFPictureData picData=ppt.addPicture(pictureData, PictureType.PNG);
		HSLFShape png=slide.createPicture(picData);
		png.setAnchor(new java.awt.Rectangle(1640, 50, 213, 71));
//		slide.addShape(png);
		
	}
	
	public void createTable(HSLFSlide slide, PmsScheduleItem[][] itemLists) throws Exception {
		// 创建表格
		HSLFTable table =slide.createTable(itemLists.length*2+1,7);
//		table.setAnchor(new Rectangle2D.Double(100, 160, 1700, 40*(itemLists.length+1)+120*itemLists.length));
		// 表头
		String[] titles = new String[] { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		
		for (int i = 0; i < titles.length; i++) {
			String title = titles[i];
			HSLFTableCell tableCell = table.getCell(0, i);
			tableCell.setText(title);
			
			HSLFTextRun tr=tableCell.getTextParagraphs().get(0).getTextRuns().get(0);
			tr.setFontSize(30.0);
			tr.setBold(true);
			tr.setFontFamily("Lato");
			tr.setFontColor(Color.white);
			
			tableCell.setHorizontalCentered(true);
			tableCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
			tableCell.setFillColor(new Color(255, 99, 71));
			
			table.setColumnWidth(i, 243);  
		}

		for (int i=0;i<itemLists.length;i++) {
			PmsScheduleItem[] itemList=itemLists[i];
			// 一周 2行
			for (int j=0;j<itemList.length;j++) {
				PmsScheduleItem item = itemList[j];
				if(item==null){
					//空样式
					item=new PmsScheduleItem();
				}
				// 日期
				HSLFTableCell tableCell = table.getCell(i*2+1, j);
				
				HSLFTextRun tr=tableCell.getTextParagraphs().get(0).getTextRuns().get(0);
				tr.setFontSize(30.0);
				tr.setFontFamily("Lato");
				tr.setFontColor(Color.DARK_GRAY);
				tr.setText(item.getDay()==null?" ":item.getDay() + "");
				
				tableCell.setHorizontalCentered(true);
				tableCell.setVerticalAlignment(VerticalAlignment.MIDDLE);				
			}
			
			for (int j=0;j<itemList.length;j++) {
				PmsScheduleItem item = itemList[j];
				if(item==null){
					//空样式
					item=new PmsScheduleItem();
				}				
				// 工作内容
				HSLFTableCell tableCell = table.getCell(i*2+2, j);
//				tableCell.setText(item.getJobContent()==null?" ":item.getJobContent());
//				tableCell.setWordWrap(true);
				
				HSLFTextRun tr=tableCell.getTextParagraphs().get(0).getTextRuns().get(0);
				tr.setFontSize(30.0);
				tr.setFontFamily("Helvetica Neue Medium");
				tr.setFontColor(Color.DARK_GRAY);
				if(ValidateUtil.isValid(item.getJobContent())){
					tr.setText(item.getJobContent());
				}else{
					tr.setText("\n\n");
				}
				
				tableCell.setHorizontalCentered(true);
				tableCell.setVerticalAlignment(VerticalAlignment.MIDDLE);	
			}
		}
		
		
		DrawTableShape dts1 = new DrawTableShape(table);  
	    dts1.setAllBorders(1.0, Color.LIGHT_GRAY);  
	    table.moveTo(100., 160.);
	}
	
	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	
}
