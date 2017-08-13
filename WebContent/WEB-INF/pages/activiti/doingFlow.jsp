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
	           
	                 <div class="titleNameWork">
	                    <div class="name">项目名称</div>
	                    <input>
	                    <div class="search btn-c-r">搜索</div>
	                    <div class="createPro">
	                        <div class="newAdd"></div>
	                        <div id="toCreate" data-value="/project/start/project">新建项目</div>
	                    </div>
	                </div>
	           
	                <div class="lineTop"></div>
	                <c:if test="${!empty gTasks}">
	                <div class="title">
	                     <div class="titleName" id="upName">待办任务</div>
	                </div>
	                <div class="setCard" id="setCard">
							<c:forEach items="${gTasks }" var="staff" varStatus="status">
						   <div class="waitCard">
						       <a href="/project/task/${staff.task.id}?task">
	                             <div class="cardH">
	                                 <div class="title">${staff.pmsProjectFlow.projectName}</div>
		                                  <c:if test="${staff.isPrincipal == 1}">
		                                    <div class="your">${staff.pmsProjectFlow.principalName}</div>
		                                  </c:if>
		                                  <c:if test="${staff.isPrincipal == 0}">
		                                     <div class="user">负责人:${staff.pmsProjectFlow.principalName}</div>
		                                  </c:if>
	                             </div>
	                             <div class="cardContent">
	                                  <img src="/resources/images/flow/demoG.png">
	                                  <div class="setContent">
	                                      <div class="listName">${staff.task.name}</div>
	                                      <div class="lastTime setLastTime">${staff.task.dueDate}</div>
	                                  </div>
	                             </div>
	                            </a>  
	                        </div>
							</c:forEach>
					
	                       <!--  <div class="waitCard">
	                             <div class="cardH">
	                                 <div class="title">这里是卡片的标题啊啊啊啊</div>
	                                 <div class="point">
	                                      <div class="showPoint">SA</div>
	                                      <div class="showDeil">
	                                            <div class="proPoint">项目评级<span>S</span></div>
	                                            <div class="cusPoint">客户评级<span>A</span></div>
	                                      </div>
	                                 </div>
	                                 <div class="your">负责人<span>她她她</span></div>
	                             </div>
	                             <div class="cardContent">
	                                  <img src="/resources/images/flow/demoY.png">
	                                  <div class="setContent">
	                                      <div class="listName">上传周期表</div>
	                                      <div class="lastTime">已超时 24h 5min 45s</div>
	                                  </div>
	                             </div>
	                        </div> -->
	                     
	                </div>
	          </c:if>
	           <div class="cardLine"><div></div></div>
	           </div>
	           <div class="otherWork">
	                <div class="title">
	                     <div class="titleName" id="downName">其它任务</div>
	                </div>
	           <div class="setCard" id="otherCard">
	           
	            <c:if test="${!empty runningTasks}">
							<c:forEach items="${runningTasks }" var="staff" varStatus="status">
							     <div class="otherCard">
							        <a href="/project/task/${staff.task.id}">
		                             <div class="cardH">
		                                 <div class="title">${staff.pmsProjectFlow.projectName}</div>
		                                  <c:if test="${staff.isPrincipal == 1}">
		                                    <div class="your">${staff.pmsProjectFlow.principalName}</div>
		                                  </c:if>
		                                  <c:if test="${staff.isPrincipal == 0}">
		                                     <div class="user">负责人:${staff.pmsProjectFlow.principalName}</div>
		                                  </c:if>
		                             </div>
		                             <div class="cardContent">
		                                  <div class="setContent">
		                                      <div class="listName">${staff.task.name}</div>
		                                      <div class="lastTime otherTime">${staff.task.dueDate}</div>
		                                  </div>
		                                  <c:if test="${staff.taskStage == '沟通阶段'}">
		                                  <img src="/resources/images/flow/isTalk.png">
		                                  </c:if>
		                                  <c:if test="${ staff.taskStage == '方案阶段'}">
		                                  <img src="/resources/images/flow/isFang.png">
		                                  </c:if>
		                                  <c:if test="${ staff.taskStage == '商务阶段'}">
		                                  <img src="/resources/images/flow/isPrice.png">
		                                  </c:if>
		                                  <c:if test="${ staff.taskStage == '制作阶段'}">
		                                  <img src="/resources/images/flow/isMake.png">
		                                  </c:if>
		                                  <c:if test="${staff.taskStage == '交付阶段'}">
		                                  <img src="/resources/images/flow/isPay.png">
		                                  </c:if>
		                             </div>
		                            </a> 
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
