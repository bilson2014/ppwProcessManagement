package com.paipianwang.activiti.resources.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.paipianwang.activiti.domin.BaseMsg;
import com.paipianwang.activiti.mq.email.service.BaseMQService;
import com.paipianwang.activiti.service.MessageService;
import com.paipianwang.pat.common.constant.PmsConstant;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.workflow.entity.PmsProjectMessage;
import com.paipianwang.pat.workflow.facade.PmsProjectMessageFacade;

@RestController
@RequestMapping("/message")
public class MessageController {
	
	@Autowired
	private PmsProjectMessageFacade pmsProjectMessageFacade;
	@Autowired
	private MessageService msMessageService;
	
	@Autowired
	@Qualifier("topicReplyInformMQService")
	private BaseMQService topicReplyInformMQServiceImpl;
	
	
	/**
	 * 添加留言topic
	 * @param pmsProjectMessage
	 * @return
	 */
	@RequestMapping(value="/addTopic" ,method = RequestMethod.POST,consumes="application/json")
	public BaseMsg addTopicResource(@RequestBody final PmsProjectMessage message,final HttpServletRequest request) {
		message.setParentId(null);
		final SessionInfo info = (SessionInfo) request.getSession().getAttribute(PmsConstant.SESSION_INFO);
		message.setFromId(info.getActivitiUserId());
		if(ValidateUtil.isValid(info.getActivitGroups())){
			message.setFromGroup(StringUtils.join(info.getActivitGroups(), ","));
		}
		message.setMessageType(PmsProjectMessage.TYPE_HANDLE);
		message.setFromName(info.getRealName());
		pmsProjectMessageFacade.insert(message);
		BaseMsg result=new BaseMsg();
		result.setCode(BaseMsg.NORMAL);
		return result;
	}
	
	/**
	 * 添加留言reply
	 * @param pmsProjectMessage
	 * @return
	 */
	@RequestMapping("/addReply")
	public BaseMsg addReplyResource(@RequestBody final PmsProjectMessage pmsProjectMessage,final HttpServletRequest request) {
		BaseMsg result=new BaseMsg();
		if(pmsProjectMessage.getParentId()==null){
			result.setCode(BaseMsg.ERROR);
			result.setErrorMsg("");
			return result;
		}
		final SessionInfo info = (SessionInfo) request.getSession().getAttribute(PmsConstant.SESSION_INFO);
		pmsProjectMessage.setFromId(info.getActivitiUserId());
		if(ValidateUtil.isValid(info.getActivitGroups())){
			pmsProjectMessage.setFromGroup(StringUtils.join(info.getActivitGroups(), ","));
		}
		pmsProjectMessage.setMessageType(PmsProjectMessage.TYPE_HANDLE);
		pmsProjectMessage.setFromName(info.getRealName());
		long id=pmsProjectMessageFacade.insert(pmsProjectMessage);
		
		//发送新留言邮件通知
		topicReplyInformMQServiceImpl.sendMessage(id+"");
		result.setCode(BaseMsg.NORMAL);
		return result;
	}
	/**
	 * 项目留言默认前3条
	 * @param projectId
	 * @return
	 */
	@RequestMapping("/getDefaultMsg/{projectId}")
	public List<PmsProjectMessage> getDefaultMessage(@PathVariable("projectId") String projectId){
		return msMessageService.getDefaultProjectMessage(projectId);
	}
	
	/**
	 * 获取全部项目留言
	 * @param projectId
	 * @return
	 */
	@RequestMapping("/getProjectMsg/{projectId}")
	public List<PmsProjectMessage> getProjectMessage(@PathVariable("projectId") String projectId){
		return msMessageService.getAllProjectMessage(projectId);
	}

	/**
	 * 获取项目task对应留言
	 * @param projectId
	 * @param taskName
	 * @return
	 */
	@RequestMapping("/getTaskMsg")
	public List<PmsProjectMessage> getTaskMessage(@RequestBody final PmsProjectMessage message){
		return msMessageService.getProjectTaskMessage(message.getTaskId());
	}

}
