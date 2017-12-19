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
			for (int i = 0; i < items.size(); i++) {
				// 获取星期几、几日
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(items.get(i).getStart()));
				week = calendar.get(Calendar.DAY_OF_WEEK) - 1;// 周日是0
				day = calendar.get(Calendar.DAY_OF_MONTH);
				if (week == 0 || i == 0) {
					// 新开始一周，开始一行
					itemInWeek = new PmsScheduleItem[7];
					itemList.add(itemInWeek);
				}
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
				if (i == items.size() - 1 && week < 6) {
					// 最后一周不满，补全
					PmsScheduleItem nItem = new PmsScheduleItem();
					nItem.setDay(++day);
					nItem.setJobContent("");
					itemInWeek[++week] = nItem;
				}
			}
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
