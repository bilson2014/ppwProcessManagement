package com.paipianwang.activiti.poi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.paipianwang.pat.common.util.JsonUtil;
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
			for (int i = 0; i < items.size(); i++) {
				// 获取星期几、几日
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(items.get(i).getStart()));
				week = calendar.get(Calendar.DAY_OF_WEEK) - 1;// 周日是0
				day = calendar.get(Calendar.DAY_OF_MONTH);
				
				int nweekOfYear=calendar.get(Calendar.WEEK_OF_YEAR);//从周一开始算一周，故还需判断week
				
				if (week == 0 || i == 0 || weekOfYear!=nweekOfYear) {
					// 新开始一周，开始一行
					itemInWeek = new PmsScheduleItem[7];
					itemList.add(itemInWeek);
				}
				weekOfYear=nweekOfYear;
				
				if (i == 0) {
					// 第一周前几天空着
					for (int w = 0; w < week; w++) {
						PmsScheduleItem nItem = new PmsScheduleItem();
						nItem.setDay(day - week + w);
						nItem.setJobContent("");
						itemInWeek[w] = nItem;
					}
				}
				// 添加进队列
				items.get(i).setDay(day);
				itemInWeek[week] = items.get(i);
				/*if (i == items.size() - 1 && week < 6) {
					// 最后一周不满，补全--当前月、循环多天
					PmsScheduleItem nItem = new PmsScheduleItem();
					nItem.setDay(++day);
					nItem.setJobContent("");
					itemInWeek[++week] = nItem;
				}*/
			}
		}
	}

	public static void main(String[] args) {
		/*PmsSchedule s=new PmsSchedule();
		s.setItemContent("[{\"jobContent\":\"方案/解说词沟通及制作 ,   \",\"start\":\"2017-12-22\"},{\"jobContent\":\"方案/解说词沟通及制作 ,   \",\"start\":\"2017-12-23\"},{\"jobContent\":\"确认方案/解说词 ,   \",\"start\":\"2017-12-24\"},{\"jobContent\":\"签订合同 ,   \",\"start\":\"2017-12-26\"}]");
		try {
			s.setItems(JsonUtil.fromJsonArray(s.getItemContent(), PmsScheduleItem.class));
			
			ScheduleDateUtil.formatScheduleItem(s);
			
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse("2017-02-05"));
		} catch (ParseException e) {
		}
		int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;// 周日是0
		System.out.println(calendar.get(Calendar.WEEK_OF_YEAR));
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
