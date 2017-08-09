package com.paipianwang.activiti.resources.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.paipianwang.activiti.utils.UserUtil;
import com.paipianwang.pat.facade.user.entity.PmsUser;
import com.paipianwang.pat.facade.user.service.PmsUserFacade;

@Controller
@RequestMapping("/user")
public class UserController {

	private final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private IdentityService identityService = null;
	@Autowired
	private final PmsUserFacade pmsUserFacade = null;

	@RequestMapping("/logon")
	public String logon(@PathParam("userName") String userName, @RequestParam("password") String password,
			final HttpSession session) {
		logger.debug("logon request: {username={}, password={}}", userName, password);
		User user = identityService.createUserQuery().userFirstName(userName).singleResult();
		boolean checkPassword = identityService.checkPassword(user.getId(), password);
		if (checkPassword) {
			// read user from database
			UserUtil.saveUserToSession(session, user);

			List<Group> groupList = identityService.createGroupQuery().groupMember(userName).list();
			session.setAttribute("groups", groupList);

			String[] groupNames = new String[groupList.size()];
			for (int i = 0; i < groupNames.length; i++) {
				System.out.println(groupList.get(i).getName());
				groupNames[i] = groupList.get(i).getName();
			}

			session.setAttribute("groupNames", ArrayUtils.toString(groupNames));

			return "redirect:/main/index";
		} else {
			return "redirect:/login?error=true";
		}
	}
	
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		
		UserUtil.removeUserSession(session);
		session.removeAttribute("groups");
		return "redirect:/login";
		
	}
	
	/**
	 * 根据客户名搜索客户
	 */
	@RequestMapping(value="/search/info",method = RequestMethod.POST)
	@ResponseBody
	public List<PmsUser> getUserByName(@RequestBody final PmsUser user) {
		
		List<PmsUser> users = pmsUserFacade.findUserByName(user);
		return users != null ? users : new ArrayList<PmsUser>();
	}
}
