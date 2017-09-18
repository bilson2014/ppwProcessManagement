<%@ page contentType="text/html;charset=UTF-8"%>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
            <%-- import CSS --%>
                <spring:url value="/resources/css/phoneActiviti/pFlowItproject.css" var="pFlowItprojectCss" />
                <%-- import JS --%>
                    <spring:url value="/resources/lib/jquery/jquery-2.0.3.min.js" var="jqueryJs" />
                    <spring:url value="/resources/js/common.js" var="commonJs" />
                    <spring:url value="/resources/js/phoneActiviti/commonPhone.js" var="commonPhoneJs" />
                    <spring:url value="/resources/js/phoneActiviti/pFlowItproject.js" var="pFlowItprojectJs" />
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

                        <link rel="stylesheet" href="${pFlowItprojectCss}">

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
                            <form method="post" action="/project/phone/flowinfo/${taskId}/${projectId}/${processInstanceId}" id="toProjectForm">
                                <input type="hidden" id="proId" name="pf_projectId" value="${flow_info['projectId']}">
                                <div class="projectbox">
                                    <!--项目名称 -->
                                    <div class="name">
                                        <div>项目名称</div>
                                        <input id="proName" name="pf_projectName" value="">
                                        <p>fffff</p>
                                    </div>
                                    <!--项目来源  -->
                                    <div class="origin">
                                        <div>项目来源</div>
                                        <!-- <input type="hidden" id="pf_ResourInput" name="pf_projectSource" value="">
                                        <ul class="oSelect" id="pResour">
                                            <li></li>
                                        </ul> -->
                                        <select class="choice">
                                        <option value="线上-电销">线上-电销</option>
                                        <option value="线下-直销">线下-直销</option>
                                        <option value="线下-活动">线下-活动</option>
                                        <option value="线下-渠道">线下-渠道</option>
                                        <option value="复购">复购/option>
                                        <option value="线上-网购">线上-网购</option>
                                        <option value="线上-活动">线上-活动</option>
                                        <option value="线上-新媒体">线上-新媒体</option>
                                        <option value="线上-400">线上-400</option>
                                        <option value="线上-商桥">线上-商桥</option>
                                        <option value="线上-PC-首页banner">线上-PC-首页banner</option>
                                        <option value="线上-PC-直接下单">线上-PC-直接下单</option>
                                        <option value="线上-PC-成本计算器">线上-PC-成本计算器</option>
                                        <option value="线上-PC-供应商首页">线上-PC-供应商首页</option>
                                        <option value="线上-PC-作品">线上-PC-作品</option>
                                        <option value="线上-移动-首页banner">线上-移动-首页banner</option>
                                        <option value="线上-移动-成本计算器">线上-移动-成本计算器</option>
                                        <option value="线上-移动-作品">线上-移动-作品</option>
                                        <option value="线上-公众号-成本计算器">线上-公众号-成本计算器</option>
                                        <option value="线上-公众号-直接下单">线上-公众号-直接下单</option>
                                        <option value="线上-公众号-作品">线上-公众号-作品</option>
                                    </select>
                                    </div>
                                    <!--项目评级  -->
                                    <div class="rate">
                                        <div class="name">项目评级</div>
                                        <div class="orderSelect">
                                            <div id="pf_projectGrade">lalalalala</div>
                                            <img src="${imgPath}/pFlow/select.png">
                                            <ul class="oSelect" id="orderCome">
                                                <li data-id="5">S</li>
                                                <li data-id="4">A</li>
                                                <li data-id="3">B</li>
                                                <li data-id="2">C</li>
                                                <li data-id="1">D</li>
                                                <li data-id="0">E</li>
                                            </ul>

                                        </div>
                                        <!-- <select class="choice">
                                        <option value="S">S</option>
                                        <option value="A">A</option>
                                        <option value="B">B</option>
                                        <option value="C">C</option>
                                        <option value="D">D</option>
                                        <option value="E">E</option>
                                    </select> -->
                                    </div>
                                    <!-- 项目周期   -->
                                    <!-- <div class="period">
                                        <div>项目周期</div>
                                        <input id="proCycle" name="pf_projectCycle">
                                        <span>天</span>
                                        <p>sssss</p>
                                    </div> -->
                                    <!--对标影片  -->
                                    <!-- <div class="film">
                                        <div>对标影片</div>
                                        <textarea id="proFdp" name="pf_filmDestPath" name="" id="" cols="30" rows="2"></textarea>
                                        <p>ssssssss</p>
                                    </div> -->
                                    <!--项目描述  -->
                                    <!-- <div class="describe">
                                        <div>项目描述</div>
                                        <textarea id="projectDes" name="pf_projectDescription"></textarea>
                                        <p>sssssdddd</p>
                                    </div> -->
                                    <!-- 取消和确认 -->
                                    <div class="surebox">
                                        <a href="/project/phone/flowInfo/${taskId}/${projectId}/${processInstanceId}">
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

                    </html>