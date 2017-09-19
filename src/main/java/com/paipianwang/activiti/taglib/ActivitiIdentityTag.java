package com.paipianwang.activiti.taglib;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.paipianwang.pat.common.constant.PmsConstant;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.ValidateUtil;

/**
 * 该自定义标签用于判断当前登录角色
 * 符合当前登录角色，输出;反之则不输出
 * @author Jack
 *
 */
public class ActivitiIdentityTag extends TagSupport{

	private static final long serialVersionUID = 2294078545765614257L;
	
	private String role = null;
	
	private String role2 = null;

	public int doStartTag() throws JspException {
		if(ValidateUtil.isValid(role)){
			
			final SessionInfo info = (SessionInfo) pageContext.getSession().getAttribute(PmsConstant.SESSION_INFO);
			if(info != null){
				final List<String> groups = info.getActivitGroups();
				System.err.println("ActivitiIdentityTag : groups is " + groups);
				// 已经登陆,判断角色
				if(ValidateUtil.isValid(groups)) {
					if(groups.contains(role)){
						return EVAL_BODY_INCLUDE;
					} else if(groups.contains(role2)) {
						return EVAL_BODY_INCLUDE;
					}
				}
			}
		}
		return SKIP_BODY;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public void setRole2(String role2) {
		this.role2 = role2;
	}
	
}
