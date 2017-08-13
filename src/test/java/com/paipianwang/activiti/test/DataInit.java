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
		repositoryService.createDeployment().addClasspathResource("activiti/procedure-workflow.bpmn").deploy();
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

		/*group = identityService.newGroup("teamGroup");
		group.setName("供应商");
		group.setType("assignment");
		identityService.saveGroup(group);*/
		
		group = identityService.newGroup("customerGroup");
		group.setName("客户");
		group.setType("assignment");
		identityService.saveGroup(group);

		User user = identityService.newUser("employee_36");
		user.setEmail("test@paipianwang.cn");
		user.setFirstName("test");
		user.setPassword("000000");
		identityService.saveUser(user);

		user = identityService.newUser("employee_35");
		user.setEmail("huge@paipianwang.cn");
		user.setFirstName("huge");
		user.setPassword("000000");
		identityService.saveUser(user);

		user = identityService.newUser("employee_72");
		user.setEmail("liuchao@paipianwang.cn");
		user.setFirstName("liuchao");
		user.setPassword("000000");
		identityService.saveUser(user);

		user = identityService.newUser("employee_33");
		user.setEmail("yanxueqin@paipianwang.cn");
		user.setFirstName("yanxueqin");
		user.setPassword("000000");
		identityService.saveUser(user);

		user = identityService.newUser("employee_25");
		user.setEmail("wangyi@paipianwang.cn");
		user.setFirstName("wangyi");
		user.setPassword("000000");
		identityService.saveUser(user);

		user = identityService.newUser("employee_68");
		user.setEmail("zhanggaoge@paipianwang.cn");
		user.setFirstName("zhanggaoge");
		user.setPassword("000000");
		identityService.saveUser(user);

		user = identityService.newUser("employee_69");
		user.setEmail("guofang@paipianwang.cn");
		user.setFirstName("guofang");
		user.setPassword("000000");
		identityService.saveUser(user);

		user = identityService.newUser("employee_70");
		user.setEmail("zhangxiaoran@paipianwang.cn");
		user.setFirstName("zhangxiaoran");
		user.setPassword("000000");
		identityService.saveUser(user);

		user = identityService.newUser("employee_71");
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
		
		user = identityService.newUser("employee_30");
		user.setEmail("liufeng@paipianwang.cn");
		user.setFirstName("liufeng");
		user.setPassword("000000");
		identityService.saveUser(user);
		
		user = identityService.newUser("employee_73");
		user.setEmail("yangwei@paipianwang.cn");
		user.setFirstName("yangwei");
		user.setPassword("000000");
		identityService.saveUser(user);
		
		user = identityService.newUser("employee_32");
		user.setEmail("lihonglei@paipianwang.cn");
		user.setFirstName("lihonglei");
		user.setPassword("000000");
		identityService.saveUser(user);

		// 销售总监 -- 虎哥
		identityService.createMembership("employee_35", "saleDirector");
		// 创意总监 -- 刘超
		identityService.createMembership("employee_72", "creativityDirector");
		// 监制总监 -- 张高哥
		identityService.createMembership("employee_68", "superviseDirector");
		// 供应商管家 -- 王壹
		identityService.createMembership("employee_25", "teamProvider");
		// 策划 -- 张晓冉
		identityService.createMembership("employee_70", "scheme");
		// 财务 -- 郭芳
		identityService.createMembership("employee_69", "finance");
		// 监制 -- 李红蕾
		identityService.createMembership("employee_32", "supervise");
		// 供应商采购 -- 陈景娜
		identityService.createMembership("employee_71", "teamPurchase");

		// 策划供应商 -- team_001 panfeng
		
		// 制作供应商 -- team_002 youwen
		
		
		// 供应商总监 -- 刘峰
		identityService.createMembership("employee_30", "teamDirector");
		
		// 财务总监 -- 杨巍
		identityService.createMembership("employee_73", "financeDirector");
		
		// 客服总监 -- 闫雪琴
		identityService.createMembership("employee_33", "customerDirector");

	}
	
	@Test
	public void test() {
		Group group = identityService.newGroup("sale");
		group.setName("销售");
		group.setType("security-role");
		identityService.saveGroup(group);
		
		identityService.createMembership("employee_36", "sale");
	}
	
	@Test
	public void testGroup() {
		Group group = identityService.newGroup("teamPlan");
		group.setName("策划供应商");
		group.setType("assignment");
		identityService.saveGroup(group);
		
		group = identityService.newGroup("teamProduct");
		group.setName("制作供应商");
		group.setType("assignment");
		identityService.saveGroup(group);
	}
	
}
