<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%-- import CSS --%>
<spring:url value="/resources/css/phoneActiviti/pFlowInfoTask.css" var="pFlowInfoTaskCss"/>
<spring:url value="/resources/lib/merge/iosSelect.css" var="iosSelectCss" />
<spring:url value="/resources/lib/Bootstrap/css/bootstrap.min.css" var="bootstrapCss" />
<spring:url value="/resources/lib/webuploader/webuploader.css" var="webuploaderCss" />


<spring:url value="/resources/lib/mobScroll/css/bootstrap.min.css" var="timeCss1"/>
<spring:url value="/resources/lib/mobScroll/css/common.css" var=""/>
<spring:url value="/resources/lib/mobScroll/css/info_self.css" var="timeCss2"/>
<spring:url value="/resources/lib/mobScroll/css/mobiscroll.android-ics-2.5.2.css" var="timeCss3"/>
<spring:url value="/resources/lib/mobScroll/css/mobiscroll.animation-2.5.2.css" var="timeCss4"/>
<spring:url value="/resources/lib/mobScroll/css/mobiscroll.core-2.5.2.css" var="timeCss5"/>
<spring:url value="/resources/lib/mobScroll/css/reset.css" var="timeCss6"/>
<%-- import JS --%>
<spring:url value="/resources/lib/jquery/jquery-2.0.3.min.js" var="jqueryJs"/>
<spring:url value="/resources/js/common.js" var="commonJs"/>
<spring:url value="/resources/js/phoneActiviti/commonPhone.js" var="commonPhoneJs"/>
<spring:url value="/resources/lib/jquery.json/jquery.json-2.4.min.js" var="jsonJs" />
<spring:url value="/resources/js/phoneActiviti/pFlowInfoTask.js" var="pFlowInfoTaskJs"/>
<spring:url value="/resources/lib/merge/iosSelect.js" var="iosSelectJs" />
<spring:url value="/resources/lib/webuploader/webuploader.js" var="webuploaderJs" />

<spring:url value="/resources/lib/mobScroll/js/mobiscroll.android-ics-2.5.2.js" var="time1"/>
<spring:url value="/resources/lib/mobScroll/js/mobiscroll.core-2.5.2-zh.js" var="time3"/>
<spring:url value="/resources/lib/mobScroll/js/mobiscroll.core-2.5.2.js" var="time2"/>
<spring:url value="/resources/lib/mobScroll/js/mobiscroll.datetime-2.5.1-zh.js" var="time5"/>
<spring:url value="/resources/lib/mobScroll/js/mobiscroll.datetime-2.5.1.js" var="time4"/>
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
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;"  />
<meta name="keywords" content="">
<meta name="description" content="">
<title></title>

<link rel="stylesheet" href="${pFlowInfoTaskCss}">
<link rel="stylesheet" href="${iosSelectCss}">
<link rel="stylesheet" href="${bootstrapCss}">
<link rel="stylesheet" href="${webuploaderCss}">

<link rel="stylesheet" href="${timeCss1}">
<link rel="stylesheet" href="${timeCss2}">
<link rel="stylesheet" href="${timeCss3}">
<link rel="stylesheet" href="${timeCss4}">
<link rel="stylesheet" href="${timeCss5}">
<link rel="stylesheet" href="${timeCss6}">


<!--[if lt IE 9]>
        <script>window.html5 || document.write('<script src="html5shivJs"><\/script>')</script>
    <![endif]-->

</head>

<body>
  
   <input type="hidden" id="projectId" value="${projectId}">
   <input type="hidden" id="taskId" value="${taskId}">
   <input type="hidden" id="processInstanceId" value="${processInstanceId}">
   <input type="hidden" id="projectName" value="${projectName}">
   <input type="hidden" id="url" value="/${taskId}/${projectId}/${processInstanceId}">
   <input type="hidden" id="initTask" value="initTask">
   
   <div class="modelTool" id="showInfo">
	          <div class="success">
	            <div class="modelTitle">提示</div>
	            <div id="setInfo">请确认当前任务无误，<br>确认后即将进入项目下一个任务</div>
	            <div id="closeInfo">返回</div>
	            <div id="checkInfo">确认</div>
	         </div>
	</div>
  
	<div class="pagePhone">
	
	       <div class="setContent">
	            <div class="contentTitle">
	                  <div class="names">${taskName}</div>
	                  <div class="time" id="missinTime">${dueDate}</div>
	            </div>
	            <div class="taskDescription">${taskDescription}</div>
	             <div class="upProgress singleProgress" style="margin-bottom:40px;" id="singleUp">
								<div class="proTitle" id="proTitle">上传进度</div>
								<div  class="progress progress-striped active">
									<div id="setWidth" class="progress-bar progress-bar-danger progress-bar-striped" role="progressbar"
										aria-valuenow="0" aria-valuemin="0" aria-valuemax="100"
										style="width: 0;"></div>
								</div>
								<div class="upIng">上传中...</div>
								<div class="upSuccess">
									<img src="/resources/images/provider/sure.png">上传成功
								</div>
								<div class="upError">
									<img src="/resources/images/provider/error.png">上传失败,请关闭窗口重新上传
								</div>
		       </div>
	            <div class="content" id="setContent">
	                
	            </div>
	             <div id="daiban">
                             <img src="/resources/images/pFlow/daiban.png">
                             <div id="daibanword">您没有任何待办任务~</div>	                 
	                 </div>
	       </div>
	</div>
	
 <jsp:include flush="true" page="pHead.jsp"></jsp:include> 
</body>

<script type="text/javascript" src="${jqueryJs}"></script>
<script type="text/javascript" src="${jsonJs}"></script>
<script type="text/javascript" src="${commonJs}"></script>
<script type="text/javascript" src="${commonPhoneJs}"></script>
<script type="text/javascript" src="${webuploaderJs}"></script>

<script type="text/javascript" src="${iosSelectJs}"></script>
<script type="text/javascript" src="${time1}"></script>
<script type="text/javascript" src="${time2}"></script>
<script type="text/javascript" src="${time3}"></script>
<script type="text/javascript" src="${time4}"></script>
<script type="text/javascript" src="${time5}"></script>
<script type="text/javascript" src="${pFlowInfoTaskJs}"></script>

</html>
