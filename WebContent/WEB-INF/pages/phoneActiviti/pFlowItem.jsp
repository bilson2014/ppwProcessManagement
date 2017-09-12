<%@ page contentType="text/html;charset=UTF-8"%>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
            <%-- import CSS --%>
                <spring:url value="/resources/css/phoneActiviti/pFlowItem.css" var="pFlowItemCss" />
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

                        <link rel="stylesheet" href="${pFlowItemCss}">

                        <!--[if lt IE 9]>
        <script>window.html5 || document.write('<script src="html5shivJs"><\/script>')</script>
    <![endif]-->
                    </head>

                    <body>
                        <div class="pagePhone">
                            <!--项目团队信息模块  -->
                            <div class="teaminfo">
                                <div class="teamtitle">
                                    <span class="pic"></span>
                                    <span>项目团队信息</span>
                                </div>
                                <div class="teambox">
                                    <ul>
                                        <li>
                                            <div></div>
                                            <span>三元君</span>
                                        </li>
                                        <li>
                                            <div></div>
                                            <span>三元君</span>
                                        </li>
                                        <li>
                                            <div></div>
                                            <span>三元君</span>
                                        </li>
                                        <li>
                                            <div></div>
                                            <span>三元君</span>
                                        </li>
                                        <li>
                                            <div></div>
                                            <span>三元君</span>
                                        </li>
                                        <li>
                                            <div></div>
                                            <span>三元君</span>
                                        </li>
                                        <li>
                                            <div></div>
                                            <span>三元君</span>
                                        </li>
                                        <li>
                                            <div></div>
                                            <span>三元君</span>
                                        </li>
                                        <li>
                                            <div></div>
                                            <span>三元君</span>
                                        </li>
                                        <li>
                                            <div></div>
                                            <span>三元君</span>
                                        </li>

                                    </ul>
                                </div>
                            </div>
                            <div class="line"></div>
                            <!--项目信息 -->
                            <div class="projectinfo">
                                <div class="protitle">
                                    <span class='pic'></span>
                                    <span>项目信息</span>
                                    <a href="#" class="write"></a>
                                </div>
                                <div class="probox">
                                    <ul>
                                        <li>
                                            <div>项目编号</div>
                                            <span>1010010</span>
                                        </li>
                                        <li>
                                            <div>项目来源</div>
                                            <span>线上-网站</span>
                                        </li>
                                        <li>
                                            <div>项目名称</div>
                                            <span>国产三维动画拍摄画拍摄</span>
                                        </li>
                                        <li>
                                            <div>项目评级</div>
                                            <span>S</span>
                                        </li>
                                        <li>
                                            <div>项目周期</div>
                                            <span>45天</span>
                                        </li>
                                        <li>
                                            <div>产品线</div>
                                            <span>企业形象宣传片</span>
                                        </li>
                                        <li>
                                            <div>立项时间</div>
                                            <span>2017-07-09</span>
                                        </li>
                                        <li>
                                            <div>项目配置</div>
                                            <span>尊享版+6分钟（两个拍摄日）+广告演员（1名）+广告摄影棚+广告级航拍4K</span>
                                        </li>
                                        <li>
                                            <div>对标影片</div>
                                            <span>http://www.apaipian.com/play/16_13461.html</span>
                                        </li>
                                        <li>
                                            <div>项目描述</div>
                                            <span>도저히 이렇겐 더 안되겠어내가 어떻게든 좀 손보겠어낡은 스타일밖에 모르는 널프로듀스 얼마나 멋져질지 좀 알겠어</span>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                            <div class="line"></div>
                            <!--客戶信息  -->
                            <div class="customerinfo">
                                <div class="custitle">
                                    <span class='pic'></span>
                                    <span>客户信息</span>
                                    <a href="#" class="write"></a>
                                </div>
                                <div class="cusbox">
                                    <ul>
                                        <li>
                                            <div>客户名称</div>
                                            <span>深圳富士康有限公司</span>
                                        </li>
                                        <li>
                                            <div>客户评级</div>
                                            <span>S</span>
                                        </li>
                                        <li>
                                            <div>客户联系人</div>
                                            <span>되다 효 혼천의</span>
                                        </li>
                                        <li>
                                            <div>客户电话</div>
                                            <span>15100004589</span>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                            <div class="line"></div>
                            <!--供应商信息  -->
                            <div class="supplierinfo">
                                <div class="suptitle">
                                    <span class='pic'></span>
                                    <span>供应商信息</span>
                                    <a href="#" class="write"></a>
                                </div>
                                <div class="supbox">
                                    <!--策划供应商  -->
                                    <div class="plantitle">
                                        <span class='pic'></span>
                                        <span>策划供应商</span>
                                    </div>
                                    <div class="planbox">
                                        <ul>
                                            <li>
                                                <div>供应商名称</div>
                                                <span>혼천의</span>
                                            </li>
                                            <li>
                                                <div>供应商联系人</div>
                                                <span>되다 효</span>
                                            </li>
                                            <li>
                                                <div>供应商联系电话</div>
                                                <span>15100001235</span>
                                            </li>
                                        </ul>
                                    </div>
                                    <!--制作供应商  -->
                                    <div class="maketitle">
                                        <span class='pic'></span>
                                        <span>制作供应商</span>
                                    </div>
                                    <div class="makebox">
                                        <ul>
                                            <li>
                                                <div>供应商名称</div>
                                                <span>혼천의</span>
                                            </li>
                                            <li>
                                                <div>供应商联系人</div>
                                                <span>되다 효</span>
                                            </li>
                                            <li>
                                                <div>供应商联系电话</div>
                                                <span>15100001235</span>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                            <div class="line"></div>
                            <!--价格信息  -->
                            <div class="priceinfo">
                                <div class="prititle">
                                    <span class='pic'></span>
                                    <span>价格信息</span>
                                    <a href="#" class="write"></a>
                                </div>
                                <div class="pribox">
                                    <ul>
                                        <li>
                                            <div>预估价格</div>
                                            <span>1000元</span>
                                        </li>
                                        <li>
                                            <div>客户项目预算</div>
                                            <span>12000元</span>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                            <!--遮罩层  -->
                            <div class="mask">
                                <div class="close"></div>
                                <div class="box">
                                    <span class="pic"></span>
                                    <span class="name">三元君</span>
                                    <div class="smallline"></div>
                                    <span class="text">负责人</span>
                                    <span class="phone">18788234111</span>
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