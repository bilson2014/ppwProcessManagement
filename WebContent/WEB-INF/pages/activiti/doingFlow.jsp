<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="r" uri="/mytaglib" %>
<%-- import CSS --%>
<spring:url value="/resources/css/activiti/nomFlow.css" var="textCss"/>
<%-- import JS --%>
<spring:url value="/resources/lib/jquery/jquery-2.0.3.min.js" var="jqueryJs"/>
<spring:url value="/resources/js/common.js" var="commonJs"/>
<spring:url value="/resources/js/activiti/doingFlow.js" var="textFlowJs"/>
<spring:url value="/resources/images" var="imgPath" />
<spring:url value="/resources/lib/jquery.json/jquery.json-2.4.min.js" var="jsonJs" />
<spring:url value="/resources/lib/jquery.json/jquery.json-2.4.min.js" var="" />
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
<script type="text/javascript" src="${jqueryJs}"></script>
<script type="text/javascript" src="${commonJs}"></script>
<script type="text/javascript" src="${jsonJs}"></script>

<!--[if lt IE 9]>
        <script>window.html5 || document.write('<script src="html5shivJs"><\/script>')</script>
    <![endif]-->
</head>

<body>
	<input type="hidden" id="storage_node" value="${file_locate_storage_path }" />
	<input type="hidden" id="height" value="" />
	<input type="hidden" id="num" value="0" />
	<div class="pages">
	<div class="productListAreas">
	           <div class="waitWork">
	                 <div class="titleNameWork">
	                    <div class="name">项目搜索</div>
	                    <input placeholder="项目名称或负责人姓名" id='titleNameInput'>
	                    <div class="search">搜索</div>
	                    <div class="errorItem errorTr">搜索不能为空</div>
	                    <r:group role="sale" >
		                    <div class="createPro" id="toCreate">
		                        <div class="newAdd"></div>
		                        <div data-value="/project/start/project">新建项目</div>
		                    </div>
	                    </r:group>
	                </div>
	           
	                <div class="lineTop"></div>
	                
	                <div class="title">
	                     <div class="titleName" id="daibanName">待办项目(<span id="daiNum"></span>)</div>
	                </div>
	                
	                 <c:if test="${empty gTasks}">
	                     <div class="nodaiban"><img src="/resources/images/index/nodai.png"></div>
	                 </c:if>
	                
	                <c:if test="${!empty gTasks}">
	               
	                <div class="setCard" id="setCard">
						<c:forEach items="${gTasks}" var="staff" varStatus="status">
						   <div class="waitCard cardNum">
						       <div class="share" data-id="${staff.pmsProjectFlow.projectId}">分享</div>
						       <a href="/project/task/${staff.task.id}/${staff.pmsProjectFlow.projectId }/${staff.pmsProjectFlow.processInstanceId }?task">
	                             <div class="cardH">
	                                 <div class="title">${staff.pmsProjectFlow.projectName}</div>
	                                 
	                             </div>
	                             <div class="cardContent">
	                                  <img src="/resources/images/flow/demoG.png">
	                                  <div class="setContent">
	                                      <div class="listName">${staff.task.name}</div>
	           
		                                   <div class="lastTime setLastTime">${staff.task.dueDate}</div>
		                                  
		                                  <c:if test="${staff.isPrincipal == 1}">
		                                    <div class="your">${staff.pmsProjectFlow.principalName}</div>
		                                  </c:if>
		                                  <c:if test="${staff.isPrincipal == 0}">
		                                     <div class="user">负责人:${staff.pmsProjectFlow.principalName}</div>
		                                  </c:if>
	                                  </div>
	                             </div>
	                            </a>  
	                        </div>
							</c:forEach>	                     
	                </div>
	          </c:if>
	           <div class="cardLine"><div></div></div>
	           </div>
	           <div class="otherWork">
	                <div class="title">
	                     <div class="titleName" id="downName"><label id="otherWord">其它项目</label>(<span id="otherNum"></span>)</div>
	                     <div class="orderSelect" id='isOther'>
				                <div id="projectGrade"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="orderCome">
				                   <li data-id="0">全部</li>
				                   <li data-id="1">沟通阶段</li>
				                   <li data-id="2">方案阶段</li>
				                   <li data-id="3">商务阶段</li>
				                   <li data-id="4">制作阶段</li>
				                   <li data-id="5">交付阶段</li>
				                </ul>    
					      </div>
	                </div>
	           <div class="setCard" id="otherCard">
	
	            <c:if test="${not empty runningTasks}">
							<c:forEach items="${runningTasks }" var="staff" varStatus="status">
							     <div class="otherCard setBorder" data-content="${staff.task.dueDate}">
							        <div class="share"  data-id="${staff.pmsProjectFlow.projectId}">分享</div>
							        <a href="/project/task/${staff.task.id}/${staff.pmsProjectFlow.projectId }/${staff.pmsProjectFlow.processInstanceId }?doing">
		                             <div class="cardH">
		                                 <div class="title">${staff.pmsProjectFlow.projectName}</div>
		                                 
		                             </div>
		                             <div class="cardContent">
		                                  <div class="setContent">
		                                      <div class="listName">${staff.task.name}</div>
		                                      <div class="lastTime otherTime">${staff.task.dueDate}</div>		                                    
		                                      <c:if test="${staff.isPrincipal == 1}">
		                                      		<div class="your">${staff.pmsProjectFlow.principalName}</div>
		                                  	  </c:if>
		                                  	  <c:if test="${staff.isPrincipal == 0}">
		                                     		<div class="user">负责人:${staff.pmsProjectFlow.principalName}</div>
		                                  	  </c:if>		                                  
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

<!-- 动态加载信息信息修改 -->
<div class="cusModel" id="shareWeChat">
	<div class="modelCard">
	            <div class="cardTop autoSetTop">
	                   <div class="title">右键复制图片至剪切板<span id="errorInfo"></span> </div>
	                   <div class="closeModel"></div>
	            </div>
				                
	            <img id="shareWeChatCode" src="/mini/qrcode?id=${projectId}">
	            
	</div>
</div>
<script type="text/javascript" src="${textFlowJs}"></script>
</body>

</html>
