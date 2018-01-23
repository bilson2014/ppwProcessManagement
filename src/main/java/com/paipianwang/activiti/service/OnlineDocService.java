package com.paipianwang.activiti.service;

import java.io.File;

import com.paipianwang.pat.workflow.entity.PmsProjectResource;

public interface OnlineDocService {
	/**
	 * TRANSFORMATION 资源转换中
	 * FINISH 转换完成
	 * FAIL转换失败
	 * DELETE资源已经删除
	 */
	public static final String TRANSFORMATION = "transformation";
	public static final String FINISH = "finish";
	public static final String FAIL = "fail";
	public static final String DELETE = "delete";
	

	public String convertFile(PmsProjectResource pmsProjectResource);
	
	public void convertToPdf(File sourceFile,String pdfPath);
}
