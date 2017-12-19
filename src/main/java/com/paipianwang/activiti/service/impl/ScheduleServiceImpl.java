package com.paipianwang.activiti.service.impl;

import java.awt.Dimension;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paipianwang.activiti.poi.ScheduleDateUtil;
import com.paipianwang.activiti.poi.SchedulePPTPoiAdapter;
import com.paipianwang.activiti.service.ScheduleService;
import com.paipianwang.pat.common.entity.PmsResult;
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

	/**
	 * 导出
	 */
	@Override
	public void export(PmsSchedule schedule, OutputStream os, HttpServletRequest request) throws Exception{

		//格式化排期明细
		try {
			ScheduleDateUtil.formatScheduleItem(schedule);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("数据格式异常");
		}
		
		if(!ValidateUtil.isValid(schedule.getItems())){
			//无数据不导出
			return;
		}	
		
		XMLSlideShow ppt = new XMLSlideShow();
		// 设置幻灯片大小
		ppt.setPageSize(new Dimension(1900, 1100));
		// 理解为ppt里的每一页
		XSLFSlide slide = ppt.createSlide();// 创建幻灯片

		// 表头
		schedulePPTPoiAdapter.createTitle(slide, schedule);
		// 图片
		schedulePPTPoiAdapter.createPic(ppt, slide);
		
		List<PmsScheduleItem[]> lists=schedule.getItemLists();
		// 表格
		int begin=0;
		int end=0;
		do {
			schedulePPTPoiAdapter.createMsg(slide);
			end=begin+5;
			if(end>=lists.size()){
				schedulePPTPoiAdapter.createTable(slide, schedule.getItemLists().subList(begin,lists.size()));
			}else{
				schedulePPTPoiAdapter.createTable(slide, schedule.getItemLists().subList(begin,end));
				slide = ppt.createSlide();// 创建幻灯片
			}
			begin=end;
		} while (begin<lists.size());
		
		try {
			ppt.write(os);
		} catch (Exception e) {
			
		}
	
	}

}
