<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
    <!-- <%-- import CSS --%> -->
    <spring:url value="/resources/css/phoneActiviti/pFlowItprovider.css" var="pFlowItproviderCss" />
    <!-- <%-- import JS --%> -->
    <spring:url value="/resources/lib/jquery/jquery-2.0.3.min.js" var="jqueryJs" />
    <spring:url value="/resources/js/common.js" var="commonJs" />
    <spring:url value="/resources/js/phoneActiviti/commonPhone.js" var="commonPhoneJs" />
    <spring:url value="/resources/js/phoneActiviti/pFlwltprovider.js" var="pFlwltproviderJs" />
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
        <link rel="stylesheet" href="${pFlowItproviderCss}">

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

        <div class="pagePhone">
            <!--供应商信息修改  -->
            <form method="post" action="/project/edit/teamInformation?client=phone&taskId=${taskId}&projectId=${projectId}&processInstanceId=${processInstanceId}" id="toProForm">
                <input type="hidden" id="proId" name="projectId" value="${flow_info['projectId']}">
                
               
 
                <div class="providerbox">
                    <!--策划供应商  -->
                    <div class="plan" id="isHideTop">
                        <div class="title">
                            <img class='pic' src="/resources/images/pFlow/plan.png" />
                            <span>策划供应商</span>
                            <input type="hidden" id="scId" name="pt_projectTeamId">
                            <input type="hidden" id="scTeamId" name="pt_teamId" value="">
                        </div>
                        <div class="planbox" >
                            <!--供应商联系人  -->
                             <div class="name" id="scTeam">
                                <div>供应商团队</div>
                                <input class="checkError" id="scName" name="pt_teamName">
                                <div class="error">内容不能为空</div>
                                <ul class="utoInfoMake"></ul>
                            </div>
                            <div class="name" id="pName">
                                <div>供应商联系人</div>
                                <input class="checkError" id="scLink" name="pt_linkman">
                                <div class="error">内容不能为空</div>
                            </div>
                            <!-- 供应商电话 -->
                            <div class="name" id="pPhone">
                                <div>供应商联系电话</div>
                                <input class="checkErrorP" id="scTel" name="pt_telephone">
                                <div class="error">内容不能为空</div>
                                
                            </div>
                        </div>
                    </div>
                    <!--制作供应商  -->
                    <div class="plan">
                        <div class="title">
                            <img class='pic' src="/resources/images/pFlow/plan.png" />
                            <span>制作供应商</span>
                            <input type="hidden" id="prId" name="pt_projectTeamId">
                             <input type="hidden" id="prTeamId" name="pt_teamId" value="">
                        </div>
                        <div class="planbox" id="isHideBot">
                            <!--供应商联系人  -->
                             <div class="name" id="prTeam">
                                <div>供应商团队</div>
                                <input class="checkError" id="prName" name="pt_teamName">
                                <div class="error">内容不能为空</div>
                                <ul class="utoInfoMakeTeam"></ul>
                            </div>
                            <div class="name" id="mName">
                                <div>供应商联系人</div>
                                <input class="checkError" id="prLink" name="pt_linkman">
                                <div class="error">内容不能为空</div>
                            </div>
                            <!-- 供应商电话 -->
                            <div class="name" id="mPhone">
                                <div>供应商联系电话</div>
                                <input class="checkErrorP" id="prTel" name="pt_telephone">
                                <div class="error">内容不能为空</div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- 取消和确认 -->
                <div class="surebox">
                    <a href="/project/phone/flowinfo/${taskId}/${projectId}/${processInstanceId}">
                        <div class="cancel">
                            <div>取消</div>
                        </div>
                    </a>
                    <div class="sure" id="submitProvide">
                        <div>确认</div>
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
    <script type="text/javascript" src="${pFlwltproviderJs}"></script>

    </html>