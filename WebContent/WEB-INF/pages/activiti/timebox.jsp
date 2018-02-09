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
     <!--  <style>
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
    </style> -->
</head>
<body>

<%-- <input type="hidden" id="scheduleId" value='${scheduleId}'><!--唯一标记  -->
<input type="hidden" id="projectId" value='${projectId}'><!--项目id  --> --%>
  <div class="pages" >   
  <div class='titles'>
  	<span>排期表生成器</span>
  	<span id="projectName">${projectName}</span>
  	<div class='searchBtn' id='openFrom'>打开项目</div>
  </div>
<%--   <div class="orderItem" id="projectNameError">
	   <div class="mR8">项目名称</div>
	   <input value='${projectName}' id="projectName" style="width:240px">
	   <p class='proerr'><p>
 </div> --%>
 <div class='divine'></div> 
<%--  <div class="orderItem" id="dayTimeError">
	   <div class="mR8">日期</div>
	   <input class="time noBorder" readonly id="updateDate" name="time" value="${updateDate}">
  </div>   --%> 
	<div id='calendar' onClick="event.cancelBubble = true"></div>
	<div class='advice'>*本时间表为预排，基于每个环节的按时确认可顺利执行，否则制作进度都会受确认环节或其他不可抗因素的变化相应的变化。</div>
 	<!-- <div class='last'><div class='best'>生成排期表</div></div> -->
 	
 	<div class="createQuo">
         <div class="btn-c-r createFrom best">导出</div>
         <div class="btn-c-r createFromTable">保存至项目</div>
    </div>
 	<!--遮罩打开项目  -->
 	<div class="cusModel" id="loadProductModel">
     	<div class="modelCard">
            <div class="cardTop">
                   <div class="title">项目排期表</div>
                   <div class="closeModel"></div>
            </div>
            <div class="modelBanner">
                <div class="tap active" id="" style="width:100%">您正在参与进行中的项目</div>
            </div>
            <div class="modelProductContent"> </div>
            <div class="modelControl">
                 <div class="btn-c-g" id="cancleLoadProduct">取消</div>
                 <div class="btn-c-r" id="CheckloadProduct">加载</div>
            </div>     
    	</div>
	</div>
	
	<!--确认覆盖的 遮罩  -->
	<div class="cusModel" id="clearTable" style="z-index:1000" >
     	<div class="modelCard">
            <div class="cardTop">
                <div class="title" >排期表信息</div>
                <div class="closeModel"></div>
            </div>
            <div class="errorContent">
                 <div class="title" id="setTableTitle" style="line-height: 24px;"></div>
                 <div class="btnMid">
                      <div class="btn-c-g cancle">取消</div>
                      <div class="btn-c-r sureClear">确认</div>
                 </div>
            </div>
     	</div>
	</div>
	<!--保存项目确认  -->
	<div class="cusModel" id="submitCheck" >
     	<div class="modelCard">
            <div class="cardTop">
                <div class="title" id="isSuccess">提交成功</div>
                <div class="closeModel"></div>
            </div>
            <div class="infoWarn">
                  <img style="margin: 0 auto;" id="errorImg" src="${imgPath}/index/waring.png">
            </div>
            <div class="errorContent">
                 <div id="successContent" style="text-align: center;"></div>
                 <div class="btnMid" style="text-align: center;">
                     <div class="btn-c-r sureCheck" style="margin-right:0px!important">确认</div>
                 </div>
            </div>
    	</div>
	</div>
	<!--更新项目排期表表  -->
	<div class="cusModel" id="errorSaveModel">
        <div class="modelCard" >
	        <div class="cardTop">
	           <div class="title">更新项目排期表</div>
	           <div class="closeModel"></div>
	        </div>
            <div class="infoWarn">
               <img src="${imgPath}/index/waring.png" style="margin: 0 auto;">
               <div>是否更新该项目排期表？</div>
            </div>
            <div class="btnMid">
			    <div class="btn-c-r SaveModelBtn" style="position: relative;left: 80px;margin-bottom: 30px;">确定</div>
		    </div>
        </div>
	</div>
	<!--保存至项目的时候   -->
 	<div class="cusModel" id="showProductName">
    	<div class="modelCard">
            <div class="cardTop">
                   <div class="title">保存项目</div>
                   <div class="closeModel"></div>
            </div>
            <div class="modelBanner">
                <div class="tap active" id="" style="width:100%">您正在参与进行中的项目</div>
            </div>
            <div class="modelProductContent" id="productSelect"> </div>
            <div class="modelControl">
                 <div class="btn-c-g" id="cancleSavesProductName">取消</div>
                 <div class="btn-c-r" id="savesProductName">保存</div>
            </div>     
     	</div>
	</div>
	<!--提交的数据  -->
	<form method="post" action="/schedule/export" id="toListForm" class="hide">
		<input type='hidden' name="scheduleId" id="scheduleId" />
		<input type='hidden' name="projectId" id="projectId"/>
		<input type='hidden' name="projectName" id="projectNames"/>
		<input type='hidden' name="updateDate" id="updateDate"/>		
		<input type='hidden' name="itemContent" id="items"/>
	</form> 
	<!--跳板隐藏的数据  -->
	<input type='hidden' name='chengnum' id='pumpum'/>
	<input type='hidden' name='chengnum' id='pums'/>    
 </div>
</body>
</html>