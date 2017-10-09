<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
    <!-- <%-- import CSS --%> -->
    <spring:url value="/resources/css/phoneActiviti/pFlowItclient.css" var="pFlowItclientCss" />
    <!-- <%-- import JS --%> -->
    <spring:url value="/resources/lib/jquery/jquery-2.0.3.min.js" var="jqueryJs" />
    <spring:url value="/resources/js/common.js" var="commonJs" />
    <spring:url value="/resources/js/phoneActiviti/commonPhone.js" var="commonPhoneJs" />
    <spring:url value="/resources/js/phoneActiviti/pFlowItclient.js" var="pFlowItclientJs" />
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

        <link rel="stylesheet" href="${pFlowItclientCss}">

        <!--[if lt IE 9]>
        <script>window.html5 || document.write('<script src="html5shivJs"><\/script>')</script>
    <![endif]-->
    </head>

    <body>
        <input type="hidden" id="projectId" value="${projectId}">
        <input type="hidden" value="${taskId }" id="currentTaskId">
        <input type="hidden" id="processInstanceId" value="${processInstanceId}">
        <input type="hidden" id="projectName" value="${projectName}">
        <input type="hidden" id="url" value="/${taskId}/${projectId}/${processInstanceId}">

        <div class="pagePhone">
            <form method="post" action="/project/edit/information?client=phone&taskId=${taskId}&projectId=${projectId}&processInstanceId=${processInstanceId}" id="toCusForm">
                <input type="hidden" id="proId" name="projectId" value="${flow_info['projectId']}">
                <!--客户信息修改  -->
                <div class="clientbox">
                    <!--客户联系人  -->
                    <input type="hidden" id="cusId" name="pu_projectUserId">
                    <div class="name">
                        <div>客户联系人</div>
                        <input id="cusLinkman" name="pu_linkman" value=''>
                        <p></p>
                    </div>
                    <!--客户电话  -->
                    <div class="phone">
                        <div>客户电话</div>
                        <input id="cusTelephone" name="pu_telephone" value=''>
                        <p></p>
                    </div>
                    <!--邮箱地址  -->
                    <div class="email">
                        <div>邮箱地址</div>
                        <input id="cusEmail" name="pu_email" value=''>
                        
                    </div>
                    <p id="emailError"></p>
                </div>
                <!-- 取消和确认 -->
                <div class="surebox">
                    <div class="cancel">
                        <div>取消</div>
                    </div>
                    <div class="sure" id="surebtn">
                        <div id="">确认</div>
                    </div>
                </div>
            </form>
        </div>
        <jsp:include flush="true" page="pHead.jsp"></jsp:include>
    </body>
    <script type="text/javascript" src="${jqueryJs}"></script>
    <script type="text/javascript" src="${jsonJs}"></script>
    <script type="text/javascript" src="${commonJs}"></script>
    <script type="text/javascript" src="${commonPhoneJs}"></script>
    <script type="text/javascript" src="${pFlowItclientJs}"></script>

    </html>