<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%-- import CSS --%>
<spring:url value="/resources/css/activiti/createFlow.css" var="textCss"/>
<spring:url value="/resources/lib/AirDatepicker/dist/css/datepicker.min.css" var="datepickerCss" />
<%-- import JS --%>
<spring:url value="/resources/lib/jquery/jquery-2.0.3.min.js" var="jqueryJs"/>
<spring:url value="/resources/js/common.js" var="commonJs"/>
<spring:url value="/resources/lib/jquery.json/jquery.json-2.4.min.js" var="jsonJs" />
<spring:url value="/resources/js/activiti/createFlow.js" var="createFlowJs"/>
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
<link rel="stylesheet" href="${textCss}">
<link rel="stylesheet" href="${datepickerCss}">
<script type="text/javascript" src="resources/lib/Clamp/clamp.js"></script>
<script type="text/javascript" src="${jqueryJs}"></script>
<script type="text/javascript" src="${jsonJs}"></script>
<script type="text/javascript" src="${commonJs}"></script>
<script type="text/javascript" src="${createFlowJs}"></script>

<!--[if lt IE 9]>
        <script>window.html5 || document.write('<script src="html5shivJs"><\/script>')</script>
    <![endif]-->

</head>

<body>
    <form method="post" action="/project/start-process" id="toListForm">
	<div class="page">
	    <jsp:include flush="true" page="../header.jsp"></jsp:include>
	    <div class="title">新建项目</div> 
	    <div class="infoTitle">项目信息</div> 
	    <div class="outSide">
	           <div class="projectInfo">
	                 <div class="smallItem errorItem" id="projectNoError">
	                       <div class="itemTitle">项目编号<span>*</span></div>
	                       <input readonly class="noborder" id="projectNo" name="pf_projectId" value="${pf_projectId }"/>
	                 </div>
	                 <div class="midItem errorItem" id="projectNameError">
	                       <div class="itemTitle">项目名称<span>*</span></div>
	                       <input  id="projectName" name="pf_projectName"/>
	                 </div>
	                  <div class="smallItem errorItem" id="projectGradeError">
	                       <div class="itemTitle">项目评级<span>*</span></div>
	                       <input type="hidden" id="pf_projectGrade" name="pf_projectGrade"/>
	                       <div class="orderSelect" >
				                <div id="projectGrade"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="orderCome">
				                   <li data-id="5">S</li>
				                   <li data-id="4">A</li>
				                   <li data-id="3">B</li>
				                   <li data-id="2">C</li>
				                   <li data-id="1">D</li>
				                   <li data-id="0">E</li>
				                </ul>    
					      </div>
	                 </div>
	                  <div class="sMidItem noMargin errorItem" id="projectSourceError">
	                       <div class="itemTitle">项目来源<span>*</span></div>
	                       <input type="hidden" id="pf_projectSource" name="pf_projectSource"/>
	                       <div class="orderSelect" >
				                <div id="projectSource"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="pResour">
				                     <li data-id="2">222</li>
				                     <li data-id="3">333</li>
				                     <li data-id="4">444</li>
				                </ul>    
					      </div>
	                 </div>
	                 
	                  <div class="smallItem errorItem" id="productIdError">
	                       <div class="itemTitle" >产品线<span>*</span></div>
	                       <input type="hidden" id="pf_productId" name="pf_productId"/>
	                       <input type="hidden" id="pf_productName" name="pf_productName"/>
	                       <div class="orderSelect" >
				                <div id="productId"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="setProduct">
				                </ul>    
					      </div>
	                 </div>
	                 <div class="midItem errorItem" id="productConfigLevelIdError">
	                       <div class="itemTitle">等级<span>*</span></div>
	                       <input type="hidden" id="pf_productConfigLevelId" name="pf_productConfigLevelId"/>
	                       <input type="hidden" id="pf_productConfigLevelName" name="pf_productConfigLevelName"/>                      
	                       <div class="orderSelect noclick" >
				                <div id="productConfigLevelId"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="setpProductConfigLength">
				                </ul>
					      </div>
	                 </div>
	                  <div class="smallItem errorItem " id="productConfigLengthError">
	                       <div class="itemTitle">时长<span>*</span></div>
	                       <input type="hidden" id="pf_productConfigLength" name="pf_productConfigLength"/>
	                       <input type="hidden" id="pf_productConfigLengthName" name="pf_productConfigLengthName"/>
	                       <div class="orderSelect noclick" >
				                <div id="productConfigLength"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="setTime">

				                </ul>    
					      </div>
	                 </div>
	                  <div class="sMidItem noMargin errorItem" id="productConfigAdditionalPackageIdsError">
	                       <div class="itemTitle">附加包</div>
	                        <input type="hidden" id="pf_productConfigAdditionalPackageIds" name="pf_productConfigAdditionalPackageIds"/>
	                        <input type="hidden" id="pf_productConfigAdditonalPackageName" name="pf_productConfigAdditionalPackageName"/>
	                       <div class="orderSelect orderMultSelect noclick"> 
	                            <div id="productConfigAdditionalPackageIds"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="multSelect" id="setMult"></ul> 
				           </div>
	                 </div>
	                 
	                  <div class="smallItem errorItem" id="createDateError">
	                       <div class="itemTitle">立项时间<span>*</span></div>
	                       <input readonly class="noborder" id="pf_createDate" name="pf_createDate"/>
	                 </div>
	                 <div class="midItem errorItem" id="projectSqlError">
	                       <div class="itemTitle">项目周期 (天数)<span>*</span></div>
	                       <input id="pf_projectSql" name="pf_projectCycle"/> 
	                 </div>
	                  <div class="bigItem noMargin errorItem" id="filmDestPathError">
	                       <div class="itemTitle">对标影片<span>*</span></div>
	                       <input id="pf_filmDestPath" placeholder="http://example.com" name="pf_filmDestPath"/>
	                 </div>
	           </div>
	    </div>
	    
	    <div class="infoTitle">协同人信息</div> 
		    <div class="outSide">
		           <div class="projectInfo">
		                <div class="smallItem errorItem" id="customerDirectorError">
	                       <div class="itemTitle">项目助理<span>*</span></div>
	                       <input type="hidden" id="ps_customerDirector" name="ps_customerDirector"/>
	                       <div class="orderSelect" >
				                <div id="customerDirector"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="customerDirectors">
				                      <li data-id="1">gg</li>
				                </ul>    
					      </div>
	                    </div>
	                    <div class="smallItem errorItem" id="saleDirectorError">
	                       <div class="itemTitle">销售总监<span>*</span></div>
	                       <input type="hidden" id="ps_saleDirector" name="ps_saleDirector"/>
	                       <div class="orderSelect" >
				                <div id="saleDirector"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="saleDirectors">
				                    <li data-id="1">gg</li>
				                </ul>    
					      </div>
	                 </div>
	                 <div class="smallItem errorItem" id="creativityDirectorError">
	                       <div class="itemTitle">创意总监<span>*</span></div>
	                       <input type="hidden" id="ps_creativityDirector" name="ps_creativityDirector"/>
	                       <div class="orderSelect" >
				                <div id="creativityDirector"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="creativityDirectors">
				                    <li data-id="1">gg</li>
				                </ul>    
					      </div>
	                 </div>
	                 <div class="smallItem errorItem" id="superviseDirectorError">
	                       <div class="itemTitle">监制总监<span>*</span></div>
	                       <input type="hidden" id="ps_superviseDirector" name="ps_superviseDirector"/>
	                       <div class="orderSelect" >
				                <div id="superviseDirector"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="superviseDirectors">
				                    <li data-id="1">gg</li>
				                </ul>    
					      </div>
	                 </div>
	                  <div class="smallItem errorItem" id="teamDirectorError">
	                       <div class="itemTitle">供应商总监<span>*</span></div>
	                       <input type="hidden" id="ps_teamDirector" name="ps_teamDirector"/>
	                       <div class="orderSelect" >
				                <div id="teamDirector"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="teamDirectors">
				                    <li data-id="1">gg</li>
				                </ul>    
					      </div>
	                 </div>
	                  <div class="smallItem errorItem" id="teamProviderError">
	                       <div class="itemTitle">供应商管家<span>*</span></div>
	                       <input type="hidden" id="ps_teamProvider" name="ps_teamProvider"/>
	                       <div class="orderSelect" >
				                <div id="teamProvider"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="teamProviders">
				                    <li data-id="1">gg</li>
				                </ul>    
					      </div>
	                 </div>
	                 <%--  <div class="smallItem errorItem" id="teamPurchaseError">
	                       <div class="itemTitle">供应商采购<span>*</span></div>
	                       <input type="hidden" id="ps_teamPurchase" name="ps_teamPurchase"/>
	                       <div class="orderSelect" >
				                <div id="teamPurchase"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="teamPurchases">
				                    <li data-id="1">gg</li>
				                </ul>    
					      </div>
	                 </div> --%>
	                  <div class="smallItem errorItem" id="financeDirectorError">
	                       <div class="itemTitle">财务主管<span>*</span></div>
	                       <input type="hidden" id="ps_financeDirector" name="ps_financeDirector"/>
	                       <div class="orderSelect">
				                <div id="financeDirector"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="financeDirectors">
				                    <li data-id="1">gg</li>
				                </ul>    
					      </div>
	                 </div>
	                  <div class="smallItem errorItem" id="financeError">
	                       <div class="itemTitle">财务出纳<span>*</span></div>
	                       <input type="hidden" id="ps_finance" name="ps_finance"/>
	                       <div class="orderSelect" >
				                <div id="finance"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="finances">
				                    <li data-id="1">gg</li>
				                </ul>    
					      </div>
	                 </div>
	                    
		           </div>
		    </div> 
		     <div class="infoTitle">客户信息</div> 
		    <div class="outSide">
		           <div class="projectInfo">
			             <div class="midItem errorItem" id="userNameError">
		                       <div class="itemTitle">客户名称<span>*</span></div>
		                       <input id="pu_userName" name="pu_userName" />
		                       <input type="hidden" id="pu_userId" name="pu_userId" />
		                       <ul class="autoFindCus">
		                       </ul>
		                 </div> 
			             <div class="smallItem errorItem" id="linkmanError">
		                       <div class="itemTitle">客户联系人<span>*</span></div>
		                       <input id="pu_linkman" name="pu_linkman"/>
		                 </div>
		                 <div class="smallItem errorItem" id="telephoneError">
		                       <div class="itemTitle">客户电话<span>*</span></div>
		                       <input id="pu_telephone" name="pu_telephone"/>
		                 </div>
		                 <div class="smallItem errorItem" id="userLevelError">
		                       <div class="itemTitle">客户评级<span>*</span></div>
		                       <input readonly id="pu_userLevel" name="pu_userLevel"/>
		                       <div class="orderSelect hide" >
					                <div id="userLevel" data-id="S"></div>
					                <img src="${imgPath}/flow/selectOrder.png">
					                <ul class="oSelect" id="cusLevel">
					                    <li data-id="0">S</li>
					                    <li data-id="1">A</li>
					                    <li data-id="2">B</li>
					                    <li data-id="3">C</li>
					                    <li data-id="4">D</li>
					                </ul>    
						      </div>
		                 </div>
		                 <div class="midItem errorItem" id="emailError">
		                       <div class="itemTitle">邮箱地址<span>*</span></div>
		                       <input id="pu_email" name="pu_email"/>
		                 </div> 
	                
		           </div>
		     </div> 
		     
		      <div class="infoTitle">价格信息</div>
		      <div class="singleItem">
		                       <div class="itemTitle">项目预算</div>
		                       <input id="estimatedPrice" name="pf_estimatedPrice" value="0"/>
		                       <div class="yuan">元</div>
		      </div>
		      <div class="infoTitle">项目描述</div>
		      <div class="outSide errorItemDis" id="outSide" >
		           <textarea id="projectDescription" name="pf_projectDescription"></textarea>         
		      </div>
		      
		      <div class="btnMid " >
		          <a href="JavaScript :history.back(-1)"><div class="btn-c-g">取消</div></a>
		          <div class="btn-c-r" id="toSubmit">确认</div>
		      </div>
	</div>
	  </form> 
	<!-- video-->
</body>

</html>
