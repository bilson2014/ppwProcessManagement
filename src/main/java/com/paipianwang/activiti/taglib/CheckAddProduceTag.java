package com.paipianwang.activiti.taglib;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.paipianwang.pat.workflow.enums.ProjectTeamType;
import com.paipianwang.pat.workflow.facade.PmsProjectTeamFacade;

/**
 * 检查是否可以添加“制作供应商” 标签
 * 
 * @author jacky
 *
 */
public class CheckAddProduceTag extends TagSupport {

	private static final long serialVersionUID = 5892999943614182166L;

	// 项目ID
	private String projectId = null;

	@Override
	public int doStartTag() throws JspException {
		if (StringUtils.isNotBlank(projectId)) {
			final ServletContext sc = pageContext.getServletContext();
			WebApplicationContext wc = WebApplicationContextUtils.findWebApplicationContext(sc);
			final PmsProjectTeamFacade pmsProjectTeamFacade = (PmsProjectTeamFacade) wc.getBean("pmsProjectTeamFacade");
			long count = pmsProjectTeamFacade.countProjectsTeamByProjectId(ProjectTeamType.produce.getCode(),
					projectId);
			if (count > 0) {
				return EVAL_BODY_INCLUDE;
			}
		}
		return SKIP_BODY;

	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

}
