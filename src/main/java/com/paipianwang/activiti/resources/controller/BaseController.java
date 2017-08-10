package com.paipianwang.activiti.resources.controller;

import javax.servlet.http.HttpServletRequest;

import com.paipianwang.pat.common.constant.PmsConstant;
import com.paipianwang.pat.common.entity.SessionInfo;

public abstract class BaseController {

	protected SessionInfo getCurrentInfo(final HttpServletRequest request){
		final SessionInfo info = (SessionInfo) request.getSession().getAttribute(PmsConstant.SESSION_INFO);
		return info;
	}
}
