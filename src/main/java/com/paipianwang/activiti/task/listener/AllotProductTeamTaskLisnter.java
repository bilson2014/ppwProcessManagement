package com.paipianwang.activiti.task.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.form.TaskFormDataImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;

import com.paipianwang.pat.workflow.enums.ProjectRoleType;
import com.paipianwang.pat.workflow.enums.ProjectTeamType;
import com.paipianwang.pat.workflow.facade.PmsProjectTeamFacade;

/**
 * 【供应商管家】分配制作供应商
 * 
 * @author jacky
 *
 */
@Component("allotProductTeamTaskLisnter")
public class AllotProductTeamTaskLisnter extends BaseTaskListener {

	private static final long serialVersionUID = -2204490453039201351L;

	@Override
	public void execute(DelegateTask delegateTask) {
		// taskId
		final String taskId = delegateTask.getId();

		// projectId
		final String projectId = delegateTask.getExecution().getProcessBusinessKey();
		
		// 权限服务
		final IdentityService identityService = delegateTask.getExecution().getEngineServices().getIdentityService();
		
		// 获取form表单信息
		FormService formService = delegateTask.getExecution().getEngineServices().getFormService();
		TaskFormDataImpl formData = (TaskFormDataImpl) formService.getTaskFormData(taskId);
		List<FormProperty> formList = formData.getFormProperties();
		if (formList != null && !formList.isEmpty()) {
			Map<String, Object> param = new HashMap<String, Object>();
			for (final FormProperty formProperty : formList) {
				String key = formProperty.getId();
				if (StringUtils.defaultString(key).startsWith("pt_")) {
					param.put(key.split("_")[1], formProperty.getValue());
				}
			}

			// 保存关于制作供应商的信息
			if (param != null && !param.isEmpty()) {
				// 预置字段
				param.put("teamType", ProjectTeamType.produce.getCode()); // 策划供应商
				param.put("projectId", projectId); // projectId
				
				ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
				PmsProjectTeamFacade pmsProjectTeamFacade = (PmsProjectTeamFacade) context
						.getBean("pmsProjectTeamFacade");
				String activitiTeamId = "team_" + String.valueOf(param.get("teamId"));
				
				// 先删除制作供应商
				pmsProjectTeamFacade.deleteProjectTeamByProjectIdAndTeamType(projectId, ProjectTeamType.produce.getCode());
				
				long projectTeamId = pmsProjectTeamFacade.insert(param);
				delegateTask.setVariable("teamProductId", activitiTeamId); // 设置供应商唯一ID
				delegateTask.setVariable("projectTeam_produce", projectTeamId); // 设置制作供应商的唯一ID
				
				// 检测该供应商在activiti表中是否存在
				User team = identityService.createUserQuery().userId(activitiTeamId).singleResult();
				if(team == null) {
					// 在activiti用户表总创建 供应商
					User activitiTeam = identityService.newUser(activitiTeamId);
					activitiTeam.setEmail((String) param.get("email"));
					activitiTeam.setFirstName((String) param.get("teamName"));
					activitiTeam.setPassword("000000");
					identityService.saveUser(activitiTeam);
				}
				
				// 检测策划供应商关系表中是否有数据
				Group group = identityService.createGroupQuery().groupMember(activitiTeamId).groupId(ProjectRoleType.teamProduct.getId()).singleResult();
				if(group == null) {
					// 将 该供应商加入  策划供应商组
					identityService.createMembership(activitiTeamId, ProjectRoleType.teamProduct.getId());
				}
			}
		}
	}

}
