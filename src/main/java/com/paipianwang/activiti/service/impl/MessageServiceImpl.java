package com.paipianwang.activiti.service.impl;

import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paipianwang.activiti.service.MessageService;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.DateUtils;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.right.entity.PmsEmployee;
import com.paipianwang.pat.facade.right.service.PmsEmployeeFacade;
import com.paipianwang.pat.facade.team.entity.PmsTeam;
import com.paipianwang.pat.facade.team.service.PmsTeamFacade;
import com.paipianwang.pat.facade.user.entity.PmsUser;
import com.paipianwang.pat.facade.user.service.PmsUserFacade;
import com.paipianwang.pat.workflow.entity.PmsProjectMessage;
import com.paipianwang.pat.workflow.entity.PmsProjectSynergy;
import com.paipianwang.pat.workflow.facade.PmsProjectMessageFacade;

@Service("messageService")
public class MessageServiceImpl implements MessageService {
	private final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);
	@Autowired
	private PmsProjectMessageFacade pmsProjectMessageFacade;
	@Autowired
	private PmsTeamFacade pmsTeamFacade;
	@Autowired
	private PmsEmployeeFacade pmsEmployeeFacade;
	@Autowired
	private PmsUserFacade pmsUserFacade;
	@Autowired
	private IdentityService identityService = null;

	@Override
	public List<PmsProjectMessage> getDefaultProjectMessage(String projectId) {
		List<PmsProjectMessage> result = pmsProjectMessageFacade.getDefaultProjectMessage(projectId);
		editShowInfo(result);
		return result;
	}

	@Override
	public List<PmsProjectMessage> getAllProjectMessage(String projectId) {
		List<PmsProjectMessage> result = pmsProjectMessageFacade.getAllProjectMessage(projectId);
		editShowInfo(result);
		return result;
	}

	@Override
	public List<PmsProjectMessage> getProjectTaskMessage(String taskId) {
		List<PmsProjectMessage> result = pmsProjectMessageFacade.getProjectTaskMessage(taskId);
		editShowInfo(result);
		return result;
	}

	private void editShowInfo(List<PmsProjectMessage> result) {
		if (ValidateUtil.isValid(result)) {
			List<Group> groups = identityService.createGroupQuery().list();
			List<User> users = identityService.createUserQuery().list();
			for (PmsProjectMessage message : result) {
				// 处理头像
				if (message.getFromId().startsWith("employee_")) {
					PmsEmployee employee = pmsEmployeeFacade
							.findEmployeeById(Long.parseLong(message.getFromId().split("_")[1]));
					message.setFromUrl(employee.getEmployeeImg());
				} else if (message.getFromId().startsWith("team_")) {
					PmsTeam team = pmsTeamFacade.findTeamById(Long.parseLong(message.getFromId().split("_")[1]));
					message.setFromUrl(team.getTeamPhotoUrl());
				} else if (message.getFromId().startsWith("customer_")) {
					PmsUser user = pmsUserFacade.findUserById(Long.parseLong(message.getFromId().split("_")[1]));
					message.setFromUrl(user.getImgUrl());
				} else {
					// 系统头像
				}

				// 处理角色+名称
				setFromName(groups, users, message);
				editDate(message);

				List<PmsProjectMessage> children = message.getChildren();
				if (ValidateUtil.isValid(children)) {
					children.stream().forEach(child -> {
						setFromName(groups, users, child);
						editDate(child);
					});
				}
			}
		}
	}

	private void setFromName(List<Group> groups, List<User> users, PmsProjectMessage message) {
		String userName = "";

		if (ValidateUtil.isValid(message.getFromGroup())) {
			if (message.getFromGroup().equals("system")) {
				// 系统
				message.setFromName("系统");
				return;
			}
			if (message.getFromGroup().equals("manager")) {
				// 系统
				message.setFromName("管理员 "+message.getFromName());
				return;
			}
			// 用户
			StringBuilder groupName = new StringBuilder();
			String[] fromGroups = message.getFromGroup().split(",");
			for (String fromGroup : fromGroups) {
				for (Group group : groups) {
					if (group.getId().equals(fromGroup)) {
						groupName.append("/");
						groupName.append(group.getName());
						break;
					}
				}
			}
			if(StringUtils.isNotBlank(groupName))
				userName = groupName.toString().substring(1) + " ";
		}

		for (User user : users) {
			if (user.getId().equals(message.getFromId())) {
				userName += user.getFirstName();
				break;
			}
		}
		message.setFromName(userName);
	}

	private void editDate(PmsProjectMessage message) {
		if (message.getCreateDate() != null) {
			message.setCreateDate(DateUtils.getDateByFormat(message.getCreateDate(), "yyyy-MM-dd HH:mm:ss").toString());
		}
	}

	/**
	 * 插入系统操作日志
	 */
	@Override
	public void insertSystemMessage(String projectId, String content) {
		PmsProjectMessage message = new PmsProjectMessage();
		message.setFromId("system");
		message.setFromGroup("system");
		message.setProjectId(projectId);
		message.setMessageType(PmsProjectMessage.TYPE_LOG);
		message.setFromName("系统");

		message.setContent(content);
		pmsProjectMessageFacade.insert(message);

	}

	/**
	 * 插入操作日志
	 */
	@Override
	public void insertOperationLog(String projectId, String taskId, String taskName, String content, SessionInfo info) {
		PmsProjectMessage message = new PmsProjectMessage();
		message.setFromId(info.getActivitiUserId());
		message.setFromGroup(StringUtils.join(info.getActivitGroups(), ","));
		message.setProjectId(projectId);
		message.setContent(content);
		message.setMessageType(PmsProjectMessage.TYPE_LOG);
		message.setFromName(info.getRealName());
		message.setTaskId(taskId);
		message.setTaskName(taskName);
		pmsProjectMessageFacade.insert(message);
	}

	@Override
	public void insertDetailOperationLog(String projectId, String taskId, String taskName, String content,
			String fromId, String fromName, List<String> activitiGroup) {
		PmsProjectMessage message = new PmsProjectMessage();
		message.setFromId(fromId);
		message.setFromGroup(StringUtils.join(activitiGroup, ","));
		message.setProjectId(projectId);
		message.setTaskName(taskName);
		message.setContent(content);
		message.setMessageType(PmsProjectMessage.TYPE_LOG);
		message.setFromName(fromName);
		message.setTaskId(taskId);
		pmsProjectMessageFacade.insert(message);
	}

	@Override
	public void insertGageWayOperationLog(String projectId, String taskId, String taskName, String content,
			PmsProjectSynergy synergy) {
		if (synergy != null) {
			PmsProjectMessage message = new PmsProjectMessage();
			message.setFromId("employee_" + synergy.getEmployeeId());
			message.setFromGroup(StringUtils.join(synergy.getEmployeeGroup(), ","));
			message.setProjectId(projectId);
			message.setTaskName(taskName);
			message.setContent(content);
			message.setMessageType(PmsProjectMessage.TYPE_LOG);
			message.setFromName(synergy.getEmployeeName());
			message.setTaskId(taskId);
			pmsProjectMessageFacade.insert(message);
		}
	}
}
