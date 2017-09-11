<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%-- import CSS --%>
<spring:url value="/resources/css/phoneActiviti/pFlowInfoTask.css" var="pFlowInfoTaskCss"/>
<spring:url value="/resources/lib/merge/iosSelect.css" var="iosSelectCss" />
<%-- import JS --%>
<spring:url value="/resources/lib/jquery/jquery-2.0.3.min.js" var="jqueryJs"/>
<spring:url value="/resources/js/common.js" var="commonJs"/>
<spring:url value="/resources/js/phoneActiviti/commonPhone.js" var="commonPhoneJs"/>
<spring:url value="/resources/lib/jquery.json/jquery.json-2.4.min.js" var="jsonJs" />
<spring:url value="/resources/js/phoneActiviti/pFlowInfoTask.js" var="pFlowInfoTaskJs"/>
<spring:url value="/resources/lib/merge/iosSelect.js" var="iosSelectJs" />
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

<link rel="stylesheet" href="${pFlowInfoTaskCss}">
<link rel="stylesheet" href="${iosSelectCss}">

<!--[if lt IE 9]>
        <script>window.html5 || document.write('<script src="html5shivJs"><\/script>')</script>
    <![endif]-->

</head>

<body>
  
	<div class="pagePhone">
	       <div class="setContent">
	            <div class="contentTitle">
	                  <div class="name">任务名字</div>
	                  <div class="time">时间</div>
	            </div>
	            <div class="content">
		                <div class="item">
		                     <div class="name">大大</div>
		                     <input  />
		                </div>
		                <div class="item">
		                     <div class="name">大大</div>
		                     <div class="orderSelect">
		                            <input readonly class="setSelect"  id="setinput"/>
		                            <div></div>
		                     </div>
		                </div>
	            </div>
	       </div>
	</div>
	
 <jsp:include flush="true" page="pHead.jsp"></jsp:include> 
</body>

<script type="text/javascript" src="${jqueryJs}"></script>
<script type="text/javascript" src="${jsonJs}"></script>
<script type="text/javascript" src="${commonJs}"></script>
<script type="text/javascript" src="${commonPhoneJs}"></script>
<script type="text/javascript" src="${iosSelectJs}"></script>
<script type="text/javascript" src="${pFlowInfoTaskJs}"></script>

</html>
