<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%-- import CSS --%>
<spring:url value="/resources/css/phoneActiviti/pFlowStep.css" var="pFlowStepInfoCss"/>
<%-- import JS --%>
<spring:url value="/resources/lib/jquery/jquery-2.0.3.min.js" var="jqueryJs"/>
<spring:url value="/resources/js/common.js" var="commonJs"/>
<spring:url value="/resources/js/phoneActiviti/commonPhone.js" var="commonPhoneJs"/>
<spring:url value="/resources/lib/jquery.json/jquery.json-2.4.min.js" var="jsonJs" />
<spring:url value="/resources/js/phoneActiviti/pFlowStep.js" var="pFlowStepInfoJs"/>
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
<title></title>
<link rel="stylesheet" href="${pFlowStepInfoCss}">
<!--[if lt IE 9]>
        <script>window.html5 || document.write('<script src="html5shivJs"><\/script>')</script>
<![endif]-->

</head>

<body>  


   <input type="hidden" id="projectId" value="${projectId}">
   <input type="hidden" id="taskName" value="${taskName}">
   <input type="hidden" id="processInstanceId" value="${processInstanceId}">
   <input type="hidden" id="projectName" value="${projectName}">
   <input type="hidden" id="url" value="/${taskId}/${projectId}/${processInstanceId}">


	<div class="pagePhone">
          	      <div class="title">项目流程及历史</div>
          	     <div class="step">
          	       
          	         <div class="stepItem">
          	                 <div class="itemTop" id="step1">
          	                      <div class="icon"></div> 
          	                      <div class="name">沟通阶段</div> 
          	                      <div class="openIcon"></div> 
          	                 </div>
          	                 <div class="line"></div>
          	                 <div class="itemInfo">
          	                        <div class="setContent">
          	                               <img src="/resources/images/pFlow/sDeil.png" />
          	                               <img src="/resources/images/pFlow/check.png" />
          	                               <div>名字名字</div>
          	                        </div>
          	                        <div class="setContent">
          	                               <img src="/resources/images/pFlow/sDeil.png" />
          	                               <img src="/resources/images/pFlow/check.png" />
          	                               <div>名字名字</div>
          	                        </div>
          	                        <div class="setContent">
          	                               <img src="/resources/images/pFlow/sDeil.png" />
          	                               <img src="/resources/images/pFlow/check.png" />
          	                               <div>名字名字</div>
          	                        </div>
          	                 </div>
          	                 <div class="line"></div>
          	         </div>
          	         <div class="stepItem">
          	                 <div class="itemTop yellowItem" id="step2">
          	                      <div class="icon"></div> 
          	                      <div class="name">方案阶段</div> 
          	                      <div class="openIcon"></div> 
          	                 </div>
          	                 <div class="line"></div>
          	                 <div class="itemInfo"></div>
          	                 <div class="line"></div>
          	         </div>
          	         <div class="stepItem">
          	                 <div class="itemTop greenItem" id="step3">
          	                      <div class="icon"></div> 
          	                      <div class="name">商务阶段</div> 
          	                      <div class="openIcon"></div> 
          	                 </div>
          	                 <div class="line"></div>
          	                 <div class="itemInfo"></div>
          	                 <div class="line"></div>
          	         </div>
          	         <div class="stepItem">
          	                 <div class="itemTop pauseItem" id="step4">
          	                      <div class="icon"></div> 
          	                      <div class="name">制作阶段</div> 
          	                      <div class="openIcon"></div> 
          	                 </div>
          	                 <div class="line"></div>
          	                 <div class="itemInfo"></div>
          	                 <div class="line"></div>
          	         </div>
          	         <div class="stepItem">
          	                 <div class="itemTop cancleItem" id="step5">
          	                      <div class="icon"></div> 
          	                      <div class="name">交付阶段</div> 
          	                      <div class="openIcon"></div> 
          	                 </div>
          	                 <div class="itemInfo"></div>

          	         </div>
          	       
          	     </div>
	</div>
 <jsp:include flush="true" page="pHead.jsp"></jsp:include> 
</body>

<script type="text/javascript" src="${jqueryJs}"></script>
<script type="text/javascript" src="${jsonJs}"></script>
<script type="text/javascript" src="${commonJs}"></script>
<script type="text/javascript" src="${commonPhoneJs}"></script>
<script type="text/javascript" src="${pFlowStepInfoJs}"></script>

</html>
