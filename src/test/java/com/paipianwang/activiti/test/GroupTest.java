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
	PmsProjectFlowFacade pmsProjectFlowFacade = null;

	@Autowired
	PmsProjectTeamFacade pmsProjectTeamFacade = null;

	@Autowired
	PmsProjectUserFacade pmsProjectUserFacade = null;

	@Autowired
	PmsProjectGroupColumnShipFacade pmsProjectColumnShipFacade = null;
	
	@Test
	public void insert() {
		List<MetaDataColumn> flowList = pmsProjectFlowFacade.getMetaDataColumn();
		List<String> flows = new ArrayList<String>();

		for (MetaDataColumn metaDataColumn : flowList) {
			flows.add(metaDataColumn.getName());
		}

		List<MetaDataColumn> teamList = pmsProjectTeamFacade.getMetaDataColumn();
		List<String> teams = new ArrayList<String>();

		for (MetaDataColumn metaDataColumn : teamList) {
			teams.add(metaDataColumn.getName());
		}

		List<MetaDataColumn> userList = pmsProjectUserFacade.getMetaDataColumn();
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
		// pmsProjectColumnShipFacade.updateColumnShip(group, columns);
		
		group = new GroupEntity();
		group.setId("sale");
		pmsProjectColumnShipFacade.updateColumnShip(group, columns);
		
		/*group = new GroupEntity();
		group.setId("creativityDirector");
		pmsProjectColumnShipFacade.updateColumnShip(group, columns);
		
		group = new GroupEntity();
		group.setId("superviseDirector");
		pmsProjectColumnShipFacade.updateColumnShip(group, columns);
		
		group = new GroupEntity();
		group.setId("teamProvider");
		pmsProjectColumnShipFacade.updateColumnShip(group, columns);
		
		group = new GroupEntity();
		group.setId("scheme");
		pmsProjectColumnShipFacade.updateColumnShip(group, columns);
		
		group = new GroupEntity();
		group.setId("teamPlan");
		pmsProjectColumnShipFacade.updateColumnShip(group, columns);
		
		group = new GroupEntity();
		group.setId("teamProduct");
		pmsProjectColumnShipFacade.updateColumnShip(group, columns);
		
		group = new GroupEntity();
		group.setId("finance");
		pmsProjectColumnShipFacade.updateColumnShip(group, columns);
		
		group = new GroupEntity();
		group.setId("supervise");
		pmsProjectColumnShipFacade.updateColumnShip(group, columns);
		
		group = new GroupEntity();
		group.setId("teamPurchase");
		pmsProjectColumnShipFacade.updateColumnShip(group, columns);
		
		group = new GroupEntity();
		group.setId("teamDirector");
		pmsProjectColumnShipFacade.updateColumnShip(group, columns);
		
		group = new GroupEntity();
		group.setId("financeDirector");
		pmsProjectColumnShipFacade.updateColumnShip(group, columns);
		
		group = new GroupEntity();
		group.setId("customerDirector");
		pmsProjectColumnShipFacade.updateColumnShip(group, columns);*/
	}
}
