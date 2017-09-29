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
<spring:url value="/resources/js/phoneActiviti/pFlwSearch.js" var="pFlwSearchJs"/>
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
 
	<div class="pagePhone">
         <div class="searchProject">
              <input>
              <img id="toSearch" src="/resources/images/index/toSearch.png">
         </div>
	     <div class="setMission">
	           <div class="MissionCard" id="setCard">
	                 
	         </div> 
	     </div>
	     <div class="setCard" id="otherCard">
	      
	     </div>
	</div>
	
	<script type="text/javascript" src="${pFlwSearchJs}"></script>
</body>

</html>
