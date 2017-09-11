<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%-- import CSS --%>
<spring:url value="/resources/css/phoneActiviti/pFlowList.css" var="pFlowListCss"/>
<%-- import JS --%>
<spring:url value="/resources/lib/jquery/jquery-2.0.3.min.js" var="jqueryJs"/>
<spring:url value="/resources/js/common.js" var="commonJs"/>
<spring:url value="/resources/js/phoneActiviti/commonPhone.js" var="commonPhoneJs"/>
<spring:url value="/resources/lib/jquery.json/jquery.json-2.4.min.js" var="jsonJs" />
<spring:url value="/resources/js/phoneActiviti/pFlwList.js" var="pFlwListJs"/>
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

<link rel="stylesheet" href="${pFlowListCss}">
<script type="text/javascript" src="${jqueryJs}"></script>
<script type="text/javascript" src="${jsonJs}"></script>
<script type="text/javascript" src="${commonJs}"></script>
<script type="text/javascript" src="${commonPhoneJs}"></script>




<!--[if lt IE 9]>
        <script>window.html5 || document.write('<script src="html5shivJs"><\/script>')</script>
    <![endif]-->

</head>

<body>
<input type="hidden" id="isScroll" value="101">
 <div class="menuTag tagTwo">
	          <div data-id="0">暂停项目</div>
	          <div data-id="1">进行项目</div>
	          <div data-id="2">历史项目</div>
	          <div class="checkLine"></div>
	     </div>
  
	<div class="pagePhone">
	     
	     <ul class="search">
    			<li class="checkSearch" data-id="0" id="getAll">全部</li>
                <li data-id="1">沟通</li>
                <li data-id="2">方案</li>
                <li data-id="3">商务</li>
                <li data-id="4">制作</li>
                <li data-id="5">交付</li>
	     </ul>
	     
	     <div class="setMission"></div>
	     
	     <div class="setCard">
	      <!--    <div class="otherCard">
	                 <div class="cardTop">
	                     <div class="cardName">项目名称</div>
	                     <div class="cardG">SA</div>
	                     <div class="user">负责人</div>
	                 </div>
	                 <div class="cardBot">
                            <div class="taskName">上传排期表</div>
                            <div class="taskTime">已超时 21h 5min 45s</div>
                            <img class="taskImg" src="/resources/images/pFlow/isBus.png"/>
	                 </div>
	         </div>
	         <div class="MissionCard">
	                 <div class="cardTop">
	                     <div class="cardState">待办</div>
	                     <div class="cardName">项目名称</div>
	                     <div class="cardG">SA</div>
	                     <div class="user">负责人</div>
	                 </div>
	                 <div class="cardBot">
                            <div class="taskName">上传排期表</div>
                            <div class="taskTime">已超时 21h 5min 45s</div>
                            <img class="taskImg" src="/resources/images/pFlow/demoG.png"/>
	                 </div>
	         </div> -->
	     </div>
	</div>
	<!-- video-->
	<script type="text/javascript" src="${pFlwListJs}"></script>
</body>

</html>
