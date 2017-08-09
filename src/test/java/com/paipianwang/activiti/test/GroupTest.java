package com.paipianwang.activiti.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.paipianwang.pat.common.entity.MetaDataColumn;
import com.paipianwang.pat.workflow.facade.PmsProjectFlowFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectGroupColumnShipFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectTeamFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectUserFacade;

public class GroupTest extends BaseTest{

	@Autowired
	PmsProjectFlowFacade flowFacade = null;

	@Autowired
	PmsProjectTeamFacade teamFacade = null;

	@Autowired
	PmsProjectUserFacade userFacade = null;

	@Autowired
	PmsProjectGroupColumnShipFacade shipFacade = null;
	
	@Test
	public void insert() {
		List<MetaDataColumn> flowList = flowFacade.getMetaDataColumn();
		List<String> flows = new ArrayList<String>();

		for (MetaDataColumn metaDataColumn : flowList) {
			flows.add(metaDataColumn.getName());
		}

		List<MetaDataColumn> teamList = teamFacade.getMetaDataColumn();
		List<String> teams = new ArrayList<String>();

		for (MetaDataColumn metaDataColumn : teamList) {
			teams.add(metaDataColumn.getName());
		}

		List<MetaDataColumn> userList = userFacade.getMetaDataColumn();
		List<String> users = new ArrayList<String>();

		for (MetaDataColumn metaDataColumn : userList) {
			users.add(metaDataColumn.getName());
		}
		
		Map<String, List<String>> columns = new HashMap<String, List<String>>();
		columns.put("PROJECT_FLOW", flows);
		columns.put("PROJECT_TEAM", teams);
		columns.put("PROJECT_USER", users);

		Group group = new GroupEntity();
		group.setId("saleDirector");

		shipFacade.updateColumnShip(group, columns);
	}
}
