<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- import CSS --%>
<spring:url value="/resources/css/activiti/textFlow.css" var="textCss"/>

<%-- import JS --%>
<spring:url value="/resources/lib/jquery/jquery-2.0.3.min.js" var="jqueryJs"/>
<spring:url value="/resources/js/common.js" var="commonJs"/>
<spring:url value="/resources/js/activiti/textFlow.js" var="textFlowJs"/>
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
<meta name="baidu-site-verification" content="dMz6jZpIwd" />
<title></title>
<link rel="stylesheet" href="${textCss}">
<script type="text/javascript" src="resources/lib/Clamp/clamp.js"></script>
<script type="text/javascript" src="${jqueryJs}"></script>
<script type="text/javascript" src="${commonJs}"></script>
<script type="text/javascript" src="${textFlowJs}"></script>

<!--[if lt IE 9]>
        <script>window.html5 || document.write('<script src="html5shivJs"><\/script>')</script>
    <![endif]-->


</head>

<body>

     <a href="/project/start/project"><div id="toCreate"></div></a>
     
	<input type="hidden" id="storage_node" value="${file_locate_storage_path }" />
	<input type="hidden" id="realName" value="${realName}" />
	<input type="hidden" id="photo" value="${photo}" />
	<input type="hidden" id="photo" value="${status}" />
	
	
	<div class="page">
	    <jsp:include flush="true" page="../header.jsp"></jsp:include>
	    <jsp:include flush="true" page="flowMenu.jsp"></jsp:include>  
	    
	     	<c:if test="${!empty gTasks}">
							<c:forEach items="${gTasks }" var="staff" varStatus="status">
                                     <div class="cardNum hide"></div>
							</c:forEach>
			</c:if> 
	
	<div class="productListArea">
	           <div class="waitWork">
	               
	                <iframe class="frame" id="content-frame" class="iframe" src="<spring:url value='/project/running-doing'/>"></iframe>
<!-- 	                <div class="lineTop"></div>
	                <div id="hideDiv">
	                <div class="title">
	                     <div class="titleName" id="upName">待办任务</div>
	                </div>
	                <div class="setCard" id="setCard">
	                        <div class="waitCard">
	                             <div class="cardH">
	                                 <div class="title">这里是卡片的标题啊啊啊啊</div>
	                                 <div class="point">
	                                      <div class="showPoint">SA</div>
	                                      <div class="showDeil">
	                                            <div class="proPoint">项目评级<span>S</span></div>
	                                            <div class="cusPoint">客户评级<span>A</span></div>
	                                      </div>
	                                 </div>
	                                 <div class="your">负责项目</div>
	                             </div>
	                             <div class="cardContent">
	                                  <img src="/resources/images/flow/demoG.png">
	                                  <div class="setContent">
	                                      <div class="listName">上传周期表</div>
	                                      <div class="lastTime">已超时 24h 5min 45s</div>
	                                  </div>
	                             </div>
	                        </div>
	                        <div class="waitCard">
	                             <div class="cardH">
	                                 <div class="title">这里是卡片的标题啊啊啊啊</div>
	                                 <div class="point">
	                                      <div class="showPoint">SA</div>
	                                      <div class="showDeil">
	                                            <div class="proPoint">项目评级<span>S</span></div>
	                                            <div class="cusPoint">客户评级<span>A</span></div>
	                                      </div>
	                                 </div>
	                                 <div class="user">负责人<span>她她她</span></div>
	                             </div>
	                             <div class="cardContent">
	                                  <img src="/resources/images/flow/demoY.png">
	                                  <div class="setContent">
	                                      <div class="listName">上传周期表</div>
	                                      <div class="lastTime">已超时 24h 5min 45s</div>
	                                  </div>
	                             </div>
	                        </div>
	                       <div class="waitCard">
	                             <div class="cardH">
	                                 <div class="title">这里是卡片的标题啊啊啊啊</div>
	                                 <div class="point">
	                                      <div class="showPoint">SA</div>
	                                      <div class="showDeil">
	                                            <div class="proPoint">项目评级<span>S</span></div>
	                                            <div class="cusPoint">客户评级<span>A</span></div>
	                                      </div>
	                                 </div>
	                                 <div class="user">负责人<span>她她她</span></div>
	                             </div>
	                             <div class="cardContent">
	                                  <img src="/resources/images/flow/demoR.png">
	                                  <div class="setContent">
	                                      <div class="listName">上传周期表</div>
	                                      <div class="lastTime">已超时 24h 5min 45s</div>
	                                  </div>
	                             </div>
	                        </div>
	                        <div class="waitCard"></div>
	                        <div class="waitCard"></div>
	                        <div class="waitCard"></div>
	                </div>
	           </div>
	           <div class="cardLine"><div></div></div>
	           </div>
	           <div class="otherWork">
	                <div class="title">
	                     <div class="titleName" id="downName">其它任务</div>
	                </div>
	           <div class="setCard" id="otherCard">
	                        <div class="otherCard">
	                             <div class="cardH">
	                                 <div class="title">这里是卡片的标题啊啊啊啊</div>
	                                 <div class="point">
	                                      <div class="showPoint">SA</div>
	                                      <div class="showDeil">
	                                            <div class="proPoint">项目评级<span>S</span></div>
	                                            <div class="cusPoint">客户评级<span>A</span></div>
	                                      </div>
	                                 </div>
	                                 <div class="user">负责人<span>她她她</span></div>
	                             </div>
	                             <div class="cardContent">
	                                  <div class="setContent">
	                                      <div class="lastTime">您于 24h 5min 45s完成了这个那个还有这个这个那个还有这个</div>
	                                  </div>
	                                  <img src="/resources/images/flow/newFinish.png">
	                             </div>
	                        </div>
	                        <div class="otherCard"></div>
	           </div>   
	            </div>      -->    
	           
	</div> 
	
</div>	  

	<!-- video-->
</body>

</html>
