package com.paipianwang.activiti.task.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.activiti.engine.FormService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.FormType;
import org.activiti.engine.impl.form.DateFormType;
import org.activiti.engine.impl.form.TaskFormDataImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import com.paipianwang.activiti.service.MessageService;
import com.paipianwang.activiti.utils.LogUtils;
import com.paipianwang.pat.common.util.DateUtils;
import com.paipianwang.pat.workflow.entity.PmsProjectSynergy;
import com.paipianwang.pat.workflow.entity.ProjectCycleItem;
import com.paipianwang.pat.workflow.facade.PmsProjectSynergyFacade;
import com.paipianwang.pat.workflow.facade.WorkFlowFacade;

public abstract class BaseTaskListener implements TaskListener{
	private static final long serialVersionUID = 5534488240464509535L;

	@Override
	public void notify(DelegateTask delegateTask) {
		execute(delegateTask);
		doAfter(delegateTask);
	}
	
	public abstract void execute(DelegateTask delegateTask);
	
	//根据task配置插入执行日志
	public void doAfter(DelegateTask delegateTask){
		
		final String taskKey=delegateTask.getTaskDefinitionKey();
		
		ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		
		//查询task配置
		WorkFlowFacade workFlowFacade=(WorkFlowFacade) context.getBean("pmsWorkFlowFacade");
		ProjectCycleItem cycle=workFlowFacade.getCycleByTaskId(taskKey);
		List<Map<String,String>> logs=cycle.getLogs();
		//存在日志
		if(logs!=null){
			String taskId = delegateTask.getId();
			
			String assginee=delegateTask.getAssignee();
			String projectId = delegateTask.getExecution().getProcessBusinessKey();
			
			FormService formService = delegateTask.getExecution().getEngineServices().getFormService();
			TaskFormDataImpl formData = (TaskFormDataImpl) formService.getTaskFormData(taskId);
			List<FormProperty> formList = formData.getFormProperties();
			
			//操作人信息
			String fromName="";
			List<String> activitiGroup=new ArrayList<>();
			
			PmsProjectSynergyFacade pmsProjectSynergyFacade = (PmsProjectSynergyFacade) context.getBean("pmsProjectSynergyFacade");
			if(assginee.startsWith("employee_")){
				PmsProjectSynergy synergy=getSynergy(pmsProjectSynergyFacade, projectId, assginee.split("_")[1]);//pmsProjectSynergyFacade.getSynergyById(Long.parseLong(assginee.split("_")[1]));
				
				fromName=synergy.getEmployeeName();
				activitiGroup.add(synergy.getEmployeeGroup());
			}
			
			MessageService messageService=(MessageService) context.getBean("messageService");
			//自动匹配日志参数值
			for(Map<String,String> log:logs){
				String logText=log.get("text");
				List<String> items=LogUtils.getLogItemKeys(logText);
				for(String item:items){
					for(FormProperty  form:formList){
						if(item.equals(form.getId())){
							String value=form.getValue();
							//时间格式处理
							if(form.getType() instanceof DateFormType){
//								DateFormType type=(DateFormType) form.getType();
//								Object format=type.getInformation("datePattern");
								value=DateUtils.getDateByFormatStr(DateUtils.getDateByFormat(value, "yyyy-MM-dd"), "yyyy年MM月dd日");
							}
							logText=LogUtils.setLogValue(logText, item, value);
							break;
						}else if(item.split("_")[0].equals("employee") && item.substring("employee_".length()).equals(form.getId())){
							//id转员工名称
							String value=form.getValue();
							PmsProjectSynergy synergy=getSynergy(pmsProjectSynergyFacade, projectId, value.split("_")[1]);//pmsProjectSynergyFacade.getSynergyById(Long.parseLong(value));
							value=synergy.getEmployeeName();
							logText=LogUtils.setLogValue(logText, item, value);
							break;
						}
					}
				}
				
				messageService.insertDetailOperationLog(projectId, taskId, cycle.getTaskName(), logText, assginee, fromName, activitiGroup);	
			}
		}
		
		
	}
	//待优化：直接获取单条
	private PmsProjectSynergy getSynergy(PmsProjectSynergyFacade pmsProjectSynergyFacade,String projectId,String employeeId){
		Map<String,PmsProjectSynergy> synergies=pmsProjectSynergyFacade.getSynergysByProjectId(projectId);
		Set<Entry<String, PmsProjectSynergy>> sets=synergies.entrySet();
		for(Entry<String, PmsProjectSynergy> set:sets){
			PmsProjectSynergy synergy=set.getValue();
			if(employeeId.equals(synergy.getEmployeeId()+"")){
				return synergy;
			}
		}
		return null;
	}
	
}
