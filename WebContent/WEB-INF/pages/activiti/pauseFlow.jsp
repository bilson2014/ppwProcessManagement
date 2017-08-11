<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- import CSS --%>
<spring:url value="/resources/css/activiti/nomFlow.css" var="textCss"/>
<%-- import JS --%>
<spring:url value="/resources/lib/jquery/jquery-2.0.3.min.js" var="jqueryJs"/>
<spring:url value="/resources/js/common.js" var="commonJs"/>
<spring:url value="/resources/js/activiti/doingFlow.js" var="textFlowJs"/>
<spring:url value="/resources/images" var="imgPath" />
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->

<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=9,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="keywords" content="">
<meta name="description" content="">
<meta name="baidu-site-verification" content="dMz6jZpIwd" />
<title></title>
<link rel="stylesheet" href="${textCss}">
<script type="text/javascript" src="resources/lib/Clamp/clamp.js"></script>
<script type="text/javascript" src="${jqueryJs}"></script>
<script type="text/javascript" src="${commonJs}"></script>
<script type="text/javascript" src="${textFlowJs}"></script>

<!--[if lt IE 9]>
        <script>window.html5 || document.write('<script src="html5shivJs"><\/script>')</script>
    <![endif]-->


</head>

<body>

	<input type="hidden" id="storage_node" value="${file_locate_storage_path }" />
	<div class="pages">
	
	<div class="productListAreas">
	           <div class="waitWork">
	                <div class="lineTop"></div>
	               
	                <div class="title">
	                     <div class="titleName" id="upName">暂停任务</div>
	                </div>
	                <div class="setCard" id="setCard">
	                
	                	 <c:if test="${!empty suspendTasks}">
							<c:forEach items="${suspendTasks}" var="staff" varStatus="status">
							     <div class="otherCard">
		                             <div class="cardH">
		                                 <div class="title">${staff.pmsProjectFlow.projectName}</div>
		                                 <div class="user">负责人:${staff.pmsProjectFlow.principalName}</div>
		                             </div>
		                             <div class="cardContent">
		                                  <div class="setContent">
		                                      <div class="lastTime">${staff.task.name} ${staff.task.assignee }</div>
		                                  </div>
		                                  <img src="/resources/images/flow/isPause.png">
		                             </div>
		                        </div>
							</c:forEach>
					</c:if>
	                  
	                </div>
	           </div>
	</div> 
	
</div>	  
	<!-- video-->
</body>

</html>
