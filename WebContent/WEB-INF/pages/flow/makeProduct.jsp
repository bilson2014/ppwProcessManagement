<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- import CSS --%>
<spring:url value="/resources/css/flow/makeProduct.css" var="makeProductCss"/>
<%-- import JS --%>
<spring:url value="/resources/lib/jquery/jquery-2.0.3.min.js" var="jqueryJs"/>
<spring:url value="/resources/lib/jqueryui/jquery-ui.min.js" var="jqueryuiJs"/>
<spring:url value="/resources/js/common.js" var="commonJs"/>
<spring:url value="/resources/js/flow/makeProduct.js" var="makeProductJs"/>
<spring:url value="/resources/js/juicer.js" var="juicerJs" />
<spring:url value="/resources/lib/jquery.json/jquery.json-2.4.min.js" var="jsonJs"/>
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
<title>分镜工具</title>

<script type="text/javascript" src="${jqueryJs}"></script>
<script type="text/javascript" src="${commonJs}"></script>
<script type="text/javascript" src="${juicerJs}"></script>
<script type="text/javascript" src="${jsonJs}"></script>
<script type="text/javascript" src="${makeProductJs}"></script>

<link rel="stylesheet" href="${makeProductCss}">
<link rel="shortcut icon" href="${imgPath }/favicon.ico" >

<!--[if lt IE 9]>
        <script>window.html5 || document.write('<script src="html5shivJs"><\/script>')</script>
    <![endif]-->

</head>

<body style="overflow:hidden">


<input type="hidden" id="projectName" name="projectName">


	
				<div class="page">
				
				        <img class="noImg" src="/resources/images/flow/noInfo.png">
				
				       <div class="toolsHead">
				             <div id="projectNameTitle" style="font-size: 1.4rem;color:#333;">未命名项目</div>
						     <div class="toolTitle">制片工具</div>
				             <div class="openTool" style="right:247px" id="openProejct">打开项目制片表</div>
				             <div class="openTool addP" style="right:140px" id="openAdd"><div></div>添加资源</div>
				             <div class="openTool delP" style="right:20px">清空制片表</div>
				       </div>
				       
				       <div class="setProductInfo" id="setProduct">
				              <!--   <div class="BigItem">
				                       <div class="titleB">标题分类</div>
				                       <div class="MidItem MidActive">
					                       <div class="titleM ">标题分类小<div></div></div>
					                       <div class="itemContent">
					                             <div class="itemContentFive itemCommon">
					                                  <img src="https://file1.apaipian.com:8000/group1/M00/00/26/Cgqp51m40FGATWfEAAAKH4Shw48846.jpg">
					                                  <img class="checkRed" src="/resources/images/flow/checkRed.png">
					                                  <div class="info">
					                                          <div class="who">我 / 员工</div>
					                                          <div class="price">￥600</div>
					                                  </div>
					                                  <div class="showTool">
					                                      <div class="toolDiv">
					                                      		<div class="moveItem">移除</div><div>查看详情</div>
					                                      </div>
					                                  </div>
					                             </div>
					                              <div class="itemContentFive itemCommon">
					                                  <img src="https://file1.apaipian.com:8000/group1/M00/00/26/Cgqp51m40FGATWfEAAAKH4Shw48846.jpg">
					                                  <div class="info">
					                                          <div class="who">我 / 员工</div>
					                                          <div class="price">￥600</div>
					                                  </div>
					                                  <div class="showTool">
					                                      <div class="toolDiv">
					                                      		<div class="moveItem">移除</div><div>查看详情</div>
					                                      </div>
					                                  </div>
					                             </div>
					                              <div class="itemContentFive itemCommon">
					                                  <img src="https://file1.apaipian.com:8000/group1/M00/00/26/Cgqp51m40FGATWfEAAAKH4Shw48846.jpg">
					                                  <div class="info">
					                                          <div class="who">我 / 员工</div>
					                                          <div class="price">￥600</div>
					                                  </div>
					                                  <div class="showTool">
					                                      <div class="toolDiv">
					                                      		<div class="moveItem">移除</div><div>查看详情</div>
					                                      </div>
					                                  </div>
					                             </div>
					                             <div class="itemContentFive itemCommon"></div>
					                             <div class="itemContentFive itemCommon"></div>
					                             <div class="itemContentFive itemCommon"></div>
					                             <div class="itemContentFive itemCommon"></div>
					                             <div class="itemContentFive itemCommon"></div>
					                       </div> 
				                       </div>
				                       <div class="MidItem">
					                       <div class="titleM">标题分类小<div></div></div>
					                       <div class="itemContent">
					                           <div class="itemContentFour itemCommon">
					                                  <img src="https://file1.apaipian.com:8000/group1/M00/00/26/Cgqp51m40FGATWfEAAAKH4Shw48846.jpg">
					                                  <div class="info">
					                                          <div class="who">我 / 员工</div>
					                                          <div class="price">￥600</div>
					                                  </div>
					                                  <div class="showTool">
					                                      <div class="toolDiv">
					                                      		<div>移除</div><div>查看详情</div>
					                                      </div>
					                                  </div>
					                             </div>
					                             <div class="itemContentFour itemCommon">
					                                  <img src="https://file1.apaipian.com:8000/group1/M00/00/26/Cgqp51m40FGATWfEAAAKH4Shw48846.jpg">
					                                  <div class="info">
					                                          <div class="who">我 / 员工</div>
					                                          <div class="price">￥600</div>
					                                  </div>
					                                  <div class="showTool">
					                                      <div class="toolDiv">
					                                      		<div>移除</div><div>查看详情</div>
					                                      </div>
					                                  </div>
					                             </div>
					                       </div>
				                       </div>
				                       <div class="MidItem">
					                       <div class="titleM">标题分类小<div></div></div>
					                       <div class="itemContent"></div>
				                       </div>
				                </div> -->
				       </div>
				       
				       <div class="toolBtn hide btn-c-r" id="saveTo" style="margin-left: 40px;">保存至项目</div>
				       <div class="toolBtn hide btn-c-r" id="exportTo">导出</div>
				       
				</div>
    	<!-- 弹窗-->
    <div class="tooltip-success-show" style="z-index:999"></div>
	<div class="cusModel" id="loadProductModel" style="">
	     <div class="modelCard">
	            <div class="cardTop">
	                   <div class="title">制片工具</div>
	                   <div class="closeModel"></div>
	            </div>
	            <div class="modelBanner">
	                <div class="tap" id="" style="width:100%">您正在参与进行中的项目</div>
	            </div>
	            <div class="modelProductContent"></div>
	            <div class="modelControl">
	                 <div class="btn-c-g" id="cancleLoadProduct">取消</div>
	                 <div class="btn-c-r" id="CheckloadProduct">加载</div>
	            </div>     
	     </div>
	</div>
	
	 <div class="cusModel" id="sameProject">
           <div class="successModel">
               <div class="closeBtn"></div>
			   <div class="oSContent">
			        <div class="tdDes" style="padding-top:80px;">存在制片工具是否覆盖?</div>
			        <div class="sureBtn" style="padding-top:40px;">
			           <div class="btn-c-r" id="toSame">确定</div>
			           <div class="btn-c-g" id="toCSame">取消</div>
			        </div>
			   </div>
           </div>
      </div>
	
	 <div class="cusModel" id="checkSureModel">
           <div class="successModel">
               <div class="closeBtn"></div>
			   <div class="oSContent">
			        <div class="tdDes" style="padding-top:80px;">确认清空列表吗?</div>
			        <div class="sureBtn" style="padding-top:40px;">
			           <div class="btn-c-r" id="tModel">确定</div>
			           <div class="btn-c-g" id="cModel">取消</div>
			        </div>
			   </div>
           </div>
      </div>
      
      <div class="cusModel" id="info1">
           <div class="showInfoModel">
               <div class="infoTitle"><div class="title">导演信息</div><div class="closeBtn"></div></div>
               <div class="showInfoContent">
                    <div class="contentItem">
                         <div class="itemTitle">姓名</div>
                         <div class="itemInfo" data-name="name"></div>
                    </div>
                     <div class="contentItem">
                         <div class="itemTitle changName">参考报价 (元)</div>
                         <div class="itemInfo" data-name="price"></div>
                    </div>
                     <div class="contentItem">
                         <div class="itemTitle">擅长领域</div>
                         <div class="itemInfo" data-name="specialty"></div>
                    </div>
                     <div class="contentItem">
                         <div class="itemTitle">职业</div>
                         <div class="itemInfo" data-name="quoTypeName"></div>
                    </div>
                    <div class="contentItem">
                         <div class="itemTitle">城市</div>
                         <div class="itemInfo" data-name="city"></div>
                    </div>
                     <div class="contentItem">
                         <div class="itemTitle">推荐人</div>
                         <div class="itemInfo" data-name="referrerName"></div>
                    </div>
                                     
                     <img class="setInfoImg" src="https://file1.apaipian.com:8000/group1/M00/00/26/Cgqp51m40FGATWfEAAAKH4Shw48846.jpg">
                     <div class="title titleMargin" style="margin-top: 45px;">备注信息</div>
                     <div class="noteInfo" data-name="remark"></div>
                     
               </div>
           </div>
      </div>
      
       <div class="cusModel" id="info2">
           <div class="showInfoModel">
               <div class="infoTitle"><div class="title">演员信息</div><div class="closeBtn"></div></div>
               <div class="showInfoContent">
                    <div class="contentItem">
                         <div class="itemTitle">姓名</div>
                         <div class="itemInfo" data-name="name"></div>
                    </div>
                     <div class="contentItem">
                         <div class="itemTitle">性别</div>
                         <div class="itemInfo" data-name="sex"></div>
                    </div>
                      <div class="contentItem">
                         <div class="itemTitle">年龄</div>
                         <div class="itemInfo" data-name="age"></div>
                    </div>
                      <div class="contentItem">
                         <div class="itemTitle">种族</div>
                         <div class="itemInfo" data-name="zone"></div>
                    </div>
                     <div class="contentItem">
                         <div class="itemTitle changName">参考报价 (元)</div>
                         <div class="itemInfo" data-name="price"></div>
                    </div>
                     <div class="contentItem">
                         <div class="itemTitle">职业</div>
                         <div class="itemInfo" data-name="quoTypeName"></div>
                    </div>
                    <div class="contentItem">
                         <div class="itemTitle">城市</div>
                         <div class="itemInfo" data-name="city"></div>
                    </div>
                     <div class="contentItem">
                         <div class="itemTitle">推荐人</div>
                         <div class="itemInfo" data-name="referrerName"></div>
                    </div>
                    
                    <div class="setShowImg">
                           <img class="setShowInfoImg" src="https://file1.apaipian.com:8000/group1/M00/00/26/Cgqp51m40FGATWfEAAAKH4Shw48846.jpg">
                           <img class="setShowInfoImg" src="https://file1.apaipian.com:8000/group1/M00/00/26/Cgqp51m40FGATWfEAAAKH4Shw48846.jpg">
                           <img class="setShowInfoImg" src="https://file1.apaipian.com:8000/group1/M00/00/26/Cgqp51m40FGATWfEAAAKH4Shw48846.jpg">
                           <img class="setShowInfoImg" src="https://file1.apaipian.com:8000/group1/M00/00/26/Cgqp51m40FGATWfEAAAKH4Shw48846.jpg">
                           <img class="setShowInfoImg" src="https://file1.apaipian.com:8000/group1/M00/00/26/Cgqp51m40FGATWfEAAAKH4Shw48846.jpg">
                    </div>
                                     
                    <img class="setInfoImg" src="https://file1.apaipian.com:8000/group1/M00/00/26/Cgqp51m40FGATWfEAAAKH4Shw48846.jpg">
                     <div class="title titleMargin">备注信息</div>
                     <div class="noteInfo" data-name="remark">
                     </div>
               </div>
           </div>
      </div>
      
      
      <div class="cusModel" id="info3">
           <div class="showInfoModel">
               <div class="infoTitle"><div class="title">设备信息</div><div class="closeBtn"></div></div>
               <div class="showInfoContent type2">
                    <div class="contentItem">
                         <div class="itemTitle">名称</div>
                         <div class="itemInfo" data-name="quoTypeName"></div>
                    </div>
                     <div class="contentItem">
                         <div class="itemTitle">设备类型</div>
                         <div class="itemInfo" data-name="typeName"></div>
                    </div>
                      <div class="contentItem">
                         <div class="itemTitle changName">参考报价 (元)</div>
                         <div class="itemInfo" data-name="price"></div>
                    </div>
                      <div class="contentItem">
                         <div class="itemTitle">库存(套)</div>
                         <div class="itemInfo" data-name="quantity"></div>
                    </div>
                     <div class="contentItem">
                         <div class="itemTitle">城市</div>
                         <div class="itemInfo" data-name="city"></div>
                    </div>
                     <div class="contentItem">
                         <div class="itemTitle">推荐人</div>
                         <div class="itemInfo" data-name="referrerName"></div>
                    </div>
                                     
                    <img class="setInfoImg" src="https://file1.apaipian.com:8000/group1/M00/00/26/Cgqp51m40FGATWfEAAAKH4Shw48846.jpg">
                     <div class="title titleMargin">备注信息</div>
                     <div class="noteInfo" data-name="remark">
                     </div>
               </div>
           </div>
      </div>
      
       <div class="cusModel" id="info4">
           <div class="showInfoModel">
               <div class="infoTitle"><div class="title">场地信息</div><div class="closeBtn"></div></div>
               <div class="showInfoContent type2">
                    <div class="contentItem">
                         <div class="itemTitle">姓名</div>
                         <div class="itemInfo" data-name="name"></div>
                    </div>
                    <div class="contentItem">
                         <div class="itemTitle">场地类型</div>
                         <div class="itemInfo" data-name="type"></div>
                    </div>
                    <div class="contentItem">
                         <div class="itemTitle">面积</div>
                         <div class="itemInfo" data-name="area"></div>
                    </div>
                    <div class="contentItem">
                         <div class="itemTitle changName">参考报价 (元)</div>
                         <div class="itemInfo" data-name="price"></div>
                    </div>
                    <div class="contentItem">
                         <div class="itemTitle">城市</div>
                         <div class="itemInfo" data-name="city"></div>
                    </div>
                     <div class="contentItem">
                         <div class="itemTitle">推荐人</div>
                         <div class="itemInfo" data-name="referrerName"></div>
                    </div>
                    
                    <div class="setShowImg">
                           <img class="setShowInfoImg" src="https://file1.apaipian.com:8000/group1/M00/00/26/Cgqp51m40FGATWfEAAAKH4Shw48846.jpg">
                           <img class="setShowInfoImg" src="https://file1.apaipian.com:8000/group1/M00/00/26/Cgqp51m40FGATWfEAAAKH4Shw48846.jpg">
                           <img class="setShowInfoImg" src="https://file1.apaipian.com:8000/group1/M00/00/26/Cgqp51m40FGATWfEAAAKH4Shw48846.jpg">
                           <img class="setShowInfoImg" src="https://file1.apaipian.com:8000/group1/M00/00/26/Cgqp51m40FGATWfEAAAKH4Shw48846.jpg">
                           <img class="setShowInfoImg" src="https://file1.apaipian.com:8000/group1/M00/00/26/Cgqp51m40FGATWfEAAAKH4Shw48846.jpg">
                    </div>
                                     
                    <img class="setInfoImg" src="https://file1.apaipian.com:8000/group1/M00/00/26/Cgqp51m40FGATWfEAAAKH4Shw48846.jpg">
                     <div class="title titleMargin">备注信息</div>
                     <div class="noteInfo" data-name="remark">
                     </div>
               </div>
           </div>
      </div>
       <div class="cusModel" id="info5">
           <div class="showInfoModel">
               <div class="infoTitle"><div class="title">摄影师信息</div><div class="closeBtn"></div></div>
               <div class="showInfoContent">
                    <div class="contentItem">
                         <div class="itemTitle">姓名</div>
                         <div class="itemInfo" data-name="name"></div>
                    </div>
                     <div class="contentItem">
                         <div class="itemTitle changName">参考报价 (元)</div>
                         <div class="itemInfo" data-name="price"></div>
                    </div>
                     <div class="contentItem">
                         <div class="itemTitle">特殊技能</div>
                         <div class="itemInfo" data-name="specialSkill"></div>
                    </div>
                     <div class="contentItem">
                         <div class="itemTitle">职业</div>
                         <div class="itemInfo" data-name="quoTypeName"></div>
                    </div>
                    <div class="contentItem">
                         <div class="itemTitle">城市</div>
                         <div class="itemInfo" data-name="city"></div>
                    </div>
                     <div class="contentItem">
                         <div class="itemTitle">推荐人</div>
                         <div class="itemInfo" data-name="referrerName"></div>
                    </div>
                                     
                    <img class="setInfoImg" src="https://file1.apaipian.com:8000/group1/M00/00/26/Cgqp51m40FGATWfEAAAKH4Shw48846.jpg">
                     <div class="title titleMargin">备注信息</div>
                     <div class="noteInfo" data-name="remark">
                     </div>
               </div>
           </div>
      </div>
       <div class="cusModel" id="info6">
           <div class="showInfoModel">
               <div class="infoTitle"><div class="title">人员信息</div><div class="closeBtn"></div></div>
               <div class="showInfoContent">
                    <div class="contentItem">
                         <div class="itemTitle">姓名</div>
                         <div class="itemInfo" data-name="name"></div>
                    </div>
                     <div class="contentItem">
                         <div class="itemTitle changName">参考报价 (元)</div>
                         <div class="itemInfo" data-name="price"></div>
                    </div>
                     <div class="contentItem">
                         <div class="itemTitle">职业</div>
                         <div class="itemInfo" data-name="quoTypeName"></div>
                    </div>
                    <div class="contentItem">
                         <div class="itemTitle">城市</div>
                         <div class="itemInfo" data-name="city"></div>
                    </div>
                     <div class="contentItem">
                         <div class="itemTitle">推荐人</div>
                         <div class="itemInfo" data-name="referrerName"></div>
                    </div>
                                     
                    <img class="setInfoImg" src="https://file1.apaipian.com:8000/group1/M00/00/26/Cgqp51m40FGATWfEAAAKH4Shw48846.jpg">
                     <div class="title titleMargin" style="margin-top: 75px;">备注信息</div>
                     <div class="noteInfo" data-name="remark">
                     </div>
               </div>
           </div>
      </div>
      <div class="cusModel" id="info7">
           <div class="showInfoModel">
               <div class="infoTitle"><div class="title">服装道具信息</div><div class="closeBtn"></div></div>
               <div class="showInfoContent type2">
                    <div class="contentItem">
                         <div class="itemTitle">姓名</div>
                         <div class="itemInfo" data-name="name"></div>
                    </div>
                    <div class="contentItem">
                         <div class="itemTitle">类别</div>
                         <div class="itemInfo" data-name="type"></div>
                    </div>
                    <div class="contentItem">
                         <div class="itemTitle">授权方式</div>
                         <div class="itemInfo" data-name="accredit"></div>
                    </div>
                    <div class="contentItem">
                         <div class="itemTitle">库存(套)</div>
                         <div class="itemInfo" data-name="stockNumber"></div>
                    </div>
                    <div class="contentItem">
                         <div class="itemTitle changName">参考报价 (元)</div>
                         <div class="itemInfo" data-name="price"></div>
                    </div>
                    <div class="contentItem">
                         <div class="itemTitle">城市</div>
                         <div class="itemInfo" data-name="city"></div>
                    </div>
                     <div class="contentItem">
                         <div class="itemTitle">推荐人</div>
                         <div class="itemInfo" data-name="referrerName"></div>
                    </div>
                    
                    <div class="setShowImg">
                           <img class="setShowInfoImg" src="https://file1.apaipian.com:8000/group1/M00/00/26/Cgqp51m40FGATWfEAAAKH4Shw48846.jpg">
                           <img class="setShowInfoImg" src="https://file1.apaipian.com:8000/group1/M00/00/26/Cgqp51m40FGATWfEAAAKH4Shw48846.jpg">
                           <img class="setShowInfoImg" src="https://file1.apaipian.com:8000/group1/M00/00/26/Cgqp51m40FGATWfEAAAKH4Shw48846.jpg">
                           <img class="setShowInfoImg" src="https://file1.apaipian.com:8000/group1/M00/00/26/Cgqp51m40FGATWfEAAAKH4Shw48846.jpg">
                           <img class="setShowInfoImg" src="https://file1.apaipian.com:8000/group1/M00/00/26/Cgqp51m40FGATWfEAAAKH4Shw48846.jpg">
                    </div>
                                     
                    <img class="setInfoImg" src="https://file1.apaipian.com:8000/group1/M00/00/26/Cgqp51m40FGATWfEAAAKH4Shw48846.jpg">
                     <div class="title titleMargin">备注信息</div>
                     <div class="noteInfo" data-name="remark">
                     </div>
               </div>
           </div>
      </div>
      
      <div class="addModel">
            <div class="toolsHead">
				             <div id="projectName" style="font-size: 1.4rem;font-weight: 500;color:#666;"></div>
						     <div class="toolTitle">选择制片资源</div>
				             <div class="openTool" id="reToMake" style="right:40px;padding-left: 20px;padding-right: 20px;">制片表</div>
		    </div>
		    <div class="addContent">
		         <div class="search">搜索</div>
		         <div class="setOption">
		               <div class="optionItem">
		                     <div class="title">类别</div>
			              	 <div class="orderSelect" id="isOther">
					                <div class="imgType" id="productType"></div>
					                <img src="/resources/images/flow/selectS.png">
					                <ul class="oSelect"  style="display: none;">
						                    <!-- <li>
						                     <div class="multSelect">                                     
						                             <div class="multTitle">                                     
						                                  <img class="quoIcon" src="/resources/images/index/quoIcon.png">               
						                                  <div class="title" data-id="">创作团队</div>                       
						                             </div>                                                  
						                             <div class="productList" id="productList">            
							                            <div data-id="director" >导演组</div> 
							                            <div data-id="actor" >演员组</div> 
						                             </div>   
						                       </div>                                                  
						                    </li> -->
						                    <li class="hoverColor"><div class="findTYpe" data-id='director'>导演</div></li>                                              
						                    <li class="hoverColor"><div class="findTYpe" data-id='actor'>演员</div></li>
						                    
						                    
						                    <li class="hoverColor"><div class="findTYpe" data-id='cameraman'>摄影师</div></li> 
						                    <li class="hoverColor"><div class="findTYpe" data-id='lighter'>灯光师</div></li> 
						                    <li class="hoverColor"><div class="findTYpe" data-id='editor'>剪辑师</div></li> 
						                    <li class="hoverColor"><div class="findTYpe" data-id='packer'>包装师</div></li> 
						                    <li class="hoverColor"><div class="findTYpe" data-id='colorist'>调色师</div></li>  
						                    <li class="hoverColor"><div class="findTYpe" data-id='propMaster'>道具美术师</div></li> 
						                   <!--  <li class="hoverColor"><div class="findTYpe" data-id='artist'>美术师</div></li>  -->
						                    <li class="hoverColor"><div class="findTYpe" data-id='costumer'>服装师</div></li> 
						                    <li class="hoverColor"><div class="findTYpe" data-id='dresser'>化妆师</div></li> 
						                    <li class="hoverColor"><div class="findTYpe" data-id='mixer'>录音师</div></li> 
						                    
						                    <li class="hoverColor"><div class="findTYpe" data-id='device'>设备</div></li>                                              
						                    <li class="hoverColor"><div class="findTYpe" data-id='studio'>场地</div></li> 
						                    <li class="hoverColor"><div class="findTYpe" data-id='clothing'>服装</div></li> 
						                    <li class="hoverColor"><div class="findTYpe" data-id='props'>道具</div></li> 
					                </ul>    
						     </div>
						</div>
						 <div class="optionItem" id="searchName">
		                     <div class="title">名称</div>
		                      <input class="nomalInput" id="nomalName" style="border: 1px solid #d1d1d1;">
						</div>  
						
						 <div class="optionItem">
		                     <div class="title">城市</div>
			              	 <div class="orderSelect" id="isOther">
					                <div class="imgType" id="city">请选择城市</div>
					                <img src="/resources/images/flow/selectS.png">
					                <ul class="oSelect" id='cityUl' style="display: none;"></ul>    
						     </div>
						</div>
						 <div class="optionItem">
		                     <div class="title">参考报价</div>
			              	 <input class="numInput" id="beginPrice" onkeyup="value=value.replace(/[^\d]/g,'')">
			              	 <div class="fu">~</div>
			              	 <input class="numInput" id="endPrice" onkeyup="value=value.replace(/[^\d]/g,'')">
						</div>   
						
						<div class="show1 showUnmInfo" style="display:none">
							<div class="optionItem">
			                     <div class="title">场地类型</div>
			                      <div class="orderSelect orderMultSelect" style="width: 200px;">
						                <div class="imgType" id="studioType">请选择场地类型</div>
						                <img src="/resources/images/flow/selectS.png"> 
						                <ul class="setMultSelect" id="studioUl" style="display: none;"></ul>    
							     </div>					              	
							</div>
						</div>
						
						<div class="show2 showUnmInfo" style="display:none">
							<div class="optionItem">
			                     <div class="title">设备类型</div>
							     <div class="orderSelect orderMultSelect">
						                <div class="imgType" id="deviceType">请选择设备类型</div>
						                <img src="/resources/images/flow/selectS.png"> 
						                <ul class="setMultSelect deviceTypeUl" id="deviceTypeUl" style="display: none;"></ul>    
							     </div>	
							</div>
							<div class="optionItem" id="searchSelectName">
		                     <div class="title">名称</div>
			              	 <div class="orderSelect" id="isOther" style="width:200px;">
					                <div class="imgType" id="speName"></div>
					                <img src="/resources/images/flow/selectS.png">
					                <ul class="oSelect" id="speNameUl" style="display: none;"></ul>    
						     </div>
						</div> 
						</div>
						
						<div class="show3 showUnmInfo" style="display:none">
							<div class="optionItem" >
			                     <div class="title">职业</div>
			                      <div class="orderSelect orderMultSelect" style="width:180px">
						                <div class="imgType" id="actorLevel">请选择职业</div>
						                <img src="/resources/images/flow/selectS.png"> 
						                <ul class="setMultSelect" id="actorLevelUl" style="display: none;"></ul>    
							     </div>				              	 
							</div>
							<div class="optionItem">
			                     <div class="title">性别</div>
				              	 <div class="orderSelect" id="isOther">
						                <div class="imgType" id="sex">请选择性别</div>
						                <img src="/resources/images/flow/selectS.png">
						                <ul class="oSelect" id="sexUl" style="display: none;">
						                    <li data-id='0'>男</li>
						                    <li data-id='1'>女</li>
						                </ul>    
							     </div>
							</div>
							
						    <div class="optionItem">
				                     <div class="title">年龄</div>
					              	 <input class="numInput" id="beginAge" onkeyup="value=value.replace(/[^\d]/g,'')">
					              	 <div class="fu">~</div>
					              	 <input class="numInput" id="endAge" onkeyup="value=value.replace(/[^\d]/g,'')">
							</div> 
								
							<div class="optionItem">
			                     <div class="title">种族</div>
				              	 <div class="orderSelect" id="isOther">
						                <div class="imgType" id="zone">请选择种族</div>
						                <img src="/resources/images/flow/selectS.png">
						                <ul class="oSelect" id="zoneUl" style="display: none;"></ul>    
							     </div>
							</div>
							
						</div>
						
						<div class="show4 showUnmInfo" style="display:none">
							<div class="optionItem">
			                     <div class="title">职业</div>
				              	 <div class="orderSelect orderMultSelect" style="width:180px">
						                <div class="imgType" id="directorLevel">请选择职业</div>
						                <img src="/resources/images/flow/selectS.png"> 
						                <ul class="setMultSelect" id="directorLevelUl" style="display: none;"></ul>    
							     </div>
							</div>
							<div class="optionItem">
			                     <div class="title">领域</div>
				              	 <div class="orderSelect" id="isOther">
						                <div class="imgType" id="directorZone">请选择领域</div>
						                <img src="/resources/images/flow/selectS.png">
						                <ul class="oSelect" id="directorZoneUl" style="display: none;"></ul>    
							     </div>
							</div>
						</div>
						<div class="show5 showUnmInfo" style="display:none">
							<div class="optionItem">
			                     <div class="title">职业</div>
			                      <div class="orderSelect orderMultSelect" style="width:180px">
						                <div class="imgType" id="cameramanLevel">请选择职业</div>
						                <img src="/resources/images/flow/selectS.png"> 
						                <ul class="setMultSelect" id="cameramanLevelUl" style="display: none;"></ul>    
							     </div>				              	 
							</div>
							<div class="optionItem">
			                     <div class="title">特殊技能</div>
				              	 <div class="orderSelect" id="isOther">
						                <div class="imgType" id="cameramanSkill">请选择特殊技能</div>
						                <img src="/resources/images/flow/selectS.png">
						                <ul class="oSelect" id="cameramanSkillUl" style="display: none;"></ul>    
							     </div>
							</div>
						</div>
						<div class="show6 showUnmInfo" style="display:none">
							<div class="optionItem">
			                     <div class="title">职业</div>
			                      <div class="orderSelect orderMultSelect" style="width:180px">
						                <div class="imgType" id="lighterLevel">请选择职业</div>
						                <img src="/resources/images/flow/selectS.png"> 
						                <ul class="setMultSelect" id="lighterLevelUl" style="display: none;"></ul>    
							     </div>				              	 
							</div>
						</div>
						<div class="show7 showUnmInfo" style="display:none">
							<div class="optionItem">
			                     <div class="title">职业</div>
			                      <div class="orderSelect orderMultSelect" style="width:180px">
						                <div class="imgType" id="editorLevel">请选择职业</div>
						                <img src="/resources/images/flow/selectS.png"> 
						                <ul class="setMultSelect" id="editorLevelUl" style="display: none;"></ul>    
							     </div>				              	 
							</div>
						</div>
						<div class="show8 showUnmInfo" style="display:none">
							<div class="optionItem">
			                     <div class="title">职业</div>
			                      <div class="orderSelect orderMultSelect" style="width:180px">
						                <div class="imgType" id="packerLevel">请选择职业</div>
						                <img src="/resources/images/flow/selectS.png"> 
						                <ul class="setMultSelect" id="packerLevelUl" style="display: none;"></ul>    
							     </div>				              	 
							</div>
						</div>
						<div class="show9 showUnmInfo" style="display:none">
							<div class="optionItem">
			                     <div class="title">职业</div>
			                      <div class="orderSelect orderMultSelect" style="width:180px">
						                <div class="imgType" id="coloristLevel">请选择职业</div>
						                <img src="/resources/images/flow/selectS.png"> 
						                <ul class="setMultSelect" id="coloristLevelUl" style="display: none;"></ul>    
							     </div>				              	 
							</div>
						</div>
						<div class="show10 showUnmInfo" style="display:none">
							<div class="optionItem">
			                     <div class="title">职业</div>
			                      <div class="orderSelect orderMultSelect" style="width:180px">
						                <div class="imgType" id="propMasterLevel">请选择职业</div>
						                <img src="/resources/images/flow/selectS.png"> 
						                <ul class="setMultSelect" id="propMasterLevelUl" style="display: none;"></ul>    
							     </div>				              	 
							</div>
						</div>
						<div class="show11 showUnmInfo" style="display:none">
							<div class="optionItem">
			                     <div class="title">职业</div>
			                      <div class="orderSelect orderMultSelect" style="width:180px">
						                <div class="imgType" id="artistLevel">请选择职业</div>
						                <img src="/resources/images/flow/selectS.png"> 
						                <ul class="setMultSelect" id="artistLevelUl" style="display: none;"></ul>    
							     </div>				              	 
							</div>
						</div>
						<div class="show13 showUnmInfo" style="display:none">
							<div class="optionItem">
			                     <div class="title">职业</div>
			                      <div class="orderSelect orderMultSelect" style="width:180px">
						                <div class="imgType" id="dresserLevel">请选择职业</div>
						                <img src="/resources/images/flow/selectS.png"> 
						                <ul class="setMultSelect" id="dresserLevelUl" style="display: none;"></ul>    
							     </div>				              	 
							</div>
						</div>
						<div class="show14 showUnmInfo" style="display:none">
							<div class="optionItem">
			                     <div class="title">职业</div>
			                      <div class="orderSelect orderMultSelect" style="width:180px">
						                <div class="imgType" id="mixerLevel">请选择职业</div>
						                <img src="/resources/images/flow/selectS.png"> 
						                <ul class="setMultSelect" id="mixerLevelUl" style="display: none;"></ul>    
							     </div>				              	 
							</div>
						</div>					
						<div class="show15 showUnmInfo" style="display:none">
							<div class="optionItem">
			                     <div class="title">服装类型</div>
				              	 <div class="orderSelect" id="isOther">
						                <div class="imgType" id="clothingType">请选择服装类型</div>
						                <img src="/resources/images/flow/selectS.png">
						                <ul class="oSelect" id="clothingTypeUl" style="display: none;"></ul>    
							     </div>
							</div>
							<div class="optionItem">
			                     <div class="title">授权方式</div>
				              	 <div class="orderSelect" id="isOther">
						                <div class="imgType" id="clothingAccredit">请选择授权方式</div>
						                <img src="/resources/images/flow/selectS.png">
						                <ul class="oSelect" id="clothingAccreditUl" style="display: none;"></ul>    
							     </div>
							</div>
						</div>
						<div class="show16 showUnmInfo" style="display:none">
							<div class="optionItem">
			                     <div class="title">授权方式</div>
				              	 <div class="orderSelect" id="isOther">
						                <div class="imgType" id="propsAccredit">请选择授权方式</div>
						                <img src="/resources/images/flow/selectS.png">
						                <ul class="oSelect" id="propsAccreditUl" style="display: none;"></ul>    
							     </div>
							</div>
						</div>
		         </div>
		         
		         <div class="MidItem controlWidth"   style="border:none;margin-top:20px">
		            <div class="newItemContent" id="addSetProductInfo" style="display:block !important"></div>
		         </div>
   
		    </div>
      </div>
      
      
<div class="cusModel" id="showModelName" >
           <div class="modelCard" >
	           <div class="cardTop">
	                   <div class="title">填写个人项目报价</div>
	                   <div class="closeModel"></div>
	            </div>
	            <div class="modelName">
	                <div class="modelWarn">输入要修改的项目报价</div>
		            <div class="orderItem" id="tempNameError">
			                <div class="modelNameTitle">项目报价</div>
				            <div class="orderSelect oredrProduct tomid" >
				                 <input class="modelNameInput" id="getModelPrice" />
					        </div>
		           		 </div>
	                <!-- <div class="modelNameTitle">模板名称</div>
	                <input class="modelNameInput" id="modelName" /> -->
	            </div>
               <div class="btnMid">
			           <div class="btn-c-r closeWindow" id="saveModelPrice" style="position: relative;left: 80px;margin-bottom: 30px;">确定</div>
		       </div>
           </div>
</div>
      
      <form method="post" action="/production/export" id="toListForm" class="hide">
                       <input type="hidden" id="projectId" name="projectId">
                       <input type="hidden" id="id" name="id">
                       <input type="hidden" id="resources" name="resources">
      </form>
    	
    	
</body>
<script type="text/javascript" src="${makeProductJs}"></script>
</html>