package com.paipianwang.activiti.taglib;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.paipianwang.pat.common.constant.PmsConstant;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.UrlResourceUtils;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.right.entity.PmsRight;
import com.paipianwang.pat.facade.right.service.PmsRightFacade;

/**
 * 判断两个URL，至少有一个满足条件
 * 
 * @author Jack
 *
 */
public class SecurityTagMultipleParam extends TagSupport {

	private static final long serialVersionUID = 7037089506680059434L;

	private String uri;

	private String uri2;

	private String uri3;

	public int doStartTag() throws JspException {

		final ServletContext sc = pageContext.getServletContext();
		WebApplicationContext wc = WebApplicationContextUtils.findWebApplicationContext(sc);
		final PmsRightFacade pmsRightFacade = (PmsRightFacade) wc.getBean("pmsRightFacade");
		final SessionInfo info = (SessionInfo) pageContext.getSession().getAttribute(PmsConstant.SESSION_INFO);

		if (ValidateUtil.isValid(uri)) {
			final String url = UrlResourceUtils.URLResolver(uri, sc.getContextPath());

			if (info != null) {
				if (info.isSuperAdmin()) {
					// 超级管理员
					return EVAL_BODY_INCLUDE;
				} else {
					// session 存在
					PmsRight right = pmsRightFacade.getRightFromRedis(url);
					if (right != null) {
						if (info.hasRight(right)) {

							return EVAL_BODY_INCLUDE;
						}
					}
				}
			}
		}

		if (ValidateUtil.isValid(uri2)) {
			final String url = UrlResourceUtils.URLResolver(uri2, sc.getContextPath());

			if (info != null) {
				if (info.isSuperAdmin()) {
					// 超级管理员
					return EVAL_BODY_INCLUDE;
				} else {
					// session 存在
					PmsRight right = pmsRightFacade.getRightFromRedis(url);
					if (right != null) {
						if (info.hasRight(right)) {

							return EVAL_BODY_INCLUDE;
						}
					}
				}
			}
		}

		if (ValidateUtil.isValid(uri3)) {
			final String url = UrlResourceUtils.URLResolver(uri3, sc.getContextPath());

			if (info != null) {
				if (info.isSuperAdmin()) {
					// 超级管理员
					return EVAL_BODY_INCLUDE;
				} else {
					// session 存在
					PmsRight right = pmsRightFacade.getRightFromRedis(url);
					if (right != null) {
						if (info.hasRight(right)) {

							return EVAL_BODY_INCLUDE;
						}
					}
				}
			}
		}

		return SKIP_BODY;

	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public void setUri2(String uri2) {
		this.uri2 = uri2;
	}

	public void setUri3(String uri3) {
		this.uri3 = uri3;
	}

}