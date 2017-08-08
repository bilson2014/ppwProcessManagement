<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>

<html>
<c:set value="${pageContext.request.contextPath}" var="ctx"></c:set>
<head>
	<title>代办任务</title>
	<script type="text/javascript" src="${ctx }/resources/lib/jquery-1.8.3.js"></script>
	<script type="text/javascript" src="${ctx }/resources/js/task.js"></script>
</head>
<body>
	<h2>Hello World!</h2>
	
	<table>
		<thead>
			<tr>
				<th>假种</th>
				<th>申请人</th>
				<th>申请时间</th>
				<th>开始时间</th>
				<th>结束时间</th>
				<th>当前节点</th>
				<th>任务创建时间</th>
				<th>流程状态</th>
				<th>操作</th>
			</tr>
		</thead>

		<tbody>
			<c:forEach items="${list }" var="vacation">
				<c:set var="task" value="${vacation.task }" />
				<c:set var="pi" value="${vacation.processInstance }" />

				<tr>
					<td>${vacation.leaveType }</td>
					<td>${vacation.userId }</td>
					<td>${vacation.createDate }</td>
					<td>${vacation.startTime }</td>
					<td>${vacation.endTime }</td>
					<td>${task.name }</td>
					<td>${task.createTime }</td>
					<td>${pi.suspended ? "已挂起" : "正常" }</td>
					<td>
						<c:if test="${empty task.assignee }">
							<a class="claim" href="${ctx }/vacation/task/claim/${author }/${task.id}">签收</a>
						</c:if>
						<c:if test="${not empty task.assignee }">
							<a class="agree" tkey='${task.taskDefinitionKey }' tname='${task.name }' taskId="${task.id }" href="javascript:void(0);">同意</a>
							<a class="disagree" tkey='${task.taskDefinitionKey }' tname='${task.name }' taskId="${task.id }" href="javascript:void(0);">不同意</a>
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>
