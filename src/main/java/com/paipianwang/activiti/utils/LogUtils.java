package com.paipianwang.activiti.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogUtils {
	/**
	 * 获取日志内容占位标记
	 * @param logText
	 * @return
	 */
	public static List<String> getLogItemKeys(String logText){
		List<String> items=new ArrayList<>();
	    Pattern p=Pattern.compile("\\{([\\w\\*]+)\\}");
	    Matcher m=p.matcher(logText);
	    while(m.find()){
	        items.add(m.group(1));
	    }
	    return items;
	}
	/**
	 * 日志内容占位标记赋值
	 * @param logText
	 * @param itemKey
	 * @param itemValue
	 * @return
	 */
	public static String setLogValue(String logText,String itemKey,String itemValue){
		if(itemValue==null){
			itemValue="";
		}
		logText=logText.replace("{"+itemKey+"}", itemValue);
		return logText;
	}
}
