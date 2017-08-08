package com.paipianwang.activiti.test;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class DataInit extends BaseTest {

	private final Logger logger = LoggerFactory.getLogger(DataInit.class);

	@Autowired
	private IdentityService identityService = null;

	@Autowired
	private RepositoryService repositoryService = null;

	@Test
	public void deployee() {
		repositoryService.createDeployment().addClasspathResource("activiti/procedure-workflow-version4.bpmn").deploy();
		logger.info("Number of process definitions:" + repositoryService.createProcessDefinitionQuery().count());
	}

	@Test
	public void initData() {
		User user = identityService.newUser("admin");
		user.setEmail("henry.yan@kafeitu.me");
		user.setFirstName("Henry");
		user.setLastName("Yan");
		user.setPassword("000000");
		identityService.saveUser(user);

		user = identityService.newUser("hruser");
		user.setEmail("lili.zhang@kafeitu.me");
		user.setFirstName("Lili");
		user.setLastName("Zhang");
		user.setPassword("000000");
		identityService.saveUser(user);

		user = identityService.newUser("leaderuser");
		user.setEmail("jhon.li@kafeitu.me");
		user.setFirstName("Jhon");
		user.setLastName("Li");
		user.setPassword("000000");
		identityService.saveUser(user);

		user = identityService.newUser("blues");
		user.setEmail("blues.li@kafeitu.me");
		user.setFirstName("blues");
		user.setLastName("Li");
		user.setPassword("000000");
		identityService.saveUser(user);

		user = identityService.newUser("kafeitu");
		user.setEmail("coffee.rabbit@kafeitu.me");
		user.setFirstName("Coffee");
		user.setLastName("Rabbit");
		user.setPassword("000000");
		identityService.saveUser(user);

		user = identityService.newUser("lutao");
		user.setEmail("lutao.rabbit@kafeitu.me");
		user.setFirstName("Lu");
		user.setLastName("Tao");
		user.setPassword("000000");
		identityService.saveUser(user);

		Group group = identityService.newGroup("deptLeader");
		group.setName("DeptLeader");
		group.setType("assignment");
		identityService.saveGroup(group);

		group = identityService.newGroup("hr");
		group.setName("Hr");
		group.setType("assignment");
		identityService.saveGroup(group);

		group = identityService.newGroup("user");
		group.setName("User");
		group.setType("security-role");
		identityService.saveGroup(group);

		group = identityService.newGroup("admin");
		group.setName("Admin");
		group.setType("security-role");
		identityService.saveGroup(group);

		identityService.createMembership("admin", "admin");
		identityService.createMembership("admin", "user");

		identityService.createMembership("hruser", "hr");
		identityService.createMembership("hruser", "user");

		identityService.createMembership("lutao", "hr");
		identityService.createMembership("lutao", "user");

		identityService.createMembership("kafeitu", "admin");
		identityService.createMembership("kafeitu", "user");

		identityService.createMembership("leaderuser", "deptLeader");
		identityService.createMembership("leaderuser", "user");

		identityService.createMembership("blues", "deptLeader");
		identityService.createMembership("blues", "user");
	}

	@Test
	public void insertUser() {
		User user = identityService.newUser("lily");
		user.setEmail("lily.yan@kafeitu.me");
		user.setFirstName("Hai");
		user.setLastName("Yan");
		user.setPassword("000000");
		identityService.saveUser(user);

		identityService.createMembership("lily", "deptLeader");
		identityService.createMembership("lily", "user");
	}

	@Test
	public void initDataSource() {
		// 销售总监
		Group group = identityService.newGroup("saleDirector");
		group.setName("销售总监");
		group.setType("security-role");
		identityService.saveGroup(group);

		group = identityService.newGroup("creativityDirector");
		group.setName("创意总监");
		group.setType("security-role");
		identityService.saveGroup(group);

		group = identityService.newGroup("superviseDirector");
		group.setName("监制总监");
		group.setType("security-role");
		identityService.saveGroup(group);

		group = identityService.newGroup("teamProvider");
		group.setName("供应商管家");
		group.setType("assignment");
		identityService.saveGroup(group);

		group = identityService.newGroup("scheme");
		group.setName("策划");
		group.setType("assignment");
		identityService.saveGroup(group);

		group = identityService.newGroup("finance");
		group.setName("财务");
		group.setType("assignment");
		identityService.saveGroup(group);

		group = identityService.newGroup("supervise");
		group.setName("监制");
		group.setType("assignment");
		identityService.saveGroup(group);

		group = identityService.newGroup("teamPurchase");
		group.setName("供应商采购");
		group.setType("assignment");
		identityService.saveGroup(group);

		group = identityService.newGroup("teamDirector");
		group.setName("供应商总监");
		group.setType("assignment");
		identityService.saveGroup(group);

		group = identityService.newGroup("financeDirector");
		group.setName("财务总监");
		group.setType("assignment");
		identityService.saveGroup(group);

		group = identityService.newGroup("customerDirector");
		group.setName("客服总监");
		group.setType("assignment");
		identityService.saveGroup(group);

		User user = identityService.newUser("36");
		user.setEmail("test@paipianwang.cn");
		user.setFirstName("test");
		user.setPassword("000000");
		identityService.saveUser(user);

		user = identityService.newUser("35");
		user.setEmail("huge@paipianwang.cn");
		user.setFirstName("huge");
		user.setPassword("000000");
		identityService.saveUser(user);

		user = identityService.newUser("72");
		user.setEmail("liuchao@paipianwang.cn");
		user.setFirstName("liuchao");
		user.setPassword("000000");
		identityService.saveUser(user);

		user = identityService.newUser("33");
		user.setEmail("yanxueqin@paipianwang.cn");
		user.setFirstName("yanxueqin");
		user.setPassword("000000");
		identityService.saveUser(user);

		user = identityService.newUser("25");
		user.setEmail("wangyi@paipianwang.cn");
		user.setFirstName("wangyi");
		user.setPassword("000000");
		identityService.saveUser(user);

		user = identityService.newUser("68");
		user.setEmail("zhanggaoge@paipianwang.cn");
		user.setFirstName("zhanggaoge");
		user.setPassword("000000");
		identityService.saveUser(user);

		user = identityService.newUser("69");
		user.setEmail("guofang@paipianwang.cn");
		user.setFirstName("guofang");
		user.setPassword("000000");
		identityService.saveUser(user);

		user = identityService.newUser("70");
		user.setEmail("zhangxiaoran@paipianwang.cn");
		user.setFirstName("zhangxiaoran");
		user.setPassword("000000");
		identityService.saveUser(user);

		user = identityService.newUser("71");
		user.setEmail("chenjingna@paipianwang.cn");
		user.setFirstName("chenjingna");
		user.setPassword("000000");
		identityService.saveUser(user);

		// 供应商
		user = identityService.newUser("team_001");
		user.setEmail("plan@paipianwang.cn");
		user.setFirstName("panfeng");
		user.setPassword("000000");
		identityService.saveUser(user);

		user = identityService.newUser("team_002");
		user.setEmail("product@paipianwang.cn");
		user.setFirstName("youwen");
		user.setPassword("000000");
		identityService.saveUser(user);

		// 销售总监 -- 虎哥
		identityService.createMembership("35", "saleDirector");
		// 创意总监 -- 刘超
		identityService.createMembership("72", "creativityDirector");
		// 监制总监 -- 张高哥
		identityService.createMembership("68", "superviseDirector");
		// 供应商管家 -- 王壹
		identityService.createMembership("25", "teamProvider");
		// 策划 -- 张晓冉
		identityService.createMembership("70", "scheme");
		// 财务 -- 郭芳
		identityService.createMembership("69", "finance");
		// 监制 -- 闫雪琴
		identityService.createMembership("33", "supervise");
		// 供应商采购 -- 陈景娜
		identityService.createMembership("71", "teamPurchase");

		// 策划供应商 -- team_001 panfeng

		// 制作供应商 -- team_002 youwen

	}

	@Test
	public void test() {
		// 供应商
		User user = identityService.newUser("team_001");
		user.setEmail("plan@paipianwang.cn");
		user.setFirstName("panfeng");
		user.setPassword("000000");
		identityService.saveUser(user);

		user = identityService.newUser("team_002");
		user.setEmail("product@paipianwang.cn");
		user.setFirstName("youwen");
		user.setPassword("000000");
		identityService.saveUser(user);
	}

	@Test
	public void testGroup() {
		Group group = identityService.newGroup("teamDirector");
		group.setName("供应商总监");
		group.setType("assignment");
		identityService.saveGroup(group);

		group = identityService.newGroup("financeDirector");
		group.setName("财务总监");
		group.setType("assignment");
		identityService.saveGroup(group);

		group = identityService.newGroup("customerDirector");
		group.setName("客服总监");
		group.setType("assignment");
		identityService.saveGroup(group);
	}
	
	@Test
	public void testUser() {
		User user = identityService.newUser("30");
		user.setEmail("liufeng@paipianwang.cn");
		user.setFirstName("liufeng");
		user.setPassword("000000");
		identityService.saveUser(user);
		
		user = identityService.newUser("73");
		user.setEmail("yangwei@paipianwang.cn");
		user.setFirstName("yangwei");
		user.setPassword("000000");
		identityService.saveUser(user);
		
		user = identityService.newUser("32");
		user.setEmail("lihonglei@paipianwang.cn");
		user.setFirstName("lihonglei");
		user.setPassword("000000");
		identityService.saveUser(user);
	}
	
	@Test
	public void testUserMerber() {
		// 供应商总监 -- 刘峰
		identityService.createMembership("30", "teamDirector");
		
		// 财务总监 -- 杨巍
		identityService.createMembership("73", "financeDirector");
		
		// 客服总监 -- 闫雪琴
		identityService.createMembership("33", "customerDirector");
	}
	
}
