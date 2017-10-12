<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="r" uri="/mytaglib" %>
    <!-- <%-- import CSS --%> -->
    <spring:url value="/resources/css/phoneActiviti/pFlowItem.css" var="pFlowItemCss" />
    <!-- <%-- import JS --%>  -->
    <spring:url value="/resources/lib/jquery/jquery-2.0.3.min.js" var="jqueryJs" />
    <spring:url value="/resources/js/common.js" var="commonJs" />
    <spring:url value="/resources/js/phoneActiviti/commonPhone.js" var="commonPhoneJs" />
    <spring:url value="/resources/js/phoneActiviti/pFlowItem.js" var="pFlowItemJs" />
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

        <input type="hidden" id="url" value="/${taskId}/${projectId}/${processInstanceId}">
        <div class="pagePhone">
            <!--项目团队信息模块  -->
            <div class="teaminfo">
                <div class="teamtitle">
                    <span class="pic"></span>
                    <span>项目团队信息</span>
                </div>
                <div class="teambox">
                    <ul>
                        <c:forEach var="item" items="${synergyList}">
                            <li>
                                <c:if test="${!empty item.imgUrl}">
                                    <img src="${file_locate_storage_path }${item.imgUrl}">
                                </c:if>
                                <c:if test="${empty item.imgUrl}">
                                    <img src="/resources/images/flow/def.png">
                                </c:if>
                                <div class="reTalkItem">${item.employeeName}</div>
                                <div class="study hide">${item.employeeGroup}</div>
                                <span class="telephone hide">${item.telephone}</span>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
            <div class="line"></div>
            <!--项目信息 -->
            <div class="projectinfo">
                <div class="protitle">
                    <span class='pic'></span>
                    <span>项目信息</span>
                    <r:group role="sale" role2="saleDirector"> 
                    <a href="/project/phone/editInfo/${taskId}/${projectId}/${processInstanceId}" class="write"></a>
                    </r:group>
                </div>
                <div class="probox">
                    <ul>
                     	<c:if test="${!empty flow_info['projectId']}">
                        <li> 
                            <div>项目编号</div>
                            <span>${flow_info["projectId"]}</span>
                        </li>
                        </c:if>
                        <c:if test="${!empty flow_info['projectSource']}">
                        <li>
                            <div>项目来源</div>
                            <span>${flow_info["projectSource"]}</span>
                        </li>
                        </c:if>
                        <c:if test="${!empty flow_info['projectName']}">
                        <li>
                            <div>项目名称</div>
                            <span class='best'>${flow_info["projectName"]}</span>
                        </li>
                        </c:if>
                        <c:if test="${!empty flow_info['projectGrade']}">
                        <li>
                            <div>项目评级</div>
                            <span>${flow_info["projectGrade"]}</span>

                        </li>
                        </c:if>
                       
                        <li>
                            <div>项目周期</div>
                            <c:if test="${flow_info['projectCycle'] == 0}">
                                <span>待定</span>
                            </c:if>
                            <c:if test="${flow_info['projectCycle'] > 0}">
                                <span>${flow_info["projectCycle"]}天</span>
                            </c:if>
                        </li>
                       
                        <c:if test="${!empty flow_info['productName']}">
                        <li>
                            <div>产品线</div>
                            <span>${flow_info["productName"]}</span>
                        </li>
                        </c:if>
                        <c:if test="${!empty flow_info['createDate']}">
                        <li>
                            <div>立项时间</div>
                            <span id="projectTime">${flow_info["createDate"]}</span>
                        </li>
                        </c:if>
                        <c:if test="${!empty flow_info['productConfigLengthName']}">
                        <li>
                            <div>项目配置</div>
                            <span>${flow_info["productConfigLevelName"]}
                                                 <c:if test="${!empty flow_info['productConfigLengthName']}"> 
		                                              +  ${flow_info['productConfigLengthName']}
		                                          </c:if>
		                                          <c:if test="${not empty flow_info['productConfigAdditionalPackageName']}"> 
		                                              +  ${flow_info['productConfigAdditionalPackageName']}
		                                          </c:if>
                                            </span>
                        </li>
                        </c:if>
                        <c:if test="${!empty flow_info['filmDestPath']}">
                        <li>
                            <div>对标影片</div>
                            <a class="film" target='_blank' href="${flow_info['filmDestPath']}">${flow_info['filmDestPath']}</a>
                        </li>
                        </c:if>
                        <c:if test="${!empty flow_info['projectDescription']}">
                        <li>
                            <div>项目描述</div>
                            <span class="miaoshu">${flow_info["projectDescription"]}</span>
                        </li>
                        </c:if>
                        <c:if test="${!empty flow_info['sampleUrl']}">
                        <li>
                            <div>水印样片地址</div>
                            <span class="miaoshu"><a href='${flow_info["sampleUrl"]}' target="_self">${flow_info["sampleUrl"]}</a></span>
                        </li>
                        </c:if>
                        <c:if test="${!empty flow_info['samplePassword']}">
                        <li>
                            <div>水印样片密码</div>
                            <span class="miaoshu">${flow_info["samplePassword"]}</span>
                        </li>
                        </c:if>
                    </ul>
                </div>
            </div>
            <div class="line"></div>
            <!--客戶信息  -->
            <c:if test="${!empty user_info}"> 
            <div class="customerinfo">
                <div class="custitle">
                    <span class='pic'></span>
                    <span>客户信息</span>
                    <r:group role="sale" role2="saleDirector">  
                      <a href="/project/phone/editUser/${taskId}/${projectId}/${processInstanceId}" class="write"></a>
                    </r:group>
                </div>
                <div class="cusbox">
                    <ul>
                    
                        <c:if test="${!empty user_info['userName']}">
                        <li>
                            <div>客户名称</div>
                            <span>${user_info["userName"]}</span>
                        </li>
                        </c:if>
                        <c:if test="${!empty user_info['userLevel']}">
                        <li>
                            <div>客户评级</div>
                            <span>${user_info["userLevel"]}</span>                            
                        </li>
                        </c:if>
                        <c:if test="${!empty user_info['linkman']}">
                        <li>
                            <div>客户联系人</div>
                            <span>${user_info["linkman"]}</span>
                        </li>
                        </c:if>
                        <c:if test="${!empty user_info['telephone']}">
                        <li>
                            <div>客户电话</div>
                            <span>${user_info["telephone"]}</span>
                        </li>
                        </c:if>    
                        
                          <c:if test="${not empty user_info['email']}">
					            <li>
		                            <div>邮箱地址</div>
		                            <span>${user_info["email"]}</span>
		                        </li>
			              </c:if>                    
                    </ul>
                </div>
            </div>
            <div class="line"></div>
            </c:if>
            <!--供应商信息  -->
            <c:if test="${not empty teamProduct_info || not empty teamPlan_info}">
            <div class="supplierinfo">
                <div class="suptitle">
                    <span class='pic'></span>
                    <span>供应商信息</span>
                     <r:group role="teamProvider" role2="teamDirector">
                    	<a href="/project/phone/editTeam/${taskId}/${projectId}/${processInstanceId}" class="write"></a>
                     </r:group>
                </div>
                <div class="supbox">
                    <!--策划供应商  -->
                 <c:if test="${!empty teamPlan_info}">
                    <div class="plantitle">
                        <span class='pic'></span>
                        <span>策划供应商</span>
                    </div>
                    <div class="planbox">
	                    <c:forEach items="${teamPlan_info }" var="plan">
	                        <ul>                        
	                        	<c:if test="${!empty plan['teamName']}">
	                            <li>
	                                <div>供应商名称</div>                               
	                                <span>${plan["teamName"]}</span>
	                            </li>
	                            </c:if>
	                            <c:if test="${!empty plan['linkman']}">
	                            <li>
	                                <div>供应商联系人</div>                              
	                                <span>${plan["linkman"]}</span>
	                            </li>
	                            </c:if>
	                            <c:if test="${!empty plan['telephone']}">
	                            <li>
	                                <div>供应商联系电话</div>
	                                <span>${plan["telephone"]}</span>
	                            </li>
	                            </c:if>
	                        </ul>
	                     </c:forEach>   
                    </div>
                  </c:if>  
                    <!--制作供应商  -->
                     <c:if test="${!empty teamProduct_info}">
                    <div class="maketitle">
                        <span class='pic'></span>
                        <span>制作供应商</span>
                    </div>
                    <div class="makebox">
	                     <c:forEach items="${teamProduct_info }" var="product">
	                        <ul>                        
	                            <c:if test="${!empty product['teamName']}">
	                            <li>
	                                <div>供应商名称</div>
	                                <span>${product["teamName"]}</span>
	                            </li>
	                            </c:if>
	                            <c:if test="${!empty product['linkman']}">
	                            <li>
	                                <div>供应商联系人</div>
	                                <span>${product["linkman"]}</span>
	                            </li>
	                            </c:if>
	                            <c:if test="${!empty product['telephone']}">
	                            <li>
	                                <div>供应商联系电话</div>
	                                <span>${product["telephone"]}</span>
	                            </li>
	                            </c:if>                           
	                        </ul>
	                      </c:forEach>  
                    </div>
                    </c:if>
                </div>
            </div>
            <div class="line"></div>
            </c:if> 
            <!--价格信息  -->
            <c:if test="${!empty price_info}">
                <div class="priceinfo">
                    <div class="prititle">
                        <span class='pic'></span>
                        <span>价格信息</span>
                        <r:group role="sale" role2="saleDirector">  
                        <a href="/project/phone/editPrice/${taskId}/${projectId}/${processInstanceId}" class="write"></a>
                        </r:group>
                    </div>
                    <div class="pribox">
                        <ul>   
                          <c:if test="${not empty price_info['estimatedPrice']}">                         
	                            <li>
	                                    <div>预估价格</div>
	                                    <c:if test="${price_info['estimatedPrice'] == 0}">
	                                        <span>待定</span>
	                                    </c:if>
	                                    <c:if test="${price_info['estimatedPrice'] > 0}">
	                                        <span>${price_info["estimatedPrice"]}元</span>
	                                    </c:if>
	                               
	                            </li>   
                           </c:if>     
                               <c:if test="${not empty price_info['projectBudget']}">                                      
		                            <li>
		                               
		                                    <div>客户项目预算</div>
		                                    <c:if test="${price_info['projectBudget'] == 0}">
		                                        <span>待定</span>
		                                    </c:if>
		                                    <c:if test="${price_info['projectBudget'] > 0}">
		                                        <span>${price_info['projectBudget']}元</span>
		                                    </c:if>
		                            </li>
                              </c:if>
                        </ul>
                    </div>
                </div>
            </c:if>
            <!--遮罩层  -->
            <div class="mask">
                <div class="close"></div>
                <div class="box">
                    <img src="" class="pic">
                    <span class="name"></span>
                    <div class="smallline"></div>
                    <span class="text"></span>
                    <span class="phone"><a href="tel:18235170627"></a></span>
                </div>
            </div>
        </div>
        <jsp:include flush="true" page="pHead.jsp"></jsp:include>
    </body>
    <script type="text/javascript" src="${jqueryJs}"></script>
    <script type="text/javascript" src="${jsonJs}"></script>
    <script type="text/javascript" src="${commonJs}"></script>
    <script type="text/javascript" src="${commonPhoneJs}"></script>
    <script type="text/javascript" src="${pFlowItemJs}"></script>

    </html>