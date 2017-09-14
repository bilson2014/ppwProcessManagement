<%@ page contentType="text/html;charset=UTF-8"%>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
            <%-- import CSS --%>
                <spring:url value="/resources/css/phoneActiviti/pFlowItprovider.css" var="pFlowItproviderCss" />
                <%-- import JS --%>
                    <spring:url value="/resources/lib/jquery/jquery-2.0.3.min.js" var="jqueryJs" />
                    <spring:url value="/resources/js/common.js" var="commonJs" />
                    <spring:url value="/resources/js/phoneActiviti/commonPhone.js" var="commonPhoneJs" />
                    <spring:url value="/resources/js/phoneActiviti/pFlwFile.js" var="pFlwFileJs" />
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
                        <div class="pagePhone">
                            <!--供应商信息修改  -->
                            <div class="providerbox">
                                <!--策划供应商  -->
                                <div class="plan">
                                    <div class="title">
                                        <span class='pic'></span>
                                        <span>策划供应商</span>
                                    </div>
                                    <div class="planbox">
                                        <!--供应商联系人  -->
                                        <div class="name">
                                            <div>供应商联系人</div>
                                            <input type="" name="" value="小橙子">
                                        </div>
                                        <!-- 供应商电话 -->
                                        <div class="phone">
                                            <div>供应商联系电话</div>
                                            <input type="" name="" value="18235478569">
                                        </div>
                                    </div>
                                </div>
                                <!--制作供应商  -->
                                <div class="make">
                                    <div class="title">
                                        <span class='pic'></span>
                                        <span>制作供应商</span>
                                    </div>
                                    <div class="planbox">
                                        <!--供应商联系人  -->
                                        <div class="name">
                                            <div>供应商联系人</div>
                                            <input type="" name="" value="小小橙">
                                        </div>
                                        <!-- 供应商电话 -->
                                        <div class="phone">
                                            <div>供应商联系电话</div>
                                            <input type="" name="" value="18214569654">
                                        </div>
                                    </div>
                                </div>
                            </div>


                            <!-- 取消和确认 -->
                            <div class="surebox">
                                <div class="cancel">
                                    <div>取消</div>
                                </div>
                                <div class="sure">
                                    <div>确认</div>
                                </div>
                            </div>

















                        </div>

                        <jsp:include flush="true" page="pHead.jsp"></jsp:include>
                    </body>

                    <script type="text/javascript" src="${jqueryJs}"></script>
                    <script type="text/javascript" src="${jsonJs}"></script>
                    <script type="text/javascript" src="${commonJs}"></script>
                    <script type="text/javascript" src="${commonPhoneJs}"></script>
                    <script type="text/javascript" src="${pFlwFileJs}"></script>

                    </html>