package com.paipianwang.activiti.resources.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.paipianwang.activiti.domin.BaseMsg;
import com.paipianwang.activiti.utils.UserUtil;
import com.paipianwang.pat.workflow.entity.PmsProjectMessage;
import com.paipianwang.pat.workflow.facade.PmsProjectMessageFacade;

@RestController
@RequestMapping("/message")
public class MessageController {
	
	@Autowired
	private PmsProjectMessageFacade pmsProjectMessageFacade;
	
	/**
	 * 添加留言topic
	 * @param pmsProjectMessage
	 * @return
	 */
	@RequestMapping(value="/addTopic" ,method = RequestMethod.POST,consumes="application/json")
	public BaseMsg addTopicResource(@RequestBody final PmsProjectMessage message,final HttpServletRequest request) {
		message.setParentId(null);
		User loginUser=UserUtil.getUserFromSession(request.getSession());
		if(loginUser==null){
			//是否需要校验
		}
		message.setFromId(loginUser.getId());
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
		User loginUser=UserUtil.getUserFromSession(request.getSession());
		if(loginUser==null){
			//是否需要校验
		}
		pmsProjectMessage.setFromId(loginUser.getId());
		pmsProjectMessageFacade.insert(pmsProjectMessage);
		
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
		return pmsProjectMessageFacade.getDefaultProjectMessage(projectId);
	}
	
	/**
	 * 获取全部项目留言
	 * @param projectId
	 * @return
	 */
	@RequestMapping("/getProjectMsg/{projectId}")
	public List<PmsProjectMessage> getProjectMessage(@PathVariable("projectId") String projectId){
		return pmsProjectMessageFacade.getAllProjectMessage(projectId);
	}

	/**
	 * 获取项目task对应留言
	 * @param projectId
	 * @param taskName
	 * @return
	 */
	@RequestMapping("/getTaskMsg")
	public List<PmsProjectMessage> getTaskMessage(@RequestBody final PmsProjectMessage message){
		return pmsProjectMessageFacade.getProjectTaskMessage(message.getProjectId(), message.getTaskName());
	}

}
