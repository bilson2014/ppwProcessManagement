package com.paipianwang.activiti.service;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

import com.paipianwang.pat.common.entity.PmsResult;
import com.paipianwang.pat.workflow.entity.PmsQuotation;

/**
 * 报价单业务处理接口
 */
public interface QuotationService {

	/**
	 * 保存/修改报价单
	 * @param pmsQuotation
	 * @return 
	 */
	public PmsResult saveOrUpdateQuotation(PmsQuotation pmsQuotation);
	/**
	 * 导出报价单
	 * @param projectId
	 * @param os
	 */
	public void export(String projectId, OutputStream os,HttpServletRequest request);
}
