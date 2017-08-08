<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="en">
<head>
	<%@ include file="/resources/common/global.jsp"%>
	<title>已结束列表</title>
	<%@ include file="/resources/common/meta.jsp" %>
    <%@ include file="/resources/common/include-base-styles.jsp" %>
    <%@ include file="/resources/common/include-jquery-ui-theme.jsp" %>
    <link href="${ctx }/resources/js/common/plugins/jui/extends/timepicker/jquery-ui-timepicker-addon.css" type="text/css" rel="stylesheet" />
    <link href="${ctx }/resources/js/common/plugins/qtip/jquery.qtip.min.css" type="text/css" rel="stylesheet" />
    <%@ include file="/resources/common/include-custom-styles.jsp" %>
</head>

<body>
	<table>
		<tr>
			<th>流程ID</th>
			<th>流程定义ID</th>
			<th>流程启动时间</th>
			<th>流程结束时间</th>
			<th>流程结束原因</th>
		</tr>

		<c:forEach items="${list }" var="hpi">
		<tr>
			<td>${hpi.id }</td>
			<td>${hpi.processDefinitionId }</td>
			<td>${hpi.startTime }</td>
			<td>${hpi.endTime }</td>
			<td>${empty hpi.deleteReason ? "正常结束" : hpi.deleteReason}</td>
		</tr>
		</c:forEach>
	</table>
	<!-- 办理任务对话框 -->
	<div id="handleTemplate" class="template"></div>

</body>
</html>
