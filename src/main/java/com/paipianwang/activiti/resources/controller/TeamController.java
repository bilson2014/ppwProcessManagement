package com.paipianwang.activiti.resources.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.team.entity.PmsTeam;
import com.paipianwang.pat.facade.team.service.PmsTeamFacade;

@RestController
@RequestMapping("/team")
public class TeamController {

	@Autowired
	private PmsTeamFacade pmsTeamFacade;
	
	@RequestMapping(value = "/listByName/{teamName}", method = RequestMethod.POST)
	public List<PmsTeam> getTaskForm(@PathVariable("teamName") final String teamName) {
		if(ValidateUtil.isValid(teamName)){
			return new ArrayList<>();
		}
		return pmsTeamFacade.listByTeamName(teamName);
	}
}
