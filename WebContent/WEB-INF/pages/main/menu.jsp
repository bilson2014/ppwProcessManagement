<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<ul id="css3menu">
	<li class="topfirst"><a rel="main/welcome">首页</a></li>
	
	<li>
		<a rel="#">动态表单</a>
		<ul>
			<li><a rel="form/dynamic/process-list">流程列表(动态)</a></li>
			<li><a rel="form/dynamic/task/list">任务列表(动态)</a></li>
			<li><a rel="form/dynamic/process-instance/running/list">运行中流程表(动态)</a></li>
			<li><a rel="form/dynamic/process-instance/finished/list">已结束流程(动态)</a></li>
		</ul>
	</li>
	
	<li>
		<a rel="#">项目流程</a>
		<ul>
			<li><a rel="form/project/process-list">流程列表</a></li>
			<li><a rel="form/project/task/list">代办任务列表</a></li>
			<li><a rel="form/project/process-instance/running/list">运行中流程表</a></li>
			<li><a rel="form/project/process-instance/finished/list">已结束流程</a></li>
		</ul>
	</li>
	
	<li>
		<a rel="#">管理模块</a>
		<ul>
			<li>
				<a rel='#'>流程管理</a>
				<ul>
					<li><a rel='workflow/process-list'>流程定义及部署管理</a></li>
					<li><a rel='workflow/processinstance/running'>运行中流程</a></li>
					<li><a rel='workflow/model/list'>模型工作区</a></li>
				</ul>
			</li>
		</ul>
	</li>
	
</ul>