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

<body>

<input type="hidden" id="id" name="id">
<input type="hidden" id="projectId" name="projectId">
<input type="hidden" id="projectName" name="projectName">


	
				<div class="page">
				
				        <img class="noImg" src="/resources/images/flow/noInfo.png">
				
				       <div class="toolsHead">
				             <div id="projectName" style="font-size: 1.4rem;font-weight: 500;color:#666;"></div>
						     <div class="toolTitle">导演工具</div>
				             <div class="openTool" style="right:191px" id="openProejct">打开项目</div>
				             <div class="openTool addP" style="right:98px" id="openAdd"><div></div>选资源</div>
				             <div class="openTool delP" style="right:20px">清空</div>
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
    <div class="tooltip-success-show"></div>
	<div class="cusModel" id="loadProductModel" style="">
	     <div class="modelCard">
	            <div class="cardTop">
	                   <div class="title">项目分镜</div>
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
			        <div class="tdDes" style="padding-top:80px;">存在镜头脚本是否覆盖?</div>
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
               <div class="infoTitle"><div class="closeBtn"></div></div>
               <div class="showInfoContent">
                    <div class="title">基本信息</div>
                    <div class="contentItem">
                         <div class="itemTitle">姓名</div>
                         <div class="itemInfo">导演</div>
                    </div>
                     <div class="contentItem">
                         <div class="itemTitle">报价</div>
                         <div class="itemInfo">导演</div>
                    </div>
                     <div class="contentItem">
                         <div class="itemTitle">擅长领域</div>
                         <div class="itemInfo">导演</div>
                    </div>
                     <div class="contentItem">
                         <div class="itemTitle">角色</div>
                         <div class="itemInfo">导演</div>
                    </div>
                    <div class="contentItem">
                         <div class="itemTitle">城市</div>
                         <div class="itemInfo">导演</div>
                    </div>
                                     
                    <img class="setInfoImg" src="https://file1.apaipian.com:8000/group1/M00/00/26/Cgqp51m40FGATWfEAAAKH4Shw48846.jpg">
                     <div class="title titleMargin">备注信息</div>
                     <div class="noteInfo">
                     </div>
               </div>
           </div>
      </div>
      
       <div class="cusModel" id="info2">
           <div class="showInfoModel">
               <div class="infoTitle"><div class="closeBtn"></div></div>
               <div class="showInfoContent">
                    <div class="title">基本信息</div>
                    <div class="contentItem">
                         <div class="itemTitle">姓名</div>
                         <div class="itemInfo">导演</div>
                    </div>
                     <div class="contentItem">
                         <div class="itemTitle">性别</div>
                         <div class="itemInfo">导演</div>
                    </div>
                      <div class="contentItem">
                         <div class="itemTitle">年龄</div>
                         <div class="itemInfo">导演</div>
                    </div>
                      <div class="contentItem">
                         <div class="itemTitle">种族</div>
                         <div class="itemInfo">导演</div>
                    </div>
                     <div class="contentItem">
                         <div class="itemTitle">报价</div>
                         <div class="itemInfo">导演</div>
                    </div>
                     <div class="contentItem">
                         <div class="itemTitle">等级</div>
                         <div class="itemInfo">导演</div>
                    </div>
                    <div class="contentItem">
                         <div class="itemTitle">城市</div>
                         <div class="itemInfo">导演</div>
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
                     <div class="noteInfo">
                     </div>
               </div>
           </div>
      </div>
      
      
      <div class="cusModel" id="info3">
           <div class="showInfoModel">
               <div class="infoTitle"><div class="closeBtn"></div></div>
               <div class="showInfoContent type2">
                    <div class="title">基本信息</div>
                    <div class="contentItem">
                         <div class="itemTitle">名称</div>
                         <div class="itemInfo">导演</div>
                    </div>
                     <div class="contentItem">
                         <div class="itemTitle">设备类型</div>
                         <div class="itemInfo">导演</div>
                    </div>
                      <div class="contentItem">
                         <div class="itemTitle">报价</div>
                         <div class="itemInfo">导演</div>
                    </div>
                      <div class="contentItem">
                         <div class="itemTitle">数量</div>
                         <div class="itemInfo">导演</div>
                    </div>
                     <div class="contentItem">
                         <div class="itemTitle">城市</div>
                         <div class="itemInfo">导演</div>
                    </div>
                                     
                    <img class="setInfoImg" src="https://file1.apaipian.com:8000/group1/M00/00/26/Cgqp51m40FGATWfEAAAKH4Shw48846.jpg">
                     <div class="title titleMargin">备注信息</div>
                     <div class="noteInfo">
                     </div>
               </div>
           </div>
      </div>
      
       <div class="cusModel" id="info4">
           <div class="showInfoModel">
               <div class="infoTitle"><div class="closeBtn"></div></div>
               <div class="showInfoContent type2">
                    <div class="title">基本信息</div>
                    <div class="contentItem">
                         <div class="itemTitle">姓名</div>
                         <div class="itemInfo">导演</div>
                    </div>
                     <div class="contentItem">
                         <div class="itemTitle">性别</div>
                         <div class="itemInfo">导演</div>
                    </div>
                      <div class="contentItem">
                         <div class="itemTitle">年龄</div>
                         <div class="itemInfo">导演</div>
                    </div>
                      <div class="contentItem">
                         <div class="itemTitle">种族</div>
                         <div class="itemInfo">导演</div>
                    </div>
                     <div class="contentItem">
                         <div class="itemTitle">报价</div>
                         <div class="itemInfo">导演</div>
                    </div>
                     <div class="contentItem">
                         <div class="itemTitle">等级</div>
                         <div class="itemInfo">导演</div>
                    </div>
                    <div class="contentItem">
                         <div class="itemTitle">城市</div>
                         <div class="itemInfo">导演</div>
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
                     <div class="noteInfo">
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
			              	 <div class="orderSelect orderMultSelect" id="isOther">
					                <div class="imgType" id="productType"></div>
					                <img src="/resources/images/flow/selectS.png">
					                <ul class="setMultSelect" id="orderCome" style="display: none;">
						                    <li>
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
						                    </li>
						                    <li class="hoverColor"><div class="findTYpe" data-id='device'>设备</div></li>                                              
						                    <li class="hoverColor"><div class="findTYpe" data-id='studio'>场地</div></li>  
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
					                <ul class="oSelect" id='cityUl' style="display: none;">
					                   <li data-id="0">全部</li>
					                   <li data-id="1">沟通阶段</li>
					                </ul>    
						     </div>
						</div>
						 <div class="optionItem">
		                     <div class="title">价格</div>
			              	 <input class="numInput" id="beginPrice" onkeyup="value=value.replace(/[^\d]/g,'')">
			              	 <div class="fu">~</div>
			              	 <input class="numInput" id="endPrice" onkeyup="value=value.replace(/[^\d]/g,'')">
						</div>   
						
						<div class="show1 showUnmInfo" style="display:none">
							<div class="optionItem">
			                     <div class="title">场地类型</div>
			                      <div class="orderSelect orderMultSelect">
						                <div class="imgType" id="directorLevel">请选择场地类型</div>
						                <img src="/resources/images/flow/selectS.png"> 
						                <ul class="setMultSelect" id="studioUl" style="display: none;"></ul>    
							     </div>					              	
							</div>
						</div>
						
						<div class="show2 showUnmInfo" style="display:none">
							<div class="optionItem">
			                     <div class="title">设备类型</div>
							     <div class="orderSelect orderMultSelect">
						                <div class="imgType" id="directorLevel">请选择设备类型</div>
						                <img src="/resources/images/flow/selectS.png"> 
						                <ul class="setMultSelect deviceTypeUl" id="deviceTypeUl" style="display: none;"></ul>    
							     </div>	
							</div>
							<div class="optionItem" id="searchSelectName">
		                     <div class="title">名称</div>
			              	 <div class="orderSelect" id="isOther">
					                <div class="imgType" id="speName"></div>
					                <img src="/resources/images/flow/selectS.png">
					                <ul class="oSelect" id="speNameUl" style="display: none;"></ul>    
						     </div>
						</div> 
						</div>
						
						<div class="show3 showUnmInfo" style="display:none">
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
							<div class="optionItem">
			                     <div class="title">等级</div>
			                      <div class="orderSelect orderMultSelect">
						                <div class="imgType" id="directorLevel">请选择等级</div>
						                <img src="/resources/images/flow/selectS.png"> 
						                <ul class="setMultSelect" id="actorLevelUl" style="display: none;"></ul>    
							     </div>				              	 
							</div>
						</div>
						
						<div class="show4 showUnmInfo" style="display:none">
							<div class="optionItem">
			                     <div class="title">等级</div>
				              	 <div class="orderSelect orderMultSelect">
						                <div class="imgType" id="directorLevel">请选择等级</div>
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
						   
		         </div>
		         
		         <div class="MidItem controlWidth"   style="border:none;margin-top:20px">
		            <div class="newItemContent" id="addSetProductInfo" style="display:block !important"></div>
		         </div>

		         
		       
		    </div>
      </div>
    	
    	
</body>
<script type="text/javascript" src="${makeProductJs}"></script>
</html>