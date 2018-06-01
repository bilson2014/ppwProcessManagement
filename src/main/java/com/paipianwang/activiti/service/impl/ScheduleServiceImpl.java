package com.paipianwang.activiti.service.impl;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paipianwang.activiti.poi.ScheduleDateUtil;
import com.paipianwang.activiti.poi.SchedulePPTPoiAdapter;
import com.paipianwang.activiti.service.OnlineDocService;
import com.paipianwang.activiti.service.ScheduleService;
import com.paipianwang.activiti.utils.HttpUtil;
import com.paipianwang.pat.common.config.PublicConfig;
import com.paipianwang.pat.common.entity.PmsResult;
import com.paipianwang.pat.common.util.FileUtils;
import com.paipianwang.pat.common.util.JsonUtil;
import com.paipianwang.pat.common.util.PathFormatUtils;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.workflow.entity.PmsProjectFlow;
import com.paipianwang.pat.workflow.entity.PmsSchedule;
import com.paipianwang.pat.workflow.entity.PmsScheduleItem;
import com.paipianwang.pat.workflow.facade.PmsProjectFlowFacade;
import com.paipianwang.pat.workflow.facade.PmsScheduleFacade;

/**
 * 排期表
 */
@Service("scheduleService")
public class ScheduleServiceImpl implements ScheduleService {

	@Autowired
	private PmsScheduleFacade pmsScheduleFacade;
	@Autowired
	private PmsProjectFlowFacade pmsProjectFlowFacade;
	@Autowired
	private SchedulePPTPoiAdapter schedulePPTPoiAdapter;
	@Autowired
	private OnlineDocService onlineDocService;

	/**
	 * 保存/更新
	 */
	@Override
	public PmsResult saveOrUpdateSchedule(PmsSchedule pmsSchedule) {
		PmsResult result = new PmsResult();
		// 对应项目id
		if (!ValidateUtil.isValid(pmsSchedule.getProjectId())) {
			List<PmsProjectFlow> flows = pmsProjectFlowFacade.getByName(pmsSchedule.getProjectName());
			if (ValidateUtil.isValid(flows)) {
				// 存在
				pmsSchedule.setProjectId(flows.get(0).getProjectId());
			}
		}
		
		orderAnUniq(pmsSchedule);
		/*if(ValidateUtil.isValid(pmsSchedule.getItems())){
			//按日期排序
			
			pmsSchedule.getItems().sort(new Comparator<PmsScheduleItem>() {
				@Override
				public int compare(PmsScheduleItem o1, PmsScheduleItem o2) {
					try {
						Date next=new SimpleDateFormat("yyyy-MM-dd").parse(o1.getStart());
						Date current=new SimpleDateFormat("yyyy-MM-dd").parse(o2.getStart());
						return next.compareTo(current);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					return 0;
				}
			});
			
			//日期去重
			for(int i=0;i<pmsSchedule.getItems().size();i++){
				if(i<pmsSchedule.getItems().size()-1 && pmsSchedule.getItems().get(i).getStart().equals(pmsSchedule.getItems().get(i+1).getStart())){
					pmsSchedule.getItems().remove(i);
					i--;
				}
			}
			
		}*/

		// 项目报价单是否存在，存在则更新，否则插入
		if (!ValidateUtil.isValid(pmsSchedule.getProjectId())) {
			// 临时插入
			result = pmsScheduleFacade.insert(pmsSchedule);
		} else {
			PmsSchedule old = pmsScheduleFacade.getByProjectId(pmsSchedule.getProjectId());
			if (old != null) {
				// 更新
				pmsSchedule.setScheduleId(old.getScheduleId());
				result = pmsScheduleFacade.update(pmsSchedule);
			} else {
				result = pmsScheduleFacade.insert(pmsSchedule);
			}
		}

		return result;
	}
	
	private void orderAnUniq(PmsSchedule pmsSchedule){
		if(ValidateUtil.isValid(pmsSchedule.getItems())){
			//按日期排序
			
			pmsSchedule.getItems().sort(new Comparator<PmsScheduleItem>() {
				@Override
				public int compare(PmsScheduleItem o1, PmsScheduleItem o2) {
					try {
						Date next=new SimpleDateFormat("yyyy-MM-dd").parse(o1.getStart());
						Date current=new SimpleDateFormat("yyyy-MM-dd").parse(o2.getStart());
						return next.compareTo(current);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					return 0;
				}
			});
			
			//日期去重
			for(int i=0;i<pmsSchedule.getItems().size();i++){
				if(i<pmsSchedule.getItems().size()-1 && pmsSchedule.getItems().get(i).getStart().equals(pmsSchedule.getItems().get(i+1).getStart())){
					pmsSchedule.getItems().remove(i);
					i--;
				}
			}
			
		}
	}

	/**
	 * 导出
	 */
	@Override
	public void export(PmsSchedule schedule, OutputStream os, HttpServletRequest request) throws Exception{
		if(ValidateUtil.isValid(schedule.getItemContent())){
			schedule.setItems(JsonUtil.fromJsonArray(schedule.getItemContent(), PmsScheduleItem.class));
		}
		orderAnUniq(schedule);
		//格式化排期明细
		List<PmsScheduleItem[][]> items=null;
		try {
			items=ScheduleDateUtil.formatScheduleItemIndex(schedule);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("数据格式异常");
		}
		
		if(!ValidateUtil.isValid(schedule.getItems())){
			//无数据不导出
			return;
		}	
		
		schedulePPTPoiAdapter.setImgPath(request.getServletContext().getRealPath("/resources/images/pptTitle.png"));
		
		HSLFSlideShow ppt = new HSLFSlideShow();
		// 设置幻灯片大小
		ppt.setPageSize(new Dimension(1900, 1140));
		
		// 理解为ppt里的每一页
		HSLFSlide  slide = ppt.createSlide();// 创建幻灯片
		

		// 表头
		schedulePPTPoiAdapter.createTitle(slide, schedule);
		// 图片
		schedulePPTPoiAdapter.createPic(ppt, slide);
		
		for(int i=0;i<items.size();i++){
			
			PmsScheduleItem[][] month=items.get(i);
			if(i!=0){
				slide = ppt.createSlide();// 创建幻灯片
			}
			schedulePPTPoiAdapter.createMsg(slide);
			schedulePPTPoiAdapter.createTable(slide, month);
		}
		String projectName=schedule.getProjectName();
		if(!ValidateUtil.isValid(projectName) || projectName.equals("未命名项目") || projectName.equals("null")){
			projectName="";
		}
		String name=projectName+"排期表"+PathFormatUtils.parse("{yy}{mm}{dd}{hh}{ii}{ss}");
		String basePath=PublicConfig.DOC_TEMP_PATH+File.separator+"temp"+File.separator;
		File sourceFile=new File(basePath+name+".ppt");
		String pdfPath=basePath+name+".pdf";
		String destPath=basePath+name+".zip";
		File pdfFile=new File(pdfPath);
		File zipFile=new File(destPath);
		try {
			ppt.write(new FileOutputStream(sourceFile));
			
			//转pdf
			onlineDocService.convertToPdf(sourceFile,pdfPath);
			//数据压缩
			FileUtils.zipFile(destPath, sourceFile,pdfFile);
			// 数据导出
			HttpUtil.saveTo(new FileInputStream(zipFile), os);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
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

}
