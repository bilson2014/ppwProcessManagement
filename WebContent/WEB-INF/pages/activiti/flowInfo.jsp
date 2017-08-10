<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%-- import CSS --%>
<spring:url value="/resources/css/activiti/flowInfo.css" var="flowInfoCss"/>
<spring:url value="/resources/lib/AirDatepicker/dist/css/datepicker.min.css" var="datepickerCss" />
<%-- import JS --%>
<spring:url value="/resources/lib/jquery/jquery-2.0.3.min.js" var="jqueryJs"/>
<spring:url value="/resources/js/common.js" var="commonJs"/>
<spring:url value="/resources/lib/AirDatepicker/dist/js/datepicker.min.js" var="datepickerJs" />
<spring:url value="/resources/lib/AirDatepicker/dist/js/i18n/datepicker.zh.js" var="datepickerZhJs" />
<spring:url value="/resources/js/activiti/textFlowI.js" var="textFlowIJs"/>
<spring:url value="/resources/js/activiti/dynamic-form-handler.js" var="textFlowIJs"/>
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
<meta name="baidu-site-verification" content="dMz6jZpIwd" />
<title></title>
<link rel="stylesheet" href="${datepickerCss}">
<link rel="stylesheet" href="${flowInfoCss}">

<script type="text/javascript" src="${jqueryJs}"></script>
<script type="text/javascript" src="${datepickerJs}"></script>
<script type="text/javascript" src="${datepickerZhJs}"></script>
<script type="text/javascript" src="${commonJs}"></script>
<script type="text/javascript" src="${textFlowIJs}"></script>

<!--[if lt IE 9]><script>window.html5 || document.write('<script src="html5shivJs"><\/script>')</script><![endif]-->

</head>

<body>


<div class="cusModel" id="cusModel">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">完善客户信息</div>
                   <div class="state">
	                   <img src="/resources/images/provider/toWait.png">
	                   <div>进行中</div>
                   </div>
                   <div class="closeModel"></div>
            </div>
            <div class="cardContent">
                 <div class="contentItem">
	                  <div class="title">事件说明 : </div>
	                  <div class="content">打算打打三大所大所多</div>
	             </div>
	              <div class="contentItem">
	                  <div class="title">开始时间 : </div>
	                  <div class="content">2017-11-12</div>
	             </div>   
	              <div class="contentItem">
	                  <div class="title">截止说明 : </div>
	                  <div class="content">2017-11-12</div>
	             </div>
	             <div class="contentItem">
	                  <div class="title">实际周期 : </div>
	                  <div class="content">2周</div>
	             </div>
	             <div class="itemHeight">
	             <div class="infoItem">
	                       <div  class="itemTop">
	                             <img class="logo" src="">
	                              <ul>
	                                 <li>策划人<span>发布于201021</span></li>
	                                 <li>上传了<span>策划方案</span> <img class="modelOpen" src="/resources/images/flow/areaMore.png"></li>
	                              </ul>
	                       </div>
	                       <div class="itemArea">
	                             <div><span>负责人 : </span><span>需要调整一下</span></div>
	                             <div><span>负责人回复负责人 :</span><span>需要调整一下</span></div>
	                             <input>
	                       </div>
	             </div>
	             
	              <div class="infoItem">
	                       <div  class="itemTop">
	                             <img class="logo" src="">
	                              <ul>
	                                 <li>策划人<span>发布于201021</span></li>
	                                 <li>上传了<span>策划方案</span> <img class="modelOpen" src="/resources/images/flow/areaMore.png"></li>
	                              </ul>
	                       </div>
	                       <div class="itemArea">
	                             <div><span>负责人 : </span><span>需要调整一下</span></div>
	                             <div><span>负责人回复负责人 :</span><span>需要调整一下</span></div>
	                             <input>
	                       </div>
	             </div>
	             </div>
	                             <div class="getMore">
	                                  <div>展开更多</div>
	                                  <div></div>
	                             </div>
            </div>
     </div>
</div>

<!-- 报错 -->
<div class="cusModel" id="errorModel" >
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">报错备注</div>
                   <div class="closeModel"></div>
            </div>
            <div class="errorContent">
                 <div class="title">请将您的报错里有些如下方留言框中：</div>
                 <textarea></textarea>
                 <div class="errorInfo">注意：报错后将开始预备报错任务相同的新任务，请确认信息无误后进行报错。</div>
                 <div class="btnMid">
                      <div class="btn-c-g">取消</div>
                      <div class="btn-c-r">确认</div>
                 </div>
            </div>
     </div>
</div>
<!-- 上传文件 -->
<div class="cusModel" id="upModel">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">文件上传</div>
                   <div class="closeModel"></div>
            </div>
            <div class="upContent">
                 <div class="item">
                      <div class="title">选择分类</div>
                      <div class="orderSelect" >
			                <div id="sIndentSource"></div>
			                <img src="${imgPath}/flow/selectOrder.png">
			                <ul class="oSelect searchSelect" id="orderCome">
			                    <li data-id="">全部</li>	
			                </ul>    
				        </div>
                 </div>
                  <div class="item">
                      <div class="title">选择文件</div>
                      <input>
                      <div class="find">浏览</div>
                 </div>
                 <div class="btnMid">
                      <div class="btn-c-g">取消</div>
                      <div class="btn-c-r">确认</div>
                 </div>
            </div>
     </div>
</div>

<!-- 客户信息修改 -->
<div class="cusModel" id="cusInfoModel">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">客户信息修改</div>
                   <div class="closeModel"></div>
            </div>
            <div class="cusInfoContent">
                 <div class="item">
                       <div class="title">客户名称</div>
                       <input>
                 </div>
                  <div class="item">
                       <div class="title">客户联系人</div>
                       <input>
                 </div>
                  <div class="item">
                       <div class="title">客户联系电话</div>
                       <input>
                 </div>
                  <div class="item">
                       <div class="title">客户评分</div>
                       <div class="point">客户评分</div>
                 </div>
	             <div class="btnMid">
	                      <div class="btn-c-g">取消</div>
	                      <div class="btn-c-r">确认</div>
	             </div>
            </div>
     </div>
</div>

<!-- 版本管理 -->
<div class="cusModel" id="controlModel">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">版本管理</div>
                   <div class="closeModel"></div>
            </div>
            <div class="controlContent">
                      <div class="item">
                             <div class="itemTop">
                                  <div class="controlOpen"></div>
                                  <div class="title">需求文档</div>
                             </div>
                             <div class="getInfoItemContent">
                                <div class="InfoItem">
                                  <div class="fileName">文件名</div>
                                  <div class="name">策划人</div>
                                  <div class="time">上传于:2017 17:59:08</div>
                                  <div class="icon">
                                              <img class="flag" src="/resources/images/flow/flag.png">
	                                          <img class="look" src="/resources/images/flow/look.png">
	                                          <img class="share" src="/resources/images/flow/share.png">
	                                          <img class="download" src="/resources/images/flow/download.png">
	                              </div>
	                            </div>
	                            <div class="InfoItem">
                                  <div class="fileName">文件名</div>
                                  <div class="name">策划人</div>
                                  <div class="time">上传于:2017 17:59:08</div>
                                  <div class="icon">
                                              <img class="flag" src="/resources/images/flow/flag.png">
	                                          <img class="look" src="/resources/images/flow/look.png">
	                                          <img class="share" src="/resources/images/flow/share.png">
	                                          <img class="download" src="/resources/images/flow/download.png">
	                              </div>
	                            </div>  
	                            <div class="InfoItem">
                                  <div class="fileName">文件名</div>
                                  <div class="name">策划人</div>
                                  <div class="time">上传于:2017 17:59:08</div>
                                  <div class="icon">
                                              <img class="flag" src="/resources/images/flow/flag.png">
	                                          <img class="look" src="/resources/images/flow/look.png">
	                                          <img class="share" src="/resources/images/flow/share.png">
	                                          <img class="download" src="/resources/images/flow/download.png">
	                              </div>
	                            </div>    
                             </div>
                      </div>
                      
                         <div class="item">
                             <div class="itemTop">
                                  <div class="controlOpen"></div>
                                  <div class="title">需求文档</div>
                             </div>
                             <div class="getInfoItemContent">
                                <div class="InfoItem">
                                  <div class="fileName">文件名</div>
                                  <div class="name">策划人</div>
                                  <div class="time">上传于:2017 17:59:08</div>
                                  <div class="icon">
                                              <img class="flag" src="/resources/images/flow/flag.png">
	                                          <img class="look" src="/resources/images/flow/look.png">
	                                          <img class="share" src="/resources/images/flow/share.png">
	                                          <img class="download" src="/resources/images/flow/download.png">
	                              </div>
	                            </div>
	                            <div class="InfoItem">
                                  <div class="fileName">文件名</div>
                                  <div class="name">策划人</div>
                                  <div class="time">上传于:2017 17:59:08</div>
                                  <div class="icon">
                                              <img class="flag" src="/resources/images/flow/flagRed.png">
	                                          <img class="look" src="/resources/images/flow/look.png">
	                                          <img class="share" src="/resources/images/flow/share.png">
	                                          <img class="download" src="/resources/images/flow/download.png">
	                              </div>
	                            </div>  
	                            <div class="InfoItem">
                                  <div class="fileName">文件名</div>
                                  <div class="name">策划人</div>
                                  <div class="time">上传于:2017 17:59:08</div>
                                  <div class="icon">
                                              <img class="flag" src="/resources/images/flow/flag.png">
	                                          <img class="look" src="/resources/images/flow/look.png">
	                                          <img class="share" src="/resources/images/flow/share.png">
	                                          <img class="download" src="/resources/images/flow/download.png">
	                              </div>
	                            </div>    
                             </div>
                      </div>
            </div>
     </div>
</div>

<!-- 协同人清单 -->
<div class="cusModel" id="helperModel">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">协同人清单</div>
                   <div class="closeModel"></div>
            </div>
            <div class="helperContent">
                   <div class="helpItem">
                       <div class="title">客服总监</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="helpItem">
                       <div class="title">销售总监</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="helpItem">
                       <div class="title">创意总监</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="helpItem">
                       <div class="title">策划</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="helpItem">
                       <div class="title">供应商总监</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="helpItem">
                       <div class="title">供应商管家</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="helpItem">
                       <div class="title">供应商采购</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="helpItem">
                       <div class="title">监制总监</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="helpItem">
                       <div class="title">监制</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="helpItem">
                       <div class="title">财务出纳</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="helpItem">
                       <div class="title">财务主管</div>
                       <div class="name">人名</div>     
                   </div>
            </div>
     </div>
</div>

<!-- 客户转账信息 -->
<div class="cusModel" id="cusPriceModel">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title" id="cusPriceModelTitle">客户转账信息</div>
                   <div class="closeModel"></div>
            </div>
            <div class="cusPriceContent">
                   <div class="cusPriceItem">
                       <div class="title">交易流水号</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="cusPriceItem">
                       <div class="title">交易方式</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="cusPriceItem">
                       <div class="title">交易时间</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="cusPriceItem">
                       <div class="title">订单编号</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="cusPriceItem">
                       <div class="title">交易方式</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="cusPriceItem">
                       <div class="title">交易金额</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="cusPriceItem">
                       <div class="title">描述</div>
                       <div class="name">人名</div>     
                   </div>
            </div>
     </div>
</div>

<!-- 发票信息 -->
<div class="cusModel" id="invoiceModel">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">发票信息</div>
                   <div class="closeModel"></div>
            </div>
            <div class="invoiceContent">
                   <div class="invoiceItem">
                       <div class="title">发票类型</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="invoiceItem">
                       <div class="title">发票编号</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="invoiceItem">
                       <div class="title">发票内容</div>
                       <div class="name">人名</div>     
                   </div>
                    <div class="invoiceItem">
                       <div class="title">价税合计</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="invoiceItem">
                       <div class="title">发票税率</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="invoiceItem">
                       <div class="title">开票时间</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="invoiceItem">
                       <div class="title">付款时间</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="invoiceItem">
                       <div class="title">提供人</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="invoiceItem">
                       <div class="title">描述</div>
                       <div class="name">人名</div>     
                   </div>
            </div>
     </div>
</div>

 <!-- 提示 -->
<div class="cusModel" id="warnModel">
     <div class="modelCard smallModel">
            <div class="cardTop">
                   <div class="title">提示</div>
                   <div class="closeModel"></div>
            </div>
            <div class="warnContent">
                     <img src="/resources/images/flow/warn.png">
                    <div class="info">内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容</div>
                    <div class="btnMid">
                      <div class="btn-c-g">取消</div>
                      <div class="btn-c-r">确认</div>
                    </div>  
            </div>
     </div>
</div>

 <!-- 分配策划 -->
<div class="cusModel" id="plotModel" >
     <div class="modelCard smallModel">
            <div class="cardTop">
                   <div class="title" id="plotTitle">分配策划</div>
                   <div class="closeModel"></div>
            </div>
            <div class="plotContent">
	                      <div class="title">策划人</div>
				          <div class="orderSelect" >
				                <div id="sIndentSource"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="orderCome">
				                     <li>adasda</li>
				                     <li>dasdas</li>
				                     <li>dasda</li>
				                </ul>    
					      </div>
			     <div class="btnMid">
                      <div class="btn-c-g">取消</div>
                      <div class="btn-c-r">确认</div>
                 </div>
	     </div>
	</div>
</div>

 <!-- 填写供应商实际金额 -->
<div class="cusModel" id="priceModel">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">填写供应商实际金额</div>
                   <div class="closeModel"></div>
            </div>
            <div class="priceContent">
	                      <div class="title">实际金额</div>
				          <input>
				          <div class="yuan">元</div>
				          <div class="title" style="margin-top:20px;">发票抬头</div>
				          <div class="orderSelect" >
				                <div id="sIndentSource"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="orderCome">
				                     <li>adasda</li>
				                     <li>dasdas</li>
				                     <li>dasda</li>
				                </ul>    
					      </div>
				          
						  <div class="btnMid">
			                      <div class="btn-c-g">取消</div>
			                      <div class="btn-c-r">确认</div>
			              </div>
	         </div>
	</div>
</div>

 <!-- 填写客户预算信息 -->
<div class="cusModel" id="budgetModel">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">填写客户预算信息</div>
                   <div class="closeModel"></div>
            </div>
            <div class="budgetContent">
                    <div class="item">
                         <div class="title">项目预算</div>
                         <input>
                         <div class="yuan">元</div>
                    </div>
                    <div class="itemTime">
                         <div class="title">项目交付时间</div>
                         <input>
                    </div>
                    <div class="itemTime">
                         <div class="title">约定付款时间</div>
                         <input>
                    </div>
                    <div class="btnMid">
		                      <div class="btn-c-g">取消</div>
		                      <div class="btn-c-r">确认</div>
		                </div>
            </div>
	</div>
</div>
 <!-- 填写收款信息 -->
<div class="cusModel" id="getPriceModel">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">填写收款信息</div>
                   <div class="closeModel"></div>
            </div>
            <div class="getPriceContent">
                    <div class="item">
                         <div class="title">交易流水号</div>
                         <input>
                    </div>
                    <div class="itemTime">
                         <div class="title">交易时间</div>
                         <input id="orderTime">
                    </div>
                    <div class="itemTime">
                         <div class="title">交易金额</div>
                         <input>
                         <div class="yuan">元</div>
                    </div>
                    <div class="item">
                         <div class="title">交易流水号</div>
                         <input>
                    </div>
                    <div class="item">
                         <div class="title areaTitle">描述</div>
                         <textarea></textarea>
                    </div>
                    <div class="btnMid">
		                      <div class="btn-c-g">取消</div>
		                      <div class="btn-c-r">确认</div>
		            </div>
            </div>
</div>
</div>

<!-- 分配策划供应商 -->
<div class="cusModel" id="">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">分配策划供应商</div>
                   <div class="closeModel"></div>
            </div>
            <div class="getPriceContent">
                    <div class="item">
                         <div class="title">供应商名称</div>
                         <input>
                    </div>
                    <div class="itemTime">
                         <div class="title">供应商联系人</div>
                         <input id="orderTime">
                    </div>
                    <div class="itemTime">
                         <div class="title">供应商电话</div>
                         <input>
                    </div>
                    <div class="itemTime slow">
                         <div class="title">供应商预算价格</div>
                         <input>
                         <div class="yuan">元</div>
                    </div>
                    <div class="itemTime">
                         <div class="title">制作类型</div>
                         <div class="orderSelect" >
				                <div id="sIndentSource"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="orderCome">
				                     <li>adasda</li>
				                     <li>dasdas</li>
				                     <li>dasda</li>
				                </ul>    
					      </div>
                    </div>
                    
                    <div class="item">
                         <div class="title areaTitle">制作内容</div>
                         <textarea></textarea>
                    </div>
                    
                    <div class="itemTime">
                         <div class="title">对接人</div>
                         <input>
                    </div>
                    
                    <div class="itemTime">
                         <div class="title">对接电话</div>
                         <input>
                    </div>
                    
                    <div class="itemTime">
                         <div class="title">项目交付时间</div>
                         <input>
                    </div>
                    
                    <div class="btnMid">
		                      <div class="btn-c-g">取消</div>
		                      <div class="btn-c-r">确认</div>
		            </div>
            </div>
</div>
</div>

<!-- 分配制作供应商 -->
<div class="cusModel" id="">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">分配制作供应商</div>
                   <div class="closeModel"></div>
            </div>
            <div class="getPriceContent">
                    <div class="item">
                         <div class="title">供应商名称</div>
                         <input>
                    </div>
                    <div class="itemTime">
                         <div class="title">供应商联系人</div>
                         <input id="orderTime">
                    </div>
                    <div class="itemTime">
                         <div class="title">供应商电话</div>
                         <input>
                    </div>
                    <div class="itemTime bigSlow">
                         <div class="title">供应商预算价格</div>
                          <div class="orderSelect" >
				                <div id="sIndentSource"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="orderCome">
				                     <li>adasda</li>
				                     <li>dasdas</li>
				                     <li>dasda</li>
				                </ul>    
					      </div>
					      <div class="yuan">元</div>
                    </div>
                    <div class="itemTime bBigSlow">
                         <div class="title">策划内容</div>
                         <div class="orderSelect" >
				                <div id="sIndentSource"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="orderCome">
				                     <li>adasda</li>
				                     <li>dasdas</li>
				                     <li>dasda</li>
				                </ul>    
					      </div>
                    </div>
                    
                    <div class="item">
                         <div class="title areaTitle">制作内容</div>
                         <textarea></textarea>
                    </div>
                    
                    <div class="itemTime">
                         <div class="title">对接人</div>
                         <input>
                    </div>
                    
                    <div class="itemTime">
                         <div class="title">对接电话</div>
                         <input>
                    </div>
                    
                    <div class="itemTime">
                         <div class="title">项目交付时间</div>
                         <input>
                    </div>
                    
                    <div class="btnMid">
		                      <div class="btn-c-g">取消</div>
		                      <div class="btn-c-r">确认</div>
		            </div>
            </div>
</div>
</div>

<!-- 项目信息修改 -->
<div class="cusModel">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">项目信息修改</div>
                   <div class="closeModel"></div>
            </div>
            <div class="getPriceContent">
                    <div class="item">
                         <div class="title">项目名称</div>
                         <input>
                    </div>
                    <div class="itemTime">
                         <div class="title">项目评级</div>
                         <div class="orderSelect so" >
				                <div id="sIndentSource"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="orderCome">
				                     <li>adasda</li>
				                     <li>dasdas</li>
				                     <li>dasda</li>
				                </ul>    
					      </div>
                    </div>
                    <div class="itemTime">
                         <div class="title">项目周期</div>
                         <input>
                         <div class="yuan">天</div>
                    </div>
                     <div class="itemTime">
                         <div class="title">等级</div>
                         <div class="orderSelect so" >
				                <div id="sIndentSource"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="orderCome">
				                     <li>adasda</li>
				                     <li>dasdas</li>
				                     <li>dasda</li>
				                </ul>    
					      </div>
                    </div>
                    <div class="itemTime">
                         <div class="title">时长</div>
                         <input id="orderTime">
                    </div>
                   
                    <div class="itemTime bBigSlow">
                         <div class="title">附加包</div>
                         <div class="orderSelect" >
				                <div id="sIndentSource"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="orderCome">
				                     <li>adasda</li>
				                     <li>dasdas</li>
				                     <li>dasda </li>
				                </ul>    
					      </div>
                    </div>
                    
                    <div class="itemTime bBigSlow">
                         <div class="title">对标影片</div>
                         <input >
                    </div>
                    
                    <div class="item">
                         <div class="title areaTitle">制作内容</div>
                         <textarea></textarea>
                    </div>
                    
                    <div class="btnMid">
		                      <div class="btn-c-g">取消</div>
		                      <div class="btn-c-r">确认</div>
		            </div>
            </div>
</div>
</div>

<!-- 客户信息修改 -->
<div class="cusModel">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">客户信息修改</div>
                   <div class="closeModel"></div>
            </div>
            <div class="getPriceContent">
                    <div class="itemTime">
                         <div class="title">客户名称</div>
                         <input>
                    </div>
                    <div class="itemTime">
                         <div class="title">客户联系人</div>
                         <input>
                    </div>
                    <div class="itemTime">
                         <div class="title">客户联系人</div>
                         <input>
                    </div>
                    <div class="itemTime">
                         <div class="title">客户评级</div>
                         <div class="orderSelect so" >
				                <div id="sIndentSource"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="orderCome">
				                     <li>adasda</li>
				                     <li>dasdas</li>
				                     <li>dasda</li>
				                </ul>    
					      </div>
                    </div>
                    
                    <div class="btnMid">
		                      <div class="btn-c-g">取消</div>
		                      <div class="btn-c-r">确认</div>
		            </div>
            </div>
</div>
</div>

<!-- 供应商信息修改 -->
<div class="cusModel">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">供应商信息修改 </div>
                   <div class="closeModel"></div>
            </div>
            <div class="getPriceContent">
                    
                    <div class="bigTitle">策划供应商</div>
            
                    <div class="item">
                         <div class="title">客户名称</div>
                         <input>
                    </div>
                    <div class="itemTime">
                         <div class="title">客户联系人</div>
                         <input>
                    </div>
                    <div class="itemTime">
                         <div class="title">客户联系人</div>
                         <input>
                    </div>
                    
                    <div class="bigTitle">策划供应商</div>
            
                    <div class="item">
                         <div class="title">客户名称</div>
                         <input>
                    </div>
                    <div class="itemTime">
                         <div class="title">客户联系人</div>
                         <input>
                    </div>
                    <div class="itemTime">
                         <div class="title">客户联系人</div>
                         <input>
                    </div>
                    
                    
                    <div class="btnMid">
		                      <div class="btn-c-g">取消</div>
		                      <div class="btn-c-r">确认</div>
		            </div>
            </div>
</div>
</div>

<!-- 其它信息修改 -->
<div class="cusModel">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">其它信息修改 </div>
                   <div class="closeModel"></div>
            </div>
            <div class="otherContent">
                     
                     <div class="item">
	                       <div class="title">客户约定付款时间</div>
	                       <input>
                     </div>
                      <div class="item">
	                       <div class="title">客户项目交付时间</div>
	                       <input>
                     </div>
                     
                      <div class="item">
	                       <div class="title">策划供应商启动函备注信息</div>
	                       <textarea></textarea>
                     </div>
                     
                     <div class="item">
	                       <div class="title">策划供应商启动函备注信息</div>
	                       <textarea></textarea>
                     </div>
                    
                    
                    <div class="btnMid">
		                      <div class="btn-c-g">取消</div>
		                      <div class="btn-c-r">确认</div>
		            </div>
            </div>
</div>
</div>

<!-- 价格信息修改 -->
<div class="cusModel" >
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">价格信息修改 </div>
                   <div class="closeModel"></div>
            </div>
            <div class="getPriceContent">
                    <div class="itemTime">
                         <div class="title">预估价格</div>
                         <input>
                         <div class="yuan syuan">元</div>
                    </div>
                    <div class="itemTime">
                         <div class="title">客户项目预算</div>
                         <input>
                         <div class="yuan syuan">元</div>
                    </div>
                    <div class="itemTime">
                         <div class="title">策划供应商预算</div>
                         <input>
                         <div class="yuan syuan">元</div>
                    </div>
                    <div class="itemTime">
                         <div class="title">制作供应商预算</div>
                         <div class="orderSelect so" >
				                <div id="sIndentSource"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="orderCome">
				                     <li>adasda</li>
				                     <li>dasdas</li>
				                     <li>dasda</li>
				                </ul>    
					      </div>
					      <div class="yuan syuan">元</div>
                    </div>
                    <div class="itemTime">
                         <div class="title">制作供应商结算</div>
                         <input>
                         <div class="yuan syuan">元</div>
                    </div>
                    
                    <div class="btnMid">
		                      <div class="btn-c-g">取消</div>
		                      <div class="btn-c-r">确认</div>
		            </div>
            </div>
</div>
</div>


 <!-- 填写供应商发票信息 -->
<div class="cusModel" id="getBillModel">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">填写供应商发票信息</div>
                   <div class="closeModel"></div>
            </div>
            <div class="getBillContent">
                    <div class="item">
                      <div class="title">发票类型</div>
                      <div class="orderSelect" >
			                <div id="sIndentSource"></div>
			                <img src="${imgPath}/flow/selectOrder.png">
			                <ul class="oSelect searchSelect" id="orderCome">
			                    <li data-id="">全部</li>
			                </ul>    
				      </div>
				   </div>
				   
				   <div class="item">
                      <div class="title">发票税率</div>
                      <input>
				   </div>
				    <div class="item">
                      <div class="title">发票编号</div>
                      <input>
				   </div>
				    <div class="item">
                      <div class="title">开票时间</div>
                      <input name="findTime" id="findTime">
				   </div>
				    <div class="item">
                      <div class="title">发票内容</div>
                      <input>
				   </div>
				    <div class="item">
                      <div class="title">付款时间</div>
                      <input id="payTime">
				   </div>
				    <div class="item">
                      <div class="title">税价合计</div>
                      <input name="payTime">
				   </div>
	               <div class="itemArea">
                      <div class="title">备注</div>
                      <textarea></textarea>
				   </div>
				   
				    <div class="btnMid">
		                      <div class="btn-c-g">取消</div>
		                      <div class="btn-c-r">确认</div>
		            </div>
            </div>
     </div>       
</div>

<%--  <!-- 文件上传 -->
<div class="cusModel" id="getBillModel" style="display:block">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">填写供应商发票信息</div>
                   <img class="closeModel" src="${imgPath}/flow/canclemodal.png">
            </div>
     </div>       
</div> --%>

	<input type="hidden" id="storage_node" value="${file_locate_storage_path }" />
	    <div class="page">
	    <div id="showPrice">收款信息</div>
	    <div id="showBudget">客户预算</div>
	    <div id="showRealPrice">实际金额</div>
	    <div id="showPlot">策划</div>
	    <div id="invoiceInfo">发票信息</div>
	    <div id="showHelper">协同人清单</div>
	    <div id="showControl">版本管理</div>
	    <div id="showCusEdit">用户信息修改</div>
	    <div id="showUp">上传文件</div>
	    <div id="showError">驳回</div>
	    <div id="finishCus">完善客户信息</div>
	    <div id="showshowExecutive">分配监制</div>
	    
             	    	    <jsp:include flush="true" page="../header.jsp"></jsp:include>
	                        <jsp:include flush="true" page="flowMenu.jsp"></jsp:include>  
	           <div class="productInfo">
	                <div class="infoTitle">
	                     <div class="titleName">项目详情标题</div>
	                     <div class="point">
                              <div class="showPoint">SA</div>
                              <div class="showDeil showDownDeil">
                                    <div class="proPoint">项目评级<span>S</span></div>
                                    <div class="cusPoint">客户评级<span>A</span></div>
                              </div>
	                     </div>
	                     <div class="proControl">项目操作
	                         <div class="newControl">
	                              <div id="showWarn">暂停项目</div>
	                              <div id="showWarn">取消项目</div>
	                         </div>
	                     </div>
	                </div>
	                <div class="infoLine"></div>
	                <div class="waitMission" id="waitMission">
	                       <div class="missionTop">
	                            <div class="missinName">待办任务一 : </div>
	                            <div class="missinInfo">完善客户信息</div>
	                            <div class="missinState"><img src="/resources/images/provider/toWait.png"><div>进行中</div></div>
	                            <div class="missinTime"><img src="/resources/images/flow/lastTime.png"><div>进行中</div></div>
	                            <div class="contentDiv">
	                               <div class="setContent">
	                                    <div class="redContent">测试文字测试文字</div>
	                                    <div class="simContent">测试文字测试文字</div>
	                                    <div class="setBtn">
	                                         <div class="grayBtn btn-c-g" id="toFinish">立即完善</div>
	                                         <div class="redBtn btn-c-r">确认完成</div>
	                                    </div>
	                               </div>
	                            </div>
	                       </div>
	                       <div class="missionTop">
	                            <div class="missinName">待办任务二 : </div>
	                            <div class="missinInfo">完善客户信息</div>
	                            <div class="missinState"><img src="/resources/images/provider/toWait.png"><div>2017</div></div>
	                            <div class="missinTime"><img src="/resources/images/flow/lastTime.png"><div>2017</div></div>
	                            <div class="contentDiv">
	                               <div class="setContent">
	                                    <div class="redContent">测试文字测试文字</div>
	                                    <div class="simContent">测试文字测试文字</div>
	                                    <div class="setBtn">
	                                         <div class="grayBtn btn-c-g">立即上传</div>
	                                         <div class="redBtn btn-c-r">确认完成</div>
	                                    </div>
	                               </div>
	                            </div>
	                       </div>
	                   </div>    
                     </div>
	                   <div class="productInfo secondProduct">    
	                       <div class="projectTitle">项目进度及历史</div>
	                        <div class="timeFlow">
	                            <img src="/resources/images/flow/demoG.png">
	                            <div class="flowIcon step2">
	                                 <div>沟通</div>
	                                 <div>方案</div>
	                                 <div>商务</div>
	                                 <div>制作</div>
	                                 <div>交付</div>
	                                 <img class="icons" src="/resources/images/flow/down.png">
	                            </div>
	                        </div>
	                       <div class="setListDiv">
	                               <div class="ListTop">
	                                     <div class="startTime">阶段起始时间:<span>2017.7.9</span></div>
	                                     <div class="endTime">阶段计划完成时间<span>2017-07-09  14：00</span></div>
	                               </div>
	                               <div class="listContent">
	                                   <div class="listItem">
	                                        <div class="lineStart"></div>
	                                        <div class="time">预计：2017-07-09  14：00</div>
	                                        <div class="user">策划人AAA</div>
	                                        <div class="info">各种信息</div>
	                                        <div class="state"><img src="/resources/images/provider/toWait.png"><div class="green">已完成</div></div>
	                                        <div class="find">查看</div>
	                                   </div>
	                                    <div class="listItem">
	                                        <div class="lineOne"></div>
	                                        <div class="time">预计：2017-07-09  14：00</div>
	                                        <div class="user">策划人AAA</div>
	                                        <div class="info">各种信息</div>
	                                        <div class="state"><img src="/resources/images/provider/toWait.png"><div class="redWord">已报错</div></div>
	                                        <div class="find">查看</div>
	                                   </div>
	                                    <div class="listItem">
	                                        <div class="lineOne"></div>
	                                        <div class="time">预计：2017-07-09  14：00</div>
	                                        <div class="user">策划人AAA</div>
	                                        <div class="info">各种信息</div>
	                                        <div class="state"><img src="/resources/images/provider/toWait.png"><div class="yellow">进行中</div></div>
	                                        <div class="find">查看</div>
	                                   </div>
	                                    <div class="listItem">
	                                        <div class="lineOne"></div>
	                                        <div class="time">预计：2017-07-09  14：00</div>
	                                        <div class="user">策划人AAA</div>
	                                        <div class="info">各种信息</div>
	                                        <div class="state"><img src="/resources/images/provider/toWait.png"><div class="dark">延误</div></div>
	                                        <div class="find">查看</div>
	                                   </div>
	                                    <div class="listItem">
	                                        <div class="lineEnd"></div>
	                                        <div class="time">预计：2017-07-09  14：00</div>
	                                        <div class="user">策划人AAA</div>
	                                        <div class="info">各种信息</div>
	                                        <div class="state"><img src="/resources/images/provider/toWait.png"><div class="gray">未开始</div></div>
	                                        <div class="find">查看</div>
	                                   </div>
	                               </div>
	                       </div>
	                       
	                         <div class="getInfoItem">
	                            <div class="getInfoItemTop">
	                                 <div class="controlOpen"></div>
	                                 <div class="info">团队信息</div>
	                                 <div class="time">最后更新于 2017-07-09  14：00</div>
	                                 <div class="update btn-c-r" >更新</div>
	                            </div>
	                            <div class="getInfoItemContent">
	                                 <div class="imgItem">
	                                      <img src>
	                                      <ul>
	                                          <li>三维</li>
	                                          <li>负责人</li>
	                                          <li>线上-网站</li>
	                                      </ul>
	                                 </div>
	                                  <div class="imgItem">
	                                      <img src>
	                                      <ul>
	                                          <li>三维</li>
	                                          <li>负责人</li>
	                                          <li>线上-网站</li>
	                                      </ul>
	                                 </div>
	                                  <div class="imgItem">
	                                      <img src>
	                                      <ul>
	                                          <li>三维</li>
	                                          <li>负责人</li>
	                                          <li>线上-网站</li>
	                                      </ul>
	                                 </div>
	                                  <div class="imgItem">
	                                      <img src>
	                                      <ul>
	                                          <li>三维</li>
	                                          <li>负责人</li>
	                                          <li>线上-网站</li>
	                                      </ul>
	                                 </div>
	                            </div>
	                         </div>   
	                       
	                       <div class="getInfoItem">
	                            <div class="getInfoItemTop">
	                                 <div class="controlOpen"></div>
	                                 <div class="info">项目信息</div>
	                                 <div class="time">最后更新于 2017-07-09  14：00</div>
	                                 <div class="update btn-c-r">更新</div>
	                            </div>
	                            <div class="getInfoItemContent">
	                                  <div class="contentItem">
	                                       <div class="item">
	                                          <div>项目编号</div>
	                                          <div>内容内容</div>
	                                       </div>
	                                        <div class="item">
	                                          <div>项目名称</div>
	                                          <div>内容内容</div>
	                                       </div>
	                                        <div class="item">
	                                          <div>项目评级</div>
	                                          <div>内容内容</div>
	                                       </div>
	                                       <div class="item">
	                                          <div>项目来源</div>
	                                          <div>内容内容</div>
	                                       </div>
	                                  </div>
	                                   <div class="contentItem">
	                                       <div class="item">
	                                          <div>产品线</div>
	                                          <div>内容内容</div>
	                                       </div>
	                                        <div class="item">
	                                          <div>项目周期</div>
	                                          <div>内容内容</div>
	                                       </div>
	                                        <div class="item">
	                                          <div>立项时间</div>
	                                          <div>内容内容</div>
	                                       </div>
	                                  </div>
	                                  <div class="longItem">
	                                      <div>项目配置</div>
	                                      <div>内容内容</div>
	                                  </div>
	                                   <div class="longItem">
	                                      <div>对标影片</div>
	                                      <div>内容内容</div>
	                                  </div>
	                                   <div class="longItem">
	                                      <div>项目描述</div>
	                                      <div>内容内容</div>
	                                  </div>
	                            </div>
	                       </div>
	                       
	                        <div class="getInfoItem">
	                            <div class="getInfoItemTop">
	                                 <div class="controlOpen"></div>
	                                 <div class="info">客户信息</div>
	                                 <div class="time">最后更新于 2017-07-09  14：00</div>
	                                 <div class="update btn-c-r">更新</div>
	                            </div>
	                            <div class="getInfoItemContent">
	                                  
	                                  <div class="contentItem">
	                                       <div class="item">
	                                          <div>客户名称</div>
	                                          <div>内容内容</div>
	                                       </div>
	                                        <div class="item">
	                                          <div>客户评级</div>
	                                          <div>内容内容</div>
	                                       </div>
	                                        <div class="item">
	                                          <div>客户联系人</div>
	                                          <div>内容内容</div>
	                                       </div>
	                                        <div class="item">
	                                          <div>客户电话</div>
	                                          <div>内容内容</div>
	                                       </div>
	                                  </div>
	                            </div>
	                       </div>
	                       
	                        <div class="getInfoItem">
	                            <div class="getInfoItemTop">
	                                 <div class="controlOpen"></div>
	                                 <div class="info">供应商信息</div>
	                                 <div class="time">最后更新于 2017-07-09  14：00</div>
	                                 <div class="update btn-c-r">更新</div>
	                            </div>
	                            <div class="getInfoItemContent">
	                                  <div class="title"><div class="long"></div><div class="short"></div>策划供应商</div>
	                                  <div class="contentItem">
	                                       <div class="item">
	                                          <div>供应商名称</div>
	                                          <div>内容内容</div>
	                                       </div>
	                                        <div class="item">
	                                          <div>供应商联系人</div>
	                                          <div>内容内容</div>
	                                       </div>
	                                        <div class="item">
	                                          <div>供应商联系电话</div>
	                                          <div>内容内容</div>
	                                       </div>
	                                  </div>
	                                  <div class="title"><div class="long"></div><div class="short"></div>制作供应商</div>
	                                  <div class="contentItem">
	                                       <div class="item">
	                                          <div>供应商名称</div>
	                                          <div>内容内容</div>
	                                       </div>
	                                        <div class="item">
	                                          <div>供应商联系人</div>
	                                          <div>内容内容</div>
	                                       </div>
	                                        <div class="item">
	                                          <div>供应商联系电话</div>
	                                          <div>内容内容</div>
	                                       </div>
	                                  </div>
	                            </div>
	                       </div>
	                       
	                       <div class="getInfoItem">
	                            <div class="getInfoItemTop">
	                                 <div class="controlOpen"></div>
	                                 <div class="info">价格信息</div>
	                                 <div class="time">最后更新于 2017-07-09  14：00</div>
	                                 <div class="update btn-c-r">更新</div>
	                            </div>
	                            <div class="getInfoItemContent">
	                                  <div class="contentItem">
	                                       <div class="item">
	                                          <div>预估价格</div>
	                                          <div>内容内容</div>
	                                       </div>
	                                       <div class="item">
	                                          <div>客户项目预算</div>
	                                          <div>内容内容</div>
	                                       </div>
	                                       <div class="item">
	                                          <div>策划供应商预算</div>
	                                          <div>内容内容</div>
	                                       </div>
	                                       <div class="item">
	                                          <div>制作供应商预算</div>
	                                          <div>内容内容</div>
	                                       </div>
	                                  </div>
	                                  <div class="contentItem">
	                                       <div class="item">
	                                          <div>制作供应商结算</div>
	                                          <div>内容内容</div>
	                                       </div>
	                                  </div>
	                            </div>
	                       </div>
	                       
	                       <div class="getInfoItem lastItem">
	                            <div class="getInfoItemTop ">
	                                 <div class="controlOpen"></div>
	                                 <div class="info">其它信息</div>
	                                 <div class="time">最后更新于 2017-07-09  14：00</div>
	                                 <div class="update btn-c-r">更新</div>
	                            </div>
	                            <div class="getInfoItemContent">
	                                  <div class="contentItem">
	                                       <div class="item">
	                                          <div>客户约定付款时间</div>
	                                          <div>内容内容</div>
	                                       </div>
	                                       <div class="item">
	                                          <div>客户项目交付时间</div>
	                                          <div>内容内容</div>
	                                       </div>
	                                  </div>
	                                   <div class="longItem">
	                                      <div>策划供应商启动函备注信息</div>
	                                      <div>内容内容</div>
	                                  </div>
	                                   <div class="longItem">
	                                      <div>制作供应商启动函备注信息</div>
	                                      <div>内容内容</div>
	                                  </div>
	                                   <div class="contentItem">
	                                       <div class="item">
	                                          <div>发票信息</div>
	                                          <div class="under" id="openBill">内容内容</div>
	                                       </div>
	                                       <div class="item">
	                                          <div>客户转账信息</div>
	                                          <div class="under" id="showCusPrice">内容内容</div>
	                                       </div>
	                                        <div class="item">
	                                          <div>供应商转账信息</div>
	                                          <div class="under" id="showProPrice">内容内容</div>
	                                       </div>
	                                  </div>
	                            </div>
	                       </div>
	                                              
	                   <div class="projectTitle">项目文件
	                        <div class="conMod btn-c-r">版本管理</div>
	                        <div class="upFile btn-c-r">上传</div>
	                   </div>
	                       <div class="projectFilm">
	                             <div class="filmItem">
	                                    <img class="filmImg" src="/resources/images/flow/ppt.png">
	                                    <div class="filmName">文件名</div>
	                                    <div class="fileType"><div>策划方案</div></div>
	                                    <div class="fileTypeName"><div>测试文件</div></div>
	                                    <div class="time"><div>上传于：2017-07-09  14：00</div></div>
	                                    <div class="icon">
	                                          <div class="look"></div>
	                                          <div class="share"></div>
	                                          <div class="download"></div>
	                                    </div>
	                             </div>
	                             <div class="filmItem">
	                                    <img class="filmImg" src="/resources/images/flow/ppt.png">
	                                    <div class="filmName">文件名</div>
	                                    <div class="fileType"><div>策划方案</div></div>
	                                    <div class="fileTypeName"><div>测试文件</div></div>
	                                    <div class="time"><div>上传于：2017-07-09  14：00</div></div>
	                                    <div class="icon">
	                                          <div class="look"></div>
	                                          <div class="share"></div>
	                                          <div class="download"></div>
	                                    </div>
	                             </div>
	                             <div class="filmItem">
	                                    <img class="filmImg" src="/resources/images/flow/ppt.png">
	                                    <div class="filmName">文件名</div>
	                                    <div class="fileType"><div>策划方案</div></div>
	                                    <div class="fileTypeName"><div>测试文件</div></div>
	                                    <div class="time"><div>上传于：2017-07-09  14：00</div></div>
	                                    <div class="icon">
	                                          <div class="look"></div>
	                                          <div class="share"></div>
	                                          <div class="download"></div>
	                                    </div>
	                             </div>
	                             <div class="getMore">
	                                  <div>展开更多</div>
	                                  <div></div>
	                             </div>
	                       </div>
	                       
	                       <div class="projectTitle">留言评论区</div>
	                       <div class="toSetArea">
	                             <textarea></textarea>
	                             <div class="upInfo">
	                                 <div class="btn-c-r">提交</div>
	                             </div>
	                       </div>
	                       <div class="setAreaDiv">
	                            <div class="areaItem">
	                                 <div class="infoItem">
	                                     <img src="/resources/images/flow/def.png">
	                                     <div class="info">策划人：完成 上传策划方案 任务</div>
	                                     <div class="time">
	                                        <span>发布时间：22017-07-09  14：00</span>
	                                        <div class="openTalk"></div>
	                                     </div>
	                                 </div>
	                                 <div class="infoContent">
	                                       <div>负责人:<span>策划方案需要调整一下</span></div>
	                                       <div>负责人:<span>策划方案需要调整一下</span></div>
	                                       <input>
	                                 </div>
	                                 <div class="upInfo">
	                                            <div class="btn-c-r">提交</div>
	                                 </div>
	                            </div>
	                             <div class="areaItem">
	                                 <div class="infoItem">
	                                     <img src="/resources/images/flow/def.png">
	                                     <div class="info">策划人：完成 上传策划方案 任务</div>
	                                     <div class="time">
	                                        <span>发布时间：22017-07-09  14：00</span>
	                                        <div class="openTalk"></div>
	                                     </div>
	                                 </div>
	                                 <div class="infoContent">
	                                       <div>负责人:<span>策划方案需要调整一下</span></div>
	                                       <div>负责人:<span>策划方案需要调整一下</span></div>
	                                       <input>
	                                 </div>
	                                 <div class="upInfo">
	                                            <div class="btn-c-r">提交</div>
	                                 </div>
	                            </div>
	                             <div class="getMore">
	                                  <div>展开更多</div>
	                                  <div></div>
	                             </div>
	                       </div>
	                </div>
	           </div>              
	    </div>
	<!-- video-->
</body>

</html>
