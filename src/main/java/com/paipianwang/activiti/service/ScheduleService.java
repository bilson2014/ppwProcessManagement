package com.paipianwang.activiti.service;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

import com.paipianwang.pat.common.entity.PmsResult;
import com.paipianwang.pat.workflow.entity.PmsSchedule;

/**
 * 排期表
 */
public interface ScheduleService {

	/**
	 * 保存/更新排期表
	 * @param pmsSchedule
	 * @return
	 */
	public PmsResult saveOrUpdateSchedule(PmsSchedule pmsSchedule);
	/**
	 * 导出排期表
	 * @param pmsSchedule
	 * @param os
	 * @param request
	 */
	public void export(PmsSchedule pmsSchedule, OutputStream os,HttpServletRequest request) throws Exception;
}
