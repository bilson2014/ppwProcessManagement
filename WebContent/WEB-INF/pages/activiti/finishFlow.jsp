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
	<div class="pages">
	<div class="productListAreas">
	           <div class="otherWork">
	           
	                <div class="titleNameWork">
	                    <div class="name">项目名称</div>
	                    <input>
	                    <div class="search">搜索</div>
	                      <r:group role="sale">
		                    <div class="createPro" id="toCreate">
		                        <div class="newAdd"></div>
		                        <div data-value="/project/start/project">新建项目</div>
		                    </div>
	                    </r:group>
	                </div>
	                <div class="title">
	                     <div class="titleName hide" id="daibanName">待办任务(<span id="daiNum"></span>)</div>
	                </div>

	                <div class="setCard" id="setCard"></div>
	                <div class="lineTop"></div>
	                <div class="title">
	                     <div class="titleName" id="downName"><label id="otherWord">完成任务</label>(<span id="otherNum"></span>)</div>
	                </div>
	                <div class="setCard" id="otherCard">
	                	 <c:if test="${!empty finishedTasks}">
							<c:forEach items="${finishedTasks }" var="staff" varStatus="status">
						   		<div class="otherCard">
	                                <div class="share"  data-id="${staff.pmsProjectFlow.projectId}">分享</div>
									<c:if test="${staff.pmsProjectFlow.projectStatus == 'cancel'}">
				                       <a href="/project/task/${staff.task.id}/${staff.pmsProjectFlow.projectId }/${staff.pmsProjectFlow.processInstanceId}?cancel">
				                    </c:if>
				                    <c:if test="${staff.pmsProjectFlow.projectStatus == 'finished'}">
				                    	<a href="/project/task/${staff.taskId}/${staff.pmsProjectFlow.projectId }/${staff.pmsProjectFlow.processInstanceId}?status=finished">
				                    </c:if>
							      	
			                             <div class="cardH">
			                                 <div class="title">${staff.pmsProjectFlow.projectName}</div>
				                                
			                             </div>
			                             <div class="cardContent">
			                             	<c:if test="${staff.pmsProjectFlow.projectStatus == 'cancel'}">
			                                  <img src="/resources/images/flow/isCancle.png">
			                             	</c:if>
			                             	<c:if test="${staff.pmsProjectFlow.projectStatus == 'finished'}">
			                                  <img src="/resources/images/flow/isFinish.png">
			                             	</c:if>
			                                  <div class="setContent">
			                                  		<c:if test="${staff.pmsProjectFlow.projectStatus == 'cancel'}">
				                                      <div class="lastTime finishTime">${staff.pmsProjectFlow.finishedDate}</div>
				                                    </c:if>
				                                    <c:if test="${staff.pmsProjectFlow.projectStatus == 'finished'}">
				                                      <div class="lastTime finishTime">${staff.historicProcessInstance.endTime}</div>
				                                    </c:if>
				                            
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
						 </c:if>
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
	<!-- video-->
</body>

</html>
