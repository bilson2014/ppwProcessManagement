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
	public static void formatScheduleItem(PmsSchedule schedule) throws Exception {
		// 数据格式组装
		List<PmsScheduleItem[]> itemList = new ArrayList<PmsScheduleItem[]>();
		schedule.setItemLists(itemList);
		// 每组7个存放
		List<PmsScheduleItem> items = schedule.getItems();
		if (items != null && items.size() > 0) {
			PmsScheduleItem[] itemInWeek = null;
			int week = 0;
			int day = 0;
			int weekOfYear=0;
			int month=0;
			for (int i = 0; i < items.size(); i++) {
				// 获取星期几、几日
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(items.get(i).getStart()));
				week = calendar.get(Calendar.DAY_OF_WEEK) - 1;// 周日是0
				day = calendar.get(Calendar.DAY_OF_MONTH);
				
				int nweekOfYear=calendar.get(Calendar.WEEK_OF_YEAR);//从周一开始算一周，故还需判断week
				int nMonth=calendar.get(Calendar.MONTH);
				
				boolean hasFirstMonth=true;
				
				if (week == 0 || i == 0 || weekOfYear!=nweekOfYear || month!=nMonth) {
					// 新开始一周，开始一行
					itemInWeek = new PmsScheduleItem[7];
					itemList.add(itemInWeek);
					
					// 第一周前几天空着
					for (int w = week-1; w >=0; w--) {
						PmsScheduleItem nItem = new PmsScheduleItem();
						int each=day - week + w;
						if(each<1){
							break;
						}
						if((w==0 && i!=0) || each==1){//周日或1号可能是新的一月
							hasFirstMonth=setDay(nItem, calendar, each,month,nMonth);
						}else{
							nItem.setDay(each+"");
						}
						
						
						nItem.setJobContent("");
						itemInWeek[w] = nItem;
					}
		
				}
						
				// 添加进队列
				if(hasFirstMonth){
					setDay(items.get(i), calendar, day,month,nMonth);
				}else{
					items.get(i).setDay(day+"");
				}
				
				itemInWeek[week] = items.get(i);
				while (week < 6 && day<calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {//i == items.size() - 1 && 
					// 最后一周不满，补全--当前月、循环多天
					PmsScheduleItem nItem = new PmsScheduleItem();
					nItem.setDay((++day)+"");
					nItem.setJobContent("");
					itemInWeek[++week] = nItem;
				}
				
				weekOfYear=nweekOfYear;
				month=nMonth;
			}
		}
	}
	
	private static boolean setDay(PmsScheduleItem item,Calendar calendar,int day,int month,int nMonth){
		if(month!=nMonth){//换月加月份
			//1月
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
	/**
	 * 分月存放--每月前后补全； schedule--month--week--item 获取月份，变更，则新加一月（list)
	 * ftl:第一页遍历schedule第一月 如果size大于1，从第二月开始
	 * <span style="page-break-after:always;"></span> <div class="page"> <table
	 * <thead> 遍历月，生成表格
	 * 
	 *
	 */
	
}
