package com.paipianwang.activiti.poi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.paipianwang.pat.workflow.entity.PmsSchedule;
import com.paipianwang.pat.workflow.entity.PmsScheduleItem;

/**
 * 处理排期表数据格式：月-周集合
 */
public class ScheduleDateUtil {

	// 存放在一个列表中
	
	/**
	 * 分月存放--每月前后补全； schedule--month--week--item 获取月份，变更，则新加一月（list)
	 * ftl:第一页遍历schedule第一月 如果size大于1，从第二月开始
	 * <span style="page-break-after:always;"></span> <div class="page"> <table
	 * <thead> 遍历月，生成表格
	 * 
	 *
	 */
	
	public static List<PmsScheduleItem[][]> formatScheduleItemIndex(PmsSchedule schedule) throws Exception {
		// 数据格式组装 每月一组：行-第几周 列-一周7天
		List<PmsScheduleItem[][]> itemList = new ArrayList<PmsScheduleItem[][]>();
				
		List<PmsScheduleItem> items = schedule.getItems();
		if (items != null && items.size() > 0) {
			PmsScheduleItem[][] itemInWeek = null;
			int day=0;
			int week = 0;
			int weekOfMonth=0;
			int month=0;
					
			for (int i = 0; i < items.size(); i++) {
				PmsScheduleItem item=items.get(i);
				// 获取星期几、几日
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(item.getStart()));
				week = calendar.get(Calendar.DAY_OF_WEEK) - 1;// 周日是0
						
				int nMonth=calendar.get(Calendar.MONTH)+1;//0开始
				weekOfMonth=calendar.get(Calendar.WEEK_OF_MONTH);//一个月的第几周 1开始
				day = calendar.get(Calendar.DAY_OF_MONTH);
						
						
				if(nMonth!=month){
					//新建一月
					itemInWeek=initMonth(calendar);
					itemList.add(itemInWeek);
					month=nMonth;
				}
						
				setDay(item, calendar, day, nMonth);
				//去掉内容末尾的\\n换行标记
				if(item.getJobContent().lastIndexOf("\n")+1==item.getJobContent().length()){
					item.setJobContent(item.getJobContent().substring(0, item.getJobContent().lastIndexOf("\n")));
				}
						
				itemInWeek[weekOfMonth-1][week]=item;
			}
		}

		return itemList;
	}
	
	/**
	 * 按日历排版一个月
	 * @param calendar
	 * @return
	 */
	private static PmsScheduleItem[][] initMonth(Calendar calendar){
		int dayOfMonth=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);//一个月的天数
		int weekOfMonth=calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);//一个月的周数
		int month=calendar.get(Calendar.MONTH)+1;
		int year=calendar.get(Calendar.YEAR);
		
		PmsScheduleItem[][] itemInWeek=new PmsScheduleItem[weekOfMonth][7];
		
		Calendar calendar1=calendar;
		calendar1.set(Calendar.DAY_OF_MONTH, 1);
		
		int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;// 周日是0

		
		int x=0;
		int y=0;
		
		for(int i=1;i<=dayOfMonth;i++){
			
			PmsScheduleItem item=new PmsScheduleItem();
			item.setDay(i+"");
			item.setJobContent(" ");
			//设置日期：每月1号：月-日；每年1月1日：年-月-日
			if(i==1){
				item.setDay(month+"-"+item.getDay());
				if(month==1){
					item.setDay(year+"-"+item.getDay());
				}
			}	
			
			x=(week+i-1)/7;
			y=(week+i-1)%7;
			
			itemInWeek[x][y]=item;
		}
		
		return itemInWeek;
	}	
	//设置日期：每月1号：月-日；每年1月1日：年-月-日
	private static boolean setDay(PmsScheduleItem item,Calendar calendar,int day,int nMonth){
		if(day==1){
			if(nMonth==0){
				item.setDay(calendar.get(Calendar.YEAR)+"-"+(nMonth+1)+"-"+day);
			}else{
				item.setDay((nMonth+1)+"-"+day);
			}
			return false;
		}else{
			item.setDay(day+"");
			return true;
		}
	}
}
