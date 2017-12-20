<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%-- import CSS --%>
<spring:url value="/resources/css/activiti/timebox.css" var="timeboxCSS"/>
<%-- import JS --%>
<spring:url value="/resources/lib/jquery.json/jquery.json-2.4.min.js" var="jsonJs" />
<spring:url value="/resources/js/common.js" var="commonJs" />
<spring:url value="/resources/lib/jquery/jquery-2.0.3.min.js" var="jqueryJs"/>
<spring:url value="/resources/images" var="imgPath" />
<spring:url value="/resources/js/activiti/timebox.js" var="timeboxJs"/>

<!--表格插件引入插件  -->
<spring:url value="/resources/lib/fullcalendar/fullcalendar.css" var="fcCss"/>
<spring:url value="/resources/lib/fullcalendar/fullcalendar.print.css" var="fcpCss"/>
<spring:url value="/resources/lib/fullcalendar/fullcalendar.js" var="fcJs"/>
<spring:url value="/resources/lib/fullcalendar/jquery.min.js" var="jmJs"/>
<spring:url value="/resources/lib/fullcalendar/jquery-ui.custom.min.js" var="jucmJs"/>
<!-- 下拉表格的 -->
<spring:url value="/resources/lib/citySelect/css/city-select.css" var="citysCss"/>
<spring:url value="/resources/lib/citySelect/js/citySelect-1.0.3.js" var="citysJs"/>
<spring:url value="/resources/lib/citySelect/js/cheng.js" var="chengJs"/><!--假数据  -->
<!-- textarea自适应-->
<spring:url value="/resources/lib/flexText-master/css/style.css" var="flexsCss"/>
<spring:url value="/resources/lib/flexText-master/js/jquery.flexText.js" var="flexTextJs"/>
<spring:url value="/resources/lib/flexText-master/js/jquery.js" var="flexJs"/>
<%--去除底部客服 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=9,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>排期表</title>
<script src="${jqueryJs }"></script>
<link rel="stylesheet" href="${flexsCss}">
<link rel="stylesheet" href="${fcCss}">
<link rel="stylesheet" href="${fcpCss}">
<link rel="stylesheet" href="${citysCss}">
<link rel="stylesheet" href="${timeboxCSS}"> 
<script type="text/javascript" src="${jsonJs}"></script>
<script type="text/javascript" src="${commonJs}"></script>
<%-- <script type="text/javascript" src="${jmJs }"></script> --%>
 <script type="text/javascript" src="${fcJs }"></script>
<script type="text/javascript" src="${fcmJs }"></script>
<script type="text/javascript" src="${citysJs }"></script>
<script type="text/javascript" src="${chengJs }"></script>  
<script type="text/javascript" src="${timeboxJs}"></script>
<link rel="shortcut icon" href="${imgPath }/favicon.ico" >
<!--[if lt IE 9]>
        <script>window.html5 || document.write('<script src="html5shivJs"><\/script>')</script>
    <![endif]-->
      <style>
        body {
            margin-top: 40px;
            text-align: center;
            font-size: 14px;
            font-family: "Lucida Grande", Helvetica, Arial, Verdana, sans-serif;
        }
        
        #calendar {
            width: 900px;
            margin: 0 auto;
        }
    </style>
</head>
<body>
<input type="hidden" id="scheduleId" value='33'><!--唯一标记  -->
<%-- <input type="hidden" id="scheduleId" value='${scheduleId}'><!--唯一标记  --> --%>
<input type="hidden" id="projectId" value='${projectId}'><!--项目id  -->
<input type="hidden" id="projectName" value='${projectName}'><!--项目名称  -->
<input type="hidden" id="updateDate" value='${updateDate}'><!--更新时间  -->
<input type="hidden" id="jobContent" value='${jobContent}'><!--工作内容  -->


  <div class="pages" style="height: 900px;">      
	<div id='calendar'></div>
 	<div class='last'><div class='best'>生成排期表</div></div>
 </div>
 <script>
        $(function() {
            /* $('#content').flexText(); */
        });
 </script>
</body>
</html>