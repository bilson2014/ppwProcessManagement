<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- import CSS --%>
<spring:url value="/resources/css/activiti/storyBoard.css" var="storyBoardCss"/>
<spring:url value="/resources/lib/webuploader/webuploader.css"
	var="webuploaderCss" />
<%-- import JS --%>
<spring:url value="/resources/lib/jquery/jquery-2.0.3.min.js" var="jqueryJs"/>
<spring:url value="/resources/lib/jqueryui/jquery-ui.min.js" var="jqueryuiJs"/>
<spring:url value="/resources/js/common.js" var="commonJs"/>
<spring:url value="/resources/js/activiti/storyBoard.js" var="storyBoardJs"/>
<spring:url value="/resources/lib/webuploader/webuploader.js" var="webuploaderJs" />
<spring:url value="/resources/lib/jquery/ajaxfileupload.js" var="ajaxfileuploadJs"/>
<spring:url value="/resources/js/juicer.js" var="juicerJs" />
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
<script type="text/javascript" src="${jqueryuiJs}"></script>
<script type="text/javascript" src="${commonJs}"></script>
<script type="text/javascript" src="${webuploaderJs}"></script>
<script type="text/javascript" src="${ajaxfileuploadJs}"></script>
<script type="text/javascript" src="${juicerJs}"></script>

<link rel="stylesheet" href="${storyBoardCss}">
<link rel="stylesheet" href="${webuploaderCss}">
<link rel="shortcut icon" href="${imgPath }/favicon.ico" >

<!--[if lt IE 9]>
        <script>window.html5 || document.write('<script src="html5shivJs"><\/script>')</script>
    <![endif]-->

</head>

<body>
	
	<div class="page">
	       <div class="toolsHead">
	             <div>测试标题</div>
	             <div class="toolTitle">分镜工具</div>
	             <div class="openTool">打开</div>
	       </div>
	       <div class="setImg" id="setImg">
	       
	        <div class="imgItem">
	                    <div class="orderSelect" id="isOther">
				                <div class="imgType checkImgType">请选择镜头</div>
				                <img src="/resources/images/activiti/selectS.png">
				                <ul class="oSelect" id="orderCome" style="display: none;">
				                   <li data-id="0">全部</li>
				                   <li data-id="1">沟通阶段</li>
				                   <li data-id="2">方案阶段</li>
				                   <li data-id="3">商务阶段</li>
				                   <li data-id="4">制作阶段</li>
				                   <li data-id="5">交付阶段</li>
				                </ul>    
					     </div>
					     <div class="loadImg">
					            <div class="updateImg">重新上传</div>
					            <img class="delLoadImg" src="/resources/images/activiti/del.png">
					            <img class="backgroundImg" src="https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1527740495&di=f2c42a682f917b686de966d95944627d&src=http://img5.duitang.com/uploads/item/201411/04/20141104171337_xaMXx.jpeg">
					     </div>
					     <textarea class="checkImgText" placeholder="请输入镜头要求..."></textarea>
	             </div>
	       
	             <div class="imgItem">
	                    <div class="orderSelect" id="isOther">
				                <div class="imgType checkImgType">请选择镜头</div>
				                <img src="/resources/images/activiti/selectS.png">
				                <ul class="oSelect" id="orderCome" style="display: none;">
				                   <li data-id="0">全部</li>
				                   <li data-id="1">沟通阶段</li>
				                   <li data-id="2">方案阶段</li>
				                   <li data-id="3">商务阶段</li>
				                   <li data-id="4">制作阶段</li>
				                   <li data-id="5">交付阶段</li>
				                </ul>    
					     </div>
					     <div class="loadImg">
					            <div class="updateImg">重新上传</div>
					            <img class="delLoadImg" src="/resources/images/activiti/del.png">
					            <img class="backgroundImg" src="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528279592&di=fc4f3c06aa8ddd220b71df6f83582273&imgtype=jpg&er=1&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F15%2F59%2F15%2F54f58PICMis_1024.png">
					     </div>
					     <textarea class="checkImgText" placeholder="请输入镜头要求..."></textarea>
	             </div>
	             
	             <div class="imgItem">
	                    <div class="orderSelect" id="isOther">
				                <div class="imgType checkImgType">请选择镜头</div>
				                <img src="/resources/images/activiti/selectS.png">
				                <ul class="oSelect" id="orderCome" style="display: none;">
				                   <li data-id="0">全部</li>
				                   <li data-id="1">沟通阶段</li>
				                   <li data-id="2">方案阶段</li>
				                   <li data-id="3">商务阶段</li>
				                   <li data-id="4">制作阶段</li>
				                   <li data-id="5">交付阶段</li>
				                </ul>    
					     </div>
					     <div class="loadImg">
					            <div class="updateImg">重新上传</div>
					            <img class="delLoadImg" src="/resources/images/activiti/del.png">
					            <img class="backgroundImg" src="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1527507228707&di=5e7521e976e53da5ace3e221447a1a74&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01635d571ed29832f875a3994c7836.png%40900w_1l_2o_100sh.jpg">
					     </div>
					     <textarea class="checkImgText" placeholder="请输入镜头要求..."></textarea>
	             </div>
	             
	             <div class="imgItem">
	                    <div class="orderSelect" id="isOther">
				                <div class="imgType checkImgType">请选择镜头</div>
				                <img src="/resources/images/activiti/selectS.png">
				                <ul class="oSelect" id="orderCome" style="display: none;">
				                   <li data-id="0">全部</li>
				                   <li data-id="1">沟通阶段</li>
				                   <li data-id="2">方案阶段</li>
				                   <li data-id="3">商务阶段</li>
				                   <li data-id="4">制作阶段</li>
				                   <li data-id="5">交付阶段</li>
				                </ul>    
					     </div>
					     <div class="loadImg">
					            <div class="updateImg">重新上传</div>
					            <img class="delLoadImg" src="/resources/images/activiti/del.png">
					            <img class="backgroundImg" src="/resources/images/supplier/55.png">
					     </div>
					     <textarea class="checkImgText" placeholder="请输入镜头要求..."></textarea>
	             </div>
	             
	             <div class="imgItem">
	                    <div class="orderSelect" id="isOther">
				                <div class="imgType checkImgType">请选择镜头</div>
				                <img src="/resources/images/activiti/selectS.png">
				                <ul class="oSelect" id="orderCome" style="display: none;">
				                   <li data-id="0">全部</li>
				                   <li data-id="1">沟通阶段</li>
				                   <li data-id="2">方案阶段</li>
				                   <li data-id="3">商务阶段</li>
				                   <li data-id="4">制作阶段</li>
				                   <li data-id="5">交付阶段</li>
				                </ul>    
					     </div>
					     <div class="loadImg">
					            <div class="updateImg">重新上传</div>
					            <img class="delLoadImg" src="/resources/images/activiti/del.png">
					            <img class="backgroundImg" src="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1527834028519&di=fd505b7fd5af5167e6badbbf6752947e&imgtype=0&src=http%3A%2F%2Fimg1.bitautoimg.com%2FVideo%2F2013%2F10%2F27%2Fc923b604726f80c51.jpg">
					     </div>
					     <textarea class="checkImgText" placeholder="请输入镜头要求..."></textarea>
	             </div>
	             
	             
	             
	             
	             
             
	              <div class="addItem" id="picker">
	                    <img src="/resources/images/flow/addImg.png">
	                    <div>点击添加图片</div>
	                    <div>可上传JPG、GIF或PNG格式的文件，文件大小不能超过2M。</div>
	              </div>
	       </div>
	       
	       <div class="toolsHead">
	             <div class="sWord">补充视频信息</div>
	       </div>
	       
	       <div class="otherInfo">
	                 <div class="otherItem">
	                       <div class="otherTitle">分镜脚本名称</div>
	                       <input id="storyName">
	                 </div>
	                 <div class="otherItem">
	                       <div class="otherTitle">产品线</div>
	                        <div class="orderSelect selectLine" id="isOther">
				                <div class="imgType" id="productLine">请选择产品</div>
				                <img src="/resources/images/activiti/selectS.png">
				                <ul class="oSelect selectUl"  style="display: none;">
				                   <li data-id="0">全部</li>
				                   <li data-id="1">沟通阶段</li>
				                   <li data-id="2">方案阶段</li>
				                   <li data-id="3">商务阶段</li>
				                   <li data-id="4">制作阶段</li>
				                   <li data-id="5">交付阶段</li>
				                </ul>    
					     </div>
					      <div class="orderSelect" id="isOther">
				                <div class="imgType" id="productType">请选择视频类型</div>
				                <img src="/resources/images/activiti/selectS.png">
				                <ul class="oSelect" id="orderCome" style="display: none;">
				                   <li data-id="0">全部</li>
				                   <li data-id="1">沟通阶段</li>
				                   <li data-id="2">方案阶段</li>
				                   <li data-id="3">商务阶段</li>
				                   <li data-id="4">制作阶段</li>
				                   <li data-id="5">交付阶段</li>
				                </ul>    
					     </div>
	                 </div>
	                  <div class="otherItem onebox" id="time">
	                       <div class="otherTitle oneboxTilte">视频时长</div>
	                       <div class="killDiv diy">
		                       <div class="boxItem">
		                            <div class="box"></div>
		                            <div class="des">30秒</div>
		                       </div>
		                       <div class="boxItem">
		                            <div class="box"></div>
		                            <div class="des">30秒30秒30秒30秒30秒30秒</div>
		                       </div>
		                    </div>   
	                 </div>
	                  <div class="otherItem onebox" id="videoType">
	                       <div class="otherTitle oneboxTilte">画幅比例</div>
	                       <div class="killDiv diy">
		                       <div class="boxItem">
		                            <div class="box"></div>
		                            <div class="des">30秒</div>
		                       </div>
		                       <div class="boxItem">
		                            <div class="box"></div>
		                            <div class="des">30秒30秒30秒30秒30秒30秒</div>
		                       </div>
		                       <div class="boxItem">
		                            <div class="box"></div>
		                            <div class="des">30秒30秒30秒30秒30秒30秒</div>
		                       </div>
		                       <div class="boxItem">
		                            <div class="box"></div>
		                            <div class="des">30秒30秒30秒30秒30秒30秒</div>
		                       </div>
		                       <div class="boxItem">
		                            <div class="box"></div>
		                            <div class="des">30秒30秒30秒30秒30秒30秒</div>
		                       </div>
		                    </div>   
	                 </div>
	                 
	                 <div class="otherItem onebox a" id="videoStyle">
	                       <div class="otherTitle lastTitle">影片风格</div>
	                       <div class="killDiv">
		                         <div class="killItem">
		                               <img> 
				                       <div class="boxItem">
				                            <div class="box"></div>
				                            <div class="des">30秒</div>
				                       </div>
			                      </div> 
			                       <div class="killItem">
		                               <img> 
				                       <div class="boxItem">
				                            <div class="box"></div>
				                            <div class="des">30秒</div>
				                       </div>
			                      </div> 
			                       <div class="killItem">
		                               <img> 
				                       <div class="boxItem">
				                            <div class="box"></div>
				                            <div class="des">30秒</div>
				                       </div>
			                      </div> 
			                       <div class="killItem">
		                               <img> 
				                       <div class="boxItem">
				                            <div class="box"></div>
				                            <div class="des">30秒</div>
				                       </div>
			                      </div> 
			                       <div class="killItem">
		                               <img> 
				                       <div class="boxItem">
				                            <div class="box"></div>
				                            <div class="des">30秒</div>
				                       </div>
			                      </div> 
		                    </div>   
	                 </div>
	       </div>
	       
	       <div class="toolBtn">
	             <div class="saveProject" id="saveProject">保存至项目</div>
	             <div class="download">导出</div>
	       </div>
	       
	 
    </div>  
    
    
     

	<!-- 弹窗-->
	<div class="cusModel" id="loadProductModel">
	     <div class="modelCard">
	            <div class="cardTop">
	                   <div class="title">项目报价单</div>
	                   <div class="closeModel"></div>
	            </div>
	            <div class="modelBanner">
	                <div class="tap" id="" style="width:100%">您正在参与进行中的项目</div>
	            </div>
	            <div class="modelProductContent"> 
	            <div class="modelProItem" data-id="40" data-pid="20171207164928889 ">未命名项目</div>
	            <div class="modelProItem" data-id="40" data-pid="20171207164928889 ">未命名项目</div>
	            </div>
	            <div class="modelControl">
	                 <div class="btn-c-g" id="cancleLoadProduct">取消</div>
	                 <div class="btn-c-r" id="CheckloadProduct">加载</div>
	            </div>     
	     </div>
	</div>
	
	 <div class="cusModel" id="checkSureModel">
           <div class="successModel">
               <div class="closeBtn"></div>
			   <div class="oSContent">
			        <div class="tdDes">确认删除镜头吗?</div>
			        <div class="sureBtn">
			           <div class="btn-c-r" id="tModel">确定</div>
			           <div class="btn-c-g" id="cModel">取消</div>
			        </div>
			   </div>
           </div>
      </div>
	
	<div class="tooltip-success-show"></div>
</body>
<script type="text/javascript" src="${storyBoardJs}"></script>
</html>
