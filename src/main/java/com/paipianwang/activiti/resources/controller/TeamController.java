package com.paipianwang.activiti.resources.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.team.entity.PmsTeam;
import com.paipianwang.pat.facade.team.service.PmsTeamFacade;
import com.paipianwang.pat.workflow.entity.PmsProjectTeam;
import com.paipianwang.pat.workflow.enums.ProjectRoleType;
import com.paipianwang.pat.workflow.facade.PmsProjectTeamFacade;

@RestController
@RequestMapping("/team")
public class TeamController {

	@Autowired
	private PmsTeamFacade pmsTeamFacade;

	@Autowired
	private PmsProjectTeamFacade pmsProjectTeamFacade;

	@Autowired
	private IdentityService identityService = null;

	@Autowired
	private TaskService taskService = null;

	@RequestMapping(value = "/listByName/{teamName}", method = RequestMethod.POST)
	public List<PmsTeam> getTaskForm(@PathVariable("teamName") final String teamName) {
		if (!ValidateUtil.isValid(teamName)) {
			return new ArrayList<>();
		}
		return pmsTeamFacade.listByTeamName(teamName);
	}

	/**
	 * 新增 制作团队
	 * @return
	 */
	@PostMapping("/add/produceTeam/{taskId}")
	public boolean addProjectProduceTeam(@RequestBody final PmsProjectTeam projectTeam,
			@PathVariable("taskId") final String taskId) {
		if (projectTeam != null) {
			final String projectId = projectTeam.getProjectId();
			final String activitiTeamId = "team_" + projectTeam.getTeamId();
			// 保证 项目ID 不为空
			if (StringUtils.isNotBlank(projectId)) {
				final Integer teamId = projectTeam.getTeamId();
				// 查看是否存在 此供应商，如果存在则跳过，不存在则创建
				User user = identityService.createUserQuery().userId(activitiTeamId).singleResult();
				if (user != null) {
					// 存在，则直接新增 项目供应商
					pmsProjectTeamFacade.insert(projectTeam);
				} else {
					// 不存在，则先创建供应商
					PmsTeam team = pmsTeamFacade.findTeamById(teamId.longValue());
					if (team != null) {
						User teamUser = identityService.newUser(activitiTeamId);
						teamUser.setEmail(team.getEmail());
						teamUser.setFirstName(team.getTeamName());
						teamUser.setPassword("000000");
						identityService.saveUser(teamUser);

						// 检测制作供应商关系表中是否有数据
						Group group = identityService.createGroupQuery().groupMember(activitiTeamId)
								.groupId(ProjectRoleType.teamProduct.getId()).singleResult();
						if (group == null) {
							// 将 该供应商加入 制作供应商组
							identityService.createMembership(activitiTeamId, ProjectRoleType.teamProduct.getId());
						}
					}
				}

				// 添加供应商后，将此供应商加入候选组中，以便查看项目
				if (StringUtils.isNotBlank(taskId)) {
					taskService.addCandidateUser(taskId, activitiTeamId);
				}

				return true;
			}
		}

		return false;
	}

	/**
	 * 删除供应商
	 * @param taskId
	 * @param projectTeam
	 * @return
	 */
	@PostMapping("/delete/{taskId}")
	public boolean deleteProjectTeam(@PathVariable("taskId") final String taskId,
			@RequestBody final PmsProjectTeam projectTeam) {
		// 逻辑删除 项目团队信息
		if (projectTeam != null) {
			final Long pmsTeamId = projectTeam.getProjectTeamId();
			
			if (pmsTeamId != null) {
				Map<String, Object> metaData = new HashMap<String, Object>();
				metaData.put("flag", 1);
				pmsProjectTeamFacade.update(metaData, pmsTeamId);

				if (StringUtils.isNotBlank(taskId)) {
					// 解除 该团队可以看见项目的权限
					final String activitiId = "team_" + pmsTeamId;
					taskService.deleteCandidateUser(taskId, activitiId);
				}
				return true;
			}
		}
		return false;
	}
}
