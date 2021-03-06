<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%-- import CSS --%>
<spring:url value="/resources/css/activiti/flowMenu.css" var="flowMenuCss"/>
<spring:url value="/resources/lib/AirDatepicker/dist/css/datepicker.min.css" var="datepickerCss" />
<spring:url value="/resources/lib/AirDatepicker/dist/js/datepicker.min.js" var="datepickerJs" />
<spring:url value="/resources/lib/AirDatepicker/dist/js/i18n/datepicker.zh.js" var="datepickerZhJs" />
<%-- import JS --%>
<spring:url value="/resources/lib/jquery/jquery-2.0.3.min.js" var="jqueryJs"/>
<spring:url value="/resources/js/activiti/flowMenu.js" var="flowMenuJs"/>
<spring:url value="/resources/images" var="imgPath" />
<spring:url value="http://localhost:8080/" var="url" />
<%-- <spring:url value="http://www.apaipian.com:8087/" var="url" /> --%>
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
<link rel="stylesheet" href="${flowMenuCss}">
<link rel="stylesheet" href="${datepickerCss}">
<script type="text/javascript" src="${jqueryJs}"></script>
<%-- <script type="text/javascript" src="${datepickerJs}"></script>
<script type="text/javascript" src="${datepickerZhJs}"></script> --%>
<script type="text/javascript" src="${flowMenuJs}"></script>

<!--[if lt IE 9]>
        <script>window.html5 || document.write('<script src="html5shivJs"><\/script>')</script>
    <![endif]-->
</head>
<body>
	<input type="hidden" id="storage_node" value="${file_locate_storage_path }" />
     
	    <div class="flowMenu">
	        <div id="shortMenu">
	                <div class="head" id="menuHead"><img src="${imgPath}/flow/open.png"></div>
	                 <a href="${url}mgr/favourites"><div class="menuItem"><div class="createIcon"></div></div></a>
	                <div class="menuItem"><div class="myPro" id="minMyPro"></div></div>
	                <div class="menuItem hide"><div class="orderList"></div></div>
	                <a href="${url}mgr/favourites"><div class="menuItem"><div class="save"></div></div></a>
	                <!-- <div class="menuItem"><div class="safe"></div></div> -->
	                <div class="bottom"><div class="loginOut"></div></div>
	        </div>
	       
	       <div id="mainMenu">
	        <img class="toMin" id="toMin" src="${imgPath}/flow/close.png"> 
	        <div class="logoDiv"><img class="logo" id="newMenuLogo" src="/resources/images/flow/def.png"></div>
	        <div class="userName" id="setRealName">用户名</div>
	        <div class="flowTree">
	              <div class="treeitem" id="myPro">
	                  <div class="myPro"></div>
	                  <div class="title">我的项目<span> Beta</span></div>
	                  <img src="${imgPath}/flow/more.png">
	             </div>
	              <ul class="productList" id="productList">
	                  <li id="nowDoing" class="checkLi">进行中<div id="cardNum"></div></li>
	                  <li id="pause">暂停</li>
	                  <li id="finish">完成/取消</li>
	              </ul>
	              <a href="http://www.apaipian.com/order/myOrder">
		              <div class="treeitem" id="myOrder">
		                <div class="orderList"></div>
		                <div class="title">我的订单</div>
		                <%--   <img src="${imgPath}/flow/more.png"> --%>
		             </div>
	              </a>

	              <div class="treeitem">
	              <a class="treeA" href="${url}running?save">

		              <div class="treeitem">
		                  <div class="save"></div>
		                  <div class="title">收藏列表</div>
		             </div>
	             </a>
	              <div class="treeitem hide">

	                  <div class="safe"></div>
	                  <a href='http://www.apaipian.com/activiti/saveInfo'><div class="title">安全设置</div></a>
	             </div>
	              <a href="${url}login/loginout">
	             <div class="treeitem treeitemRed">
	                  <div class="line"></div>
	                  <div class="loginOut"></div>
	                 <div class="title">退出登录</div>
	             </div>
	             </a>
	        </div>
	   </div>
	</div> 
	</div>
  
	<!-- video-->
</body>

</html>
