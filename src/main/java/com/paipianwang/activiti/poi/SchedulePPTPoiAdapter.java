package com.paipianwang.activiti.poi;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.io.FileInputStream;
import java.util.List;

import org.apache.poi.sl.usermodel.PictureData.PictureType;
import org.apache.poi.sl.usermodel.TableCell.BorderEdge;
import org.apache.poi.sl.usermodel.TextParagraph.TextAlign;
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

import com.paipianwang.pat.workflow.entity.PmsSchedule;
import com.paipianwang.pat.workflow.entity.PmsScheduleItem;

@Component
@Aspect
public class SchedulePPTPoiAdapter {

	public void createTitle(XSLFSlide slide, PmsSchedule schedule) {
		// 创建文本框
		XSLFTextBox textBox = slide.createTextBox();
		// 4个参数分别为 x , y , width , height
		textBox.setAnchor(new Rectangle2D.Double(50, 20, 30 * 40, 2 * 40));
		// 添加文本进文本段落
		XSLFTextRun xslfTextRun = textBox.addNewTextParagraph().addNewTextRun();
		xslfTextRun.setText("《" + schedule.getProjectName() + "》制作周期预估表");
		// 样式
		xslfTextRun.setFontColor(Color.DARK_GRAY);
		xslfTextRun.setFontSize(48.0);
		xslfTextRun.setFontFamily("Lato");
		
		//横线
		
		/*XSLFShape shape;
		Line line = new Line();

		line.setAnchor(new java.awt.Rectangle(x,y,x1,y1); line.setLineColor(new Color(0, 128, 0));
		line.setLineStyle(Line.LINE_DOUBLE);
		slide.addShape(shape);
		XSLFAutoShape autoShape=new XSLFShapeContainer();*/
	}
	
	public void createMsg(XSLFSlide slide) {
		// 创建文本框
		XSLFTextBox textBox = slide.createTextBox();
		// 4个参数分别为 x , y , width , height
		textBox.setAnchor(new Rectangle2D.Double(100, 1020, 1700, 40));
		// 添加文本进文本段落
		XSLFTextRun xslfTextRun = textBox.addNewTextParagraph().addNewTextRun();
		xslfTextRun.setText("本时间表为预排，基于每一环节的按时确认方可顺利执行，否则制作进度都会受确认环节或其他不可抗因素的变化而相应变化。");
		// 样式
		xslfTextRun.setFontColor(Color.LIGHT_GRAY);
		xslfTextRun.setFontSize(30.0);
		xslfTextRun.setFontFamily("Lato");
	}

	public void createPic(XMLSlideShow ppt, XSLFSlide slide) throws Exception {
		// 插入图片
		byte[] pictureData = IOUtils.toByteArray(new FileInputStream("F://paipianwang.png"));
		/** 添加图片,返回索引 **/

		XSLFPictureData pictureIndex = ppt.addPicture(pictureData, PictureType.PNG);
		/** 创建图片 **/
		XSLFPictureShape pictureShape = slide.createPicture(pictureIndex);
		/** 设置图片的位置 四个参数分别为 x y width height **/
		pictureShape.setAnchor(new java.awt.Rectangle(1640, 50, 213, 71));
	}

	public void createTable(XSLFSlide slide, List<PmsScheduleItem[]> itemLists) throws Exception {
		XSLFTable table = slide.createTable();// 创建表格
		table.setAnchor(new Rectangle2D.Double(100, 160, 1700, 0));
		// 表头
		XSLFTableRow tableRow = table.addRow();
		String[] titles = new String[] { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		tableRow.setHeight(40);

		for (int i = 0; i < titles.length; i++) {
			String title = titles[i];
			XSLFTableCell tableCell = tableRow.addCell();
			XSLFTextParagraph p = tableCell.addNewTextParagraph();
			XSLFTextRun tr = p.addNewTextRun();
			tr.setText(title);

			tr.setFontSize(30.0);
			tr.setBold(true);
			tr.setFontFamily("Lato");
			tr.setFontColor(Color.white);

			setTableCellFont(tableCell);
			tableCell.setFillColor(new Color(255, 99, 71));
			p.setTextAlign(TextAlign.CENTER);
			tableCell.setVerticalAlignment(VerticalAlignment.MIDDLE);

			table.setColumnWidth(i, 244);
		}

		for (PmsScheduleItem[] itemList : itemLists) {
			// 一周 2行
			XSLFTableRow tableRow1 = table.addRow();
			XSLFTableRow tableRow2 = table.addRow();

			tableRow1.setHeight(40);
			tableRow2.setHeight(120);
			for (PmsScheduleItem item : itemList) {
				if(item==null){
					break;
				}
				// 日期
				XSLFTableCell tableCell = tableRow1.addCell();
				XSLFTextParagraph p = tableCell.addNewTextParagraph();
				XSLFTextRun tr = p.addNewTextRun();
				tr.setText(item.getDay() + "");

				tr.setFontSize(30.0);
				tr.setFontFamily("Lato");
				tr.setFontColor(Color.DARK_GRAY);

				setTableCellFont(tableCell);
				p.setTextAlign(TextAlign.CENTER);
				tableCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
				// 工作内容
				tableCell = tableRow2.addCell();
				p = tableCell.addNewTextParagraph();
				tr = p.addNewTextRun();
				tr.setText(item.getJobContent());
				tableCell.setWordWrap(true);

				tr.setFontSize(30.0);
				tr.setFontFamily("Helvetica Neue Medium");
				tr.setFontColor(Color.DARK_GRAY);

				setTableCellFont(tableCell);
				p.setTextAlign(TextAlign.CENTER);
				tableCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
			}
		}

	}

	public void setTableCellFont(XSLFTableCell tableCell) {
		tableCell.setBorderColor(BorderEdge.bottom, Color.LIGHT_GRAY);
		tableCell.setBorderColor(BorderEdge.left, Color.LIGHT_GRAY);
		tableCell.setBorderColor(BorderEdge.top, Color.LIGHT_GRAY);
		tableCell.setBorderColor(BorderEdge.right, Color.LIGHT_GRAY);
		// tableCell.setBorderBottom(1);
	}



}
