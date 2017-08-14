package com.paipianwang.activiti.interceptors;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.paipianwang.pat.common.constant.PmsConstant;
import com.paipianwang.pat.common.entity.SessionInfo;

/**
 * 登录拦截器
 * 
 * @author jacky
 *
 */
public class SecurityInterceptor implements HandlerInterceptor {

	@Autowired
	private IdentityService identityService = null;

	private List<String> excludeUrls;

	public List<String> getExcludeUrls() {
		return excludeUrls;
	}

	public void setExcludeUrls(List<String> excludeUrls) {
		this.excludeUrls = excludeUrls;
	}

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {

		final SessionInfo info = (SessionInfo) req.getSession().getAttribute(PmsConstant.SESSION_INFO);

		// 首先判断info是否为空
		if (info == null) {
			// 未登录
			System.err.println("info is null");
			req.setAttribute("message", "您还没有登录或登录已超时，请重新登录，然后再刷新本功能！");
			req.getRequestDispatcher("/login").forward(req, resp);
			return false;
		} else {
			// 判断是否为超级管理员
			if (!info.isSuperAdmin()) {
				// 如果是非管理员，判断是否有权限组
				List<String> groupIds = info.getActivitGroups();
				if (groupIds != null && !groupIds.isEmpty()) {
					// 有权限，放行
					return true;
				} else {
					// 没有权限组，则查询
					System.err.println("没有权限组");
					String sessionType = info.getSessionType(); // 身份验证
					Long systermUserId = info.getReqiureId();
					String userId = null;
					if (PmsConstant.ROLE_EMPLOYEE.equals(sessionType)) {
						// 内部员工
						userId = "employee_" + systermUserId;
					}

					if (PmsConstant.ROLE_PROVIDER.equals(sessionType)) {
						// 供应商
						userId = "team_" + systermUserId;
					}

					if (PmsConstant.ROLE_CUSTOMER.equals(sessionType)) {
						// 客户
						userId = "customer_" + systermUserId;
					}

					info.setActivitiUserId(userId);
					List<Group> list = identityService.createGroupQuery().groupMember(userId).list();
					if (list == null || list.size() == 0) {
						req.setAttribute("message", "您还没有登录或登录已超时，请重新登录，然后再刷新本功能！");
						req.getRequestDispatcher("/login").forward(req, resp);
						return false;
					} else {
						List<String> groups = new ArrayList<String>();
						for (final Group group : list) {
							groups.add(group.getId());
						}

						// 放入session中
						info.setActivitGroups(groups);
						req.getSession().removeAttribute(PmsConstant.SESSION_INFO);
						req.getSession().setAttribute(PmsConstant.SESSION_INFO, info);
					}
				}
			}

			return true;
		}

	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mv)
			throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
