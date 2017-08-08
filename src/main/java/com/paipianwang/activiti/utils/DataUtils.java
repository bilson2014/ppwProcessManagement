package com.paipianwang.activiti.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.paipianwang.pat.workflow.entity.ProjectFlowConstant;

/**
 * 数据处理工具类
 * 
 * @author jacky
 *
 */
public class DataUtils {

	/**
	 * 数据分组
	 * 
	 * @return
	 */
	public static Map<String, Object> divideFlowData(Map<String, Object> form) {
		Map<String, Object> flowData = new HashMap<String, Object>();
		Map<String, Object> flowMap = new HashMap<String, Object>();
		Map<String, Object> synergyMap = new HashMap<String, Object>();
		Map<String, Object> teamMap = new HashMap<String, Object>();
		Map<String, Object> userMap = new HashMap<String, Object>();
		for (final Entry<String, Object> entry : form.entrySet()) {
			String key = entry.getKey();

			// PROJECT_FLOW 项目信息
			if (StringUtils.defaultString(key).startsWith("pf_")) {
				flowMap.put(key.split("_")[1], entry.getValue());
			}
			// PROJECT_SYNERGY 协同人
			if (StringUtils.defaultString(key).startsWith("ps_")) {
				synergyMap.put(key.split("_")[1], entry.getValue());
			}

			// PROJECT_TEAM 供应商
			if (StringUtils.defaultString(key).startsWith("pt_")) {
				teamMap.put(key.split("_")[1], entry.getValue());
			}

			// PROJECT_USER 客户
			if (StringUtils.defaultString(key).startsWith("pu_")) {
				userMap.put(key.split("_")[1], entry.getValue());
			}

		}

		flowData.put(ProjectFlowConstant.PROJECT_FLOW, flowMap);
		flowData.put(ProjectFlowConstant.PROJECT_SYNENGY, synergyMap);
		flowData.put(ProjectFlowConstant.PROJECT_TEAM, teamMap);
		flowData.put(ProjectFlowConstant.PROJECT_USER, userMap);

		return flowData;
	}

	public static String listToString(List<String> list, char separator) {
		if (list != null && !list.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < list.size(); i++) {
				sb.append(list.get(i)).append(separator);
			}
			return sb.toString().substring(0, sb.toString().length() - 1);
		}
		return null;
	}
}
