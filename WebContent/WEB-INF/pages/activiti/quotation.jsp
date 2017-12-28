<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- css  -->
<spring:url value="/resources/css/activiti/quotation.css" var="quotationCss"/>
<spring:url value="/resources/lib/AirDatepicker/dist/css/datepicker.min.css" var="datepickerCss" />
<!-- js -->
<spring:url value="/resources/lib/jquery/jquery-2.0.3.min.js" var="jqueryJs"/>
<spring:url value="/resources/lib/jquery/jquery.table2excel.js" var="table2excelJs"/>
<spring:url value="/resources/js/common.js" var="commonJs"/>
<spring:url value="/resources/js/activiti/quotation.js" var="quotationJs"/>
<spring:url value="/resources/js/activiti/tablesMergeCell.js" var="tablesMergeCellJs"/>
<spring:url value="/resources/lib/AirDatepicker/dist/js/datepicker.min.js" var="datepickerJs" />
<spring:url value="/resources/lib/AirDatepicker/dist/js/i18n/datepicker.zh.js" var="datepickerZhJs" />
<spring:url value="/resources/lib/jquery.json/jquery.json-2.4.min.js" var="jsonJs" />
<spring:url value="/resources/js/common.js" var="commonJs"/>



<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=9,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="keywords" content="">
<meta name="description" content="">
<meta name="" content="" />
<title>报价单生成器</title>
<!-- css -->
<link rel="stylesheet" href="${quotationCss}">
<link rel="stylesheet" href="${datepickerCss}">

<!-- js -->
<script type="text/javascript" src="${jqueryJs}"></script>
<script type="text/javascript" src="${table2excelJs}"></script>
<script type="text/javascript" src="${commonJs}"></script>
<script type="text/javascript" src="${quotationJs}"></script>
<script type="text/javascript" src="${tablesMergeCellJs}"></script>
<script type="text/javascript" src="${datepickerJs}"></script>
<script type="text/javascript" src="${datepickerZhJs}"></script>
<script type="text/javascript" src="${jsonJs}"></script> 
<script type="text/javascript" src="${commonJs}"></script>


<spring:url value="/resources/images" var="imgPath" />


<!--[if lt IE 9]>
        <script>window.html5 || document.write('<script src="html5shivJs"><\/script>')</script>
    <![endif]-->
</head>


<body>   

<input type="hidden" id="quotationId" value='${quotationId}'>
<input type="hidden" id="projectId" value='${quotationId}'>

<div class="cusModel" id="errorSame">
           <div class="modelCard" >
	           <div class="cardTop">
	                   <div class="title">错误提示</div>
	                   <div class="closeModel"></div>
	            </div>
               <div class="infoWarn">
                   <img src="${imgPath}/index/waring.png">
                   <div>该项目已存在，不能重复添加</br>请修改相应天数和数量</div>
               </div>
               <div class="btnMid">
			           <div class="btn-c-r closeWindow" style="position: relative;left: 80px;margin-bottom: 30px;">确定</div>
		       </div>
           </div>
</div>

<!-- 报错 -->
<div class="cusModel" id="errorModel" >
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">清空表单</div>
                   <div class="closeModel"></div>
            </div>
            <div class="errorContent">
                 <div class="title">确认清空表单吗？</div>
                 <div class="btnMid">
                      <div class="btn-c-g cancle">取消</div>
                      <div class="btn-c-r sureDel">确认</div>
                 </div>
            </div>
     </div>
</div>


<div class="cusModel" id="submitCheckBtn" style="position:block">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">报价单</div>
                   <div class="closeModel"></div>
            </div>
            <div class="infoWarn">
                   <img src="${imgPath}/index/waring.png">
                   <div id="setCheck">确认删除该条数据吗？</div>
               </div>
            <div class="errorContent">
                 <div class="btnMid">
                      <div class="btn-c-g cancle">取消</div>
                      <div class="btn-c-r submitCheckBtn">确认</div>
                 </div>
            </div>
     </div>
</div>



<div class="cusModel" id="submitCheck" >
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title" id="isSuccess">提交成功</div>
                   <div class="closeModel"></div>
            </div>
             <div class="infoWarn">
                   <img id="errorImg" src="${imgPath}/index/waring.png">
             </div>
            <div class="errorContent">
                 <div id="successContent" style="text-align: center;"></div>
                 <div class="btnMid" style="text-align: center;">
                      <div class="btn-c-r sureCheck" style="margin-right:0px!important">确认</div>
                 </div>
            </div>
     </div>
</div>

<!-- <div class="cusModel" id="errorSame" >
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">错误提示</div>
                   <div class="closeModel"></div>
            </div>
            <div class="errorContent">
                 <div class="title" style="line-height: 20px;">该项目已存在，不能重复添加</br>请修改相应天数和数量</div>
                 <div class="btnMid" style="text-align: center;">
                       <div class="btn-c-r closeWindow" style="margin-right:0px!important">确认</div>
                 </div>
            </div>
     </div>
</div> -->


     
     <div class='banner'>
         <span>报价单生成器</span>
     </div>
   
   <div class="pages">
     <div class="searchInfo">
         	<div class="orderItem" id="projectNameError">
	            <div class="mR8">项目名称</div>
	            <input value='${projectName}' id="projectName" style="width:240px">
            </div>
            <div class="orderItem" id="dayTimeError">
	            <div class="mR8">更新于</div>
	            <input class="time noBorder" readonly id="dayTime" name="time" value="">
	        </div>
	          <br/>  
	          
             <div class="orderItem" id="typeError"> 
		            <div class="mR8">收费类</div>
		            <div class="orderSelect oredrTypeSelect"  >
		                <div id="type"></div>
		                <img src="${imgPath}/index/select.png">
		                <ul class="oSelect searchSelect" id="orderType">
		                    <li data-id="">全部</li>
		                    <li data-id="5">线下-直销</li>
		                </ul>    
			        </div>
			  </div>  
			  
			   <div class="orderItem" id="projectChildenError" > 
		            <div class="mR8">收费项</div>
		            <div class="orderSelect orderMultSelect" style="width: 250px !important;">
		                <input type="hidden" id="projectParent"/>
		                <div id="projectChilden"></div>
		                <img src="${imgPath}/index/select.png">
		                <ul class="setMultSelect" id="orderCome">
		                      
		                </ul>    
			        </div>
			  </div>    
	      
	        <div  class="orderItem changeitem" id="dayNumError">
	            <div class="mR8" id="dayT">天数</div>
	            <input class="shortDiv" id="dayNum"  value="">
            </div>
            <div  class="orderItem changeitem" id="needNumError">
	            <div class="mR8" id="needT">数量</div>
	            <input class="shortDiv" id="needNum" value="">
            </div>
             <div class="orderItem " style="position: static;"> 
	            <div class=" searchBtn" style="text-align:center" id="toAdd">添加</div>
	            <div class=" searchBtn"  id="toClear">清空表单</div>
            </div>
            </br>
       <!--      <div  class="orderItem" id="dayNumError">
	            <div class="mR8">整包价</div>
	            <input id="checkbox"  type="checkbox" value="">
            </div> -->
            </br>
             <div class="dir">单价 ：<span id="setCost"></span></div> 
             <div class="dir">收费项描述 ：<span id="setDir"></span></div> 
         </div>

     <table id="process-demo-1" class="tb tb-b c-100 c-t-center">
        <thead>
        <tr class="tableTitle">
            <th>项目组</th>
            <th>项目</th>
            <th>描述</th>
            <th>天数</th>
            <th>数量</th>
            <th>单价(元)</th>
            <th>标价(元)</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody class="setTr">
        </tbody>
    </table>
    
    <div class="tableFoot">
        <div class="noTax">不含税价格： ¥ <span id="localPrice">0</span></div>
        <div class="taxItem">
             <div>税率（%）：</div>
             <input id="tax" value="6">
        </div>
         <div class="taxItem">
             <div>优惠（元）：</div>
             <input id="free" value="0">
        </div>
         <div class="hasTax"><div>含税总价格</div>  <div>¥</div> <div id="setFinalCost">0</div></div>
         <div class="createQuo"><div class="btn-c-r">生成报价单</div></div>
    </div>
  </div>  

</body>


</html>