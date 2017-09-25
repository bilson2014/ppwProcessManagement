<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
    <!-- <%-- import CSS --%> -->
    <spring:url value="/resources/css/phoneActiviti/pFlowItprice.css" var="pFlowItpriceCss" />
    <!-- <%-- import JS --%> -->
    <spring:url value="/resources/lib/jquery/jquery-2.0.3.min.js" var="jqueryJs" />
    <spring:url value="/resources/js/common.js" var="commonJs" />
    <spring:url value="/resources/js/phoneActiviti/commonPhone.js" var="commonPhoneJs" />
    <spring:url value="/resources/js/phoneActiviti/pFlowItprice.js" var="pFlowItpriceJs" />
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

        <link rel="stylesheet" href="${pFlowItpriceCss}">

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
            <form method="post" action="/project/edit/information?client=phone&taskId=${taskId}&projectId=${projectId}&processInstanceId=${processInstanceId}" id="toPriceForm">
                <input type="hidden" id="priceId" name="pf_projectId">
                <!--价格信息修改  -->
                <div class="pricebox">
                    <!--预估价格  -->
                    <div class="porecast">
                        <div>预估价格</div>

                        <input id="est" name="pf_estimatedPrice">
                        <span>元</span>
                        <p></p>
                    </div>
                </div>
                <!-- 取消和确认 -->
                <div class="surebox">
                    <div class="cancel">
                        <div>取消</div>
                    </div>
                    <div class="sure">
                        <div id="sumbitPrice">确认</div>
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
    <script type="text/javascript" src="${pFlowItpriceJs}"></script>

    </html>