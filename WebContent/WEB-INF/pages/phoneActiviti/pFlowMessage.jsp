<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%-- import CSS --%>
<spring:url value="/resources/css/phoneActiviti/pFlowMessage.css" var="pFlowMessageCss"/>
<%-- import JS --%>
<spring:url value="/resources/lib/jquery/jquery-2.0.3.min.js" var="jqueryJs"/>
<spring:url value="/resources/js/common.js" var="commonJs"/>
<spring:url value="/resources/js/phoneActiviti/commonPhone.js" var="commonPhoneJs"/>
<spring:url value="/resources/lib/jquery.json/jquery.json-2.4.min.js" var="jsonJs" />
<spring:url value="/resources/js/phoneActiviti/pFlowMessage.js" var="pFlowMessageJs"/>
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
<link rel="stylesheet" href="${pFlowMessageCss}">
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
	       <div class="title">
	            <img src="/resources/images/pFlow/message.png">
	            <img class="showMessage" src="/resources/images/pFlow/edit.png">
	            <div >留言</div>
	       </div>
	       <div class="setMessage" data-content="">
	            <textarea id="addmessage"></textarea>
	            <div class="btn-c-r" id="submitTalkInfo">确认</div>
	       </div>
	       
	       <div class="setMessageContent">
	             <!--  <div class="item">
                        <img class="itemMore" src="/resources/images/pFlow/moreMessage.png">
	                    <div class="content">
	                            <div class="contentItem">
	                                   <div class="name">名字时间20171818</div>
	                                   <div class="itemContent">内容内容内容内</div>               
	                            </div>
	                             <div class="contentItem">
	                                   <div class="name">名字时间20171818</div>
	                                   <div class="itemContent">内容内容内容内容内容内容内容内容内容内容内容内容内容内容
	                                                                         内容内容内容内容内容内容内容内容内容内容内容内容内容内容</div>               
	                            </div>
	                    </div>
	                    <div class="reTalkItem" data-content="error">
		                    <textarea></textarea>
		                    <div class="btn-c-r reTalk">回复</div>
		                </div>    
	                    <div class="itemLine"></div>
	              </div> -->
	       </div>
	</div>
	
 <jsp:include flush="true" page="pHead.jsp"></jsp:include> 
</body>

<script type="text/javascript" src="${jqueryJs}"></script>
<script type="text/javascript" src="${jsonJs}"></script>
<script type="text/javascript" src="${commonJs}"></script>
<script type="text/javascript" src="${commonPhoneJs}"></script>
<script type="text/javascript" src="${pFlowMessageJs}"></script>

</html>
