package com.paipianwang.activiti.task.listener;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;

import com.paipianwang.pat.workflow.facade.PmsProjectFlowFacade;

/**
 * 项目结束监听
 * @author jacky
 *
 */
@Component("endProjectEventListener")
public class EndProjectEventListener implements JavaDelegate,Serializable {

	private static final long serialVersionUID = -5886397723124115854L;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// 将项目状态修改为 finished
		String projectId = execution.getProcessBusinessKey();
		String processInstanceId = execution.getProcessInstanceId();
		
		// 获取 上下文环境
		ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		PmsProjectFlowFacade flowFacade = (PmsProjectFlowFacade) context
				.getBean("pmsProjectFlowFacade");
		
		Map<String, Object> metaData = new HashMap<String, Object>();
		metaData.put("projectStatus", "finished");
		flowFacade.update(metaData, projectId, processInstanceId);
	}

}
