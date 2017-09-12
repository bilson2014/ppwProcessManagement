<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%-- import CSS --%>
<spring:url value="/resources/css/phoneActiviti/pFlowFile.css" var="pFlowFileCss"/>
<%-- import JS --%>
<spring:url value="/resources/lib/jquery/jquery-2.0.3.min.js" var="jqueryJs"/>
<spring:url value="/resources/js/common.js" var="commonJs"/>
<spring:url value="/resources/js/phoneActiviti/commonPhone.js" var="commonPhoneJs"/>
<spring:url value="/resources/js/phoneActiviti/pFlwFile.js" var="pFlwFileJs"/>
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

<link rel="stylesheet" href="${pFlowFileCss}">

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
	      <div class="title">项目文件</div>
	      
	      <div class="setFile">
	      
	      </div>
	     <%--  <div class="item">
		         <img class="file" src="${imgPath}/pFlow/avi.png">
		         <img class="fileMore" src="${imgPath}/pFlow/fileMore.png">
		         <div class="fileContent">
		               <div>文件名</div>
		               <div>2017上传人</div>
		         </div>
		         <div class="fileContentMore">
		              <div class="moreItem">
			              <img src="${imgPath}/pFlow/download.png">
			              <div>下载</div>
		              </div>
		              <div class="moreItem">
			              <img src="${imgPath}/pFlow/share.png">
			              <div>分享</div>
		              </div>
		         </div>
	      </div> --%>
	    
	</div>
	
		<!-- webshare -->
	<div class="-mob-share-ui-button -mob-share-open" id="share-open" style="visibility: hidden;"></div>
	<div class="-mob-share-ui" style="display: none">
		<ul class="-mob-share-list">
			<li class="-mob-share-weixin share"><p>微信</p></li>
			<li class="-mob-share-qq"><p>QQ</p></li>
			<li class="-mob-share-weibo share"><p>新浪微博</p></li>
			<li class="-mob-share-qzone"><p>QQ空间</p></li>
		</ul>
		<div class="-mob-share-close">取消</div>
	</div>
	<div class="-mob-share-ui-bg"></div>
	<script id="-mob-share" src="https://f1.webshare.mob.com/code/mob-share.js?appkey=8c49c537a706"></script>
	
 <jsp:include flush="true" page="pHead.jsp"></jsp:include> 
</body>

<script type="text/javascript" src="${jqueryJs}"></script>
<script type="text/javascript" src="${jsonJs}"></script>
<script type="text/javascript" src="${commonJs}"></script>
<script type="text/javascript" src="${commonPhoneJs}"></script>
<script type="text/javascript" src="${pFlwFileJs}"></script>

</html>
