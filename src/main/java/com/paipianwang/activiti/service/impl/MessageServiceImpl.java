package com.paipianwang.activiti.service.impl;

import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paipianwang.activiti.service.MessageService;
import com.paipianwang.pat.common.util.DateUtils;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.right.entity.PmsEmployee;
import com.paipianwang.pat.facade.right.service.PmsEmployeeFacade;
import com.paipianwang.pat.facade.team.entity.PmsTeam;
import com.paipianwang.pat.facade.team.service.PmsTeamFacade;
import com.paipianwang.pat.facade.user.entity.PmsUser;
import com.paipianwang.pat.facade.user.service.PmsUserFacade;
import com.paipianwang.pat.workflow.entity.PmsProjectMessage;
import com.paipianwang.pat.workflow.facade.PmsProjectMessageFacade;

//@Service
//public class MessageServiceImpl implements MessageService {
//	private final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);
//	@Autowired
//	private PmsProjectMessageFacade pmsProjectMessageFacade;
//	@Autowired
//	private PmsTeamFacade pmsTeamFacade;
//	@Autowired
//	private PmsEmployeeFacade pmsEmployeeFacade;
//	@Autowired
//	private PmsUserFacade pmsUserFacade;
//	@Autowired
//	private IdentityService identityService = null;
//	
//	@Override
//	public List<PmsProjectMessage> getDefaultProjectMessage(String projectId) {
//		List<PmsProjectMessage> result=pmsProjectMessageFacade.getDefaultProjectMessage(projectId);
////		editShowInfo(result);
//		return result;
//	}
//	
//	@Override
//	public List<PmsProjectMessage> getAllProjectMessage(String projectId) {
//		List<PmsProjectMessage> result=pmsProjectMessageFacade.getAllProjectMessage(projectId);
////		editShowInfo(result);
//		return result;
//	}
//
//
//
//	@Override
////	public List<PmsProjectMessage> getProjectTaskMessage(String projectId, String taskName) {
////		List<PmsProjectMessage> result=pmsProjectMessageFacade.getProjectTaskMessage(projectId, taskName);
////		editShowInfo(result);
////		return result;
////	}
////	
//
////	private void editShowInfo(List<PmsProjectMessage> result){
////		if(ValidateUtil.isValid(result)){
////			List<Group> groups = identityService.createGroupQuery().list();
////			List<User> users=identityService.createUserQuery().list();
////			for(PmsProjectMessage message:result){
////				//处理头像
////				if(message.getFromId().startsWith("employee_")){
////					PmsEmployee employee=pmsEmployeeFacade.findEmployeeById(Long.parseLong(message.getFromId().split("_")[1]));
////					message.setFromUrl(employee.getEmployeeImg());
////				}else if(message.getFromId().startsWith("team_")){
////					PmsTeam team=pmsTeamFacade.findTeamById(Long.parseLong(message.getFromId().split("_")[1]));
////					message.setFromUrl(team.getTeamPhotoUrl());
////				}else if(message.getFromId().startsWith("customer_")){
////					PmsUser user=pmsUserFacade.findUserById(Long.parseLong(message.getFromId().split("_")[1]));
////					message.setFromUrl(user.getImgUrl());
////				}else{
////					//系统头像
////				}
////				
////				//处理角色+名称
////				setFromName(groups, users, message);
////				editDate(message);
////				
////				List<PmsProjectMessage> children=message.getChildren();
////				if(ValidateUtil.isValid(children)){
////					children.stream().forEach(child->{
////						setFromName(groups, users, child);
////						editDate(child);
////					});
////				}
////			}
////		}
////	}
//	
//	private void setFromName(List<Group> groups,List<User> users,PmsProjectMessage message){
//		String userName="";
//
//		if(ValidateUtil.isValid(message.getFromGroup())){
//			if(message.getFromGroup().equals("system")){
//				//系统
//				message.setFromName("系统");
//				return ;
//			}
//			//用户
//			StringBuilder groupName = new StringBuilder();
//			String[] fromGroups=message.getFromGroup().split(",");
//			for(String fromGroup:fromGroups){
//				for(Group group:groups){
//					if(group.getId().equals(fromGroup)){
//						groupName.append("/");
//						groupName.append(group.getName());
//						break;
//					}
//				}
//			}
//			userName=groupName.toString().substring(1)+" ";
//		}
//		
//		for(User user:users){
//			if(user.getId().equals(message.getFromId())){
//				userName+=user.getFirstName();
//				break;
//			}
//		}
//		message.setFromName(userName);
//	}
//	
//	private void editDate(PmsProjectMessage message){
//		if(message.getCreateDate()!=null){
//			message.setCreateDate(DateUtils.getDateByFormat(message.getCreateDate(), "yyyy-MM-dd HH:mm:ss").toString());	
//		}
//	}
//}
