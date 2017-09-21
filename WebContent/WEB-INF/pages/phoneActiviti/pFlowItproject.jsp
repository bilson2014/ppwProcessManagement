<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
    <!-- <%-- import CSS --%> -->
    <spring:url value="/resources/css/phoneActiviti/pFlowItproject.css" var="pFlowItprojectCss" />
    <spring:url value="/resources/lib/merge/iosSelect.css" var="iosSelectCss" />
    <!-- <%-- import JS --%> -->
    <spring:url value="/resources/lib/jquery/jquery-2.0.3.min.js" var="jqueryJs" />
    <spring:url value="/resources/js/common.js" var="commonJs" />
    <spring:url value="/resources/js/phoneActiviti/commonPhone.js" var="commonPhoneJs" />
    <spring:url value="/resources/js/phoneActiviti/pFlowItproject.js" var="pFlowItprojectJs" />
    <spring:url value="/resources/images" var="imgPath" />
    <spring:url value="/resources/lib/merge/iosSelect.js" var="iosSelectJs" />

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

        <link rel="stylesheet" href="${pFlowItprojectCss}">
        <link rel="stylesheet" href="${iosSelectCss}">

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
            <!--项目信息修改  -->
            <form method="post" action="/edit/information?client=phone&taskId=${taskId}&projectId=${projectId}&processInstanceId=${processInstanceId}" id="toProjectForm">
                <input type="hidden" id="proId" name="pf_projectId" value="${flow_info['projectId']}">
                <div class="projectbox">
                    <!--项目名称 -->
                    <div class="name">
                        <div>项目名称</div>
                        <input id="proName" name="pf_projectName" value="">
                        <p></p>
                    </div>
                    <!--项目来源  -->
                    <div class="origin">
                        <div>项目来源</div>
                        <input type="hidden" id="pf_ResourInput" name="pf_projectSource" value="">
                        <div class="orderSelect ">
                            <div class="setSelect" id="source">
                                <div id="setinput" class="pf_Resour"></div>
                                <img src="${imgPath}/pFlow/select.png">
                            </div>
                        </div>
                    </div>
                    <!--项目评级  -->
                    <div class="rate">
                        <div class="name">项目评级</div>
                        <input type="hidden" id="pf_projectGradeInput" name="pf_projectGrade" value="">
                        <div class="orderSelect" id="grade">
                            <div class="setSelect">
                                <div id="setinputs" class="pf_Resour"></div>
                                <img src="${imgPath}/pFlow/select.png">
                            </div>
                        </div>
                    </div>
                    <!-- 项目周期   -->
                    <div class="period">
                        <div>项目周期</div>
                        <input id="proCycle" name="pf_projectCycle">
                        <span>天</span>
                        <p></p>
                    </div>
                    <!--对标影片  -->
                    <div class="film">
                        <div>对标影片</div>
                        <textarea id="proFdp" name="pf_filmDestPath" name="" id="" cols="30" rows="2"></textarea>
                        <p></p>
                    </div>
                    <!--项目描述  -->
                    <div class="describe">
                        <div>项目描述</div>
                        <textarea id="projectDes" name="pf_projectDescription"></textarea>
                        <p></p>
                    </div>
                    <!-- 取消和确认 -->
                    <div class="surebox">
                        <a href="/project/phone/flowinfo/${taskId}/${projectId}/${processInstanceId}">
                            <div class="cancel">
                                <div>取消</div>
                            </div>
                        </a>
                        <div class="sure">
                            <div id='surebtn'>确认</div>
                        </div>
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
    <script type="text/javascript" src="${pFlowItprojectJs}"></script>
    <script type="text/javascript" src="${iosSelectJs}"></script>

    </html>